package code;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.RootLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

public class LogTest {

  public static void main(String[] args) {
    //Logger logger = LoggerFactory.getLogger("trainInsuRecordLogger");
	  
	Logger logger = LoggerFactory.getLogger("ROOT");
    logger.info("{},{}", "hello", "world"); 
    //代码的流程：
    //StaticLoggerBinder.getSingleton().getLoggerFactory();
    //Log4jLoggerFactory类的getLogger方法
    //进入LogManager.getLogger(name);
    //初始化这两个类：
    //Hierarchy h = new Hierarchy(new RootLogger((Level) Level.DEBUG));
	//repositorySelector = new DefaultRepositorySelector(h);
    //进入Hierarchy类的getLogger方法，然后获得方法
  }
}
