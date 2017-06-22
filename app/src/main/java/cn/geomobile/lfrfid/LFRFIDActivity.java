package cn.geomobile.lfrfid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.lflibs.DeviceControl;
import com.android.lflibs.serial_native;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.geomobile.lfrfid.Dao.MainDao;
import cn.geomobile.lfrfid.bean.AllMetrailClass;
import cn.geomobile.lfrfid.utils.TimeFormatePresenter;

public class LFRFIDActivity extends MyBaseActivity implements OnCheckedChangeListener, OnClickListener {
    /**
     * Called when the activity is first created.
     */
    private DeviceControl DevCtrl;
    private static final String SERIALPORT_PATH = "/dev/ttyMT2";
    private static final int BUFSIZE = 64;

    private ToggleButton powerBtn;
    private Button clearBtn;
    private Button closeBtn;
    private Button DJBtn;
    private TextView contView,noInformation;
    private LinearLayout haveInformation,ll_main;
    private File device_path;
    private BufferedWriter proc;
    private serial_native NativeDev;
    private Handler handler;
    private ReadThread reader;
    //	private long dec_result;
    private int size = 0;
    private byte xor_result = 0;
    private int count0 = 0;
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;
    private int count5 = 0;
    private AllMetrailClass allMetrailClass;
    private MainDao mainDao;
    private TextView NStv,MYtv;
    private TextView DFXXet;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mainDao = new MainDao(mContext);
        initSoundPool();
        allMetrailClass = new AllMetrailClass();

        noInformation= (TextView) findViewById(R.id.tv_no_information);
        haveInformation= (LinearLayout) findViewById(R.id.ll_information);
        ll_main= (LinearLayout) findViewById(R.id.ll_main);
        NStv= (TextView) findViewById(R.id.tv_nianshen);
        MYtv= (TextView) findViewById(R.id.tv_nianyi);
        DFXXet= (TextView) findViewById(R.id.et_difangxinxi);


        powerBtn = (ToggleButton) findViewById(R.id.toggleButton_power);
        powerBtn.setOnCheckedChangeListener(this);

        clearBtn = (Button) findViewById(R.id.button_clear);
        clearBtn.setOnClickListener(this);
//        clearBtn.setEnabled(false);

        closeBtn = (Button) findViewById(R.id.button_close);
        closeBtn.setOnClickListener(this);

        DJBtn = (Button) findViewById(R.id.button_dengji);
        DJBtn.setOnClickListener(this);
//        DJBtn.setEnabled(false);

        contView = (TextView) findViewById(R.id.tv_content);

        NativeDev = new serial_native();
        if (NativeDev.OpenComPort(SERIALPORT_PATH) < 0) {
            contView.setText(R.string.Status_OpenSerialFail);
            powerBtn.setEnabled(false);
            clearBtn.setEnabled(false);
            return;
        }

        try {
            DevCtrl = new DeviceControl("/sys/class/misc/mtgpio/pin");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            contView.setText(R.string.Status_OpenDevFileFail);
            powerBtn.setEnabled(false);
            clearBtn.setEnabled(false);
            new AlertDialog.Builder(this).setTitle(R.string.DIA_ALERT).setMessage(R.string.DEV_OPEN_ERR).setPositiveButton(R.string.DIA_CHECK, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    finish();
                }
            }).show();
            return;
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    byte[] buf = (byte[]) msg.obj;
                    if (buf.length == 30) {
                        for (int a = 1; a < 27; a++) {
                            xor_result ^= buf[a];
                        }
                        String cnt = new String(buf);
                        String[] serial_number = new String[30];
                        serial_number[9] = cnt.substring(1, 2);
                        serial_number[8] = cnt.substring(2, 3);
                        serial_number[7] = cnt.substring(3, 4);
                        serial_number[6] = cnt.substring(4, 5);
                        serial_number[5] = cnt.substring(5, 6);
                        serial_number[4] = cnt.substring(6, 7);
                        serial_number[3] = cnt.substring(7, 8);
                        serial_number[2] = cnt.substring(8, 9);
                        serial_number[1] = cnt.substring(9, 10);
                        serial_number[0] = cnt.substring(10, 11);
                        String reverse = serial_number[0] + serial_number[1] + serial_number[2] + serial_number[3] + serial_number[4] + serial_number[5] + serial_number[6] + serial_number[7] + serial_number[8] + serial_number[9];
                        long dec_first = Long.parseLong(reverse, 16);
                        String string = Long.toString(dec_first);
                        size = string.length();
                        switch (size) {
                            case 1:// if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//						    		      contView.append("\n");
                                //   break;
                            }
                            break;
                            case 2: // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//						    		      contView.append("\n");
                                //   break;
                            }
                            break;
                            case 3: // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//							    		  contView.append("\n");
                                // break;
                            }
                            break;
                            case 4:   //if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + "0" + "0" + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//							    		  contView.append("\n");
                                //  break;
                            }
                            break;
                            case 5:  // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + "0" + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//							    		  contView.append("\n");
                                //  break;
                            }
                            break;
                            case 6:  // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//							    		  contView.append("\n");
                                //  break;
                            }
                            break;
                            case 7:   // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//							    		  contView.append("\n");
                                // break;
                            }
                            break;
                            case 8:   // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//							    		  contView.append("\n");
                                //  break;
                            }
                            break;
                            case 9:   // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//							    		  contView.append("\n");
                                //  break;
                            }
                            break;
                            case 10:  // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//							    		  contView.append("\n");
                                //	  break;
                            }
                            break;
                            case 11:   //if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + "0" + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//							    		  contView.append("\n");
                                //	  break;
                            }
                            break;
                            case 12:   // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14, 15);
                                serial_number[11] = cnt.substring(13, 14);
                                serial_number[12] = cnt.substring(12, 13);
                                serial_number[13] = cnt.substring(11, 12);
                                String country_code = serial_number[11] + serial_number[12] + serial_number[13];
                                long dec_result = Long.parseLong(country_code, 16);
                                String second_dec = Long.toString(dec_result);
                                String combine = second_dec + string;
                                contView.setTextSize(15);
                                contView.setText("0"+combine);
                                powerBtn.setChecked(false);
                                selectInformation();
//								    		  contView.append("\n");
                                //  break;
                            }
                            break;
                        }
                    } else {
                        String cnt = new String(buf);
                        count0 = Integer.parseInt(cnt.substring(1, 3), 16);
                        count1 = Integer.parseInt(cnt.substring(3, 5), 16);
                        count2 = Integer.parseInt(cnt.substring(5, 7), 16);
                        count3 = Integer.parseInt(cnt.substring(7, 9), 16);
                        count4 = Integer.parseInt(cnt.substring(9, 11), 16);
                        count5 = count0 ^ count1 ^ count2 ^ count3 ^ count4;
                        byte[] b = new byte[4];
                        b[0] = (byte) (count5 & 0xff);
                        if (b[0] == buf[11]) {
                            contView.setTextSize(15);
                            contView.setText("0"+cnt.substring(1, cnt.length() - 2));
                            powerBtn.setChecked(false);
                            selectInformation();
//						     contView.append("\n");
                        }
                    }
                }
            }
        };
    }

    private void selectInformation() {
        List<AllMetrailClass> allMetrailClassList=mainDao.imRawQuery
                ("select * from allMetrail where id=? "
                        , new String[]{contView.getText().toString()}
                        ,AllMetrailClass.class);
        if (allMetrailClassList.size()>0){
            haveInformation.setVisibility(View.VISIBLE);
            noInformation.setVisibility(View.GONE);
            AllMetrailClass allMetrailClass = allMetrailClassList.get(0);
            NStv.setText(allMetrailClass.getNSTime());
            String myTime = allMetrailClass.getMYTime();
            MYtv.setText(myTime);
            DFXXet.setText(allMetrailClass.getDQHTime());
            long strinTimeToLongTimeDay =
                    TimeFormatePresenter.getStrinTimeToLongTime(myTime);
            long yearMS=-31536000000L;
            if (strinTimeToLongTimeDay<=yearMS){
                ll_main.setBackgroundColor(Color.parseColor("#ff0000"));
                play(1, 2);
            }else {
                ll_main.setBackgroundColor(Color.parseColor("#FF04C243"));
                play(2, 0);
            }

        }else {
            haveInformation.setVisibility(View.GONE);
            noInformation.setVisibility(View.VISIBLE);
            noInformation.setText("没有查询到有关信息，请进行登记");
            ll_main.setBackgroundColor(Color.parseColor("#b0000000"));
        }
    }

    private SoundPool sp; //声音池
    private Map<Integer, Integer> mapSRC;
    //初始化声音池
    private void initSoundPool() {
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mapSRC = new HashMap<Integer, Integer>();
        mapSRC.put(1, sp.load(this, R.raw.error, 0));
        mapSRC.put(2, sp.load(this, R.raw.scan, 0));
    }

    /**
     * 播放声音池的声音
     */
    private void play(int sound, int number) {
        sp.play(mapSRC.get(sound),//播放的声音资源
                1.0f,//左声道，范围为0--1.0
                1.0f,//右声道，范围为0--1.0
                0, //优先级，0为最低优先级
                number,//循环次数,0为不循环
                1);//播放速率，0为正常速率
    }
    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    public void onDestroy() {
        if (sp != null) {
            sp.release();
        }
        if (powerBtn.isChecked()) {
            try {
                reader.interrupt();
                DevCtrl.PowerOffDevice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        NativeDev.CloseComPort();
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        // TODO Auto-generated method stub
        if (arg1) {
            try {
                contView.setText("");
                DevCtrl.PowerOnDevice();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                NativeDev.ClearBuffer();
                reader = new ReadThread();
                reader.start();
//				contView.setText(" status is " + powerBtn.isChecked());
            } catch (IOException e) {
                contView.setText(R.string.Status_ManipulateFail);
            }
        } else {
            try {
                reader.interrupt();
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                DevCtrl.PowerOffDevice();
//				contView.setText(" status is " + powerBtn.isChecked());
            } catch (IOException e) {
                contView.setText(R.string.Status_ManipulateFail);
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        String itemId = contView.getText().toString();
        if (TextUtils.isEmpty(itemId)){
            Toast.makeText(this,"请先进行扫描",Toast.LENGTH_SHORT).show();
            return;
        }
        List<AllMetrailClass> allMetrailClassList=mainDao.imRawQuery
                ("select * from allMetrail where id=? "
                        , new String[]{itemId},AllMetrailClass.class);
        if (arg0 == clearBtn) {

            if (allMetrailClassList.size()==0){
                Toast.makeText(mContext,"没有找到有关扫描ID信息，请先登记",Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            setXml("itemId",itemId);
            contView.setText("");
            Intent intent=new Intent(LFRFIDActivity.this,XGActivity.class);
            startActivity(intent);
        } else if (arg0 == DJBtn) {
            if (allMetrailClassList.size()!=0){
                Toast.makeText(mContext,"数据库中已有扫描ID信息，请点击修改",Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            setXml("itemId",itemId);
            contView.setText("");
            Intent intent=new Intent(LFRFIDActivity.this,DJActivity.class);
            startActivity(intent);
        }

    }

    class ReadThread extends Thread {
        public void run() {
            super.run();
            Log.d("lfrfid", "thread start");
            while (!isInterrupted()) {
                byte[] buf = NativeDev.ReadPort(BUFSIZE);
                if (buf != null) {
                    Message msg = new Message();
/*					for(byte a: buf)
                    {
						Log.d("lfrfid", String.format("%02x", a));
					}*/

                    if (buf.length >= 2) {
                        size = 0;
                        msg.what = 1;
                        msg.obj = buf;
                        handler.sendMessage(msg);
                    }
                }
            }
            Log.d("lfrfid", "thread stop");
        }
    }
}