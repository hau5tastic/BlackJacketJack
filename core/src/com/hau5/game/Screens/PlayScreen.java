package com.hau5.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hau5.game.Items.Food;
import com.hau5.game.Items.Item;
import com.hau5.game.Items.ItemDef;
import com.hau5.game.Items.Powerup;
import com.hau5.game.MyGame;
import com.hau5.game.Scenes.Hud;
import com.hau5.game.Sprites.Enemy;
import com.hau5.game.Sprites.Sprite;
import com.hau5.game.Sprites.Walker;
import com.hau5.game.Tools.B2WorldCreator;
import com.hau5.game.Tools.MyGestureListener;
import com.hau5.game.Tools.ParallaxBackground;
import com.hau5.game.Tools.ParallaxLayer;
import com.hau5.game.Tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Hau5 on 3/25/2016.
 */
public class PlayScreen implements Screen {

    // libgdx
    private MyGame game;
    private OrthographicCamera gameCam;
    private float camBaseLine;
    private final float camYOffset = 2.0f;
    private Viewport gameViewPort;
    private Hud hud;

    // tiled
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // textures
    private TextureAtlas atlas;
    Texture texture;
    private Array<ParallaxLayer> bgLayers;
    private ParallaxBackground parallaxBackground;

    // box2d
    private World world;
    private final Vector2 gravity = new Vector2(0, -10);
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    // input
    MyGestureListener myGestureListener;

    // player
    private Sprite player;

    // audio
    private AssetManager assetManager;
    private Music music;

    // items
    private Array<Food> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public PlayScreen(MyGame game)
    {
        this.game = game;

        // ui & camera
        gameCam = new OrthographicCamera();
        //gameCam.zoom = 2.0f;
        gameViewPort = new FitViewport(MyGame.WIDTH / MyGame.PIXELSPERMETER, MyGame.HEIGHT / MyGame.PIXELSPERMETER, gameCam);
        hud = new Hud(game.spriteBatch);

        // map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("levels/level.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PIXELSPERMETER);

        gameCam.position.set(gameViewPort.getWorldWidth() / 2, gameViewPort.getWorldHeight() / 2, 0);

        // background
        bgLayers = new Array<ParallaxLayer>();
        texture = new Texture("background/background.png");
        bgLayers.add(new ParallaxLayer(new TextureRegion(texture, texture.getWidth(), texture.getHeight()), new Vector2(0, 0), new Vector2(20, 0), new Vector2(0, 0)));
        texture = new Texture("background/moon.png");
        bgLayers.add(new ParallaxLayer(new TextureRegion(texture, texture.getWidth(), texture.getHeight()), new Vector2(MyGame.WIDTH - texture.getWidth(), MyGame.HEIGHT - texture.getHeight()), new Vector2(15, 0), new Vector2(MyGame.WIDTH, 0)));
        texture = new Texture("background/clouds.png");
        bgLayers.add(new ParallaxLayer(new TextureRegion(texture, texture.getWidth(), texture.getHeight()), new Vector2(0, MyGame.HEIGHT - texture.getHeight()), new Vector2(40, 0), new Vector2(0, 1000)));

        parallaxBackground = new ParallaxBackground(bgLayers, new Vector2(5, 0));

        // world
        world = new World(gravity, true);
        b2dr = new Box2DDebugRenderer();
        b2dr.SHAPE_STATIC.set(1, 0, 0, 1);

        items = new Array<Food>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        creator = new B2WorldCreator(this, items);

        //Gdx.app.log("items", String.valueOf(items.size));

        // player
        atlas = new TextureAtlas("sprites/BlackJacketJack.pack");
        player = new Sprite(this);
        camBaseLine = player.b2Body.getPosition().y;


        // input
        myGestureListener = new MyGestureListener(player);
        Gdx.input.setInputProcessor(new GestureDetector(myGestureListener));

        world.setContactListener(new WorldContactListener());

        assetManager = new AssetManager();
        assetManager.load("audio/music/song.mp3", Music.class);
        assetManager.load("audio/sounds/coin.wav", Sound.class);
        assetManager.load("audio/sounds/hit.wav", Sound.class);
        assetManager.load("audio/sounds/jump.wav", Sound.class);
        assetManager.load("audio/sounds/powerup.wav", Sound.class);
        assetManager.finishLoading();

        music = assetManager.get("audio/music/song.mp3", Music.class);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void show() {

    }

    public void update(float deltaTime)
    {

        world.step(1 / 60f, 6, 2);

        for(Enemy enemy : creator.getWalkers())
        {
            enemy.update(deltaTime);
            if(enemy.getX() < player.getX())// + MyGame.WIDTH / 2); // enemy activator distance
            {
                enemy.b2Body.setActive(true);
            }
        }

        for(Food item : items)
        {
            item.update(deltaTime);
        }

        player.update(deltaTime);
        hud.update(deltaTime);

        gameCam.position.x = player.b2Body.getPosition().x;
        /*if(player.b2Body.getPosition().y <= camBaseLine + camYOffset)
        {
            gameCam.position.y = player.b2Body.getPosition().y;
        }*/

        gameCam.update();
        renderer.setView(gameCam.combined, 0, 0, MyGame.WIDTH, MyGame.HEIGHT);
    }

    @Override
    public void render(float deltaTime) {
        //update logic
        update(deltaTime);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render background
        parallaxBackground.render(game.spriteBatch, deltaTime, gameCam);

        // render map
        renderer.render();

        game.spriteBatch.setProjectionMatrix(gameCam.combined);
        game.spriteBatch.begin();

        player.draw(game.spriteBatch);
        //draw here
        for(Enemy enemy : creator.getWalkers())
        {
            enemy.draw(game.spriteBatch);
        }
        for(Food item : items)
        {
            item.draw(game.spriteBatch);
        }
        game.spriteBatch.end();

        // debug draw
        //b2dr.render(world, gameCam.combined);

        game.spriteBatch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameViewPort.update(width, height);
    }

    public float toWorld(float value)
    {
        return value / MyGame.PIXELSPERMETER;
    }

    public TiledMap getMap()
    {
        return map;
    }

    public World getWorld()
    {
        return world;
    }

    public AssetManager getAssetManager() { return assetManager; }

    public Hud getHud() { return  hud; }

    public TextureAtlas getAtlas(){ return atlas; }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        assetManager.dispose();
    }
}
