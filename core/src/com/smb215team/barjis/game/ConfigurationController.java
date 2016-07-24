package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

/**
 * Created by ammar on 7/17/16.
 */
public class ConfigurationController {
    private static final String TAG = ConfigurationController.class.getName();
    
    public static Array<Vector2> boardMap = new Array<Vector2>();

    public static void initCells(){
        boardMap = new Array<Vector2>();
        XmlReader xml = new XmlReader();
        try {
            XmlReader.Element element = xml.parse(Gdx.files.internal("configuration.xml"));

            Array<XmlReader.Element> root = element.getChildByName("BoardMap").getChildrenByName("cell");
            for(XmlReader.Element cell : root) {
                boardMap.add(new Vector2(cell.getFloatAttribute("x"), cell.getFloatAttribute("y")));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Array<Vector2> GetPawnInitialPlaceholder(int playerIndex) {
        Array<Vector2> resultToReturn = new Array<Vector2>();
        XmlReader xml = new XmlReader();
        try {
            XmlReader.Element element = xml.parse(Gdx.files.internal("configuration.xml"));

            XmlReader.Element playerElement = element.getChildByName("PlayersPawnsPlaceholders").getChildrenByName("Player").get(playerIndex);
            for(XmlReader.Element entry : playerElement.getChildrenByName("DeadPawnPlaceholder")) {
                resultToReturn.add(new Vector2(entry.getFloatAttribute("x"), entry.getFloatAttribute("y")));
            }
        }
        catch (Exception e){
            Gdx.app.debug(TAG, e.getMessage());
        }
        return resultToReturn;
    }
}