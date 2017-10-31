package bzu.skyn.travehelper;

import android.Manifest;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class ShanGuangDActivity extends Activity implements OnClickListener {

    private Button onebutton = null;
    private Camera camera = null;
    private Parameters parameters = null;
    public static boolean kaiguan = true, isFirst = true; //定义开关状态，状态为false，打开状态，状态为true，关闭状态
    //    public static boolean action = false;  //定义的状态，状态为false，当前界面不退出，状态为true，当前界面退出
    private int back = 0;//判断按几次back
    private Intent intent;

    private Camera m_Camera = null;// 声明Camera对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //全屏设置，隐藏窗口所有装饰
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置屏幕显示无标题，必须启动就要设置好，否则不能再次被设置
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD, WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.shan_guang_d);

        onebutton = (Button) findViewById(R.id.onebutton);
        onebutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //lightSwitch(kaiguan);
        if (isFirst) {
            camera = Camera.open();
            isFirst = false;

            parameters = camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);//开启
            camera.setParameters(parameters);
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);//关闭
            camera.setParameters(parameters);
        }
        if (kaiguan) {
            onebutton.setBackgroundResource(R.drawable.shanguang1);

            parameters = camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);//开启
            camera.setParameters(parameters);
            // turnLightOn(camera);
//				onebutton.setText("关闭");
            kaiguan = false;
        } else {
            onebutton.setBackgroundResource(R.drawable.shanguang);
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);//关闭
            camera.setParameters(parameters);
            // turnLightOff(camera);
//				onebutton.setText("开启");
            kaiguan = true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "返回主菜单");
        menu.add(0, 2, 2, "退出");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                intent = new Intent();
                intent.setClass(ShanGuangDActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
                break;

            case 2:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			back++;
			switch (back) {
			case 1:
				Toast.makeText(ShanGuangDActivity.this, "再按一次退出外星人电筒", Toast.LENGTH_LONG).show();
				break;
			case 2:
				back = 0;//初始化back值
				Myback();
				break;
			}
			return true;//设置成false让back失效    ，true表示 不失效
		}
		else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	public void Myback(){ //关闭程序
			if(kaiguan){//开关关闭时
				ShanGuangDActivity.this.finish();
				android.os.Process.killProcess(android.os.Process.myPid());//关闭进程
			}else if(!kaiguan){//开关打开时
				camera.release();
				ShanGuangDActivity.this.finish();
				android.os.Process.killProcess(android.os.Process.myPid());//关闭进程
				kaiguan = true;//避免，打开开关后退出程序，再次进入不打开开关直接退出时，程序错误
			}
	}
	*/

    @Override
    protected void onPause() {
        super.onPause();
        if(camera!=null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            isFirst = true;
        }
    }

    @Override
    protected void onStop() {
        if(camera!=null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            isFirst = true;
        }
        super.onStop();
        finish();
    }
}
