package com.xunce.common.dailylesson;

import lombok.val;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.util.Date;

import static java.time.temporal.ChronoField.*;

public class DateCase {

    public static void main(String[] args) {
        // 1、当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        int year = localDateTime.getYear();
        int monthValue = localDateTime.getMonthValue();
        int dayOfMonth = localDateTime.getDayOfMonth();

        // 2、加一天、减一天操作
        LocalDateTime plusDays = localDateTime.plusDays(1L); // 加一天
        LocalDateTime minusDays = localDateTime.minusDays(1L);// 减一天

        // 3、日期转字符串
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = dateTimeFormatter.format(localDateTime);

        // 4、字符串转日期
        String str = "2023-06-18 21:58:27";
        LocalDateTime parse = LocalDateTime.parse(str, dateTimeFormatter);
        LocalDate of = LocalDate.of(2023, 6, 18);

        // 5、Date和LocalDate的转化
        ZonedDateTime zonedDateTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        Date from = Date.from(zonedDateTime.toInstant());

        LocalDateTime localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String format1 = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
