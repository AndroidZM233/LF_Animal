package cn.geomobile.lfrfid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import cn.geomobile.lfrfid.utils.SharedXmlUtil;

/**
 * Created by 张明_ on 2016/11/3.
 */

public abstract class MyBaseActivity extends Activity {
    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
    }
    protected abstract Context regieterBaiduBaseCount();
    protected void setXml(String key, String value) {
        SharedXmlUtil.getInstance(mContext).write(key, value);
    }

    protected String getXml(String key, String devalue) {
        String read = SharedXmlUtil.getInstance(mContext).read(key, devalue);
        return read;
    }
}
