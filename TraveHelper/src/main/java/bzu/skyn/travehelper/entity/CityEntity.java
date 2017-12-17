package bzu.skyn.travehelper.entity;

import com.bigkoo.pickerview.model.IPickerViewData;

import org.litepal.crud.DataSupport;

/**
 * Created by Dustray on 2017/11/19 0019.
 */

public class CityEntity  extends DataSupport implements IPickerViewData {
    private String cityId;
    private String cityName;
    private String ids;
    private String names;
    private String proId;
    private String proName;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    @Override
    public String getPickerViewText() {
        return names;
    }
}
