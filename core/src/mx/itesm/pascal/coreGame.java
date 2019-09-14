package mx.itesm.pascal;

import com.badlogic.gdx.Game;


public class coreGame extends com.badlogic.gdx.Game {


	//World Dimensions
	public static final float Widht = 1280;
	public static final float Height = 720;
	@Override
	public void create() {
		setScreen(new menuScreen(this));

	}
}