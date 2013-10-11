package com.strawhead.android.dietribe.ui;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.strawhead.android.dietribe.Codes;
import com.strawhead.android.dietribe.Dietribe;
import com.strawhead.android.dietribe.FoodItem;
import com.example.helloandroid.R;
import com.strawhead.android.dietribe.database.UserDB;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>This example shows how to use cell spanning in a table layout.</p>
 */
public class NutritionalFacts extends Activity implements OnClickListener {

	private static final int DIALOG_NO_NAME = 1;
    private static final int DIALOG_NO_NUTRIENTS = 2;
    private static final int DELETE_CONFIRMATION_DIALOG = 3;
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    
    // Menu item IDs
    public static final int MENU_ITEM_SAVE = 1;
    public static final int MENU_ITEM_DONT_SAVE = 2;
    public static final int MENU_ITEM_DELETE = 3;
    public static final int MENU_ITEM_ADD = 5;
    
	DecimalFormat df = new DecimalFormat("###.#");
	private Button saveButton;
	private Button discardButton;
	private Button moreInfoButton;
	private UserDB userDB;
	private EditText mCalories;
	private EditText mTotalFat;
	private EditText mSatFat;
	private EditText mTransFat;
	private EditText mTotalCarb;
	private EditText mFiber;
	private EditText mSugars;
	private EditText mCholesterol;
	private EditText mSodium;
	private EditText mPotassium;
	private EditText mProtein;
	private EditText mVitaminA;
	private EditText mVitaminC;
	private EditText mCalcium;
	private EditText mIron;
	private EditText mMealItemName;
	private EditText mAmount;
	private int userID;
	private int date;
	private int mealItemID;
	private TextView mMeasureName;
	private boolean editMode;
	private int iconId;
	private boolean mPhotoChanged;
	private ImageView mPhotoImageView;
	private Button mPhotoButton;
	private boolean mPhotoPresent;
	
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photoButton:
		case R.id.photoImage:
			// Show ImageGrid of icons available
			Intent intent = new Intent();
			intent.setClass(NutritionalFacts.this, IconGrid.class);
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
			
			//TODO The following is the action used by the Contacts app to grab pics from the SD card.
			//TODO Perhaps, this could be included as an option
//			//doPickPhotoAction();
//	        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//	        // TODO: get these values from constants somewhere
//	        intent.setType("image/*");
//	        intent.putExtra("crop", "true");
//	        intent.putExtra("aspectX", 1);
//	        intent.putExtra("aspectY", 1);
//	        intent.putExtra("outputX", 96);
//	        intent.putExtra("outputY", 96);
//	        try {
//	            intent.putExtra("return-data", true);
//	            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
//	        } catch (ActivityNotFoundException e) {
//	            new AlertDialog.Builder(NutritionalFacts.this)
//	                .setTitle(R.string.errorDialogTitle)
//	                .setMessage(R.string.photoPickerNotFoundText)
//	                .setPositiveButton(R.string.okButtonText, null)
//	                .show();
//	        }
			break;
		case R.id.discardButton:
			finish();
			break;
		case R.id.saveButton:
			// Save Button will act accordingly depending on whether it is editing or adding
			if (editMode)
			{
				// Update item in the User Database
				userDB.updateMealItem(mealItemID, getNutrients());
				setResult(RESULT_OK, getIntent());
				finish();
			}
			else {
				// Add item to User Database
				//TODO Pull information on serving type and amount ???
				String mealname = mMealItemName.getText().toString();
				HashMap<Integer, Double> nutrients = getNutrients();
				if (mealname.equals(""))
					showDialog(DIALOG_NO_NAME);
				else if (nutrients.isEmpty())
					showDialog(DIALOG_NO_NUTRIENTS);
				else 
				{
					//TODO Support changing FoodGroup, serving quantity, and perhaps other items
					//such as its rating
					long meal_item_id = userDB.insertMealItem(mealname + iconId, "serving", 1, 2200, getNutrients());
					long entry_id = userDB.insertEntry(mealname, 0, meal_item_id);
					userDB.insertUserEntryItem(userID, date, entry_id);
					setResult(RESULT_OK, getIntent());
					finish();
				}
			}
			break;
		case R.id.addMore:
			//TODO Add logic to add additional fields
			break;
		}

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ITEM_SAVE, 0, R.string.save)
                .setIcon(android.R.drawable.ic_menu_save)
                .setAlphabeticShortcut('\n');
        menu.add(0, MENU_ITEM_DONT_SAVE, 0, R.string.discard)
                .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setAlphabeticShortcut('q');
        if (editMode) {
            menu.add(0, MENU_ITEM_DELETE, 0, R.string.delete_entry)
                    .setIcon(android.R.drawable.ic_menu_delete);
        }

        menu.add(0, MENU_ITEM_ADD, 0, R.string.moreinfo)
                .setIcon(android.R.drawable.ic_menu_add)
                .setAlphabeticShortcut('n');

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_SAVE:
            	saveButton.performClick();
                return true;
    
            case MENU_ITEM_DONT_SAVE:
                discardButton.performClick();
                return true;
    
            case MENU_ITEM_DELETE:
                // Get confirmation
                showDialog(DELETE_CONFIRMATION_DIALOG);
                return true;
    
            case MENU_ITEM_ADD:
                moreInfoButton.performClick();
                return true;
        }

        return false;
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA: {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    iconId = extras.getInt("icon");
                    //TODO Change icon of the button to show the icon
                    mPhotoChanged = true;
                    mPhotoImageView.setImageResource(mThumbIds[iconId]);
                    setPhotoPresent(true);
                }
                break;
            }
        }
	}
	
	private void setPhotoPresent(boolean present) {
		mPhotoImageView.setVisibility(present ? View.VISIBLE : View.GONE);
        mPhotoButton.setVisibility(present ? View.GONE : View.VISIBLE);
        mPhotoPresent = present;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
        case DIALOG_NO_NAME:
        	return new AlertDialog.Builder(NutritionalFacts.this)
            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(R.string.missing_name)
            .setMessage(R.string.missing_name_msg)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                }
            })
            .create();
        case DIALOG_NO_NUTRIENTS:
        	return new AlertDialog.Builder(NutritionalFacts.this)
            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(R.string.missing_nutrients)
            .setMessage(R.string.missing_nutrients_msg)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                }
            })
            .create();
        case DELETE_CONFIRMATION_DIALOG:
        	return new AlertDialog.Builder(NutritionalFacts.this)
            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(R.string.delete_entry)
            .setMessage(R.string.delete_entry_msg)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                }
            })
            .create();
		}
		
		return null;
		
	}

	@Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.nutritionfacts);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            date = extras.getInt("com.strawhead.android.dietribe.date");
        	userID = extras.getInt("com.strawhead.android.dietribe.userID");
        	mealItemID = extras.getInt("com.strawhead.android.dietribe.meal_item_id");
        }
        if (mealItemID > 0)
        	editMode = true;
        userDB = new UserDB(this);
        
        
        // Set up header with Icon and Food Name
        mPhotoImageView = (ImageView) findViewById(R.id.photoImage);
        mPhotoImageView.setOnClickListener(this);
        mPhotoImageView.setVisibility(View.GONE);
        mPhotoButton = (Button) findViewById(R.id.photoButton);
        mPhotoButton.setOnClickListener(this);
        
        
        mMealItemName = (EditText) findViewById(R.id.meal_item_name);
        mAmount = (EditText) findViewById(R.id.manual_entry_amount);
        mMeasureName = (TextView) findViewById(R.id.serving_name);
        // EditViews for each nutrient
        mCalories = (EditText) findViewById(R.id.calories);
        mTotalFat = (EditText) findViewById(R.id.totalfat);
        mSatFat = (EditText) findViewById(R.id.satfat);
        mTransFat = (EditText) findViewById(R.id.transfat);
        mTotalCarb = (EditText) findViewById(R.id.totalcarb);
        mFiber = (EditText) findViewById(R.id.fiber);
        mSugars = (EditText) findViewById(R.id.sugars);
        mCholesterol = (EditText) findViewById(R.id.cholesterol);
        mSodium = (EditText) findViewById(R.id.sodium);
        mPotassium = (EditText) findViewById(R.id.potassium);
        mProtein = (EditText) findViewById(R.id.protein);
        mVitaminA = (EditText) findViewById(R.id.vitaminA);
        mVitaminC = (EditText) findViewById(R.id.vitaminC);
        mCalcium = (EditText) findViewById(R.id.calcium);
        mIron = (EditText) findViewById(R.id.iron);
        
        // Hide most of the EditViews by default
        mSatFat.setVisibility(View.GONE);
        mTransFat.setVisibility(View.GONE);
        mFiber.setVisibility(View.GONE);
        mSugars.setVisibility(View.GONE);
        mCholesterol.setVisibility(View.GONE);
        mSodium.setVisibility(View.GONE);
        mPotassium.setVisibility(View.GONE);
        mVitaminA.setVisibility(View.GONE);
        mVitaminC.setVisibility(View.GONE);
        mCalcium.setVisibility(View.GONE);
        mIron.setVisibility(View.GONE);
        
        // Watch for button clicks.
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        
        discardButton = (Button) findViewById(R.id.discardButton);
        discardButton.setOnClickListener(this);
        
        moreInfoButton = (Button) findViewById(R.id.addMore);
        moreInfoButton.setOnClickListener(this);
        		
        
        
        //TODO Decide what control to use for Food Group / Measurement selection in a manual edit
        // The best way is probably to do it as a floating dialog that can pop up via Menu
        
        
//        foodGroupSpinner = (Spinner) findViewById(R.id.manual_foodgroups);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.food_group_items));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        foodGroupSpinner.setAdapter(adapter);
        
//        // AutoComplete in Food Group
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, FOOD_GROUPS);
//        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.food_group);
//        textView.setAdapter(adapter);
        
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, MEASURES);
//        AutoCompleteTextView textView1 = (AutoCompleteTextView) findViewById(R.id.measure);
//        textView1.setAdapter(adapter1);
        
        
        // If we are in edit mode, populate all the fields with their values from the DB
        if (editMode)
        {
        	FoodItem food = userDB.getMealItem(mealItemID);
        	populateFields(food);
        }
    }
    
    private void populateFields(FoodItem food) {
    	mMealItemName.setText(food.Long_Desc);
    	mAmount.setText("" + food.amount);
    	mMeasureName.setText(food.measure);
    	
    	Set<Integer> keys = food.nutrients.keySet();
    	for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
    		int key = iterator.next();
    		Double value = food.nutrients.get(key);
    		//TODO: Parse the value to shorten decimal places
    		String nutrientValue = df.format(value);
			switch(key)
			{
			case Codes.PROTEIN:
				mProtein.setText(nutrientValue);
				break;
			case Codes.SODIUM:
				mSodium.setText(nutrientValue);
				break;
			case Codes.POTASSIUM:
				mPotassium.setText(nutrientValue);
				break;
			case Codes.SATFAT:
				mSatFat.setText(nutrientValue);
				break;
			case Codes.TRANSFAT:
				mTransFat.setText(nutrientValue);
				break;
			case Codes.TOTALFAT:
				mTotalFat.setText(nutrientValue);
				break;
			case Codes.CALORIES:
				mCalories.setText(nutrientValue);
				break;
			case Codes.CARB:
				mTotalCarb.setText(nutrientValue);
				break;
			case Codes.FIBER:
				mFiber.setText(nutrientValue);
				break;
			case Codes.TOTALSUGAR:
				mSugars.setText(nutrientValue);
				break;
			case Codes.CHOLESTEROL:
				mCholesterol.setText(nutrientValue);
				break;
			case Codes.VITAMINA:
				mVitaminA.setText(nutrientValue);
				break;
			case Codes.VITAMINC:
				mVitaminC.setText(nutrientValue);
				break;
			case Codes.IRON:
				mIron.setText(nutrientValue);
				break;
			case Codes.CALCIUM:
				mCalcium.setText(nutrientValue);
				break;
			default:
				break;
			
			}
			
		}
    	
	}

	
	protected HashMap<Integer, Double> getNutrients() {
		HashMap<Integer, Double> nutrients = new HashMap<Integer, Double>();

		if (!mCalories.getText().toString().equals(""))
			nutrients.put(Codes.CALORIES, Double.parseDouble(mCalories.getText().toString()));
		if (!mProtein.getText().toString().equals(""))
			nutrients.put(Codes.PROTEIN, Double.parseDouble(mProtein.getText().toString()));
		if (!mTotalFat.getText().toString().equals(""))
			nutrients.put(Codes.TOTALFAT, Double.parseDouble(mTotalFat.getText().toString()));
		if (!mSatFat.getText().toString().equals(""))
			nutrients.put(Codes.SATFAT, Double.parseDouble(mSatFat.getText().toString()));
		if (!mTransFat.getText().toString().equals(""))
			nutrients.put(Codes.TRANSFAT, Double.parseDouble(mTransFat.getText().toString()));
		if (!mTotalCarb.getText().toString().equals(""))
			nutrients.put(Codes.CARB, Double.parseDouble(mTotalCarb.getText().toString()));
		if (!mFiber.getText().toString().equals(""))
			nutrients.put(Codes.FIBER, Double.parseDouble(mFiber.getText().toString()));
		if (!mSugars.getText().toString().equals(""))
			nutrients.put(Codes.TOTALSUGAR, Double.parseDouble(mSugars.getText().toString()));
		if (!mCholesterol.getText().toString().equals(""))
			nutrients.put(Codes.CHOLESTEROL, Double.parseDouble(mCholesterol.getText().toString()));
		if (!mSodium.getText().toString().equals(""))
			nutrients.put(Codes.SODIUM, Double.parseDouble(mSodium.getText().toString()));
		if (!mPotassium.getText().toString().equals(""))
			nutrients.put(Codes.POTASSIUM, Double.parseDouble(mPotassium.getText().toString()));
		if (!mVitaminA.getText().toString().equals(""))
			nutrients.put(Codes.VITAMINA, Double.parseDouble(mVitaminA.getText().toString()));
		if (!mVitaminC.getText().toString().equals(""))
			nutrients.put(Codes.VITAMINC, Double.parseDouble(mVitaminC.getText().toString()));
		if (!mCalcium.getText().toString().equals(""))
			nutrients.put(Codes.CALCIUM, Double.parseDouble(mCalcium.getText().toString()));
		if (!mIron.getText().toString().equals(""))
			nutrients.put(Codes.IRON, Double.parseDouble(mIron.getText().toString()));

		return nutrients;
	}

	private
	
	static final String[] FOOD_GROUPS = new String[] {
    	"Dairy",
		"Fast Food",
        "Fruits",
        "Grains",
        "Meat and Beans",
        "Snacks",
        "Vegetables" 
    };
    
//    static final String[] MEASURES = new String[] {
//    	"box",
//    	"container",
//    	"cubic inch",
//		"cup",
//		"fl oz",
//		"gallon",
//		"item",
//		"lb",
//		"oz",
//		"package",
//		"pat",
//		"patty",
//		"pie",
//		"piece",
//		"portion",
//		"quart",
//		"serving",
//		"slice",
//		"stick",
//        "tbsp",
//        "wedge", 
//    };
	
	private Integer[] mThumbIds = {
            R.drawable.beverage, R.drawable.breakfast, R.drawable.croissant,
            R.drawable.fastfood, R.drawable.grapes,
            R.drawable.hamburger, R.drawable.meat, R.drawable.milk,
            R.drawable.snack, R.drawable.snowpea
    };


}