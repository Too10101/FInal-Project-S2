package pkgfinal.project.s2;


import java.awt.Color;
import java.awt.Graphics;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 629469
 */
public class Terrain {
    int x;
    int y;
    private int dx;
    private int dy;
    int length;
    int width;
    private Color color;
    
    //Constructor
    public Terrain() {
        x = 0;
        y = 600;
        dx = 0;
        dy = 0;
        color = Color.BLACK;
        length = 40;
        width = 200;
    }
    
    public Terrain(int x, int y) {
        this.x = x;
        this.y = y;
        dx = 2;
        dy = 0;
        color = new Color(23, 119, 0);
        length = 200;
        width = 250;
    }
    
    public Terrain(int x, int y, int length, int width) {
        this.x = x;
        this.y = y;
        dx = 2;
        dy = 0;
        color = new Color(23, 130, 0);
        this.length = length;
        this.width = width;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, length);
    }
    
    public void update() {
            x -= dx;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Color getColor() {
        return color;
    }
    public int getLength() {
        return length;
    }
    public int getWidth() {
        return width;
    }
    public int getDX() {
        return dx;
    }
    public int getDY() {
        return dy;
    }
   
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setDX(int dx) {
        this.dx = dx;
    }
    public void setDY(int dy) {
        this.dy = dy;
    }
}
