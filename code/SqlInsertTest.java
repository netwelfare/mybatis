package code;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class SqlInsertTest
{
	public static Connection getConnection() throws SQLException, java.lang.ClassNotFoundException
	{
		String url = "jdbc:mysql://localhost:3306/test";
		Class.forName("com.mysql.jdbc.Driver");
		String userName = "root";
		String password = "";
		Connection con = (Connection) DriverManager.getConnection(url, userName, password);
		return con;
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException
	{
		Connection con = SqlInsertTest.getConnection();
		Statement sql = con.createStatement();
		sql.execute("drop table if exists student");
		sql
				.execute("create table student(id int not null,name varchar(20) not null default 'name',math int not null default 60,primary key(id));");
		long begin = System.currentTimeMillis();
		int count = 10000;
		for (int i = 0; i < count; i++)
		{
			sql.execute("insert student values(" + i + ",'AAA','90')");
		}
		long end = System.currentTimeMillis();
		long elapse = end - begin;
		System.out.println("插入" + count + "条数据所需要的时间为:" + elapse);
		sql.close();
		con.close();
	}

	//	插入10000条数据所需要的时间为:236605
}
