package com.example.Snake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.example.Snake.gameLogic.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class ActivityGame extends Activity implements IObserver, View.OnTouchListener {

    final int MAX_X = 24; // game field width in blocks
    private int MAX_Y; // game field height in blocks
    private TextView [][] gameFieldView; // game field view
    GameField gameField; // game field model
    private final int _timerStep = 120; // delay before next model changing step (in milliseconds)
    private volatile ArrayList<DrawableObject> drawableObjects;
    private boolean gameOver = false;
    private int blockSide;

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {

        @Override
        public void run() {
//            if (drawableObjects == null) drawableObjects = gameField.getDrawableObjects();
//            for(int i = 0; i < drawableObjects.size(); i++){
//                setUIChanges(DrawableObject.asOtherGameObjectType(drawableObjects.get(i), GameObjectType.none));
//            }
            gameField.makeStep();
//            drawableObjects = gameField.getDrawableObjects();
//            for(int i = 0; i < drawableObjects.size(); i++){
//                setUIChanges(drawableObjects.get(i));
//            }
//            handler.postDelayed(this, _timerStep);
        }
    };

    private Handler uiChangesHandler;
    private void setUIChanges(DrawableObject drawableObject){
        switch (drawableObject.getGameObjectType()){
            case snake:
                gameFieldView[drawableObject.getX()][drawableObject.getY()].
                        setBackgroundDrawable(getResources().getDrawable(R.drawable.snake_block));
                break;
            case food:
                gameFieldView[drawableObject.getX()][drawableObject.getY()].
                        setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case toxin:
                gameFieldView[drawableObject.getX()][drawableObject.getY()].
                        setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case fire_hole:
                gameFieldView[drawableObject.getX()][drawableObject.getY()].
                        setBackgroundDrawable(getResources().getDrawable(R.drawable.fire_hole));
                break;
            case none:
                gameFieldView[drawableObject.getX()][drawableObject.getY()].
                        setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_game);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.game_tablelayout);

        handler.postDelayed(task, _timerStep);

        //-------------------Example---------------------
//        final TableRow tableRow = new TableRow(this);
//        tableRow.setId(15);
//        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
//        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//        final Button bt = new Button(this);
//        bt.setId(10);
//        bt.setLayoutParams(layoutParams);
//        bt.setText("Hello");
//        tableRow.addView(bt);
//        TableRow tr = (TableRow)findViewById(R.id.tr_test);
//        tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        blockSide = dm.widthPixels / MAX_X;
        MAX_Y = (dm.heightPixels / blockSide) - 5;
        TableRow.LayoutParams tableRowLayoutParams =
                new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableLayout.LayoutParams tableLayoutLayoutParams =
                new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tableLayout.setPadding((dm.widthPixels - MAX_X*blockSide) / 2, (dm.heightPixels - (MAX_Y+4)*blockSide) / 2,
                (dm.widthPixels - MAX_X*blockSide) / 2, (dm.heightPixels - (MAX_Y+4)*blockSide) / 2);
        gameFieldView = new TextView[MAX_X][MAX_Y];

        for (int y=MAX_Y-1; y>=0; y--)
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(tableRowLayoutParams);
            for(int x=0; x<MAX_X; x++)
            {
                gameFieldView[x][y] = new TextView(this);
                gameFieldView[x][y].setBackgroundColor(getResources().getColor(R.color.white));
                gameFieldView[x][y].setWidth(blockSide);
                gameFieldView[x][y].setHeight(blockSide);
                gameFieldView[x][y].setMaxWidth(blockSide);
                gameFieldView[x][y].setMaxHeight(blockSide);
                gameFieldView[x][y].setId(x*100+y);
                gameFieldView[x][y].setLayoutParams(tableRowLayoutParams);

                tableRow.addView(gameFieldView[x][y]);
            }
            tableLayout.addView(tableRow, tableLayoutLayoutParams);
        }

        gameField = new GameField(MAX_X, MAX_Y);
        gameField.registerObserver(this);
        tableLayout.setOnTouchListener(this);

    }

    private float xDown = 0, xUp = 0, yDown = 0, yUp = 0;



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                xDown = event.getX(); yDown = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchHandler(xDown, yDown, event.getX(), event.getY(), false);
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                xUp = event.getX(); yUp = event.getY();
                /*finger up -> changing direction*/

                onTouchHandler(xDown, yDown, xUp, yUp, true);
                break;
        }

        return true;
    }

    private void showToast(String s){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        (Toast.makeText(context, s, duration)).show();
    }

    private boolean gamePaused = false;
    private long firstTouchTime = -1;
    private long secondTouchTime = -1;

    private void onTouchHandler(float xDown, float yDown, float xUp, float yUp, boolean canBePaused){
        int horizontal = (int)(xUp - xDown);
        int vertical = (int)(yDown - yUp);
        if (Math.abs(horizontal) - Math.abs(vertical) > 80){ //horizontal move
            if (horizontal > 0){ //right
                gameField.setSnakeDirection(Direction.right);
            }
            else if(horizontal < 0){//left
                gameField.setSnakeDirection(Direction.left);
            }
            firstTouchTime = -1;
            this.xDown = xUp;
            this.yDown = yUp;
        } else if ((Math.abs(horizontal) - Math.abs(vertical) < -80)){
            if (vertical > 0){ //up
                gameField.setSnakeDirection(Direction.up);
            }
            else if (vertical < 0){//down
                gameField.setSnakeDirection(Direction.down);
            }
            firstTouchTime = -1;
            this.xDown = xUp;
            this.yDown = yUp;
        } else {
            //-------- on tap ---------
            if (canBePaused){
                if (firstTouchTime < 0) firstTouchTime = System.currentTimeMillis();
                else {
                    secondTouchTime = System.currentTimeMillis();
                    if (secondTouchTime - firstTouchTime < 500) {
                        if (!gamePaused){
                            handler.removeCallbacks(task);
                            gamePaused = true;
                            showToast("      Paused...     \nDouble tap to unpause.");
                        } else{
                            handler.postDelayed(task, _timerStep);
                            gamePaused = false;
                        }
                    }
                    firstTouchTime = -1;
                }
            }
        }
    }

    @Override
    public synchronized void updateObserver(ISubject subject, SubjectMessage subjectMessage) {
        if (gameOver) return;
        switch (subjectMessage){
            case game_over:
                gameOver = true;
                handler.removeCallbacks(task);
                //gameField.clearAll();
//                showToast("Game Over!");

                Intent intent = new Intent(ActivityGame.this, GameOver.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);

                this.finish();
                break;
            case model_changed:
                /* removing all old game objects from view */
                if (drawableObjects == null) drawableObjects = gameField.getDrawableObjects();
                for(int i = 0; i < drawableObjects.size(); i++){
                    setUIChanges(DrawableObject.asOtherGameObjectType(drawableObjects.get(i), GameObjectType.none));
                }
                /* displaying new game objects on view */
                drawableObjects = gameField.getDrawableObjects();
                for(int i = 0; i < drawableObjects.size(); i++){
                    setUIChanges(drawableObjects.get(i));
                }
                handler.postDelayed(task, _timerStep);
                break;
        }
    }
}