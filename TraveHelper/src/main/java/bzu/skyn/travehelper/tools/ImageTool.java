package bzu.skyn.travehelper.tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import bzu.skyn.travehelper.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageTool {
	public static Bitmap getBitmap(URL imageURL){
		URLConnection con;
		Bitmap bitmap = null;
		try{
			con = imageURL.openConnection();
			con.connect();
			InputStream is = con.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return bitmap;
	}
	// 工具方法，该方法负责把返回的天气图标字符串，转换为程序的图片资源ID。
		public int parseIcon(String strIcon)
		{
			if (strIcon == null)
				return -1;
			if ("0.png".equals(strIcon))
				return R.drawable.weather_0;
			if ("1.png".equals(strIcon))
				return R.drawable.weather_1;
			if ("2.png".equals(strIcon))
				return R.drawable.weather_2;
			if ("3.png".equals(strIcon))
				return R.drawable.weather_3;
			if ("4.png".equals(strIcon))
				return R.drawable.weather_4;
			if ("5.png".equals(strIcon))
				return R.drawable.weather_5;
			if ("6.png".equals(strIcon))
				return R.drawable.weather_6;
			if ("7.png".equals(strIcon))
				return R.drawable.weather_7;
			if ("8.png".equals(strIcon))
				return R.drawable.weather_8;
			if ("9.png".equals(strIcon))
				return R.drawable.weather_9;
			if ("10.png".equals(strIcon))
				return R.drawable.weather_10;
			if ("11.png".equals(strIcon))
				return R.drawable.weather_11;
			if ("12.png".equals(strIcon))
				return R.drawable.weather_12;
			if ("13.png".equals(strIcon))
				return R.drawable.weather_13;
			if ("14.png".equals(strIcon))
				return R.drawable.weather_14;
			if ("15.png".equals(strIcon))
				return R.drawable.weather_15;
			if ("16.png".equals(strIcon))
				return R.drawable.weather_16;
			if ("17.png".equals(strIcon))
				return R.drawable.weather_17;
			if ("18.png".equals(strIcon))
				return R.drawable.weather_18;
			if ("19.png".equals(strIcon))
				return R.drawable.weather_19;
			if ("20.png".equals(strIcon))
				return R.drawable.weather_20;
			if ("21.png".equals(strIcon))
				return R.drawable.weather_21;
			if ("22.png".equals(strIcon))
				return R.drawable.weather_22;
			if ("23.png".equals(strIcon))
				return R.drawable.weather_23;
			if ("24.png".equals(strIcon))
				return R.drawable.weather_24;
			if ("25.png".equals(strIcon))
				return R.drawable.weather_25;
			if ("26.png".equals(strIcon))
				return R.drawable.weather_26;
			if ("27.png".equals(strIcon))
				return R.drawable.weather_27;
			if ("28.png".equals(strIcon))
				return R.drawable.weather_28;
			if ("29.png".equals(strIcon))
				return R.drawable.weather_29;
			if ("30.png".equals(strIcon))
				return R.drawable.weather_30;
			if ("31.png".equals(strIcon))
				return R.drawable.weather_31;
			if ("32.png".equals(strIcon))
				return R.drawable.weather_32;
			if ("33.png".equals(strIcon))
				return R.drawable.weather_33;
			if ("34.png".equals(strIcon))
				return R.drawable.weather_34;
			if ("35.png".equals(strIcon))
				return R.drawable.weather_35;
			if ("36.png".equals(strIcon))
			return R.drawable.weather_36;
			if ("37.png".equals(strIcon))
			return R.drawable.weather_37;
			if ("38.png".equals(strIcon))
				return R.drawable.weather_38;
			if ("99.png".equals(strIcon))
				return R.drawable.weather_99;
			return 0;
		}

}
