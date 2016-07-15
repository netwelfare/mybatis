package org.apache.ibatis.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//拦截器注解，其中包含的方法是：value();因为仅仅是get方法，所以没有实现体，也就是说不用实现的方法都可以使用这种形式。
//而且方法返回的参数是Signature[]，而这又是一个注解：例如，
/*
@Intercepts 标明注解
( 标明注解的值
	{ 标明值是这个数组
		@Signature 数组元素是注解类型
		( 注解值的开始
			type= Executor.class,  class类型
			method = "update",     字符串类型
			args = {MappedStatement.class,Object.class}   class 数组类型
		)
	}
)
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Intercepts {
  Signature[] value();
}

