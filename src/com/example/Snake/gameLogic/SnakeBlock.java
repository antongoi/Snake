package com.example.Snake.gameLogic;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 28.10.13
 * Time: 1:04
 * To change this template use File | Settings | File Templates.
 */
public class SnakeBlock implements IGameObject {

    private Direction direction;
    private Coords coords;

    public SnakeBlock(Direction direction, Coords coords)
    {
        this.direction = direction;
        this.coords = coords;
    }

    public Coords getNextBlockCoords(Coords coords) {
        switch (direction){
            case down: coords.setXY(this.coords.getX(), this.coords.getY() + 1); break;
            case left: coords.setXY(this.coords.getX() + 1, this.coords.getY()); break;
            case right: coords.setXY(this.coords.getX() - 1, this.coords.getY()); break;
            case up: coords.setXY(this.coords.getX(), this.coords.getY() - 1); break;
        }
        return coords;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Coords getCoords() {
        return coords.clone();
    }

    @Override
    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    @Override
    public void step() {
        switch (direction){
            case down: coords.decY(); break;
            case left: coords.decX(); break;
            case right: coords.incX(); break;
            case up: coords.incY(); break;
        }
    }

    public void stepTo(Direction direction){
        switch (direction){
            case down: coords.decY(); break;
            case left: coords.decX(); break;
            case right: coords.incX(); break;
            case up: coords.incY(); break;
        }
    }

    public boolean compareStepDirections(SnakeBlock snakeBlock) {
        boolean result = (this.direction == snakeBlock.direction);
        return result;
    }

    @Override
    public ArrayList<IGameObject> getSubObjectsCollection() {
        return  null;
    }

    @Override
    public IGameObject clone() {
        return new SnakeBlock(direction, coords);
    }

    @Override
    public GameObjectType getGameObjectType() {
        return GameObjectType.snake;
    }
}
