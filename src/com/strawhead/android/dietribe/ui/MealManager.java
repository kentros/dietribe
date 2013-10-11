package com.strawhead.android.dietribe.ui;

import com.example.helloandroid.R;

import android.app.Activity;
import android.os.Bundle;

public class MealManager extends Activity {
	/**
	 * Called with the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setTheme(android.R.style.Theme);
		setContentView(R.layout.mealmanager);
	}
}