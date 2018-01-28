package main;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateCalculator {
    private static final String COMMA_SPACE_DELIMITER = ", ";
    private static final String SPACE_DELIMITER = " ";
    private static final String REGEX_FORMAT = "(0[1-9]|[12][0-9]|3[01])[\\s]" +
            "(0[1-9]|1[012])[\\s]" +
            "(19\\d\\d|200\\d|2010)" ;
    private static final int[] NON_LEAP_YEAR_MONTHS = { 31,28,31,30,31,30,31,31,30,31,30,31 };
    private static final int[] LEAP_YEAR_MONTHS = { 31,29,31,30,31,30,31,31,30,31,30,31 };
    private static final int LEAP_YEAR_DAYS = 366;
    private static final int NON_LEAP_YEAR_DAYS = 365;
    private static final int FEBRUARY = 2, DECEMBER = 12;
    private static final Map<Integer, String> MONTH_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(1, "January"),
            new AbstractMap.SimpleEntry<>(2, "February"),
            new AbstractMap.SimpleEntry<>(3, "March"),
            new AbstractMap.SimpleEntry<>(4, "April"),
            new AbstractMap.SimpleEntry<>(5, "May"),
            new AbstractMap.SimpleEntry<>(6, "June"),
            new AbstractMap.SimpleEntry<>(7, "July"),
            new AbstractMap.SimpleEntry<>(8, "August"),
            new AbstractMap.SimpleEntry<>(9, "September"),
            new AbstractMap.SimpleEntry<>(10, "October"),
            new AbstractMap.SimpleEntry<>(11, "November"),
            new AbstractMap.SimpleEntry<>(12, "December"))
            .collect(Collectors.toMap((entry) -> entry.getKey(), (entry) -> entry.getValue()));

    ArrayList<String> THIRTY_MONTHS = new ArrayList<String>(
            Arrays.asList("April", "June", "September", "November"));
    ArrayList<String> THIRTY_ONE_MONTHS = new ArrayList<String>(
            Arrays.asList("January", "March", "May", "July", "August", "October", "December"));

    private static final int THIRTY_ONE_MONTH = 31;
    private static final int THIRTY_MONTH = 30;
    private static final int LEAP_YEAR_FEB_MONTH = 29;
    private static final int NON_LEAP_YEAR_FEB_MONTH = 28;

    public static void main(String args[]) throws Exception {
        System.out.println("Please enter two dates in the format DD MM YYY, DD MM YYYY:");

        Scanner scanIn = new Scanner(System.in);
        String inputString = scanIn.nextLine();
        scanIn.close();
        DateCalculator dateCalculator = new DateCalculator();
        List<String> datesList = dateCalculator.validateInput(inputString);
        if ( datesList != null && !datesList.isEmpty() ) {
            int numberOfDays = dateCalculator.calculateDays(datesList);
           System.out.println(inputString + COMMA_SPACE_DELIMITER + numberOfDays);
        } else {
            System.out.println("Invalid Input!! Please enter correct dates");
        }
    }

    public int calculateDays(List<String> datesList) {
        int numberOfDays = 0;
        int[] firstDate = splitTheDate(datesList.get(0));
        int[] secondDate = splitTheDate(datesList.get(1));
        int daysFromTillYearEnd = 0;
        int daysFromYearStart = 0;
        int daysBetweenYears = 0;
        if (checkIfValidDate(firstDate) && checkIfValidDate(secondDate)) {


           // first year should be less than the second year
            if (firstDate[2] > secondDate[2]) {
                System.err.println("Please enter year in first date smaller than the year in second date");
                return 0;
            }

            if (firstDate[2] == secondDate[2]) {
                // years are equal - calculate months and days

                // first month should be less than the second month
                if (firstDate[1] > secondDate[1]) {
                    System.err.println("Please enter month in first date smaller than month in second date");
                    return 0;
                }


                if (firstDate[1] == secondDate[1]) {
                    // months are equal too, calculate only days
                    numberOfDays =  secondDate[0] - firstDate[0];
                } else {
                    daysFromTillYearEnd = calculateDateTillYearEnd(secondDate[0], secondDate[1], secondDate[2]);
                    daysFromYearStart = calculateStartOfYearToDate(firstDate[0], firstDate[1], firstDate[2]);

                    numberOfDays = (getDaysInYear(firstDate[2])) - (daysFromTillYearEnd + daysFromYearStart);
                }
            } else {
                daysFromTillYearEnd = calculateDateTillYearEnd(firstDate[0], firstDate[1], firstDate[2]);
                daysFromYearStart = calculateStartOfYearToDate(secondDate[0], secondDate[1], secondDate[2]);
                daysBetweenYears =  daysBetweenTwoYears(firstDate[2], secondDate[2]);

                numberOfDays = daysFromYearStart + daysFromTillYearEnd + daysBetweenYears;
            }
        }
        return numberOfDays;
    }

    public List<String> validateInput(String inputString) {
        List<String> datesList = new ArrayList<>();
        List<String> dates = Arrays.asList(inputString.split(COMMA_SPACE_DELIMITER));
        // we expect only two string input dates
        if (dates.size() == 2) {
            Pattern pattern = Pattern.compile(REGEX_FORMAT);
            datesList = dates.stream()
                    .filter(date -> pattern.matcher(date).matches())
                    .collect(Collectors.<String>toList());

            if ( datesList.size() != 2) {
                System.err.println("Please enter the dates in the format DD MM YYYY");
            }
        } else {
            System.err.println("Please enter only two dates for comparison");
        }
        return datesList;
    }

    public boolean checkIfValidDate(int[] date) {
        boolean isValidDate = false;
        int day = date[0];
        int month = date[1];
        int year = date[2];

        if ( (THIRTY_MONTHS.contains(MONTH_MAP.get(month)) && day <= 30) ||
             (THIRTY_ONE_MONTHS.contains(MONTH_MAP.get(month)) && day <=  31) ||
             (month == FEBRUARY && ((day <= 28) || (day == 29 && isLeapYear(year))))  ) {
               isValidDate = true;
        }

        return isValidDate;
    }

    public int[] splitTheDate (String dateString) {
        String[] stringArray =  dateString.split(SPACE_DELIMITER);

        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            String numberAsString = stringArray[i];
            intArray[i] = Integer.parseInt(numberAsString);
        }

        return intArray;
    }

    public int calculateStartOfYearToDate(int dayOfMonth, int month, int year) {
        int daysFromNewYear = dayOfMonth;
        int[] daysInMonths = getDaysInMonthsFromYear(year);

        if ( month != 1 ) {
            for ( int month_index = 1; month_index < month; month_index++ ) {
                daysFromNewYear += daysInMonths[month_index];
            }
        }

        return daysFromNewYear;
    }


    public int calculateDateTillYearEnd(int day, int month, int year) {
        int currentMonthDays = 0;
        if (THIRTY_MONTHS.contains(MONTH_MAP.get(month)) ) {
            currentMonthDays = THIRTY_MONTH;
        } else if (THIRTY_ONE_MONTHS.contains(MONTH_MAP.get(month)) ) {
            currentMonthDays = THIRTY_ONE_MONTH;
        } else {
            currentMonthDays = isLeapYear(year) ? LEAP_YEAR_FEB_MONTH : NON_LEAP_YEAR_FEB_MONTH;
        }
        int days = currentMonthDays - day;
        int[] daysInMonths = getDaysInMonthsFromYear(year);

        if (month != DECEMBER) {
            for ( int month_index = DECEMBER - 1; month_index >= month; month_index-- ) {
                days += daysInMonths[month_index];
            }
        }

        return days;
    }

    public int daysBetweenTwoYears(int firstYear, int secondYear) {
        // starts the calculation from the firstYear + 1  and secondYear-1
        int daysInYears = 0;
        for (int year = firstYear + 1; year < secondYear; year++) {
            daysInYears += getDaysInYear(year);
        }
        return daysInYears;
    }

    public boolean isLeapYear(int year) {

        if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
            return true;
        } else {
            return false;
        }
    }

    private int[] getDaysInMonthsFromYear (int year) {
        if (isLeapYear(year)) {
            return LEAP_YEAR_MONTHS;
        } else {
            return NON_LEAP_YEAR_MONTHS;
        }
    }

    public int getDaysInYear (int year) {
      if ( isLeapYear(year)) {
         return LEAP_YEAR_DAYS;
      } else {
         return NON_LEAP_YEAR_DAYS;
      }
    }

}
