package com.strawhead.android.dietribe;

import java.util.ArrayList;
import java.util.List;

import com.example.helloandroid.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ImageView;


public class EfficientAdapter extends BaseAdapter {
	
	private List<BulletedText> mItems;
    private LayoutInflater mInflater;

    public EfficientAdapter(Context context) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        
        // Store Items
        mItems = new ArrayList<BulletedText>();
    }
    
    public void setListItems(List<BulletedText> bti) {
		mItems = bti;
	}

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        return mItems.size();
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficent to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    public BulletedText getItem(int position) {
        return mItems.get(position);
    }

    /**
     * Use the array index as a unique id.
     *
     * @see android.widget.ListAdapter#getItemId(int)
     */
    public long getItemId(int position) {
    	//TODO Remove/Rework this method to return actual Item ID?
        return position;
    }

    /**
     * Make a view to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_icon_text, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.rating = (RatingBar) convertView.findViewById(R.id.small_ratingbar);
            holder.rating.setNumStars(3);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.text.setText(mItems.get(position).getText());
        holder.icon.setImageDrawable(mItems.get(position).getBullet());
        float rating = mItems.get(position).getRating();
        if (rating > 0)
        {
        	holder.rating.setRating(rating);
        }
        else
        	holder.rating.setVisibility(View.GONE);
        return convertView;
    }

    static class ViewHolder {
        TextView text;
        ImageView icon;
        RatingBar rating;
    }
}

