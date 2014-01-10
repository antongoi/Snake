package com.example.Snake.gameLogic;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 01.11.13
 * Time: 5:29
 * To change this template use File | Settings | File Templates.
 */
public class FireHole extends Eatable {
    private Direction direction;
    private GameObjectType gameObjectType;
    //private Coords coords;

    public FireHole(Coords coords){
        super(coords);
        direction = Direction.none;
        gameObjectType = GameObjectType.fire_hole;
    }

    @Override
    public void run() {
        notifyObservers(SubjectMessage.fire_hole_destructed);
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
