package mx.itesm.pascal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class menuScreen implements Screen {

    private final coreGame coreGame;
    private OrthographicCamera camera;
    private Viewport view;
    private SpriteBatch batch;

    //Background
    private Texture backgroundTexture; //For the background image
    //Stages
    private Stage menuStage;


    public menuScreen(coreGame coreGame) {
        this.coreGame = coreGame;
    }

    @Override
    public void show() {
        configureView();
        loadTextures();
        createMenu();

    }

    private void createMenu() {
        menuStage = new Stage(view);
        TextureRegionDrawable trdSpaceInvaders = new TextureRegionDrawable(new TextureRegion(new Texture("btnSpaceInvaders.png")));
        TextureRegionDrawable trdSpaceInvadersPressed = new TextureRegionDrawable(new TextureRegion(new Texture("btnSpaceInvadersPressed.png")));
        ImageButton btnSpaceInvaders = new ImageButton(trdSpaceInvaders,trdSpaceInvadersPressed);
        btnSpaceInvaders.setPosition(coreGame.Widht/2-btnSpaceInvaders.getWidth()/2,2*coreGame.Height/3);

        btnSpaceInvaders.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event,x,y);
                coreGame.setScreen(new spaceinvadersScreen(coreGame));
            }
        });

        menuStage.addActor(btnSpaceInvaders);
        Gdx.input.setInputProcessor(menuStage);


    }

    private void loadTextures() {
        //Possible method loadFile();
        backgroundTexture = new Texture("background.jpg");
    }

    private void configureView() {
        camera = new OrthographicCamera();
        camera.position.set(coreGame.Widht /2,coreGame.Height /2,0);
        camera.update();
        view = new StretchViewport(coreGame.Widht, coreGame.Height,camera);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        clearScreen();

        //batch escala de acuerdo a la vista y la camara
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundTexture,0,0);
        batch.end();

        menuStage.draw();

    }

    private void clearScreen() {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();

    }
}
