package com.hau5.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.hau5.game.Items.Food;
import com.hau5.game.MyGame;
import com.hau5.game.Screens.PlayScreen;
import com.hau5.game.Sprites.Brick;
import com.hau5.game.Sprites.Walker;

/**
 * Created by Hau5 on 3/25/2016.
 */
public class B2WorldCreator {
    // Tiled Layer Indexes
    private final int WALKERLAYER = 7;
    private final int GROUNDLAYER = 6;
    private final int BRICKLAYER = 3;
    private final int COINLAYER = 4;
    private final int OBJECTLAYER = 8;
    private final int DEADLAYER = 9;
    private final int ITEMLAYER = 10;
    private final int ENDLAYER = 12;

    private Array<Walker> walkers;

    public B2WorldCreator(PlayScreen screen, Array<Food> items)
    {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        // ground
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        // boxes
        for(MapObject object : map.getLayers().get(GROUNDLAYER).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MyGame.PIXELSPERMETER, (rect.getY() + rect.getHeight() / 2) / MyGame.PIXELSPERMETER);

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2 / MyGame.PIXELSPERMETER, rect.getHeight() / 2 / MyGame.PIXELSPERMETER);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        // triangles
        for(MapObject object : map.getLayers().get(GROUNDLAYER).getObjects().getByType(PolygonMapObject.class))
        {
            Polygon tri = ((PolygonMapObject) object).getPolygon();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(tri.getX() / MyGame.PIXELSPERMETER, tri.getY() / MyGame.PIXELSPERMETER);

            body = world.createBody(bodyDef);

            float[] vertices = tri.getVertices();
            float[] worldVertices = new float[vertices.length];
            for(int i = 0; i < vertices.length; i++)
            {
                worldVertices[i] = vertices[i] / MyGame.PIXELSPERMETER;
            }

            shape.set(worldVertices);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef).setFriction(0.0f);
        }

        // objects
        for(MapObject object : map.getLayers().get(OBJECTLAYER).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MyGame.PIXELSPERMETER, (rect.getY() + rect.getHeight() / 2) / MyGame.PIXELSPERMETER);

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2 /MyGame.PIXELSPERMETER, rect.getHeight() / 2 / MyGame.PIXELSPERMETER);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = MyGame.OBJECT_BIT;
            body.createFixture(fixtureDef);
        }

        // dead zone
        for(MapObject object : map.getLayers().get(DEADLAYER).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MyGame.PIXELSPERMETER, (rect.getY() + rect.getHeight() / 2) / MyGame.PIXELSPERMETER);

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2 /MyGame.PIXELSPERMETER, rect.getHeight() / 2 / MyGame.PIXELSPERMETER);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = MyGame.DEAD_BIT;
            body.createFixture(fixtureDef);
        }

        // end zone
        for(MapObject object : map.getLayers().get(ENDLAYER).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MyGame.PIXELSPERMETER, (rect.getY() + rect.getHeight() / 2) / MyGame.PIXELSPERMETER);

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2 /MyGame.PIXELSPERMETER, rect.getHeight() / 2 / MyGame.PIXELSPERMETER);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = MyGame.END_BIT;
            body.createFixture(fixtureDef);
        }

        // food
        for(MapObject object : map.getLayers().get(ITEMLAYER).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            items.add(new Food(screen, rect.getX(), rect.getY()));
        }

        // bricks
        for(MapObject object : map.getLayers().get(BRICKLAYER).getObjects().getByType(RectangleMapObject.class))
        {
            new Brick(screen, object);
        }


        // walkers
        walkers = new Array<Walker>();
        for(MapObject object : map.getLayers().get(WALKERLAYER).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            walkers.add(new Walker(screen, rect.getX() / MyGame.PIXELSPERMETER, rect.getY() / MyGame.PIXELSPERMETER));
        }
    }

    public Array<Walker> getWalkers() {
        return walkers;
    }
}
