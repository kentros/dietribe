package com.strawhead.android.dietribe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.helloandroid.R;
import com.strawhead.android.dietribe.User;
import com.strawhead.android.dietribe.database.NutritionalDB;
import com.strawhead.android.dietribe.database.UserDB;

public class UserProfile extends Activity {
	
	private UserDB userDB;
	private NutritionalDB nutDB;
	
	// Menu item Ids
    public static final int PREFERENCES_ID = Menu.FIRST;
    
	private EditText mHeightText;
	private EditText mWeightText;
	private EditText mAgeText;
	private EditText mNameText;
	private Spinner genderSpinner;
	private Spinner activityLevel;
	//TODO Allow multiple-users
	private int userID = 1;
	private boolean firstTime;
	private static final int ACTIVITY_ADD = 1;
	
	private static final int DIALOG_FIRST_RUN = 1;
	
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_FIRST_RUN:
        	return new AlertDialog.Builder(UserProfile.this)
            //.setIcon(R.drawable.alert_dialog_icon)
            .setTitle(R.string.welcome)
            .setMessage(R.string.alert_dialog_first_run)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                }
            })
            .create();
        }
		return null;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        setTheme(android.R.style.Theme);
        
        // First time?  Show form to enter a new profile
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            firstTime = extras.getBoolean("com.strawhead.android.dietribe.firstTime");
        }
        
        setContentView(R.layout.profile);
        
        userDB = new UserDB(this);
        
        if (firstTime)
        {
        	// Show Dialog for First Time Run
        	showDialog(DIALOG_FIRST_RUN);
        }
        
        // Silently create the database during this Profile Entry
        startLongRunningOperation();
        
        // Gender Spinner
        genderSpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, 
                getResources().getStringArray(R.array.genders));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        
        // Level of Activity Spinner
        activityLevel = (Spinner) findViewById(R.id.activity_level);
        ArrayAdapter<String> activityAdapter = new ArrayAdapter<String>(this, 
        		android.R.layout.simple_spinner_item, 
        		getResources().getStringArray(R.array.activity_levels));

        activityLevel.setAdapter(activityAdapter);
        
        // Watch for button clicks.
        Button nextButton = (Button)findViewById(R.id.next);
        nextButton.setOnClickListener(mNextListener);
        
        //TODO Externalize
        mNameText = (EditText) findViewById(R.id.name);
        mAgeText = (EditText) findViewById(R.id.age);
        mAgeText.setHint("In Years");
        mHeightText = (EditText) findViewById(R.id.heightfeet);
        mHeightText.setHint("In Inches");
        mWeightText = (EditText) findViewById(R.id.weight);
        mWeightText.setHint("In Pounds");
        
        populateFields();
    }
    
    private void populateFields() {
		// TODO Populate Fields for multiple users
    	User user = userDB.getUser(userID);
    	if (user != null) {
    		mNameText.setText(user.user_name);
    		mAgeText.setText("" + user.age);
    		mHeightText.setText("" + user.height);
    		mWeightText.setText("" + user.weight);
    		genderSpinner.setSelection(user.gender);
    		activityLevel.setSelection(user.activity_level);
    	}
	}
    
    private void initializeNutDB()
    {
    	nutDB = new NutritionalDB(this);
    }
    
    protected void startLongRunningOperation() {

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
            	initializeNutDB();
            	//TODO UI does not really need an update based on when the USDA DB is setup?
                //mHandler.post(mUpdateResults);
            }
        };
        t.start();
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch(requestCode) {
        case ACTIVITY_ADD :
        	if (resultCode == RESULT_OK) {
        		setResult(RESULT_OK, getIntent());
            	finish();
        	}
            break;
        default:
            break;
        }
	}

//	@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        //TODO Externalize string
//        menu.add(0, PREFERENCES_ID, "Use Metric");
//        return true;
//    }
//    
//    @Override
//    public boolean onOptionsItemSelected(Menu.Item item) {
//        switch (item.getId()) {
//
//        case PREFERENCES_ID:
//            // TODO Set view to Preferences...
//            return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
    
    private OnClickListener mNextListener = new OnClickListener()
    {
		private Double[] activityLevels = {1.2, 1.375, 1.55, 1.725, 1.9};

		public void onClick(View v)
        {
        	// Initialize defaults
        	int heightFt = 0;
        	int heightIn = 68;
        	int weightLbs = 150;
        	int age = 30;
        	int gender = 0;
        	int activity_level = 1;
        	double BMR;
        	
        	// Get Name
        	String userName = mNameText.getText().toString().trim();
        	// Get Age
        	try
        	{
        		age = Integer.parseInt(mAgeText.getText().toString().trim());
        	} catch (NumberFormatException nfe)
        	{
        		//TODO Alert user that field is invalid
        	}
        	
        	// Get Gender
        	gender = genderSpinner.getSelectedItemPosition();
        	
        	// Get Activity Level
        	activity_level = activityLevel.getSelectedItemPosition();
        	
        	// Get Weight
        	try
        	{
        		weightLbs = Integer.parseInt(mWeightText.getText().toString().trim());
        	} catch (NumberFormatException nfe)
        	{
        		//TODO Alert user that field is invalid
        	}
        	
        	// Get Height
        	String heightInfo = mHeightText.getText().toString().trim();
        	try
        	{
        		heightIn = Integer.parseInt(heightInfo);
        	}
        	catch (NumberFormatException nfe)
        	{
        		// If the height entered is not a valid integer, try to parse
        		// the height numbers
        		if (heightInfo.contains("'"))
        		{
        			String[] feetInches = heightInfo.split("'");
        			if (feetInches.length == 1)
        			{
        				try 
        				{
        					heightFt = Integer.parseInt(feetInches[0].trim());
        				} catch (NumberFormatException nfe1)
        	        	{
        					//TODO Alert user that field is invalid
        	        	}
        				
        			}
        			else if (feetInches.length == 2)
        			{
        				try
        				{
        					heightFt = Integer.parseInt(feetInches[0].trim());
        					heightIn = feetInches[1].contains("\"") ? Integer.parseInt(feetInches[1].substring(0,feetInches[1].indexOf('"'))) :
        						Integer.parseInt(feetInches[1].trim());
        					
        					heightIn += (heightFt*12);
        				} catch (NumberFormatException nfe1)
        	        	{
        					//TODO Alert user that field is invalid
        	        	}
        				
        			}
        		}
        	}
        	
        	// Perform calculation of BMR (using Mifflin)
        	BMR = (9.99 * weightLbs) + (6.25 * heightIn) - (4.92 * age) + (166 * gender) - 161;
        	// Adjust for Activity Level
        	BMR *= activityLevels[activity_level];

        	// Store information in user database
        	if (!userDB.hasUser())
        		userDB.insertUser(userName, age, weightLbs, heightIn, gender, activity_level);
        	else
        		userDB.updateUser(userID, userName, age, weightLbs, heightIn, gender, activity_level);
        	
            // Here we start the next activity
            Intent intent = new Intent();
            intent.setClass(UserProfile.this, ProfileTarget.class);
            intent.putExtra("com.strawhead.android.dietribe.BMR", BMR);
            intent.putExtra("com.strawhead.android.dietribe.userID", userID);
            startActivityForResult(intent, ACTIVITY_ADD);
        }
    };
}