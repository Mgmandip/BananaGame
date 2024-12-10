
package com.perisic.banana.engine;

import java.awt.image.BufferedImage;

public class Game {
    BufferedImage image = null;
    int solution = -1;

    public Game(BufferedImage var1, int var2) {
        this.image = var1;
        this.solution = var2;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public int getSolution() {
        return this.solution;
    }
}
