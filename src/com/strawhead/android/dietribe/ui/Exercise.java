package com.strawhead.android.dietribe.ui;

import com.example.helloandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;

public class Exercise extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setTheme(android.R.style.Theme);
		setContentView(R.layout.exercise);
		
		TimePicker duration = (TimePicker) findViewById(R.id.duration_timepicker);
		duration.setCurrentHour(0);
		duration.setCurrentMinute(0);
		duration.setIs24HourView(true);
		
		Button addButton = (Button) findViewById(R.id.add_exercise_button);
		addButton.setText("ADD!");
	}
}