package org.apache.ibatis.plugin;

import java.util.Properties;
//拦截器接口定义
public interface Interceptor {

  Object intercept(Invocation invocation) throws Throwable;

  Object plugin(Object target);

  void setProperties(Properties properties);

}
