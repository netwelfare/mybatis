package entity;

import java.io.Serializable;

public class User implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7670205573371336959L;
	/**
	 * 
	 */

	private int id;
	private String name;
	private int age;

	public User()
	{

	}

	public User(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return "User [id:" + this.id + ";name:" + this.name + "]";
	}

	// insert into user(id,name)values(1,'');
	//insert into user(id)values(2);

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}
}