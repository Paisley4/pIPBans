package pl.paisley4.pipbans.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Oclock {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public Oclock(Date date){
        this.autoSet(date);
    }

    public Oclock(){}

    public String toString(){
        String hour = getHour()+"";
        String minute = getMinute()+"";
        String second = getSecond()+"";
        String day = getDay()+"";
        String month = getMonth()+"";
        String year = getYear()+"";
        if(hour.length()==1){
            hour = "0"+hour;
        }
        if(minute.length()==1){
            minute = "0"+minute;
        }
        if(second.length()==1){
            second = "0"+second;
        }
        if(month.length()==1){
            month = "0"+month;
        }
        if(day.length()==1){
            day = "0"+day;
        }
        return Messages.getMessage("time-format").replace("year", year).replace("month", month).replace("day", day).replace("hour", hour).replace("minute", minute).replace("second", second);
    }

    public String toGetterString(){
        String hour = getHour()+"";
        String minute = getMinute()+"";
        String second = getSecond()+"";
        String day = getDay()+"";
        String month = getMonth()+"";
        String year = getYear()+"";
        if(hour.length()==1){
            hour = "0"+hour;
        }
        if(minute.length()==1){
            minute = "0"+minute;
        }
        if(second.length()==1){
            second = "0"+second;
        }
        if(month.length()==1){
            month = "0"+month;
        }
        if(day.length()==1){
            day = "0"+day;
        }
        return hour+":"+minute+":"+second+":"+day+":"+month+":"+year;
    }

    public String noZero(){
        String hour = getHour()+"";
        String minute = getMinute()+"";
        String second = getSecond()+"";
        String day = getDay()+"";
        if(hour.length()==1){
            hour = "0"+hour;
        }
        if(minute.length()==1){
            minute = "0"+minute;
        }
        if(second.length()==1){
            second = "0"+second;
        }
        if(day.length()==1){
            day = "0"+day;
        }
        if(day.equalsIgnoreCase("00")){
            return hour+":"+minute+":"+second;
        }else{
            return day+":"+hour+":"+minute+":"+second;
        }
    }

    public String toDay(){
        String hour = getHour()+"";
        String minute = getMinute()+"";
        String second = getSecond()+"";
        String day = getDay()+"";
        if(hour.length()==1){
            hour = "0"+hour;
        }
        if(minute.length()==1){
            minute = "0"+minute;
        }
        if(second.length()==1){
            second = "0"+second;
        }
        if(day.length()==1){
            day = "0"+day;
        }
        return day+":"+hour+":"+minute+":"+second;
    }

    @Deprecated
    public void addSeconds(int seconds){
        int meSecond = this.second;
        int newSeconds = meSecond+seconds;
        while (newSeconds>=60){
            newSeconds-=60;
            this.minute+=1;
        }
        this.second = newSeconds;
        int newMinute = this.minute;
        while (newMinute>=60){
            newMinute-=60;
            this.hour+=1;
        }
        this.minute = newMinute;
        int newHour = this.hour;
        while (newHour>=24){
            newHour-=24;
            this.day+=1;
        }
        this.hour = newHour;
        int newDay = this.day;
        int newMonth = this.month;
        while (newDay>month(getMonth())){
            newDay-=month(getMonth());
            newMonth+=1;
        }
        this.day = newDay;
        while (newMonth>12){
            newMonth-=12;
            this.year+=1;
        }
        this.month = newMonth;
    }

    @Deprecated
    public void addMinutes(int minutes){
        int newMinute = this.minute+minutes;
        while (newMinute>=60){
            newMinute-=60;
            this.hour+=1;
        }
        this.minute = newMinute;
        int newHour = this.hour;
        while (newHour>=24){
            newHour-=24;
            this.day+=1;
        }
        this.hour = newHour;
        int newDay = this.day;
        int newMonth = this.month;
        while (newDay>month(getMonth())){
            newDay-=month(getMonth());
            newMonth+=1;
        }
        this.day = newDay;
        while (newMonth>12){
            newMonth-=12;
            this.year+=1;
        }
        this.month = newMonth;
    }

    @Deprecated
    public void addHours(int hours){
        int newHour = this.hour+hours;
        while (newHour>=24){
            newHour-=24;
            this.day+=1;
        }
        this.hour = newHour;
        int newDay = this.day;
        int newMonth = this.month;
        while (newDay>month(getMonth())){
            newDay-=month(getMonth());
            newMonth+=1;
        }
        this.day = newDay;
        while (newMonth>12){
            newMonth-=12;
            this.year+=1;
        }
        this.month = newMonth;
    }

    @Deprecated
    public void addDays(int days){
        int newDay = this.day+days;
        int newMonth = this.month;
        while (newDay>month(getMonth())){
            newDay-=month(getMonth());
            newMonth+=1;
        }
        this.day = newDay;
        while (newMonth>12){
            newMonth-=12;
            this.year+=1;
        }
        this.month = newMonth;
    }

    @Deprecated
    public void addMonths(int months){
        int newMonth = this.month+months;
        while (newMonth>12){
            newMonth-=12;
            this.year+=1;
        }
        this.month = newMonth;
    }

    private int month(int i){
        if(i==1){
            return 31;
        }
        if(i==2){
            if(year==2020||year==2024||year==2028){
                return 29;
            }else{
                return 28;
            }
        }
        if(i==3){
            return 31;
        }
        if(i==4){
            return 30;
        }
        if(i==5){
            return 31;
        }
        if(i==6){
            return 30;
        }
        if(i==7){
            return 31;
        }
        if(i==8){
            return 31;
        }
        if(i==9){
            return 30;
        }
        if(i==10){
            return 31;
        }
        if(i==11){
            return 30;
        }
        if(i==12){
            return 31;
        }
        return 0;
    }

    @Deprecated
    public void autoSet(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        format.format(date);
        setYear(Integer.parseInt(format.format(date)));
        SimpleDateFormat format1 = new SimpleDateFormat("MM");
        format1.format(date);
        setMonth(Integer.parseInt(format1.format(date)));
        SimpleDateFormat format2 = new SimpleDateFormat("dd");
        format2.format(date);
        setDay(Integer.parseInt(format2.format(date)));
        setSecond(date.getSeconds());
        setMinute(date.getMinutes());
        setHour(date.getHours());
    }

    public void removeSeconds(int seconds){
        while (seconds>getSecond()){
            if(minute==0){
                if(hour==0){
                    if(day==1){
                        if(month==1){
                            year--;
                            month+=11;
                            day+=month(month)-1;
                            hour+=23;
                            minute+=59;
                            second+=60;
                        }else{
                            month--;
                            day+=month(month)-1;
                            hour+=23;
                            minute+=59;
                            second+=60;
                        }
                    }else{
                        day--;
                        hour+=23;
                        minute+=59;
                        second+=60;
                    }
                }else{
                    hour--;
                    minute+=59;
                    second+=60;
                }
            }else{
                minute--;
                second+=60;
            }
        }
        second-=seconds;
    }

    public void removeMinutes(int minutes){
        while (minutes>getMinute()) {
            if (hour == 0) {
                if (day == 1) {
                    if (month == 1) {
                        year--;
                        month += 11;
                        day += month(month) - 1;
                        hour += 23;
                        minute += 60;
                    } else {
                        month--;
                        day += month(month) - 1;
                        hour += 23;
                        minute += 60;
                    }
                } else {
                    day--;
                    hour += 23;
                    minute += 60;
                }
            } else {
                hour--;
                minute += 60;
            }
        }
        minute-=minutes;
    }

    public void removeHours(int hours){
        while (hours>getHour()) {
            if (day == 1) {
                if (month == 1) {
                    year--;
                    month += 11;
                    day += month(month) - 1;
                    hour += 24;
                } else {
                    month--;
                    day += month(month) - 1;
                    hour += 24;
                }
            } else {
                day--;
                hour += 24;
            }
        }
        hour-=hours;
    }

    public void removeDays(int days){
        while (days>getDay()) {
            if (month == 1) {
                year--;
                month += 11;
                day += month(month);
            } else {
                month--;
                day += month(month);
            }
        }
        day-=days;
    }

    public void removeMonths(int months){
        while (months>getMonth()) {
            year--;
            month += 12;
        }
        month-=months;
    }

    public void removeYear(int years){
        year-=years;
    }

    public boolean isAfter(Oclock oclock){
        if(oclock.getYear()==year){
            if(oclock.getMonth()==month){
                if(oclock.getDay()==day){
                    if(oclock.getHour()==hour){
                        if(oclock.getMinute()==minute){
                            if(oclock.getSecond()==second||oclock.getSecond()>second){
                                return false;
                            }
                            if(oclock.getSecond()<second){
                                return true;
                            }
                        }
                        if(oclock.getMinute()>minute){
                            return false;
                        }
                        if(oclock.getMinute()<minute){
                            return true;
                        }
                    }
                    if(oclock.getHour()>hour){
                        return false;
                    }
                    if(oclock.getHour()<hour){
                        return true;
                    }
                }
                if(oclock.getDay()>day){
                    return false;
                }
                if(oclock.getDay()<day){
                    return true;
                }
            }
            if(oclock.getMonth()>month){
                return false;
            }
            if(oclock.getMonth()<month){
                return true;
            }
        }
        if(oclock.getYear()>year){
            return false;
        }
        if(oclock.getYear()<year){
            return true;
        }
        return false;
    }

}
