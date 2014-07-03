/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oilopt.math;
import oilopt.math.ProbalisticFunction;
import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.LinearMultivariateRealFunction;
import com.joptimizer.functions.QuadraticMultivariateRealFunction;
import oilopt.orm.*;
import java.sql.SQLException;
import java.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.NormalDistribution;
/**
 * Все условия задачи
 * @author r655
 */
public class ProblemConditions 
{
    
   
    private LinearMultivariateRealFunction[] toolsConditions = null;
    private LinearMultivariateRealFunction[] resourcesLinear = null;
    private ConvexMultivariateRealFunction[] resourcesConvex = null;
    private ProbalisticFunction[] resourcesProbalistic = null;
    private LinearMultivariateRealFunction objectiveFunction = null;
    private LinearMultivariateRealFunction[] goodsConditions = null;
    private LinearMultivariateRealFunction[] constConditions = null;
    
    private int dim;
    private ProblemData db;
    private NormalDistribution normalDistribution = null;
            
    /**
     * Условия не зависящие от alpha
     * @return 
     */
    public LinearMultivariateRealFunction[] getConstConditions()
    {
        if(constConditions == null)
        {
            constConditions = ArrayUtils.addAll(
                    getToolsConditions(),
                    getGoodsConditions()
            );
        }
        return constConditions;
    }
    
    /**
     * Размерность задачи
     * @return 
     */
    public int getDim()
    {
        return dim;
    }
    
    /**
     * t(alpha) (вычисляется по нормальному распределению)
     * @param alpha - вероятность
     * @return 
     */
    public double getT(double alpha)
    {
        if(normalDistribution == null)
            normalDistribution = new NormalDistribution(0.0, 1.0);
        double result = normalDistribution.inverseCumulativeProbability(alpha);
        return result;
        //return (new TDistribution(1)).cumulativeProbability(alpha);
    }
    
    public ProblemConditions(ProblemData data) throws SQLException
    {
        db = data;
        dim = db.tools.size();
    }

    public LinearMultivariateRealFunction[] getToolsConditions()
    {
        if(toolsConditions == null)
        {
            int mainSize = db.tools.size();

            toolsConditions 
                    = new LinearMultivariateRealFunction[2*db.tools.size()];

            //tools power <= maxpower
            for(Tool currentTool : db.tools)
            {
                int i = db.toolIndexById.get(currentTool.getId());

                double[] a = new double[mainSize];
                a[i] = 1.0;
                double[] b = new double[mainSize];
                b[i] = -1.0;
                toolsConditions[2*i] = new LinearMultivariateRealFunction(a, -currentTool.getPower()); 
                toolsConditions[2*i + 1] = new LinearMultivariateRealFunction(b, 0);
            }
        }
        
        return toolsConditions;
    }
    
    /**
     * Линейные ограничения для ресурсов
     * @param deviationCorrection - учёт дисперсии (нужно для предварительного решения)
     * @param alpha - вероятность (нужная только в случае deviationCorrection = true)
     * @return 
     */
    public LinearMultivariateRealFunction[] getResourcesLinearConditions(boolean deviationCorrection, double alpha)
    {
        resourcesLinear = null; // нужный костыль
        if(resourcesLinear == null)
        {
            int mainSize = db.tools.size();
            resourcesLinear 
                    = new LinearMultivariateRealFunction[db.resources.size()];
            double studentCoefficient = getT(alpha);
            //resources <= resourceNumber - correction
            for(Resource currentResource : db.resources)
            {
                int i = db.resourceIndexById.get(currentResource.getId());


                
                
                
                double r2 = -getResourceNumber()[i];
                
                double[] nrftsm;
                
                if(deviationCorrection)
                {
                    nrftsm = new double[dim];
                    
                    for (int k = 0; k < nrftsm.length; ++k)
                    {
                        nrftsm[k] = 
                                getNrftsm()[i][k] 
                                + getNrftsd()[i][k]*studentCoefficient;
                    }
                    
                    r2 += studentCoefficient
                        * getResourceNumberDeviation()[i];
                    
                }
                else
                {
                    nrftsm = getNrftsm()[i];
                }
                
                resourcesLinear[i] = new LinearMultivariateRealFunction(nrftsm, r2);
                
            }
        }
        
        return resourcesLinear;
    }
    
    
    
    public LinearMultivariateRealFunction[] getGoodsConditions()
    {
        if(goodsConditions == null)
        {
            int mainSize = db.tools.size();
            int rowSize = db.goods.size();

            LinearMultivariateRealFunction[] conditions 
                    = new LinearMultivariateRealFunction[rowSize];


            //condition for good
            for(Good good : db.goods)
            {
                int i = db.goodIndexById.get(good.getId());

                double[] coefficients = new double[mainSize];

                for(NeedGoodForTool ngft : good.getNeedGoodForTool())
                {
                    coefficients[db.toolIndexById.get(ngft.getToolId())] +=  ngft.getRequestNumber().doubleValue();
                }
                for(GoodFromTool gft : good.getGoodFromTool())
                {
                    coefficients[db.toolIndexById.get(gft.getToolId())] += - gft.getReceiveNumber().doubleValue();
                }


                conditions[i] = 
                        new LinearMultivariateRealFunction(coefficients, 0);
            }

            goodsConditions = conditions;
        }
        
        return goodsConditions;
    }
    
    
    
    /**
     * Возвращаются условия на ограниченность ресурсов с учётом
     * дисперсии потребления и числа ресурсов.
     * Используется неотлаженнный лкасс ProbalisticFunction
     * @param alpha
     * @return 
     */
    public ConvexMultivariateRealFunction[] getResourcesProbalisticConditions(double alpha)
    {
        resourcesProbalistic = null; // нужный костыль
        if(resourcesProbalistic == null)
        {
            double t_alpha = getT(alpha);

            ProbalisticFunction[] conditions = new ProbalisticFunction[db.resources.size()];

            for(Resource currentResource : db.resources)
            {
                int i = db.resourceIndexById.get(currentResource.getId());

                double[] nrftsm = getNrftsm()[i];
                double[] nrftsD = getNrftsd()[i];

                double resourceNumber = getResourceNumber()[i];
                double resourceNumberDeviation = getResourceNumberDeviation()[i];
                

                conditions[i] = 
                        new ProbalisticFunction(
                                nrftsm, 
                                resourceNumber, 
                                nrftsD, 
                                resourceNumberDeviation, 
                                t_alpha);
            }

            resourcesProbalistic = conditions;
        }
        
        return resourcesProbalistic;
    }
    
    /**
     * Возвращаются условия на ограниченность ресурсов с учётом
     * дисперсии потребления и числа ресурсов.
     * Используется страндартный класс JOptimizer QuadraticMultivariateRealFunction
     * @param alpha
     * @return 
     */
    public ConvexMultivariateRealFunction[] getResourcesConvexConditions(double alpha)
    {
        resourcesConvex = null; // нужный костыль
        if(resourcesConvex == null)
        {
            double talpha = getT(alpha);
            int mainSize = db.tools.size();
            int rowSize = db.resources.size();

            ConvexMultivariateRealFunction[] conditions = new ConvexMultivariateRealFunction[rowSize];

            for(Resource currentResource : db.resources)
            {
                int i = db.resourceIndexById.get(currentResource.getId());

                double[][] p = new double[mainSize][mainSize];
                double[] q = new double[mainSize];

                double[] nrftsm = getNrftsm()[i];
                double[] nrftsD = getNrftsd()[i];

                double resourceNumber = getResourceNumber()[i];
                double resourceNumberDeviation = getResourceNumberDeviation()[i];

                //0.5*XPX + QX + R <= 0
                //Fill p-matrix
                for (int j = 0; j < mainSize; ++j)
                {
                    for (int k = 0; k < mainSize; ++k)
                    {
                        p[j][k] = -nrftsm[j]*nrftsm[k];//-2*nrftsm[j]*nrftsm[k];
                        if(k == j)
                            p[j][j] += Math.pow(talpha*nrftsD[j], 2);
                        
                        p[j][k] *= 2;
                    }
                    
                }

                //Fill q-array
                for (int j = 0; j < mainSize; ++j)
                    q[j] = 2*resourceNumber*nrftsm[j];

                //r-value
                double r = 
                         Math.pow(talpha*resourceNumberDeviation,2) 
                         - Math.pow(resourceNumber, 2);

                conditions[i] 
                        = new QuadraticMultivariateRealFunction(p, q, r);
            }

            resourcesConvex = conditions;
        }
        
        return resourcesConvex;
    }
    
          
    /**
     * Возвращаются коэфициенты целевой функции
     * @return 
     */
    public LinearMultivariateRealFunction getObjectiveFunction()
    {
        if(objectiveFunction == null)
        {
            double[] goodPriceQVector = new double[db.tools.size()];
            for(Tool tool : db.tools)
            {
                goodPriceQVector[db.toolIndexById.get(tool.getId())] = 0;
            }

            //condition for good
            for(Good good : db.goods)
            {
                int i = db.goodIndexById.get(good.getId());


                //finder
                for(NeedGoodForTool ngft : good.getNeedGoodForTool())
                {
                    goodPriceQVector[db.toolIndexById.get(ngft.getToolId())] += 
                            ngft.getRequestNumber().doubleValue()
                            * good.getPrice().doubleValue();
                }
                for(GoodFromTool gft : good.getGoodFromTool())
                {
                    goodPriceQVector[db.toolIndexById.get(gft.getToolId())] += 
                            - gft.getReceiveNumber().doubleValue()
                            * good.getPrice().doubleValue();
                }
                
            }
            for(Resource resource : db.resources)
            {
                for(NeedResourceForTool nrft : resource.getNeedResourceForTool())
                {
                    goodPriceQVector[db.toolIndexById.get(nrft.getToolId())] += 
                             nrft.getRequestNumber()
                            * resource.getPrice();
                }
            }
            
            objectiveFunction
               = new LinearMultivariateRealFunction(goodPriceQVector, 0);
        }
        
        return objectiveFunction;
    }
    
    
    public double[][] getNrftsm()
    {
        return rd.getNrftsm();
    }

    public double[][] getNrftsd()
    {
        return rd.getNrftsd();
    }

    public double[] getResourceNumberDeviation()
    {
        return rd.getResourceNumberDeviation();
    }

    public double[] getResourceNumber()
    {
       return rd.getResourceNumber();
    }
    
    ResourceData rd = new ResourceData();
    
    private class ResourceData
    {
    
        private double[][] nrftsm = null;
        private double[][] nrftsD = null;
        private double[] resourceNumberDeviation = null;
        private double[] resourceNumber = null;

        
        public double[][] getNrftsm()
        {
            if(nrftsm == null)
                recalcResourceConstData();

            return nrftsm;
        }

        public double[][] getNrftsd()
        {
            if(nrftsD == null)
                recalcResourceConstData();

            return nrftsD;
        }

        public double[] getResourceNumberDeviation()
        {
            if(resourceNumberDeviation == null)
                recalcResourceConstData();

            return resourceNumberDeviation;
        }

        public double[] getResourceNumber()
        {
            if(resourceNumber == null)
                recalcResourceConstData();

            return resourceNumber;
        }
        
        private void recalcResourceConstData()
        {
            nrftsm = new double[db.resources.size()][];
            nrftsD = new double[db.resources.size()][];
            resourceNumberDeviation = new double[db.resources.size()];
            resourceNumber = new double[db.resources.size()];

            for(Resource currentResource : db.resources)
            {
                int i = db.resourceIndexById.get(currentResource.getId());

                resourceNumberDeviation[i] = currentResource.getDeviation();
                resourceNumber[i] = currentResource.getTotalNumber();

                nrftsm[i] = new double[getDim()];
                nrftsD[i] = new double[getDim()];
                for (NeedResourceForTool nrft : currentResource.getNeedResourceForTool())
                {
                    double count = nrft.getRequestNumber();
                    int index = db.toolIndexById.get(nrft.getToolId());

                    nrftsD[i][index] += nrft.getDeviation();
                    nrftsm[i][index] += count;
                }

            }
        }
    }
}
