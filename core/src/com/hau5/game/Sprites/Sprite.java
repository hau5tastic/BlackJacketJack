package com.hau5.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.hau5.game.MyGame;
import com.hau5.game.Screens.PlayScreen;
import com.sun.javafx.geom.Edge;

import javax.xml.soap.Text;

/**
 * Created by Hau5 on 3/25/2016.
 */
public class Sprite extends com.badlogic.gdx.graphics.g2d.Sprite{

    private final float jumpMagnitude = 5.0f;
    private final float speed = 3.0f;
    private final float dashDistance = 3.0f;

    public enum State {RUNNING, JUMPING, DJUMPING, SLAMMING, PUNCHING, LANDING, DYING, DASHING, WINNING};
    public State currentState;
    public State previousState;

    public boolean facingForward;
    public boolean punching;
    public boolean doubleJumping;
    public boolean grounded;
    public boolean dashing;
    public boolean slamming;
    public boolean dying;
    public boolean wallJumping;
    public boolean win;
    private int dashCount;

    // sprite sheet & animations
    private Animation runAnim;
    private Animation jumpAnim;
    private Animation landAnim;
    private Animation flyingAnim;
    private Animation punchAnim;
    private Animation doubleJumpAnim;
    private Animation dashAnim;
    private Animation slamAnim;
    private Animation dyingAnim;
    private Animation powerRunAnim;
    private Animation powerPunchAnim;
    private Animation superPunchAnim;
    private Animation superKickAnim;
    private float stateTimer;
    private float spriteRotation;

    private PlayScreen screen;

    public World world;
    public Body b2Body;

    public int power;
    public float powerDuration;

    public Sprite(PlayScreen screen)
    {
        super(screen.getAtlas().findRegion("run"));

        this.screen = screen;

        this.world = screen.getWorld();

        currentState = State.RUNNING;
        previousState = State.RUNNING;
        stateTimer = 0;
        facingForward = true;
        punching = false;
        doubleJumping = false;
        dashing = false;
        dashCount = 0;
        slamming = false;
        dying = false;
        wallJumping = false;
        win = false;
        spriteRotation = 0.0f;

        defineTextureRegions(screen);
        definePlayer();
    }

    public void definePlayer()
    {
        // body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MyGame.PIXELSPERMETER, 200 / MyGame.PIXELSPERMETER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(20 / MyGame.PIXELSPERMETER);
        fixtureDef.filter.categoryBits = MyGame.SPRITE_BIT;
        fixtureDef.filter.maskBits =
            MyGame.GROUND_BIT |
            MyGame.COIN_BIT |
            MyGame.BRICK_BIT |
            MyGame.ENEMY_BIT |
            MyGame.OBJECT_BIT |
            MyGame.ENEMY_HEAD_BIT |
            MyGame.DEAD_BIT |
            MyGame.END_BIT |
            MyGame.ITEM_BIT;

        fixtureDef.shape = shape;
        b2Body.createFixture(fixtureDef).setUserData(this);

        // head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MyGame.PIXELSPERMETER, 7 / MyGame.PIXELSPERMETER), new Vector2(2 / MyGame.PIXELSPERMETER, 7 / MyGame.PIXELSPERMETER));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true; // trigger

        b2Body.createFixture(fixtureDef).setUserData("head");

    }

    public void draw(SpriteBatch batch)
    {
        if(doubleJumping)
        {
            rotate(spriteRotation);
        }
        else
        {
            setRotation(0.0f);
        }
        //effect.draw(batch, 0.16f);
        super.draw(batch);
    }

    public void update(float deltaTime)
    {
        setRegion(getFrame(deltaTime));
        setPosition(b2Body.getPosition().x - (getWidth() / 2), b2Body.getPosition().y - (getHeight() / 2));
        //effect.setPosition(b2Body.getPosition().x, b2Body.getPosition().y);
        // movement
        if(!win && !dying)
        {
            if(slamming)
            {
                if(grounded)
                {
                    slamming = false;
                }
                else
                {
                    b2Body.applyLinearImpulse(new Vector2(0.0f, -1.5f), b2Body.getWorldCenter(), true);
                }
            }
            else if(facingForward && b2Body.getLinearVelocity().x <= speed)
            {
                b2Body.applyLinearImpulse(new Vector2(0.1f, 0), b2Body.getWorldCenter(), true);
            }
            else if(!facingForward && b2Body.getLinearVelocity().x >= -speed)
            {
                b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), b2Body.getWorldCenter(), true);
            }
        }
    }

    private TextureRegion getFrame(float deltaTime) {
        currentState = getState();
        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;

        TextureRegion region;
        switch(currentState)
        {
            case JUMPING:
                region = jumpAnim.getKeyFrame(stateTimer);
                break;
            case DJUMPING:
                region = doubleJumpAnim.getKeyFrame(stateTimer);
                if(doubleJumpAnim.isAnimationFinished(stateTimer))
                {
                    doubleJumping = false;
                    spriteRotation = 0.0f;
                }
                break;
            case LANDING:
                region = landAnim.getKeyFrame(stateTimer);
                break;
            case SLAMMING:
                region = slamAnim.getKeyFrame(stateTimer);
                if(slamAnim.isAnimationFinished(stateTimer))
                {
                    slamming = false;
                }
                break;
            case PUNCHING:
                region = punchAnim.getKeyFrame(stateTimer);
                if(punchAnim.isAnimationFinished(stateTimer))
                {
                    punching = false;
                }
                break;
            case DASHING:
                region = dashAnim.getKeyFrame(stateTimer);
                if(dashAnim.isAnimationFinished(stateTimer))
                {
                    dashCount++;
                    stateTimer = 0;
                    if(dashCount >= 5)
                    {
                        dashing = false;
                    }
                }
                break;
            case WINNING:
                region = flyingAnim.getKeyFrame(stateTimer, true);
            case RUNNING:
            default:
                region = runAnim.getKeyFrame(stateTimer, true);
                break;
        }

        if((b2Body.getLinearVelocity().x < 0 || !facingForward) && !region.isFlipX())
        {
            region.flip(true, false);
            facingForward = false;
        }
        else if((b2Body.getLinearVelocity().x > 0 || facingForward) && region.isFlipX())
        {
            region.flip(true, false);
            facingForward = true;
        }

        setBounds(region.getRegionX(), region.getRegionY(), region.getRegionWidth() / MyGame.PIXELSPERMETER, region.getRegionHeight() / MyGame.PIXELSPERMETER);
        setOrigin(region.getRegionWidth() / 2 / MyGame.PIXELSPERMETER, region.getRegionHeight() / 2 / MyGame.PIXELSPERMETER);
        previousState = currentState;
        return region;
    }


    public State getState()
    {
        if(win)
        {
            return State.WINNING;
        }
        else if(dying)
        {
            return State.DYING;
        }
        else if(punching)
        {
            return State.PUNCHING;
        }
        else if(dashing)
        {
            return State.DASHING;
        }
        else if(grounded)
        {
            doubleJumping = false;
            wallJumping = false;
            return State.RUNNING;
        }
        else if(slamming)
        {
            doubleJumping = false;
            return State.SLAMMING;
        }
        else if(doubleJumping)
        {
            rotateSprite(0.6f);
            return State.DJUMPING;
        }
        else if(b2Body.getLinearVelocity().y > 0)
        {
            return State.JUMPING;
        }
        else if(b2Body.getLinearVelocity().y < 0)
        {
            return State.LANDING;
        }
        else
        {
            return currentState;
        }
    }

    public void jump()
    {
        if(currentState == State.JUMPING || currentState == State.LANDING)
        {

            if(currentState == State.JUMPING)
            {
                b2Body.applyLinearImpulse(new Vector2(0, jumpMagnitude / 2), b2Body.getWorldCenter(), true);
            }
            else if(currentState == State.LANDING || wallJumping)
            {
                b2Body.applyLinearImpulse(new Vector2(0, jumpMagnitude), b2Body.getWorldCenter(), true);
            }
            doubleJumping = true;
        }
        else if(currentState != State.DJUMPING)
        {
            b2Body.applyLinearImpulse(new Vector2(0, jumpMagnitude), b2Body.getWorldCenter(), true);
        }
        grounded = false;
    }

    public void changeDirection()
    {
        facingForward = !facingForward;

        // prevent delay on direction change
        Vector2 velocity = b2Body.getLinearVelocity();
        velocity.x = -velocity.x;
        b2Body.setLinearVelocity(velocity);
    }

    public void wallJump()
    {
        Vector2 velocity = b2Body.getLinearVelocity();
        velocity.x = -velocity.x;
        velocity.y += 1.0f;
        b2Body.setLinearVelocity(velocity);
        wallJumping = true;
    }

    public void punch()
    {
        punching = true;
    }

    public void slam()
    {
        if(!grounded)
        {
            slamming = true;
        }
    }

    public void die()
    {
        dying = true;
        b2Body.setLinearVelocity(0, 0);
        Filter filter = new Filter();
        filter.maskBits = MyGame.NOTHING_BIT;
        for(Fixture fixture : b2Body.getFixtureList())
        {
            fixture.setFilterData(filter);
        }
        screen.getHud().lose();
    }

    public void win()
    {
        win = true;
        b2Body.setLinearVelocity(0, 0);
        screen.getHud().win();
    }


    public void dash()
    {
        doubleJumping = false;
        if(facingForward)
        {
            b2Body.applyLinearImpulse(new Vector2(dashDistance, 0), b2Body.getWorldCenter(), true);
        }
        else
        {
            b2Body.applyLinearImpulse(new Vector2(-dashDistance, 0), b2Body.getWorldCenter(), true);
        }
        dashing = true;
    }

    public void rotateSprite(float magnitude)
    {
        float abs = Math.abs(spriteRotation);

        if(abs <= 360)
        {
            if(facingForward)
            {
                spriteRotation -= magnitude;
            }
            else
            {
                spriteRotation += magnitude;
            }

        }
        else
        {
            spriteRotation = 0.0f;
        }
    }

    public void hit()
    {
        int orientation;
        if(facingForward)
        {
            orientation = -1;
        }
        else
        {
            orientation = 1;
        }
        Filter filter = new Filter();
        filter.maskBits = MyGame.NOTHING_BIT;
        for(Fixture fixture : b2Body.getFixtureList())
        {
            fixture.setFilterData(filter);
        }
        b2Body.applyLinearImpulse(new Vector2(orientation * 2f, 4f), b2Body.getWorldCenter(), true);
    }

    public void defineTextureRegions(PlayScreen screen)
    {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        setBounds(412, 141, 47 / MyGame.PIXELSPERMETER, 44 / MyGame.PIXELSPERMETER);
        setRegion(new TextureRegion(screen.getAtlas().findRegion("run"), 412, 141, 47, 44));

        // run animation
        frames.add(new TextureRegion(getTexture(), 1, 30, 47, 44));
        frames.add(new TextureRegion(getTexture(), 1 + 47, 30, 46, 42));
        frames.add(new TextureRegion(getTexture(), 1 + 93, 30, 46, 41));
        frames.add(new TextureRegion(getTexture(), 1 + 139, 30, 48, 44));
        frames.add(new TextureRegion(getTexture(), 1 + 187, 30, 47, 42));
        frames.add(new TextureRegion(getTexture(), 1 + 234, 30, 46, 41));
        runAnim = new Animation(0.1f, frames);
        frames.clear();

        // jump animation
        frames.add(new TextureRegion(getTexture(), 324, 77, 35, 36));
        frames.add(new TextureRegion(getTexture(), 324 + 35, 77, 29, 47));
        jumpAnim = new Animation(0.15f, frames);
        frames.clear();

        // land/slam animation
        frames.add(new TextureRegion(getTexture(), 324 + 64, 77, 34, 49));
        frames.add(new TextureRegion(getTexture(), 324 + 98, 77, 34, 39));
        landAnim = new Animation(0.4f, frames);
        slamAnim = new Animation(0.5f, frames);
        frames.clear();

        // punch animation
        frames.add(new TextureRegion(getTexture(), 852, 210, 37, 45));
        frames.add(new TextureRegion(getTexture(), 852 + 37, 210, 38, 43));
        frames.add(new TextureRegion(getTexture(), 852 + 75, 210, 55, 40));
        frames.add(new TextureRegion(getTexture(), 852 + 130, 210, 36, 41));
        punchAnim = new Animation(0.1f, frames);
        frames.clear();

        // doublejump animation
        frames.add(new TextureRegion(getTexture(), 372, 166, 28, 41));
        doubleJumpAnim = new Animation(1.0f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 147, 1, 52, 27));
        frames.add(new TextureRegion(getTexture(), 147 + 52, 1, 51, 27));
        flyingAnim = new Animation(0.1f, frames);
        frames.clear();

        // dash animation
        frames.add(new TextureRegion(getTexture(), 324, 46, 51, 29));
        frames.add(new TextureRegion(getTexture(), 324 + 51, 46, 51, 22));
        dashAnim = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 412, 84, 29, 45));
        frames.add(new TextureRegion(getTexture(), 412 + 29, 84, 36, 43));
        frames.add(new TextureRegion(getTexture(), 412 + 65, 84, 50, 26));
        frames.add(new TextureRegion(getTexture(), 412 + 115, 84, 49, 28));
        dyingAnim = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 1, 193, 74, 46));
        frames.add(new TextureRegion(getTexture(), 1 + 74, 193, 82, 45));
        frames.add(new TextureRegion(getTexture(), 1 + 156, 193, 73, 41));
        frames.add(new TextureRegion(getTexture(), 1 + 229, 193, 82, 44));
        frames.add(new TextureRegion(getTexture(), 1 + 311, 193, 74, 43));
        frames.add(new TextureRegion(getTexture(), 1 + 385, 193, 81, 43));
        powerRunAnim = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 1, 40, 67, 50));
        frames.add(new TextureRegion(getTexture(), 1 + 67, 40, 74, 47));
        frames.add(new TextureRegion(getTexture(), 1 + 141, 40, 88, 44));
        frames.add(new TextureRegion(getTexture(), 1 + 229, 40, 49, 43));
        frames.add(new TextureRegion(getTexture(), 1 + 278, 40, 43, 41));
        powerPunchAnim = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 529, 187, 94, 51));
        frames.add(new TextureRegion(getTexture(), 529 + 94, 187, 94, 52));
        frames.add(new TextureRegion(getTexture(), 529 + 188, 187, 95, 51));
        frames.add(new TextureRegion(getTexture(), 529 + 283, 187, 98, 51));
        superPunchAnim = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 1, 102, 97, 79));
        frames.add(new TextureRegion(getTexture(), 1 + 97, 102, 82, 71));
        frames.add(new TextureRegion(getTexture(), 1 + 179, 102, 101, 78));
        frames.add(new TextureRegion(getTexture(), 1 + 280, 102, 89, 75));
        superKickAnim = new Animation(0.1f, frames);
        frames.clear();
    }
}
