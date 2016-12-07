package code;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import org.apache.ibatis.session.Configuration;

public class SqlRunner {

	private String file;
	private Connection connection;

	public SqlRunner(String file, Connection connection) {
		this.file = file;
		this.connection = connection;
	}

	public ResultSet query(String statement, Object parameter) {
		InputStream inputStream = null;
		ResultSet resultSet = null;
		try {
			inputStream = Resources.getResourceAsStream(file);
			Configuration configuration = new Configuration();
			XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, file,
					configuration.getSqlFragments());
			mapperParser.parse();
			MappedStatement mapstatement = configuration.getMappedStatement(statement, false);
			SqlSource sql = mapstatement.getSqlSource();
			BoundSql sql2 = sql.getBoundSql(parameter);
			DefaultParameterHandler handler = new DefaultParameterHandler(mapstatement, parameter, sql2);
			PreparedStatement ps = connection.prepareStatement(sql2.getSql());
			handler.setParameters(ps);
			System.err.println(ps.toString());
			resultSet = ps.executeQuery();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException  e) {

				e.printStackTrace();
			}

		}
		return resultSet;
	}
}
