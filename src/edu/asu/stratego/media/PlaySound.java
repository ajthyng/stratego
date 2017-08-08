package edu.asu.stratego.media;

import javafx.scene.media.AudioClip;

import edu.asu.stratego.util.HashTables;

public class PlaySound {
	public static void playMusic(String file, int Volume) {
		AudioClip music = HashTables.SOUND_MAP.get(file);
		music.setVolume(Volume);
		music.play();
		System.out.println("Played music (\"" + file + "\", " + Volume*100 + "% volume)");
	}
}