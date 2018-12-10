package ts.trainticket.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtil {

    public static Calendar getToday() {
        return Calendar.getInstance();
    }


    public static Calendar getLastDay() {
        Calendar lastDay = getToday();
        lastDay.add(Calendar.DAY_OF_MONTH, 30 - 1);
        return lastDay;
    }

    // 比较时间时分秒的大小
    public static boolean compareTimeDate(String beginTime) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        try {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String dateNowStr = sdf.format(d);
            Date dt1 = df.parse(dateNowStr);//将字符串转换为date类型
            Date dt2 = df.parse(beginTime);
            if (dt1.getTime() > dt2.getTime())//比较时间大小,如果dt1大于dt2
                return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean compare_date(String DATE2) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String DATE1 = sdf.format(d);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 比较两个日期在年月日上的大小
     *
     * @return 1 表示 参数1 比 参数2 日期大, 0 表示相等
     */
    public static int compareCalendar(Calendar minuend, Calendar subtrahend) {
        int fy = minuend.get(Calendar.YEAR);
        int fm = minuend.get(Calendar.DAY_OF_YEAR);
        int sy = subtrahend.get(Calendar.YEAR);
        int sm = subtrahend.get(Calendar.DAY_OF_YEAR);

        if (fy > sy || fy == sy && fm > sm) {
            return 1;
        } else if (fy == sy && fm == sm) {
            return 0;
        } else {
            return -1;
        }
    }


    /**
     * 比较两个时间在小时分钟上的大小，1为前一个大于后一个，-1为前一个小于后一个
     */
    public static int compareDate(Calendar minuend, Calendar subtrahend) {
        int fh = minuend.get(Calendar.HOUR_OF_DAY);
        int fm = minuend.get(Calendar.MINUTE);
        int sh = subtrahend.get(Calendar.HOUR_OF_DAY);
        int sm = subtrahend.get(Calendar.MINUTE);

        if (fh > sh || fh == sh && fm > sm) {
            return 1;
        } else if (fh == sh && fm == sm) {
            return 0;
        } else {
            return -1;
        }
    }


    /**
     * 获得一个时间是否超出范围
     *
     * @return true 表示没有超出范围
     */
    public static boolean isDateInRange(Calendar date, Calendar min, Calendar max) {
        return compareDate(date, min) * compareDate(max, date) >= 0;
    }


    /**
     * 获得天单位的Format
     */
    public static String getDayFormatStr(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(calendar.getTime());
    }


    /**
     * 获得一个0点的日历
     */
    public static Calendar get0clock() {
        Calendar calendar0 = Calendar.getInstance();
        calendar0.set(Calendar.HOUR_OF_DAY, 0);
        calendar0.set(Calendar.MINUTE, 0);
        return calendar0;
    }


    /**
     * 获得一个23:59的日历
     */
    public static Calendar get2359clock() {
        Calendar calendar2359 = Calendar.getInstance();
        calendar2359.set(Calendar.HOUR_OF_DAY, 23);
        calendar2359.set(Calendar.MINUTE, 59);
        return calendar2359;
    }


    /**
     * 通过 HH:mm 获得一个 Calendar
     */
    public static Calendar getCalendarByStr(String time) {
        String[] split = time.trim().split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(split[1]));
        return calendar;
    }


    /**
     * 通过 yyyy-MM-dd 获得一个 Calendar
     */
    public static Calendar getYearCalendarByStr(String time) {
        String[] split = time.trim().split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(split[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(split[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(split[2]));
        return calendar;
    }


    /**
     * 复制后一个 Calendar 的 小时、分钟 到前一个中，
     */
    public static void copyTimeToFirst(Calendar first, Calendar second) {
        first.set(Calendar.HOUR_OF_DAY, second.get(Calendar.HOUR_OF_DAY));
        first.set(Calendar.MINUTE, second.get(Calendar.MINUTE));
    }


    /**
     * 通过yyyy-MM-dd HH:mm:SS 获得Calendar
     */
    public static Calendar getCalendarByDateTime(String dateTime) {
        Calendar calendar = Calendar.getInstance();
        String[] split = dateTime.split("[- :.]");
        calendar.set(Calendar.YEAR, Integer.parseInt(split[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(split[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(split[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[3]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(split[4]));
        calendar.set(Calendar.SECOND, Integer.parseInt(split[5]));
        return calendar;
    }

    // 通过timeStamp 获得时分秒
    public static String getHMS(String timeStamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res.substring(11, 19);
    }

    public static String getHM(String timeStamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res.substring(11, 16);
    }


    public static String getHMSMin(Long t1, Long t2) {
        long number = t1 - t2;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(number);
        System.out.println(number + " = " + formatter.format(calendar.getTime()));
        return formatter.format(calendar.getTime());
    }

    public static String getDate(String timeStamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getYMD(String timeStamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (day != 0)
            return day + "" + hour + "h" + min + "m";
        else if (hour != 0)
            return hour + "h" + min + "m";
        else if (min != 0)
            return min + "m";
        return "0s";
    }

    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String tempTime = datetime.substring(5,10);
        if('0' == tempTime.charAt(0))
            tempTime = tempTime.substring(1,4);
        tempTime = tempTime.replaceAll("-","-");

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return tempTime +" "+ weekDays[w];
    }


}
