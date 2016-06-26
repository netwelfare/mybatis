package org.apache.ibatis.logging.slf4j;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jImpl implements Log
{

	private Logger log;

	// 构造方法，利用了slf4j的类
	public Slf4jImpl(Class<?> clazz)
	{
		System.out.println(log.isInfoEnabled());
		log.info("hello {}", "world");
		log = LoggerFactory.getLogger(clazz);

	}

	public boolean isDebugEnabled()
	{
		return log.isDebugEnabled();
	}

	public void error(String s, Throwable e)
	{
		log.error(s, e);
	}

	public void error(String s)
	{
		log.error(s);
	}

	public void debug(String s)
	{
		log.debug(s);
	}

	public void warn(String s)
	{
		log.warn(s);
	}

}
