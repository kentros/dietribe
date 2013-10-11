package com.strawhead.android.dietribe;

import java.util.HashMap;

public class FoodItem 
{
	public int NDB_No;
	public int FdGrp_Cd;
	public String Long_Desc;
    public int category;
    
    //public double calories;
    
    public HashMap<Integer, Double> nutrients;
	public double amount;
	public String measure;
    
	private void initialize()
	{    	
    	nutrients = new HashMap<Integer, Double>();
	}
	
    public FoodItem()
    {
    	initialize();
    }
    
    public FoodItem(String foodItemName)
    {
    	Long_Desc = foodItemName;
    	initialize();
    }
	
	public String toString()
	{
		return Long_Desc;
	}
}