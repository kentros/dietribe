package com.strawhead.android.dietribe.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.strawhead.android.dietribe.BulletedText;
import com.strawhead.android.dietribe.Codes;
import com.strawhead.android.dietribe.FoodItem;
import com.strawhead.android.dietribe.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDB
{
	private static final String DATABASE_NAME = "user.db";
	
	private static final String USER_TABLE = "USER";
	private static final String ENTRY_TABLE = "ENTRY";
	private static final String USER_ENTRY_TABLE = "USER_ENTRY";
	private static final String MEAL_ITEM_TABLE = "MEAL_ITEM";
	
	private static final String CREATE_TABLE_USER = "create table IF NOT EXISTS user (user_id integer primary key autoincrement, "
        + "name text not null, age int not null, weight int not null, height int not null, gender int not null, activity_level int not null," +
        		"calorie_target int null, protein_target real null, total_fat_target real null," +
        		"sat_fat_target real null, total_carb_target real null, fiber_target real null," +
        		"sugar_target real null, sodium_target real null, cholesterol_target real null," +
        		"potassium_target real null, vitaminA_target real null, vitaminC_target real null," +
        		"iron_target real null, calcium_target real null);";
	
	private static final String CREATE_TABLE_USER_ENTRY = "create table IF NOT EXISTS user_entry (user_id int not null, "
        + "date integer not null, entry_id int not null, primary key(user_id, date, entry_id));";
	
	// The entry table acts as a bridge between different types of entries, allowing
	// either USDA or User entered food items or exercises or medical information
	// to be considered an entry.
	private static final String CREATE_TABLE_ENTRY = "create table IF NOT EXISTS entry (entry_id integer primary key autoincrement, "
		+ "description text not null, type int not null, subtype int null, meal_item_id int null, exercise_item_id int null, medical_item_id int null);";
	
	private static final String CREATE_TABLE_WEIGHT_HISTORY = "create table IF NOT EXISTS weight_history (user_id int not null,"
		+ "date int not null, weight int not null, primary key(user_id, date));";
	
	private static final String CREATE_TABLE_MEAL_ITEM = "create table IF NOT EXISTS meal_item (meal_item_id integer primary key autoincrement,"
		+ "fdgrp_id int null, meal_item_name text not null, measure text not null, quantity real not null," +
				"calories real null, protein real null, total_fat real null, sat_fat real null," +
				"trans_fat real null, total_carb real null, fiber real null, sugar real null," +
				"cholesterol real null, potassium real null," +
				"sodium real null, vitaminA real null, vitaminC real null, calcium real null, iron real null);";
	
	private SQLiteDatabase db;
	private final Context context;
	

	public UserDB(Context ctx)
	{
		context = ctx;
		db = ctx.openOrCreateDatabase(DATABASE_NAME, 1, null);
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_USER_ENTRY);
		db.execSQL(CREATE_TABLE_ENTRY);
		db.execSQL(CREATE_TABLE_MEAL_ITEM);
		db.execSQL(CREATE_TABLE_WEIGHT_HISTORY);

	}
	
	public long insertUser(String user_name, int age, int weight, int height, int gender, int activity_level)
	{
		ContentValues values = new ContentValues();
		values.put("name", user_name);
		values.put("age", age);
		values.put("weight", weight);
		values.put("height", height);
		values.put("gender", gender);
		values.put("activity_level", activity_level);
		return db.insert(USER_TABLE, null, values);
	}
	
	public int updateUser(int user_id, String user_name, int age, int weight, int height, int gender, int activity_level)
	{
		ContentValues values = new ContentValues();
		values.put("name", user_name);
		values.put("age", age);
		values.put("weight", weight);
		values.put("height", height);
		values.put("gender", gender);
		values.put("activity_level", activity_level);
		return db.update(USER_TABLE, values, "user_id=" + user_id, null);
	}
	
	public long insertEntry(String description, int type, long meal_item_id)
	{
		ContentValues values = new ContentValues();
		values.put("description", description);
		values.put("type", type);
		//values.put("subtype", subtype);
		values.put("meal_item_id", meal_item_id);
		return db.insert(ENTRY_TABLE, null, values);
	}
	
	public boolean updateUserTargets(int user_id, int calorie_target)
	{
		ContentValues values = new ContentValues();
		values.put("calorie_target", calorie_target);
		return (db.update(USER_TABLE, values, "user_id=" + user_id, null) > 0);
	}
	
	public boolean updateUserTargets(int user_id, int calorie_target, float... targets)
	{
		ContentValues values = new ContentValues();
		values.put("calorie_target", calorie_target);
		if (targets != null || targets.length != 13) {
			values.put("protein_target", targets[0]);
			values.put("total_fat_target", targets[1]);
			values.put("sat_fat_target", targets[2]);
			values.put("total_carb_target", targets[3]);
			values.put("fiber_target", targets[4]);
			values.put("sugar_target", targets[5]);
			values.put("sodium_target", targets[6]);
			values.put("cholesterol_target", targets[7]);
			values.put("potassium_target", targets[8]);
			values.put("vitaminA_target", targets[9]);
			values.put("vitaminC_target", targets[10]);
			values.put("iron_target", targets[11]);
			values.put("calcium_target", targets[12]);
		}
		return (db.update(USER_TABLE, values, "user_id=" + user_id, null) > 0);
	}
	
	public long insertUserEntryItem(int user_id, int date, long entry_id)
	{
		ContentValues values = new ContentValues();
		values.put("user_id", user_id);
		values.put("date", date);
		values.put("entry_id", entry_id);
		return db.insert(USER_ENTRY_TABLE, null, values);
	}
	
	public long insertMealItem(String meal_item_name, String measure, double quantity, int fdgrp_id, HashMap<Integer, Double> nutrients)
	{
		ContentValues values = getNutrientContentValues(nutrients);
		values.put("fdgrp_id", fdgrp_id);
		values.put("meal_item_name", meal_item_name);
		values.put("measure", measure);
		values.put("quantity", quantity);

		return db.insert(MEAL_ITEM_TABLE, null, values);
	}
	
	private ContentValues getNutrientContentValues(HashMap<Integer, Double> nutrients)
	{
		ContentValues values = new ContentValues();
		Set<Integer> keys = nutrients.keySet();
    	for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
    		int key = iterator.next();
    		double value = nutrients.get(key);
    		switch(key)
			{
			case Codes.CALORIES:
				values.put("calories", value);
				break;
			case Codes.PROTEIN:
				values.put("protein", value);
				break;
			case Codes.TOTALFAT:
				values.put("total_fat", value);
				break;
			case Codes.SATFAT:
				values.put("sat_fat", value);
				break;
			case Codes.TRANSFAT:
				values.put("trans_fat", value);
				break;
			case Codes.CARB:
				values.put("total_carb", value);
				break;
			case Codes.FIBER:
				values.put("fiber", value);
				break;
			case Codes.TOTALSUGAR:
				values.put("sugar", value);
				break;
			case Codes.CHOLESTEROL:
				values.put("cholesterol", value);
				break;
			case Codes.SODIUM:
				values.put("sodium", value);
				break;
			case Codes.POTASSIUM:
				values.put("potassium", value);
				break;
			case Codes.VITAMINA:
				values.put("vitaminA", value);
				break;
			case Codes.VITAMINC:
				values.put("vitaminC", value);
				break;
			case Codes.CALCIUM:
				values.put("calcium", value);
				break;
			case Codes.IRON:
				values.put("iron", value);
				break;
			}
    	}
		return values;
	}
	
	public long updateMealItem(int meal_item_id, HashMap<Integer, Double> nutrients) {
		return db.update(MEAL_ITEM_TABLE, getNutrientContentValues(nutrients), "meal_item_id=" + meal_item_id, null);
	}
	
	public int removeUserEntryItem(int user_id, int date, int entry_id)
	{
		//TODO Remove an entry on entry table and meal_item table as well as user_entry_table
		//remove entry based on entry_id and then search DB to see if any other entries
		//include the meal_item_id.  If no other entries reference the meal_item_id,
		//then remove the meal_item as well
		return (db.delete(USER_ENTRY_TABLE, "user_id=" + user_id
				+ " AND date=" + date + " AND entry_id=" + entry_id, null));
	}

	public User getUser(int user_id) {
		User user = null;
		try {
			// Get long description of food item
			Cursor c = db.query(USER_TABLE, new String[] { "name", "age", "weight", "height", "gender", "activity_level" },
					"user_id=" + user_id, null, null, null, null);

			if (c.moveToFirst())
				user = new User(c.getString(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));

			c.close();
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		
		return user;
		
	}

	public boolean hasUser() {
		Cursor c = db.query(USER_TABLE, null, null, null, null, null, null);
		boolean hasUser = c.moveToFirst();
		c.close();
		return hasUser;
	}

	public ArrayList<BulletedText> getMealEntriesForDate(int userID, int date) {
		// Get all entries for a particular user and date
		// This WILL include duplicate entries referencing the same meal_item
		ArrayList<BulletedText> entries = new ArrayList<BulletedText>();
		try {
			// Query Entry table first for list of entry_id
			Cursor c = db.query(USER_ENTRY_TABLE, new String[] { "entry_id" },
					"date=" + date + " AND user_id=" + userID, null, null, null, null);

			int numRows = c.getCount();
			if (!c.moveToFirst())
				return entries;
			
			// Construct entry_id set for IN statement for next Query
			String entryIdSet = "(";
			for (int i = 0; i < numRows; i++) {
				if (i == (numRows - 1))
					entryIdSet += c.getInt(0);
				else
					entryIdSet += c.getInt(0) + ", ";
				
				c.moveToNext();
			}
			entryIdSet += ")";
			
			
			// Query Entry table first for list of entry_id
			c = db.query(ENTRY_TABLE, new String[] { "description", "type", "meal_item_id", "entry_id" },
					"entry_id IN " + entryIdSet, null, null, null, null);
			
			numRows = c.getCount();
			if (!c.moveToFirst())
				return entries;
			
			for (int i = 0; i < numRows; i++) {
				BulletedText entry = new BulletedText(context, c.getString(0), c.getInt(1), c.getInt(2), c.getInt(3));
				entries.add(entry);
				c.moveToNext();
			}
			c.close();
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		
		return entries;
	}
	
	public ArrayList<BulletedText> getMealEntriesForUser(int userID) {
		// Get all meal entries for a particular user and date
		// This will not count entries referencing the same meal_item
		HashSet<Integer> usedMealItems = new HashSet<Integer>();
		ArrayList<BulletedText> entries = new ArrayList<BulletedText>();
		try {
			// Query Entry table first for list of entry_id
			Cursor c = db.query(USER_ENTRY_TABLE, new String[] { "entry_id" },
					"user_id=" + userID, null, null, null, null);

			int numRows = c.getCount();
			if (!c.moveToFirst())
				return null;
			
			// Construct entry_id set for IN statement for next Query
			String entryIdSet = "(";
			for (int i = 0; i < numRows; i++) {
				if (i == (numRows - 1))
					entryIdSet += c.getInt(0);
				else
					entryIdSet += c.getInt(0) + ", ";
				
				c.moveToNext();
			}
			entryIdSet += ")";
			
			
			// Query Entry table first for list of entry_id
			c = db.query(ENTRY_TABLE, new String[] { "description", "type", "meal_item_id", "entry_id" },
					"entry_id IN " + entryIdSet, null, null, null, null);
			
			numRows = c.getCount();
			if (!c.moveToFirst())
				return null;
			
			for (int i = 0; i < numRows; i++) {
				// Get the meal_item_id first
				int meal_item_id = c.getInt(2);
				BulletedText entry = null;
				// Add the meal item only if it has not already been added
				if (!usedMealItems.contains(Integer.valueOf(meal_item_id)))
				{
					entry = new BulletedText(context, c.getString(0), c.getInt(1), meal_item_id, c.getInt(3));
					entries.add(entry);
					usedMealItems.add(Integer.valueOf(meal_item_id));
				}
				c.moveToNext();
			}
			c.close();
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		
		return entries;
	}
	
	public int getCaloriesForDate(int userID, int date)
	{
		int calories = 0;
		// Get all entries for a particular user and date
		ArrayList<BulletedText> entries = getMealEntriesForDate(userID, date);
		
		// Query the ENTRY and MEAL_ITEM tables on meal_item_id with the given entries
		// and select the sum(calories)
		
		// Construct entry_id set for IN statement for next Query
		int entriesSize = entries.size();
		String entryIdSet = "(";
		for (int i = 0; i < entriesSize; i++) {
			if (i == (entriesSize - 1))
				entryIdSet += entries.get(i).entry_id;
			else
				entryIdSet += entries.get(i).entry_id + ", ";
		}
		entryIdSet += ")";


		try {
			// Query Meal_Item and Entry table for sum of calories
			Cursor c = db.query(MEAL_ITEM_TABLE + ", " + ENTRY_TABLE, new String[] { "sum(meal_item.calories)" },
					"entry.meal_item_id=meal_item.meal_item_id AND entry.entry_id IN " + entryIdSet, null, null, null, null);

			if (!c.moveToFirst())
				return 0;

			calories = c.getInt(0);

			c.close();
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		
		return calories;
	}
	
	public double[] getNutrientsForDate(int userID, int date)
	{

		double[] sums = new double[6];
		
		// Get all entries for a particular user and date
		ArrayList<BulletedText> entries = getMealEntriesForDate(userID, date);
		
		// Query the ENTRY and MEAL_ITEM tables on meal_item_id with the given entries
		// and select the sums of the nutrients
		
		// Construct entry_id set for IN statement for next Query
		int entriesSize = entries.size();
		String entryIdSet = "(";
		for (int i = 0; i < entriesSize; i++) {
			if (i == (entriesSize - 1))
				entryIdSet += entries.get(i).entry_id;
			else
				entryIdSet += entries.get(i).entry_id + ", ";
		}
		entryIdSet += ")";


		try {
			// Query Meal_Item and Entry table for sum of calories
			Cursor c = db.query(MEAL_ITEM_TABLE + ", " + ENTRY_TABLE, new String[] { "sum(meal_item.protein)", "sum(meal_item.total_carb)", "sum(meal_item.fiber)", "sum(meal_item.total_fat)", "sum(meal_item.sat_fat)", "sum(meal_item.cholesterol)" },
					"entry.meal_item_id=meal_item.meal_item_id AND entry.entry_id IN " + entryIdSet, null, null, null, null);

			if (!c.moveToFirst())
				return sums;
			
			sums[0] = c.getDouble(0);
			sums[1] = c.getDouble(1);
			sums[2] = c.getDouble(2);
			sums[3] = c.getDouble(3);
			sums[4] = c.getDouble(4);
			sums[5] = c.getDouble(5);
			
			

			c.close();
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		
		return sums;
	}

	public int getCalorieTarget(int userID) {
		int calories = 2000;
		try {
			// Get long description of food item
			Cursor c = db.query(USER_TABLE, new String[] { "calorie_target" },
					"user_id=" + userID, null, null, null, null);

			if (c.moveToFirst())
				calories = c.getInt(0);

			c.close();
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		
		return calories;
	}

	public FoodItem getMealItem(int meal_item_id) {
		FoodItem food = new FoodItem();
		try {
			// Get long description of food item
			Cursor c = db.query(MEAL_ITEM_TABLE, new String[] { "calories", "protein", "total_fat", "sat_fat", "trans_fat", "total_carb", "fiber","sugar","cholesterol","sodium","potassium","vitaminA", "vitaminC","calcium","iron","meal_item_name", "quantity", "measure"},
					"meal_item_id=" + meal_item_id, null, null, null, null);

			if (!c.moveToFirst())
				return null;

			if (!c.isNull(0))
				food.nutrients.put(Codes.CALORIES, c.getDouble(0));
			if (!c.isNull(1))
				food.nutrients.put(Codes.PROTEIN, c.getDouble(1));
			if (!c.isNull(2))
				food.nutrients.put(Codes.TOTALFAT, c.getDouble(2));
			if (!c.isNull(3))
				food.nutrients.put(Codes.SATFAT, c.getDouble(3));
			if (!c.isNull(4))
				food.nutrients.put(Codes.TRANSFAT, c.getDouble(4));
			if (!c.isNull(5))
				food.nutrients.put(Codes.CARB, c.getDouble(5));
			if (!c.isNull(6))
				food.nutrients.put(Codes.FIBER, c.getDouble(6));
			if (!c.isNull(7))
				food.nutrients.put(Codes.TOTALSUGAR, c.getDouble(7));
			if (!c.isNull(8))
				food.nutrients.put(Codes.CHOLESTEROL, c.getDouble(8));
			if (!c.isNull(9))
				food.nutrients.put(Codes.SODIUM, c.getDouble(9));
			if (!c.isNull(10))
				food.nutrients.put(Codes.POTASSIUM, c.getDouble(10));
			if (!c.isNull(11))
				food.nutrients.put(Codes.VITAMINA, c.getDouble(11));
			if (!c.isNull(12))
				food.nutrients.put(Codes.VITAMINC, c.getDouble(12));
			if (!c.isNull(13))
				food.nutrients.put(Codes.CALCIUM, c.getDouble(13));
			if (!c.isNull(14))
				food.nutrients.put(Codes.IRON, c.getDouble(14));

			food.Long_Desc = c.getString(15);
			
			food.amount = c.getDouble(16);
			food.measure = c.getString(17);



			c.close();
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		return food;
	}

}
