using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Web.Razor;
using System.IO;
using Microsoft.CSharp;
using System.CodeDom.Compiler;
using System.Reflection;
using System.Diagnostics;
using System.CodeDom;

namespace OilPlanCalculation
{
    class Templater
    {
        RazorEngineHost host = null;
        RazorTemplateEngine templateEngine = null;
        TemplateBase template = null;

        public Templater()
        {
            RazorEngineHost host = new RazorEngineHost(new CSharpRazorCodeLanguage());

            host.DefaultBaseClass = typeof(TemplateBase).FullName;
            host.DefaultNamespace = "OilPlanCalculation";
            host.DefaultClassName = "Template";
            host.NamespaceImports.Add("System");
            host.NamespaceImports.Add("System.IO");

            templateEngine = new RazorTemplateEngine(host);
        }


        public void loadTemplate(string templateStrOrFile, bool isFile = true)
        {
            CodeCompileUnit parsedTemplate =
                (isFile)?
                    parsedTemplate = loadFile(templateStrOrFile):
                    parsedTemplate = loadString(templateStrOrFile);

            template = asseblyTemplate(parsedTemplate);
        }

        public string renderTemplate(OutputModel model)
        {
            string result;
            using (var writer = new StringWriter())
            {
                template.Output = writer;
                template.Model = model;
                template.Execute();

                File.WriteAllText("out.html", writer.ToString(), Encoding.UTF8);
                Process.Start("out.html");
                result = writer.ToString();    
            }
            return result;
            
        }

        private CodeCompileUnit loadFile(string templateFilePath)
        {
            CodeCompileUnit parsedTemplate = null;
            using (StreamReader reader = File.OpenText(templateFilePath))
            {
                parsedTemplate = parseTemplate(reader);
            }
            return parsedTemplate;
            
        }

        private CodeCompileUnit loadString(string str)
        {
            CodeCompileUnit parsedTemplate = null;
            using (StringReader streader = new StringReader(str))
            {
                parsedTemplate = parseTemplate(streader);
            }
            return parsedTemplate;
        }

        private TemplateBase asseblyTemplate(CodeCompileUnit parsedTemplate)
        {
            string outAssemblyName = compileTemplate(parsedTemplate);
            var assembly = Assembly.LoadFrom(outAssemblyName);
            var type = assembly.GetType("OilPlanCalculation.Template", true);
            var template = Activator.CreateInstance(type) as TemplateBase;
            return template;
        }




        private CodeCompileUnit parseTemplate(TextReader reader)
        {
            GeneratorResults generatorResults = null;
            generatorResults = templateEngine.GenerateCode(reader);

            if (!generatorResults.Success)
            {
                StringBuilder errorStr = new StringBuilder();
                foreach (var error in generatorResults.ParserErrors)
                {
                    errorStr.Append(
                        String.Format(
                        "Razor error: ({0}, {1}) {2}",
                        error.Location.LineIndex + 1,
                        error.Location.CharacterIndex + 1,
                        error.Message));
                }

                throw new Exception(errorStr.ToString());
            }

            return generatorResults.GeneratedCode;
        }

        private string compileTemplate(CodeCompileUnit generatedCode)
        {
            var codeProvider = new CSharpCodeProvider();

#if DEBUG
            using (var writer = new StreamWriter("out.cs", false, Encoding.UTF8))
            {
                codeProvider.GenerateCodeFromCompileUnit(
                    generatedCode, writer, new CodeGeneratorOptions());
            }
#endif

            var outDirectory = "temp";
            Directory.CreateDirectory(outDirectory);

            var outAssemblyName = Path.Combine(outDirectory,
                String.Format("{0}.dll", Guid.NewGuid().ToString("N")));

            var refAssemblyNames = new List<string>();
            refAssemblyNames.Add(new Uri(typeof(TemplateBase).Assembly.CodeBase).AbsolutePath);
            refAssemblyNames.Add("System.Core.dll");
            refAssemblyNames.Add("Microsoft.CSharp.dll");

            var compilerResults = codeProvider.CompileAssemblyFromDom(
                new CompilerParameters(refAssemblyNames.ToArray(), outAssemblyName),
                generatedCode);

            if (compilerResults.Errors.HasErrors)
            {
                var errors = compilerResults
                    .Errors
                    .OfType<CompilerError>()
                    .Where(x => !x.IsWarning);

                foreach (var error in errors)
                {
                    Console.WriteLine("Compiler error: ({0}, {1}) {2}",
                        error.Line, error.Column, error.ErrorText);
                }

                throw new ApplicationException();
            }

            return outAssemblyName;
        }
    }
}
