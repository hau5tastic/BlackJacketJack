package com.hau5.game.Tools;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Hau5 on 4/11/2016.
 */
public class ParallaxLayer {
    public TextureRegion region;
    public Vector2 parallaxRatio;
    public Vector2 startPosition;
    public Vector2 padding;

    public ParallaxLayer(TextureRegion region, Vector2 startPosition, Vector2 parallaxRatio, Vector2 padding)
    {
        this.region = region;
        this.parallaxRatio = parallaxRatio;
        this.startPosition = startPosition;
        this.padding = padding;
    }
}
