/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oilopt.output;

/**
 * Форматирует числа с плавающей запятой в html шаблонах для вывода
 * @author r655
 */
public class TemplateValuesFormatter {
    
    int valPrecision;
    
    public TemplateValuesFormatter(int valPrecision)
    {
        this.valPrecision = valPrecision;
    }
    
    public TemplateValuesFormatter()
    {
        this.valPrecision = 5;
    }
    
    public String percent(double val)
    {
        return
        String.format("%.2f%%", val*100);
    }
    
    public String value(double val)
    {
        
        if(val == 0.0)
            return "0";
        else
            return String.format("%."+valPrecision+"g", val);
    }
    
    public String price(double val)
    {
        return
        String.format("%.2f", val);
    }
    
    public String rate(double val)
    {
        return
        String.format("%.2f", val);
    }
}
