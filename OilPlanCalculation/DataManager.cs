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
            if (System.IO.File.Exists("data.sqlite"))
            {
                con = new SQLiteConnection(string.Format("Data Source={0};New=False;Compress=True;", dbName));
                //var lol = con.GetSchema();
            }
            else if (System.IO.File.Exists("data.sql"))
            {
                con = new SQLiteConnection(string.Format("Data Source={0};New=True;Compress=True;", dbName));
                con.Open();
                cmd = con.CreateCommand();
                cmd.CommandText = System.IO.File.ReadAllText("data.sql");
                int rowCount = cmd.ExecuteNonQuery();
                con.Close();
            }
            else
            {
                throw new Exception("Нет БД");
            }
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
        public DataRowCollection q(string queryText, object[] args)
        {
            return q(String.Format(queryText, args));
        }

        public DataRowCollection q(string queryText, object arg)
        {
            //cmd.Parameters.Add(arg);
            return q(String.Format(queryText, arg));
        }

        public DataRowCollection q(string queryText, object arg1, object arg2)
        {
            return q(String.Format(queryText, arg1, arg2));
        }


        // Выполнение запроса-выборки (SELECT).
        public DataRowCollection q(string queryText)
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
            return manager.q("select * from Таблица;");
        }

        private void initOilLpData()
        {
            var manager = this;
            goods = manager.q("select * from Good order by Id ASC");
            tools = manager.q("select * from Tool order by Id ASC");
            resources = manager.q("select * from Resource order by Id ASC");
            resourcesNumber = manager.q("select * from ResourceNumber");
            toolsOutGoods = manager.q("select * from GoodFromTool");
            toolsInGoods = manager.q("select * from NeedGoodForTool");
            toolsInResources = manager.q("select * from NeedResourceForTool");
        }
    }
}
