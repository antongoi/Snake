package com.example.Snake.gameLogic;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Toxin extends Eatable {

    private Direction direction;
    private GameObjectType gameObjectType;
//    private Coords coords;
    Timer timer;

    public Toxin(Coords coords){
        super(coords);
        direction = Direction.none;
        gameObjectType = GameObjectType.toxin;
//        timer = new Timer();
//        Runnable runnable = new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        };
//        Thread thread = new Thread();
//        thread.setDaemon(true);
//        //thread.
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
//        return new Toxin(coords.clone());
//    }

    @Override
    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }
}
