//package com.strawhead.android.dietribe;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//public class BulletedTextListAdapter extends BaseAdapter {
//
//	/**
//	 * Remember our context so we can use it when constructing views.
//	 */
//	private Context mContext;
//
//	private List<BulletedText> mItems;
//
//	/**
//	 * 
//	 * @param context
//	 */
//	public BulletedTextListAdapter(Context context) {
//		mContext = context;
//		mItems  = new ArrayList<BulletedText>();
//	}
//
//	/**
//	 * 
//	 * @param bt
//	 */
//	public void addItem(BulletedText bt) {
//		mItems.add(bt);
//	}
//
//	/**
//	 * 
//	 * @param bti
//	 */
//	public void setListItems(List<BulletedText> bti) {
//		mItems = bti;
//	}
//
//	/**
//	 * The number of items in the
//	 */
//	public int getCount() {
//		return mItems.size();
//	}
//
//	/**
//	 * 
//	 */
//	public Object getItem(int position) {
//		return mItems.get(position);
//	}
//
//	/**
//	 * 
//	 */
//	public boolean areAllItemsSelectable() {
//		return false;
//	}
//
//	/**
//	 * 
//	 */
//	public boolean isSelectable(int position) {
//		return mItems.get(position).isSelectable();
//	}
//
//	/**
//	 * Use the array index as a unique id.
//	 */
//	public long getItemId(int position) {
//		return position;
//	}
//
//	/**
//	 * Make a BulletedTextView to hold each row.
//	 */
//	public View getView(int position, View convertView, ViewGroup parent) {
//		BulletedTextView btv;
//		if (convertView == null) {
//			btv = new BulletedTextView(mContext, mItems.get(position).getText(),
//					mItems.get(position).getBullet());
//		} else {
//			btv = (BulletedTextView) convertView;
//			btv.setText(mItems.get(position).getText());
//			btv.setBullet(mItems.get(position).getBullet());
//		}
//
//		return btv;
//	}
//}
