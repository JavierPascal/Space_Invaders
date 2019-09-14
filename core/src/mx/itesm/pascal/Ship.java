package mx.itesm.pascal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ship {
    private Sprite sprite;

    public Ship(Texture texture, float x, float y){
        sprite = new Sprite(texture);
        sprite.setPosition(x,y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void move(float dx){
        sprite.setX(sprite.getX()+dx);
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
