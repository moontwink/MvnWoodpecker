
package database;

import static database.CalendarType.CST;
import static database.CalendarType.GMT;
import static database.CalendarType.OTHER;

/**
 *
 * @author Nancy
 */
public class CalendarHandler {
    
    /**
     * Converts Month to its Number Equivalent
     * @param month
     * @return int this is the number form of the month
     */
    public static int monthNumber(String month){
        int monthnum = 0;
        
        switch(month.toUpperCase()){
            case "JAN": return 1;
            case "FEB":return 2;
            case "MAR":return 3;
            case "APR":return 4;
            case "MAY":return 5;
            case "JUN":return 6;
            case "JUL":return 7;
            case "AUG":return 8;
            case "SEPT":return 9;
            case "OCT":return 10;
            case "NOV":return 11;
            case "DEC":return 12;
            default: return monthnum;
        }
    }
    
    /**
     * Converts Month Number to its name equivalent
     * @param month This is the number form on a month.
     * @return String This is the word form on a month.
     */
    public static String monthName(int month){
        String name = " ";
        
        switch(month){
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
            default: return name;
        }
    }
    
    /**
     * This returns the number of days in a month.
     * @param month
     * @param year
     * @return int 
     */
    public static int numDaysinMonth(int month, int year){
        int numdays = 30;
        
        switch(month){
            case 1: return 31;
            case 2: if(year%4 == 0) return 29;
                    else return 28;
            case 3: return 31;
            case 4: return 30;
            case 5: return 31;
            case 6: return 30;
            case 7: return 31;
            case 8: return 31;
            case 9: return 30;
            case 10: return 31;
            case 11: return 30;
            case 12: return 31;
            default: return numdays;
        }
    }
    
    /**
     * This returns the number of days in a month by name.
     * @param month
     * @param year
     * @return int 
     */
    public static int numDaysinMonthname(String month, int year){
        int numdays = 30;
        
        switch(month){
                
            case "JAN": return 31;
            case "FEB":if(year%4 == 0) return 29;
                    else return 28;
            case "MAR":return 31;
            case "APR":return 30;
            case "MAY":return 31;
            case "JUN":return 30;
            case "JUL":return 31;
            case "AUG":return 31;
            case "SEPT":return 30;
            case "OCT":return 31;
            case "NOV":return 30;
            case "DEC":return 31;
            default: return numdays;
        }
    }
    
    /**
     * This method identifies the type of date (GMT or CST).
     * @param date
     * @return CalendarType
     */
    public static CalendarType identifyDateType(String date){
        if(date.contains("GMT")){
            //[0] day, [1] month, [2] year
            return GMT; }
        else if(date.contains("CST")){
            //Wed May 01 14:34:14 CST 2013
            return CST; }
        else
            return OTHER;
    }
    
    /**
     * This method return an array of Strings with the date formatted by DAY-MONTH-YEAR.
     * @param date
     * @return String[]
     */
    public static String[] getDateFormatted(String date){
        String[] newdate = new String[3];
        String[] splitted = date.split(" ");
        switch(identifyDateType(date)){
            case GMT:
                newdate[0] = splitted[0];   //day
                newdate[1] = splitted[1];   //month
                newdate[2] = splitted[2];   //year
                break;
            case CST:
                //Wed May 01 14:34:14 CST 2013
                newdate[0] = splitted[2];   //day
                newdate[1] = splitted[1];   //month
                newdate[2] = splitted[5];   //year
                break;
        }
        return newdate;
    }
}
