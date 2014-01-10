package com.example.Snake.gameLogic;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 30.10.13
 * Time: 4:37
 * To change this template use File | Settings | File Templates.
 */
public class Food extends Eatable {

    private Direction direction;
    private GameObjectType gameObjectType;
    //private Coords coords;

    public Food(Coords coords){
        super(coords);
        direction = Direction.none;
        gameObjectType = GameObjectType.food;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        return;
    }

//    @Override
//    public Coords getCoords() {
//        return coords.clone();
//    }

//    @Override
//    public void setCoords(Coords coords) {
//        this.coords = coords.clone();
//    }

    @Override
    public void step() {
        return;
    }

    @Override
    public ArrayList<IGameObject> getSubObjectsCollection() {
        return null;
    }

//    @Override
//    public IGameObject clone() {
//        return new Food(coords.clone());
//    }

    @Override
    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }
}
