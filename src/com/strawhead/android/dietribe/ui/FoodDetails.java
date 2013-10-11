package com.strawhead.android.dietribe.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.example.helloandroid.R;
import com.strawhead.android.dietribe.Codes;
import com.strawhead.android.dietribe.FoodItem;
import com.strawhead.android.dietribe.FoodWeight;
import com.strawhead.android.dietribe.database.UserDB;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class FoodDetails extends Activity implements OnItemSelectedListener {

	WebView webView;
	EditText qty;
	
	
	DecimalFormat df = new DecimalFormat("#,###");
	DecimalFormat df_fraction = new DecimalFormat("###.#");
	
	private FoodItem foodItem = new FoodItem();


	private int caloriesfromfat = -1;

	private int satfatPercent;
	private int totalfatPercent;
	private int sodiumPercent;
	private int dietaryfiberPercent;
	private int cholesterolPercent;
	private int totalCarbPercent;
	private int vitaminAPercent;
	private int vitaminCPercent;
	private int calciumPercent;
	private int ironPercent;


	private int NDB_No;
	private int userID;
	private int date;
	private int entryID;
	
	//private UserDB nutDB;

	private List<FoodWeight> servingOptions;

	private FoodWeight foodWeight;
	//TODO Default DRI values from based on label guidelines
	private int totalCalories = 2000;
	private int satFatDRI = 20;
	private int fiberDRI = 25;
	private int totalFatDRI = 65;
	private int sodiumDRI = 2400;
	private int cholesterolDRI = 300;
	private int totalCarbDRI = 300;
	private int calciumDRI = 1000;
	private int ironDRI = 18;
	private int vitADRI = 900;
	private int vitCDRI = 90;
	
	private int calories = -1;
	private double totalFat = -1;
	private double satFat = -1;
	private double dietaryfiber = -1;
	private double sodium = -1;
	private double cholesterol = -1;
	private double totalCarb = -1;
	private double protein = -1;
	private double transFat = -1;
	private double potassium = -1;
	private double sugars = -1;
	private double iron = -1;
	private double calcium = -1;
	private double vitaminA = -1;
	private double vitaminC = -1;
	

	
	private UserDB userDB;
	private String mealItemName;
	private int FdGrp_Cd;
	private String quantity;
	private double amount = 0.0;
	private Button editItemButton;
	private static final int MENU_ITEM_EDIT = 0;
	private static final int MENU_ITEM_DELETE = 1;

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ITEM_EDIT , 0, R.string.edit)
                .setIcon(android.R.drawable.ic_menu_edit)
                .setAlphabeticShortcut('e');
        menu.add(0, MENU_ITEM_DELETE, 0, R.string.remove)
                .setIcon(android.R.drawable.ic_menu_delete);

        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_EDIT:
            	editItemButton.performClick();
                return true;
    
            case MENU_ITEM_DELETE:
            	// TODO Confirmation dialog?
                userDB.removeUserEntryItem(userID, date, entryID);
                setResult(RESULT_OK, getIntent());
				finish();
                return true;

        }

        return false;
    }

	/**
	 * Called with the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setTheme(android.R.style.Theme);
		setContentView(R.layout.fooddetails);

		userDB = new UserDB(this);

		// Get NDB_No and user_id passed in...
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			NDB_No = extras.getInt("com.strawhead.android.dietribe.meal_item_id");
			mealItemName = extras.getString("com.strawhead.android.dietribe.Long_Desc");
			FdGrp_Cd = extras.getInt("com.strawhead.android.dietribe.FdGrp_Cd");
			date = extras.getInt("com.strawhead.android.dietribe.date");
			userID = extras.getInt("com.strawhead.android.dietribe.userID");
			entryID = extras.getInt("com.strawhead.android.dietribe.entryID");
		}

		//TODO If userID was passed in, update DRI from userDB 
		if (userID > 0)
		{
//			totalCalories = 2000;
//			satFatDRI = 20;
//			fiberDRI = 25;
//			totalFatDRI = 65;
//			sodiumDRI = 2400;
//			cholesterolDRI = 300;
//			totalCarbDRI = 300;
//			calciumDRI = 1000;
//			ironDRI = 18;
//			vitADRI = 900;
//			vitCDRI = 90;
		}

		// TODO Get Serving Options for Spinner control
		servingOptions = new ArrayList<FoodWeight>();
		
		// Add default "100g edible portion" measure to the list
		foodWeight = new FoodWeight("100g edible portion", 1.0, 100.0);
		servingOptions.add(foodWeight);
		//servingOptions = nutDB.getServingOptions(NDB_No);

		// Get FoodItem
		foodItem = userDB.getMealItem(NDB_No);

		


		// Update textview to indicate which Meal Item
		TextView foodName = (TextView) findViewById(R.id.name_label);
		foodName.setText(mealItemName);
		qty = (EditText) findViewById(R.id.qty);
		qty.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

					// If the quantity represents a valid number, reload the
					// Nutrition Facts label
					try
					{
						quantity = qty.getText().toString();
						amount = Double.valueOf(quantity);
						webView.loadData(generateNutritionFacts(), "text/html", "utf-8");
						return false;
					}
					catch (NumberFormatException nfe)
					{
						// If the quantity value is invalid, just return
						return false;
					}
					catch (Exception e) {
						return false;
					}

			}
		});

		// Get the WebView to use for the Nutrition Facts label
		webView = (WebView) findViewById(R.id.webView);
		

		Button addItemButton = (Button)findViewById(R.id.addbutton);
		addItemButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Add to UserDB and finish
				// Add item to User Database
				long meal_item_id = userDB.insertMealItem(mealItemName, foodWeight.Msre_Desc, amount, FdGrp_Cd, getNutrients());
				long entry_id = userDB.insertEntry(mealItemName + " " + amount + " " + foodWeight.Msre_Desc, FdGrp_Cd, meal_item_id);
				userDB.insertUserEntryItem(userID, date, entry_id);
				setResult(RESULT_OK, getIntent());
				finish();
			}
		});



		editItemButton = (Button)findViewById(R.id.editbutton);
		editItemButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// On Edit, go to Manual Entry view with passed-in extras to populate
				Intent intent = new Intent();
				// Pass EXTRA info, NBR_No and user_id
				intent.setClass(FoodDetails.this, NutritionalFacts.class);
				intent.putExtra("com.strawhead.android.dietribe.meal_item_id", NDB_No);
				intent.putExtra("com.strawhead.android.dietribe.userID", userID);
				intent.putExtra("com.strawhead.android.dietribe.date", date);
				startActivity(intent);

			}
		});

		// Serving Type Spinner
		Spinner s1 = (Spinner) findViewById(R.id.servingsize);
		ArrayAdapter<FoodWeight> adapter = new ArrayAdapter<FoodWeight>(this,
				android.R.layout.simple_spinner_item, servingOptions);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s1.setAdapter(adapter);
		s1.setOnItemSelectedListener(this);
		if (servingOptions.size() > 1)
			s1.setSelection(1);


		// Make sure amount is initialized
		amount = Double.valueOf(qty.getText().toString());
		// Generate Nutrition Facts label in WebView
		webView.loadData(generateNutritionFacts(), "text/html", "utf-8");

	}

	protected HashMap<Integer, Double> getNutrients() {
		HashMap<Integer, Double> nutrients = new HashMap<Integer, Double>();
		if (calories >= 0)
			nutrients.put(Codes.CALORIES, (double) calories);
		if (protein >= 0)
			nutrients.put(Codes.PROTEIN, protein);
		if (totalFat >= 0)
			nutrients.put(Codes.TOTALFAT, totalFat);
		if (satFat >= 0)
			nutrients.put(Codes.SATFAT, satFat);
		if (transFat >= 0)
			nutrients.put(Codes.TRANSFAT, transFat);
		if (totalCarb >= 0)
			nutrients.put(Codes.CARB, totalCarb);
		if (dietaryfiber >= 0)
			nutrients.put(Codes.FIBER, dietaryfiber);
		if (sugars >= 0)
			nutrients.put(Codes.TOTALSUGAR, sugars);
		if (cholesterol >= 0)
			nutrients.put(Codes.CHOLESTEROL, cholesterol);
		if (sodium >= 0)
			nutrients.put(Codes.SODIUM, sodium);
		if (potassium >= 0)
			nutrients.put(Codes.POTASSIUM, potassium);
		if (vitaminA >= 0)
			nutrients.put(Codes.VITAMINA, vitaminA);
		if (vitaminC >= 0)
			nutrients.put(Codes.VITAMINC, vitaminC);
		if (calcium >= 0)
			nutrients.put(Codes.CALCIUM, calcium);
		if (iron >= 0)
			nutrients.put(Codes.IRON, iron);

		return nutrients;
	}

	public String generateNutritionFacts()
	{
		// Get resource instance for string values to insert into Nutrition Facts label
		Resources res = getResources();
		
		Set<Integer> keys = foodItem.nutrients.keySet();
    	for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
    		int key = iterator.next();
    		double value = foodItem.nutrients.get(key);
    		//Calculate current values of nutrients taking into account the quantity and measure
			switch(key)
			{
			case Codes.CALORIES:
				calories = (int) Math.round((amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value);
				break;
			case Codes.TOTALFAT:
				totalFat =  (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				// Calculation: Total calories from Fat = Total Fat grams * 9
				caloriesfromfat = (int) (totalFat >= 0 ? Math.round(totalFat * 9) : -1);
				totalfatPercent = (int) Math.round(100 * totalFat / totalFatDRI);
				break;
			case Codes.SATFAT:
				satFat = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				satfatPercent = (int) Math.round(100 * satFat / satFatDRI);
				break;
			case Codes.TRANSFAT:
				transFat = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				break;
			case Codes.CARB:
				totalCarb = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				totalCarbPercent = (int) Math.round(100 * totalCarb / totalCarbDRI);
				break;
			case Codes.TOTALSUGAR:
				sugars = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				break;
			case Codes.FIBER:
				dietaryfiber = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				dietaryfiberPercent = (int) Math.round(100 * dietaryfiber / fiberDRI);
				break;
			case Codes.SODIUM:
				sodium = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				sodiumPercent = (int) Math.round(100 * sodium / sodiumDRI);
				break;
			case Codes.CHOLESTEROL:
				cholesterol = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				cholesterolPercent = (int) Math.round(100 * cholesterol / cholesterolDRI);
				break;
			case Codes.POTASSIUM:
				//TODO Include Potassium in LABEL!!
				potassium = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				break;
			case Codes.PROTEIN:
				protein = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				break;
			case Codes.VITAMINA:
				vitaminA = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100.0) * value;
				vitaminAPercent = (int) Math.round(100 * vitaminA / vitADRI);
				break;
			case Codes.VITAMINC:
				vitaminC = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100.0) * value;
				vitaminCPercent = (int) Math.round(100 * vitaminC / vitCDRI);
				break;
			case Codes.CALCIUM:
				calcium = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				calciumPercent = (int) Math.round(100 * calcium / calciumDRI);
				break;
			case Codes.IRON:
				iron = (amount / foodWeight.Amount) * (foodWeight.Gm_Wgt/100) * value;
				ironPercent = (int) Math.round(100 * iron / ironDRI);
				break;
			}
    	}
			

		
		
		
		
		
		
		
		
		

		

		//Generate Header
		String nutInfo = "<html><body><table style=\"border: 1px solid rgb(0, 0, 0); padding: 3px; width: 230px; font-size: 14px;\" cellpadding=\"0\" cellspacing=\"0\">"
			+ "<tbody><tr><td colspan=\"2\">"
			+ "<span style=\"font-size: 20px; font-weight: 900; letter-spacing: 2px;\">" + res.getText(R.string.nutrition_facts) + "<span>"
			+ "</span></span></td></tr>";

		//Generate Calories
		//Catch case where Calories are null and display empty string
		if (calories >= 0)
		{
			nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
				+ "<span style=\"font-weight: 900;\">" + res.getText(R.string.calories) + " </span><span id=\"calories\"> " + calories + "</span></td>";
			if (caloriesfromfat >= 0)
				nutInfo += "<td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">"
					+ "Cal from Fat <span id=\"caloriesfromfat\"> " + caloriesfromfat + "</span></td>";
			nutInfo += "</tr>";
		}

		// Finish Header with Percent Daily Value
		nutInfo += "<tr><td style=\"border-top: 4px solid rgb(0, 0, 0); text-align: right;\" colspan=\"2\">"
			+ "<span style=\"font-weight: 900;\">" + res.getText(R.string.percent_dv) +"</span></td></tr>";

		// If fats data exists, include it
		if (totalFat >= 0)
			nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
				+ "<span style=\"font-weight: 900;\">" + res.getText(R.string.totalfat) + "</span><span id=\"totalfat\"> " + df.format(totalFat) + "g</span></td>"
				+ " <td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">"
				+ "<span id=\"totalfatpercent\" style=\"font-weight: 900;\">" + totalfatPercent + "</span></td></tr>";

		// If Saturated Fat or Trans Fat is indicated, display it, otherwise do not display
		if (satFat >= 0 || transFat >= 0)
		{
			nutInfo += "<tr><td style=\"padding-left: 5px;\" colspan=\"2\">"
				+  "<table style=\"width: 220px; font-size: 14px;\" cellpadding=\"0\" cellspacing=\"0\"><tbody>";

			if (satFat >= 0)
				nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
					+ res.getText(R.string.satfat) + "<span id=\"saturatedfat\"> " + df_fraction.format(satFat) + "g</span></td>"
					+ "<td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">"
					+ "<span id=\"saturatedfatpercent\" style=\"font-weight: 900;\">" + satfatPercent + "</span></td></tr>";
			if (transFat >= 0)
				nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
					+ res.getText(R.string.transfat) + "<span id=\"transfat\"> " + df_fraction.format(transFat) + "g</span></td>"
					+ "<td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">&nbsp;</td></tr>";

			nutInfo += "</tbody></table></td></tr>";
		}

		// If cholesterol, display it
		if (cholesterol >= 0)
			nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
				+ "<span style=\"font-weight: 900;\">" + res.getText(R.string.cholesterol) + "</span><span id=\"cholesterol\"> " + df.format(cholesterol) + "mg</span></td>"
				+ "<td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">"
				+ "<span id=\"cholesterolpercent\" style=\"font-weight: 900;\">" + cholesterolPercent + "</span></td></tr>";

		// If sodium, display it
		if (sodium >= 0)
			nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
				+ "<span style=\"font-weight: 900;\">" + res.getText(R.string.sodium) + "</span><span id=\"sodium\"> " + df.format(sodium) + "mg</span></td>"
				+ "<td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">"
				+ "<span id=\"sodiumpercent\" style=\"font-weight: 900;\">" + sodiumPercent + "</span></td></tr>";
		//If carbs data exists, display it
		if (totalCarb >= 0)
			nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
				+     "<span style=\"font-weight: 900;\">" + res.getText(R.string.totalcarb) + " </span><span id=\"totalcarbohydrate\"> " + df.format(totalCarb) + "g</span>"
				+   "</td><td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">"
				+     "<span id=\"totalcarbohydratepercent\" style=\"font-weight: 900;\">" + totalCarbPercent + "</span></td></tr>";
		if (dietaryfiber >= 0 || sugars >= 0) {
			nutInfo += "<tr><td style=\"padding-left: 5px;\" colspan=\"2\">"
				+ "<table style=\"width: 220px; font-size: 14px;\" cellpadding=\"0\" cellspacing=\"0\"><tbody>";

			if (dietaryfiber >= 0)
				nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
					+           res.getText(R.string.dietaryfiber) + "<span id=\"diataryfiber\"> " + df.format(dietaryfiber) + "g</span></td>"
					+         "<td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">"
					+           "<span id=\"diataryfiberpercent\" style=\"font-weight: 900;\">" + dietaryfiberPercent + "</span></td></tr>";
			if (sugars >= 0)	
				nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
					+           res.getText(R.string.sugars) + "<span id=\"sugars\"> " + df.format(sugars) + "g</span></td>"
					+         "<td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">&nbsp;</td></tr>";

			nutInfo += "</tbody></table></td></tr>";
		}
		//If protein, display it
		if (protein >= 0)
			nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">"
				+     "<span style=\"font-weight: 900;\">" + res.getText(R.string.protein) + " </span><span id=\"protein\">" + df.format(protein) + "g</span></td>"
				+   "<td style=\"border-top: 1px solid rgb(0, 0, 0); text-align: right;\">&nbsp;</td></tr>";
		//If Vitamins...
		if (vitaminA >= 0 || vitaminC >= 0 || calcium >= 0 || iron >= 0)
			nutInfo += "<tr><td style=\"border-top: 4px solid rgb(0, 0, 0);\" colspan=\"2\">"
				+     "<table style=\"width: 220px; font-size: 14px;\" cellpadding=\"0\" cellspacing=\"0\">"
				+       "<tbody><tr>"
				+         "<td>" + res.getText(R.string.vitaminA) + "</td>"
				+         "<td><span id=\"vitaminapercent\">" + vitaminAPercent + "</span></td>"
				+         "<td style=\"padding: 0pt 5px;\">•</td>"
				+         "<td>"  + res.getText(R.string.vitaminC) + "</td>"
				+         "<td><span id=\"vitamincpercent\">" + vitaminCPercent + "</span></td></tr>"
				+       "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\">" + res.getText(R.string.calcium) + "</td>"
				+         "<td style=\"border-top: 1px solid rgb(0, 0, 0);\"><span id=\"calciumpercent\">" + calciumPercent + "</span></td>"
				+         "<td style=\"border-top: 1px solid rgb(0, 0, 0); padding: 0pt 5px;\">•</td>"
				+         "<td style=\"border-top: 1px solid rgb(0, 0, 0);\">" + res.getText(R.string.iron) + "</td>"
				+         "<td style=\"border-top: 1px solid rgb(0, 0, 0);\"><span id=\"ironpercent\">" + ironPercent + "</span></td></tr></tbody></table></td></tr>";

		// Disclaimer
		nutInfo += "<tr><td style=\"border-top: 1px solid rgb(0, 0, 0);\" colspan=\"2\">"
			+     "<table style=\"width: 220px; font-size: 14px;\" cellpadding=\"0\" cellspacing=\"0\">"
			+       "<tbody><tr><td style=\"text-align: center; vertical-align: top; width: 10px;\">*</td>"
			+         "<td style=\"width: 210px;\">Percent Daily Values are based on a " + totalCalories  + " calorie diet.</td></tr>"
			+     "</tbody></table></td></tr>";

		// TODO Change Ingredients row to include Good/Bad analysis of food
//		+ "<tr><td style=\"border-top: 4px solid rgb(0, 0, 0);\" colspan=\"2\">"
//		+  "<span style=\"font-weight: 900;\">INGREDIENTS: </span><span id=\"ingredients\">Rice</span></td></tr>";


		// Close tables
		nutInfo += "</tbody></table></body></html>";

		return nutInfo;
	}

	@SuppressWarnings("unchecked")
	public void onItemSelected(AdapterView parent, View v, int position, long id)
	{
		//TODO See if this works...
		if (parent != null)
		{
			foodWeight = (FoodWeight) parent.getItemAtPosition(position);
			if (foodWeight != null)
			{
				if (qty != null)
					qty.setText(foodWeight.Amount + "");
				amount = foodWeight.Amount;
				webView.loadData(generateNutritionFacts(), "text/html", "utf-8");
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void onNothingSelected(AdapterView arg0) {
		// TODO Auto-generated method stub

	}
}