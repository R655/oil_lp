/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oilopt.math;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import oilopt.orm.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
/**
 * Необходимые данные для задачи
 * @author r655
 */
public class ProblemData 
{
    public List<Resource> resources = null;
    public List<Good> goods = null;
    public List<Tool> tools = null;
    public List<NeedGoodForTool> goodRequirments = null;
    public List<GoodFromTool> goodReceivers = null;
    public List<NeedResourceForTool> resourceRequirments = null;
    public List<ResourceNumber> resourceNumbers = null;
    
    /*
     Соответсвие ключей БД индексам задачи оптимизации
     Необходимо, если ключи БД идут не по порядку или начинаются не с нуля
    */
    public Map<Integer, Integer> resourceIndexById = new HashMap<>();
    public Map<Integer, Integer> goodIndexById = new HashMap<>();
    public Map<Integer, Integer> toolIndexById = new HashMap<>();
    
    public ProblemData(String dbName)
            throws SQLException, FileNotFoundException
    {
        if(!(new File(dbName).exists()))
            throw new FileNotFoundException(dbName);
        
        final String url = "jdbc:sqlite:"+dbName;
        ConnectionSource source = new JdbcConnectionSource(url);
        
        Dao<Resource, String> resourceDao = DaoManager.createDao(source, Resource.class);
        Dao<Good, String> goodDao = DaoManager.createDao(source, Good.class);
        Dao<Tool, String> toolDao = DaoManager.createDao(source, Tool.class);
        Dao<NeedGoodForTool, String> ngftDao = DaoManager.createDao(source, NeedGoodForTool.class);
        Dao<GoodFromTool, String> gftDao = DaoManager.createDao(source, GoodFromTool.class);
        Dao<NeedResourceForTool, String> nrftDao = DaoManager.createDao(source, NeedResourceForTool.class);
        Dao<ResourceNumber, String> rnumDao = DaoManager.createDao(source, ResourceNumber.class);
        
        resources = resourceDao.queryForAll();
        goods = goodDao.queryForAll();
        tools = toolDao.queryForAll();
        goodRequirments = ngftDao.queryForAll();
        goodReceivers = gftDao.queryForAll();
        resourceRequirments = nrftDao.queryForAll();
        resourceNumbers = rnumDao.queryForAll();
        
        initProblemIndicies();
    }
    
        
    /*
        Инициализация соответсвия первичных ключей БД индексам задачи оптимизации
        Необходимо, если ключи БД идут не по порядку или начинаются не с нуля
    */
    final void initProblemIndicies()
    {
        
        int i = 0;
        for(Good good : goods)
        {
            goodIndexById.put(good.getId(), i);
            ++i;
        }
        
        i = 0;
        for(Tool tool : tools)
        {
            toolIndexById.put(tool.getId(), i);
            ++i;
        }
        
        i = 0;
        for(Resource resource : resources)
        {
            resourceIndexById.put(resource.getId(), i);
            ++i;
        }
        
        
    }
    
    
    public int getToolIndex(int i)
    {
        return toolIndexById.get(i);
    }
    
    public int getGoodIndex(int i)
    {
        return goodIndexById.get(i);
    }
    
    public int getResourceIndex(int i)
    {
        return resourceIndexById.get(i);
    }
}
