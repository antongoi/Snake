package com.example.Snake.gameLogic;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 28.10.13
 * Time: 3:44
 * To change this template use File | Settings | File Templates.
 */
public class Coords {
    private int x;
    private int y;

    private int maxX;
    private int maxY;
    private boolean safeMode = false;

    public Coords(int x, int y, int max_x, int max_y)
    {
        this.maxX = max_x;
        this.maxY = max_y;
        safeMode = true;
        setXY(x, y);
    }

    public Coords(int x, int y)
    {
        setXY(x, y);
    }

    public Coords()
    {
        this(0, 0);
    }

    protected Coords(Coords coords){
        if(coords.safeMode){
            this.maxX = coords.getMaxX();
            this.maxY = coords.getMaxY();
            safeMode = true;
        }
        setXY(coords.getX(), coords.getY());
    }

    public Coords clone(){
        if (safeMode) return new Coords(x, y, maxX, maxY);
        else return new Coords(x, y);
    }

    public void setXY(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x < 0 ? maxX - (Math.abs(x) % maxX) : x % maxX; //x;
    }

    public void setX(int x) {
        if (safeMode) this.x = x % maxX; //this.x = x < 0 ? maxX - (x % maxX) : x % maxX;
        else this.x = x;
    }

    public int getY() {
        return y < 0 ? maxY - (Math.abs(y) % maxY) : y % maxY; //y;
    }

    public void setY(int y) {
        if (safeMode) this.y = y % maxY; //this.y = y < 0 ? maxY - (y % maxY) : y % maxY;
        else this.y = y;
    }

    public void incX(){
        setX(x + 1);
    }

    public void decX(){
        setX(x - 1);
    }

    public void incY(){
        setY(y + 1);
    }

    public void decY(){
        setY(y - 1);
    }

    protected int getMaxX() {
        return maxX;
    }

    protected int getMaxY() {
        return maxY;
    }

    public boolean isEqual(Coords coords) {
        return (getX() == coords.getX())&&(getY() == coords.getY());
    }
}
