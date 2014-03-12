/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oilopt.dbutils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

/**
 * Используется для отправки прямых запросов к БД
 * (нужно в случае, если не хватает орм)
 * @author R65
 */
public class DataManager implements AutoCloseable {
    public Connection con = null;
    private Statement stmt = null;
    protected String dbName = null;
    
    static public void onlyInitDB(String dbName, String queryFileName) throws FileNotFoundException, ClassNotFoundException, SQLException, IOException
    {
        
        try(DataManager dm = new DataManager(dbName, queryFileName))
        {
            ;
        }
    }
    
            
    public Collection<Map<String, Object>> goods;
    public Collection<Map<String, Object>> tools;
    public Collection<Map<String, Object>> resources;
    public Collection<Map<String, Object>> resourcesNumber;
    public Collection<Map<String, Object>> toolsOutGoods;
    public Collection<Map<String, Object>> toolsInGoods;
    public Collection<Map<String, Object>> toolsInResources;
    
    /**
     * Вытаскивает данные для решения задачи из БД
     * @throws SQLException
     */
    @Deprecated
    public void initOilLpData() throws SQLException
    {
        DataManager manager = this;
        goods = manager.q("select * from Good order by Id ASC");
        tools = manager.q("select * from Tool order by Id ASC");
        resources = manager.q("select * from Resource order by Id ASC");
        resourcesNumber = manager.q("select * from ResourceNumber");
        toolsOutGoods = manager.q("select * from GoodFromTool");
        toolsInGoods = manager.q("select * from NeedGoodForTool");
        toolsInResources = manager.q("select * from NeedResourceForTool");
    }
 
    public static Collection<Map<String, Object>> convertResultSet(ResultSet rs) throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        Collection<Map<String, Object>> result = new LinkedList<>();
        
        while(rs.next())
        {
            Map<String, Object> row = new LinkedHashMap<>();
            
            for(int i = 1;
                    i <= rsmd.getColumnCount();
                    ++i)
            {
                String colName = rsmd.getColumnName(i);
                row.put(colName, rs.getObject(colName));
                
            }
            result.add(row);
            
        }
        return result;
    }
    
    /**
     * Инициализирует класс только в том случае, если существует файл с БД dbName
     * @param dbName
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException 
     */
    public DataManager(String dbName) 
            throws ClassNotFoundException, SQLException, FileNotFoundException
    {
        this.dbName = dbName;
        
        boolean dbexists = new File(dbName).exists();
        if(!dbexists)
        {
            throw new FileNotFoundException(dbName);
        }
        else
        {
            connect(dbName);
        }
    }
    

    /**
     * Перегоняет запросы из queryFileName в БД dbName
     * @param dbName
     * @param queryFileName
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public DataManager(String dbName, String queryFileName) 
            throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        this.dbName = dbName;
        
        boolean dbexists = new File(dbName).exists();
        boolean qfexists = new File(queryFileName).exists();
        if(dbexists || qfexists)
        {
            connect(dbName);
            if (!dbexists)
                initFromFile(queryFileName);
        }
        else
        {
            throw new FileNotFoundException(queryFileName);
        }
    }
    
    private void initFromFile(String queryFileName) throws ClassNotFoundException, SQLException, IOException
    {
        String queries = readFile(queryFileName);
        u(queries);
    }
    
    private void connect(String dbName) throws ClassNotFoundException, SQLException
    {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbName));
    }

    /**
     * Выполнение запросов, которые не возвращают данные (INSERT, UPDATE, DELETE, CREATE, ... ).
     * @param queryText
     * @return
     * @throws SQLException 
     */
    public int u(String queryText) throws SQLException
    {
        if(con == null)
            throw new SQLException("Не инициализировано подключение");
                    
        int rs = -1;
        
        try(Statement ustmt = con.createStatement()){
            rs = ustmt.executeUpdate(queryText);}
        
        if(rs == -1)
             throw new SQLException("Ошибка обработке запроса");
        
        return rs;
        
    }
    
 
    
    private String readFile(String fileName) throws IOException 
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
        {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        }
    }
    
    /**
     * Выполнение запроса-выборки (SELECT).
     * @param queryText
     * @param args
     * @return
     * @throws SQLException 
     */
    public Collection<Map<String, Object>> q(String queryText, Object[] args) throws SQLException
    {
        return q(String.format(queryText, args));
    }

    /**
     * Выполнение запроса-выборки (SELECT).
     * @param queryText
     * @param arg
     * @return
     * @throws SQLException 
     */    
    public Collection<Map<String, Object>> q(String queryText, Object arg) throws SQLException 
    {
        //cmd.Parameters.Add(arg);
        return q(String.format(queryText, arg));
    }

    /**
     * Выполнение запроса-выборки (SELECT).
     * @param queryText
     * @param arg1
     * @param arg2
     * @return
     * @throws SQLException 
     */
    public Collection<Map<String, Object>> q(String queryText, Object arg1, Object arg2) throws SQLException
    {
        return q(String.format(queryText, arg1, arg2));
    }


    /**
     * Выполнение запроса-выборки (SELECT).
     * @param queryText
     * @return
     * @throws SQLException 
     */
   public Collection<Map<String, Object>> q(String queryText) throws SQLException
    {
        if(con == null)
             throw new SQLException("Не инициализировано подключение");
        ResultSet rs = null;
        if(stmt != null)
        {
            stmt.close();
            stmt = null;
        }
        stmt = con.createStatement();
        rs = stmt.executeQuery(queryText);
        if(rs == null)
            throw new SQLException();
        
        return convertResultSet(rs);
    }
   
    /**
     * Выполнение запроса-выборки (SELECT). 
     * (Неудобно в отличие от q, но правильно освобождает ресурсы)
     * @param queryText
     * @return
     * @throws SQLException 
     */
   public QueryData mq(String queryText) throws SQLException, Exception
    {
         if(con == null)
            throw new Exception("Не инициализировано подключение");
        return new QueryData(con.createStatement(), queryText);
    }
   

    public static void SimpleExample() throws SQLException, ClassNotFoundException, Exception
    {
        DataManager manager = new DataManager("test_db.sqlite"); // База рядом с .exe
        int r = -1;
        r = manager.u("create table if not exists accounts (id int PRIMARY KEY, name varchar);");
        r = manager.u("insert into accounts ('name') values ('abcd');");
        r = manager.u("insert into accounts ('name') values ('abcd1');");
        r = manager.u("insert into accounts ('name') values ('abcd2');");
        r = manager.u("insert into accounts ('name') values ('abcd3');");
        
        /*
         * Способ неудобный, но даёт гарантию, что ресурсы будут освобождены в любом случае
         */
        try(QueryData qd = manager.mq("select * from accounts;"))
        {
            ResultSet rs = qd.getResult();
            while(rs.next())
            {
                String ri = String.valueOf(rs.getRow());
                String name = rs.getString("name");
                System.out.println( ri + ". " 
                    + "\t" + name);
            }
        }
        
        /*
         * Удобный способ
         */
        Collection<Map<String, Object>> rs = manager.q("select * from accounts;");
        for (Map<String, Object> re : rs) {
            for(Map.Entry<String, Object> e : re.entrySet())
            {
                
                System.out.print( e.getValue()
                + "\t");
            }
            System.out.println();
        }
        
    }
    
    @Override
    public void close() throws SQLException
    {
        con.close();
    }
}
