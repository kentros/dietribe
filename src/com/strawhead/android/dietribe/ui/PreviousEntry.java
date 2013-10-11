package com.strawhead.android.dietribe.ui;

import java.util.List;

import com.strawhead.android.dietribe.BulletedText;
import com.strawhead.android.dietribe.EfficientAdapter;
import com.example.helloandroid.R;
import com.strawhead.android.dietribe.database.UserDB;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class PreviousEntry extends ListActivity implements OnItemClickListener
{
	private int userID;
	private int date;
	private UserDB userDB;

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
        }
        
        userDB = new UserDB(this);
        List<BulletedText> items = userDB.getMealEntriesForUser(userID);        
        //BulletedTextListAdapter btla = new BulletedTextListAdapter(this);
        EfficientAdapter btla = new EfficientAdapter(this);
        btla.setListItems(items);
        setListAdapter(btla);
        
        getListView().setOnItemClickListener(this);
    }
    
    public void onItemClick(AdapterView parent, View v, int position, long id)
	{
		// Get parent
    	BulletedText food = (BulletedText) getListAdapter().getItem(position);
		long entry_id = userDB.insertEntry(food.getText(), food.type, food.meal_item_id);
    	userDB.insertUserEntryItem(userID, date, entry_id);
        setResult(RESULT_OK, getIntent());
        finish();
	}
}
