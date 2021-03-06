package code.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }) })
public class ExamplePlugin implements Interceptor {
	public Object intercept(Invocation invocation) throws Throwable {
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		System.err.println("ExamplePlugin：>>>>>>>>>>>>>>>>>>>>>>");

		if (target instanceof RoutingStatementHandler) {
			RoutingStatementHandler m = (RoutingStatementHandler) target;
			System.out.println(m.getBoundSql().getSql());
		}
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {

	}

}