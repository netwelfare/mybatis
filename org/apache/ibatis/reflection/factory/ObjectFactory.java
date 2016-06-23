package org.apache.ibatis.reflection.factory;

import java.util.List;
import java.util.Properties;
//不错的工厂方法
public interface ObjectFactory {

  Object create(Class type);

  Object create(Class type, List<Class> constructorArgTypes, List<Object> constructorArgs);

  void setProperties(Properties properties);

}
