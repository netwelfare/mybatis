package code;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class SqlBatchInsertTest
{
	public static Connection getConnection() throws SQLException, java.lang.ClassNotFoundException
	{
		String url = "jdbc:mysql://localhost:3306/test";
		Class.forName("com.mysql.jdbc.Driver");
		String userName = "";
		String password = "";
		Connection con = (Connection) DriverManager.getConnection(url, userName, password);
		return con;
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException
	{
		Connection con = SqlBatchInsertTest.getConnection();
		Statement sql = (Statement) con.createStatement();
		sql.execute("drop table if exists student");
		sql.execute(
				"create table student(id int not null,name varchar(20) not null default 'name',math int not null default 60,primary key(id));");
		con.setAutoCommit(false);
		Statement stmt = (Statement) con.createStatement();
		long begin = System.currentTimeMillis();
		int count = 100;
		for (int i = 1; i <= count; i++)
		{
			stmt.addBatch("insert student values(" + i + ",'AAA','90')");
			if (i % 50 == 0)
			{
				stmt.executeBatch();
				stmt.clearBatch();
				con.commit();
				System.out.println("插入" + i + "条数据。");
			}
		}
		long end = System.currentTimeMillis();
		long elapse = end - begin;
		System.out.println("插入" + count + "条数据所需要的时间为:" + elapse);
		sql.close();
		con.close();
	}
	// 插入10000条数据所需要的时间为:1547
}
