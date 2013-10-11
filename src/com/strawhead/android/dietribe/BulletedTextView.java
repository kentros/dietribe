//package com.strawhead.android.dietribe;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//public class BulletedTextView extends LinearLayout {
//
//	private TextView mText;
//	private ImageView mBullet;
//	private RatingBar mRating;
//	
//	public BulletedTextView(Context context, String text, Drawable bullet) {
//		super(context);
//
//		this.setOrientation(HORIZONTAL);
//
//		mBullet = new ImageView(context);
//		mBullet.setImageDrawable(bullet);
//		// left, top, right, bottom
//		mBullet.setPadding(0, 2, 5, 0);
//		addView(mBullet,  new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		
//		mText = new TextView(context);
//		mText.setText(text);
//		addView(mText, new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		
//		mRating = (RatingBar) findViewById(R.id.small_ratingbar);
//		mRating.setNumStars(3);
//		addView(mRating, new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//	}
//
//	public void setText(String words) {
//		mText.setText(words);
//	}
//	
//	public void setBullet(Drawable bullet) {
//		mBullet.setImageDrawable(bullet);
//	}
//	
//	public void setRating(float stars) {
//		mRating.setRating(stars);
//	}
//}
