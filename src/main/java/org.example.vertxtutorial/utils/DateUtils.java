package org.example.vertxtutorial.utils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static String getCurrentDateTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static int getYYYY(String ymd) {
        return Integer.parseInt(ymd.substring(0, 4));
    }

    public static String date2Str(Date date) {
        return date2Str(date, DATE_FORMAT);

    }

    public static String date2Str(Date date, String format) {
        DateFormat dateFormat;
        if (format == null) {
            dateFormat = new SimpleDateFormat(DATE_FORMAT);
        } else {
            dateFormat = new SimpleDateFormat(format);
        }
        return dateFormat.format(date);

    }

    public static Date str2Date(String dateStr) throws ParseException {
        return str2Date(dateStr, DATE_FORMAT);
    }

    public static Date str2Date(String dateStr, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (format != null) {
            dateFormat = new SimpleDateFormat(format);
        }
        return dateFormat.parse(dateStr);
    }

    public static Date add(Date date, int year, int month, int day, int hour, int minute, int second, int milisecond) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);
        cal.add(Calendar.HOUR, hour);
        cal.add(Calendar.MINUTE, minute);
        cal.add(Calendar.SECOND, second);
        cal.add(Calendar.MILLISECOND, milisecond);

        return cal.getTime();
    }

    public static String getYMD(int delDay) {
        Calendar firstExecutionDate = new GregorianCalendar();
        firstExecutionDate.add(Calendar.DATE, delDay);
        Date dt = firstExecutionDate.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(dt);
    }

    public static Date getTimeByHour(int hour, int minute) {

        Calendar today = Calendar.getInstance();

        Calendar nextTime = Calendar.getInstance();
        nextTime.set(Calendar.YEAR, today.get(Calendar.YEAR));
        nextTime.set(Calendar.MONTH, today.get(Calendar.MONTH));
        nextTime.set(Calendar.DATE, today.get(Calendar.DATE));

        int minuteNow = today.get(Calendar.HOUR_OF_DAY) * 60 + today.get(Calendar.MINUTE);
        if (minuteNow > (hour * 60 + minute)) {
            nextTime.add(Calendar.DATE, 1);
        }

        nextTime.set(Calendar.HOUR_OF_DAY, hour);
        nextTime.set(Calendar.MINUTE, minute);
        nextTime.set(Calendar.SECOND, 0);

        Date date = nextTime.getTime();
        return date;
    }

    public static String getYesterdayYmd(String ymd, String format) throws ParseException {
        Date d = DateUtils.str2Date(ymd, format);
        Date yesterday = DateUtils.add(d, 0, 0, -1, 0, 0, 0, 0);

        return DateUtils.date2Str(yesterday, format);
    }

    public static String getLastMonthYm(String ymd, String format, String newformat) throws ParseException {
        Date d = DateUtils.str2Date(ymd, format);
        Date yesterday = DateUtils.add(d, 0, -1, 0, 0, 0, 0, 0);

        return DateUtils.date2Str(yesterday, newformat);
    }

    public static Date parseDate(String dateStr, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (format != null) {
            dateFormat = new SimpleDateFormat(format);
        }
        dateFormat.setLenient(false);
        return dateFormat.parse(dateStr);
    }

    public static Date addMonths(Date date, int months) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static int getCurrentMonth() {
        final Calendar now = Calendar.getInstance();
        return now.get(Calendar.MONTH) + 1;
    }

    public static Date getFirstDateOfMonth() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    public static String getFirstDateOfMonth(String format) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return date2Str(cal.getTime(), format);
    }

    public static String getEkycApiDate() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
            return sdf.format(new Date());
        } catch (Exception e) {
            System.err.println(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }
}
