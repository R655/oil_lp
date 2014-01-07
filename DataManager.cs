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

        public DataManager(string dbName)
        {
            con = new SQLiteConnection(string.Format("Data Source={0};New=False;Compress=True;", dbName));
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
        public DataTable Select(string queryText)
        {
            con.Open();
            cmd = con.CreateCommand();
            db = new SQLiteDataAdapter(queryText, con);
            dataSet.Reset();
            db.Fill(dataSet);
            dataTable = dataSet.Tables[0];
            con.Close();
            return dataTable;

            // Если необходимо достать какие-то данные из DataTable:
            // dataTable.Rows[id]["column_name"].ToString();
        }

        public static DataTable SimpleExample()
        {
            var manager = new DataManager("test_db.sqlite"); // База рядом с .exe
            manager.ExecuteQuery("create table if not exists Таблица (столб1 int, столб2 varchar default 'Джигурда');");
            manager.ExecuteQuery("insert into Таблица ('столб1') values (42);");
            return manager.Select("select * from Таблица;");
        }
    }
}
