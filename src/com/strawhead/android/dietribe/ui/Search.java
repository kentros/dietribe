package com.strawhead.android.dietribe.ui;

import java.util.ArrayList;
import java.util.List;

import com.strawhead.android.dietribe.FoodItem;
import com.example.helloandroid.R;
import com.strawhead.android.dietribe.database.NutritionalDB;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Search extends ListActivity implements OnItemClickListener, OnItemSelectedListener
{
	private NutritionalDB nutDB;
	List<FoodItem> items;
	private TextView mFoodDescription;
	private TextView mSearchText;
	private Button searchButton;
	private ProgressDialog pd;
	private int userID;
	private int foodGroup = 0;
	private int date;
	private static final int ACTIVITY_ADD = 1;
	protected static final int GUIUPDATEIDENTIFIER = 0x101;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setTheme(android.R.style.Theme);
        
        final Intent queryIntent = getIntent();
        final String queryAction = queryIntent.getAction();
        
        
        // Set to Search View
        setContentView(R.layout.search);
        
        
        // Get user_id and date passed in...
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
        	date = extras.getInt("com.strawhead.android.dietribe.date");
        	userID = extras.getInt("com.strawhead.android.dietribe.userID");
        }
          
        // Access Nutrition DB
        nutDB = new NutritionalDB(this);
        
        mFoodDescription = (TextView) findViewById(R.id.resultsummary);
        //mFoodDescription.setVisibility(View.GONE);
        mSearchText = (TextView) findViewById(R.id.searchtext);
        mSearchText.setOnKeyListener(new OnKeyListener() {

        	public boolean onKey(View v, int keyCode, KeyEvent event) {
        		if (keyCode==KeyEvent.KEYCODE_DPAD_CENTER) {

        			search();
        			return true;
        		}

        		return false;

        	}
        });
 
        
        // Watch for button clicks.
        searchButton = (Button)findViewById(R.id.searchbutton);
        searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	// Perform DB Search
    			search();
            }
        });
        
        // Food Group Spinner
        Spinner s1 = (Spinner) findViewById(R.id.foodgroups);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.food_group_items));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);
        
        ArrayAdapter<FoodItem> notes = 
            new ArrayAdapter<FoodItem>(this, R.layout.result_row);
        setListAdapter(notes);
        
        // Listen to Item Selections
        //getListView().setOnItemSelectedListener(this);
        getListView().setOnItemClickListener(this);
        //getListView().setOnItemLongClickListener(this);
        
        
//        if (Intent.SEARCH_ACTION.equals(queryAction))
//        {
//        	doSearchQuery(queryIntent, "onCreate()");
//        }
        
        
    }
    
    private void doSearchQuery(Intent queryIntent, String string)
    {

    	 String queryString = queryIntent.getStringExtra(SearchManager.QUERY);
    	 mSearchText.setText(queryString);

    	 setTitle("Search results for " + "'" + queryString + "'");
    	 search();
		
	}

	public void search() 
    {
		this.pd = ProgressDialog.show(this, null, "Searching Database", true, false);
		new Thread() {
			public void run() {

				items = new ArrayList<FoodItem>();    		
				items = nutDB.getMealItems(mSearchText.getText().toString(), foodGroup);	

				// Close Progress-Dialog
				Search.this.pd.dismiss();

				// Send message to handler to update GUI
				Message m = new Message();
				m.what = Search.GUIUPDATEIDENTIFIER; 
				Search.this.handler.sendMessage(m);
			}
		}.start();
    }
    
	public void onNothingSelected(AdapterView arg0) {
		mFoodDescription.setText("");
		
	}

	public void onItemClick(AdapterView parent, View v, int position, long id)
	{
		// Display FoodDetails view with EXTRA data to populate custom label
		//TODO See if this works...
//		FoodItem food = (FoodItem) parent.obtainItem(position);
//		Intent intent = new Intent();
//		intent.setClass(Search.this, FoodDetails.class);
//		intent.putExtra("com.strawhead.android.dietribe.NDB_No", food.NDB_No);
//		intent.putExtra("com.strawhead.android.dietribe.userID", userID);
//		intent.putExtra("com.strawhead.android.dietribe.Long_Desc", food.Long_Desc);
//		intent.putExtra("com.strawhead.android.dietribe.FdGrp_Cd", food.FdGrp_Cd);
//		intent.putExtra("com.strawhead.android.dietribe.date", date);
//        startActivity(intent);
	}

//	public boolean onItemLongClick(AdapterView arg0, View v, int position, long id)
//	{
//		// Action when they hold down on an item...
//		//TODO Add to database journal table...
//		Intent intent = new Intent();
//		intent.setClass(Search.this, Dietribe.class);
//        startActivity(intent);
//		return true;
//	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
			case Search.GUIUPDATEIDENTIFIER:
				//handle GUI update
				ArrayAdapter<FoodItem> notes = new ArrayAdapter<FoodItem>(Search.this, R.layout.result_row, items);
				setListAdapter(notes);
				if (items != null)
					mFoodDescription.setText(items.size() + " results found");
				break;
			} 
		}

	};

	public void onItemSelected(AdapterView parent, View v, int position, long id)
	{
		// Handle changing Food Group
		foodGroup = position;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, String data, Bundle extras) {
		switch(requestCode) {
        case ACTIVITY_ADD:
        	if (resultCode == RESULT_OK) {
        		setResult(RESULT_OK, getIntent());
            	finish();
        	}
            break;
        default:
            break;
        }
	}
}