package code;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class Urs
{

	public static Connection getConnection() throws SQLException, java.lang.ClassNotFoundException
	{
		String url = "jdbc:mysql://localhost:3306/urs";
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
		String account = "didawangxiaofei@126.com";
		ResultSet result = sql.executeQuery("select * from urs.tb_urs where urs ='" + account + "';");

	}
}
