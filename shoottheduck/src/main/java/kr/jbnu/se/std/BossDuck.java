package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class BossDuck extends Duck {

    private int health = 5;

    public BossDuck(int x, int y, int speed, BufferedImage duckImg) {
        super(x, y, speed, 500, duckImg);  // 보스몹 점수는 500점
    }

    public void takeDamage() {
        health--;
        System.out.println("Boss hit! Remaining health: " + health);
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void update() {
        double yOffset = 0;
        x += speed;
        if (x < 0 - getImage().getWidth() * 3) {
            x = Framework.frameWidth;
        }

        double ySpeed = 0.01;
        int amplitude = 30;
        yOffset = Math.sin(System.nanoTime() * ySpeed) * amplitude;
        y += (int) yOffset;

        int maxY = Framework.frameHeight - getImage().getHeight() * 3;
        if (y < 0) {
            y = 0;
        } else if (y > maxY) {
            y = maxY;
        }
    }

    @Override
    public void Draw(Graphics2D g2d) {
        int scaledWidth = getImage().getWidth() * 3;
        int scaledHeight = getImage().getHeight() * 3;
        g2d.drawImage(getImage(), x, y, scaledWidth, scaledHeight, null);
    }

    @Override
    public Rectangle getHitBox() {
        int scaledWidth = getImage().getWidth() * 3;
        int scaledHeight = getImage().getHeight() * 3;
        return new Rectangle(x, y, scaledWidth, scaledHeight);
    }
}
