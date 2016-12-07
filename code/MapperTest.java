package code;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.logicalcobwebs.proxool.ProxoolDataSource;

import entity.QueryStatisUserGroup;
import entity.User;

public class MapperTest {

	public static void main(String[] args) throws IOException, SQLException {
		String file = "conf/DataStatisUserGroup.xml";
		InputStream inputStream = Resources.getResourceAsStream(file);
		Configuration configuration = new Configuration();
		XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, file,
				configuration.getSqlFragments());
		mapperParser.parse();
		MappedStatement statement = configuration.getMappedStatement("getStatisUserGroups", false);
		SqlSource sql = statement.getSqlSource();
//		User user = new User();
//		user.setName("wxf");
//		user.setId(1111);
		QueryStatisUserGroup group = new QueryStatisUserGroup();
		group.setStatFrequency("HOUR");
		group.setStartDate(new Date());
		group.setEndDate(new Date());
		List<String> pageNames = new ArrayList<String>();
		pageNames.add("gold");
		pageNames.add("yyg");
		group.setPageNames(pageNames);
		BoundSql sql2 = sql.getBoundSql(group);
		System.out.println(removeBreakingWhitespace(sql2.getSql()));
		DefaultParameterHandler handler = new DefaultParameterHandler(statement, group, sql2);

		ProxoolDataSource ds = new ProxoolDataSource();
		ds.setUser("qdm16813886");
		ds.setPassword("Wxf19831206");
		ds.setDriverUrl("jdbc:mysql://qdm16813886.my3w.com/qdm16813886_db?useUnicode=true&amp;characterEncoding=utf-8");
		ds.setDriver("com.mysql.jdbc.Driver");
		Connection conn = ds.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql2.getSql());
		handler.setParameters(ps);
		System.out.println(removeBreakingWhitespace(ps.toString()));
//		ResultSet resultSet = ps.executeQuery();
//		
//		while (resultSet.next()) {
//			System.out.println("id: " + resultSet.getInt(1));
//			System.out.println("name: " + resultSet.getString(2));
//		}
		//System.out.println(removeBreakingWhitespace(sql2.getSql()));

	}

	protected static String removeBreakingWhitespace(String original) {
		StringTokenizer whitespaceStripper = new StringTokenizer(original);
		StringBuilder builder = new StringBuilder();
		while (whitespaceStripper.hasMoreTokens()) {
			builder.append(whitespaceStripper.nextToken());
			builder.append(" ");
		}
		return builder.toString();
	}
}
