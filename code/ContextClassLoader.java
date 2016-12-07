package code;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class ContextClassLoader {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://localhost:3306/test";
		Class.forName("com.mysql.jdbc.Driver");
		String userName = "root";
		String password = "";
		Connection con = (Connection) DriverManager.getConnection(url, userName, password);
	}
	private static Connection getConnection(String url, java.util.Properties info, Class<?> caller)
			throws SQLException {
		ClassLoader callerCL = caller != null ? caller.getClassLoader() : null;
		synchronized (DriverManager.class) {
			if (callerCL == null) {
				// 第一步：找到上下文类加载器
				callerCL = Thread.currentThread().getContextClassLoader();
			}
		}
		/*for (DriverInfo aDriver : registeredDrivers) {// DriverInfo仅仅是Driver的封装
			Driver driver = aDriver.driver;
			Class<?> aClass = null;
			try {
				// 第二步：利用上下文类加载器加载driver类，虽然driver类已经加载了，但是启动类
				aClass = Class.forName(driver.getClass().getName(), true, callerCL);
			} catch (Exception ex) {

			}
			Connection con = aDriver.driver.connect(url, info);
		}*/
		
		Connection con=null;
		return con;

	}

}
