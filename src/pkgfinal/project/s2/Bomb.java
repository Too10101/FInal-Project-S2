/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgfinal.project.s2;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author 629469
 */
public class Bomb {
    private int x;
    private int y;
    private double dx;
    private double dy;
    private int size;
    
    private ImageIcon ii;
    private Image img;
    
    private double grav = .2;
    private double vel = 0;
    
    //Constructor  
    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
        dx = 0;
        dy = 0;
        size = 25;
        ii = new ImageIcon(getClass().getResource("/Images/Bomb.gif"));
        img = ii.getImage();
    }
    
    //Methods
    public void draw(Graphics g) {
        g.drawImage(img, x, y, size, size, null);
    }
    
    public void update() {
        x += dx;
        y += dy;
    }
    
    public void kill() {
        x = -9000;
        y = -9000;
    }
    
    public void grav() {
        vel += grav;
        vel *= .99;
        y += vel;
    }

    //Getters and Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getDX() {
        return dx;
    }

    public void setDX(double dx) {
        this.dx = dx;
    }

    public double getDY() {
        return dy;
    }

    public void setDY(double dy) {
        this.dy = dy;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
