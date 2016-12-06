package code.eum;

import org.apache.ibatis.mapping.ParameterMode;

public class Test {

	public static void main(String[] args) {
		System.out.println(ParameterMode.IN.equals("IN"));
		System.out.println(ParameterMode.IN.toString().equals("IN"));
		System.out.println(ParameterMode.valueOf("IN").equals(ParameterMode.IN));
		System.out.println(ParameterMode.valueOf("IN")==(ParameterMode.IN));
		System.out.println(ParameterMode.IN.ordinal());
	}

}
