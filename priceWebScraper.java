//////////////////////////////////////////////////////////////////////////
/////                                                                /////
/////                      Joseph Gutierrez                          /////
/////                    Prof. Desmond Koomson                       /////
/////               Last Modified: April 25, 2017                    /////
/////                                                                /////
/////   This is a web scraper that identifies gas prices from the    /////
/////   site GasBuddy.com. It uses the external java library jaunt   /////
/////   This is a program that will be run several times in order    /////
/////   to identify average trends in gas prices in order to perform /////
/////   a statistical study on whether the price of gas at a station /////
/////   is at all dependent on the proximity to other gas stations.  /////
/////                                                                /////
//////////////////////////////////////////////////////////////////////////
import com.jaunt.*;
import com.jaunt.component.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.List;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class priceWebScraper{
  public static void main(String [] args) throws FileNotFoundException, IOException{
    long start = System.currentTimeMillis();
    //open the .txt file with the URLs
    BufferedReader br = new BufferedReader(new FileReader("URLs.txt"));
    //create a String ArrayList with the URLs
    String line = "";
    ArrayList<String> URLs = new ArrayList<String>();

    while((line = br.readLine()) != null){
      URLs.add(line);
    }
    br.close();
    //pass string ArrayList to a method that makes an array list with gas prices from the given URLs.
    ArrayList<String> prices = getPrices(URLs);
    //export data to a .txt file
    exportData(prices);
    long end = System.currentTimeMillis();
    long seconds = ((end - start)/1000);
    long minutes = seconds / 60;
    System.out.println("Scraping took " + minutes + " minutes and " + (seconds % 60) + " seconds.");
    System.out.println("Done!");
  }

  public static ArrayList<String> getPrices(ArrayList<String> URLs){
    System.out.println("Getting Prices...");
    ArrayList<String> prices = new ArrayList<String>();
    for(int i = 0; i < URLs.size(); i ++){
      System.out.println((i + 1) + " of " + URLs.size());
      try{
        UserAgent userAgent = new UserAgent();
        userAgent.visit(URLs.get(i));

        Elements divs = userAgent.doc.findEvery("<div class>");
        List<Element> in = divs.toList();
        String value = "";
        for(int j = 0 ; j < in.size(); j++){
          if(in.get(j).getAt("class").contains("price_num")){
            value = in.get(j).innerText();
            //For Debugging purposes
            //System.out.println(value);
            break;
          }
        }

        prices.add(value);
      }
      catch(JauntException e){
        System.err.println(e);
      }
    }
    return prices;
  }


  public static void exportData(ArrayList<String> data) throws FileNotFoundException {
    System.out.println("Printing Results...");
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    PrintWriter pw = new PrintWriter(dateFormat.format(date) + " prices.txt");
    for(int i = 0; i < data.size(); i++){
      pw.println(data.get(i));
    }
    pw.close();
  }

}
