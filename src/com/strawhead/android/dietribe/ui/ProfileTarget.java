package com.strawhead.android.dietribe.ui;

import com.example.helloandroid.R;
import com.strawhead.android.dietribe.database.UserDB;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ProfileTarget extends Activity {

	private UserDB userDB;
	private double BMR = 0;
	private EditText mCalorieTarget;
	private int userID;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setTheme(android.R.style.Theme);
        
        setContentView(R.layout.profile_measurements);
        userDB = new UserDB(this);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            BMR = extras.getDouble("com.strawhead.android.dietribe.BMR");
            userID = extras.getInt("com.strawhead.android.dietribe.userID");
        }
        
        mCalorieTarget = (EditText) findViewById(R.id.calorie_intake);
        if (BMR > 0)
        {
        	int targetCalories = (int) (BMR < 1200 ? 1200 : BMR);
        	mCalorieTarget.setText("" + ((int) targetCalories));
        }
        
        // Watch for button clicks.
        Button searchButton = (Button)findViewById(R.id.searchbutton);
        searchButton.setOnClickListener(mNextListener);
    }
    
    private OnClickListener mNextListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	//TODO Add to Database
        	//TODO Add error checking on invalid number entered
        	int calorie_target = Integer.parseInt(mCalorieTarget.getText().toString());
        	userDB.updateUserTargets(userID, calorie_target);
        	setResult(RESULT_OK, getIntent());
			finish();
        }
    };
}
