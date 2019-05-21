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
    private Player player, playerTwo;
    private Timer timer;
    
    private int tempo = 0;
    private int score = 0;
    private int highScore = 0;
    private int bombAct = 0;
    
    private int jump = 2;
    private int jumpTwo = 2;
    private int birdsCol = 0;
    
    private int r = 0;
    
    public boolean start  = false;
    public boolean onePlayer  = true;
    
    public Space() {
        super();
        
        terrain = new ArrayList<>();
        platforms = new ArrayList<>();
        barriers = new ArrayList<>();
        birds = new ArrayList<>();
        bombs = new ArrayList<>();
        player = new Player();
        playerTwo = new Player();
                
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
            if (onePlayer) {
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("A: Move Left", 25, 250);
                g.drawString("D: Move Right", 25, 275);
                g.drawString("S: Move Down", 25, 300);
                g.drawString("T: Teleport Up (Once 2 Birds are Collected)", 25, 325);
                g.drawString("W: Jump", 25, 350);
            }
            else {
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("A & Left Arrow: Move Left", 25, 250);
                g.drawString("D & Right Arrow: Move Right", 25, 275);
                g.drawString("S & Down Arrow: Move Down", 25, 300);
                g.drawString("T: Teleport Up (Once 2 Birds are Collected)", 25, 325);
                g.drawString("W & Up Arrow: Jump", 25, 350);
            }
            if (onePlayer) {
                g.setFont(new Font("Arial", Font.BOLD, 80));
                g.setColor(Color.BLACK);
                g.fillRect(450,480, 40, 80);
            }
            else {
                g.setFont(new Font("Arial", Font.BOLD, 80));
                
                g.setColor(Color.BLACK);
                g.fillRect(650, 480, 46, 80);
            }
            g.setColor(Color.GRAY);
            g.drawString("1", 450, 550);
            g.drawString("2", 650, 550);
            player.setSize(100);
            player.setX(120);
            player.setY(100);
            player.setVel(0);
             player.setDX(0);
            
            playerTwo.setX(100);
            playerTwo.setY(100);
            playerTwo.setVel(0);
            playerTwo.setDX(0);
            
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
        
        if (!onePlayer) {
            playerTwo.draw(g);
        }
        
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
                playerTwo.grav();
                playerTwo.update();
                
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
            
            
            collision(player);
            collision(playerTwo);
            borderCollision(player);
            borderCollision(playerTwo);
            
            repaint();
        }
    }

    public void collision(Player c) {
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
        for (Terrain t: terrain) {
            if (t.getX() + 10 <= playerTwo.getX() + playerTwo.getSize() && t.getY() <= playerTwo.getY() + playerTwo.getSize()) {
                if (t.getX() + t.getWidth() >= playerTwo.getX() && t.getY() + t.getLength() >= playerTwo.getY()) {
                    playerTwo.setY(t.getY() - playerTwo.getSize());
                    //Corrects Velocity
                    playerTwo.setVel(0);
                    jumpTwo = 2;
                }
            }
        }
        for (Barrier t: barriers) {
            if (t.getX() <= c.getX() + c.getSize() && t.getY() <= c.getY() + c.getSize()) {
                if (t.getX() + t.getWidth() >= c.getX() && t.getY() + t.getLength() >= c.getY()) {
                    c.setX(t.getX() - c.getSize() - 4);
                    c.setDX(-5);
                }
            }
        }
        for (Platform p: platforms) {
            if (p.getX() <= c.getX() + c.getSize() && p.getY() <= c.getY() + c.getSize()) {
                if (p.getX() + p.getWidth() >= c.getX() && p.getY() + p.getLength() >= c.getY()) {
                     c.setX(-1000000);
                }
            }
        }
        
        for (Bird bi: birds) {
            if (bi.getX() <= c.getX() + c.getSize() && bi.getY() <= c.getY() + c.getSize()) {
                if (bi.getX() + bi.getSize() >= c.getX() && bi.getY() + bi.getSize() >= c.getY()) {
                    bi.setX(-1000000);
                    jump = 1;
                    jumpTwo = 1;
                    birdsCol++;
                }
            }
        }
        
        for (Bomb bo: bombs) {
            if (bo.getX() <= c.getX() + c.getSize() && bo.getY() <= c.getY() + c.getSize()) {
                if (bo.getX() + bo.getSize() >= c.getX() && bo.getY() + bo.getSize() >= c.getY()) {
                     c.setX(-100000);
                }
            }
        }
    }
    
    public void borderCollision(Player c) {
        if (c.getX() < 0) {
            c.setX(-100000);
        }
        if (c.getX() + c.getSize() > this.getWidth()) {
            c.setX(this.getWidth() - c.getSize() - 3);
        }
    }
    
    public void keyPressed(KeyEvent e) {
        //Player 1 controls
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.setDX(4);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.setDX(-4);
        }
        if (e.getKeyCode() == KeyEvent.VK_W && jump >= 1) {
            player.up();
            jump--;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            player.setDY(5);
        }
        if (e.getKeyCode() == KeyEvent.VK_T && birdsCol >= 2) {
            player.setY(player.getY() - 300);
            player.setVel(0);
            birdsCol -= 2;
        }
        
        //Player 2 controls
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerTwo.setDX(4);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerTwo.setDX(-4);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && jumpTwo >= 1) {
            playerTwo.up();
            jumpTwo--;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            playerTwo.setDY(5);
        }
        
        //Controls for both
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            start = true;
        }
        if (start == false) {
            if (e.getKeyCode() == KeyEvent.VK_1) {
                onePlayer = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_2) {
                onePlayer = false;
            }
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
        //Player 1
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.setDX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.setDX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            player.setDY(0);
        }
        
        //Player 2
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerTwo.setDX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerTwo.setDX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            playerTwo.setDY(0);
        }
    }
}