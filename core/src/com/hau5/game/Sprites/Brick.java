package com.hau5.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.hau5.game.MyGame;
import com.hau5.game.Scenes.Hud;
import com.hau5.game.Screens.PlayScreen;

/**
 * Created by Hau5 on 3/25/2016.
 */
public class Brick extends InteractiveTileObject{

    public Brick (PlayScreen screen, MapObject object)
    {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MyGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(MyGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        //MyGame.assetManager.get("audio/sounds/hit.wav", Sound.class).play();
    }
}
