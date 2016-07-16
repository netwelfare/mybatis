package org.apache.ibatis.plugin;

import java.util.Properties;

//拦截器接口定义
/**
 * 拦截器的整体实现过程：
 * 第一步：解析xml文件，分离出拦截器。XMLConfigBuilder.pluginElement
 *  <!ELEMENT plugins (plugin+)>
 *	<!ELEMENT plugin (property*)>
 *	<!ATTLIST plugin
 *	interceptor CDATA #REQUIRED
 *	>
 *	第二步：Configuration.addInterceptor
 *  第三步：InterceptorChain.addInterceptor
 *  第四步：Configuration.newExecutor
 *  第五步：(Executor) interceptorChain.pluginAll(executor);
 *  executor是target，翻译为：拦截器插入目标。相当于重新包装了target对象
 *  public Object pluginAll(Object target) {
    for (Interceptor interceptor : interceptors) {
      target = interceptor.plugin(target);
    }
    return target;
  }
 * 第六步：Executor.update方法》》》》SimpleExecutor.doUpdate
 * 第七步：configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null);
 * 第八步：interceptorChain.pluginAll(statementHandler)
 */
public interface Interceptor
{

	Object intercept(Invocation invocation) throws Throwable;

	Object plugin(Object target);

	void setProperties(Properties properties);

}
