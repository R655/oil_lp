using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

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
        public Dictionary<Int64, double> createdByTool; // result tools array_index to volume
        public Dictionary<Int64, double> usedByTool; // result tools array_index to volume

        ForDB.DataManager db;

        Tool[] Tools
        {
            get
            {
                var toolsRows = db.q(
@"select * from {0} where {1} in (select {2} from {3} where {4} = {5})", 
                    new object[]{"Tool", "ToolId", "ToolId", "NeedGoodForTool", "GoodId", id});
                var tools = new Tool[toolsRows.Count];
                int i = 0;
                foreach (DataRow row in toolsRows)
                {
                    tools[i] = new Tool();
                    tools[i].id = (Int64)row["Id"];
                    //tools[i].id = (Int64)row["Id"];
                    //tools[i].id = (Int64)row["Id"];
                    //tools[i].id = (Int64)row["Id"];
                    ++i;
                }
                return tools;
                
            }
        }


    }
}
