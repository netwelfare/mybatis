package code.net.swiftlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogNameTest2 {

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(LogNameTest2.class);
	    logger.info("{},{}", "hello", "LogNameTest2"); 
	}

}
