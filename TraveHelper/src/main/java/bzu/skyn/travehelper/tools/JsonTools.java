package bzu.skyn.travehelper.tools;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import bzu.skyn.travehelper.entity.AttractionEntity;
import bzu.skyn.travehelper.entity.CityEntity;
import bzu.skyn.travehelper.entity.ProvinceEntity;
import bzu.skyn.travehelper.entity.WeatherJsonEntity;

/**
 * Created by Dustray on 2017/10/28 0028.
 */

public class JsonTools {

    public static List<AttractionEntity> jsonAttraction(String jsonString) throws NetworkErrorException{
        if(jsonString==null){
            throw new NullPointerException();
        }
        List<AttractionEntity> list =new ArrayList<AttractionEntity>();
        JsonParser parser = new JsonParser();// json 解析器
        JsonObject obj;
        try {
             obj = (JsonObject) parser.parse(jsonString); /* 获取返回状态码 */
        }catch(JsonSyntaxException e){
            throw new NetworkErrorException();
        }
        String resultCode = obj.get("showapi_res_code").getAsString(); /* 如果状态码是200说明返回数据成功*/
        if (0 == Integer.parseInt(resultCode)) {
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
                //Log.i("logme","get(\"name\"):"+item.get("name").getAsString());


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
    public static List<ProvinceEntity> jsonProvince(String jsonString){
        List<ProvinceEntity> list =new ArrayList<ProvinceEntity>();

        JsonParser parser = new JsonParser();// json 解析器
        JsonObject obj = (JsonObject) parser.parse(jsonString); /* 获取返回状态码 */
        String resultCode = obj.get("showapi_res_code").getAsString(); /* 如果状态码是200说明返回数据成功*/
        if (0 == Integer.parseInt(resultCode)) {
            JsonObject showapi_res_body = obj.get("showapi_res_body").getAsJsonObject();
            //Log.i("logme","listsssssss:"+showapi_res_body);
            JsonArray contentlist =showapi_res_body.get("list").getAsJsonArray();

            //Log.i("logme","contentlist:"+contentlist.get(1).getAsJsonObject());
            //Log.i("logme","list:"+pagebean);
            for(int i = 0;i<contentlist.size();i++){
                ProvinceEntity cEntity = new ProvinceEntity();
                JsonObject item = contentlist.get(i).getAsJsonObject();
                if(item.get("cityId") !=null)continue;
                //Log.i("logme","list123:"+item);
                //cEntity.setCityId(item.get("cityId").getAsString());
                //cEntity.setCityName(item.get("cityName").getAsString());
                cEntity.setIds(item.get("id").getAsString());
                cEntity.setNames(item.get("name").getAsString());


                list.add(cEntity);
            }
        }
        return list;
    }

    public static List<CityEntity> jsonCity(String jsonString){
        List<CityEntity> list =new ArrayList<CityEntity>();

        JsonParser parser = new JsonParser();// json 解析器
        JsonObject obj = (JsonObject) parser.parse(jsonString); /* 获取返回状态码 */
        String resultCode = obj.get("showapi_res_code").getAsString(); /* 如果状态码是200说明返回数据成功*/
        if (0 == Integer.parseInt(resultCode)) {
            JsonObject showapi_res_body = obj.get("showapi_res_body").getAsJsonObject();
            //Log.i("logme","list:"+showapi_res_body);
            JsonArray contentlist =showapi_res_body.get("list").getAsJsonArray();

           // Log.i("logme","contentlist:"+contentlist.get(1).getAsJsonObject());
            //Log.i("logme","list:"+pagebean);
            for(int i = 0;i<contentlist.size();i++){
                CityEntity cEntity = new CityEntity();
                JsonObject item = contentlist.get(i).getAsJsonObject();
                if(item.get("cityId") !=null)continue;
                //cEntity.setCityId(item.get("cityId").getAsString());
                //cEntity.setCityName(item.get("cityName").getAsString());
                cEntity.setIds(item.get("id").getAsString());
                cEntity.setNames(item.get("name").getAsString());

                cEntity.setProId(item.get("proId").getAsString());
                cEntity.setProName(item.get("proName").getAsString());
                //Log.i("logme","get测试:"+item.get("proId").getAsString()+item.get("name").getAsString());
                if(cEntity.getNames()==null){
                    cEntity.setNames("本省无信息");
                }
                list.add(cEntity);
            }
        }
       //Log.i("logme","String测试***:"+(jsonString.length()>100?"一个":jsonString));
//        Log.i("logme","list测试:"+list.get(0).getProName());

        return list;
    }
}
