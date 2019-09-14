package mx.itesm.pascal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy {

    private Sprite sprite;
    private Texture upTexture;
    private Texture downTexture;

    public Enemy(Texture texture, float x, float y){
        sprite = new Sprite(texture);
        sprite.setPosition(x,y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Enemy(Texture upTexture, Texture downTexture, float x, float y){
        this.upTexture=upTexture;
        this.downTexture = downTexture;
        sprite = new Sprite(this.upTexture);
        sprite.setPosition(x,y);
    }
    public void changeImage(){
        if (sprite.getTexture()==upTexture){
            sprite.setTexture(downTexture);
        }else{
            sprite.setTexture(upTexture);
        }
    }

    public void move(float dx, float dy){
        sprite.setPosition(sprite.getX()+dx, sprite.getY()+dy);
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
