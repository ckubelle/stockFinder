import java.util.Scanner;
import java.io.IOException;
//Made by Christian Kubelle, Cole Smith, Micheal Semeraro
//stockFinder school project
public class finalMain {

    public static void main(String[] args) throws IOException
    {
        String doMore = "y";

        System.out.println("Enter the ticker of the stock you would like to search: ");
        Scanner userInput = new Scanner(System.in);
        String tickerSearch = userInput.nextLine();

        readData x = new readData();
        x.getCurrentPrice(tickerSearch);

        System.out.println();

        x.getStockData(tickerSearch);
        x.convertToArray();
        x.convertToFloat();
        x.dailyPercentChange();

        System.out.println("Two hundred day sma is:" + x.twoHundredDaySma());

        System.out.println();

        while(doMore.equals("y")) {

            System.out.println("What else would you like to do?");
            System.out.println("1 = Access all historical data of the stock");
            System.out.println("2 = Find the all time high");
            System.out.println("3 = Find the all time low");
            System.out.println("4 = Search for data on a certain date");
            System.out.println("5 = Calculate the simple moving average");
            System.out.println("6 = Simulate a historical trade");
            System.out.println("7 = Monthly percent change");
            System.out.println("8 = Yearly percent change");

            String choice = userInput.nextLine();

            if (choice.equals("1")) {
                System.out.println("Date, Open, High, Low, Close, Volume");
                x.printString();
            }

            else if (choice.equals("2")) {
                //x.convertToFloat();
                x.findHigh();
            }

            else if (choice.equals("3")) {
                x.findLow();
            }

            else if (choice.equals("4")) {
                System.out.println("Enter the date that you would like to search(MM-DD-YYYY with no spaces or dashes ex. 12102018)");
                String date = userInput.nextLine();
                System.out.println("Date, Open, High, Low, Close, Volume");
                x.dayFinder(date);
                System.out.println();
            }

            else if (choice.equals("5")) {
                System.out.println("Enter the amount of days you would like to base your calculation off of:");
                int days = new Integer(userInput.nextLine());
                System.out.println("Now enter how you would like to calculate the SMA");
                System.out.println("1 = Open");
                System.out.println("2 = High");
                System.out.println("3 = Low");
                System.out.println("4 = Close");
                int type = new Integer(userInput.nextLine());
                System.out.println(x.simpleMovingAverage(days, type));
            }

            else if (choice.equals("6")) {
                System.out.println("Enter the date you would have liked to buy the stock(MM-DD-YYYY with no spaces or dashes ex. 12102018):");
                float buyDate = new Float(userInput.nextLine());
                System.out.println("Now enter the date you would have liked to sell it(MM-DD-YYYY with no spaces or dashes ex. 12102018):");
                float sellDate = new Float(userInput.nextLine());
                x.makeHistoricalTrade(buyDate, sellDate);
            }

            else if (choice.equals("7")) {

                x.monthlyPercentChange();

            }

            else if (choice.equals("8")) {

                x.yearlyPercentChange();

            }
            System.out.println("Would you like to do anything else?(y/n)");
            doMore = userInput.nextLine();
        }
    }
}