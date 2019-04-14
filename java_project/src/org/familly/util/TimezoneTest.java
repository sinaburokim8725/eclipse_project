package org.familly.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TimezoneTest {

    public static void main(String[] args) {

        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            System.out.println(displayTimeZone(TimeZone.getTimeZone(id)));
        }

        System.out.println("\nTotal TimeZone ID " + ids.length);

////////////////////////////////////////////////////////////////////////////////////////////////////

        //2. Calendar클래스의 getInstance()메서드를 활용하는 방법

        SimpleDateFormat format5 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format6 = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분ss초");

        Calendar time = Calendar.getInstance();

        String format_time5 = format5.format(time.getTime());
        String format_time6 = format6.format(time.getTime());

        System.out.println(format_time5);
        System.out.println(format_time6);


        //1. Date객체를 활용하는 방법

        SimpleDateFormat format3 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format4 = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분ss초");

        Date time1 = new Date();

        String time3 = format3.format(time1);
        String time4 = format4.format(time1);

        System.out.println(time3);
        System.out.println(time4);
//////////////////////////////////////////////////
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd a hh:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분ss초");

        String format_time1 = format1.format (System.currentTimeMillis());
        String format_time2 = format2.format (System.currentTimeMillis());

        long localDate = System.currentTimeMillis();
        long gmtOffset = (TimeZone.getDefault()).getOffset(localDate);
        long utcDate = localDate + gmtOffset;
        System.out.println("localDate + gmtOffset > "+format2.format(utcDate));
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        System.out.println(dayFormat.format(utcDate));

        System.out.println(format_time1);
        System.out.println(format_time2);

////////////////////////////////////////////////////
        Calendar cal = Calendar.getInstance();

//현재 년도, 월, 일
        int year = cal.get ( cal.YEAR );
        int month = cal.get ( cal.MONTH ) + 1 ;
        int date = cal.get ( cal.DATE ) ;


//현재 (시,분,초)
        int hour = cal.get ( cal.HOUR_OF_DAY ) ;
        int min = cal.get ( cal.MINUTE );
        int sec = cal.get ( cal.SECOND );

        System.out.println("현재날짜 " + new Date());



        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy년MM월dd일 HH:mm:ss", Locale.KOREA );
        Date currentTime = new Date ();
        String mTime = mSimpleDateFormat.format ( currentTime );
        System.out.println ( mTime );





        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        System.out.println("TimeZone.getRawOffset() > " + mGMTOffset);
        System.out.printf("GMT offset is %s hours\n\n", TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS));


        long  millisSecond = System.currentTimeMillis();
        TimeZone tz = TimeZone.getDefault();
        long offset = tz.getOffset(millisSecond);


        System.out.println("TimeZone Id > " + TimeZone.getDefault().getID());
        System.out.println("System.currentTimeMillis() > " + millisSecond);
        System.out.println("getOffset(millisSecond) 시간 > " + offset/(1000*60*60));
        System.out.println("일수 > " + (millisSecond + offset)/(1000*60*60*24));
        System.out.println("개월수 > " + ((millisSecond + offset)/(1000*60*60*24))/12);
        System.out.println("년수 > " + ((millisSecond + offset)/(1000*60*60*24))/365);


//////////////////////////////////////////////////////////////////////////////////

        String timezoneId = TimeZone.getDefault().getID();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        long millis = timeZone.getRawOffset() + (timeZone.inDaylightTime(new Date()) ? timeZone.getDSTSavings() : 0);


        String timeDiff = String.format("%s%s:%s",
                millis < 0 ? "-" : "+",
                String.format("%02d", Math.abs(TimeUnit.MILLISECONDS.toHours(millis))),
                String.format("%02d", Math.abs(TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))))
        );

        System.out.println("timezoneId = " + timezoneId);
        System.out.println("timeDifference = " + millis);
        System.out.println("timeDiff = " + timeDiff);

    }
    private static String displayTimeZone(TimeZone tz) {
        //LocalDateTime localNow = LocalDateTime.now(TimeZone.getTimeZone("Europe/Madrid").toZoneId());
        //System.out.println(localNow);
        // Prints current time of given zone without zone information : 2016-04-28T15:41:17.611
        ZonedDateTime zoneNow = ZonedDateTime.now(TimeZone.getTimeZone(tz.getID()).toZoneId());

        //System.out.println(DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss").format(zoneNow));
        // Prints current time of given zone with zone information : 2016-04-28T15:41:17.627+02:00[Europe/Madrid]

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

        //timeFormat.format(Calendar.getInstance(TimeZone.getTimeZone(tz.getID())).getTime());
        //timeFormat.format(zoneNow);

        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);

        String result = "";
        if (hours > 0) {
            result = String.format("(GMT+%02d:%02d) %s >> %s",
                    hours,
                    minutes, tz.getID(),
                    DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss").format(zoneNow)
            );
        } else if(hours < 0) {
            result = String.format("(GMT-%02d:%02d) %s >> %s",
                    Math.abs(hours),
                    minutes, tz.getID(),
                    DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss").format(zoneNow)
            );

        }else{

            result = String.format("(GMT %02d:%02d) %s >> %s",
                    hours,
                    minutes, tz.getID(),
                    DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss").format(zoneNow)
            );

        }

        return result;

    }



}
