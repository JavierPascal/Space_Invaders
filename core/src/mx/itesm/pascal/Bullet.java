package mx.itesm.pascal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {

    private Sprite sprite;
    private float speed = 300; //px * sec

    public Bullet(Texture texture, float x, float y){
        sprite = new Sprite(texture);
        sprite.setPosition(x,y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void move(float delta){
        float distance = speed*delta;
        sprite.setY(sprite.getY()+distance);
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
