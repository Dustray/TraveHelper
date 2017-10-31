package bzu.skyn.travehelper;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;

import bzu.skyn.travehelper.compass.Piclayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class CompassActivity extends Activity {

	private static final String TAG = "Compass";

	   private SensorManager sensorManager;
	   private SampleView view;
	   private float[] Values;
	   private Intent intent;
	 
	   
	   private final SensorListener mListener = new SensorListener() {
		   
	       public void onSensorChanged(int sensor, float[] values) {
	           if (Config.LOGD) Log.d(TAG, "sensorChanged (" + values[0] + ", " + values[1] + ", " + values[2] + ")");
	           Values = values;
	           if (view != null) {
	               view.invalidate();
	           }
	       }
	       public void onAccuracyChanged(int sensor, int accuracy) {
	           // TODO Auto-generated method stub
	           
	       }
	   };
	   @Override
	   public void setContentView(View view) {
	       if (false) { // set to true to test Picture
	           ViewGroup vg = new Piclayout(this);
	           vg.addView(view);
	           view = vg;
	       }
	       super.setContentView(view);
	   }
	   @Override
	   protected void onCreate(Bundle icicle) {
	       super.onCreate(icicle);
	       sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	       view = new SampleView(this);
	       setContentView(view);
	       File a = new File("/sdcard/");
	       try{
	       File.createTempFile("test", "txt", a);
	       }
	       catch(IOException e){
	    	   
	       }
	       
	   }

	   @Override
	   protected void onResume()
	   {
	       if (Config.LOGD) Log.d(TAG, "onResume");
	       super.onResume();
	       sensorManager.registerListener(mListener, 
	       		SensorManager.SENSOR_ORIENTATION,
	       		SensorManager.SENSOR_DELAY_GAME);
	   }
	   
	   @Override
	   protected void onStop()
	   {
	       if (Config.LOGD) Log.d(TAG, "onStop");
	       sensorManager.unregisterListener(mListener);
	       super.onStop();
	   }

	   private class SampleView extends View {
	       private Paint mapPaint = new Paint();
	       
	       private Bitmap[] mapArray = new Bitmap[6];
	       InputStream is;
	       
	       int[] mapWidth = new int[6];
	       int[] mapHeight = new int[6];
	       private Resources resource;
	       
	       private boolean Active;

	       public SampleView(Context context) {
	           super(context);
	           
	           resource = CompassActivity.this.getResources();
	           
	           BitmapFactory.Options opts = new BitmapFactory.Options();
	           
	           opts.inJustDecodeBounds = false;    // this will request the bm
	           opts.inSampleSize = 2;             // scaled down by 2
	           
	           SetBitmapArray(0, opts, R.drawable.panel);
	           SetBitmapArray(1, opts, R.drawable.needle);
	           SetBitmapArray(2, opts, R.drawable.compass_degree);
	       }
	       
	       private void SetBitmapArray(int index, BitmapFactory.Options opts, int resId){
	    	   is = resource.openRawResource(resId);
	    	   mapArray[index] = BitmapFactory.decodeStream(is);//300*300
	           mapWidth[index] = mapArray[index].getWidth();
	           mapHeight[index] = mapArray[index].getHeight();
	           mapArray[index+3] = BitmapFactory.decodeStream(is, null, opts);
	           mapWidth[index+3] = mapArray[index+3].getWidth();
	           mapHeight[index+3] = mapArray[index+3].getHeight();
	       }
	       
	       @Override protected void onDraw(Canvas canvas) {
	           Paint paint = mapPaint;
	           
	           paint.setColor(Color.RED);

	           canvas.drawColor(Color.GRAY);
	           paint.setAntiAlias(true);

	           int w = canvas.getWidth();
	           int h = canvas.getHeight();
	           int cx = w / 2;
	           int cy = h / 2;
	           
	           int mCurrentOrientation = getResources().getConfiguration().orientation;
	           if ( mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ) { 
	        	   // If current screen is portrait
	        	   canvas.translate(cx, cy);
	        	   drawPictures(canvas,0);
	           } else if ( mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE ) {
	        	   //If current screen is landscape
	        	   canvas.translate(cx, cy-20);
	        	   drawPictures(canvas,3);
	           }
	       }
	       
	       private void drawPictures(Canvas canvas, int idDelta){
	    	   if (Values != null) {   
//	            	Log.d(TAG, "Values[0] = "+ Values[0]);
	                canvas.rotate(-Values[0]);
	                canvas.drawBitmap(mapArray[0+idDelta], -mapWidth[0+idDelta]/2, -mapHeight[0+idDelta]/2, mapPaint);
	                canvas.drawBitmap(mapArray[1+idDelta], -mapWidth[1+idDelta]/2, -mapHeight[1+idDelta]/2, mapPaint);
	                canvas.rotate(360+Values[0]);
	                canvas.drawBitmap(mapArray[2+idDelta], -mapWidth[2+idDelta]/2, -mapHeight[2+idDelta]/2, mapPaint);
	            }
	            else{
	         	    canvas.drawBitmap(mapArray[0+idDelta], -mapWidth[0+idDelta]/2, -mapHeight[0+idDelta]/2, mapPaint);
	                canvas.drawBitmap(mapArray[1+idDelta], -mapWidth[1+idDelta]/2, -mapHeight[1+idDelta]/2, mapPaint);
	                canvas.drawBitmap(mapArray[2+idDelta], -mapWidth[2+idDelta]/2, -mapHeight[2+idDelta]/2, mapPaint);
	            }
	       }
	       
	       @Override
	       protected void onAttachedToWindow() {
	           setActive(true);
	           super.onAttachedToWindow();
	       }
	       
	       @Override
	       protected void onDetachedFromWindow() {
	           setActive(false);
	           super.onDetachedFromWindow();
	       }

		public void setActive(boolean Active) {
			this.Active = Active;
		}

		@SuppressWarnings("unused")
		public boolean isActive() {
			return Active;
		}
	   }

	   @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		   switch (item.getItemId()) {
		case R.id.comback:
			intent=new Intent();
			intent.setClass(CompassActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.comquit:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
