package ru.list.rbs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ValentinesCardGame extends ApplicationAdapter {

	private class Heart {
		private Vector2 position; // this is a pair of coordinates
		// Set the speed
		private Vector2 velocity; // Since speed has 2 components
		private float size; // Picture scale factor
		// Let's say each object is affected by the wind
		private float wind;
		// We assign different shades to hearts
		private float color;
		// Adding the effect of torsion around an axis
		private float time;

		public Heart() {
			this.position = new Vector2(0, 0); // Create with coordinates 0,0
			this.velocity = new Vector2(0, 0);
			this.init();
		}
		public void init() {
			this.position.set(MathUtils.random(0, 1280), -100f - MathUtils.random(720)); // Hearts are created randomly by X from 0 to 1280. Created in advance, below, abroad
			this.velocity.set(0.0f, 100.0f + MathUtils.random(100.0f)); // y speed from 100 to 200
			this.size = 0.15f + MathUtils.random(0.15f); // from 0.15 - 0.3 scale
			this.wind = MathUtils.random(-30.0f, 30.0f);
			this.color = MathUtils.random(0.7f, 1.0f);
			this.time = MathUtils.random(0.0f, 100.f);
		}
		public void render() { // The heart draws itself on the shape
			int imageCenterX = (int) (0.5 * heartTexture.getWidth());
			int imageCenterY = (int) (0.5 * heartTexture.getHeight());
			// Add a shadow effect (create a dark object in the background)
			batch.setColor(0.2f, 0.2f, 0.2f, 0.2f); // Darkened, offset below
			batch.draw(heartTexture,    // Butch, draw a heart texture with coordinates. x, y. Offset to point to the center of the picture rather than the bottom left edge
					position.x - imageCenterX - 5,
					position.y - imageCenterY - 5,
					// We install the anchor in the center. Relative to the anchor, scaling and rotation are performed
					(float) imageCenterX, (float) imageCenterY,
					// Original dimensions
					heartTexture.getWidth(), heartTexture.getHeight(),
					// scale
					size * (float) Math.sin(time),
					size,
					// Angle of rotation. Set by speed value
					velocity.angle() - 90.0f,
					// Starting points
					0, 0,
					// Original width and height
					heartTexture.getWidth(), heartTexture.getHeight(),
					// Automatic mirroring
					false, false
					);
			batch.setColor(color, color, color, 1.0f);
			batch.draw(heartTexture,    // Butch, draw a heart texture with coordinates. x, y. Offset to point to the center of the picture rather than the bottom left edge
					position.x - imageCenterX,
					position.y - imageCenterY,
					// We install the anchor in the center. Relative to the anchor, scaling and rotation are performed
					(float) imageCenterX, (float) imageCenterY,
					// Original dimensions
					heartTexture.getWidth(), heartTexture.getHeight(),
					// scale
					size * (float) Math.sin(time), // Adding a torsion effect (sine wave)
					size,
					// Angle of rotation. Set by speed value
					velocity.angle() - 90.0f,
					// Starting points
					0, 0,
					// Original width and height
					heartTexture.getWidth(), heartTexture.getHeight(),
					// Automatic mirroring
					false, false
			);
				batch.setColor(1,1,1,1); // To reset the first setting
		}
		public void update(float dt) { // Calculate coordinates, check collisions, etc.
			// Let's fly up
			position.y += velocity.y * dt; // y changes at velocity.y pix per second
			position.x += velocity.x * dt;

			// The wind will affect the speed of X
			velocity.x += wind * dt;
			// Upward acceleration will increase
			velocity.y += 20.0f * dt;
			// If the y coordinate > the maximum screen size, plus the size of the heart, then we re-initialize (the heart flies up completely, disappears and is drawn again)
			// The faster it moves, the stronger the wind
			 time += velocity.x * dt / 100.0f;

			if (position.y > (720 + size * heartTexture.getHeight())) {
				init();
			}
			// If an object flies off the right and left edges, then we throw it back inside, to the other edge
			if (position.x < -100.0f) {
				position.x = 1380f;
			}
			if (position.x > 1380f) {
				position.x = -100f;
			}
		}
	}

	private SpriteBatch batch; // The area of the window where we can draw
	private Heart[] hearts;
	private static final int HEART_COUNT = 220;
	private Texture heartTexture;
	private Texture logoTexture;
	private Vector2 logoCenter; // logo center
	private float globalTime; // Time of the application itself
	private Vector2 temp;
	private Vector2 mousePrevious; // Mouse coordinates


	@Override
	public void create () { // When the application starts, this method starts
		batch = new SpriteBatch();
		heartTexture = new Texture("heart.png");
		logoTexture = new Texture("logo.png");
		logoCenter = new Vector2(0, 0);
		hearts = new Heart[HEART_COUNT]; // Create all the hearts and initialize
		mousePrevious = new Vector2(-1, -1);
		for (int i = 0; i < hearts.length; i++) {
			hearts[i] = new Heart();
		}
		temp = new Vector2(0,0);
	}

	@Override
	public void render () { // No more than 60 times per second. Can be changed
		float dt = Gdx.graphics.getDeltaTime(); // We ask the video chip how much time has passed since rendering (frames per second)
		update(dt);
		Gdx.gl.glClearColor(1, 1, 1, 1); // Screen clear color. 0-1. Alpha - transparency
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		// Place the logo in the center, the image moves up and down with an amplitude of 10px
		logoCenter.set(640.0f, 360.0f + 10.0f * (float) Math.sin(globalTime * 2.0f));
		// Drawing a picture with a shadow
		batch.setColor(0.2f, 0.2f, 0.2f, 0.2f);
		batch.draw(logoTexture, logoCenter.x - (int) (0.5 * logoTexture.getWidth()), logoCenter.y - (int) (0.5 * logoTexture.getHeight()));
		batch.setColor(1, 1, 1, 1);
		batch.draw(logoTexture, logoCenter.x - (int) (0.5 * logoTexture.getWidth()) + 10, logoCenter.y - (int) (0.5 * logoTexture.getHeight()) + 10);

		// Add a couple of beating hearts
		int imageCenterX = (int) (0.5 * heartTexture.getWidth());
		int imageCenterY = (int) (0.5 * heartTexture.getHeight());
		float size = 0.2f + Math.abs((float) Math.sin(globalTime * 4.0f)) * 0.1f;
		batch.draw(heartTexture,    // Butch, draw a heart texture with coordinates. x, y. Offset to point to the center of the picture rather than the bottom left edge
				logoCenter.x - imageCenterX - 135,
				logoCenter.y - imageCenterY - 135,
				// We install the anchor in the center. Relative to the anchor, scaling and rotation are performed
				(float) imageCenterX, (float) imageCenterY,
				// Original dimensions
				heartTexture.getWidth(), heartTexture.getHeight(),
				// scale
				size,
				size,
				// Angle of rotation. Set by speed value
				0,
				// Starting points
				0, 0,
				// Original width and height
				heartTexture.getWidth(), heartTexture.getHeight(),
				// Automatic mirroring
				false, false
		);
		batch.draw(heartTexture,    // Butch, draw a heart texture with coordinates. x, y. Offset to point to the center of the picture rather than the bottom left edge
				logoCenter.x - imageCenterX - 70,
				logoCenter.y - imageCenterY - 160,
				// We install the anchor in the center. Relative to the anchor, scaling and rotation are performed
				(float) imageCenterX, (float) imageCenterY,
				// Original dimensions
				heartTexture.getWidth(), heartTexture.getHeight(),
				// scale
				size,
				size,
				// Angle of rotation. Set by speed value
				0,
				// Starting points
				0, 0,
				// Original width and height
				heartTexture.getWidth(), heartTexture.getHeight(),
				// Automatic mirroring
				false, false
		);

		// We go through all the hearts and ask everyone to draw themselves on the screen
		for (int i = 0; i < hearts.length; i++) {
			hearts[i].render();
		}
		batch.end();
	}

	public void update(float dt) {
		float dx = 0.0f, dy = 0.0f; // Remembers the position of the mouse. If you move the mouse to the right, then dx is positioned. if to the left then dx is negative.
		if (mousePrevious.x > 1 && mousePrevious.y > -1 && Gdx.input.isTouched()) { // If the coordinates were recorded and the user presses the screen
			dx = (Gdx.input.getX() - mousePrevious.x) * dt * 10.0f; // We calculate the difference between the last frame and the current one and set the scale factor
			dy = ((720.0f - Gdx.input.getY()) - mousePrevious.y) * dt * 10.0f;
		}
		// x += 100 * dt; // we say that the movement should be 100 pixels per second
		// We tell hearts that they must update
		globalTime += dt;
		for (Heart h : hearts) {
			h.update(dt);
			h.velocity.x += dx; // for touch
			h.velocity.y += dy;
			// there should be a place in the center of the screen where hearts do not fly (radius 300px)
			float rad = 300.0f;
			if (h.position.dst(logoCenter) < rad) { // If the distance from the heart to the center of the picture is less than the radius, then we move the heart
				temp.set(logoCenter).sub(h.position).nor().scl(-rad).add(logoCenter);
				h.position.set(temp);
				// If they hit the zone on one side, then we move them to the other (we shift the speed)
				h.velocity.x += Math.signum(h.position.x - logoCenter.x) * rad * dt;
			}
		}
		// Let's look where the mouse was looking at this point
		mousePrevious.set(Gdx.input.getX(), 720.0f - Gdx.input.getY()); // We request the mouse coordinate and invert Y
	}
	
	@Override
	public void dispose () { // Unloads. Clears resources. Working outside the JVM
		batch.dispose();
		heartTexture.dispose();
		logoTexture.dispose();
	}
}
