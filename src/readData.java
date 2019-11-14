import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class readData {

    int numRows;
    String stockSearch;
    String[][] items;
    float[][] dataArray;


    public String getStockData(String ticker)
    {
        //returns a string of the URL that accesses the API
        //used when making the string and float arrays of accessed stock data
        stockSearch = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + ticker + "&outputsize=full&apikey=GYNTN6U36C5MWDCD&datatype=csv";
        return stockSearch;
    }

    private int findRowNum() throws IOException{
        numRows = 0;
        /*This method finds the number of rows in the csv file that is accessed from the API
          By finding the number of rows, we can create perfectly sized arrays that
          contain all of the data. This also allows us to create arrays of any size.*/

        URL url = new URL(stockSearch);
        URLConnection urlconn = url.openConnection();
        InputStreamReader isr = new InputStreamReader(urlconn.getInputStream());
        BufferedReader reader = new BufferedReader(isr);

        while(reader.readLine() != null){
            numRows++;
        }
        return numRows;
    }



    public String[][] convertToArray() throws IOException
    {
        /* This method converts the csv file that is accessed from the API
        into a String array. All the data is transferred over*/

        items = new String[findRowNum()][6];
        int r = 0;

        URL url = new URL(stockSearch);
        URLConnection urlconn = url.openConnection();
        InputStreamReader isr = new InputStreamReader(urlconn.getInputStream());
        BufferedReader buff = new BufferedReader(isr);


        String line = null;
        //String line = buff.readLine();

        while ((line = buff.readLine()) != null) {
            StringTokenizer z = new StringTokenizer(line, ",");
            while (z.hasMoreTokens()) {
                for (int c = 0; c < 6; c++) {
                    items[r][c] = z.nextToken();
                }
                r++;
            }

        }

        return items;
    }



    public float[][] convertToFloat() throws IOException
    {
        //This method converts the String array to an array of usable float values
        //By converting it to float values, calculations can be performed on the numbers

        dataArray = new float[findRowNum()][6];

        for (int row = 1; row < items.length; row++)
        {
            //row is equal to 1 in this case because the first row
            //of the String array is the header

            items[row][0] = items[row][0].substring(5,7) + items[row][0].substring(8)+ items[row][0].substring(0,4); // converting dates

            for (int column = 0; column < items[row].length; column++)
            {
                float val = new Float(items[row][column]);
                dataArray[row][column] = val;

            }
        }

        return dataArray;
    }




    public void printFloat()
    {
        for (int row = 1; row < dataArray.length; row++)
        {
            for (int column = 0; column < dataArray[row].length; column++)
            {

                if(column == 0) {
                    System.out.printf("%.0f", dataArray[row][column]);
                    System.out.print(" ");
                    continue;
                }
                System.out.print(dataArray[row][column] + " ");
            }

            System.out.println();
        }

    }

    public void printString()
    {
        //Inserts '/' so that date is more clear to user
        for(int row = 1; row < items.length; row++)
        {
            items[row][0] = items[row][0].substring(0,2) + "/" + items[row][0].substring(2,4)+ "/" + items[row][0].substring(4,items[row][0].length());
        }

        for (int row = 1; row < items.length; row++)
        {
            /*prints out the entirety of the array minus the first
            row, which is just the header*/

            for (int column = 0; column < items[row].length; column++)
            {
                System.out.print(items[row][column] + " ");
            }
            System.out.println();
        }

    }

    public float simpleMovingAverage(int days, int type)
    {

        float sma = 0;

        // Calculate current sma based on day value
        // Type determines if sma is calculated from high, low, close, or open

        for (int row = 2; row < days + 2; row++)
        {

            sma +=  dataArray[row][type];
        }

        sma /=  days;

        return(sma);
    }

    public float twoHundredDaySma()
    {
        /*Two hundred day sma
          Although we already have a sma method that allows you to find
          the sma of any day, historically the 200 day sma is significant
          and we wanted to provide the user with the information
          when typing in the stock information*/

        float sma = 0;
        // Calculate current sma based on day value
        // Type determines if sma is calculated from high, low, close, or open

        for (int row = 1; row < 201; row++)
        {

            sma +=  dataArray[row][4];
        }

        sma /=  200;

        return(sma);
    }


    public void dayFinder(String date)
    {
        /*This method will print out all the data of a stock
          on a certain day that is inputted by the user. It is
          able to do this by searching through the array until
          the date entered matches a date found in the array*/

        boolean found = false;
        for (int row = 1; row < items.length; row++)
        {

            //Starting at row 1 because first row is irrelevant data

            if(items[row][0].equals(date))
            {
                for(int column = 0; column < items[row].length; column++)
                    System.out.print(items[row][column] + " ");
                found = true;

            }
        }

        if(!found)
            System.out.println("Invalid date, try a different one");
    }

    public void findHigh()
    {
        /*This method returns the all time high of the stock
          It is able to do this by searching through all of
          the closing prices of the stock and then prints out
          which one is the greatest*/

        float allTimeHigh = dataArray[0][2];
        for(int i = 0; i < dataArray.length; i++){
            for(int j = 0; j < dataArray[i].length; j++){
                if(dataArray[i][2] > allTimeHigh){
                    allTimeHigh = dataArray[i][2];
                }
            }
        }
        System.out.println("The all time high is: " + allTimeHigh);
    }

    public void findLow()
    {
        /*This method is the same as the findHigh() method
          The only difference being that it prints out the
          lowest value found out of all the closing prices*/

        float allTimeLow = dataArray[1][3];
        for(int i = 1; i < dataArray.length; i++){
            for(int j = 0; j < dataArray[i].length; j++){
                if(dataArray[i][3] < allTimeLow){
                    allTimeLow = dataArray[i][3];
                }
            }
        }
        System.out.println("The all time low is: " + allTimeLow);
    }


    public void getCurrentPrice(String ticker) throws IOException{

        //This method gets the most recent price of the stock within the minute
        //from the API. It updates the stockSearch string which is used as the
        //URL which accesses the API.

        stockSearch = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + ticker + "&interval=1min&apikey=GYNTN6U36C5MWDCD&datatype=csv";
        convertToArray();
        System.out.println("The current price of " + ticker + " is "  + items[1][4]);

    }

    public void dailyPercentChange(){

        //percent change of the most recent day based on closing price

        float percentChange = ((dataArray[1][4] - dataArray[2][4])/dataArray[1][4])*100;

        if(percentChange < 0){
            System.out.println("The stock is down " + percentChange + " percent from yesterday");
        }

        else {
            System.out.println("The stock is up " + percentChange + " percent from yesterday");
        }
    }

    public void monthlyPercentChange()
    {

        //percent change from most recent day to month

        float currentDate = dataArray[1][0];
        boolean found = false;
        float monthBefore = currentDate - 1000000;

        while(!found)
        {
            if(findRowIndex(monthBefore)!= 0) {
                found = true;
                break;
            }
            monthBefore -= 10000;
            findRowIndex(monthBefore);

        }

        float percentChange = ((dataArray[findRowIndex(currentDate)][4] - dataArray[findRowIndex(monthBefore)][4]) / dataArray[findRowIndex(monthBefore)][4]) * 100;



        System.out.println("Percent change is: " + percentChange);
        System.out.println();
    }

    public void yearlyPercentChange()
    {
        //percent change from most recent day to year from most previous day
        float currentDate = dataArray[1][0];
        boolean found = false;
        float yearBefore = currentDate - 1;

        while(!found)
        {
            if(findRowIndex(yearBefore)!= 0) {
                found = true;
                break;
            }
            yearBefore -= 10000;
            findRowIndex(yearBefore);

        }

        float percentChange = ((dataArray[findRowIndex(currentDate)][4] - dataArray[findRowIndex(yearBefore)][4]) / dataArray[findRowIndex(yearBefore)][4]) * 100;
        //Determines the percentChange by accessing the dataArray float[][], and by calling a findRowIndex method to determine the row which the date is in

        System.out.println("Percent change is: " + percentChange);
        System.out.println();
    }

    public int findRowIndex(float date) {

        //This method is used to find where in the array a date is - used for error catching
        //later on in the code.
        //It returns the row number of an inputted date

        int index = 0;
        for(int i = 0; i < dataArray.length; i++){

            if(dataArray[i][0] == date){

                index = i;

            }

        }
        return index;
    }


    public void makeHistoricalTrade(float buyDate, float sellDate){

        /*This method simulates a trade if the user were to buy
          the stock at on a certain date, and then sell it on a
          different date. It also uses the findRowIndex() method to
          determine whether or not the specified dates for buying and selling
          are valid*/

        float profitOrLoss = 0;
        float buyPrice = 0;
        float sellPrice = 0;

        if (findRowIndex(buyDate) - findRowIndex(sellDate) > 0) {

            //Because the array is sorted with the most recent data
            //at the top, the index of the buyDate should always be
            //greater than then index of the sellDate.

            for (int i = 1; i < dataArray.length; i++) {
                if (buyDate == dataArray[i][0]) {
                    buyPrice = dataArray[i][4];

                    //retrieves the closing price within the buyDate row
                }

                if (sellDate == dataArray[i][0]) {
                    sellPrice = dataArray[i][4];

                    //retrieves the closing price within the sellDate row
                }
            }

            profitOrLoss = sellPrice - buyPrice;

            if (profitOrLoss < 0) {

                System.out.println(profitOrLoss + " was lost per share for this trade");

            } else {

                System.out.println(profitOrLoss + " was made per share in profit for this trade");
            }

        } else{

            System.out.println("Invalid Date Range");

        }
    }

}
