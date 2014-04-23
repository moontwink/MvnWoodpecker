
package database;

/**
 *
 * @author Nancy
 */
public class CalendarHandler {
    //Converts Month to its Number Equivalent
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
    
    //Converts Month Number to its name equivalent
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
    
    //Returns number of days in a month
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
    
    public static int numDaysinMonthname(String month){
        int numdays = 30;
        
        switch(month){
                
            case "Jan": return 31;
            case "Feb":return 28;
            case "Mar":return 31;
            case "Apr":return 30;
            case "May":return 31;
            case "Jun":return 30;
            case "Jul":return 31;
            case "Aug":return 31;
            case "Sep":return 30;
            case "Oct":return 31;
            case "Nov":return 30;
            case "Dec":return 31;
            default: return numdays;
        }
    }
}
