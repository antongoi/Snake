package com.example.Snake.gameLogic;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 28.10.13
 * Time: 0:44
 * To change this template use File | Settings | File Templates.
 */
public interface ISubject {
    void registerObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers(SubjectMessage subjectMessage);
}
