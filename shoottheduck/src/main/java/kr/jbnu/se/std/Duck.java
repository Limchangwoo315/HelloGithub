package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Duck {
    public static final long TIME_BETWEEN_DUCKS = Framework.SEC_IN_NANOSEC / 2;
    public static long lastDuckTime = 0;

    public int x;
    public int y;
    protected int speed;
    public int score;

    private BufferedImage duckImg;

    // 기절 상태 관련
    private boolean isStunned = false;
    private long stunnedStartTime = 0;
    private static final long STUN_DURATION = 1500000000L; // 1.5초
    private int originalSpeed;

    // 속도 관련 상수
    private static final int SPEED_FAST = -5;
    private static final int SPEED_MEDIUM = -4;
    private static final int SPEED_SLOW = -3;
    private static final int SPEED_VERY_SLOW = -2;

    // y 위치에 따른 범위 설정
    private static final double[] Y_THRESHOLDS = {0.15, 0.30, 0.58, 0.65, 0.70};

    public Duck(int x, int y, int speed, int score, BufferedImage duckImg) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.score = score;
        this.duckImg = duckImg;
        this.originalSpeed = speed;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(duckImg, x, y, null);
    }

    public void update() {
        updateStunStatus();

        if (isStunned) {
            speed = 0;
        } else {
            adjustSpeedBasedOnY();
        }

        x += speed;
    }

    private void adjustSpeedBasedOnY() {
        double frameHeight = Framework.frameHeight;

        speed = determineSpeed(frameHeight);
    }

    private int determineSpeed(double frameHeight) {
        if (y < frameHeight * Y_THRESHOLDS[0]) {
            return SPEED_FAST;
        } else if (y < frameHeight * Y_THRESHOLDS[1]) {
            return SPEED_MEDIUM;
        } else if (y < frameHeight * Y_THRESHOLDS[2]) {
            return SPEED_MEDIUM;
        } else if (y < frameHeight * Y_THRESHOLDS[3]) {
            return SPEED_SLOW;
        } else if (y < frameHeight * Y_THRESHOLDS[4]) {
            return SPEED_SLOW;
        } else {
            return SPEED_VERY_SLOW;
        }
    }

    public void stun() {
        if (!isStunned) {
            isStunned = true;
            stunnedStartTime = System.nanoTime();
            originalSpeed = speed;
            speed = 0;
        }
    }

    public void updateStunStatus() {
        if (isStunned && System.nanoTime() - stunnedStartTime >= STUN_DURATION) {
            isStunned = false;
            speed = originalSpeed;
        }
    }

    public boolean isStunned() {
        return isStunned;
    }

    public BufferedImage getImage() {
        return duckImg;
    }

    public int getScore() {
        return score;
    }

    public Rectangle getHitBox() {
        return new Rectangle(x, y, duckImg.getWidth(), duckImg.getHeight());
    }

    public static void resetLastDuckTime() {
        lastDuckTime = 0;
    }

    public static void updateLastDuckTime(long newTime) {
        lastDuckTime = newTime;
    }
}