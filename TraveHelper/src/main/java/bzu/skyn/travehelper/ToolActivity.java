package bzu.skyn.travehelper;

import bzu.skyn.travehelper.R;
import bzu.skyn.travehelper.controls.Carousel;
import bzu.skyn.travehelper.controls.CarouselAdapter;
import bzu.skyn.travehelper.controls.CarouselAdapter.OnItemClickListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

	public class ToolActivity extends Activity {
		private Intent intent;
		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.tool);
			Carousel carousel = (Carousel) findViewById(R.id.carousel);
			carousel.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(CarouselAdapter<?> parent, View view,
						int position, long id) {
					
					switch (position) {
					case 0:
						intent=new Intent();
						intent.setClass(ToolActivity.this, OperationActivity.class);
						startActivity(intent);							
						break;
					case 1:
						intent=new Intent();
						intent.setClass(ToolActivity.this, DiaryActivity.class);
						startActivity(intent);						
						break;
					case 2:
						intent=new Intent();
						intent.setClass(ToolActivity.this, CompassActivity.class);
						startActivity(intent);						
						break;
					case 3:
						intent=new Intent();
						intent.setClass(ToolActivity.this, ShanGuangDActivity.class);
						startActivity(intent);						
						break;
					case 4:
						intent=new Intent();
						intent.setClass(ToolActivity.this, CalendarActivity.class);
						startActivity(intent);						
						break;

					default:
						break;
					}
				}

			});
		}

	}


