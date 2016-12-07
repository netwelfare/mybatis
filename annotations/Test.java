package annotations;

import java.lang.reflect.Method;

public class Test {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		
		Method method = WeakCache.class.getMethod("putObject",Object.class,Object.class);
		Cache cache= 	method.getAnnotation(Cache.class);
		System.out.println(cache.value());

	}

}
