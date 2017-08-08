package edu.asu.stratego.game;

import java.io.ObjectInputStream;
import java.util.concurrent.Callable;

public class BoardCallable implements Callable<Object> {

    private ObjectInputStream player;

    private BoardCallable() {}

    public BoardCallable(ObjectInputStream player) {
        this.player = player;
    }

    @Override
    public Object call() throws Exception {
        return player.readObject();
    }
}
