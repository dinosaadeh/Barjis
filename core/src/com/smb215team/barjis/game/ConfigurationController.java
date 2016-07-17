package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

/**
 * Created by ammar on 7/17/16.
 */
public class ConfigurationController {

    //
    public static Array<Vector2> boardMap=new Array<Vector2>();

    public static void initCells(){
        boardMap =new Array<Vector2>();
        XmlReader xml = new XmlReader();
        try {
            XmlReader.Element element = xml.parse(Gdx.files.internal("configuration.xml"));

            Array<XmlReader.Element> root = element.getChildrenByName("cell");
            for(XmlReader.Element cell:root){

                boardMap.add(new Vector2(cell.getFloatAttribute("x"),cell.getFloatAttribute("y")));

            }

        }catch (Exception e){

            e.printStackTrace();
        }

    }

}

