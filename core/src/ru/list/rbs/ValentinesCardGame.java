package ru.list.rbs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

public class ValentinesCardGame extends ApplicationAdapter {

	private class Heart {
		private Vector2 position; // это пара координат
		// Задаем скорость
		private Vector2 velocity; // Так как у скорости 2 компоненты
		private float size; // Коэффициент масштаба картинки
		// Скажем на каждый объект действует ветер
		private float wind;
		// Задаем разные оттенки сердцам
		private float color;
		// Добавляем эффект кручения вокруг оси
		private float time;

		public Heart() {
			this.position = new Vector2(0, 0); // СОздаем с координатами 0,0
			this.velocity = new Vector2(0, 0);
			this.init();
		}
		public void init() {
			this.position.set(MathUtils.random(0, 1280), -100f - MathUtils.random(720)); // Сердца создаются случайно по X от 0 до 1280. Создаются заранее, внизу, за границей
			this.velocity.set(0.0f, 100.0f + MathUtils.random(100.0f)); // скорость по y от 100 до 200
			this.size = 0.15f + MathUtils.random(0.15f); // от 0,15 - 0,3 масштаб
			this.wind = MathUtils.random(-30.0f, 30.0f);
			this.color = MathUtils.random(0.7f, 1.0f);
			this.time = MathUtils.random(0.0f, 100.f);
		}
		public void render() { // Сердце рисует себя на форме
			int imageCenterX = (int) (0.5 * heartTexture.getWidth());
			int imageCenterY = (int) (0.5 * heartTexture.getHeight());
			// Добавляем эффект тени (создаем темный объект на заднем фоне)
			batch.setColor(0.2f, 0.2f, 0.2f, 0.2f); // Затемненный, смещение ниже
			batch.draw(heartTexture,    // Батч, нарисуй текстуру сердца с координ. х, y. Смещение для указания на центр рисунка, а не на левый нижний край
					position.x - imageCenterX - 5,
					position.y - imageCenterY - 5,
					// Якорь устанавливаем в центр. Относительно якоря производится масштабирование и поворот
					(float) imageCenterX, (float) imageCenterY,
					// Исходные размеры
					heartTexture.getWidth(), heartTexture.getHeight(),
					// Масштаб
					size * (float) Math.sin(time),
					size,
					// Угол поворота. Задаем по значению скорости
					velocity.angle() - 90.0f,
					// Исходные точки
					0, 0,
					// Исходная ширина и высота
					heartTexture.getWidth(), heartTexture.getHeight(),
					// Автоматическое отзеркаливание
					false, false
					);
			batch.setColor(color, color, color, 1.0f);
			batch.draw(heartTexture,    // Батч, нарисуй текстуру сердца с координ. х, y. Смещение для указания на центр рисунка, а не на левый нижний край
					position.x - imageCenterX,
					position.y - imageCenterY,
					// Якорь устанавливаем в центр. Относительно якоря производится масштабирование и поворот
					(float) imageCenterX, (float) imageCenterY,
					// Исходные размеры
					heartTexture.getWidth(), heartTexture.getHeight(),
					// Масштаб
					size * (float) Math.sin(time), // Добавляем эффект кручения (синусоида)
					size,
					// Угол поворота. Задаем по значению скорости
					velocity.angle() - 90.0f,
					// Исходные точки
					0, 0,
					// Исходная ширина и высота
					heartTexture.getWidth(), heartTexture.getHeight(),
					// Автоматическое отзеркаливание
					false, false
			);
				batch.setColor(1,1,1,1); // Чтобы сбросить первую настройку
		}
		public void update(float dt) { // Вычислять координаты, проверять столкновения и т.п.
			// Делаем полет вверх
			position.y += velocity.y * dt; // y меняется со скоростью velocity.y пикс в секунду
			position.x += velocity.x * dt;

			// Ветер будет влиять на скорость Х
			velocity.x += wind * dt;
			// Вверх ускорение будет увеличиваться
			velocity.y += 20.0f * dt;
			// Если координата по y > макс.размер экрана, плюс размер сердца, то делаем переинициализацию (сердце залетело вверх полностью, скрылось и заново рисуется)
			// Чем быстрее двигается, тем сильнее действует ветер
			 time += velocity.x * dt / 100.0f;

			if (position.y > (720 + size * heartTexture.getHeight())) {
				init();
			}
			// Если объект улетает за правый и левый край, то мы перекидываем его обратно внутрь, к другому краю
			if (position.x < -100.0f) {
				position.x = 1380f;
			}
			if (position.x > 1380f) {
				position.x = -100f;
			}
		}
	}

	private SpriteBatch batch; // Область окна где мы можем рисовать
	private Heart[] hearts;
	private static final int HEART_COUNT = 220;
	private Texture heartTexture;
	private Texture logoTexture;
	private Vector2 logoCenter; // центр для лого
	private float globalTime; // Время самого приложения
	private Vector2 temp;
	private Vector2 mousePrevious; // Координаты мыши


	@Override
	public void create () { // Когда запускается приложение, стартует этот метод
		batch = new SpriteBatch();
		heartTexture = new Texture("heart.png");
		logoTexture = new Texture("logo.png");
		logoCenter = new Vector2(0, 0);
		hearts = new Heart[HEART_COUNT]; // Создаем все сердца и инициализируем
		mousePrevious = new Vector2(-1, -1);
		for (int i = 0; i < hearts.length; i++) {
			hearts[i] = new Heart();
		}
		temp = new Vector2(0,0);
	}

	@Override
	public void render () { // Не более 60 раз в секунду. Можно менять
		float dt = Gdx.graphics.getDeltaTime(); // У видеочипа спрашиваем сколько времени прошло с отрисовки (кадры в секунду)
		update(dt);
		Gdx.gl.glClearColor(1, 1, 1, 1); // Цвет очистки экрана. 0-1. Альфа - прозрачность
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		// Лого помещаем в центре, картинка перемещается вверх вниз с амплитудой 10px
		logoCenter.set(640.0f, 360.0f + 10.0f * (float) Math.sin(globalTime * 2.0f));
		// Рисуем картинку с тенью
		batch.setColor(0.2f, 0.2f, 0.2f, 0.2f);
		batch.draw(logoTexture, logoCenter.x - (int) (0.5 * logoTexture.getWidth()), logoCenter.y - (int) (0.5 * logoTexture.getHeight()));
		batch.setColor(1, 1, 1, 1);
		batch.draw(logoTexture, logoCenter.x - (int) (0.5 * logoTexture.getWidth()) + 10, logoCenter.y - (int) (0.5 * logoTexture.getHeight()) + 10);

		// Добавляем пару бьющихся сердец
		int imageCenterX = (int) (0.5 * heartTexture.getWidth());
		int imageCenterY = (int) (0.5 * heartTexture.getHeight());
		float size = 0.2f + Math.abs((float) Math.sin(globalTime * 4.0f)) * 0.1f;
		batch.draw(heartTexture,    // Батч, нарисуй текстуру сердца с координ. х, y. Смещение для указания на центр рисунка, а не на левый нижний край
				logoCenter.x - imageCenterX - 135,
				logoCenter.y - imageCenterY - 135,
				// Якорь устанавливаем в центр. Относительно якоря производится масштабирование и поворот
				(float) imageCenterX, (float) imageCenterY,
				// Исходные размеры
				heartTexture.getWidth(), heartTexture.getHeight(),
				// Масштаб
				size,
				size,
				// Угол поворота. Задаем по значению скорости
				0,
				// Исходные точки
				0, 0,
				// Исходная ширина и высота
				heartTexture.getWidth(), heartTexture.getHeight(),
				// Автоматическое отзеркаливание
				false, false
		);
		batch.draw(heartTexture,    // Батч, нарисуй текстуру сердца с координ. х, y. Смещение для указания на центр рисунка, а не на левый нижний край
				logoCenter.x - imageCenterX - 70,
				logoCenter.y - imageCenterY - 160,
				// Якорь устанавливаем в центр. Относительно якоря производится масштабирование и поворот
				(float) imageCenterX, (float) imageCenterY,
				// Исходные размеры
				heartTexture.getWidth(), heartTexture.getHeight(),
				// Масштаб
				size,
				size,
				// Угол поворота. Задаем по значению скорости
				0,
				// Исходные точки
				0, 0,
				// Исходная ширина и высота
				heartTexture.getWidth(), heartTexture.getHeight(),
				// Автоматическое отзеркаливание
				false, false
		);

		// Проходим по всем сердцам и каждое просим нарисовать себя на экране
		for (int i = 0; i < hearts.length; i++) {
			hearts[i].render();
		}
		batch.end();
	}

	public void update(float dt) {
		float dx = 0.0f, dy = 0.0f; // Запоминает положение мыши. Если двинули мышь вправо, то dx полож. если влево то dx отрицат.
		if (mousePrevious.x > 1 && mousePrevious.y > -1 && Gdx.input.isTouched()) { // Если запись координат была и пользователь жмет на экран
			dx = (Gdx.input.getX() - mousePrevious.x) * dt * 10.0f; // Считаем разницу между прошлым кадром и текущим и  задаем коэффициент масштаба
			dy = ((720.0f - Gdx.input.getY()) - mousePrevious.y) * dt * 10.0f;
		}
//		x += 100 * dt; // говорим что движение должно быть за секунду на 100 пикселей
		// Говорим сердцам что они должны апдейтится
		globalTime += dt;
		for (Heart h : hearts) {
			h.update(dt);
			h.velocity.x += dx; // для тача
			h.velocity.y += dy;
			// в центре экрана должно быть место куда не залетают сердца (радиус 300px)
			float rad = 300.0f;
			if (h.position.dst(logoCenter) < rad) { // Если расстояние от сердца до центра картинки меньше радиуса, то мы двигаем сердце
				temp.set(logoCenter).sub(h.position).nor().scl(-rad).add(logoCenter);
				h.position.set(temp);
				// Если они с одной стороны бьются о зону, то мы их двигаем в другую (скорость смещаем)
				h.velocity.x += Math.signum(h.position.x - logoCenter.x) * rad * dt;
			}
		}
		// Смотрим куда смотрела мышь в этой точке
		mousePrevious.set(Gdx.input.getX(), 720.0f - Gdx.input.getY()); // Запрашиваем коорд мыши и инвертируем Y
	}
	
	@Override
	public void dispose () { // Выгружает. Очищает ресурсы. Работа за пределами JVM
		batch.dispose();
		heartTexture.dispose();
		logoTexture.dispose();
	}
}
