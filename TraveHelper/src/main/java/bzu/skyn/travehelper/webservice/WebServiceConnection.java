package bzu.skyn.travehelper.webservice;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Dustray on 2018/2/3 0003.
 */

public class WebServiceConnection {

    private String result;

    public String getRemoteInfo(Map<String, String> parameter, String methodName)
            throws Exception {
        Log.e("logme", "景点更新1");
        String WSDL_URI = "http://192.168.31.221:8080/TraveService/AttractionsPort?wsdl";// wsdl的uri
        String namespace = "http://api.service.trave/";// namespace
        //methodName = "getSynTime";// 要调用的方法名称

        SoapObject request = new SoapObject(namespace, methodName);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        if (parameter.size() != 0) {
            for (String key : parameter.keySet()) {
                request.addProperty(key, parameter.get(key));

            }
        }
        // 创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapSerializationEnvelope.VER10);
        envelope.bodyOut = request;// 由于是发送请求，所以是设置bodyOut
        envelope.dotNet = false;// 是否是.net开发的webservice
        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        httpTransportSE.call(namespace + methodName, envelope);// 调用
        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        //Log.e("logme", "景点更新2");
        //判断成功否
        if (envelope.getResponse() == "") {
            Log.e("logme", "景点更新失败");
            throw new TimeoutException();
        }
        // 获取返回的结果
        try {
            result = object.getProperty(0).toString();
        } catch (Exception e) {
            result = "失败";

            Log.e("logme", "景点更新失败" + e.toString());
            throw new SocketTimeoutException();
        }
        //Log.e("logme", "景点更新3"+result);
        return result;
    }

    public String getRemoteStringInfo(Map<String, String> parameter, String methodName)
            throws Exception {
        String WSDL_URI = "http://192.168.31.221:8080/TraveService/WeatherPort?wsdl";// wsdl的uri
        String namespace = "http://api.service.trave/";// namespace
        //methodName = "getSynTime";// 要调用的方法名称

        SoapObject request = new SoapObject(namespace, methodName);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        if (parameter.size() != 0) {
            for (String key : parameter.keySet()) {
                request.addProperty(key, parameter.get(key));

            }
        }
        // 创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapSerializationEnvelope.VER10);
        envelope.bodyOut = request;// 由于是发送请求，所以是设置bodyOut
        envelope.dotNet = false;// 是否是.net开发的webservice
        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        httpTransportSE.call(namespace + methodName, envelope);// 调用
        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        //判断成功否
        if (envelope.getResponse() == null) {
            Log.e("logme", "湿度更新失败");
            throw new SocketTimeoutException();
        }
        // 获取返回的结果
        try {
            result = object.getProperty(0).toString();
        } catch (Exception e) {
            result = "失败";
            Log.e("logme", "湿度更新失败" + e.toString());
            throw new SocketTimeoutException();
        }
        return result;
    }
}
