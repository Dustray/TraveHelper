package bzu.skyn.travehelper.util;

import android.util.Log;


import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import bzu.skyn.travehelper.entity.WeatherJsonEntity;
import bzu.skyn.travehelper.tools.WeatherSignUrl;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SoapUtil {
    // 定义Web Service的命名空间
    static final String NAME_SPACE = "http://WebXml.com.cn/";
    // 定义Web Service提供服务的URL
    static final String SERVICE_URL = "http://ws.webxml.com.cn/WebServices/WeatherWebService.asmx";


    /**
     * 获得州，国内外省份和城市信息
     *
     * @return
     */
    public static List<String> getProvinceList() {

        // 需要调用的方法名(获得本天气预报Web Services支持的洲、国内外省份和城市信息)
        String methodName = "getSupportProvince";
        // 创建HttpTransportSE传输对象
        HttpTransportSE httpTranstation = new HttpTransportSE(SERVICE_URL);

        // 使用SOAP1.1协议创建Envelop对象
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // 实例化SoapObject对象
        SoapObject soapObject = new SoapObject(NAME_SPACE, methodName);
        envelope.bodyOut = soapObject;
        // 设置与.Net提供的Web Service保持较好的兼容性
        envelope.dotNet = true;
        try {
            // 调用Web Service
            httpTranstation.call(NAME_SPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                // 获取服务器响应返回的SOAP消息
                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject detail = (SoapObject) result.getProperty(methodName
                        + "Result");
                // 解析服务器响应的SOAP消息。
                return parseProvinceOrCity(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();

          //  Log.i("logme","cityerror1"+e.toString());
        }

        return null;
    }

    /**
     * 根据省份获取城市列表
     *
     * @param province
     * @return
     */
    public static List<String> getCityListByProvince(String province) {

        // 需要调用的方法名(获得本天气预报Web Services支持的城市信息,根据省份查询城市集合：带参数)
        String methodName = "getSupportCity";

        HttpTransportSE httpTranstation = new HttpTransportSE(SERVICE_URL);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        SoapObject soapObject = new SoapObject(NAME_SPACE, methodName);
        soapObject.addProperty("byProvinceName", province);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        try {
            // 调用Web Service
            httpTranstation.call(NAME_SPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                // 获取服务器响应返回的SOAP消息
                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject detail = (SoapObject) result.getProperty(methodName
                        + "Result");
                // 解析服务器响应的SOAP消息。
                return parseProvinceOrCity(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
           // Log.i("logme","cityerror2"+e.toString());
        }

        return null;

    }

    private static List<String> parseProvinceOrCity(SoapObject detail) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < detail.getPropertyCount(); i++) {
            String str = detail.getProperty(i).toString();
            // 解析出每个省份
            result.add(str.split(",")[0]);
        }
        return result;
    }

    public static List<WeatherJsonEntity> getWeatherByCity(String theCityName) {
/*
{
    "results":
    [
        {
            "location":
            {
                "id":"WTW3SJ5ZBJUY",
                "name":"上海",
                "country":"CN",
                "path":"上海,上海,中国",
                "timezone":"Asia/Shanghai",
                "timezone_offset":"+08:00"
            },
            "daily":
            [
                {
                    "date":"2017-10-25",
                    "text_day":"晴",
                    "code_day":"0",
                    "text_night":"晴",
                    "code_night":"1",
                    "high":"20",
                    "low":"13",
                    "precip":"",
                    "wind_direction":"东北",
                    "wind_direction_degree":"45",
                    "wind_speed":"10",
                    "wind_scale":"2"
                }
            ],
            "last_update":"2017-10-24T18:00:00+08:00"
        }
    ]
}
*/
        WeatherSignUrl wsu = new WeatherSignUrl();
        try {
            String url = wsu.generateGetDiaryWeatherURL(theCityName,"zh-Hans","c","0","3");

            JSONObject json = RequestUrl(url);
            List<WeatherJsonEntity> listWeather ;
            listWeather =  jsonWeather(json);
            JSONObject json2 = RequestUrl("https://api.seniverse.com/v3/life/suggestion.json?key=7nk4iowy524iry3l&location=shanghai&language=zh-Hans");

            listWeather.set(0, jsonLife(listWeather.get(0),json2));
            return listWeather;

        }catch (SignatureException e) {
            e.printStackTrace();
           // Log.i("logme","$error1:"+e.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
           // Log.i("logme","$error1:"+e.toString());
        }
        return null;
    }

    private static List<WeatherJsonEntity> jsonWeather(JSONObject json){

        Log.i("logme","StringWeather测试***:"+json);
        String jsonString = json.toString();
        List<WeatherJsonEntity> listWeather = new ArrayList<WeatherJsonEntity>();
        JsonParser parser = new JsonParser();// json 解析器
        JsonObject obj = (JsonObject) parser.parse(jsonString); /* 获取返回状态码 */
        //String resultcode = obj.get("results").getAsString(); /* 如果状态码是200说明返回数据成功*/
        if (true) {
            //Log.i("logme","%%%%%%%%%%%%%%%%");
            JsonArray futureWeatherArray = obj.get("results").getAsJsonArray();

            JsonObject jLocation = futureWeatherArray.get(0).getAsJsonObject();
            JsonObject itemLocation = jLocation.get("location").getAsJsonObject();
            JsonObject jDaily = futureWeatherArray.get(0).getAsJsonObject();
            JsonArray jaDaily= jDaily.get("daily").getAsJsonArray();


            for(int i = 0;i<3;i++){

                WeatherJsonEntity weather = new WeatherJsonEntity();

                JsonObject jWeather = jaDaily.get(i).getAsJsonObject();
                //Log.i("logme","nothiii:"+jDaily.get("daily").getAsJsonObject());
                weather.setCity(itemLocation.get("name").getAsString());
                weather.setCityCountry(itemLocation.get("path").getAsString());
                weather.setDate(jWeather.get("date").getAsString());
                weather.setWeatherDay(jWeather.get("text_day").getAsString());
                weather.setCodeDay(jWeather.get("code_day").getAsString());
                weather.setWeatherNight(jWeather.get("text_night").getAsString());
                weather.setHighTem(jWeather.get("high").getAsString());
                weather.setLowTem(jWeather.get("low").getAsString());
                weather.setWindDirection(jWeather.get("wind_direction").getAsString());
                weather.setWindSpeed(jWeather.get("wind_speed").getAsString());
                weather.setPrecip(jWeather.get("precip").getAsString());

                listWeather.add(weather);
            }
           // Log.i("logme","JsonCity:"+jWeather.get("text_day").getAsString());
        }
        return listWeather;
    }

    private static WeatherJsonEntity jsonLife(WeatherJsonEntity weather,JSONObject json) {
        if(json==null)return null;
        String jsonString = json.toString();

        JsonParser parser = new JsonParser();// json 解析器
        JsonObject obj = (JsonObject) parser.parse(jsonString); /* 获取返回状态码 */
        //String resultcode = obj.get("results").getAsString(); /* 如果状态码是200说明返回数据成功*/
        if (true) {
            JsonArray futureWeatherArray = obj.get("results").getAsJsonArray();
            JsonObject jLocation = futureWeatherArray.get(0).getAsJsonObject();
            JsonObject itemSuggestion = jLocation.get("suggestion").getAsJsonObject();
            //穿衣
            JsonObject itemDressing = itemSuggestion.get("dressing").getAsJsonObject();
            String dress = itemDressing.get("brief").getAsString();
            //紫外线
            JsonObject itemUv = itemSuggestion.get("uv").getAsJsonObject();
            String uv = itemUv.get("brief").getAsString();
            //洗车
            JsonObject itemCarWashing = itemSuggestion.get("car_washing").getAsJsonObject();
            String carWashing = itemCarWashing.get("brief").getAsString();
            //旅游
            JsonObject itemTravel = itemSuggestion.get("travel").getAsJsonObject();
            String travel = itemTravel.get("brief").getAsString();
            //感冒
            JsonObject itemFlu = itemSuggestion.get("flu").getAsJsonObject();
            String flu = itemFlu.get("brief").getAsString();
            //运动
            JsonObject itemSport = itemSuggestion.get("sport").getAsJsonObject();
            String sport = itemSport.get("brief").getAsString();

            Log.i("logme","$jlocation:"+itemDressing.get("brief").getAsString());
            weather.setLifeIndex("生活小贴士：穿衣："+dress+"；洗车："+carWashing+"；旅游："+travel+"；感冒："+flu+"；运动："+sport+"。");

            weather.setUv(uv);

            //Log.i("logme","JsonCity:"+jLocation.get("suggestion").getAsString());
        }
        return weather;
    }

    public static JSONObject RequestUrl(String url)
    {
        try {
            String result = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            result = EntityUtils.toString(response.getEntity());
            JSONObject object = new JSONObject(result);
            Log.i("HttpActivity", result);
            return object;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
