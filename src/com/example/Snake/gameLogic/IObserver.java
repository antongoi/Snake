package com.example.Snake.gameLogic;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 28.10.13
 * Time: 0:43
 * To change this template use File | Settings | File Templates.
 */
public interface IObserver {
    void updateObserver(ISubject subject, SubjectMessage subjectMessage);
}
