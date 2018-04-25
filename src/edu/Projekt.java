package edu;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Projekt extends Application {

    Button btn4;
    Button btn6;
    CSVRead plik;
    Label chooseMarker;
    VBox layout;
    Button btnOK;
    Button btnwykresy;
    Button btnAnimacja;
    Button btnCOM;
    Button btneksport;
    ListView<String> selected;
    List<String> selectedMarker;
    ChartAnimComWrite file;

    @Override
    public void start(Stage primaryStage) throws Exception {


        layout = new VBox();

        layout.setPadding(new Insets(20, 20, 30, 30));
        plik = new CSVRead();
        file = new ChartAnimComWrite();


        btn4 = new Button("v = 4 km/h");
        btn6 = new Button("v = 6 km/h");
        btn4.setPrefWidth(150);
        btn6.setPrefWidth(150);
        btn4.setOnAction(e -> {
            plik.setFilePath("Bartek_threadmill_4.csv");
            CSVRead.read();
            chooseMar();
        });
        btn6.setOnAction(e -> {
            plik.setFilePath("Bartek_threadmill_6.csv");
            CSVRead.read();
            chooseMar();
        });

        HBox hbox1 = new HBox(btn4, btn6);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setSpacing(20);
        layout.getChildren().add(hbox1);
        layout.setAlignment(Pos.CENTER);


        Scene scene = new Scene(layout, 1000, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void chooseMar() {
        chooseMarker = new Label("Proszę wybrać marker: ");
        layout.setSpacing(20);
        HBox hb = new HBox(chooseMarker);
        hb.setAlignment(Pos.CENTER);
        layout.getChildren().add(hb);
        layout.setSpacing(20);
        ObservableList<String> items = FXCollections.observableArrayList(
                "LIAS", "RIAS", "LIPS", "RIPS", "LFAL", "RFAL");
        ListView<String> list = new ListView<>(items);
        //ListView<String>
        selected = new ListView<>();
        HBox root = new HBox(list);
        root.setPrefSize(100, 100);
        root.setAlignment(Pos.CENTER);
        layout.setSpacing(30);
        layout.getChildren().add(root);
        btnOK = new Button("OK");
        selectedMarker = new ArrayList<>();
        //selectedMarkers.add(selected);
        list.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            selected.setItems(list.getSelectionModel().getSelectedItems());
            layout.getChildren().add(btnOK);
        });


        btnOK.setOnAction(e -> {
            selectedMarker.clear();
            selectedMarker.add(selected.getItems().get(0));
            System.out.println(selectedMarker + " wcisniety(selectedMarker)");
            CSVRead.chooseMarkers(selectedMarker);
            CSVRead.chosenMarkers();
            CSVRead.coordinates();
            buttons();
        });

    }

    private void buttons() {
        btnwykresy = new Button("Wykresy");
        btnwykresy.setPrefSize(120, 50);
        btnAnimacja = new Button("Animacja");
        btnAnimacja.setPrefSize(120, 50);
        btnCOM = new Button("Środek masy");
        btnCOM.setPrefSize(120, 50);
        btneksport = new Button("Eksportuj");
        btneksport.setPrefSize(120, 50);
        HBox hb = new HBox(btnwykresy, btnAnimacja, btnCOM, btneksport);
        hb.setSpacing(60);
        hb.setAlignment(Pos.CENTER);
        layout.getChildren().add(hb);

        btnwykresy.setOnAction(e -> {
            file.displayCharts();
        });
        btnAnimacja.setOnAction(e -> {
            file.displayAnimation();
        });
        btnCOM.setOnAction(e -> {
            file.displayCOM();
        });
        btneksport.setOnAction(e -> {
            try {
                file.Eksport();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }


}
