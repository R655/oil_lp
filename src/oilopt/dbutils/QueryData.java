/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oilopt.dbutils;
import java.sql.*;

/**
 * Костыль для правильной чистки ресурсов в случае exception'a
 * в DataManager
 * @author r655
 */
public class QueryData implements AutoCloseable 
{

    private Statement stmt;
    private ResultSet rs;
    
    public ResultSet getResult()
    {
        return rs;
    }
    
    public QueryData(Statement stmt, String q) throws SQLException
    {
        this.stmt = stmt;
        rs = stmt.executeQuery(q);
    }
    
    @Override
    public void close() throws Exception
    {
        stmt.close();
    }
}
