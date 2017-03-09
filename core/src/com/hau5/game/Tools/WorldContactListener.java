package com.hau5.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.hau5.game.Items.Food;
import com.hau5.game.Items.Item;
import com.hau5.game.MyGame;
import com.hau5.game.Sprites.Enemy;
import com.hau5.game.Sprites.InteractiveTileObject;
import com.hau5.game.Sprites.Sprite;

/**
 * Created by Hau5 on 3/25/2016.
 */
public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Gdx.app.log("Fixture A", String.valueOf(fixA.getUserData()));
        //Gdx.app.log("Fixture B", String.valueOf(fixB.getUserData()));

        int contactDefinition = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        // hitting bricks
        if(fixA.getUserData() == "head" || fixB.getUserData() == "head")
        {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass()))
            {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        switch (contactDefinition)
        {
            case MyGame.SPRITE_BIT | MyGame.END_BIT:
                //Gdx.app.log("end", "game");
                if(fixA.getFilterData().categoryBits == MyGame.SPRITE_BIT)
                {
                    ((Sprite)fixA.getUserData()).win();
                }
                else
                {
                    ((Sprite)fixB.getUserData()).win();
                }
                break;
            case MyGame.SPRITE_BIT | MyGame.DEAD_BIT:
                //Gdx.app.log("collision", "dead");
                if(fixA.getFilterData().categoryBits == MyGame.SPRITE_BIT)
                {
                    ((Sprite)fixA.getUserData()).die();
                }
                else
                {
                    ((Sprite)fixB.getUserData()).die();
                }
                break;
            case MyGame.SPRITE_BIT | MyGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == MyGame.SPRITE_BIT)
                {
                    if(((Sprite)fixA.getUserData()).punching)
                    {
                        if(((Sprite)fixA.getUserData()).facingForward)
                        {
                            ((Enemy)fixB.getUserData()).hit(1);
                        }
                        else
                        {
                            ((Enemy)fixB.getUserData()).hit(-1);
                        }
                    }
                    else
                    {
                        ((Sprite)fixA.getUserData()).die();
                    }
                }
                else
                {
                    if(((Sprite)fixB.getUserData()).punching)
                    {
                        if(((Sprite)fixB.getUserData()).facingForward)
                        {
                            ((Enemy)fixA.getUserData()).hit(1);
                        }
                        else
                        {
                            ((Enemy)fixA.getUserData()).hit(-1);
                        }
                    }
                    else
                    {
                        ((Sprite)fixB.getUserData()).die();
                    }
                }
                break;
            case MyGame.SPRITE_BIT | MyGame.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == MyGame.SPRITE_BIT)
                {
                    ((Sprite)fixA.getUserData()).grounded = true;
                }
                else
                {
                    ((Sprite)fixB.getUserData()).grounded = true;
                }
                break;
            case MyGame.SPRITE_BIT | MyGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MyGame.SPRITE_BIT)
                {
                    ((Sprite)fixA.getUserData()).wallJump();
                }
                else
                {
                    ((Sprite)fixB.getUserData()).wallJump();
                }
                break;
            // sprite vs enemy head
            case MyGame.ENEMY_HEAD_BIT | MyGame.SPRITE_BIT:
                if(fixA.getFilterData().categoryBits == MyGame.ENEMY_HEAD_BIT)
                {
                    ((Enemy)fixA.getUserData()).hitOnHead();
                }
                else
                {
                    ((Enemy)fixB.getUserData()).hitOnHead();
                }
                break;
            // enemy vs object
            case MyGame.ENEMY_BIT | MyGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MyGame.ENEMY_BIT)
                {
                    ((Enemy)fixA.getUserData()).reverseVelocity();
                }
                else
                {
                    ((Enemy)fixB.getUserData()).reverseVelocity();
                }
                break;
            // enemy vs enemy
            case MyGame.ENEMY_BIT | MyGame.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity();
                ((Enemy)fixB.getUserData()).reverseVelocity();
                break;
            // item vs object
            case MyGame.ITEM_BIT | MyGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MyGame.ITEM_BIT)
                {
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                }
                else
                {
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
            // item vs player
            case MyGame.ITEM_BIT | MyGame.SPRITE_BIT:
                if(fixA.getFilterData().categoryBits == MyGame.ITEM_BIT)
                {
                    ((Food)fixA.getUserData()).destroy();
                }
                else
                {
                    ((Food)fixB.getUserData()).destroy();
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
