package com.strawhead.android.dietribe;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.helloandroid.R;
import com.strawhead.android.dietribe.database.UserDB;
import com.strawhead.android.dietribe.ui.Arcs;
import com.strawhead.android.dietribe.ui.Exercise;
import com.strawhead.android.dietribe.ui.FastFoodMenuList;
import com.strawhead.android.dietribe.ui.FoodDetails;
import com.strawhead.android.dietribe.ui.NutrientProgress;
import com.strawhead.android.dietribe.ui.NutritionalFacts;
import com.strawhead.android.dietribe.ui.PreviousEntry;
import com.strawhead.android.dietribe.ui.Search;
import com.strawhead.android.dietribe.ui.UserProfile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Dietribe extends ListActivity implements OnItemClickListener, OnItemLongClickListener
{
	private UserDB userDB;
	
	public String currentDate;
	private int date;
	private int userID = 1;
	protected int mYear;
	protected int mMonth;
	protected int mDay;
	
	private Button dateButton;
	private ArrayList<BulletedText> entries;
	private ProgressBar progressHorizontal;
	private TextView calorieRunningTotal;
	Animation alphaAnimation;
	
	// Activity Results
	private static final int ACTIVITY_FIRST_TIME = 0;
	private static final int ACTIVITY_ADD = 1;
		
	// Menu item Ids
    private static final int MENU_ITEM_ADD = 0;
    private static final int MENU_ITEM_SETTINGS = 1;
    private static final int MENU_ITEM_PROFILE = 2;
    
    // Dialog Ids
    private static final int DATE_DIALOG_ID = 0;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ITEM_ADD, 0, R.string.add)
                .setIcon(android.R.drawable.ic_menu_add)
                .setAlphabeticShortcut('\n');
        menu.add(0, MENU_ITEM_SETTINGS, 0, R.string.preferences)
                .setIcon(android.R.drawable.ic_menu_preferences)
                .setAlphabeticShortcut('q');
        menu.add(0, MENU_ITEM_PROFILE, 0, R.string.profile)
                .setIcon(android.R.drawable.ic_menu_add)
                .setAlphabeticShortcut('n');

        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_ADD:
            	//saveButton.performClick();
                return true;
    
            case MENU_ITEM_SETTINGS:
                //discardButton.performClick();
                return true;
    
            case MENU_ITEM_PROFILE:
                // Get confirmation
                //showDialog(DELETE_CONFIRMATION_DIALOG);
                return true;
        }

        return false;
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);
        }
        return null;
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Set theme
        setTheme(android.R.style.Theme);
        
        // Profile already exists?  Display main
        setContentView(R.layout.main);
        
        // If this is the very first time, we'll need to build the initial database.
        // This can take a significant amount of time, so we need to do this in the
        // background while presenting the user an activity to enter their profile.
        userDB = new UserDB(this);
        if (!userDB.hasUser()) {
        	Intent intent = new Intent();
        	intent.setClass(Dietribe.this, UserProfile.class);
        	intent.putExtra("com.strawhead.android.dietribe.firstTime", true);
        	startActivityForResult(intent, ACTIVITY_FIRST_TIME);
        }
        
        // Set up List on main view        
        EfficientAdapter btla = new EfficientAdapter(this);
        setListAdapter(btla);
        // Listen to Item Clicks
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
        
        
        //dailysummary
        calorieRunningTotal = (TextView)findViewById(R.id.dailysummary);
        calorieRunningTotal.setText("0% \t 0/2000");

        dateButton = (Button)findViewById(R.id.currentdate);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        
        dateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	showDialog(DATE_DIALOG_ID);
            }
        });
        
        dateButton.setOnKeyListener(new OnKeyListener() {

        	public boolean onKey(View v, int keyCode, KeyEvent event) 
        	{
        		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
        		{
        			c.roll(Calendar.DAY_OF_YEAR, -1);
        			mYear = c.get(Calendar.YEAR);
        	        mMonth = c.get(Calendar.MONTH);
        	        mDay = c.get(Calendar.DAY_OF_MONTH);

        	        updateDisplay();
        	        
        		}
        		else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
        		{
        			c.roll(Calendar.DAY_OF_YEAR, 1);
        			mYear = c.get(Calendar.YEAR);
        	        mMonth = c.get(Calendar.MONTH);
        	        mDay = c.get(Calendar.DAY_OF_MONTH);

        	        updateDisplay();
        	        
        		}
				return false;

        	}
        });
        
        
        Button mealsButton = (Button)findViewById(R.id.meals_button);
        mealsButton.requestFocus();
        // Display a list of meal options
        mealsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(Dietribe.this)
                        .setIcon(R.drawable.hamburger)
                        .setTitle(R.string.add_meal_from)
                        .setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) 
                            {
                            	Intent intent = new Intent();
                            	switch (which) {
                                case 0:
                                	// Here we start the search activity
                                	intent.putExtra("com.strawhead.android.dietribe.date", date);
                                	intent.putExtra("com.strawhead.android.dietribe.userID", userID);
                                    intent.setClass(Dietribe.this, Search.class);
                                    startActivityForResult(intent, ACTIVITY_ADD);
                                    break;
                                case 1:
                                	// Set EXTRA info and start Previous Entry view
                                	intent.putExtra("com.strawhead.android.dietribe.date", date);
                                	intent.putExtra("com.strawhead.android.dietribe.userID", userID);
                                	intent.setClass(Dietribe.this, PreviousEntry.class);
                                	startActivityForResult(intent, ACTIVITY_ADD);
                                	break;
                                case 2:
                                	// Here we start the fastfood list activity
                                	intent.putExtra("com.strawhead.android.dietribe.date", date);
                                	intent.putExtra("com.strawhead.android.dietribe.userID", userID);
                                	intent.setClass(Dietribe.this, FastFoodMenuList.class);
                                	startActivityForResult(intent, ACTIVITY_ADD);
                                    break;
                                case 3:
                                	// Here we start the manual entry activity
                                	intent.putExtra("com.strawhead.android.dietribe.date", date);
                                	intent.putExtra("com.strawhead.android.dietribe.userID", userID);
                                    intent.setClass(Dietribe.this, NutritionalFacts.class);
                                    startActivityForResult(intent, ACTIVITY_ADD);
                                    break;

                                }
                            }
                        })
                        .show();
            }
        });
        
        // Go to Add Exercise Activity
        Button exerciseButton = (Button) findViewById(R.id.exercise_button);
        exerciseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent();
            	intent.putExtra("com.strawhead.android.dietribe.date", date);
            	intent.putExtra("com.strawhead.android.dietribe.userID", userID);
            	intent.setClass(Dietribe.this, Exercise.class);
            	startActivityForResult(intent, ACTIVITY_ADD);
            }
        });
        
        Button dashboardButton = (Button) findViewById(R.id.tools_button);
        dashboardButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	// Here we start the Dashboard activity
            	Intent intent = new Intent();
            	intent.putExtra("com.strawhead.android.dietribe.date", date);
            	intent.putExtra("com.strawhead.android.dietribe.userID", userID);
                intent.setClass(Dietribe.this, NutrientProgress.class);
                //TODO Revert back after testing
                intent.setClass(Dietribe.this, Arcs.class);
                startActivity(intent);
            }});
        
//        dashboardButton.setVisibility(View.INVISIBLE);
        
        Button profileButton = (Button) findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	// Here we start the user profile activity
            	Intent intent = new Intent();
            	intent.putExtra("com.strawhead.android.dietribe.userID", userID);
                intent.setClass(Dietribe.this, UserProfile.class);
                startActivity(intent);
            }});
      
        
        
        // Progress bar for Calories
        progressHorizontal = (ProgressBar) findViewById(R.id.progress_horizontal);
        
        // Update the Display
        updateDisplay();

        //Animate List
        Animation alphaAnimation = new AlphaAnimation(0.8f, 1.0f);
        AnimationSet set = new AnimationSet(true);
        Animation animation = new TranslateAnimation(
        		Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
        		Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(200);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        getListView().setLayoutAnimation(controller);


    }
    
    
	private void updateDisplay()
	{
		// Calculate the data as an integer
		date = getIntegerFromDate(mYear, mMonth, mDay);
		// Pull entries using the date from database and display items in list...
		entries = new ArrayList<BulletedText>();
		entries = userDB.getMealEntriesForDate(userID, date);
		if (entries != null)
		{
			//BulletedTextListAdapter btla = new BulletedTextListAdapter(this);
			EfficientAdapter btla = new EfficientAdapter(this);
			btla.setListItems(entries);
			setListAdapter(btla);
		}
		
		// Update Running Total of Calories
		int currentTotal = userDB.getCaloriesForDate(userID, date);
		int calorieTarget = userDB.getCalorieTarget(userID);
		int caloriePercent = (100 * currentTotal / calorieTarget);
		progressHorizontal.setProgress(caloriePercent);
		calorieRunningTotal.setText(caloriePercent + "%     " + currentTotal + "/" + calorieTarget);
		
		// Get string value corresponding to month number
		String[] items = getResources().getStringArray(R.array.months);
		String month = items[mMonth];

		// Set Date on Button
		dateButton.setText(
	            new StringBuilder()
	                    .append(month).append(" ")
	                    .append(mDay).append(", ")
	                    .append(mYear));

	}


	private DatePickerDialog.OnDateSetListener mDateSetListener =
		new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	// y is 4 digit year
	private int getIntegerFromDate(int year, int month, int day)
	{
		int M1 = (month - 14) / 12;
		int Y1 = year + 4800;
		int J = 1461 * (Y1 + M1) / 4 + 367 * 
		(month - 2 - 12 * M1) / 12 - (3 * ((Y1 + M1 + 100) / 100))
		/ 4 + day - 32075;
		return J;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch(requestCode) {
        case ACTIVITY_ADD :
        	if (resultCode == RESULT_OK) {
        		updateDisplay();
        	}
            break;
        default:
            break;
        }
	}
	
	private void startLabelView(Intent intent, BulletedText food)
	{
		intent.putExtra("com.strawhead.android.dietribe.meal_item_id", food.meal_item_id);
		intent.putExtra("com.strawhead.android.dietribe.userID", userID);
		intent.putExtra("com.strawhead.android.dietribe.Long_Desc", food.getText());
		intent.putExtra("com.strawhead.android.dietribe.date", date);
		intent.putExtra("com.strawhead.android.dietribe.entryID", food.entry_id);
		//TODO Handle FoodGroup_CD correctly, instead of this magic number
		intent.putExtra("com.strawhead.android.dietribe.FdGrp_Cd", 1);
		intent.setClass(Dietribe.this, FoodDetails.class);
		startActivityForResult(intent, ACTIVITY_ADD);
	}

	public void onItemClick(AdapterView parent, View v, final int position, long id)
	{
		//display the food info in familiar nutrition facts label...
		BulletedText food = (BulletedText) getListAdapter().getItem(position);
		startLabelView(new Intent(), food);
	}

	public boolean onItemLongClick(AdapterView parent, View v, final int position, long id) {
		new AlertDialog.Builder(Dietribe.this)
        .setItems(R.array.main_list_options, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) 
            {
            	Intent intent = new Intent();
            	BulletedText food = (BulletedText) getListAdapter().getItem(position);
            	switch (which) {
                case 0:
                	//edit the meal item entry
            		intent.putExtra("com.strawhead.android.dietribe.meal_item_id", food.meal_item_id);
                    intent.setClass(Dietribe.this, NutritionalFacts.class);
                    startActivityForResult(intent, ACTIVITY_ADD);
                    break;
                case 1:
                	//display the food info in familiar nutrition facts label...
                	startLabelView(intent, food);
                	break;
                case 2:
            		//remove the entry
                	//TODO Confirmation dialog?
            		int entry_id = food.entry_id;
            		userDB.removeUserEntryItem(userID, date, entry_id);
            		updateDisplay();
                    break;
                }
            }
        })
        .show();
		return true;
	}
}