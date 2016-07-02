package code.net.swiftlet.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogNameTest1 {

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(LogNameTest1.class);
	    logger.info("{},{}", "hello", "LogNameTest1"); 

	}

}
