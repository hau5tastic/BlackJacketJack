package com.hau5.game.Tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hau5.game.MyGame;

/**
 * Created by Hau5 on 4/11/2016.
 */
public class ParallaxBackground {
    private Array<ParallaxLayer> layers;
    private Vector2 speed = new Vector2();

    public ParallaxBackground(Array<ParallaxLayer> layers,Vector2 speed){
        this.layers = layers;
        this.speed.set(speed);
    }

    public void render(SpriteBatch batch, float delta, OrthographicCamera camera)
    {
        for(ParallaxLayer layer : layers){
           //batch.setProjectionMatrix(camera.projection);
            batch.begin();
            float currentX = -camera.position.x * layer.parallaxRatio.x % (layer.region.getRegionWidth() + layer.padding.x);

            if(speed.x < 0)
                currentX += -(layer.region.getRegionWidth() + layer.padding.x);
            do{
                float currentY = - camera.position.y * layer.parallaxRatio.y % (layer.region.getRegionHeight() + layer.padding.y);
                if(speed.y < 0)
                    currentY += - (layer.region.getRegionHeight() + layer.padding.y);
                do{
                    batch.draw(layer.region, -camera.viewportWidth / 2 + currentX + layer.startPosition.x , -camera.viewportHeight / 2 + currentY + layer.startPosition.y);
                    batch.draw(layer.region, -camera.viewportWidth / 2 + currentX + layer.region.getRegionWidth() + layer.startPosition.x + layer.padding.x , -camera.viewportHeight / 2 + currentY + layer.startPosition.y);
                    currentY += (layer.region.getRegionHeight());
                }while(currentY < camera.viewportHeight);
                currentX += (layer.region.getRegionWidth() + layer.padding.x);
            }while(currentX < camera.viewportWidth);
            batch.end();
        }
    }

    public float toWorld(float value)
    {
        return value / MyGame.PIXELSPERMETER;
        /*
        for(ParallaxLayer layer : layers){
           //batch.setProjectionMatrix(camera.projection);
            batch.begin();
            float currentX = -camera.position.x * layer.parallaxRatio.x % (layer.region.getRegionWidth() + layer.padding.x);

            if(speed.x < 0)
                currentX += -(layer.region.getRegionWidth() + layer.padding.x);
            do{
                float currentY = - camera.position.y * layer.parallaxRatio.y % (layer.region.getRegionHeight() + layer.padding.y) ;
                if(speed.y < 0)
                    currentY += - (layer.region.getRegionHeight()+layer.padding.y);
                do{
                    batch.draw(layer.region, -camera.viewportWidth / 2 + currentX + layer.startPosition.x , -camera.viewportHeight / 2 + currentY + layer.startPosition.y);
                    currentY += (layer.region.getRegionHeight() + layer.padding.y);
                }while(currentY < camera.viewportHeight);
                currentX += (layer.region.getRegionWidth() + layer.padding.x);
            }while(currentX < camera.viewportWidth);
            batch.end();
        }
         */
    }
}