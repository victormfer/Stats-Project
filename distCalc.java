import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class distCalc{
  public static void main(String [] args) throws FileNotFoundException, IOException{
    long start = System.currentTimeMillis();
    //open the .txt file with the coordinates
    BufferedReader br = new BufferedReader(new FileReader("dummy.txt"));
    //create a String ArrayList with the coordinates
    String line = "";
    ArrayList<String> coordinates = new ArrayList<String>();
    while((line = br.readLine()) != null){
      coordinates.add(line);
    }
    br.close();
    //pass string ArrayList to a method that makes an array list with distances.
    ArrayList<Double> distances = getDistances(coordinates);
    //export data to a .txt file
    exportData(distances);
    long end = System.currentTimeMillis();
    System.out.println("This operation took " + (end - start) + " ns.");
    System.out.println("Done!");
  }

  public static ArrayList<Double> getDistances(ArrayList<String> coordinates){
    System.out.println("Getting Distances...");

    ArrayList<Double> lng = new ArrayList<Double>();
    ArrayList<Double> lat = new ArrayList<Double>();
    String [] seperated = new String [1];
    //Makes lat and lng array lists to pass to distance method
    for(int i = 0; i < coordinates.size(); i ++){
      seperated = coordinates.get(i).split(",");
      lat.add(Double.parseDouble(seperated[0]));
      lng.add(Double.parseDouble(seperated[1]));
    }

    //Make a distance matrix
    double [] [] distance = new double [coordinates.size()][coordinates.size()];
    double d = 0.0;
    for(int i = 0; i < coordinates.size(); i++){
      distance[i][i] = Integer.MAX_VALUE;
      for(int j = i + 1; j < coordinates.size(); j++){
        d = calculateDistanceInKm(lat.get(i), lng.get(i), lat.get(j), lng.get(j));
        distance[i][j] = d;
        distance[j][i] = d;
      }
    }

    //print(distance);

    //Calculates distances
    ArrayList<Double> dist = new ArrayList<Double>();
    for(int i = 0; i < coordinates.size(); i++){
     double sum = 0;
     Arrays.sort(distance[i]);
     for(int j = 0; j < 5; j++){
       sum += distance[i][j];
     }
     double average = 0;
     average = sum/5;
     //System.out.println(average);
     dist.add(average);
  }
    //System.out.println();
    //print(distance);


    return dist;
  }

  public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

  public static double calculateDistanceInKm(double userLat, double userLng, double venueLat, double venueLng) {
      double latDistance = Math.toRadians(userLat - venueLat);
      double lngDistance = Math.toRadians(userLng - venueLng);
      double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
        * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      return (AVERAGE_RADIUS_OF_EARTH_KM * c);
  }


  public static void exportData(ArrayList<Double> data) throws FileNotFoundException {
    System.out.println("Printing Results...");
    PrintWriter pw = new PrintWriter("distances.txt");
    for(int i = 0; i < data.size(); i++){
      pw.println(data.get(i));
    }
    pw.close();
  }

  //For Debugging purposes
	public static void print( double [] [] A){
		for(int i = 0 ; i < A.length; i ++){
			for(int j = 0 ; j < A[i].length;j++){
				System.out.print(A[i][j] + " ");
			}
			System.out.println();
		}
	}

}
