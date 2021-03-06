package cn.geomobile.lfrfid;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cn.geomobile.lfrfid.Dao.MainDao;
import cn.geomobile.lfrfid.bean.AllMetrailClass;
import cn.geomobile.lfrfid.view.SelectDateDialog;

/**
 * Created by 张明_ on 2016/11/3.
 */

public class DJActivity extends MyBaseActivity {
    private Button saveBtn;
    private TextView NStv,MYtv;
    private EditText DFXXet;
    private AllMetrailClass allMetrailClass;
    private MainDao mainDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainDao = new MainDao(mContext);
        allMetrailClass = new AllMetrailClass();
        initUI();
        String Id = getXml("itemId", "");
        DFXXet.setText(Id.substring(0,4));
    }

    private void initUI() {
        setContentView(R.layout.act_dj);
        saveBtn= (Button) findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Id = getXml("itemId", "");
                UUID uuid = UUID.randomUUID();
                String nsTime = NStv.getText().toString();
                String myTime = MYtv.getText().toString();
                String dfdjTV = DFXXet.getText().toString();
                if (TextUtils.isEmpty(Id) || TextUtils.isEmpty(nsTime) ||
                        TextUtils.isEmpty(myTime) || TextUtils.isEmpty(dfdjTV)){
                    Toast.makeText(mContext,"信息不能为空，请全部填写",Toast.LENGTH_SHORT).show();
                    return;
                }
                allMetrailClass.setItemNM(String.valueOf(uuid));
                allMetrailClass.setMYTime(myTime);
                allMetrailClass.setNSTime(nsTime);
                allMetrailClass.setDQHTime(dfdjTV);
                allMetrailClass.setId(Id);
                mainDao.imInsert(allMetrailClass);
                Toast.makeText(mContext,"登记成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        NStv= (TextView) findViewById(R.id.tv_nianshen);
        NStv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                SelectDateDialog dateDialog = new SelectDateDialog(mContext, new SelectDateDialog
                        .ShowDate() {
                    @Override
                    public void showDate(String time) {
                        NStv.setText(time);
                    }
                }, str);
                dateDialog.show();
            }
        });


        MYtv= (TextView) findViewById(R.id.tv_nianyi);
        MYtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                SelectDateDialog dateDialog = new SelectDateDialog(mContext, new SelectDateDialog
                        .ShowDate() {
                    @Override
                    public void showDate(String time) {
                        MYtv.setText(time);
                    }
                }, str);
                dateDialog.show();
            }
        });
        DFXXet= (EditText) findViewById(R.id.et_difangxinxi);
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }
}
