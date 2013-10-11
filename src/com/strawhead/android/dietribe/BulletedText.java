package com.strawhead.android.dietribe;

import com.example.helloandroid.R;
import com.strawhead.android.dietribe.database.NutritionalDB;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class BulletedText {

	private String mText = "";
	private Drawable mBullet;
	private float mRating = 0;
	public int entry_id;
	public int meal_item_id;
	public int type;
	private boolean mSelectable = true;

	/**
	 * 
	 * @param text
	 * @param bullet
	 */
	public BulletedText(String text, Drawable bullet) {
		mBullet = bullet;
		mText = text;
		mRating = 0;
	}
	
	public BulletedText(Context ctx, String text, int type, int meal_item_id, int entry_id) {
		this.entry_id = entry_id;
		mText = text;
		this.meal_item_id = meal_item_id;
		this.type = type;
		switch(type)
		{
		case 1400:
		case NutritionalDB.BEVERAGES:
			mBullet = ctx.getResources().getDrawable(R.drawable.beverage);
			break;
		case 2100: 
		case NutritionalDB.FASTFOOD:
			mBullet = ctx.getResources().getDrawable(R.drawable.fastfood);
			break;
		case 100:
		case NutritionalDB.DAIRY:
			mBullet = ctx.getResources().getDrawable(R.drawable.milk);
			break;
		case 900:
		case NutritionalDB.FRUITS:
			mBullet = ctx.getResources().getDrawable(R.drawable.grapes);
			break;
		case 800:
		case 1800:
		case 2000:
		case NutritionalDB.GRAINS:
			mBullet = ctx.getResources().getDrawable(R.drawable.croissant);
			break;
		case 500:
		case 700:
		case 1000:
		case 1200:
		case 1300:
		case 1500:
		case 1600:
		case 1700:
		case NutritionalDB.MEAT:
			mBullet = ctx.getResources().getDrawable(R.drawable.meat);
			break;
		case 200:
		case 1100:
		case NutritionalDB.VEGETABLES:
			mBullet = ctx.getResources().getDrawable(R.drawable.snowpea);
			break;
		case 400:
		case 600:
		case NutritionalDB.OILS:
			mBullet = ctx.getResources().getDrawable(R.drawable.oils);
			break;
		case 1900:
		case 2500:
		case NutritionalDB.SNACKS:
			mBullet = ctx.getResources().getDrawable(R.drawable.snack);
			break;
		default:
			mBullet = ctx.getResources().getDrawable(R.drawable.breakfast);
			break;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSelectable() {
		return mSelectable;
	}
	
	/**
	 * 
	 * @param selectable
	 */
	public void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getText() {
		return mText;
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setText(String text) {
		mText = text;
	}
	
	/**
	 * 
	 * @param bullet
	 */
	public void setBullet(Drawable bullet) {
		mBullet = bullet;
	}
	
	/**
	 * 
	 * @return
	 */
	public Drawable getBullet() {
		return mBullet;
	}

	public float getRating() {
		return mRating;
	}
	
	public void setRating(float rating) {
		mRating = rating;
	}
}
