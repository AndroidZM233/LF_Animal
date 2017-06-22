/*
 *
 * @author Echo
 * @created 2016.6.4
 * @email bairu.xu@speedatagroup.com
 * @version v1.0
 *
 */

package cn.geomobile.lfrfid.utils;

import android.annotation.SuppressLint;

import com.elsw.base.utils.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeFormatePresenter {

    private static final String TAG = LogUtil.DEGUG_MODE ? "TimeFormatePresenter"
            : TimeFormatePresenter.class.getSimpleName();
    private static final boolean debug = true;


    // 将时间转化为long类型 并计算距此刻的时间差 秒
    public static long getStrinTimeToLongTime(String startDate) {
        startDate=startDate+" 00:00:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = format.parse(startDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                date = format.parse(startDate);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        LogUtil.i(debug, TAG,
                "【TimeFormatePresenter.getStrinTimeToLongTime()】【date.getTime()="
                        + date.getTime() + "】");
        long currentTimeMillis = System.currentTimeMillis();
        long selectTimeMillis = date.getTime();
        long dates = selectTimeMillis - currentTimeMillis;
        return dates;
    }

    /**
     * yyyy年MM月dd日 HH:mm:ss 格式
     * 将时间转化为long类型// 年月日时分秒
     *
     * @param startDate
     * @return
     */
    public static long getStrinTimeToLongTimeMs(String startDate) {
        return getStrinTimeToLongTimeDay(startDate, "yyyy年MM月dd日 HH:mm:ss");
    }

    /**
     * yyyy-MM-dd HH:mm:ss 格式
     *
     * @param startDate
     * @return
     */
    public static long getStrinTimeToLongTimeM(String startDate) {
        return getStrinTimeToLongTimeDay(startDate, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * yyyy-MM-dd HH:mm:ss 格式
     *
     * @param startDate
     * @return
     */
    public static long getLongTimeMByStr(String startDate, String format) {
        return getStrinTimeToLongTimeDay(startDate, format);
    }

    // 将时间转化为long类型 //年月日
    public static long getStrinTimeToLongTimeDay(String startDate) {
        return getStrinTimeToLongTimeDay(startDate, "yyyy/MM/dd");
    }

    // 将时间转化为long类型 //年月日
    public static long getStrinTimeToLongTimeDay(String startDate,
                                                 String formatstr) {
        SimpleDateFormat format = new SimpleDateFormat(formatstr);
        Date date = null;
        if (!StringUtil.isBlank(startDate)) {
            try {
                date = format.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }
        } else {
            date = new Date();
        }


        long selectTimeMillis = date.getTime();
        return selectTimeMillis;
    }

    /**
     * 将yyyy-MM-dd 或者yyyy-MM-dd HH:mm 转换为yyyy-MM-dd HH:mm:ss
     *
     * @param datstr
     * @return
     */
    public static String auToCompleteDate(String datstr) {
        String res = null;
        if (datstr != null) {
            int length = datstr.length();
            if (length == 10) {
                res = new StringBuffer(datstr).append(" 08:00:00").toString();
            } else if (length == 16) {
                res = new StringBuffer(datstr).append(":00").toString();
            }
        }
        return res;
    }

    /**
     * 时间上下午格式化
     *
     * @param str
     * @return
     */
    public static String getHourString(String str) {
        if (str == null) {
            return str;
        }
        if (str.length() > 10) {
            LogUtil.i(debug, TAG, "【TimeFormatePresenter.getHourString()】【str=" + str + "】");
            String hourstring = str.substring(11, 13);//为了区分上下午
            int hour = Integer.parseInt(hourstring);
            LogUtil.i(debug, TAG, "【TimeFormatePresenter.getHourString()】【hour=" + hour + "】");
            if (hour < 12) {
                return /*"上午" + */str.substring(11, str.length() - 3);
            } else {
                return /*"下午" +*/ str.substring(11, str.length() - 3);
            }
        } else {
            return str;
        }

    }

    /**
     * @param title
     * @return
     */
    public static String getChineseDateStr(String title) {
        if (title != null) {
            int length = title.length();
            if (length >= 5) {
                String tmp = title.substring(0, 5);
                String[] split = tmp.split("-");
                if (split.length == 2) {
                    int month = Integer.parseInt(split[0]);
                    int day = Integer.parseInt(split[1]);
                    title = month + "月" + day + "日" + title.substring(5, length);
                }
            }
        } else {
            title = "";
        }
        return title;
    }

    public static String getNoSecond(String str) {
        if (str == null) {
            return str;
        }
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String s = String.valueOf(year);
        String years = str.substring(0, 4);
        LogUtil.i(debug, TAG, "【TimeFormatePresenter.getNoSecond()】【years=" + years + ",s=" + s + "】");
        if (s.equals(years)) {
            if (str.length() > 10) {
                return str.substring(5, str.length() - 3);
            } else {
                return str;
            }
        } else {
            if (str.length() > 10) {
                return str.substring(0, 10);
            } else {
                return str;
            }
        }
    }

    public static String getYearButNoSecond(String str) {
        if (str == null) {
            return str;
        }
        if (str.length() > 10) {
            return str.substring(0, str.length() - 3);
        } else {
            return str;
        }
    }

    public static String getCompleteSecond(String str) {
        if (str == null) {
            return str;
        }
        if (str.length() < 17) {
            return str + ":00";
        } else {
            return str;
        }
    }

    public static String getNoHourAndSecond(String str) {
        if (str == null) {
            return str;
        }
        if (str.length() > 10) {
//            return str.substring(5, 10);
            return str.substring(0, 10);
        } else {
            return str;
        }
    }

    // 将时间转化为String类型 并格式化显示时间
    public static String formateTimeDayToString(long startDate, String formatstr) {
        SimpleDateFormat format = new SimpleDateFormat(formatstr);
        Date date = new Date(startDate);
        String format2 = format.format(date);
        return format2;
    }

    /**
     * 时间戳到对应的时间
     *
     * @param timeStamp 时间戳
     * @return string时间
     */
    public static String getHourAndMinute(long timeStamp) {
        return formateTimeDayToString(timeStamp, "HH:mm");
    }

    /**
     * 时间戳到对应的时间
     *
     * @param timeStamp 时间戳
     * @return string时间
     */
    public static String getDay(long timeStamp) {
        return formateTimeDayToString(timeStamp, "yyyy-MM-dd");
    }

    /**
     * 时间戳到对应的时间
     *
     * @param timeStamp 时间戳
     * @return string时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeFromTimeStampMs(long timeStamp) {
        return formateTimeDayToString(timeStamp, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 时间戳到对应的时间
     *
     * @param timeStamp 时间戳
     * @return string时间
     */
    public static String getTimeFromTimeStampMin(long timeStamp) {
        return formateTimeDayToString(timeStamp, "yyyy-MM-dd HH:mm");
    }

    /**
     * 获取当前时间
     *
     * @return string时间
     */
    public static String getCurrentTime() {
        return formateTimeDayToString(System.currentTimeMillis(), "yyyyMMddHHmmss");
    }
    /**
     * 获取当前时间
     *
     * @return string时间
     */
    public static String getCurrentTime(String format) {
        return formateTimeDayToString(System.currentTimeMillis(), format);
    }

    /**
     * 获取当前时间
     *
     * @return string时间
     */
    public static String getYesterdayTime() {
        return formateTimeDayToString(System.currentTimeMillis() - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 获取今天之后的几天10:00
     *
     * @return
     */
    public static String getTimeAfterDay(int step) {
        return formateTimeDayToString(System.currentTimeMillis() + step * 24 * 60 * 60 * 1000, "yyyy-MM-dd ")+"10:00";
    }




    /**
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param mintue
     * @param seconds
     * @return
     */
    public static String getStringTime(int year, int month, int day, int hour,
                                       int mintue, int seconds) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-").append(month).append("-").append(day)
                .append(" ").append(hour).append(":").append(mintue)
                .append(":").append(seconds);
        return sb.toString();
    }

    /**
     * int类型的日期到对应的时间戳
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param mintue
     * @param seconds
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long getTimeStamp(int year, int month, int day, int hour,
                                    int mintue, int seconds) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-").append(month).append("-").append(day)
                .append(" ").append(hour).append(":").append(mintue)
                .append(":").append(seconds);
        long timeStemp = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = simpleDateFormat.parse(sb.toString());
            timeStemp = date.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return timeStemp;
    }

    /**
     * 将传入的时间传化为前开始时间和结束时间，间隔八小时
     *
     * @param time
     * @return
     */
    @SuppressWarnings("null")
    public static String[] changeTimeToStartAndEndTime(long time) {
        String[] playBackTime = new String[3];

        long fourHourTimeMs = 4 * 60 * 60 * 1000;
        long startTime = time - fourHourTimeMs;
        long endtTime = time + fourHourTimeMs;

        String startTimeStamp = getTimeFromTimeStampMs(startTime);
        String endTimeStamp = getTimeFromTimeStampMs(endtTime);
        String thistime = getTimeFromTimeStampMs(time);
        playBackTime[0] = startTimeStamp;
        playBackTime[1] = endTimeStamp;
        playBackTime[2] = thistime;
        return playBackTime;
    }

    /**
     * 将传入的时间传化为前开始时间和结束时间，间隔八小时
     *
     * @param timestr
     * @return
     */
    @SuppressWarnings("null")
    public static String[] changeTimeToStartAndEndTime(String timestr) {
        LogUtil.i(debug, TAG,
                "【TimeFormatePresenter.changeTimeToStartAndEndTime()】【timestr="
                        + timestr + "】");
        String[] playBackTime = new String[3];
        long time = getStrinTimeToLongTimeDay(timestr, "yyyy-MM-dd HH:mm:ss");
        long fourHourTimeMs = 4 * 60 * 60 * 1000;
        long startTime = time - fourHourTimeMs;
        long endtTime = time + fourHourTimeMs;

        String startTimeStamp = getTimeFromTimeStampMs(startTime);
        String endTimeStamp = getTimeFromTimeStampMs(endtTime);
        String thistime = getTimeFromTimeStampMs(time);
        playBackTime[0] = startTimeStamp;
        playBackTime[1] = endTimeStamp;
        playBackTime[2] = thistime;
        return playBackTime;
    }



    /**
     * 获取当前时间和1小时后的时间
     *
     * @param time
     * @return
     */
    public static String[] getPlayBackTime(long time) {
        String[] playBackTime = new String[2];
        long delayTimeMs = 1 * 60 * 60 * 1000;
        long endtTime = time + delayTimeMs;
        String thistime = getTimeFromTimeStampMs(time);
        String endTimeStamp = getTimeFromTimeStampMs(endtTime);
        playBackTime[0] = thistime;
        playBackTime[1] = endTimeStamp;
        return playBackTime;
    }


    /**
     * 例如"yyyy-MM-dd"
     *
     * @param datestr
     * @return
     */
    public static int getAgeFromDate(String datestr, String sdformat) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdformat);
        Date date = null;
        try {
            date = sdf.parse(datestr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            int now = calendar.get(Calendar.YEAR);
            calendar.setTime(date);
            int age = calendar.get(Calendar.YEAR);
            return now - age;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;

        }
    }

}
