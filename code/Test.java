package code;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import entity.User;

public class Test {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/**
		 * 关于日志配置文件的： http://javapub.iteye.com/blog/866664
		 */
		String resource = "conf/configuration.xml";// 可以使用/当做根目录
		Reader reader = Resources.getResourceAsReader(resource);
		SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(reader);// 解析文件比较难的。
		System.err.println(ssf.getClass().getName());// DefaultSqlSessionFactory
		SqlSession session = ssf.openSession();// 通过DateSource实现Connection的，
		System.err.println(session.getClass().getName());// DefaultSqlSession
		try {
			User user1 = (User) session.selectOne("selectUserById", "1111");

			System.out.println(user1.getName());
			System.out.println(user1.getAge());

			/*
			 * User user2 = new User(); user2.setId(2014);
			 * 
			 * user2 = (User) session.selectOne("selectUserByUser", user2);
			 * System.out.println(user2);
			 * 
			 * List<User> result1 = session.selectList("selectAllUser");
			 * System.out.println(result1.get(0).getAge());
			 * System.out.println(result1);
			 * 
			 * List<User> result = session.selectList("selectAllUser2");
			 * System.out.println(result.get(0).getAge());
			 * System.out.println(result);
			 */

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

}
