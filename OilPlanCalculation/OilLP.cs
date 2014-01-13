using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;
using GlpkSharp;

namespace OilPlanCalculation
{
    class OilLP
    {
        private LPProblem problem;
        private ForDB.DataManager db;
        public OutputModel output;
        public double[] resNeed;
        public double[] powNeed;
        public double[] prodBuild;
        private int columnsCount;
        private int rowsCount;

        public OilLP(ForDB.DataManager data)
        {
            this.db = data;
           
        }

        public bool findSolution()
        {
            problem = new LPProblem();

            columnsCount = db.tools.Count;
            rowsCount = db.tools.Count + db.resources.Count + db.goods.Count;

            problem.AddCols(columnsCount);
            problem.AddRows(rowsCount);

            problem.ObjectiveDirection = OptimisationDirection.MAXIMISE;

            int[] ia = new int[columnsCount * rowsCount + 1];
            int[] ja = new int[columnsCount * rowsCount + 1];
            double[] coefficients = new double[columnsCount * rowsCount + 1];

            /* Ограничения на мощность установок */
            for (int i = 0; i < columnsCount; ++i)
            {
                for (int j = 0; j < columnsCount; ++j)
                {
                    ia[1 + i * columnsCount + j] = i + 1;
                    ja[1 + i * columnsCount + j] = j + 1;
                    coefficients[1 + i * columnsCount + j] = 0;
                }
                ia[1 + i * columnsCount + i] = i + 1;
                ja[1 + i * columnsCount + i] = i + 1;
                coefficients[1 + i * columnsCount + i] = 1;
            }

            /* Коэфициенты входных ресурсов для установок */
            int nrtcount = db.toolsInResources.Count;
            for (int i = columnsCount; i < rowsCount - db.goods.Count; ++i)
            {
                int realResourceId = (int)(Int64)(db.resources[i - columnsCount]["Id"]);

                for (int j = 0; j < columnsCount; ++j)
                {
                    int realToolId = (int)(Int64)(db.tools[j]["Id"]);

                    ia[1 + i * columnsCount + j] = i + 1;
                    ja[1 + i * columnsCount + j] = j + 1;

                    bool flag = false;
                    for (int k = 0; k < nrtcount; ++k)
                    {
                        int toolId = (int)(Int64)(db.toolsInResources[k]["ToolId"]);
                        int resourceId = (int)(Int64)(db.toolsInResources[k]["ResourceId"]);

                        if (toolId == realToolId && resourceId == realResourceId)
                        {
                            flag = true;
                            coefficients[1 + i * columnsCount + j] = (double)(db.toolsInResources[k]["RequestNumber"]);
                        }
                    }

                    if (!flag)
                        coefficients[1 + i * columnsCount + j] = 0;
                }
            }

            /* Коэфициенты входных и выходных продуктов для установок */
            int ngtcount = db.toolsInGoods.Count;
            int gftcount = db.toolsOutGoods.Count;
            for (int i = rowsCount - db.goods.Count; i < rowsCount; ++i)
            {
                int realGoodId = (int)(Int64)(db.goods[i - (rowsCount - db.goods.Count)]["Id"]);

                for (int j = 0; j < columnsCount; ++j)
                {
                    int realToolId = (int)(Int64)(db.tools[j]["Id"]);

                    ia[1 + i * columnsCount + j] = i + 1;
                    ja[1 + i * columnsCount + j] = j + 1;

                    double coefficient = 0;

                    for (int k = 0; k < ngtcount; ++k)
                    {
                        int toolId = (int)(Int64)(db.toolsInGoods[k]["ToolId"]);
                        int goodId = (int)(Int64)(db.toolsInGoods[k]["GoodId"]);

                        if (toolId == realToolId && goodId == realGoodId)
                        {
                            coefficient -= (double)(db.toolsInGoods[k]["RequestNumber"]);
                            break;
                        }
                    }

                    for (int k = 0; k < gftcount; ++k)
                    {
                        int toolId = (int)(Int64)(db.toolsOutGoods[k]["ToolId"]);
                        int goodId = (int)(Int64)(db.toolsOutGoods[k]["GoodId"]);

                        if (toolId == realToolId && goodId == realGoodId)
                        {
                            coefficient += (double)(db.toolsOutGoods[k]["ReceiveNumber"]);
                            break;
                        }
                    }
                    coefficients[1 + i * columnsCount + j] = coefficient;
                }
            }

            problem.LoadMatrix(ia, ja, coefficients);

            /* Знаки сравнения в неравенствах */

            /* Ограничения работы установок их мощностью */
            for (int i = 0; i < columnsCount; ++i)
            {
                double power = (double)(Int64)(db.tools[i]["Power"]);
                problem.SetRowBounds(i + 1, BOUNDSTYPE.Double, 0, power);
            }
            
            /* Ограничение используемых ресурсов их количеством в запасе */
            for (int i = columnsCount; i < rowsCount - db.goods.Count; ++i)
            {
                int realResourceId = (int)(Int64)(db.resources[i - columnsCount]["Id"]);
#if DEBUG
                string name = (String)(db.resources[i - columnsCount]["Name"]);
#endif
                double resourceTotalNumber = 0;
                for (int k = 0; k < db.resourcesNumber.Count; ++k)
                {
                    int resourceId = (int)(Int64)(db.resourcesNumber[k]["ResourceId"]);

                    if (resourceId == realResourceId)
                    {
                        resourceTotalNumber = (double)(db.resourcesNumber[k]["TotalNumber"]);
                        break;
                    }
                }
                if (resourceTotalNumber > 0)
                    problem.SetRowBounds(i + 1, BOUNDSTYPE.Double, 0, resourceTotalNumber); 
                else
                    problem.SetRowBounds(i + 1, BOUNDSTYPE.Fixed, 0, 0); 
                /*
                if(resourceTotalNumber > 0)
                    problem.SetRowBounds(i + 1, BOUNDSTYPE.Double, 0, resourceTotalNumber); 
                else
                    problem.SetRowBounds(i + 1, BOUNDSTYPE.Fixed, 0, 0);
                 */
            }

            /* Произведенная продукция больше нуля */
            for (int i = rowsCount - db.goods.Count; i < rowsCount; ++i)
            {
                problem.SetRowBounds(i + 1, BOUNDSTYPE.Lower, 0, 0); // > 0
            }

            /* Максимизируемая функция = количество произведенной продукции на её цену */
            for (int i = 0; i < columnsCount; ++i)
            {
                int realToolId = (int)(Int64)(db.tools[i]["Id"]);

                double minicoef = 0;
                for (int j = 0; j < db.goods.Count; ++j)
                {
                    int realGoodId = (int)(Int64)(db.goods[j]["Id"]);
                    double coefficient = 0;
                    for (int k = 0; k < ngtcount; ++k)
                    {
                        int toolId = (int)(Int64)(db.toolsInGoods[k]["ToolId"]);
                        int goodId = (int)(Int64)(db.toolsInGoods[k]["GoodId"]);

                        if (toolId == realToolId && goodId == realGoodId)
                        {
                            coefficient -= (double)(db.toolsInGoods[k]["RequestNumber"]);
                        }
                    }

                    for (int k = 0; k < gftcount; ++k)
                    {
                        int toolId = (int)(Int64)(db.toolsOutGoods[k]["ToolId"]);
                        int goodId = (int)(Int64)(db.toolsOutGoods[k]["GoodId"]);

                        if (toolId == realToolId && goodId == realGoodId)
                        {
                            coefficient += (double)(db.toolsOutGoods[k]["ReceiveNumber"]);
                        }
                    }

                    coefficient *= (double)(db.goods[j]["Price"]);
                    minicoef += coefficient;
                }
                problem.SetObjCoef(i + 1, minicoef);
            }

            /* Все переменные больше нуля */
            
            for (int i = 0; i < columnsCount; ++i)
            {
                problem.SetColBounds(i + 1, BOUNDSTYPE.Lower, 0, 0); // > 0
            }

            SOLVERSTATUS status = problem.SolveSimplex();
            if (status == SOLVERSTATUS.Solved)
            {
                
                int i = 1;
                powNeed = new double[db.tools.Count];
                for (int powInd = 0; powInd < db.tools.Count; ++i, ++powInd)
                {
                    powNeed[powInd] = problem.GetRowPrimal(i);
                }

                resNeed = new double[db.resources.Count];
                for (int resInd = 0; resInd < db.resources.Count; ++i, ++resInd)
                {
                    resNeed[resInd] = problem.GetRowPrimal(i);
                }

                prodBuild = new double[db.goods.Count];
                for (int prodInd = 0; prodInd < db.goods.Count; ++i, ++prodInd)
                {
                    prodBuild[prodInd] = problem.GetRowPrimal(i);
                }

                output = new OutputModel(resNeed, powNeed, prodBuild, db);
                return true;
                
            }
            else
            {
                return false;
            }
        }


    }
}
