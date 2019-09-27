package mx.itesm.pascal;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

class MarioScreen  extends Screen {

    private coreGame game;
    //Map
    private OrthogonalTiledMapRenderer rendererMap;
    private TiledMap map;

    public MarioScreen(coreGame coreGame) {
        this.game = coreGame;
    }

    @Override
    public void show() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("Map/marioMap.tmx", TiledMap.class);
        manager.finishLoading(); //Segundo Plano
        map = manager.get("Map/marioMap.tmx");
        rendererMap = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void render(float delta) {
        clearScreen(1,0,0);

        batch.setProjectionMatrix(camara.combined);
        rendererMap.setView(camara);
        rendererMap.render();

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
}
