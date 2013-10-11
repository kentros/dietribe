package com.strawhead.android.dietribe.ui;

import java.text.DecimalFormat;

import com.example.helloandroid.R;
import com.strawhead.android.dietribe.database.UserDB;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NutrientProgress extends Activity
{
	private UserDB userDB;
	private int date;
	private int userID;
	private int proteinMax = 90;
	private int carbMax = 300;
	private int fiberMax = 25;
	private int totalFatMax = 65;
	private int cholesterolMax = 300;
	DecimalFormat df = new DecimalFormat("###.#");
	
	@Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Set theme
        setTheme(android.R.style.Theme);
        
        setContentView(R.layout.nutrient_progress);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            date = extras.getInt("com.strawhead.android.dietribe.date");
        	userID = extras.getInt("com.strawhead.android.dietribe.userID");
        }
        userDB = new UserDB(this);

        double[] sums = userDB.getNutrientsForDate(userID, date);
        double proteinSum = sums[0];
        double carbSum = sums[1];
        double fiberSum = sums[2];
        double totalFatSum = sums[3];
        double satFatSum = sums[4];
        double cholesterolSum = sums[5];
        
        // Update Protein TextViews
        TextView proteinGrams = (TextView) findViewById(R.id.protein_progress_grams);
        TextView proteinPercent = (TextView) findViewById(R.id.protein_progress_percent);
        proteinGrams.setText(getResources().getString(R.string.protein) + " " + df.format(proteinSum) + "g ");
        proteinPercent.setText((int) (100*proteinSum/proteinMax) + "%");
        // Update Carb TextViews
        TextView carbGrams = (TextView) findViewById(R.id.carb_progress_grams);
        TextView carbPercent = (TextView) findViewById(R.id.carb_progress_percent);
        carbGrams.setText(getResources().getString(R.string.totalcarb) + " " + df.format(carbSum) + "g ");
        carbPercent.setText((int) (100*carbSum/carbMax) + "%");
        // Update Fiber TextViews
        TextView fiberGrams = (TextView) findViewById(R.id.fiber_progress_grams);
        TextView fiberPercent = (TextView) findViewById(R.id.fiber_progress_percent);
        fiberGrams.setText(getResources().getString(R.string.fiber) + " " + df.format(fiberSum) + "g ");
        fiberPercent.setText((int) (100*fiberSum/fiberMax) + "%");
        // Update Total Fat TextViews
        TextView totalFatGrams = (TextView) findViewById(R.id.totalfat_progress_grams);
        TextView totalFatPercent = (TextView) findViewById(R.id.totalfat_progress_percent);
        totalFatGrams.setText(getResources().getString(R.string.totalfat) + " " + df.format(totalFatSum) + "g ");
        totalFatPercent.setText((int) (100*totalFatSum/totalFatMax) + "%");
        // Update Cholesterol TextViews
        TextView cholesterolGrams = (TextView) findViewById(R.id.cholesterol_progress_grams);
        TextView cholesterolPercent = (TextView) findViewById(R.id.cholesterol_progress_percent);
        cholesterolGrams.setText(getResources().getString(R.string.cholesterol) + " " + df.format(totalFatSum) + "mg ");
        cholesterolPercent.setText((int) (100*cholesterolSum/cholesterolMax) + "%");
        
        
        // Progress bar for Protein
        ProgressBar proteinBar = (ProgressBar) findViewById(R.id.progress_protein);
        proteinBar.setMax(proteinMax);
        proteinBar.setProgress((int) (sums[0]));
        
        ProgressBar carbBar = (ProgressBar) findViewById(R.id.progress_carb);
        carbBar.setMax(carbMax);
        carbBar.setProgress((int) (sums[1]));
        
        ProgressBar fiberBar = (ProgressBar) findViewById(R.id.progress_fiber);
        fiberBar.setMax(fiberMax);
        fiberBar.setProgress((int) (sums[2]));
        
        ProgressBar totalFatBar = (ProgressBar) findViewById(R.id.progress_fat);
        totalFatBar.setMax(totalFatMax);
        totalFatBar.setProgress((int) (sums[3]));
        //ProgressBar satFatBar = (ProgressBar) findViewById(R.id.progress_sugars);
        
        ProgressBar cholesterolBar = (ProgressBar) findViewById(R.id.progress_cholesterol);
        cholesterolBar.setMax(cholesterolMax);
        cholesterolBar.setProgress((int) (sums[5]));
        
        
    }
}
