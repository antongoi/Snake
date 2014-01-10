package com.example.Snake.gameLogic;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 28.10.13
 * Time: 1:04
 * To change this template use File | Settings | File Templates.
 */
public interface IGameObject {

    public Direction getDirection();
    public void setDirection(Direction direction);
    public Coords getCoords();
    public void setCoords(Coords coords);
    public void step();
    public ArrayList<IGameObject> getSubObjectsCollection();
    public IGameObject clone();
    public GameObjectType getGameObjectType();
}
