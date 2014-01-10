package com.example.Snake.gameLogic;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 28.10.13
 * Time: 3:19
 * To change this template use File | Settings | File Templates.
 */
public class Snake implements IGameObject, ISubject {

    private final ArrayList<SnakeBlock> snakeBlocks;
    private int maxX;
    private int maxY;
    private boolean directionLocked = false;

    private Snake(ArrayList<SnakeBlock> snakeBlocks, int maxX, int maxY){
        this.snakeBlocks = snakeBlocks;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public Snake(int max_x, int max_y, int size)
    {
        snakeBlocks = new ArrayList<SnakeBlock>();
        maxX = max_x;
        maxY = max_y;
        if(size < 1) size = 1;
        snakeBlocks.add(new SnakeBlock(Direction.up, new Coords(max_x / 2, max_y / 2, maxX, maxY)));
        for (int i=1; i<size; i++) incrementSnake();
    }

    public int getLength() {
        return snakeBlocks.size();
    }

    public void incrementSnake()
    {
        Direction lastDirection = snakeBlocks.get(snakeBlocks.size()-1).getDirection();
        SnakeBlock lastSnakeBlock = snakeBlocks.get(snakeBlocks.size()-1);
        Coords lastCoords = new Coords(lastSnakeBlock.getCoords().getX(), lastSnakeBlock.getCoords().getY(), maxX, maxY);
        lastCoords = snakeBlocks.get(snakeBlocks.size()-1).getNextBlockCoords(lastCoords);
        snakeBlocks.add(new SnakeBlock(lastDirection, lastCoords));
    }

    public void decrementSnake()
    {
        if (slowKilling){
            snakeBlocks.remove(snakeBlocks.size()-1);
            if (snakeBlocks.size() < 2) killSnake();
            directionLocked = true;
            stepBack();
        } else {
            snakeBlocks.remove(snakeBlocks.size()-1);
            if (snakeBlocks.size() < 2) killSnake();
        }
    }

    public void killSnake()
    {
        notifyObservers(SubjectMessage.game_over);
        //snakeBlocks.clear();
    }

    private boolean slowKilling = false;
    public void killSnakeSlowly(){
        slowKilling = true;
        decrementSnake();
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

    public ArrayList<IGameObject> getSubObjectsCollection(){
        ArrayList<IGameObject> gameObjects = new ArrayList<IGameObject>();
        for (IGameObject gameObject : snakeBlocks) gameObjects.add(gameObject.clone());
        return gameObjects;
    }

    @Override
    public Direction getDirection() {
        if (snakeBlocks.size() > 0) return snakeBlocks.get(0).getDirection();
        else return Direction.none;
    }

    @Override
    public void setDirection(Direction direction) {
        if(snakeBlocks.size()>0){
            while(directionLocked){}
            snakeBlocks.get(0).setDirection(direction);
        }
    }

    @Override
    public Coords getCoords() {
        if (snakeBlocks.size() > 0) return snakeBlocks.get(0).getCoords().clone();
        else return null;
    }

    @Override
    public void setCoords(Coords coords) {
        if(snakeBlocks.size()>0)
            snakeBlocks.get(0).setCoords(coords);
    }


    @Override
    public void step() {
        directionLocked = true;
        Direction[] directions = new Direction[snakeBlocks.size()];
        if (snakeBlocks.size() == 0) return;
        for (int i = 0; i < snakeBlocks.size(); i++){
            snakeBlocks.get(i).step();
            directions[i] = snakeBlocks.get(i).getDirection();
        }
        snakeBlocks.get(0).setDirection(directions[0]);
        for (int i = 1; i < snakeBlocks.size(); i++){
            snakeBlocks.get(i).setDirection(directions[i - 1]);
        }
        directionLocked = false;
    }

    private Direction getOppositeDirection(Direction direction){
        switch (direction){
            case up: return Direction.down;
            case down: return Direction.up;
            case left: return Direction.right;
            case right: return Direction.left;
        }
        return Direction.none;
    }

    private void stepBack() {
        directionLocked = false;
        Direction[] directions = new Direction[snakeBlocks.size()];
        if (snakeBlocks.size() == 0) return;
        for (int i = snakeBlocks.size() - 1; i >= 0; i--){
            snakeBlocks.get(i).setDirection(getOppositeDirection(snakeBlocks.get(i).getDirection()));
            snakeBlocks.get(i).step();
            snakeBlocks.get(i).setDirection(getOppositeDirection(snakeBlocks.get(i).getDirection()));
            directions[i] = snakeBlocks.get(i).getDirection();
        }
        snakeBlocks.get(0).setDirection(directions[0]);
        for (int i = 1; i < snakeBlocks.size(); i++){
            snakeBlocks.get(i).setDirection(directions[i - 1]);
        }
    }

    @Override
    public IGameObject clone() {
        return new Snake(snakeBlocks, maxX, maxY);
    }

    @Override
    public GameObjectType getGameObjectType() {
        return GameObjectType.snake;
    }
}
