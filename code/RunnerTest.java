package code;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import entity.QueryStatisUserGroup;
import entity.User;

public class RunnerTest {

	

	public static void main(String[] args) throws SQLException {
		ProxoolDataSource ds = new ProxoolDataSource();
		ds.setUser("qdm16813886");
		ds.setPassword("Wxf19831206");
		ds.setDriverUrl("jdbc:mysql://qdm16813886.my3w.com/qdm16813886_db?useUnicode=true&amp;characterEncoding=utf-8");
		ds.setDriver("com.mysql.jdbc.Driver");
		Connection conn = ds.getConnection();
		
		String file = "conf/user2.xml";
		User user = new User();
		user.setName("wxf");
		user.setId(1111);
		SqlRunner  runner =new SqlRunner(file,conn);
		ResultSet resultSet=	runner.query("selectUserByUser", user);
		while (resultSet.next()) {
		System.out.println("id: " + resultSet.getInt(1));
		System.out.println("name: " + resultSet.getString(2));
	}
		
		file = "conf/DataStatisUserGroup.xml";
		runner =new SqlRunner(file,conn);
		
		QueryStatisUserGroup group = new QueryStatisUserGroup();
		group.setStatFrequency("HOUR");
		group.setStartDate(new Date());
		group.setEndDate(new Date());
		List<String> pageNames = new ArrayList<String>();
		pageNames.add("gold");
		pageNames.add("yyg");
		group.setPageNames(pageNames);
		resultSet=	runner.query("getStatisUserGroups", group);
		
		

	}

}
