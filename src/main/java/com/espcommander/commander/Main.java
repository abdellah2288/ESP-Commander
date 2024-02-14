package com.espcommander.commander;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;

import java.lang.reflect.Field;
import java.util.HashMap;

public class Main extends Application
{
    private static HashMap<String,String[]> pinMap = new HashMap<>()
    {{
        put("GND",new String[]{"GND","POWER"});
        put("GPIO 23",new String[]{"GPIO 23","IO & PWM","VSPI_D","MOSI"});
        put("GPIO 22",new String[]{"GPIO 22","IO & PWM","VSPI_WP","SCL","RTS 0"});
        put("TX",new String[]{"GPIO 1","IO & PWM","TXD 0","CLK 3"});
        put("RX",new String[]{"GPIO 3","IO & PWM","RXD 0","CLK 3"});
        put("GPIO 21",new String[]{"GPIO 21","IO & PWM","VSPI_HD","SDA"});
        put("GPIO 19",new String[]{"GPIO 19","IO & PWM","VSPI_Q","MISO","CTS 0"});
        put("GPIO 18",new String[]{"GPIO 19","IO & PWM","VSPI_CLK","SCK"});
        put("GPIO 5",new String[]{"GPIO 5","IO & PWM","VSPI_CS0","SS"});
        put("GPIO 17",new String[]{"GPIO 17","IO & PWM","TXD 2"});
        put("GPIO 16",new String[]{"GPIO 16","IO & PWM","RXD 2"});
        put("GPIO 4",new String[]{"GPIO 4","IO & PWM","ADC2_0","HSPI_HD","TOUCH 0","RTC GPIO10"});
        put("GPIO 0",new String[]{"GPIO 0","IO & PWM","ADC2_1","CLK 1","TOUCH 1","RTC GPIO11"});
        put("GPIO 2",new String[]{"GPIO 2","IO & PWM","ADC2_2","HSPI_WP 0","TOUCH 2","RTC GPIO12"});
        put("GPIO 15",new String[]{"GPIO 15","IO & PWM","ADC2_3","HSPI_CS 0","TOUCH 3","RTC GPIO13"});
        put("D1",new String[]{"GPIO 8","IO & PWM","CTS 2","HSPI_DATA1","SD DATA1","FLASH D1"});
        put("D0",new String[]{"GPIO 7","IO & PWM","RTS 2","HSPI_DATA0","SD DATA0","FLASH D0"});
        put("CLK",new String[]{"CLK","IO & PWM","CTS 1","HSPI_CLK","SD CLK","FLASH SCL"});
        put("3v3",new String[]{"3v3","POWER"});
        put("EN",new String[]{"EN"});
        put("VP",new String[]{"GPIO 36","INPUT ONLY","ADC1_0","SENSVP","RTC GPIO0"});
        put("VN",new String[]{"GPIO 39","INPUT ONLY","ADC1_3","SENSVN","RTC GPIO3"});
        put("GPIO 34",new String[]{"GPIO 34","INPUT ONLY","ADC1_6","RTC GPIO4"});
        put("GPIO 35",new String[]{"GPIO 35","INPUT ONLY","ADC1_7","RTC GPIO5"});
        put("GPIO 32",new String[]{"GPIO 32","IO & PWM","ADC1_4","XTAL32P","RTC GPIO9"});
        put("GPIO 33",new String[]{"GPIO 33","IO & PWM","ADC1_5","XTAL32N","RTC GPIO8"});
        put("GPIO 25",new String[]{"GPIO 25","IO & PWM","ADC2_8","RTC GPIO6","DAC1"});
        put("GPIO 26",new String[]{"GPIO 26","IO & PWM","ADC2_9","RTC GPIO7","DAC2"});
        put("GPIO 27",new String[]{"GPIO 27","IO & PWM","ADC2_7","TOUCH 7","RTC GPIO17"});
        put("GPIO 14",new String[]{"GPIO 14","IO & PWM","ADC2_6","HSPI_CLK","TOUCH 6","RTC GPIO16"});
        put("GPIO 12",new String[]{"GPIO 12","IO & PWM","ADC2_5","HSPI_Q","TOUCH 5","RTC GPIO15"});
        put("GPIO 13",new String[]{"GPIO 13","IO & PWM","ADC2_4","HSPI_ID","TOUCH 4","RTC GPIO14"});
        put("D2",new String[]{"GPIO 9","IO & PWM (NR)","RXD1","HSPI_DATA2","SD DATA2","FLASH D2"});
        put("D3",new String[]{"GPIO 10","IO & PWM (NR)","TXD1","HSPI_DATA3","SD DATA3","FLASH D3"});
        put("CMD",new String[]{"GPIO 11","IO & PWM (NR)","RTS1","HSPI_CMD","SD CMD","FLASH CMD"});
        put("5V",new String[]{"5V","POWER"});
    }};
    private static Label pressedPin;
    private static float secondRowX = 116.0f;
    private static float firstRowX = 366.0f;
    private static float pinGap = 9.0f;
    private static float pinDiameter = 17.0f;
    private static float firstPin_Y = 102.0f;
    private static ImageView pointer ;
    private static VBox infoBox;
    private static Group baseStack;
    private static HBox inputBox;
    private static VBox consoleBox;
    static private String[] tileColorList = new String[]{"gray","red","brown","piss","purple","blue"};
    private static String[] pinRow_1 = new String[]{"GND","GPIO 23","GPIO 22","TX","RX","GPIO 21","GND","GPIO 19","GPIO 18","GPIO 5","GPIO 17","GPIO 16","GPIO 4","GPIO 0","GPIO 2","GPIO 15","D1","D0","CLK"};
    private static String[] pinRow_2 = new String[]{"3v3","EN","VP","VN","GPIO 34","GPIO 35","GPIO 32","GPIO 33","GPIO 25","GPIO 26","GPIO 27","GPIO 14","GPIO 12","GND","GPIO 13","D2","D3","CMD","5V"};
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane rootPane = new BorderPane();
        infoBox = new VBox();
        consoleBox = new VBox();
        baseStack = new Group();
        pointer = getStaticAsset("pointer.png",0,0);
        pressedPin = new Label();
        baseStack.getChildren().add(getStaticAsset("esp32-devkitc_vertical.png"));
        baseStack.setOnMouseClicked(e -> locatePress(e));

        rootPane.setLeft(consoleBox);
        rootPane.setCenter(baseStack);
        rootPane.setRight(infoBox);
        rootPane.setBottom(pressedPin);

        resetInfoBox();

        Scene rootScene = new Scene(rootPane, Color.BEIGE);
        stage.setScene(rootScene);

        stage.sizeToScene();
        stage.show();

        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());


    }
    void locatePress(MouseEvent mouseEvent)
    {
        if(mouseEvent.getX() < firstRowX + pinDiameter && mouseEvent.getX() > firstRowX)
        {
            for(int i = 0 ; i < 19 ;i++)
            {
                if(mouseEvent.getY() < (firstPin_Y + pinDiameter) + (pinDiameter + pinGap)*i)
                {
                    pressedPin.setText("Pressed ( " + pinRow_1[i] + " ) at ( "+ mouseEvent.getX() +" , "+mouseEvent.getY()+" )");
                    displayPinInfo(pinRow_1[i]);
                    baseStack.getChildren().remove(pointer);
                    pointer.setLayoutX(firstRowX);
                    pointer.setLayoutY(firstPin_Y+ (pinDiameter + pinGap)*(i));
                    baseStack.getChildren().add(pointer);
                    break;
                }
            }
        }
        else if(mouseEvent.getX() < secondRowX + pinDiameter && mouseEvent.getX() > secondRowX)
        {
            for(int i = 0 ; i < 19 ;i++)
            {
                if(mouseEvent.getY() < (firstPin_Y + pinDiameter) + (pinDiameter + pinGap)*i)
                {
                    pressedPin.setText("Pressed ( " + pinRow_2[i] + " ) at ( "+ mouseEvent.getX() +" , "+mouseEvent.getY()+" )");
                    displayPinInfo(pinRow_2[i]);
                    baseStack.getChildren().remove(pointer);
                    pointer.setLayoutX(secondRowX);
                    pointer.setLayoutY(firstPin_Y+ (pinDiameter + pinGap)*(i));
                    baseStack.getChildren().add(pointer);
                    break;
                }
            }
        }
        else
        {
            pressedPin.setText("Pressed  ( "+ mouseEvent.getX() +" , "+mouseEvent.getY()+" )");
            resetInfoBox();
            baseStack.getChildren().remove(pointer);
        }

    }
    void displayPinInfo(String pinName)
    {

        infoBox.getChildren().clear();
        String[] pinParams = pinMap.get(pinName);
        if(pinParams != null)
        {
            for (int i = 0; i < pinParams.length; i++)
            {
                infoBox.getChildren().add(makeTile(tileColorList[i], pinParams[i]));
            }
        }
        else
        {
            resetInfoBox();
        }
    }
    Node makeTile(String tileColor, String tileText)
    {
        try
        {
        StackPane testTile_pane = new StackPane();
        Label testTile_label = new Label(tileText);

        testTile_pane.getChildren().add(getStaticAsset("tile_"+tileColor.toLowerCase()+".png"));
        testTile_pane.getChildren().add(testTile_label);
        testTile_label.setTextFill(Color.WHITE);
        return testTile_pane;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    ImageView getStaticAsset(String assetName)
    {
        try
        {
            Image asset = new Image(new FileInputStream("/home/abdellah/IdeaProjects/ESP Commander/static/" + assetName));
            ImageView assetView = new ImageView(asset);
            return assetView;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    static ImageView getStaticAsset(String assetName,double X,double Y)
    {
        try
        {
            Image asset = new Image(new FileInputStream("/home/abdellah/IdeaProjects/ESP Commander/static/" + assetName));
            ImageView assetView = new ImageView(asset);

            assetView.setLayoutX(X);
            assetView.setLayoutY(Y);

            return assetView;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    void resetInfoBox()
    {
        infoBox.getChildren().clear();
        infoBox.getChildren().add(makeTile("gray","SELECT PIN"));
        infoBox.getChildren().add(makeTile("gray",""));
        infoBox.getChildren().add(makeTile("gray",""));
        infoBox.getChildren().add(makeTile("gray",""));
        infoBox.getChildren().add(makeTile("gray",""));
        infoBox.getChildren().add(makeTile("gray",""));
    }
}
