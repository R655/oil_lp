using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace OilPlanCalculation
{
    public class Resource
    {
        public Int64 id;
        public String name;
        public double stock;
        public double used;
        public double notused;
        public double purchased;

        public Dictionary<Int64, double> usedByTools;
    }
}
