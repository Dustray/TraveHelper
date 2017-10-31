package bzu.skyn.travehelper.entity;

import java.util.List;

/**
 * Created by Dustray on 2017/10/24 0024.
 */

public class WeatherJsonEntity {

    private String city;
    private String cityCountry;
    private String date;
    private String weatherDay;
    private String weatherNight;
    private String highTem;
    private String lowTem;
    private String windDirection;
    private String windSpeed;
    private String lifeIndex;
    private String uv;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public void setCityCountry(String cityCountry) {
        this.cityCountry = cityCountry;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherDay() {
        return weatherDay;
    }

    public void setWeatherDay(String weatherDay) {
        this.weatherDay = weatherDay;
    }

    public String getWeatherNight() {
        return weatherNight;
    }

    public void setWeatherNight(String weatherNight) {
        this.weatherNight = weatherNight;
    }

    public String getHighTem() {
        return highTem;
    }

    public void setHighTem(String highTem) {
        this.highTem = highTem;
    }

    public String getLowTem() {
        return lowTem;
    }

    public void setLowTem(String lowTem) {
        this.lowTem = lowTem;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getLifeIndex() {
        return lifeIndex;
    }

    public void setLifeIndex(String lifeIndex) {
        this.lifeIndex = lifeIndex;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }
}
