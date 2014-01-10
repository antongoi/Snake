package com.example.Snake.gameLogic;

import com.example.Snake.DrawableObject;

import java.util.*;
import android.os.Handler;

import static com.example.Snake.gameLogic.Direction.*;
import static com.example.Snake.gameLogic.SubjectMessage.*;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 28.10.13
 * Time: 0:59
 * To change this template use File | Settings | File Templates.
 */
public class GameField implements IObserver, ISubject {

    private final int MAX_X;
    private final int MAX_Y;
    Random random;
    private ArrayList<IGameObject> gameObjects;
    private Snake snake;
    private Handler handler;
    boolean gameOver = false;
    private final int _countEatableObjects = 4;

    public GameField(int max_x, int max_y)
    {
        MAX_X = max_x;
        MAX_Y = max_y;
        random = new Random();
        gameObjects = new ArrayList<IGameObject>();
        handler = new Handler();
        addSnake();
        for (int i = 0; i < _countEatableObjects; i++){
//            addFood();
            addSmthEatable();
//            addToxin();
        }
    }

    public void clearAll(){
        for (IGameObject iGameObject : gameObjects)
            if(iGameObject instanceof Runnable) handler.removeCallbacks((Runnable)iGameObject);
        gameObjects.clear();
    }

    private boolean isOppositeDirections(Direction direction1, Direction direction2){
        switch (direction1){
            case up: return direction2 == down;
            case down: return direction2 == up;
            case left: return direction2 == right;
            case right: return direction2 == left;
        }
        return false;
    }

    private void addSnake()
    {
        snake = new Snake(MAX_X, MAX_Y, 5);
        gameObjects.add(snake);
        snake.registerObserver(this);
    }

    private boolean isAddressUsed(Coords coords){
        if(findGameObjectsByCoords(coords).size() > 0) return true;
        return false;
    }

    private int postToHandlerAdditionalTime = 0;
    private void postToHandler(Runnable runnable){
        handler.postDelayed(runnable, 5000 * (random.nextInt(3) + 1));
        //postToHandlerAdditionalTime = (postToHandlerAdditionalTime + 100) % (100 * _countEatableObjects);
    }

    private void addFood(){
        Coords foodCoords = new Coords(random.nextInt(MAX_X), random.nextInt(MAX_Y), MAX_X, MAX_Y);
        while (isAddressUsed(foodCoords)){
            foodCoords = new Coords(random.nextInt(MAX_X), random.nextInt(MAX_Y), MAX_X, MAX_Y);
        }
        final Eatable food = new Food(foodCoords);
//        food.setDaemon(true);
//        food.start();
        gameObjects.add(food);
        food.registerObserver(this);
        postToHandler(food);
    }

    private void addToxin(){
        Coords toxinCoords = new Coords(random.nextInt(MAX_X), random.nextInt(MAX_Y), MAX_X, MAX_Y);
        while (isAddressUsed(toxinCoords)){
            toxinCoords = new Coords(random.nextInt(MAX_X), random.nextInt(MAX_Y), MAX_X, MAX_Y);
        }
        final Eatable toxin = new Toxin(toxinCoords);
//        toxin.setDaemon(true);
//        toxin.start();
        gameObjects.add(toxin);
        toxin.registerObserver(this);
        postToHandler(toxin);
    }
    private void addFireHole(){
        Coords fireHoleCoords = new Coords(random.nextInt(MAX_X), random.nextInt(MAX_Y), MAX_X, MAX_Y);
        while (isAddressUsed(fireHoleCoords)){
            fireHoleCoords = new Coords(random.nextInt(MAX_X), random.nextInt(MAX_Y), MAX_X, MAX_Y);
        }
        final Eatable fireHole = new FireHole(fireHoleCoords);
//        toxin.setDaemon(true);
//        toxin.start();
        gameObjects.add(fireHole);
        fireHole.registerObserver(this);
        postToHandler(fireHole);
        fireHoleOnField = true;
    }

    public void makeStep()
    {
        int iteratorMax = gameObjects.size();
        for(int i = 0; i < iteratorMax; i++){
            gameObjects.get(i).step();
            ArrayList<IGameObject> gameObjectsStepped = findGameObjectsByCoords(gameObjects.get(i).getCoords());
            if(gameObjectsStepped.size() == 2)
                for (int j = 0; j<2; j++){
                    if(gameObjectsStepped.get(j).getGameObjectType() == GameObjectType.food) {
                        snake.incrementSnake();
                        handler.removeCallbacks((Runnable)gameObjectsStepped.get(j));
                        gameObjects.remove(gameObjectsStepped.get(j));
                        gameObjectsStepped.remove(j);
                        iteratorMax--;
                        addSmthEatable();
                        break;
                    }
                    if(gameObjectsStepped.get(j).getGameObjectType() == GameObjectType.toxin) {
                        handler.removeCallbacks((Runnable)gameObjectsStepped.get(j));
                        gameObjects.remove(gameObjectsStepped.get(j));
                        gameObjectsStepped.remove(j);
                        iteratorMax--;
                        addSmthEatable();
                        snake.decrementSnake();
                        break;
                    }
                    if(gameObjectsStepped.get(j).getGameObjectType() == GameObjectType.fire_hole) {
                        handler.removeCallbacks((Runnable)gameObjectsStepped.get(j));
                        //gameObjects.remove(gameObjectsStepped.get(j));
                        gameObjectsStepped.remove(j);
                        iteratorMax--;
                        //addSmthEatable();
                        snake.killSnakeSlowly();
                        break;
                    }
                }
        }
        notifyObservers(model_changed);

    }

    private boolean fireHoleOnField = false;
    private void addSmthEatable(){
        int rnd = random.nextInt(1);
        int rnd_fireHole = random.nextInt(7);
        for(int i = 0; i < rnd + 1; i++)
            switch (random.nextInt(2)){
                case 0: addFood(); break;
                case 1: addToxin(); break;
            }
        if (!fireHoleOnField && snake.getLength() > 10)
            if (rnd_fireHole == 3) addFireHole();
    }

    private ArrayList<IGameObject> findGameObjectsByCoords(Coords coords){
        ArrayList<IGameObject> gameObjectsResult = new ArrayList<IGameObject>();
        for (IGameObject gameObject : this.gameObjects)
            if(gameObject.getCoords().isEqual(coords)) gameObjectsResult.add(gameObject);
        return gameObjectsResult;
    }

    public ArrayList<DrawableObject> getDrawableObjects(){
        ArrayList<DrawableObject> drawableObjects = new ArrayList<DrawableObject>();
        for (IGameObject gameObject : gameObjects)
            if(gameObject.getSubObjectsCollection() == null)
                drawableObjects.add(new DrawableObject(gameObject.getCoords(), gameObject.getGameObjectType()));
            else for (IGameObject gameObject1 : gameObject.getSubObjectsCollection())
                     drawableObjects.add(new DrawableObject(gameObject1.getCoords(), gameObject.getGameObjectType()));
        return drawableObjects;
    }

    public void setSnakeDirection(Direction snakeDirection){
        if(!isOppositeDirections(snake.getDirection(), snakeDirection))
            snake.setDirection(snakeDirection);
    }

    @Override
    public void updateObserver(ISubject subject, SubjectMessage subjectMessage) {
        if (gameOver) return;
//        if(subject instanceof Snake){
            switch (subjectMessage){
                case game_over:
                    gameOver = true;
                    notifyObservers(game_over); break;
//            }
//        }
//        if(subject instanceof Eatable){
//            switch (subjectMessage){
                case destroyed:
                    handler.removeCallbacks(((Runnable)subject));
                    addSmthEatable();
                    gameObjects.remove(subject);
                    break;
                case fire_hole_destructed:
                    handler.removeCallbacks(((Runnable)subject));
                    gameObjects.remove(subject);
                    fireHoleOnField = false;
                    break;
//            }
        }
    }

    private ArrayList<IObserver> observers;
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
    public synchronized void notifyObservers(SubjectMessage subjectMessage) {
        if (observers == null) return;
        for (IObserver observer : observers) observer.updateObserver(this, subjectMessage);
    }
}
