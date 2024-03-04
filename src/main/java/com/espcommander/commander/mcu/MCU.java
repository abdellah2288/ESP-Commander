package com.espcommander.commander.mcu;

import com.espcommander.commander.utils.MiscUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.util.HashMap;

public abstract class MCU
{
    private HashMap<String,String[]> pinMap;
    private HashMap<String,String> pinInfo;
    private static float secondRowX = 0.0f;
    private static float firstRowX = 0.0f;
    private static float pinGap = 0.0f;
    private static float pinDiameter = 0.0f;
    private static float firstPin_Y = 0.0f;
    /*Board view is vertical only*/
    private ImageView boardView;
    public MCU(String boardImagePath)
    {
        try
        {
            /*Board view is vertical only*/
            Image board_img = MiscUtils.getImage(boardImagePath);
            boardView = new ImageView(board_img);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
