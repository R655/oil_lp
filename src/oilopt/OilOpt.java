/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oilopt;

import oilopt.output.TemplateValuesFormatter;
import oilopt.math.ProblemSolver;
import oilopt.orm.*;
import java.io.*;
import java.util.*;
import java.sql.SQLException;
import oilopt.output.OutputModel;
import org.apache.velocity.*;
import org.apache.velocity.app.*;


/**
 *
 * @author R65
 */
public class OilOpt {

    static String usage = 
            "Команда используется в виде:\n"
            + "oilopt -c [a1 a2 ...]\n"
            + "где ai имеет формат: \n"
            + " - либо X%, 0 < X < 100\n"
            + " - либо X, 0 < X < 1\n"
            + "Если ai не задано, то используются вероятности:\n"
            + "0.6, 0.7, 0.8, 0.9\n"
            + "Во всех случаях вычисляетcя детерминированная задача";
    
    
    static ProblemSolver solver;
    
    public static void main(String[] args) 
            throws SQLException, ClassNotFoundException, Exception 
    {
        String dbName = "resources/data.sqlite";
        String qName = "resources/data.sql";
        
        double[] alphas;
        if(args.length == 0)
        {
            /**
             * Запилить запуск с гуями
             */
           
            oilopt.gui.MainWindow window = new oilopt.gui.MainWindow();
            window.setVisible(true);

            solver = new ProblemSolver(dbName, qName);
        }
        else
        {
            alphas = new double[args.length];
            for(int i = 0; i < args.length; ++i)
            {
                String arg = args[i];
                double val;
                boolean percent = false;
                String lol = arg;
                if(arg.charAt(arg.length()-1) == '%')
                {
                    percent = true;
                    lol = arg.substring(0, arg.length()-1);
                }
                    
                try{
                    val = Double.parseDouble(lol);
                    
                }
                catch(NumberFormatException e)
                {
                    System.out.print(usage);
                    return;
                }
                
                if(percent)
                    val /= 100;
                
                alphas[i] = val;
                
            }
            
            solver = new ProblemSolver(dbName, qName);
            
            generateOutput(alphas);
        }
        
        
    }
    
    public static String[] generateOutput(double[] alphas)
            throws IOException, Exception
    {
        
        
        OutputModel[] oms = new OutputModel[1 + alphas.length];
        oms[0] = solver.solve();
        
        String[] outputFiles;
        templater("resources/by_alpha_tpl.html", oms[0], "outdet.html");
        
        if(alphas.length > 0)
        {
            outputFiles = new String[alphas.length + 2];
                        
            int i = 1;
            for(; i < oms.length; ++i)
            {
                oms[i] = solver.solve(alphas[i - 1]);
                String fileName = "out"
                        + i
                        + ".html";
                outputFiles[i] = fileName;
                
                templater("resources/by_alpha_tpl.html", oms[i], 
                        fileName);
            }

            templater("resources/general_tpl.html", oms, "general.html");
            outputFiles[i] = "general.html";
            
        }
        else
        {
            outputFiles = new String[1];
        }
        outputFiles[0] = "outdet.html";
        
        return outputFiles;
    }
    
    
    private static void templater(String templateFile, Object modl, String outFile) throws IOException
    {
        java.util.Properties props = new java.util.Properties();
        //props.setProperty("resource.loader", "class");
        //props.setProperty("resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty("input.encoding", "UTF-8");
        props.setProperty("output.encoding", "UTF-8");
        VelocityEngine engine = new VelocityEngine(props);
        VelocityContext context = new VelocityContext();
        
        context.put("model", modl);
        context.put("f", new TemplateValuesFormatter());
        
        File file = new File(outFile);
        
        if (!file.exists())
            file.createNewFile();
        
        Template template = engine.getTemplate(templateFile, "UTF-8");
        
        try (BufferedWriter writer = new BufferedWriter(
                     new FileWriter(file.getAbsoluteFile()))) 
        {
            if ( template != null)
                template.merge(context, writer);

            writer.flush();
        }
    }
    
}
