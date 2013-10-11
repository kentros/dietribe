package com.strawhead.android.dietribe.ui;

import java.util.List;

import com.example.helloandroid.R;
import com.strawhead.android.dietribe.FoodItem;
import com.strawhead.android.dietribe.database.NutritionalDB;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
          
public class FastFoodMenu extends ListActivity implements OnItemSelectedListener, OnItemClickListener
{
	private int date;
	private int userID;
	private String restaurant;
	private NutritionalDB nutDB;
	private static final int ACTIVITY_ADD = 1;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setTheme(android.R.style.Theme);
        
        // Set to Search View
        setContentView(R.layout.fastfoodmenu);
        
        // Get user_id and date passed in...
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
        	userID = extras.getInt("com.strawhead.android.dietribe.userID");
        	date = extras.getInt("com.strawhead.android.dietribe.date");
        	restaurant = extras.getString("com.strawhead.android.dietribe.restaurant");
        }
        
        nutDB = new NutritionalDB(this);
        List<FoodItem> items = nutDB.getMealItems(restaurant, NutritionalDB.FASTFOOD);
        
        ArrayAdapter<FoodItem> notes = new ArrayAdapter<FoodItem>(this, R.layout.result_row, items);
        setListAdapter(notes);
        getListView().setOnItemClickListener(this);
    }

    /**
     * This method is called when the sending activity has finished, with the
     * result it supplied.
     * 
     * @param requestCode The original request code as given to
     *                    startActivity().
     * @param resultCode From sending activity as per setResult().
     * @param data From sending activity as per setResult().
     * @param extras From sending activity as per setResult().
     */
    protected void onActivityResult(int requestCode, int resultCode, String data, Bundle extras)
    {
        // You can use the requestCode to select between multiple child
        // activities you may have started.  Here there is only one thing
        // we launch.
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
    
	public void onItemSelected(AdapterView parent, View v, int position, long id) {
		// TODO Go to specialized menu view based on what is selected
		
	}

	public void onNothingSelected(AdapterView arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void onItemClick(AdapterView parent, View v, int position, long id)
	{
		// Display FoodDetails view with EXTRA data to populate custom label
		//TODO See if this works...
//		FoodItem food = (FoodItem) parent.obtainItem(position);
//		Intent intent = new Intent();
//		intent.setClass(FastFoodMenu.this, FoodDetails.class);
//		intent.putExtra("com.strawhead.android.dietribe.NDB_No", food.NDB_No);
//		intent.putExtra("com.strawhead.android.dietribe.userID", userID);
//		intent.putExtra("com.strawhead.android.dietribe.date", date);
//		intent.putExtra("com.strawhead.android.dietribe.Long_Desc", food.Long_Desc);
//		intent.putExtra("com.strawhead.android.dietribe.FdGrp_Cd", food.FdGrp_Cd);
//        startActivity(intent);
	}
}