package com.nico.Screens;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.nico.Screens.ScreenObjects.MenuBorder;

public class FrontPage extends Menu {


    public FrontPage(Alpha alpha, OrthographicCamera camera, SpriteBatch batch, MenuBorder border, TextureAtlas atlas, float width, float height) {
        super(alpha, camera, batch, border, atlas, width, height);
    }
}
