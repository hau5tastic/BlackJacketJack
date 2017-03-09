package com.hau5.game.Sprites;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.hau5.game.Screens.PlayScreen;

import java.util.Vector;

/**
 * Created by Hau5 on 3/25/2016.
 */
public abstract class Enemy extends com.badlogic.gdx.graphics.g2d.Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2Body;
    protected boolean facingForward;

    public Enemy(PlayScreen screen, float x, float y)
    {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        b2Body.setActive(true); // put body to sleep
    }

    public void reverseVelocity()
    {
        facingForward = !facingForward;
        Vector2 velocity = b2Body.getLinearVelocity();
        velocity.x = -velocity.x;
        b2Body.setLinearVelocity(velocity);
    }

    public abstract void update(float deltaTime);

    protected abstract void defineEnemy();

    public abstract void hitOnHead();

    public abstract void hit(int orientation);
}
