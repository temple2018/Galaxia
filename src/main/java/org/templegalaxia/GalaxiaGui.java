package org.templegalaxia;

import processing.core.PApplet;

public class GalaxiaGui extends PApplet {
    static public void main(String args[]) {
        PApplet.main(new String[]{"--present", GalaxiaGui.class.getName()});
    }


    @Override
    public void settings() {
        // TODO: Customize screen size and so on here
        size(200, 200);
    }

    @Override
    public void setup() {
        // TODO: Your custom drawing and setup on applet start belongs here
        clear();
    }

    @Override
    public void draw() {
        // TODO: Do your drawing for each frame here
        clear();
        fill(255);
        rect(50, 50, 100, 100);
    }
}