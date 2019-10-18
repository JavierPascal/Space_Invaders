package mx.itesm.pascal;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

class MarioScreen  extends Screen {

    private coreGame game;
    //Map
    private OrthogonalTiledMapRenderer rendererMap;
    private TiledMap map;
    //Audio
    private Music backAudio;
    private Sound fx;
    //States
    private GameState gameState;
    private PauseScene pauseScene;
    private ParticleEffect pe;
    private ParticleEmitter fireEmitter;

    public MarioScreen(coreGame coreGame) {
        this.game = coreGame;
    }

    @Override
    public void show() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("Map/marioMap.tmx", TiledMap.class);
        manager.load("Audios/marioBros.mp3", Music.class);
        manager.load("Audios/moneda.mp3",Sound.class);
        manager.finishLoading(); //Segundo Plano
        map = manager.get("Map/marioMap.tmx");
        rendererMap = new OrthogonalTiledMapRenderer(map);
        //Read Audios
        backAudio = manager.get("Audios/marioBros.mp3");
        fx = manager.get("Audios/moneda.mp3");

        backAudio.setLooping(true);
        backAudio.play();
        backAudio.setVolume(.2f);

        Gdx.input.setInputProcessor( new EntryProcessor());

        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("fuego.p"),Gdx.files.internal("./"));
        Array<ParticleEmitter> emitters = pe.getEmitters();
        emitters.get(0).setPosition(Width/2,Height/2);
        pe.start();

    }

    @Override
    public void render(float delta) {

        pe.update(delta);
        clearScreen(1,0,0);

        batch.setProjectionMatrix(camara.combined);
        rendererMap.setView(camara);
        rendererMap.render();
        batch.begin();
        pe.draw(batch);
        batch.end();

        if (gameState == GameState.PAUSE) {
            pauseScene.draw();
        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private class EntryProcessor implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            fx.play();
            //Pause
            if (gameState==GameState.PLAYING){
                gameState = GameState.PAUSE;
                backAudio.pause();
                if (pauseScene == null) {
                    pauseScene = new PauseScene(vista,batch);
                }
            } else{
                gameState = GameState.PLAYING;
                backAudio.play();
            }

            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
    class PauseScene extends Stage{

        public PauseScene(Viewport view, SpriteBatch batch){
            super(view,batch);
            Pixmap pixmap =  new Pixmap((int)(Width*0.7f),(int)(Height*0.8f), Pixmap.Format.RGBA8888);
            pixmap.setColor(0,0,0,.5f);
            pixmap.fillRectangle(0,0,pixmap.getWidth(),pixmap.getHeight());
            Texture rectangleTexture = new Texture(pixmap);

            Image rectangleImage = new Image(rectangleTexture);
            rectangleImage.setPosition(Width/2-pixmap.getWidth()/2,Height/2-pixmap.getHeight()/2);
            this.addActor(rectangleImage);
        }
    }
}
