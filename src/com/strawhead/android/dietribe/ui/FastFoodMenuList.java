package com.strawhead.android.dietribe.ui;

import com.example.helloandroid.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TabHost.TabSpec;

public class FastFoodMenuList extends ListActivity implements OnItemClickListener, OnItemLongClickListener
{
	private ListView ls1;
    private ListView ls2;   
    private TabHost myTabHost;
	private int date;
	private int userID;
	private static final int ACTIVITY_ADD = 1;
	
	@Override
    public void onCreate(Bundle icicle)
	{
        super.onCreate(icicle);
        
        // Set theme
        setTheme(android.R.style.Theme);
        
        
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            date = extras.getInt("com.strawhead.android.dietribe.date");
        	userID = extras.getInt("com.strawhead.android.dietribe.userID");
        }
        
        ls1 = new ListView(FastFoodMenuList.this);             
        ls2 = new ListView(FastFoodMenuList.this);
        
        setContentView(R.layout.fastfoodmenulist);
        
        this.myTabHost = (TabHost)this.findViewById(R.id.th_set_menu_tabhost);
        this.myTabHost.setup();         

        TabSpec ts = myTabHost.newTabSpec("TAB_TAG_1");

        ts.setIndicator(getResources().getString(R.string.allrestaurants), this.getResources().getDrawable(R.drawable.fastfood));                
                  
        ts.setContent(new TabHost.TabContentFactory(){
             public View createTabContent(String tag)
             {                                            
                  ArrayAdapter<String> adapter = new ArrayAdapter<String>(FastFoodMenuList.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.fast_food_stores));
                  ls1.setAdapter(adapter); 
                  return ls1;
             }         
        }); 
        
        myTabHost.addTab(ts);
                  
        TabSpec ts1 = myTabHost.newTabSpec("TAB_TAG_2");
        
        ts1.setContent(new TabHost.TabContentFactory(){
             public View createTabContent(String tag)
             {
            	 //TODO Set Adapter to Favorites
                  ArrayAdapter<String> adapter = new ArrayAdapter<String>(FastFoodMenuList.this, android.R.layout.simple_list_item_1,new String[]{""});
                  ls2.setAdapter(adapter); 
                  return ls2;
             }         
        });       
        
        ts1.setIndicator(getResources().getString(R.string.favorites), this.getResources().getDrawable(R.drawable.star_big_on));
        myTabHost.addTab(ts1);

        // Set Animation
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller =
                new LayoutAnimationController(set, 0.5f);        
        ls1.setLayoutAnimation(controller);
        ls2.setLayoutAnimation(controller);
        
        ls1.setOnItemClickListener(this);
        ls1.setOnItemLongClickListener(this);
        
        ls2.setOnItemClickListener(this);

        
        //TODO Check if Favorites has any items.  If it does, set it to the current tab
        //Otherwise, set the default tab to show all restaurants.
        myTabHost.setCurrentTab(0);
	}

	public void onItemClick(AdapterView parent, View v, int position, long id) {
		// Display FastFoodMenu view with EXTRA data to populate
//		String restaurant = (String) parent.obtainItem(position);
//		Intent intent = new Intent();
//		intent.setClass(FastFoodMenuList.this, FastFoodMenu.class);
//		intent.putExtra("com.strawhead.android.dietribe.restaurant", restaurant);
//		intent.putExtra("com.strawhead.android.dietribe.userID", userID);
//		intent.putExtra("com.strawhead.android.dietribe.date", date);
//        startActivity(intent);
	}
	
	public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {
		// TODO Open a "context-menu to give user choice to add to favorites
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, String data, Bundle extras) {
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
}