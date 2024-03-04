package com.espcommander.commander.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class CalibrationTool
{
    static private TextField boardImagePath = new TextField();
    static private TextField boardFirstRowX = new TextField();
    static private TextField boardFirstRowY = new TextField();
    static private TextField boardSecondRowX = new TextField();
    static private TextField boardSecondRowY = new TextField();
    static private TextField pinDiameter = new TextField();
    static private TextField pinGap = new TextField();
    static private FileChooser boardImageChooser = new FileChooser();
    static private Button fileChooserButton = new Button("...");
    static private Button loadButton = new Button("Load");
    static private Button savePack = new Button("Save");
    static private Label viewCoordinates = new Label();
    static private ImageView boardView = new ImageView();


    static private boolean calibrationAssistantActive = false;
    static public Node spawn()
    {
        BorderPane mainPane = new BorderPane();
        Stage mainStage = new Stage();
        Scene mainScene = new Scene(mainPane, Color.rgb(250,250,250));


        boardView.setFitHeight(640);
        boardView.setFitWidth(480);
        boardImageChooser.setTitle("Pick board image");

        boardImagePath.setMinWidth(150.0);

        fileChooserButton.setOnMousePressed((e)->{
            File theChosenOne = boardImageChooser.showOpenDialog(mainStage);
            if(theChosenOne != null) boardImagePath.setText(theChosenOne.getPath());
        });
        loadButton.setOnMousePressed((ev)->{
            try {
                boardView.setImage(MiscUtils.getImageAbsolute(boardImagePath.getText()));
            }
            catch(Exception ex)
            {
                System.out.println(ex.getStackTrace());
                System.out.println(ex.getMessage());
            }
        });
        boardView.setOnMouseClicked(CalibrationTool::boardViewCallBack);
        mainPane.setTop(makeRow(new Node[]{ new Label("Board image path"),boardImagePath,fileChooserButton,loadButton}));
        mainPane.setCenter(boardView);
        mainPane.setBottom(viewCoordinates);
        mainPane.setRight(generateCalibrationPanel());
        mainStage.setScene(mainScene);
        mainStage.setWidth(1024);
        mainStage.setHeight(720);
        //mainStage.setResizable(false);
        mainStage.setTitle("Board Calibration Tool");
        mainStage.show();
        return mainPane;
    }
    static private HBox makeRow(Node[] nodeList)
    {
        HBox rowBox = new HBox();
        rowBox.setSpacing(20);
        rowBox.setAlignment(Pos.CENTER_LEFT);
        for(Node node : nodeList)
        {
            if( node instanceof TextField) HBox.setHgrow(node, Priority.ALWAYS);
            rowBox.getChildren().add(node);
        }
        return rowBox;
    }
    static private void boardViewCallBack(MouseEvent event)
    {
        viewCoordinates.setText("Pressed ( "+ event.getX() +" , "+ event.getY()+" )");
    }
    static private Node generateCalibrationPanel()
    {
        VBox calibrationPanel = new VBox();
        Button toggleCalibrationAssistant = new Button("Start Calibration Assistant");

        toggleCalibrationAssistant.setOnMousePressed((event)->{
            if(calibrationAssistantActive)
            {
                toggleCalibrationAssistant.setText("Start Calibration Assistant");
                calibrationAssistantActive = false;
            }
            else
            {
                toggleCalibrationAssistant.setText("Stop Calibration Assistant");
                calibrationAssistantActive = true;
            }
            for(Node row : calibrationPanel.getChildren())
            {
                    for(Node node : ((HBox) row).getChildren())
                    {
                        if(node instanceof TextField) node.setDisable(calibrationAssistantActive);
                    }
            }
        });

        calibrationPanel.setSpacing(20);
        calibrationPanel.setAlignment(Pos.CENTER);
        calibrationPanel.getChildren().add(makeRow(new Node[]{new Label("First row X"),boardFirstRowX}));
        calibrationPanel.getChildren().add(makeRow(new Node[]{new Label("First Row Y"),boardFirstRowY}));
        calibrationPanel.getChildren().add(makeRow(new Node[]{new Label("Second row X"),boardSecondRowX}));
        calibrationPanel.getChildren().add(makeRow(new Node[]{new Label("Second Row Y"),boardSecondRowY}));
        calibrationPanel.getChildren().add(makeRow(new Node[]{new Label("Pin diameter"),pinDiameter}));
        calibrationPanel.getChildren().add(makeRow(new Node[]{new Label("Pin gap"),pinGap}));
        calibrationPanel.getChildren().add(makeRow(new Node[]{toggleCalibrationAssistant,savePack}));

        for(Node node : calibrationPanel.getChildren()) VBox.setVgrow(node,Priority.ALWAYS);
        return calibrationPanel;
    }
}
