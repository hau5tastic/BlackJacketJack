package com.hau5.game.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.hau5.game.MyGame;
import com.hau5.game.Screens.PlayScreen;

/**
 * Created by Hau5 on 3/26/2016.
 */
public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y)
    {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), MyGame.TILESIZE / MyGame.PIXELSPERMETER, MyGame.TILESIZE / MyGame.PIXELSPERMETER);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();
    public abstract void useItem(Sprite player);

    public void update(float deltaTime)
    {
        if(toDestroy && !destroyed)
        {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void Draw(Batch batch)
    {
        if(!destroyed)
        {
            super.draw(batch);
        }
    }

    public void destroy()
    {
        toDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y)
    {
        if(x)
        {
            velocity.x = -velocity.x;
        }
        if(y)
        {
            velocity.y = -velocity.y;
        }
    }
}
