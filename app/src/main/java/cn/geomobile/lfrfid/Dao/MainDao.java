package cn.geomobile.lfrfid.Dao;

import android.content.Context;

import com.elsw.base.db.orm.dao.ABaseDao;

import cn.geomobile.lfrfid.bean.AllMetrailClass;

/**
 * Created by 张明_ on 2016/11/3.
 */

public class MainDao extends ABaseDao<AllMetrailClass> {
    public MainDao(Context context) {
        super(new DBInsideHelper(context),  AllMetrailClass.class);
    }
}
