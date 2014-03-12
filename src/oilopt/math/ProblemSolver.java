package oilopt.math;

import oilopt.math.ProblemData;
import oilopt.math.ProblemConditions;
import com.joptimizer.functions.*;
import com.joptimizer.optimizers.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import oilopt.dbutils.DataManager;
import oilopt.output.OutputModel;
import org.apache.commons.lang.ArrayUtils;

/**
 * Решение задачи
 * @author r655
 */
public class ProblemSolver
{
    
    private ProblemConditions conditions = null;
    private ProblemData problemData = null;
    // private DataManager deprecatedOilData = null;
    
    public ProblemSolver(String dbName, String qName) throws SQLException, ClassNotFoundException, FileNotFoundException, IOException
    {
        
        
        // deprecatedOilData = new DataManager(dbName, qName);
        // deprecatedOilData.initOilLpData(); deprecated
        if(!(new File(dbName)).exists())
            DataManager.onlyInitDB(dbName, qName);
        
        problemData = new ProblemData(dbName);
        conditions = new ProblemConditions(problemData);
    }
    
    /**
     * Решает линейную задачу (без учёта дисперсий)
     * @return
     * @throws Exception 
     */
    public OutputModel solve()
            throws Exception
            
    {
        double[] initialPoint = presolve(false, 0.9);
        OutputModel model = new OutputModel(initialPoint, conditions, problemData);
        return model;
    }
    
    /**
     * Решение пробалистической задачи
     * @param alpha
     * @return
     * @throws Exception 
     */
    public OutputModel solve(double alpha)
            throws Exception
    {
        double up = 0.995;
        double down = 0.005;
        
        if(alpha <= down || up <= alpha )
        {
            throw new IllegalArgumentException(String.format(
            "Указана недопустимая вероятность. alpha E [%f, %f]", down, up));
        }
        
        return _solve(true, alpha);
        
    }
    
    private OutputModel _solve(boolean probalistic, double alpha)
            throws Exception            
    {
        //Debug flags
        boolean needPresolveCorrection = true;
        boolean ownConvexClass = false;
        boolean needHalfPresolve = true;
        boolean KKKT = false;
        
        //Conditions
        int mainSize = conditions.getDim();
  
        
        ConvexMultivariateRealFunction[] goodsConds = conditions.getGoodsConditions();
        ConvexMultivariateRealFunction[] toolsConds = conditions.getToolsConditions();
        ConvexMultivariateRealFunction[] resourcesLinConds = 
                conditions.getResourcesLinearConditions(false, alpha);
        ConvexMultivariateRealFunction[] resourcesConds =  ((ownConvexClass)?
                            conditions.getResourcesProbalisticConditions(alpha):
                            conditions.getResourcesConvexConditions(alpha));
        
        int condsLen = goodsConds.length
                        + toolsConds.length
                        + resourcesLinConds.length;
            
        
        ConvexMultivariateRealFunction[] problem;
        int prIndex = 0;
        OptimizationRequest or = new OptimizationRequest();
        
        if(probalistic)
        {
            condsLen += resourcesConds.length;
            problem = new ConvexMultivariateRealFunction[condsLen];
            for(int k = 0; k < resourcesConds.length; ++k)
                problem[prIndex++] = resourcesConds[k];
            
            double[] initialPoint = presolve(needPresolveCorrection, alpha);

            if(needHalfPresolve)
            {
                for(int i = 0; i < mainSize; ++i)
                    initialPoint[i] *= 0.5;
            }
            or.setInitialPoint(initialPoint);
        }
        else
        {
            problem = new ConvexMultivariateRealFunction[condsLen];
        }
        
        for(int k = 0; k < goodsConds.length; ++k)
                problem[prIndex++] = goodsConds[k];
        
        for(int k = 0; k < toolsConds.length; ++k)
                problem[prIndex++] = toolsConds[k];
        
        for(int k = 0; k < resourcesLinConds.length; ++k)
                problem[prIndex++] = resourcesLinConds[k];
        
        
        
        ConvexMultivariateRealFunction objectiveFunction
               = conditions.getObjectiveFunction();
        
        //optimization problem
        
        
        or.setF0(objectiveFunction);
        or.setFi(problem);
        
        if(KKKT)
            or.setCheckKKTSolutionAccuracy(true);

        JOptimizer opt = new JOptimizer();
        opt.setOptimizationRequest(or);
        int returnCode = opt.optimize();    
        double[] sol = opt.getOptimizationResponse().getSolution();
        
        double summ = objectiveFunction.value(sol);
        
        return new OutputModel(sol, conditions, problemData, alpha);
        
    }
    
    
    
    
    
    /**
     * Предварительное решение линейной задачи
     * используется для нахождения начальной удовлетворяющей условиям точки
     * @param alpha
     * @return
     * @throws Exception 
     */
    double[] presolve(boolean deviationCorrection, double alpha)
            throws Exception
    {
        LinearMultivariateRealFunction objectiveFunction 
                = conditions.getObjectiveFunction();
        
        LinearMultivariateRealFunction[] goodsConds = conditions.getGoodsConditions();
        LinearMultivariateRealFunction[] toolsConds = conditions.getToolsConditions();
        LinearMultivariateRealFunction[] resourcesConds = 
                            conditions.getResourcesLinearConditions(deviationCorrection, alpha);
                            
        
        
        LinearMultivariateRealFunction[] problem = 
                (LinearMultivariateRealFunction[])
                ArrayUtils.addAll(
                    resourcesConds,
                    ArrayUtils.addAll(goodsConds, toolsConds));
                
        
        //optimization problem
        OptimizationRequest or = new OptimizationRequest();
        or.setF0(objectiveFunction);
        or.setFi(problem);
        
        // Параметры из примеров, но почемуто оптимизация с ними не работает
        //or.setCheckKKTSolutionAccuracy(true);
        //or.setToleranceFeas(1.E-9);
        //or.setTolerance(1.E-9);

        //optimization
        JOptimizer opt = new JOptimizer();
        opt.setOptimizationRequest(or);
        
        int returnCode = opt.optimize();    
        
        double[] sol = opt.getOptimizationResponse().getSolution();
        
        // debug data
        //sol[2] = 0;
        //sol[4] = 0;
        double summ = objectiveFunction.value(sol);
        double[] goodsVolume = new double[goodsConds.length];
        double[] resRequire = new double[resourcesConds.length];
        for(int i = 0; i < goodsVolume.length; ++i)
            goodsVolume[i] = goodsConds[i].value(sol);
        for(int i = 0; i < resourcesConds.length; ++i)
            resRequire[i] = resourcesConds[i].value(sol);
        
        return sol;
    }
   

   
}

