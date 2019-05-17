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
    private ArrayList<Platform> platforms;
    private ArrayList<Barrier> barriers;
    private ArrayList<Bird> birds;
    private ArrayList<Bomb> bombs;
    private Player player;
    private Timer timer;
    
    private int tempo = 0;
    private int score = 0;
    private int highScore = 0;
    private int bombAct = 0;
    
    private int jump = 2;
    private int birdsCol = 0;
    
    private int r = 0;
    
    public boolean start  = false;
    
    public Space() {
        super();
        
        terrain = new ArrayList<>();
        platforms = new ArrayList<>();
        barriers = new ArrayList<>();
        birds = new ArrayList<>();
        bombs = new ArrayList<>();
        player = new Player();
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 150, 1000/60); 
        
        terrainGeneration();
        enemyGeneration();
    }
    
    private void enemyGeneration() {
        for (int i = 0; i < 100; i++) {
            birds.add(new Bird(i * 934 + 1300, (int) (Math.random() * 250 + 50), 40));
        }
    }
    
    private void terrainGeneration() {  
        for (int i = 0; i < 5000; i++) {
            int height = (int) (Math.random() * 200 + 430);
            terrain.add(new Terrain((i * 250) + 1000, (int) (Math.random() * 60 + 600)));
            terrain.add(new Terrain((i * 1000) + 2000, height, 250, 100));
            barriers.add(new Barrier((i * 1000) + 1990, height + 1, 250, 10));
            platforms.add(new Platform((i * 679) + 1345, (int) (Math.random() * 225 + 100), (int) (Math.random() * 30 + 20), (int) (Math.random() * 100 + 100), Color.WHITE));
        }
        terrain.add(new Terrain(0, 600, 200, 1000));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(104, 207, 210));
        
        g.setFont(new Font("Arial Black", Font.BOLD, 20));
        g.drawString("Score: " + score, 1000, 50);
        g.drawString("High Score: " + highScore, 938, 20);
        
        //Starting "Screen"
        if (start == false) {
            g.setFont(new Font("Arial", Font.BOLD, 70));
            g.drawString("Click Enter To Start!", 250, 400);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Controls:", 25, 225);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("A: Move Left", 25, 250);
            g.drawString("D: Move Right", 25, 275);
            g.drawString("S: Move Down", 25, 300);
            g.drawString("T: Teleport Up (Once 2 Birds are Collected)", 25, 325);
            g.drawString("Space Bar: Jump", 25, 350);
            player.setSize(100);
            player.setX(100);
            player.setY(100);
            player.setVel(0);
            
            jump = 2;
            birdsCol = 0;
        }
        else {
            player.setSize(50);
        }
        
        //Draws Objects
        for (Bomb bo: bombs)
            bo.draw(g);
        for (Barrier b: barriers)
            b.draw(g);
        for (Terrain t: terrain)
            t.draw(g);
        for (Platform p: platforms)
            p.draw(g);
        for (Bird bi: birds)
            bi.draw(g);

        player.draw(g);
        
        //Ending "Screen"
        if (player.getX() < -500 && start == true) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 100));
            g.drawString("Game Over!!!", 250, 400);
            g.setFont(new Font("Arial", Font.PLAIN, 75));
            g.drawString("Score: " + score, 400, 500);
            g.setFont(new Font("Arial", Font.PLAIN, 35));
            g.drawString("Click R to Restart! ", 10, 35);
            r = 1;
        }
    }
    
    private class ScheduleTask extends TimerTask {
    
        @Override
        public void run() {
            //Runs once game is started
            if (start == true && player.getX() > 0) {
                //Updates objects
                for (Terrain t: terrain) {
                    t.update();
                }
                for (Barrier b: barriers) {
                    b.update();
                }
                for (Platform p: platforms) {
                    p.update();
                }
                for (Bird bi: birds) {
                    bi.update();
                }
                for (Bomb bo: bombs) {
                    bo.update();
                    bo.grav();
                }
                player.grav();
                
                //Controls when bombs drop
                if (bombAct % 150 == 0 && score > 2500) {
                    bombs.add(new Bomb((int) (Math.random() * 1000), (int) (Math.random() * 100)));
                }
                
                //Sets pace of the game
                if (tempo % 1500 == 0) {
                    for (Terrain t: terrain) {
                        t.setDX(t.getDX() + 1);
                    }
                    for (Barrier b: barriers) {
                        b.setDX(b.getDX() + 1);
                    }
                    for (Platform p: platforms) {
                        p.setDX(p.getDX() + 1);
                    }
                    for (Bird bi: birds) {
                        bi.setDX(bi.getDX() - 1);
                    }
                }
                
                //Timers
                tempo++;
                bombAct++;
                score++;
            }

            //Always runs
            player.update();
            
            
            collision();
            borderCollision();
            
            repaint();
        }
    }

    public void collision() {
        for (Terrain t: terrain) {
            if (t.getX() + 10 <= player.getX() + player.getSize() && t.getY() <= player.getY() + player.getSize()) {
                if (t.getX() + t.getWidth() >= player.getX() && t.getY() + t.getLength() >= player.getY()) {
                    player.setY(t.getY() - player.getSize());
                    //Corrects Velocity
                    player.setVel(0);
                    jump = 2;
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
        for (Platform p: platforms) {
            if (p.getX() <= player.getX() + player.getSize() && p.getY() <= player.getY() + player.getSize()) {
                if (p.getX() + p.getWidth() >= player.getX() && p.getY() + p.getLength() >= player.getY()) {
                     player.setX(-1000);
                }
            }
        }
        
        for (Bird bi: birds) {
            if (bi.getX() <= player.getX() + player.getSize() && bi.getY() <= player.getY() + player.getSize()) {
                if (bi.getX() + bi.getSize() >= player.getX() && bi.getY() + bi.getSize() >= player.getY()) {
                    bi.setX(-1000);
                    jump = 1;
                    birdsCol++;
                }
            }
        }
        
        for (Bomb bo: bombs) {
            if (bo.getX() <= player.getX() + player.getSize() && bo.getY() <= player.getY() + player.getSize()) {
                if (bo.getX() + bo.getSize() >= player.getX() && bo.getY() + bo.getSize() >= player.getY()) {
                     player.setX(-1000);
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
        if (e.getKeyCode() == KeyEvent.VK_S) {
            player.setDY(5);
        }
        if (e.getKeyCode() == KeyEvent.VK_T && birdsCol >= 2) {
            player.setY(player.getY() - 300);
            player.setVel(0);
            birdsCol -= 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_R && r == 1) {
            start = false;
            for (Terrain t: terrain) {
                t.kill();
                t.setDX(2);
            }
            for (Platform p: platforms) {
                p.kill();
                p.setDX(2);
            }
            for (Barrier b: barriers) {
                b.kill();
                b.setDX(2);
            }
            for (Bird bi: birds) {
                bi.kill();
                bi.setDX(2);
            }
            for (Bomb bo: bombs) {
                    bo.kill();
            }
            
            if (score > highScore) {
                highScore = score;
            }
            
            terrainGeneration();
            enemyGeneration();
            
            tempo = 0;
            bombAct = 0;
            score = 0;
            
            r = 0;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.setDX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.setDX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            player.setDY(0);
        }
    }
}