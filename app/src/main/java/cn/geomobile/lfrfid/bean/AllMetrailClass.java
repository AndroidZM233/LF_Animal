package cn.geomobile.lfrfid.bean;

import com.elsw.base.db.orm.annotation.Column;
import com.elsw.base.db.orm.annotation.Id;
import com.elsw.base.db.orm.annotation.Table;

/**
 * Created by 张明_ on 2016/11/3.
 */
@Table(name = "allMetrail")
public class AllMetrailClass {
    @Id
    @Column(name = "ItemNM")
    // 内码
    private String ItemNM;

    @Column(name = "NSTime")
    // 年审日期
    private String NSTime;

    @Column(name = "id")
    // 扫描id
    private String id;

    @Column(name = "MYTime")
    // 免疫日期
    private String MYTime;

    @Column(name = "DQHTime")
    // 地区号存储日期
    private String DQHTime;

    public String getItemNM() {
        return ItemNM;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItemNM(String itemNM) {
        ItemNM = itemNM;
    }

    public String getNSTime() {
        return NSTime;
    }

    public void setNSTime(String NSTime) {
        this.NSTime = NSTime;
    }

    public String getMYTime() {
        return MYTime;
    }

    public void setMYTime(String MYTime) {
        this.MYTime = MYTime;
    }

    public String getDQHTime() {
        return DQHTime;
    }

    public void setDQHTime(String DQHTime) {
        this.DQHTime = DQHTime;
    }
}
