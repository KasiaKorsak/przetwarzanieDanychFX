package edu;

import javafx.scene.chart.XYChart;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVRead {

    private static String filePath;
    private static List<Pattern> patterns;
    private static String model;
    static List<Double> x;
    static List<Double> y;
    static List<Double> z;
    static List<Double> time;
    private static List<Integer> out;
    private static int number;
    private static int rows;
    static ArrayList<String> file;
    static int k;


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static void setNumber(int number) {
        CSVRead.number = number;
    }

    public static void read() {
        file = new ArrayList<>();
        rows=0;
        k=0;
        try {
            Scanner scanner = new Scanner(new File(filePath));
            scanner.useDelimiter(",");
            for (int i=0; i<7; i++) {
                file.add(scanner.nextLine());
            }
            String temp;
            while (scanner.hasNext()) {
                temp = scanner.nextLine();
                if (rows%100==1) {
                    file.add(temp);
                    System.out.println(file.get(k));
                    k++;
                }
                rows++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(rows);
    }


    public static void chooseMarkers(List<String> selectedMarker) {

        System.out.println(selectedMarker);
        if (selectedMarker.get(0).equals("LIAS"))
            number=0;
        else if(selectedMarker.get(0).equals("RIAS"))
            number=1;
        else if(selectedMarker.get(0).equals("LIPS"))
            number=2;
        else if(selectedMarker.get(0).equals("RIPS"))
            number=3;
        else if(selectedMarker.get(0).equals("LFAL"))
            number=4;
        else if(selectedMarker.get(0).equals("RFAL"))
            number=5;
        else
            System.out.println("blad");
        System.out.println(number + " number");
    }


    public static List<Integer> chosenMarkers() {
        out = new ArrayList<>();
        String[] markers;
        int m = 0;
        markers = file.get(3).split(",");
        for (int i = 0; i < markers.length - 2; i++) {
            if (markers[i].equals(markers[i + 1]) && markers[i + 1].equals(markers[i + 2])) {
                for (Pattern p : patterns) {
                    Matcher matcher = p.matcher(markers[i]);
                    if (matcher.find()) {
                        out.add(i);
                        if (m == 0) {
                            model = markers[i];
                            m++;
                        }
                    }
                }
            }
        }

        model = model.replace("LIAS", "");
        return out;
    }

    public static void coordinates(){ //wspolrzedne
        int i=out.get(number);
        x=new ArrayList<>();
        y=new ArrayList<>();
        z=new ArrayList<>();
        time=new ArrayList<>();
        for(int j=7;j<k;j++){
            String [] line=file.get(j).split(",");
            x.add(Double.parseDouble(line[i]));
            y.add(Double.parseDouble(line[i+1]));
            z.add(Double.parseDouble(line[i+2]));
            time.add(Double.parseDouble(line[1]));
            // System.out.println(x.get(j-7)+" "+time.get(j-7));
        }
    }

    public static List<XYChart.Series> getSeries() {
        List<XYChart.Series> out = new ArrayList<>();
        XYChart.Series x_points=new XYChart.Series();
        XYChart.Series y_points=new XYChart.Series();
        XYChart.Series z_points=new XYChart.Series();
        for(int i=0;i<time.size();i++){
            System.out.println(time.get(i));
            x_points.getData().add(new XYChart.Data(time.get(i),x.get(i)));
            y_points.getData().add(new XYChart.Data(time.get(i),y.get(i)));
            z_points.getData().add(new XYChart.Data(time.get(i),z.get(i)));
        }
        out.add(x_points);
        out.add(y_points);
        out.add(z_points);

        return out;
    }

    public static List getAnim(){
        List<List<Double>> out = new ArrayList<>();
        out.add(y);
        out.add(z);
        out.add(x);
        out.add(time);
        return out;
    }

    static {
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("LIAS"));
        patterns.add(Pattern.compile("RIAS"));
        patterns.add(Pattern.compile("LIPS"));
        patterns.add(Pattern.compile("RIPS"));
        patterns.add(Pattern.compile("LFAL"));
        patterns.add(Pattern.compile("RFAL"));
        model = "Bartek:";
    }
}
