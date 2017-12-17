package bzu.skyn.travehelper.entity;

import com.bigkoo.pickerview.model.IPickerViewData;

import org.litepal.crud.DataSupport;

/**
 * Created by Dustray on 2017/11/19 0019.
 */

public class ProvinceEntity extends DataSupport implements IPickerViewData {
    private String ids;
    private String names;

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

    @Override
    public String getPickerViewText() {
        return names;
    }
}
