package com.example.Snake.gameLogic;

import java.util.ArrayList;
import android.os.Handler;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 31.10.13
 * Time: 2:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class Eatable/*extends Thread*/ implements Runnable, ISubject, IGameObject {

    private Direction direction;
    private GameObjectType gameObjectType;
    private Coords coords;

    public Eatable(Coords coords){
        this.coords = coords;
    }

    @Override
    public void run() {
        //gameObjectType = GameObjectType.none;
        notifyObservers(SubjectMessage.destroyed);
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        return;
    }

    @Override
    public Coords getCoords() {
        return coords.clone();
    }

    @Override
    public void setCoords(Coords coords) {
        this.coords = coords.clone();
    }

    @Override
    public void step() {
        return;
    }

    @Override
    public ArrayList<IGameObject> getSubObjectsCollection() {
        return null;
    }

    @Override
    public IGameObject clone() {
        return new Food(coords.clone());
    }

    @Override
    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }

    ArrayList<IObserver> observers;
    @Override
    public void registerObserver(IObserver observer) {
        if (observers == null) observers = new ArrayList<IObserver>();
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        if (observers == null) return;
        if (observers.size() > 0) observers.remove(observer);
    }

    @Override
    public void notifyObservers(SubjectMessage subjectMessage) {
        if (observers == null) return;
        for (IObserver observer : observers) observer.updateObserver(this, subjectMessage);
    }
}
