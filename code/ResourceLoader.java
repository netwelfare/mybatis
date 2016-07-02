package code;

import java.net.URL;
import java.util.List;

public class ResourceLoader {
	 public static void main(String[] args) {
		 
		 //ClassLoader加载资源，1 静态方法，2 对象方法（2.1系统对象，2.2自定义对象）
		 
		 //1 静态方法
		 
		 //加载系统类
		 URL url=  ClassLoader.getSystemResource("java/lang/String.class");
		 System.out.println(url);
		 
		 //加载自定义类
		 url =  ClassLoader.getSystemResource("code/ResourceLoader.class");
		 System.out.println(url);
		 
		 //2  对象方法加载
		 
		 //系统类对象，加载系统类
		 url = ClassLoader.class.getClassLoader().getResource("java/lang/String.class");
		 System.out.println(url);
		 
		 //系统类对象，加载自定义类
		 url = ClassLoader.class.getClassLoader().getResource("code/ResourceLoader.class");
		 System.out.println(url);
	 
		 //自定义类对象，加载系统类
		 url = ResourceLoader.class.getClassLoader().getResource("java/lang/String.class");
		 System.out.println(url);
		 
		 //自定义类对象，加载自定义类
		 url = ResourceLoader.class.getClassLoader().getResource("code/ResourceLoader.class");
		 System.out.println(url);
		 
		 
		 //加载系统类
		 url = ClassLoader.class.getResource("/java/lang/String.class");
		 System.out.println(url);
		 
		 //加载本类
		 url = ClassLoader.class.getResource("/code/ResourceLoader.class");
		 System.out.println(url);
		 
		 
		 url =  List.class.getResource("/java/lang/String.class");
		 System.out.println(url);
		 
		 //解析不同的路径
		 System.out.println(resolveName("/java/lang/String.class"));
		 System.out.println(resolveName("java/lang/String.class"));
		 
		 
	 }
	 
	 
	 private static String  resolveName(String name) {
	      if (name == null) {
	          return name;
	      }
	      if (!name.startsWith("/")) {
	          //Class<?> c = ResourceLoader.class;
	    	  Class<?> c = List.class;
	          while (c.isArray()) {
	              c = c.getComponentType();
	          }
	          String baseName = c.getName();
	          int index = baseName.lastIndexOf('.');
	          if (index != -1) {
	              name = baseName.substring(0, index).replace('.', '/')
	                  +"/"+name;
	          }
	      } else {
	          name = name.substring(1);
	      }
	      return name;
	  }
}
