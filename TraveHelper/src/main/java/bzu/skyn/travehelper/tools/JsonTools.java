package bzu.skyn.travehelper.tools;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import bzu.skyn.travehelper.entity.AttractionEntity;
import bzu.skyn.travehelper.entity.WeatherJsonEntity;

/**
 * Created by Dustray on 2017/10/28 0028.
 */

public class JsonTools {

    public static List<AttractionEntity> jsonAttraction(String jsonString){
        List<AttractionEntity> list =new ArrayList<AttractionEntity>();



        JsonParser parser = new JsonParser();// json 解析器
        JsonObject obj = (JsonObject) parser.parse(jsonString); /* 获取返回状态码 */
        String resultCode = obj.get("showapi_res_code").getAsString(); /* 如果状态码是200说明返回数据成功*/
        if (0 == Integer.parseInt(resultCode)) {
            Log.i("logme","%%%%%%%%%%%%%%%%");
            //Log.i("logme","%%%%%%%%%%%%%%%%");
            //JsonArray futureWeatherArray = obj.get("results").getAsJsonArray();

            //JsonObject jLocation = futureWeatherArray.get(0).getAsJsonObject();
            JsonObject showapi_res_body = obj.get("showapi_res_body").getAsJsonObject();
            JsonObject pagebean = showapi_res_body.get("pagebean").getAsJsonObject();
            JsonArray contentlist = pagebean.get("contentlist").getAsJsonArray();
            //Log.i("logme","list:"+pagebean);
            for(int i = 0;i<contentlist.size();i++){
                AttractionEntity attEntity = new AttractionEntity();
                JsonObject item = contentlist.get(i).getAsJsonObject();
                attEntity.setAddressMore(item.get("address").getAsString());
                attEntity.setAddress(item.get("cityName").getAsString()+item.get("areaName").getAsString());
                attEntity.setName(item.get("name").getAsString());
                attEntity.setContent(item.get("summary").getAsString());
                Log.i("logme","get(\"name\"):"+item.get("name").getAsString());


                JsonArray picList = item.get("picList").getAsJsonArray();
                //Log.i("logme","get(pic):"+picList.size());
                //Log.i("logme","get(pic2):"+item.get("picList").getAsJsonObject());
                if(picList.size() != 0) {
                    JsonObject picUrl = picList.get(0).getAsJsonObject();
                    attEntity.setImageUrl(picUrl.get("picUrl").getAsString());
                }
                list.add(attEntity);
            }

        }
        return list;
    }
}
