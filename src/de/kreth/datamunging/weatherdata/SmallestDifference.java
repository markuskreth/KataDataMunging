package de.kreth.datamunging.weatherdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class SmallestDifference {

   private List<WeatherData> data;
   private WeatherData matching = null;
   
   public SmallestDifference() {
      data = new ArrayList<>();
   }
   
   private void find(File selectedFile) {
      loadData(selectedFile);
      findMatching();
   }
   
   private void findMatching() {
      for(WeatherData d : data) {
         if(matching == null || matching.difference()>5) 
            matching = d;
      }
      System.out.println(matching);
   }

   private void loadData(File selectedFile) {
      try {
         BufferedReader reader = Files.newBufferedReader(selectedFile.toPath());
         String line;
         while ((line = reader.readLine())!= null) {
            if(line.length()>10) {

               String day = line.substring(0, 5).trim();
               String maxTmp = line.substring(5, 8).trim();
               String minTmp = line.substring(11, 14).trim();
               if(day.matches("\\d{1,2}")) {
                  WeatherData data = new WeatherData();
                  data.dayNo = Integer.valueOf(day);
                  data.maxTmp = Integer.valueOf(maxTmp);
                  data.minTmp = Integer.valueOf(minTmp);
                  if(line.substring(8, 9).trim().length()>0)
                     data.monthsMax = true;
                  if(line.substring(14, 15).trim().length()>0)
                     data.monthsMin = true;
                  this.data.add(data);
               }
               
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   class WeatherData {
      int dayNo;
      int minTmp;
      int maxTmp;
      boolean monthsMin;
      boolean monthsMax;
      
      @Override
      public String toString() {
         return dayNo + ": " + minTmp + asterix(monthsMin) + " - " + maxTmp + asterix(monthsMax);
      }

      public int difference() {
         return maxTmp - minTmp;
      }

      private String asterix(boolean value) {
         return value?"*":"";
      }
   }
   

   public static void main(String[] args) {
      String fileName = null;
      if (args.length > 0) {
         
      } else {
         JFileChooser fileChooser = new JFileChooser(".");
         fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
         fileChooser.addChoosableFileFilter(new FileFilter() {
            
            @Override
            public String getDescription() {
               return "Wetterdaten weather.dat";
            }
            
            @Override
            public boolean accept(File f) {
               return f.getName().matches("weather\\.dat");
            }
         });
         int result = fileChooser.showOpenDialog(null);
         if(result == JFileChooser.APPROVE_OPTION) {
            SmallestDifference smallestDifference = new SmallestDifference();
            smallestDifference.find(fileChooser.getSelectedFile());
         }
      }
      
   }

}
