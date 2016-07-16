package org.apache.ibatis.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.reflection.ExceptionUtil;

//注解拦截器
public class Plugin implements InvocationHandler
{

	private Object target;//目标
	private Interceptor interceptor;//拦截器
	private Map<Class<?>, Set<Method>> signatureMap;//要拦截的目标的方法

	private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap)
	{
		this.target = target;
		this.interceptor = interceptor;
		this.signatureMap = signatureMap;
	}

	/**
	 * 将target进行代理，按照拦截器的方法
	 * @param target
	 * @param interceptor
	 * @return
	 */
	public static Object wrap(Object target, Interceptor interceptor)
	{
		Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
		Class<?> type = target.getClass();
		Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
		if (interfaces.length > 0)
		{
			return Proxy.newProxyInstance(type.getClassLoader(), interfaces,
					new Plugin(target, interceptor, signatureMap));
		}
		return target;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		try
		{
			Set<Method> methods = signatureMap.get(method.getDeclaringClass());
			if (methods != null && methods.contains(method))
			{
				return interceptor.intercept(new Invocation(target, method, args));
			}
			return method.invoke(target, args);
		}
		catch (Exception e)
		{
			throw ExceptionUtil.unwrapThrowable(e);//不错的写法，很灵活！
		}
	}

	private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor)
	{//如何使用缓存的话，效果更好。
		Signature[] sigs = interceptor.getClass().getAnnotation(Intercepts.class).value();//value是Signature数组
		Map<Class<?>, Set<Method>> signatureMap = new HashMap<Class<?>, Set<Method>>();
		//存储的类和方法
		for (Signature sig : sigs)
		{
			Set<Method> methods = signatureMap.get(sig.type());
			if (methods == null)
			{
				methods = new HashSet<Method>();
				signatureMap.put(sig.type(), methods);
			}
			try
			{
				Method method = sig.type().getMethod(sig.method(), sig.args());//class.getMethod(String name, Class<?>... parameterTypes)
				methods.add(method);
			}
			catch (NoSuchMethodException e)
			{
				throw new PluginException(
						"Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
			}
		}
		return signatureMap;
	}

	private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap)
	{
		Set<Class<?>> interfaces = new HashSet<Class<?>>();
		while (type != null)
		{
			for (Class<?> c : type.getInterfaces())
			{
				if (signatureMap.containsKey(c))
				{
					interfaces.add(c);
				}
			}
			type = type.getSuperclass();
		}
		return interfaces.toArray(new Class<?>[interfaces.size()]);
	}

}
