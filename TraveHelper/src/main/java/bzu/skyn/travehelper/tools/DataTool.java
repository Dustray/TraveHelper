package bzu.skyn.travehelper.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 日记本工具类
 * @author Administrator
 *
 */
public class DataTool {

	/*获取当前时间*/
	public static String getCurrentTime(){
    	SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	return formatter.format(new Date());
    }
}
