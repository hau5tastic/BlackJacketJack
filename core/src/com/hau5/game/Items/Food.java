package com.hau5.game.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.hau5.game.MyGame;
import com.hau5.game.Scenes.Hud;
import com.hau5.game.Screens.PlayScreen;
import com.sun.prism.shader.Texture_LinearGradient_REFLECT_AlphaTest_Loader;

import java.util.Random;

/**
 * Created by hau5t on 4/21/2016.
 */
public class Food extends Sprite {

    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;
    Texture texture;

    public Food(PlayScreen screen, float x, float y)
    {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x / MyGame.PIXELSPERMETER, y / MyGame.PIXELSPERMETER);
        setBounds(getX(), getY(), 32 / MyGame.PIXELSPERMETER, 32 / MyGame.PIXELSPERMETER);
        defineItem();
        toDestroy = false;
        destroyed = false;

        Random r = new Random();
        int type = r.nextInt(10);

        texture = new Texture("sprites/items.png");
        setRegion(new TextureRegion(texture, 0, 0, 20, 30));
        defineItem();
    }

    public void defineItem() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + (getWidth() / 2), getY() + (getHeight() / 2));
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16 / MyGame.PIXELSPERMETER);
        fixtureDef.filter.categoryBits = MyGame.ITEM_BIT;
        fixtureDef.filter.maskBits =
                MyGame.SPRITE_BIT |
                        MyGame.OBJECT_BIT |
                        MyGame.GROUND_BIT |
                        MyGame.COIN_BIT |
                        MyGame.BRICK_BIT;

        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);
    }

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
        Hud.addScore(200);
        toDestroy = true;
    }

}
