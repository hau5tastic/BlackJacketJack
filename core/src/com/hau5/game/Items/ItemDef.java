package com.hau5.game.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Hau5 on 3/26/2016.
 */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
