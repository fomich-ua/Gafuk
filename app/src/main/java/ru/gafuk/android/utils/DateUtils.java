package ru.gafuk.android.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Александр on 30.10.2017.
 */

public class DateUtils {
    private final Locale locale = new Locale("ru");
    private final SimpleDateFormat dateShortFormat = new SimpleDateFormat("dd MMMM yyyy", locale);
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", locale);
    private final SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd MMMM", locale);
    private final SimpleDateFormat parseDateTimeFormat = new SimpleDateFormat("dd MMMM yyyy 'в' HH:mm", locale);

    private static DateUtils instance = null;

    public static DateUtils getInstance() {
        if (instance == null) instance = new DateUtils();
        return instance;
    }
    // This class should not be initialized
    private DateUtils() {
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);
        String[] months = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        String[] shortMonths = {
                "янв", "фев", "мар", "апр", "май", "июн",
                "июл", "авг", "сен", "окт", "ноя", "дек"};
        dfs.setMonths(months);
        dfs.setShortMonths(shortMonths);
        String[] weekdays = {"", "Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
        String[] shortWeekdays = {"", "вс", "пн", "вт", "ср", "чт", "пт", "сб"};
        dfs.setWeekdays(weekdays);
        dfs.setShortWeekdays(shortWeekdays);

        dateShortFormat.setDateFormatSymbols(dfs);
//        timeFormat.setDateFormatSymbols(dfs); для часов/минут не нужно
        dayMonthFormat.setDateFormatSymbols(dfs);
    }

    /**
     * Gets timestamp in millis and converts it to HH:mm (e.g. 16:44).
     */
    public String formatTime(long timeInMillis) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//        return dateFormat.format(timeInMillis);
        return getInstance().timeFormat.format(timeInMillis);
    }

    /**
     * If the given time is of a different date, display the date.
     * If it is of the same date, display the time.
     * @param timeInMillis  The time to convert, in milliseconds.
     * @return  The time or date.
     */
    public String formatDateTime(long timeInMillis) {
        if(isToday(timeInMillis)) {
            return formatTime(timeInMillis);
        } else {
            return formatDate(timeInMillis);
        }
    }

    /**
     * Formats timestamp to 'date month' format (e.g. 'February 3').
     */
    public String formatDate(long timeInMillis) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
//        return dateFormat.format(timeInMillis);
        return getInstance().dayMonthFormat.format(timeInMillis);
    }

    /**
     * Returns whether the given date is today, based on the user's current locale.
     */
    public boolean isToday(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String date = dateFormat.format(timeInMillis);
        return date.equals(dateFormat.format(System.currentTimeMillis()));
    }

    /**
     * Checks if two dates are of the same day.
     * @param millisFirst   The time in milliseconds of the first date.
     * @param millisSecond  The time in milliseconds of the second date.
     * @return  Whether {@param millisFirst} and {@param millisSecond} are off the same day.
     */
    public boolean hasSameDate(long millisFirst, long millisSecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return dateFormat.format(millisFirst).equals(dateFormat.format(millisSecond));
    }

    public String formatFullDate(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    public String formatFullDateTime(long timeInMillis) {
        SimpleDateFormat dateFormat = getInstance().parseDateTimeFormat;
        return dateFormat.format(timeInMillis);
    }

    public Date dateFromString(String dateString){
        SimpleDateFormat formatter = getInstance().dateShortFormat;

        if (dateString.contains("вчера")){
            GregorianCalendar calendar = new GregorianCalendar(getInstance().locale);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            dateString = dateString.replace("вчера", getInstance().dateShortFormat.format(calendar.getTime()));
            formatter = getInstance().parseDateTimeFormat;
        }if (dateString.contains("сегодня")){
            GregorianCalendar calendar = new GregorianCalendar(getInstance().locale);
            dateString = dateString.replace("сегодня", getInstance().dateShortFormat.format(calendar.getTime()));
            formatter = getInstance().parseDateTimeFormat;
        }
        try {
            return formatter.parse(dateString);
        }catch (Exception ex){
            ex.printStackTrace();
            return new Date(0);
        }

    }

    public Date commentsDateFromString(String dateString){

        if (dateString.contains("вчера")){
            GregorianCalendar calendar = new GregorianCalendar(getInstance().locale);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            dateString = dateString.replace("вчера", getInstance().dateShortFormat.format(calendar.getTime()));
        }if (dateString.contains("сегодня")){
            GregorianCalendar calendar = new GregorianCalendar(getInstance().locale);
            dateString = dateString.replace("сегодня", getInstance().dateShortFormat.format(calendar.getTime()));
        }
        try {
            return getInstance().parseDateTimeFormat.parse(dateString);
        }catch (Exception ex){
            ex.printStackTrace();
            return new Date(0);
        }

    }
}
