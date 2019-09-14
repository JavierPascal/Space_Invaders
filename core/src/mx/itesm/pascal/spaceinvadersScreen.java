package mx.itesm.pascal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class spaceinvadersScreen implements Screen {

    private final coreGame coreGame;
    private OrthographicCamera camera;
    private Viewport view;
    private SpriteBatch batch;

    //Background
    private Texture backgroundTexture; //For the background image
    //Stages
    private Stage HUD;
    //Enemies
    private Array<Enemy> arrEnemies;
    private int stepInX =7;
    private int steps = 0;
    private float stepTimer = 0f;
    private float MAX_STEP = 0.1f;
    //Ship
    private Ship ship;
    private Movement shipState = Movement.IDLE;
    //Bullet
    private Bullet bullet;
    private Texture bulletTexture;


    public spaceinvadersScreen(coreGame coreGame) {
        this.coreGame = coreGame;
    }

    @Override
    public void show() {
        configureView();
        loadTextures();
        createHUD();
        createEnemies();
        createShip();

    }

    private void createShip() {
        Texture shipTexture =  new Texture("space/ship.png");
        ship = new Ship(shipTexture,coreGame.Widht/2, 40);
    }

    private void createEnemies() {
        arrEnemies = new Array<>(11*5);
        Texture enemyUpTexture =  new Texture("space/upEnemy.png");
        Texture enemyDownTexture = new Texture("space/downEnemy.png");
        for (int line=0; line<5; line++){
            for (int column=0; column<11; column++){
                Enemy enemy = new Enemy(enemyUpTexture,enemyDownTexture, 40+column*80, 350+line*40);
                arrEnemies.add(enemy);
            }
        }
    }

    private void createHUD() {

        HUD = new Stage(view);

        //Space Invaders Button
        TextureRegionDrawable trdBack = new TextureRegionDrawable(new TextureRegion(new Texture("back.png")));
        TextureRegionDrawable trdBackPressed = new TextureRegionDrawable(new TextureRegion(new Texture("backPressed.png")));
        ImageButton btnBack = new ImageButton(trdBack,trdBackPressed);
        btnBack.setPosition(10,coreGame.Height-btnBack.getHeight());

        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event,x,y);
                coreGame.setScreen(new menuScreen(coreGame));
            }
        });

        //ShootButton

        TextureRegionDrawable trdShoot = new TextureRegionDrawable(new TextureRegion(new Texture("space/shoot.png")));
        ImageButton btnShoot = new ImageButton(trdShoot);
        btnShoot.setPosition(coreGame.Widht-btnShoot.getWidth(),0);

        btnShoot.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Shoot
                if (bullet==null){
                    bullet = new Bullet(bulletTexture,ship.getSprite().getX()+ship.getSprite().getWidth()/2,ship.getSprite().getY()+ship.getSprite().getHeight());

                }
                return true;
            }
        });


        //LEFT Button
        TextureRegionDrawable trdLeft = new TextureRegionDrawable(new TextureRegion(new Texture("space/leftArrow.png")));
        ImageButton btnLeft = new ImageButton(trdLeft);
        btnLeft.setPosition(0,0);

        btnLeft.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                shipState = Movement.LEFT;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                shipState = Movement.IDLE;
            }
        });

        //RIGHT Button
        TextureRegionDrawable trdRight = new TextureRegionDrawable(new TextureRegion(new Texture("space/rightArrow.png")));
        ImageButton btnRight = new ImageButton(trdRight);
        btnRight.setPosition(btnRight.getWidth()+30,0);

        btnRight.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                shipState = Movement.RIGHT;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                shipState = Movement.IDLE;
            }
        });

        HUD.addActor(btnBack);
        HUD.addActor(btnLeft);
        HUD.addActor(btnRight);
        HUD.addActor(btnShoot);

        Gdx.input.setInputProcessor(HUD);


    }

    private void loadTextures() {
        //Possible method loadFile();
        backgroundTexture = new Texture("fondoSpace.jpg");
        bulletTexture = new Texture("space/bullet.png");
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
        //UPDATE
        update(delta);
        //DRAW
        clearScreen();

        //batch escala de acuerdo a la vista y la camara
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundTexture,0,0);
        //Draw enemies
        for (Enemy enemy: arrEnemies) {
            enemy.render(batch);
        }
        ship.render(batch);
        if (bullet != null) {
            bullet.render(batch);

        }
        batch.end();

        HUD.draw();

    }

    private void update(float delta) {
        updateEnemies(delta);
        updateShip();
        updateBullet(delta);
        checkCollision();
        checkPlayerLooses();
    }

    private void checkPlayerLooses() {
        for (Enemy enemy: arrEnemies) {
            if (enemy.getSprite().getY()<=ship.getSprite().getY());
            //Looses
            //Freezes enemies and delete ship
            ship.getSprite().setPosition(0,coreGame.Height*2);
            break;

        }
    }

    private void checkCollision() {
        if (bullet==null){
            return;
        }
        for (int i=arrEnemies.size-1; i>=0; i--){
            Rectangle bulletRectangle = bullet.getSprite().getBoundingRectangle();
            Rectangle enemyRectangle = arrEnemies.get(i).getSprite().getBoundingRectangle();
            if (bulletRectangle.overlaps(enemyRectangle)){
                arrEnemies.removeIndex(i);
                bullet=null;
                break;
            }
        }
    }

    private void updateBullet(float delta) {
        if (bullet != null) {
            bullet.move(delta);
            if (bullet.getSprite().getY()>=coreGame.Height){
                bullet = null;
            }
        }

    }

    private void updateShip() {
        switch (shipState) {
            case RIGHT:
                ship.move(10);
                break;
            case LEFT:
                ship.move(-10);
                break;
        }
    }

    private void updateEnemies(float delta) {
        if (ship.getSprite().getY()==coreGame.Height*2){
            return;
        }
        stepTimer += delta;
        if (stepTimer>MAX_STEP){
            stepTimer=0;

            for (Enemy enemy:arrEnemies) {
                enemy.move(stepInX,0);
                enemy.changeImage();
            }
            steps++;
            //coreGame.Widht-1080)/Math.abs(stepInX)
            if (steps>=40){
                steps = 0;
                stepInX=-stepInX;
                for (Enemy enemy: arrEnemies){
                    enemy.move(0,-50);
                }
            }
        }
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

    private enum Movement {
        IDLE,
        RIGHT,
        LEFT
    }
}
