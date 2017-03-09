package com.hau5.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.hau5.game.MyGame;
import com.hau5.game.Screens.PlayScreen;

import java.util.Random;

/**
 * Created by Hau5 on 3/25/2016.
 */
public class Walker extends Enemy {

    private final float speed = 0.5f;

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private Texture texture;

    private boolean setToDestroy;
    private boolean destroyed;

    public Walker(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        texture = new Texture("sprites/walkers.png");

        Random r = new Random();
        int type = r.nextInt(4);

        for(int i = 0; i < 3; i++)
        {
            frames.add(new TextureRegion(texture, (type * 3 * 32) + (i * 32), 0, 32, 32));
        }
        walkAnimation = new Animation(0.4f, frames);

        stateTime = 0;

        setBounds(getX(), getY(), 40 / MyGame.PIXELSPERMETER, 40 / MyGame.PIXELSPERMETER);

        setToDestroy = false;
        destroyed = false;
    }

    public void update(float deltaTime)
    {
        stateTime += deltaTime;

        if(setToDestroy && !destroyed)
        {
            world.destroyBody(b2Body);
            destroyed = true;
            setScale(1.0f, 0.5f);
            stateTime = 0;
        }
        else if (!destroyed && b2Body.isActive())
        {
            //Gdx.app.log("walker", String.valueOf(b2Body.getPosition().y));
            if(b2Body.getPosition().y * MyGame.PIXELSPERMETER < MyGame.HEIGHT / MyGame.PIXELSPERMETER)
            {
                //Gdx.app.log("walker", "dead");
                hitOnHead();
            }
            if(facingForward && b2Body.getLinearVelocity().x <= speed)
            {
                b2Body.applyLinearImpulse(new Vector2(0.1f, 0), b2Body.getWorldCenter(), true);
            }
            if(!facingForward && b2Body.getLinearVelocity().x >= -speed)
            {
                b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), b2Body.getWorldCenter(), true);
            }

            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);

            TextureRegion region = walkAnimation.getKeyFrame(stateTime, true);
            setRegion(region);

            if((b2Body.getLinearVelocity().x < 0 || !facingForward) && region.isFlipX())
            {
                region.flip(true, false);
                facingForward = false;
            }
            else if((b2Body.getLinearVelocity().x > 0 || facingForward) && !region.isFlipX())
            {
                region.flip(true, false);
                facingForward = true;
            }
        }
    }

    public void draw(Batch batch)
    {
        if(!destroyed || stateTime < 1)
        {
            super.draw(batch);
        }
    }


    @Override
    protected void defineEnemy() {
        // body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / MyGame.PIXELSPERMETER);

        fixtureDef.filter.maskBits =
            MyGame.GROUND_BIT |
            MyGame.COIN_BIT |
            MyGame.BRICK_BIT |
            MyGame.OBJECT_BIT |
            MyGame.SPRITE_BIT |
            MyGame.ENEMY_BIT;

        fixtureDef.shape = shape;

        fixtureDef.filter.categoryBits = MyGame.ENEMY_BIT;
        b2Body.createFixture(fixtureDef).setUserData(this);

        // head
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-15, 20).scl(1 / MyGame.PIXELSPERMETER);
        vertice[1] = new Vector2(15, 20).scl(1 / MyGame.PIXELSPERMETER);
        vertice[2] = new Vector2(-5, 8).scl(1 / MyGame.PIXELSPERMETER);
        vertice[3] = new Vector2(5, 8).scl(1 / MyGame.PIXELSPERMETER);
        head.set(vertice);

        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = MyGame.ENEMY_HEAD_BIT;
        b2Body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void hitOnHead() {
        //Gdx.app.log("Walker", "Collision");
        setToDestroy = true;
    }

    @Override
    public void hit(int orientation)
    {
        Filter filter = new Filter();
        filter.maskBits = MyGame.NOTHING_BIT;
        for(Fixture fixture : b2Body.getFixtureList())
        {
            fixture.setFilterData(filter);
        }
        b2Body.applyLinearImpulse(new Vector2(orientation * 2f, 4f), b2Body.getWorldCenter(), true);
    }
}
