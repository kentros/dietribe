package com.strawhead.android.dietribe;

public class FoodWeight 
{
	public String Msre_Desc;
	public int Seq;
	public double Amount;
	public double Gm_Wgt;
	
	public FoodWeight(String Msre_Desc, double Amount, double Gm_Wgt)
	{
		this.Amount = Amount;
		this.Gm_Wgt = Gm_Wgt;
		this.Msre_Desc = Msre_Desc;
	}
	
	public String toString()
	{
		return Msre_Desc;
	}
}
