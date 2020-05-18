package com.alyrow.test.library;

import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.alyrow.gdx.particle.ParticleRules;
import com.alyrow.gdx.particle.ParticleSystem;
import com.alyrow.gdx.particle.ParticleType;
import com.alyrow.gdx.particle.physics.*;
import com.alyrow.gdx.particle.rules.*;
import com.alyrow.gdx.particle.texture.ParticleTexture;
import io.anuke.gif.GifRecorder;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class TestMain extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture image;
	private ParticleSystem system;
	private World world;
	private Viewport vp;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private RayHandler rayHandler;
	private GifRecorder recorder;

	@Override
	public void create() {
		batch = new SpriteBatch();
		image = new Texture("badlogic.png");


		vp = new FitViewport(640, 480); //to draw sprites along with the spritebatch
		vp.getCamera().position.x = 0;
		vp.getCamera().position.y = 0;
		vp.getCamera().viewportWidth = 640;
		vp.getCamera().viewportHeight = 480;
		camera = new OrthographicCamera();
		camera.position.x = 0;
		camera.position.y = 0;
		camera.viewportWidth = 640;
		camera.viewportHeight = 480;
		camera.setToOrtho(false);

		Box2D.init();
		world = new World(new Vector2(10, 0), true);
		rayHandler = new RayHandler(world);
		rayHandler.setBlurNum(3);
		//rayHandler.setAmbientLight(new Color(0.1f, 0.1f, 0.1f, 0.3f));
		//rayHandler.setAmbientLight(new Color(0f, 0f, 0f, 1f));
		rayHandler.setShadows(true);
		debugRenderer = new Box2DDebugRenderer();


		ParticleRules rules = new ParticleRules();
		rules.setNumber(new ParticleEmissionNumber(ParticleEmissionNumber.PER_SECONDS, 40));
		rules.setLife(new ParticleLife(5, true));
		rules.setDuration(new ParticleEmissionDuration(true));
		//rules.setLight(new ParticleEmissionLightRandom(rayHandler, 128, new Color(0.37647f, 1f, 1f, 1), 35, 45));
		rules.setLight(new ParticleEmissionLightRandom(rayHandler, 128, new Color(0.3f, 1f, 0.4f, 1), 35, 45));

		system = new ParticleSystem(ParticleType.HALO, null, camera);
		system.setRules(rules);
		system.setParticlesPosition(300, 200);

		PhysicManager physicManager = new PhysicManager();
		physicManager.addForce(new BrownianForce(50, 100, 10000, 0.1D));
		physicManager.addForce(new RandomLinearForce(5, 30, 0, 0));
		physicManager.addForce(new RadialForce(10));
		system.setPhysicManager(physicManager);

		//system.setTexture(new ParticleTexture("nade.png"));

		recorder = new GifRecorder(batch);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(image, 165, 180);
		batch.end();



		camera.update();

		world.step(1f / 60f,6,2);
		//system.setParticlesPosition(Math.round(Math.random()*640), Math.round(Math.random()*480));
		system.setParticlesPosition(1, Math.round(Math.random()*480));
		//vp.apply();
		system.render();

		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.updateAndRender();
		debugRenderer.render(world, camera.combined);
		//Gdx.app.log("Particles", String.valueOf(system.particles.size));
		//if (system.particles.size > 0)
		//	Gdx.app.log("Inner Screen", String.valueOf(system.particles.first().isInnerScreen));

		recorder.update();
	}

	@Override
	public void dispose() {
		batch.dispose();
		image.dispose();
		system.dispose();
		rayHandler.dispose();
	}
}