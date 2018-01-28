package test;

import main.DateCalculator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DateCalculatorTest {

    DateCalculator dateCalculator = new DateCalculator();

    @Test
    public void testCalculateDays_1985_2009() throws Exception {
       List<String> inputList = new ArrayList<>();
       inputList.add("05 02 1985");
       inputList.add("28 01 2009");
       Assert.assertEquals(8758, dateCalculator.calculateDays(inputList));
    }

    @Test
    public void testCalculateDays_1910_1911() throws Exception {
        List<String> inputList = new ArrayList<>();
        inputList.add("04 05 1910");
        inputList.add("06 08 1911");
        Assert.assertEquals(459, dateCalculator.calculateDays(inputList));
    }

    @Test
    public void testCalculateDays_1965() throws Exception {
        List<String> inputList = new ArrayList<>();
        inputList.add("01 02 1965");
        inputList.add("31 12 1965");
        Assert.assertEquals(336, dateCalculator.calculateDays(inputList));
    }

    @Test
    public void testCalculateDays_1947_2000() throws Exception {
        List<String> inputList = new ArrayList<>();
        inputList.add("15 07 1947");
        inputList.add("31 12 2000");
        Assert.assertEquals(19528, dateCalculator.calculateDays(inputList));
    }

    @Test
    public void testCalculateDays_2006_2010() throws Exception {
        List<String> inputList = new ArrayList<>();
        inputList.add("14 06 2006");
        inputList.add("1 1 2010");
        Assert.assertEquals(1297, dateCalculator.calculateDays(inputList));
    }

    @Test
    public void testCalculateDays_larger_month_as_firstDate() throws Exception {
        List<String> inputList = new ArrayList<>();
        inputList.add("15 09 1990");
        inputList.add("16 06 1990");
        Assert.assertEquals(0, dateCalculator.calculateDays(inputList));
    }

    @Test
    public void testCalculateDays_larger_year_as_firstDate() throws Exception {
        List<String> inputList = new ArrayList<>();
        inputList.add("15 09 1990");
        inputList.add("16 06 1965");
        Assert.assertEquals(0, dateCalculator.calculateDays(inputList));
    }

    @Test
    public void testValidateInput() throws Exception {
        List<String> expectedList = new ArrayList<>();
        expectedList.add("12 01 1999");
        expectedList.add("01 02 2000");
        List<String> actualList = dateCalculator.validateInput("12 01 1999, 01 02 2000");
        Assert.assertSame(expectedList.size(), actualList.size());
    }

    @Test
    public void testValidateInput_NoSpace() throws Exception {
        List<String> expectedList = new ArrayList<>();
        expectedList.add("12 01 1999");
        expectedList.add("01 02 2000");
        List<String> actualList = dateCalculator.validateInput("12 01 1999,01 02 2000");
        Assert.assertNotSame(expectedList.size(), actualList.size());
    }

    @Test
    public void testValidateInput_NoComma() throws Exception {
        List<String> expectedList = new ArrayList<>();
        expectedList.add("12 01 1999");
        expectedList.add("01 02 2000");
        List<String> actualList = dateCalculator.validateInput("12 01 1999 01 02 2000");
        Assert.assertNotSame(expectedList.size(), actualList.size());
    }

    @Test
    public void testCheckIfValidDate_validDate() throws Exception {
        int[] date = { 26, 02, 2010};
        Assert.assertTrue(dateCalculator.checkIfValidDate(date));
    }

    @Test
    public void testCheckIfValidDate_InvalidDate() throws Exception {
        int[] date = { 29, 02, 2011};
        Assert.assertFalse(dateCalculator.checkIfValidDate(date));
    }

    @Test
    public void testCheckIfValidDate_InvalidYear() throws Exception {
        int[] date = { 29, 02, 201};
        Assert.assertFalse(dateCalculator.checkIfValidDate(date));
    }

    @Test
    public void testSplitTheDate_validInput() throws Exception {
       int[] expectedArray = {15, 02, 2012};
       int[] actualArray = dateCalculator.splitTheDate("15 02 2012");
       Assert.assertArrayEquals(expectedArray, actualArray);

    }

    @Test
    public void testSplitTheDate_NoDelimiter() throws Exception {
        int[] expectedArray = {15, 02, 2012};
        int[] actualArray = dateCalculator.splitTheDate("15022012");
        Assert.assertNotSame(expectedArray, actualArray);
    }

    @Test
    public void testCalculateStartOfYearToDate() throws Exception {
        Assert.assertEquals(43, dateCalculator.calculateStartOfYearToDate(15, 02, 1990));
    }

    @Test
    public void testCalculateDateTillYearEnd() throws Exception {
        Assert.assertEquals(19, dateCalculator.calculateDateTillYearEnd(12, 12, 2000));
    }

    @Test
    public void testDaysBetweenTwoYears() throws Exception {
       Assert.assertEquals(4018, dateCalculator.daysBetweenTwoYears(1975, 1987));
    }

    @Test
    public void testDaysBetweenTwoYears_bothYears_19() throws Exception {
        Assert.assertEquals(3287, dateCalculator.daysBetweenTwoYears(1989, 1999));
    }

    @Test
    public void testDaysBetweenTwoYears_bothYears_20() throws Exception {
        Assert.assertEquals(2922, dateCalculator.daysBetweenTwoYears(2000, 2009));
    }

    @Test
    public void testIsLeapYear_1992() throws Exception {
        Assert.assertTrue(dateCalculator.isLeapYear(1992));
    }

    @Test
    public void testIsLeapYear_1965() throws Exception {
        Assert.assertFalse(dateCalculator.isLeapYear(1965));
    }

    @Test
    public void testIsLeapYear_2000() throws Exception {
        Assert.assertTrue(dateCalculator.isLeapYear(2000));
    }

    @Test
    public void testIsLeapYear_1900() throws Exception {
        Assert.assertFalse(dateCalculator.isLeapYear(1900));
    }

    @Test
    public void testIsLeapYear_2010() throws Exception {
        Assert.assertFalse(dateCalculator.isLeapYear(2010));
    }
}
