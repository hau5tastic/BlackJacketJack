package com.hau5.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hau5.game.MyGame;
import com.sun.media.jfxmediaimpl.MediaDisposer;

import java.lang.invoke.LambdaConversionException;

/**
 * Created by Hau5 on 3/25/2016.
 */
public class Hud implements Disposable{

    public Stage stage;
    private Viewport viewport;

    private int worldTimer;
    private float timeCount;
    private static int score;

    private Label countDownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;
    private Label winLabel;
    private Label loseLabel;

    public Hud(SpriteBatch sb)
    {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(MyGame.WIDTH, MyGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countDownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("PLAYER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        winLabel = new Label("YOU WIN!", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        loseLabel = new Label("YOU LOSE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(marioLabel).expandX().padTop(10);
        table.add();
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add();
        table.add(countDownLabel).expandX();
        table.row();
        table.add();
        table.add(winLabel).center().padTop(100);
        table.row();
        table.add();
        table.add(loseLabel).center().padTop(100);

        stage.addActor(table);

        winLabel.setVisible(false);
        loseLabel.setVisible(false);
    }

    public void update(float deltaTime)
    {
        timeCount += deltaTime;

        if(timeCount >= 1)
        {
            worldTimer--;
            countDownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static void addScore(int value)
    {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    public void win()
    {
        winLabel.setVisible(true);
    }

    public void lose()
    {
        loseLabel.setVisible(true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
