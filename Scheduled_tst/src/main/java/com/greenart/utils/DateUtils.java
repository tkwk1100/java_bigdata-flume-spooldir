package com.greenart.utils;

import java.util.Calendar;

public class DateUtils {
    public static String makeTodayString() {
        //YYYYMMDD
        Calendar c = Calendar.getInstance();
        String todqy = c.get(Calendar.YEAR)+leadingZero(c.get(Calendar.MONTH)+1)+leadingZero(c.get(Calendar.DATE));
        return todqy;
    }

    public static String makeAWeekAgoDateString() {
        //YYYYMMDD
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1); //1일 전 시간으로 만든다.
        String todqy = c.get(Calendar.YEAR)+leadingZero(c.get(Calendar.MONTH)+1)+leadingZero(c.get(Calendar.DATE));
        return todqy;
    }

    public static String leadingZero(int i) {
        //파라미터로 전달된 i이가 10보다 작으면 0i 형태로 내보내고,
        //드렇지 않다면 i를 무낮열로 반환해서 내보낸다.
        return i<10 ? "0"+i : ""+i;
    }
}

