package com.hau5.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.hau5.game.Sprites.Sprite;

/**
 * Created by Hau5 on 4/10/2016.
 */
public class MyGestureListener implements GestureDetector.GestureListener {

    Sprite player;
    final float swipeLength = 1000.0f;

    public MyGestureListener(Sprite player)
    {
        this.player = player;
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        //Gdx.app.log("tap", "short");
        player.jump();
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        Gdx.app.log("tap", "long");
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        //Gdx.app.log("velocityX", String.valueOf(velocityX));
        //Gdx.app.log("velocityY", String.valueOf(velocityY));

        float deltaX = Math.abs(velocityX);
        float deltaY = Math.abs(velocityY);

        if(deltaX > deltaY)
        {
            if(deltaX  > swipeLength)
            {
                if(velocityX > 0)
                {
                    //Gdx.app.log("Swipe", "Right");
                    if(!player.facingForward)
                    {
                        player.changeDirection();
                    }
                    else
                    {
                        //Gdx.app.log("punch", "forward");
                        player.punch();
                    }
                }
                else
                {
                    //Gdx.app.log("Swipe", "Left");
                    if(player.facingForward)
                    {
                        player.changeDirection();
                    }
                    else
                    {
                        player.punch();
                    }
                }
            }
        }
        else
        {
            if(deltaY > swipeLength)
            {
                if(velocityY > 0)
                {
                    //.app.log("Swipe", "Down");
                    player.slam();
                }
                else
                {
                    //Gdx.app.log("Swipe", "Up");
                    player.dash();
                }
            }
        }
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
