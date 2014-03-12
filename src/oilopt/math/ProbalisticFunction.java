/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oilopt.math;
 import com.joptimizer.functions.ConvexMultivariateRealFunction;
/**
 * Условия представленные в виде Aj*X + Wj <= b
 * где Wj = t * sqrt(summ(aij*aij*xDij*xDij) + bD*bD)
 * @author r655
 */
public class ProbalisticFunction implements ConvexMultivariateRealFunction
{
    int dim;
    double[] xD;
    double bD;
    double t;
    double b;
    double[] a;
    /**
     * Evaluation of the function at point X.
     */
    
    public ProbalisticFunction(
            double[] a,
            double b,
            double[] xD,
            double bD,
            double t
        )
    {
        this.dim = a.length;
        this.a = a;
        this.b = b;
        this.xD = xD;
        this.bD = bD;
        this.t = t;
    }
    
    public double value(double[] x)
    {
        double linear = 0;
        double sqrt = 0;
        for(int i = 0; i < dim; ++i)
        {
            sqrt += sqr(xD[i] * x[i]);
            linear += a[i] * x[i];
        }
        sqrt += sqr(bD);
        linear -= b;
        return Math.sqrt(sqrt)+linear;
    }
    
    private double sqr(double value)
    {
        return value*value;
    }
    
    private double sqrtPart(double[] x)
    {
        double sqrt = 0;
        for(int i = 0; i < dim; ++i)
        {
            sqrt += sqr(xD[i] * x[i]);
        }
        sqrt += sqr(bD);
        return Math.sqrt(sqrt);
    }
    
    /**
     * Function gradient at point X.
     */
    public double[] gradient(double[] x)
    {
        double[] result = new double[dim];
        for(int i = 0; i < dim; ++i)
        {
            result[i] = t*x[i]/Math.sqrt(sqrtPart(x))+a[i];
        }
        return result;
    }

    /**
     * Function hessian at point X.
     */
    public double[][] hessian(double[] x)
    {
        double[][] result = new double[dim][dim];
        for(int i = 0; i < dim; ++i)
        {
            for(int j = 0; j < dim; ++j)
            {
                if(i == j)
                {
                    double value = 1.0/Math.sqrt(sqrtPart(x));
                    value -= x[i]*x[i]/Math.pow(sqrtPart(x), 1.5);
                    value *= t;
                    result[i][j] = value;
                }
                else
                {
                    result[i][j] = -t*x[i]*x[j]/Math.pow(sqrtPart(x), 1.5);
                }
            }
        }
        return result;
    }

    /**
     * Dimension of the function argument.
     */
    public int getDim()
    {
        return dim;
    }
}
