package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    final static int SCREEN_WIDTH = 600;
    final static int SCREEN_HEIGHT = 600;
    final static int UNIT_SIZE = 50;
    final static int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 125;
    int seconds = 0;
    int minutes = 0;
    int counterForTime = 0;
    int lengthOfTheSnake = 1;
    int score = 0;
    int counterForApple = 0;
    int appleX;
    int appleY;
    int x[] = new int[GAME_UNITS];
    int y[] = new int[GAME_UNITS];
    Direction direction = Direction.RIGHT;
    boolean running = false;
    Timer timer;

    GamePanel(){

        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.LIGHT_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void newApple(){
        appleX = (int)(Math.random()*(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = (int)(Math.random()*(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
            super.paintComponent(g);
            draw(g);
    }

    public void draw(Graphics g) {
        if(running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            if (counterForApple == 3)
                g.setColor(Color.blue);
            else
                g.setColor(Color.red);

            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < lengthOfTheSnake; i++) {
                if (i == 0) {
                    g.setColor(Color.ORANGE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

            }
            g.setColor(Color.GRAY);
            Font font = new Font("impact", Font.PLAIN, 20);
            g.setFont(font);
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score,(SCREEN_WIDTH - metrics.stringWidth("Score: " + score))/2, g.getFont().getSize());
            g.drawString(minutes + ":" + seconds,0,g.getFont().getSize());

        }
        else {
            gameOver(g);
        }
    }

    public void move(){
        for(int i = lengthOfTheSnake;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case RIGHT -> {
                x[0] = x[0] + UNIT_SIZE;
                break;
            }
            case LEFT -> {
                x[0] = x[0] - UNIT_SIZE;
                break;
            }
            case UP -> {
                y[0] = y[0] - UNIT_SIZE;
                break;
            }
            case DOWN -> {
                y[0] = y[0] + UNIT_SIZE;
                break;
            }

        }


    }
/** sprawdza czy głowa snake-a znajduje się tam gdzie jabłko **/
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY) && counterForApple == 3  && lengthOfTheSnake > 1) {
            lengthOfTheSnake--;
            score++;
            counterForApple = 0;
            newApple();
        }

        if((x[0] == appleX) && (y[0] == appleY)) {
            lengthOfTheSnake++;
            score++;
            counterForApple++;
            newApple();
        }
    }

    public void checkCollision(){
        for(int i = lengthOfTheSnake;i>0;i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //sprawdza czy nie wychodzi poza górę ekranu
        if(y[0] < 0){
            running = false;
        }
        //sprawdza czy nie wychodzi poza dół ekranu
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }
        //sprawdza czy nie wychodzi poza prawą stronę ekranu
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //sprawdza czy nie wychodzi poza lewą stronę ekranu
        if(x[0] < 0) {
            running = false;
        }

        if(!running)
            timer.stop();
    }
    public void gameOver(Graphics g){
        g.setColor(Color.GRAY);
        Font font = new Font("impact", Font.PLAIN, UNIT_SIZE);
        g.setFont(font);
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game over",(SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/3);
        g.drawString("Your score: " + score,(SCREEN_WIDTH - metrics.stringWidth("Your score: " + score))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running=true){
            move();
            checkApple();
            checkCollision();
            counterForTime++;
            if(counterForTime == 8){
                counterForTime = 0;
                seconds++;
            }
        }
        repaint();

    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A -> {
                if(direction != Direction.RIGHT)
                    direction = Direction.LEFT;

            }
            case KeyEvent.VK_D -> {
                if(direction != Direction.LEFT)
                    direction = Direction.RIGHT;

            }
            case KeyEvent.VK_W -> {
                if(direction != Direction.DOWN)
                    direction = Direction.UP;

            }
            case KeyEvent.VK_S -> {
                if(direction != Direction.UP)
                    direction = Direction.DOWN;

            }
        }



        }
    }
    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
