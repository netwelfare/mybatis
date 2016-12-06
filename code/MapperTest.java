package code;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import entity.User;

public class MapperTest
{

	public static void main(String[] args) throws IOException
	{
		String file = "conf/user2.xml";
		InputStream inputStream = Resources.getResourceAsStream(file);
		Configuration configuration = new Configuration();
		XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, file,
				configuration.getSqlFragments());
		mapperParser.parse();
		MappedStatement statement = configuration.getMappedStatement("selectUserByUser", false);
		SqlSource sql = statement.getSqlSource();
		User user = new User();
		user.setName("wxf");
		BoundSql sql2 = sql.getBoundSql(user);
		System.out.println(sql2.getSql());

	}

}
