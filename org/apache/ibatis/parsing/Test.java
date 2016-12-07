package org.apache.ibatis.parsing;

import java.util.Properties;

public class Test {

	public static void main(String[] args) {
		String sql ="select a,b from t where name=${name}";
		Properties variables= new Properties();
		variables.put("name", "wxf");
		String result =PropertyParser.parse(sql, variables);
		System.out.println(result);

	}

}
