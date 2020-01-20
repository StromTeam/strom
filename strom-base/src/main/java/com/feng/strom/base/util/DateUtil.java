
package com.feng.strom.base.util;

import com.feng.strom.base.constants.DatePattern;
import org.apache.commons.lang3.StringUtils;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间操作类
 *
 * @author
 */
public final class DateUtil {

    /**
     * 获得当前的日期毫秒
     *
     * @return
     */
    public static long nowTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获得当前的时间戳
     *
     * @return
     */
    public static Timestamp nowTimeStamp() {
        return new Timestamp(nowTimeMillis());
    }
   /**
     * 获得当前的时间获取指定时间的时间戳（精确到毫秒）
     * @param date
     * @return Long
     */
    public static Long getTimeStampsByDate(Date date) {
        return date.getTime();
    }

    /**
     * 格式化日期为字符串
     *
     * @param date 时间对象
     * @return 返回固定格式为"yyyy-MM-dd HH:mm:ss"的时间字符串
     */
    public static String format(Date date) {
        return format(date, DatePattern.DATETIME_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 格式化日期为字符串
     *
     * @param date    时间对象
     * @param pattern 接收符合时间格式化规则的字符串模板
     * @return 返回符合时间格式化模板"pattern"规则的时间字符串
     */
    public static String format(Date date, String pattern) {
        return format(date, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期为字符串
     *
     * @param date      时间对象
     * @param formatter {@link java.time.format.DateTimeFormatter}
     * @return 返回符合时间格式化模板"pattern"规则的时间字符串
     */
    public static String format(Date date, DateTimeFormatter formatter) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(DatePattern.DATETIME_YYYY_MM_DD_HH_MM_SS);
        }

        return formatter.format(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    /**
     * LocalDateTime类型转为Date
     *
     * @param localDateTime LocalDateTime object
     * @return Date object
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date类型转换为LocalDateTime
     *
     * @param date 时间对象
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date
     */
    public static Date parse(String dateStr, String pattern) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 为Date增加分钟,减传负数
     *
     * @param date        日期
     * @param plusMinutes 要增加的分钟数
     * @return 新的日期
     */
    public static Date addMinutes(Date date, Long plusMinutes) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime newDateTime = dateTime.plusMinutes(plusMinutes);
        return Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时区id
     */
    public static String getTimeZoneId() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getID();
    }
}
