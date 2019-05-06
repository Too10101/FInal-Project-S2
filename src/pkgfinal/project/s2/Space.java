/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgfinal.project.s2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

/**
 *
 * @author 629469
 */
public class Space extends JPanel {
    
    private ArrayList<Terrain> terrain;
    private ArrayList<Barrier> barriers;
    private Player player;
    private Timer timer;
    
    private int tempo = 0;
    private int score = 0;
    private int jump = 1;
    
    public boolean start  = false;
    
    public Space() {
        super();
        
        terrain = new ArrayList<>();
        barriers = new ArrayList<>();
        player = new Player();
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 150, 1000/60); 
        
        terrainGeneration();
    }
    
    private void terrainGeneration() {  
        for (int i = 0; i < 5000; i++) {
            int height = (int) (Math.random() * 200 + 430);
            terrain.add(new Terrain((i * 250) + 1000, (int) (Math.random() * 60 + 600)));
            terrain.add(new Terrain((i * 1000) + 2000, height, 250, 100));
            barriers.add(new Barrier((i * 1000) + 1990, height + 1, 250, 10));
        }
        terrain.add(new Terrain(0, 600, 200, 1000));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(104, 207, 210));
        
        g.setFont(new Font("Arial Black", Font.BOLD, 20));
        g.drawString("Score: " + score, 1000, 20);
        
        if (start == false) {
            g.setFont(new Font("Arial", Font.BOLD, 70));
            g.drawString("Click Enter To Start!", 250, 400);
            player.setSize(100);
            player.setX(100);
            player.setY(100);
        }
        else {
            player.setSize(50);
        }
        
        for (Barrier b: barriers) {
            b.draw(g);
        }
        for (Terrain t: terrain) {
            t.draw(g);
        }
        player.draw(g);
        
        if (player.getX() < -500 && start == true) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 100));
            g.drawString("Game Over!!!", 250, 400);
            g.setFont(new Font("Arial", Font.PLAIN, 75));
            g.drawString("Score: " + score, 400, 500);
        }
    }
    
    private class ScheduleTask extends TimerTask {
    
        @Override
        public void run() {
            if (start == true && player.getX() > 0) {
                for (Terrain t: terrain) {
                    t.update();
                }
                for (Barrier b: barriers) {
                    b.update();
                }

                player.grav();
                
                if (tempo % 1000 == 0) {
                    for (Terrain t: terrain) {
                        t.setDX(t.getDX() + 1);
                    }
                    for (Barrier b: barriers) {
                        b.setDX(b.getDX() + 1);
                    }
                }
                
                tempo++;
                score++;
            }

            player.update();
            
            
            collision();
            borderCollision();
            
            repaint();
        }
    }
    
    public void collision() {
        for (Terrain t: terrain) {
            if (t.getX() <= player.getX() + player.getSize() && t.getY() <= player.getY() + player.getSize()) {
                if (t.getX() + t.getWidth() >= player.getX() && t.getY() + t.getLength() >= player.getY()) {
                    player.setY(t.getY() - player.getSize());
                    jump = 1;
                }
            }
        }
        for (Barrier t: barriers) {
            if (t.getX() <= player.getX() + player.getSize() && t.getY() <= player.getY() + player.getSize()) {
                if (t.getX() + t.getWidth() >= player.getX() && t.getY() + t.getLength() >= player.getY()) {
                    player.setX(t.getX() - player.getSize() - 4);
                    player.setDX(-5);
                    
                }
            }
        }
    }
    
    public void borderCollision() {
        if (player.getX() < 0) {
            player.setX(-1000);
        }
        if (player.getX() + player.getSize() > this.getWidth()) {
            player.setX(this.getWidth() - player.getSize() - 3);
        }
    }
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.setDX(4);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.setDX(-4);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && jump >= 1) {
            player.up();
            jump--;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            start = true;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.setDX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.setDX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {

        }
    }
}