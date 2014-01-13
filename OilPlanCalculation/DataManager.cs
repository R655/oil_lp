using System;
using System.Data;
using System.Data.SQLite;


namespace ForDB
{
    public class DataManager
    {
        private SQLiteConnection con;
        private SQLiteCommand cmd;
        private SQLiteDataAdapter db;
        private DataSet dataSet = new DataSet();
        private DataTable dataTable = new DataTable();

        public DataRowCollection goods;
        public DataRowCollection tools;
        public DataRowCollection resources;
        public DataRowCollection resourcesNumber;
        public DataRowCollection toolsOutGoods;
        public DataRowCollection toolsInGoods;
        public DataRowCollection toolsInResources;

        public DataManager(string dbName)
        {
            con = new SQLiteConnection(string.Format("Data Source={0};New=False;Compress=True;", dbName));
            initOilLpData();
        }

        // Выполнение запросов, которые не возвращают данные (INSERT, UPDATE, DELETE, CREATE, ... ).
        public int ExecuteQuery(string queryText)
        {
            con.Open();
            cmd = con.CreateCommand();
            cmd.CommandText = queryText;
            int rowCount = cmd.ExecuteNonQuery();
            con.Close();

            // Количество строк, затронутых последним запросом.
            return rowCount;
        }

        // Выполнение запроса-выборки (SELECT).
        public DataRowCollection Select(string queryText, object[] args)
        {
            return Select(String.Format(queryText, args));
        }

        public DataRowCollection Select(string queryText, object arg)
        {
            return Select(String.Format(queryText, arg));
        }

        public DataRowCollection Select(string queryText, object arg1, object arg2)
        {
            return Select(String.Format(queryText, arg1, arg2));
        }


        // Выполнение запроса-выборки (SELECT).
        public DataRowCollection Select(string queryText)
        {
            con.Open();
            cmd = con.CreateCommand();

            db = new SQLiteDataAdapter(queryText, con);
            dataSet = new DataSet();
            db.Fill(dataSet);
            dataTable = dataSet.Tables[0];
            con.Close();
            return dataTable.Rows;

            // Если необходимо достать какие-то данные из DataTable:
            // dataTable.Rows[id]["column_name"].ToString();
        }

        public static DataRowCollection SimpleExample()
        {
            var manager = new DataManager("test_db.sqlite"); // База рядом с .exe
            manager.ExecuteQuery("create table if not exists Таблица (столб1 int, столб2 varchar default 'Джигурда');");
            manager.ExecuteQuery("insert into Таблица ('столб1') values (42);");
            return manager.Select("select * from Таблица;");
        }

        private void initOilLpData()
        {
            var manager = this;
            goods = manager.Select("select * from Good order by Id ASC");
            tools = manager.Select("select * from Tool order by Id ASC");
            resources = manager.Select("select * from Resource order by Id ASC");
            resourcesNumber = manager.Select("select * from ResourceNumber");
            toolsOutGoods = manager.Select("select * from GoodFromTool");
            toolsInGoods = manager.Select("select * from NeedGoodForTool");
            toolsInResources = manager.Select("select * from NeedResourceForTool");
        }
    }
}
