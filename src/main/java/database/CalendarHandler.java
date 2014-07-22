
package database;

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
        
        switch(month){
            case "Jan": return 1;
            case "Feb":return 2;
            case "Mar":return 3;
            case "Apr":return 4;
            case "May":return 5;
            case "Jun":return 6;
            case "Jul":return 7;
            case "Aug":return 8;
            case "Sep":return 9;
            case "Oct":return 10;
            case "Nov":return 11;
            case "Dec":return 12;
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
}
