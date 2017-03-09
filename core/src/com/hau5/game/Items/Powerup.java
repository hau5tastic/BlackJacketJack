package com.hau5.game.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.hau5.game.MyGame;
import com.hau5.game.Screens.PlayScreen;

/**
 * Created by Hau5 on 3/26/2016.
 */
public class Powerup extends Item {
    private final float SPEED = 0.7f;

    public Powerup(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        // TODO create image sprite
        //setRegion(screen.getAtlas().findRegion("powerup"), 0, 0, MyGame.TILESIZE, MyGame.TILESIZE);
        velocity = new Vector2(SPEED, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MyGame.PIXELSPERMETER);
        fixtureDef.filter.categoryBits = MyGame.ITEM_BIT;
        fixtureDef.filter.maskBits =
            MyGame.SPRITE_BIT |
            MyGame.OBJECT_BIT |
            MyGame.GROUND_BIT |
            MyGame.COIN_BIT |
            MyGame.BRICK_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void useItem(Sprite player) {
        destroy();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
