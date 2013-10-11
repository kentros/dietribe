package com.strawhead.android.dietribe;

public class User {
	public String user_name;
	public int age;
	public int weight;
	public int height;
	public int gender;
	public int activity_level;
	
	public User(String name, int age, int weight, int height, int gender, int activity_level)
	{
		this.user_name = name;
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.gender = gender;
		this.activity_level = activity_level;
	}
	
	public String toString() {
		return user_name;
	}
}