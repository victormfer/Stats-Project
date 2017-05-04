//////////////////////////////////////////////////////////////////////////
/////                                                                /////
/////                      Joseph Gutierrez                          /////
/////                    Prof. Desmond Koomson                       /////
/////                 Last Modified: May 1, 2017                     /////
/////                                                                /////
/////   This is a web scraper that identifies the address of each    /////
/////   station from the data at the site GasBuddy.com. It uses the  /////
/////   external java library jaunt. This is a program that will be  /////
/////   run only one time to get addresses then the addresses will   /////
/////   be passed to a batch coordinate generator on the site        /////
/////  http://stevemorse.org/jcal/latlonbatch.html?direction=forward /////
/////   this then gives latitude and longitude of each station.      /////
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

public class stationAddressScraper{
  public static void main(String [] args) throws FileNotFoundException, IOException{
    //open the .txt file with the URLs
    BufferedReader br = new BufferedReader(new FileReader("URLs.txt"));
    //create a String ArrayList with the URLs
    String line = "";
    ArrayList<String> URLs = new ArrayList<String>();
    while((line = br.readLine()) != null){
      URLs.add(line);
    }
    br.close();
    //pass string ArrayList to a method that makes an array list with addresses from the given URLs.
    ArrayList<String> addresses = getAddresses(URLs);
    //export data to a .txt file
    exportData(addresses);
    System.out.println("Done!");
  }

  public static ArrayList<String> getAddresses(ArrayList<String> URLs){
    System.out.println("Getting Addresses...");
    ArrayList<String> addresses = new ArrayList<String>();
    for(int i = 0; i < URLs.size(); i ++){
      System.out.println((i + 1) + " of " + URLs.size());
      try{
        UserAgent userAgent = new UserAgent();
        userAgent.visit(URLs.get(i));

        Elements dds = userAgent.doc.findEvery("<dd>");
        List<Element> in = dds.toList();
        String value = "";

        value = in.get(0).innerText() + in.get(2).innerText();

        addresses.add(value);

        //For Debugging purposes
        //System.out.println(value);
      }
      catch(JauntException e){
        System.err.println(e);
      }
    }
    return addresses;
  }


  public static void exportData(ArrayList<String> data) throws FileNotFoundException {
    System.out.println("Printing Results...");
    PrintWriter pw = new PrintWriter("addresses.txt");
    for(int i = 0; i < data.size(); i++){
      pw.println(data.get(i));
    }
    pw.close();
  }

}
