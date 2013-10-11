package com.strawhead.android.dietribe.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.strawhead.android.dietribe.FoodItem;
import com.strawhead.android.dietribe.FoodWeight;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NutritionalDB {

	private static final String DATABASE_NAME = "usda.db";
	//private static final int DATABASE_VERSION = 1;

	public static final int BEVERAGES = 1;
	public static final int DAIRY = 2;
	public static final int FASTFOOD = 3;
	public static final int FRUITS = 4;
	public static final int GRAINS = 5;
	public static final int MEAT = 6;
	public static final int OILS = 7;
	public static final int SNACKS = 8;
	public static final int VEGETABLES = 9;
	
	private static final String FOOD_DES_TABLE = "FOOD_DES";
	private static final String NUT_DATA_TABLE = "NUT_DATA";
	private static final String WEIGHT_TABLE = "WEIGHT";
	
	
//	private static final String CREATE_TABLE_FOOD_DES = "create table food_des (NDB_No integer primary key, "
//        + "FdGrp_Cd integer not null, Long_Desc text not null, rating int null);";
//	
//	private static final String CREATE_TABLE_NUT_DATA = "create table nut_data (NDB_No integer not null, "
//        + "Nutr_No integer not null, Nutr_Val real not null, primary key(NDB_No, Nutr_No));";
//	
//	private static final String CREATE_TABLE_WEIGHT = "create table weight (NDB_No integer not null, " +
//			"Seq integer not null, Amount real not null, Msre_desc text not null, Gm_Wgt real not null, " +
//			"primary key(NDB_No, Seq));";
	
	
	
	
	private SQLiteDatabase db;
	private final Context context;

	public NutritionalDB(Context ctx)
	{
		context = ctx;
//		try
//		{
        	db = ctx.openOrCreateDatabase(DATABASE_NAME, 1, null);  
//        } catch (FileNotFoundException e) {
//        	try {
//        		// TODO Get database from internet and recreate?
//        		
//        		// Create new database
////        		db = ctx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0, null);
////        		db.execSQL(CREATE_TABLE_FOOD_DES);
////            	db.execSQL(CREATE_TABLE_NUT_DATA);
////            	db.execSQL(CREATE_TABLE_WEIGHT);
//            	
//            	//TODO Recreate database
//            	try {
//					unzipFiles();
//					
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//
//				db = ctx.openDatabase(DATABASE_NAME, null);
//            	
//        	} catch (FileNotFoundException e1) {
//        		//TODO Handle case where database can not be created at all
//        		db = null;
//        	}
//        }
	}

//	private void unzipFiles() throws IOException {
//		InputStream is = context.getResources().openRawResource(R.raw.usda);
//		ZipInputStream zis = new ZipInputStream(is);
//		ZipEntry ze;
//
//		while ((ze = zis.getNextEntry()) != null) {
//			writeFile(zis, ze.getName());
//		}
//	}
	
	private void writeFile(InputStream is, String filename) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		File file;
		String directory;

		filename = context.getDatabasePath("").getAbsolutePath() + "/" + filename;
		if (filename.lastIndexOf('/') != filename.length()-1) {
			directory = filename.substring(0, filename.lastIndexOf('/'));
			file = new File(directory);
			if (!file.exists()) file.mkdirs();
		} else {
			return;
		}
		OutputStream os = null;
		os = new FileOutputStream(filename);
		while ((read = is.read(buffer)) > 0) {
			os.write(buffer, 0, read);
		}
		os.close();
		is.close();
	}

	public boolean insertFoodItem(int NDB_No, int FdGrp_Cd, String Long_Desc)
	{
		ContentValues values = new ContentValues();
		values.put("NDB_No", NDB_No);
		values.put("FdGrp_Cd", FdGrp_Cd);
		values.put("Long_Desc", Long_Desc);
		return (db.insert(FOOD_DES_TABLE, null, values) > 0);
	}
	
	public int removeFoodItem(int NDB_No)
	{
		// Delete nutritional information as well as name from food_des_table
		return (db.delete(FOOD_DES_TABLE, "NDB_No=" + NDB_No, null)
				+ db.delete(NUT_DATA_TABLE, "NDB_No=" + NDB_No, null));
	}
	
	public boolean insertFoodItemNutrient(int NDB_No, int Nutr_No, double Nutr_Val)
	{
		ContentValues values = new ContentValues();
		values.put("NDB_No", NDB_No);
		values.put("Nutr_No", Nutr_No);
		values.put("Nutr_Val", Nutr_Val);
		return (db.insert(NUT_DATA_TABLE, null, values) > 0);
	}
	
	
	public List<FoodItem> getMealItems(String searchTerm, int foodGroup) {
		ArrayList<FoodItem> articles = new ArrayList<FoodItem>();

		if (searchTerm != null && !searchTerm.trim().equals(""))
    		searchTerm = "%" + assembleSearchTerm(searchTerm) + "%";
		else
			searchTerm = null;
		
		//Construct Selection statement for query
		String selection = searchTerm != null ? "Long_Desc LIKE '" + searchTerm + "'"
				: "Long_Desc LIKE '%'";
		
		// If Food Group is set to something other than All Food Groups, add it to query
		switch(foodGroup)
		{
		case BEVERAGES:
			selection += " AND FdGrp_CD=1400";
			break;
		case FASTFOOD:
			selection += " AND FdGrp_CD=2100";
			break;
		case DAIRY:
			selection += " AND FdGrp_CD=100";
			break;
		case FRUITS:
			selection += " AND FdGrp_CD=900";
			break;
		case GRAINS:
			selection += " AND FdGrp_CD IN " + "(800, 1800, 2000)";
			break;
		case MEAT:
			selection += " AND FdGrp_CD IN " + "(500, 700, 1000, 1200, 1300, 1500, 1600, 1700)";
			break;
		case VEGETABLES:
			selection += " AND FdGrp_CD IN " + "(200, 1100)";
			break;
		case OILS:
			selection += " AND FdGrp_CD IN " + "(400, 600)";
			break;
		case SNACKS:
			selection += " AND FdGrp_CD IN " + "(1900, 2500)";
			break;
		default:
			break;
		}
			
		
		
		try {
			// Get long description of food item
			Cursor c = db.query(FOOD_DES_TABLE, new String[] { "NDB_No", "Long_Desc", "FdGrp_CD" },
					selection, null, null, null, "rating limit 250");

			int numRows = c.getCount();
			c.moveToFirst();
				
			for (int i = 0; i < numRows; i++) {
				FoodItem article = new FoodItem();
				article.NDB_No = c.getInt(0);
				article.Long_Desc = c.getString(1);
				article.FdGrp_Cd = c.getInt(2);
				articles.add(article);
				c.moveToNext();
			}
			c.close();
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		
		return articles;
	}
	
	private String assembleSearchTerm(String searchTerm) {
		//TODO Change this special logic to better handle Fast Food names with apostrophes
		if (searchTerm.equalsIgnoreCase("McDonalds"))
			return "McDonald''s";
		else if (searchTerm.equalsIgnoreCase("Dominos Pizza"))
			return "Domino";
		else if (searchTerm.equalsIgnoreCase("Papa Johns Pizza"))
			return "Papa John";
		else if (searchTerm.equalsIgnoreCase("Wendys"))
			return "Wendy''s";
		// Replace any single quotes with two single quotes to get correct results
		return searchTerm.replace("'", "''");
	}

	public List<FoodWeight> getServingOptions(int NDB_No)
	{
		ArrayList<FoodWeight> foodWeights = new ArrayList<FoodWeight>();
		
		// Add default "100g edible portion" measure to the list
		foodWeights.add(new FoodWeight("100g edible portion", 1.0, 100));
		
		// Select Measures (Serving Sizes) from the Weight Table
		try 
		{
			Cursor c = db.query(WEIGHT_TABLE, new String[] { "Msre_Desc", "Amount", "Gm_Wgt" },
					"NDB_No=" + NDB_No, null, null, null, "Gm_Wgt asc");

			int numRows = c.getCount();
			c.moveToFirst();
				
			for (int i = 0; i < numRows; i++) {
				FoodWeight weight = new FoodWeight(c.getString(0), c.getDouble(1), c.getDouble(2));
				foodWeights.add(weight);
				c.moveToNext();
			}
			c.close();
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		return foodWeights;
	}
	
	public FoodItem getFoodItem(int NDB_No)
	{
		FoodItem foodItem = new FoodItem();
		foodItem.NDB_No = NDB_No;
		try {
			// Query NUT_DATA table to populate FoodItem nutrition information
			Cursor c = db.query(NUT_DATA_TABLE, new String[] { "Nutr_No", "Nutr_Val" },
					"NDB_No=" + NDB_No, null, null, null, null);

			int numRows = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < numRows; i++) {
				//Depending on the Nutr_No, fill the appropriate FoodItem field
				foodItem.nutrients.put(c.getInt(0), c.getDouble(1));
				c.moveToNext();
			}
			c.close();
			
		} catch (SQLException e) {
			Log.e("Dietribe", e.toString());
		}
		return foodItem;
	}

}
