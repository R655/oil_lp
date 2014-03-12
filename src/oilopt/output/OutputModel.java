package oilopt.output;
import oilopt.dbutils.DataManager;
import oilopt.math.ProblemData;
import oilopt.math.ProblemConditions;
import com.joptimizer.functions.ConvexMultivariateRealFunction;
import oilopt.*;
import java.util.*;


/**
 * Данные, необходимые для вывода решения одной задачи
 * @author r655
 */
public class OutputModel
{
    private Good[] goods;
    private Resource[] resources;
    private Tool[] tools;
    private double goodsCostSumm;
    private double resourcesCostSumm;
    private double profit;
    private double alpha;
    private boolean showAlpha;
    private double oilRefinery;
    private ProblemData db;

    
    /**
     * Вывод для решения детерминированной задачи
     * @param sol
     * @param conds
     * @param db 
     */
    public OutputModel(double[] sol, 
                        ProblemConditions conds,
                        ProblemData db)
    {
        
        this.db = db;
        alpha = -1.0;
        showAlpha = false;
 
        
        _convertResult(false, sol, conds, 0.0);
    }
    
    /**
     * Вывод для решения стохастической задачи
     * @param sol
     * @param conds
     * @param db
     * @param alpha 
     */
    public OutputModel(double[] sol, 
                        ProblemConditions conds,
                        ProblemData db, 
                        double alpha)
    {
        this.db = db;
        this.alpha = alpha;
        this.showAlpha = true;
       
        
        _convertResult(true, sol, conds, alpha);
    }
    
    private void _convertResult(boolean probalistic, double[] toolsPow, ProblemConditions conditions, double alpha)
    {
        
        
        int mainSize = conditions.getDim();
        
        ConvexMultivariateRealFunction[] goodsConds 
                = conditions.getGoodsConditions();
        
        ConvexMultivariateRealFunction[] resourcesLinConds 
                = conditions.getResourcesLinearConditions(false, 0.9);
        
        double[] goodsVolume = new double[goodsConds.length];
        double[] resNeed = new double[resourcesLinConds.length];
        double[] nullvector = new double[mainSize];
        
        for(int i = 0; i < goodsVolume.length; ++i)
            goodsVolume[i] = -goodsConds[i].value(toolsPow);
        
        if(probalistic)
        {
            double[] gamma = new double[resourcesLinConds.length];
            
             ConvexMultivariateRealFunction[] resourcesConds 
                = conditions.getResourcesProbalisticConditions(alpha);
             
             /**
              * getResourceConvexConditions не подходит, 
              * т.к. значения возведены в квадрат
              **/
            
        
            for(int i = 0; i < resourcesLinConds.length; ++i)
            {
                
                
                /**
                 * Ai*X + Wi(X) ==
                 *
                 *  (Ai*X + Wi(X) - Ri) - (Ai*[0] - Ri) 
                 **/
                 
                double resNeedAlt = resourcesConds[i].value(toolsPow) 
                             - resourcesLinConds[i].value(nullvector);
                
                double resNeedLin = resourcesLinConds[i].value(toolsPow) 
                             - resourcesLinConds[i].value(nullvector);
                
                /**
                 * Wi(X) ==
                 *
                 *  (Ai*X + Wi(X) - Ri) - (Ai*X - Ri) 
                 **/
                
                
                double wi = resourcesConds[i].value(toolsPow) 
                             - resourcesLinConds[i].value(toolsPow);
                
                double wiAlt = gamma[i] = wi/resNeed[i];                
                
                /**
                 * gamma[i] ==
                 *  
                 *   Wi(X)/(Ai*X + Wi(X))
                 **/
                gamma[i] = wi/resNeedAlt;
                
                resNeed[i] = resNeedLin;

            }
            
             fillData( 
                resNeed,
                toolsPow,
                goodsVolume,
                gamma
            );
             
        }   
        else
        {
            
            for(int i = 0; i < resourcesLinConds.length; ++i)
            {
                /**
                 * Ai*X  ==
                 *
                 *  (Ai*X  - Ri) - (Ai*[0] - Ri) 
                 **/
                 
                resNeed[i] = resourcesLinConds[i].value(toolsPow) 
                             - resourcesLinConds[i].value(nullvector);
                
            }
            
            fillData( 
                resNeed,
                toolsPow,
                goodsVolume,
                null
            );
        }
    }
    
    
    private void fillData( 
        double[] resNeed,
        double[] powNeed,
        double[] prodBuild,
        double[] gamma

    )
            
    {
        OutputModel output = this;
        
      
        /* Установки */
        int toolsLen = db.tools.size();
        tools = new Tool[toolsLen];

        for (oilopt.orm.Tool tool: db.tools)
        {
            int i = db.getToolIndex(tool.getId());
            output.tools[i] = new Tool();
            output.tools[i].id = tool.getId();
            output.tools[i].name = tool.getName();
            output.tools[i].maxPower = ((double)tool.getPower());
            output.tools[i].recPower = powNeed[i];
            output.tools[i].revenue = 0.0;
            output.tools[i].abbr = tool.getAbbreviation();
            
        }

        /* Продукция */

        output.goods = new Good[db.goods.size()];
        output.goodsCostSumm = 0;
        
        for (oilopt.orm.Good dbgood: db.goods)
        {
            int i = db.getGoodIndex(dbgood.getId());
            output.goods[i] = new Good();
            Good good = output.goods[i];

            good.setId(dbgood.getId());
            good.setName(dbgood.getName());
            good.setPrice(dbgood.getPrice().doubleValue());
            good.setVolume(prodBuild[i]);
            good.setCost(good.getVolume() * good.getPrice());
            good.setUsed(0);
            good.setCreated(0);
            output.goodsCostSumm += good.getCost();
            

            good.setCreatedByTool(new HashMap<Integer, Double>());
            good.setUsedByTool(new HashMap<Integer, Double>());
            
            
            for (oilopt.orm.Tool tool : db.tools)
            {
                /* Получено добра с установки */
                good.getCreatedByTool().put(tool.getId(), 0.0);
                
                 /* Потребность установки в продукции */
                good.getUsedByTool().put(tool.getId(), 0.0);
            }
            
            
            
            for(oilopt.orm.GoodFromTool entry : dbgood.getGoodFromTool())
            {
                int tid = entry.getToolId();
                int tind = db.getToolIndex(tid);
                double recpower = tools[tind].recPower;
                double outGood = 
                        recpower
                        * entry.getReceiveNumber().doubleValue();

                tools[db.getToolIndex(tid)].revenue += good.getPrice() * outGood;
                good.getCreatedByTool().put(tid, outGood);
                good.setCreated(good.getCreated() + outGood);
            }

           

            for(oilopt.orm.NeedGoodForTool entry : dbgood.getNeedGoodForTool())
            {
                int tid = entry.getToolId();
                int tind = db.getToolIndex(tid);
                double recpower = tools[tind].recPower;
                double need = 
                        recpower
                        * entry.getRequestNumber().doubleValue();
                good.getUsedByTool().put(tid, need);
                good.setUsed(good.getUsed() + need);
            }
        }

        for (Tool t : tools) {t.relativeRevenue = t.revenue / goodsCostSumm;}

        /* 
         * Количество ресурсов:
         *  - ограничение - limit, 
         *  - требуемое - used,
         *  - оставшееся - notused 
         */
        output.resourcesCostSumm = 0;
        output.resources = new Resource[db.resources.size()];
        for(oilopt.orm.Resource r : db.resources)
        {
            int i = db.getResourceIndex(r.getId());

            double limit = r.getNumber();
            double used = resNeed[i];

            double purchased = 0;
            if (used - limit > 0.0)
                purchased = used - limit;
            
            output.resourcesCostSumm += used*r.getPrice();
            
            if(r.getName().contains("Нефть"))
                output.oilRefinery = used;

            output.resources[i] = new Resource();
            Resource res = output.resources[i];

            res.id = (int)r.getId();
            res.name = (String)r.getName();
            res.price = r.getPrice();
            res.stock = limit;
            res.used = used;
            res.purchased = purchased;
            res.notused = limit + purchased - used;
            res.gamma = (gamma == null)? 0.0 : gamma[i]; 

            res.usedByTools = new HashMap<>();
            
            /* Потребность установки в ресурсах */
            
            // Инициализация
            for(oilopt.orm.Tool tool: db.tools)
            {
                res.usedByTools.put(tool.getId(), 0.0);
            }
            
            for(oilopt.orm.NeedResourceForTool tire: r.getNeedResourceForTool())
            {
                int tid = tire.getToolId();
                int tind = db.getToolIndex(tid);
                double recpower = tools[tind].recPower;
                Double need = recpower * (double)tire.getRequestNumber();
                res.usedByTools.put(tid, need);
            }
        }
        
        output.profit = output.goodsCostSumm - output.getResourcesCostSumm();
    }
    
    @Deprecated
    public OutputModel( 
        double[] resNeed,
        double[] powNeed,
        double[] prodBuild,
        DataManager db
    ) throws Exception
    {
        OutputModel output = this;

        /* Установки */
        int toolsLen = db.tools.size();
        output.tools = new Tool[toolsLen];
        int i = 0;
        for (Map<String, Object> tool: db.tools)
        {
            output.tools[i] = new Tool();

            output.tools[i].id = (int)tool.get("Id");
            output.tools[i].name = (String)tool.get("Name");
            output.tools[i].maxPower = ((Integer)tool.get("Power")).doubleValue();
            output.tools[i].recPower = powNeed[i];
            output.tools[i].revenue = 0.0;
            output.tools[i].abbr = (String)tool.get("Abbreviation");
            
            ++i;
        }

        /* Продукция */

        output.goods = new Good[db.goods.size()];
        output.goodsCostSumm = 0;
        output.oilRefinery = 0;
        i = 0;
        for (Map<String, Object> dbgood: db.goods)
        {
            output.goods[i] = new Good();
            Good good = output.goods[i];

            good.setId((int)(dbgood.get("Id")));
            good.setName((String)(dbgood.get("Name")));
            good.setPrice((double)(dbgood.get("Price")));
            good.setVolume(prodBuild[i]);
            good.setCost(good.getVolume() * good.getPrice());
            good.setUsed(0);
            good.setCreated(0);
            output.goodsCostSumm += good.getCost();
            output.oilRefinery += prodBuild[i];

            good.setCreatedByTool(new HashMap<Integer, Double>());
            good.setUsedByTool(new HashMap<Integer, Double>());
            
            
            for (Tool tool : tools)
            {
                /* Получено добра с установки */
                Collection<Map<String, Object>> tog =
                    db.q("select "
                        + "ReceiveNumber "
                    + "from "
                      +  "GoodFromTool "
                    + "where "
                      +  "ToolId = %d "
                       + "AND GoodId = %d ",
                         tool.id, good.getId());
                good.getCreatedByTool().put(tool.id, 0.0);
                
                for(Map<String, Object> entry : tog)
                {
                    double outGood = tool.recPower * (double)entry.get("ReceiveNumber");

                    tool.revenue += good.getPrice() * outGood;
                    good.getCreatedByTool().put(tool.id, outGood);
                    good.setCreated(good.getCreated() + outGood);
                }

                /* Потребность установки в продукции */
                Collection<Map<String, Object>> tig =
                    db.q("select "
                        +"RequestNumber "
                    +"from "
                        +"NeedGoodForTool "
                    +"where "
                        +"ToolId = %d "
                        +"AND GoodId = %d",
                            tool.id, good.getId());
                
                good.getUsedByTool().put(tool.id, 0.0);
                
                for(Map<String, Object> entry : tig)
                {
                    double need = tool.recPower * (double)entry.get("RequestNumber");
                    good.getUsedByTool().put(tool.id, need);
                    good.setUsed(good.getUsed() + need);
                }
            }
            
            ++i;
        }

        for (Tool t : tools) {t.relativeRevenue = t.revenue / goodsCostSumm;}

        /* 
         * Количество ресурсов:
         *  - запас - stock, 
         *  - требуемое к закупке - purchased,
         *  - оставшееся - notused 
         */
        i = 0;
        output.resources = new Resource[db.resources.size()];
        for(Map<String, Object> r : db.resources)
        {

            Collection<Map<String, Object>> stockRaw = db.q("select * from ResourceNumber as rn where rn.ResourceID = %d", r.get("Id"));
            double stock = 0;
            if (stockRaw.size() > 1)
                throw new Exception("Чтото не так с таблицей ResourceNumber");
            else if (stockRaw.size() == 1)
                stock = (double)stockRaw.iterator().next().get("TotalNumber");

            double used = resNeed[i];

            double purchased = 0;
            if (used - stock > 9)
                purchased = used - stock;

            output.resources[i] = new Resource();
            Resource res = output.resources[i];

            res.id = (int)r.get("Id");
            res.name = (String)r.get("Name");
            res.stock = stock;
            res.used = used;
            res.purchased = purchased;
            res.notused = stock + purchased - used;

            res.usedByTools = new HashMap<>();
            for(Tool tool: tools)
            {
                /* Потребность установки в ресурсах */
                Collection<Map<String,Object>> tir = 
                    db.q("select "
                                +"RequestNumber "
                            +"from "
                            +"NeedResourceForTool "
                            +"where "
                            +"ToolId = %d "
                            +"AND ResourceId = %d",
                            tool.id, res.id);
                
                res.usedByTools.put(tool.id, 0.0);
                for(Map<String, Object> tire: tir)
                {
                    Double need = tool.recPower * (double)tire.get("RequestNumber");

                    res.usedByTools.put(tool.id, need);
                }
            }
            ++i;
        }
    }

    /**
     * @return the goods
     */
    public Good[] getGoods() {
        return goods;
    }

    /**
     * @param goods the goods to set
     */
    public void setGoods(Good[] goods) {
        this.goods = goods;
    }

    /**
     * @return the resources
     */
    public Resource[] getResources() {
        return resources;
    }

    /**
     * @param resources the resources to set
     */
    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    /**
     * @return the tools
     */
    public Tool[] getTools() {
        return tools;
    }

    /**
     * @param tools the tools to set
     */
    public void setTools(Tool[] tools) {
        this.tools = tools;
    }

    /**
     * @return the goodsCostSumm
     */
    public double getGoodsCostSumm() {
        return goodsCostSumm;
    }

    /**
     * @param goodsCostSumm the goodsCostSumm to set
     */
    public void setGoodsCostSumm(double goodsCostSumm) {
        this.goodsCostSumm = goodsCostSumm;
    }

    /**
     * @return the alpha
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * @return the showAlpha
     */
    public boolean isShowAlpha() {
        return showAlpha;
    }

    /**
     * @param showAlpha the showAlpha to set
     */
    public void setShowAlpha(boolean showAlpha) {
        this.showAlpha = showAlpha;
    }

    /**
     * @return the oilRefinery
     */
    public double getOilRefinery() {
        return oilRefinery;
    }

    /**
     * @param oilRefinery the oilRefinery to set
     */
    public void setOilRefinery(double oilRefinery) {
        this.oilRefinery = oilRefinery;
    }

    /**
     * @return the profit
     */
    public double getProfit() {
        return profit;
    }

    /**
     * @return the resourceCostSumm
     */
    public double getResourcesCostSumm() {
        return resourcesCostSumm;
    }
}
