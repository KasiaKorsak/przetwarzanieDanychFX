package edu;

import javafx.animation.PathTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ChartAnimComWrite {

    LineChart<Number, Number> figure;

    LineChart<Number, Number> figure2;

    LineChart<Number, Number> figure3;

    private static int counter;

    public void displayCharts() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        NumberAxis x = new NumberAxis();
        x.setLabel("time");
        NumberAxis y = new NumberAxis();
        y.setLabel("X");
        figure = new LineChart<Number, Number>(x, y);
        List<XYChart.Series> data = CSVRead.getSeries();

        XYChart.Series dataChart = data.get(0);
        figure.setLegendVisible(false);
        figure.setCreateSymbols(false);
        figure.getData().add(dataChart);

        dataChart = data.get(1);

        NumberAxis x2 = new NumberAxis();
        x.setLabel("time");
        NumberAxis y2 = new NumberAxis();
        y.setLabel("Y");
        figure2 = new LineChart<Number, Number>(x2, y2);
        figure2.setLegendVisible(false);
        figure2.setCreateSymbols(false);
        figure2.getData().add(dataChart);

        dataChart = data.get(2);

        NumberAxis x3 = new NumberAxis();
        x.setLabel("time");
        NumberAxis y3 = new NumberAxis();
        y.setLabel("Z");
        figure3 = new LineChart<Number, Number>(x3, y3);
        figure3.setLegendVisible(false);
        figure3.setCreateSymbols(false);
        figure3.getData().add(dataChart);

        HBox hbox = new HBox(figure, figure2, figure3);
        hbox.setSpacing(50);
        hbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(hbox, 800, 400);
        window.setScene(scene);

        window.show();
    }

    public void displayAnimation() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        List<Double> y = (List<Double>) CSVRead.getAnim().get(0);
        List<Double> z = (List<Double>) CSVRead.getAnim().get(1);
        Path out = new Path();
        PathTransition pt = new PathTransition();
        double xc = 400;
        double yc = 400;
        Circle circle = new Circle();
        circle.setCenterX(xc);
        circle.setCenterY(yc);
        circle.setRadius(10);
        out.getElements().add(new MoveTo(y.get(0) * 200 + xc, z.get(0) * 200 + yc));
        for (int i = 1; i < y.size(); i++) {
            double x_p = xc + y.get(i) * 200;
            double y_p = yc + z.get(i) * 200;
            out.getElements().add(new LineTo(x_p, y_p));
        }
        pt.setDuration(Duration.seconds(100));
        pt.setPath(out);
        pt.setNode(circle);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.setCycleCount(1);
        pt.setAutoReverse(false);

        Group root = new Group(circle);
        pt.play();
        Scene scene = new Scene(root, 800, 800);
        window.setScene(scene);

        window.show();
    }

    public void displayCOM() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        List<List<Double>> data = CSVRead.getAnim();

        List<Double> y = data.get(0);
        List<Double> z = data.get(1);
        List<Double> x = data.get(2);
        List<Double> t = data.get(3);

        System.out.println(t.get(5) + " displaycom");
        XYChart.Series x_points = new XYChart.Series();
        XYChart.Series y_points = new XYChart.Series();
        XYChart.Series z_points = new XYChart.Series();


        for (int j = 0; j < t.size(); j++) {
            Double sumx = 0.0;
            Double sumy = 0.0;
            Double sumz = 0.0;
            for (int i = 0; i < 4; i++) {
                CSVRead.setNumber(i);
                CSVRead.chosenMarkers();
                CSVRead.coordinates();
                sumx += x.get(j);
                sumy += y.get(j);
                sumz += z.get(j);
            }
            Double avgx = sumx / 4;
            Double avgy = sumy / 4;
            Double avgz = sumz / 4;
            x_points.getData().add(new XYChart.Data<>(t.get(j), avgx));
            y_points.getData().add(new XYChart.Data<>(t.get(j), avgy));
            z_points.getData().add(new XYChart.Data<>(t.get(j), avgz));
        }

        NumberAxis x1 = new NumberAxis();
        x1.setLabel("time");
        NumberAxis y1 = new NumberAxis();
        y1.setLabel("X");
        figure = new LineChart<Number, Number>(x1, y1);
        figure.setLegendVisible(false);
        figure.setCreateSymbols(false);
        figure.getData().add(x_points);

        NumberAxis x2 = new NumberAxis();
        x2.setLabel("time");
        NumberAxis y2 = new NumberAxis();
        y2.setLabel("Y");
        figure2 = new LineChart<Number, Number>(x2, y2);
        figure2.setLegendVisible(false);
        figure2.setCreateSymbols(false);
        figure2.getData().add(y_points);

        NumberAxis x3 = new NumberAxis();
        x3.setLabel("time");
        NumberAxis y3 = new NumberAxis();
        y3.setLabel("Z");
        figure3 = new LineChart<Number, Number>(x3, y3);
        figure3.setLegendVisible(false);
        figure3.setCreateSymbols(false);
        figure3.getData().add(z_points);

        HBox hbox = new HBox(figure, figure2, figure3);
        hbox.setSpacing(50);
        hbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(hbox, 800, 800);
        window.setScene(scene);

        window.show();
    }

    public void Eksport() throws IOException {

        List data = CSVRead.getAnim();
        List<Double> x = (List<Double>) data.get(2);
        List<Double> y = (List<Double>) data.get(0);
        List<Double> z = (List<Double>) data.get(1);
        List<Double> t = (List<Double>) data.get(3);
        System.out.println(t.get(2));

        File file = new File("src\\export\\export_" + Integer.toString(counter) + ".csv");
        FileWriter fw = new FileWriter(file, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Time, X, Y, Z");
        bw.newLine();
        for (int i = 0; i < t.size(); i++) {
            bw.write(Double.toString(t.get(i)) + "," + Double.toString(x.get(i)) + "," + Double.toString(y.get(i)) + "," + Double.toString(z.get(i)));
            bw.newLine();
        }
        bw.close();
        counter++;
    }

    static {
        counter = 1;
    }
}
