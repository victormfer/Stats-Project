//////////////////////////////////////////////////////////////////////////
/////                                                                /////
/////                      Joseph Gutierrez                          /////
/////                    Prof. Desmond Koomson                       /////
/////                 Last Modified: May 1, 2017                     /////
/////                                                                /////
/////   This is a web scraper that identifies the URL for gas        /////
/////   stations within predetermined zip codes from the site        /////
/////   GasBuddy.com. It uses the external java library jaunt.       /////
/////   This is a program that will be run only once in order to     /////
/////   get a list of stations we will be studying for the project.  /////
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

public class URLScraper{
  public static void main(String [] args) throws FileNotFoundException{
    //open the .txt file with the zipcodes
    BufferedReader br = new BufferedReader(new FileReader("Blosum62.txt"));
    //create a String ArrayList with the zipcodes
    String line = "";
    ArrayList<String> zipcodes = new ArrayList<String>();
    while((line = br.readLine()) != null){
      zipcodes.add(line);
    }
    br.close();
    //pass string ArrayList to a method that makes an array list with anchors from the given zipcodes.
    ArrayList<String> anchors = getAnchors(zipcodes);
    //takes in anchors and returns just the URLs
    ArrayList<String> URLs = getURLs(anchors);
    //check for and take out any duplicate URLs
    URLs = deleteDuplicates(URLs);
    //export data to a .txt file
    exportData(URLs);
    System.out.println("Done!");
  }

  public static ArrayList<String> getAnchors(ArrayList<String> zipcodes){
    System.out.println("Getting Anchors...");
    ArrayList<String> anchors = new ArrayList<String>();
    for(int i = 0; i < zipcodes.size(); i ++){
      try{
        UserAgent userAgent = new UserAgent();
        userAgent.visit("https://www.gasbuddy.com/");
        userAgent.doc.apply(zipcodes.get(i));
        userAgent.doc.submit("search-btn");

        Elements strong = userAgent.doc.findEvery("<strong>");
        List<Element> links = strong.toList();
        for(int j = 0; j < links.size(); j++){
          if(links.get(j).getInnerText().contains("<a href = \"\\Station\\")){
            anchors.add(links.get(j).getInnerText());
          }
        }
      }
      catch(JauntException e){
        System.err.println(e);
      }
    }
    return anchors;
  }

  public static ArrayList<String> getURLs(ArrayList<String> anchors){
    System.out.println("Getting URLs...");
    ArrayList<String> URLs = new ArrayList<String>();
    String [] info = new String [1];
    //splits it into 3 parts: before quotation, between quotations, and after quotation
    // we want therefore the middle.
    for(int i = 0; i < anchors.size(); i ++){
      info = anchors.get(i).split("\"");
      URLs.add(info[1]);
    }
    return URLs;
  }

  public static ArrayList<String> deleteDuplicates(ArrayList<String> data){
    System.out.println("Deleting Duplicates...");
    ArrayList<String> noDuplicates = new ArrayList<String>();
    String elem = "";
    for(int i = 0; i < data.size(); i++){
      elem = data.get(i);
      if(!noDuplicates.contains(elem)){
        noDuplicates.add(elem);
      }
    }
    return noDuplicates;
  }

  public static void exportData(ArrayList<String> data) throws FileNotFoundException {
    System.out.println("Printing Results...");
    PrintWriter pw = new PrintWriter("URLs.txt");
    for(int i = 0; i < data.size(); i++){
      pw.println(data.get(i));
    }
    pw.close();
  }

}
