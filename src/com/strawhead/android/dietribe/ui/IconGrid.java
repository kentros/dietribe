package com.strawhead.android.dietribe.ui;

import com.example.helloandroid.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class IconGrid extends Activity
{

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.icongrid);
        GridView grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(new ImageAdapter(this));
        
    }

    

	public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(45, 45));
                imageView.setAdjustViewBounds(false);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            imageView.setId(position);
            imageView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("icon", v.getId());
					setResult(RESULT_OK, intent);
					finish();
				}
            	
            });
            

            return imageView;
        }

        private Context mContext;
        
        private Integer[] mThumbIds = {
                R.drawable.beverage, R.drawable.breakfast, R.drawable.croissant,
                R.drawable.fastfood, R.drawable.grapes,
                R.drawable.hamburger, R.drawable.meat, R.drawable.milk,
                R.drawable.snack, R.drawable.snowpea
        };
	}
}
