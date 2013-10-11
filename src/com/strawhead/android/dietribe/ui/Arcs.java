package com.strawhead.android.dietribe.ui;

/* 
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


// Need the following import to get access to the app resources, since this
// class is in a sub-package.
//import com.example.android.apis.R;

import java.io.File;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Arcs extends Activity {

//	@Override
//    public void setContentView(View view) {
//        if (false) { // set to true to test Picture
//            ViewGroup vg = new PictureLayout(this);
//            vg.addView(view);
//            view = vg;
//        }
//        
//        super.setContentView(view);
//    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create a simple pie chart
//        DefaultPieDataset pieDataset = new DefaultPieDataset();
//        pieDataset.setValue("A", new Integer(75));
//        pieDataset.setValue("B", new Integer(10));
//        pieDataset.setValue("C", new Integer(10));
//        pieDataset.setValue("D", new Integer(5));
//        JFreeChart chart = ChartFactory.createPieChart(
//               "CSC408 Mark Distribution",
//                pieDataset, 
//                true, 
//                true, 
//                false);
//        try {
//            ChartUtilities.saveChartAsJPEG(new File("/sdcard/chart.jpg"), chart, 320,
//                320);
//        } catch (Exception e) {
//            System.out.println("Problem occurred creating chart.");
//        }
        
        Bitmap bmp=BitmapFactory.decodeFile("/sdcard/chart.jpg");

        ImageView iv = (ImageView) findViewById(com.example.helloandroid.R.id.image0);
        iv.setImageBitmap(bmp); 
        
        setContentView(com.example.helloandroid.R.layout.chart);
    }
    
//    private static class SampleView extends View {
//        private Paint[] mPaints;
//        private Paint mFramePaint;
//        private boolean[] mUseCenters;
//        private RectF[] mOvals;
//        private RectF mBigOval;
//        private float mStart;
//        private float mSweep;
//        private int mBigIndex;
//        
//        private static final float SWEEP_INC = 2;
//        private static final float START_INC = 15;
//        
//        public SampleView(Context context) {
//            super(context);
//            
//            mPaints = new Paint[4];
//            mUseCenters = new boolean[4];
//            mOvals = new RectF[4];
//    
//            mPaints[0] = new Paint();
//            mPaints[0].setAntiAlias(true);
//            mPaints[0].setStyle(Paint.Style.FILL);
//            mPaints[0].setColor(0x88FF0000);
//            mUseCenters[0] = false;
//            
//            mPaints[1] = new Paint(mPaints[0]);
//            mPaints[1].setStyle(Paint.Style.FILL_AND_STROKE);
//            mPaints[1].setStrokeWidth(2);
//            mPaints[1].setColor(0x8800FF00);
//            mUseCenters[1] = true;
//            
//            mPaints[2] = new Paint(mPaints[0]);
//            mPaints[2].setStyle(Paint.Style.FILL_AND_STROKE);
//            mPaints[2].setStrokeWidth(2);
//            mPaints[2].setColor(0x880000FF);
//            mUseCenters[2] = false;
//
//            mPaints[3] = new Paint(mPaints[0]);
//            mPaints[3].setStyle(Paint.Style.FILL_AND_STROKE);
//            mPaints[2].setStrokeWidth(2);
//            mPaints[3].setColor(0x00008888);
//            mUseCenters[3] = true;
//            
//            mBigOval = new RectF(40, 10, 280, 250);
//            
//            mOvals[0] = new RectF( 10, 270,  70, 330);
//            mOvals[1] = new RectF( 90, 270, 150, 330);
//            mOvals[2] = new RectF(170, 270, 230, 330);
//            mOvals[3] = new RectF(250, 270, 310, 330);
//            
//            mFramePaint = new Paint();
//            mFramePaint.setAntiAlias(true);
//            mFramePaint.setStyle(Paint.Style.STROKE);
//            mFramePaint.setStrokeWidth(0);
//        }
//        
//        private void drawArcs(Canvas canvas, RectF oval, boolean useCenter,
//                              Paint paint) {
//            //canvas.drawRect(oval, mFramePaint);
//            canvas.drawArc(oval, mStart, mSweep, useCenter, paint);
//        }
//        
//        @Override
//        protected void onDraw(Canvas canvas) {
//            canvas.drawColor(Color.WHITE);
//            
//            
//            mStart = 2;
//            mSweep = 120;
//            drawArcs(canvas, mBigOval, true,
//                     mPaints[2]);
//            
//            mStart = 120;
//            mSweep = 120;
//            drawArcs(canvas, mBigOval, true,
//                    mPaints[1]);
//            
//            mStart = 320;
//            mSweep = 360;
//            drawArcs(canvas, mBigOval, true,
//                    mPaints[2]);
//            
//            //for (int i = 0; i < 4; i++) {
//                //drawArcs(canvas, mOvals[2], mUseCenters[2], mPaints[2]);
//            //}
//            
////            mSweep += SWEEP_INC;
////            if (mSweep > 120) {
////            	mPaints[2].setColor(0x88880088);
////            	mStart = 120;
////            }
////            if (mSweep > 360) {
////                mSweep -= 360;
////                mStart += START_INC;
////                if (mStart >= 360) {
////                    mStart -= 360;
////                }
////                mBigIndex = (mBigIndex + 1) % mOvals.length;
////            }
////            else
////            	invalidate();
//            
//        }
//    }
}

