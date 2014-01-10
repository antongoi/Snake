package com.example.Snake;

import com.example.Snake.gameLogic.*;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 28.10.13
 * Time: 5:19
 * To change this template use File | Settings | File Templates.
 */
public class DrawableObject extends Coords {

    public static DrawableObject asOtherGameObjectType(DrawableObject drawableObject, GameObjectType gameObjectType){
        return new DrawableObject(drawableObject.clone(), gameObjectType);
    }

    private GameObjectType gameObjectType;

    public int test = 0;

    public DrawableObject(Coords coords, GameObjectType gameObjectType){
        super(coords);
        this.gameObjectType = gameObjectType;
    }

    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }

    @Override
    protected int getMaxX() {
        return super.getMaxX();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected int getMaxY() {
        return super.getMaxY();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return getX() + " " + getY();
    }
}
