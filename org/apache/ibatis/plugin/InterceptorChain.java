package org.apache.ibatis.plugin;

import java.util.ArrayList;
import java.util.List;

public class InterceptorChain
{
	//拦截器链定义
	private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

	public Object pluginAll(Object target)
	{
		for (Interceptor interceptor : interceptors)
		{
			target = interceptor.plugin(target);//里面是层层封装的含义
		}
		return target;
	}

	public void addInterceptor(Interceptor interceptor)
	{
		interceptors.add(interceptor);
	}

}
