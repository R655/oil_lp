using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace OilPlanCalculation
{
    public class Good
    {
        public Int64 id;
        public String name;
        public double price;
        public double volume;
        public double cost;
        public double used;
        public double created;
        public Dictionary<Int64, double> createdByTool;
        public Dictionary<Int64, double> usedByTool;
    }
}
