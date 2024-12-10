package com.perisic.banana.engine;

import java.awt.image.BufferedImage;


public class GameEngine {
    String thePlayer = null;

    /**
     * Each player has their own game engine.
     *      *
             *      * @param player
     *      */
    public GameEngine(String player) {
        thePlayer = player;
    }

    int counter = 0;
    int score = 0;
    GameServer theGames = new GameServer();
    Game current = null;

    /**
     * Retrieves a game. This basic version only has two games that alternate.
     */
    public BufferedImage nextGame() {
        current = theGames.getRandomGame();
        return current.getImage();

    }


    public boolean checkSolution( int i) {
        if (i == current.getSolution()) {
            score++;
            return true;
        } else {
            return false;
        }
    }

    public int getScore() {
        return score;
    }

}
