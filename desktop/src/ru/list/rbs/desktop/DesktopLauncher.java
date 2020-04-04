package ru.list.rbs.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.list.rbs.ValentinesCardGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// Меняем размер окна
		config.width = 1280;
		config.height = 720;

		new LwjglApplication(new ValentinesCardGame(), config);
	}
}
