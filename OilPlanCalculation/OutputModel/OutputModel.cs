using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

namespace OilPlanCalculation
{
    public class OutputModel
    {
        public Good[] goods;
        public Resource[] resources;
        public Tool[] tools;
        public double goodsCostSumm;
        

        public OutputModel()
        {
        }

        public OutputModel( 
            double[] resNeed,
            double[] powNeed,
            double[] prodBuild,
            ForDB.DataManager db
        )
        {
            OutputModel output = this;

            /* Установки */
            output.tools = new Tool[db.tools.Count];
            for (int i = 0; i < output.tools.Length; ++i)
            {
                output.tools[i] = new Tool();

                output.tools[i].id = (Int64)db.tools[i]["Id"];
                output.tools[i].name = Convert.ToString(db.tools[i]["Name"]);
                output.tools[i].maxPower = Convert.ToDouble(db.tools[i]["Power"]);
                output.tools[i].recPower = powNeed[i];
                output.tools[i].revenue = 0;
                output.tools[i].abbr = (String)db.tools[i]["Abbreviation"];
            }

            /* Продукция */

            output.goods = new Good[db.goods.Count];
            output.goodsCostSumm = 0;
            for (int i = 0; i < output.goods.Length; ++i)
            {
                output.goods[i] = new Good();
                var good = output.goods[i];
                
                good.id = (Int64)(db.goods[i]["Id"]);
                good.name = (String)(db.goods[i]["Name"]);
                good.price = (double)(db.goods[i]["Price"]);
                good.volume = prodBuild[i];
                good.cost = good.volume * good.price;
                good.used = 0;
                good.created = 0;
                output.goodsCostSumm += good.cost;

                good.createdByTool = new Dictionary<Int64, double>();
                good.usedByTool = new Dictionary<Int64, double>();
                for (int tooli = 0; tooli < output.tools.Length; ++tooli)
                {

                    Tool tool = tools[tooli];

                    /* Получено добра с установки */
                    var tog =
                        db.Select(@"select 
                            ReceiveNumber
                        from 
                            GoodFromTool 
                        where 
                            ToolId = {0}
                            AND GoodId = {1}",
                            tool.id, good.id);
                    if (tog.Count > 0)
                    {
                        double outGood = tool.recPower * (double)tog[0]["ReceiveNumber"];

                        tool.revenue += good.price * outGood;
                        good.createdByTool[tooli] = outGood;
                        good.created += outGood;
                    }
                    else
                    {
                        good.createdByTool[tooli] = 0;
                    }

                    /* Потребность установки в продукции */
                    var tig =
                        db.Select(@"select 
                            RequestNumber 
                        from 
                            NeedGoodForTool 
                        where 
                            ToolId = {0}
                            AND GoodId = {1}",
                                tool.id, good.id);
                    if(tig.Count > 0)
                    {
                        double need = tool.recPower * (double)tig[0]["RequestNumber"];
                        good.usedByTool[tooli] = need;
                        good.used += need;
                    }
                    else
                    {
                        good.usedByTool[tooli] = 0;
                    }
                }
            }

            foreach (var t in tools) {t.relativeRevenue = t.revenue / goodsCostSumm;}

            /* 
             * Количество ресурсов:
             *  - запас - stock, 
             *  - требуемое к закупке - purchased,
             *  - оставшееся - notused 
             */

            output.resources = new Resource[db.resources.Count];
            for (int i = 0; i < db.resources.Count; ++i)
            {
                var r = db.resources[i];

                var stockRaw = db.Select("select * from ResourceNumber as rn where rn.ResourceID = {0}", r["Id"]);
                double stock = 0;
                if (stockRaw.Count > 1)
                    throw new Exception("Чтото не так с таблицей ResourceNumber");
                else if (stockRaw.Count == 1)
                    stock = (double)stockRaw[0]["TotalNumber"];

                double used = resNeed[i];

                double purchased = 0;
                if (used - stock > 9)
                    purchased = used - stock;

                output.resources[i] = new Resource();
                var res = output.resources[i];

                res.id = (Int64)r["Id"];
                res.name = (string)r["Name"];
                res.stock = stock;
                res.used = used;
                res.purchased = purchased;
                res.notused = stock + purchased - used;

                res.usedByTools = new Dictionary<long, double>();
                for (int tooli = 0; tooli < output.tools.Length; ++tooli)
                {

                    Tool tool = tools[tooli];

                    /* Потребность установки в ресурсах */
                    var tir = 
                        db.Select(@"select 
                                    RequestNumber 
                                from 
                                NeedResourceForTool
                                where 
                                ToolId = {0}
                                AND ResourceId = {1}",
                                tool.id, res.id);
                    if(tir.Count > 0)
                    {
                        Double need = tool.recPower * (double)tir[0]["RequestNumber"];

                        res.usedByTools[tooli] = need;
                    }
                    else
                    {
                        res.usedByTools[tooli] = 0;
                    }
                }
            }
        }
    }
}
