package starter.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间工具类
 *
 * @author zhyf
 */
@Slf4j
public class DateUtil {

    private static final String yyyyMMdd = "yyyyMMdd";

    private static final String yyyyMMddHH = "yyyyMMddHH";

    private static final String yyyy_MM_dd = "yyyy-MM-dd";

    private static final String yyyy_MM = "yyyy-MM";

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat(DEFAULT_PATTERN);

    private static SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat(yyyyMMdd);

    private static SimpleDateFormat YYYY_MM_DD_FORMAT = new SimpleDateFormat(yyyy_MM_dd);

    private static SimpleDateFormat yyyyMMddHH_FORMAT = new SimpleDateFormat(yyyyMMddHH);

    private static SimpleDateFormat yyyy_MM_FORMAT = new SimpleDateFormat(yyyy_MM);

    public static Date getCurrentTime() {
        return new Date();
    }

    public static synchronized Date parse(String time) {
        if (StringUtil.isEmpty(time)) {
            return null;
        }
        try {
            return DEFAULT_FORMAT.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized Date parse(String time, String pattern) {
        if (StringUtil.isEmpty(pattern) || StringUtil.isEmpty(time)) {
            return null;
        }
        try {
            if (pattern.equalsIgnoreCase(yyyyMMdd)) {
                return YYYYMMDD_FORMAT.parse(time);
            }
            if (pattern.equalsIgnoreCase(yyyy_MM_dd)) {
                return YYYY_MM_DD_FORMAT.parse(time);
            }
            if (pattern.equalsIgnoreCase(yyyyMMddHH)) {
                return yyyyMMddHH_FORMAT.parse(time);
            }
            if (pattern.equalsIgnoreCase(yyyy_MM)) {
                return yyyyMMddHH_FORMAT.parse(time);
            }
            if (pattern.equalsIgnoreCase(DEFAULT_PATTERN)) {
                return DEFAULT_FORMAT.parse(time);
            }
            SimpleDateFormat dateFromat = new SimpleDateFormat(pattern);
            return dateFromat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转成默认格式yyyy-MM-dd HH:mm:ss
     */
    public static synchronized String format(Date date) {
        if (date == null) {
            return null;
        }
        return DEFAULT_FORMAT.format(date);
    }

    /**
     * 将Date类型的日期转换为参数定义的格式的字符串。
     */
    public static synchronized String format(Date date, String pattern) {
        if (date == null || StringUtil.isEmpty(pattern)) {
            return null;
        }
        if (pattern.equalsIgnoreCase(yyyyMMdd)) {
            return YYYYMMDD_FORMAT.format(date);
        }
        if (pattern.equalsIgnoreCase(yyyy_MM_dd)) {
            return YYYY_MM_DD_FORMAT.format(date);
        }
        if (pattern.equalsIgnoreCase(yyyyMMddHH)) {
            return yyyyMMddHH_FORMAT.format(date);
        }
        if (pattern.equalsIgnoreCase(yyyy_MM)) {
            return yyyy_MM_FORMAT.format(date);
        }
        SimpleDateFormat dateFromat = new SimpleDateFormat(pattern);
        return dateFromat.format(date);
    }

    /**
     * 获取每天开始时间
     */
    public static Date getDateStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取每天结束时间
     */
    public static Date getDateEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 是否在两个时间范围内。
     */
    public static boolean isDateBetwon(Date date, Date d1, Date d2) {
        return d1.before(date) && d2.after(date);
    }

    /**
     * 获取当前时间之前或之后几分钟 minute
     */
    public static Date getDateByMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 获取指定时间之前或之后几分钟 minute
     */
    public static Date getDateByMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

}
