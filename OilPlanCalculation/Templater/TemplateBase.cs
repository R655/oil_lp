using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.ComponentModel;

namespace OilPlanCalculation
{
    public abstract class TemplateBase
    {
        public TextWriter Output { get; set; }

        public dynamic Model { get; set; }

        public abstract void Execute();

        // Формат наименьшей длины с 4мя значащими знаками
        public String FloatFormat = "{0:G4}";

        // Минимальное дробное число
        public double Zero = 1.0E-10; 
        

        public virtual void Write(object value)
        {
            if (
                value.GetType() == typeof(Double)
                || value.GetType() == typeof(float))
            {
                Double dvalue = (Double)value;
                if (Math.Abs(dvalue) < Zero)
                    dvalue = 0;
                string str = String.Format(FloatFormat, (Double)dvalue);
                this.Output.Write(str);
            }
            else
            {
                this.Output.Write(value);
            }
        }

        public virtual void WriteLiteral(object value)
        {
            this.Output.Write(value);
        }
    }
}
