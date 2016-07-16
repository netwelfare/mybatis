package code.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

@Intercepts(
{ @Signature(type = Executor.class, method = "update", args =
		{ MappedStatement.class, Object.class }) })
public class ExamplePlugin2 implements Interceptor
{
	public Object intercept(Invocation invocation) throws Throwable
	{
		return invocation.proceed();
	}

	public Object plugin(Object target)
	{
		System.err.println("ExamplePlugin2：>>>>>>>>>>>>>>>>>>>>>>");
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties)
	{

	}

}