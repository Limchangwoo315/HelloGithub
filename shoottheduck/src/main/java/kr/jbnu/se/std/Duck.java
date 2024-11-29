package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Duck {
    public static long timeBetweenDucks = Framework.secInNanosec / 2;
    public static long lastDuckTime = 0;


    public int x;
    public int y;
    protected int speed;

    public int score;

    private BufferedImage duckImg;

    /**
     * Creates new duck.
     *
     * @param x Starting x coordinate.
     * @param y Starting y coordinate.
     * @param speed The speed of this duck.
     * @param score How many points this duck is worth?
     * @param duckImg Image of the duck.
     */
    // 기절 상태 관련
    private boolean isStunned = false;
    private long stunnedStartTime = 0;
    private static final long STUN_DURATION = 1500000000L; // 1.5초
    private int originalSpeed;

    //속도 관련 상수
    private static final int SPEED_FAST = -5;
    private static final int SPEED_MEDIUM = -4;
    private static final int SPEED_SLOW = -3;
    private static final int SPEED_VERY_SLOW = -2;


    public Duck(int x, int y, int speed, int score, BufferedImage duckImg) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.score = score;
        this.duckImg = duckImg;
        this.originalSpeed = speed;
    }

    /**
     * Move the duck.
     */
    public void update() {
        updateStunStatus(); // 기절 상태 업데이트

        if (!isStunned) {
            adjustSpeedBasedOnY(); // 기절 상태일 때 속도를 0으로 설정
        } else {
            speed = 0; // 기절 상태에 상관없이 y값에 따라 속도 조정
        }

        x += speed; // 속도에 따라 위치 이동
    }

    private void adjustSpeedBasedOnY() {
        if (y < Framework.frameHeight * 0.15) {
            speed = SPEED_FAST;
        } else if (y < Framework.frameHeight * 0.30) {
            speed = SPEED_MEDIUM;
        } else if (y < Framework.frameHeight * 0.58) {
            speed = SPEED_MEDIUM;
        } else if (y < Framework.frameHeight * 0.65) {
            speed = SPEED_SLOW;
        } else if (y < Framework.frameHeight * 0.70) {
            speed = SPEED_SLOW;
        } else {
            speed = SPEED_VERY_SLOW;
        }
    }


    public void Draw(Graphics2D g2d) {
        g2d.drawImage(duckImg, x, y, null);
    }

    public void stun() {
        if (!isStunned) { // 이미 기절 상태가 아닐 때만 기절
            isStunned = true;
            stunnedStartTime = System.nanoTime();
            originalSpeed = speed; // 현재 속도를 기절 이전의 속도로 저장
            speed = 0; // 기절 시 속도를 0으로 설정
        }
    }

    public void updateStunStatus() {
        if (isStunned && System.nanoTime() - stunnedStartTime >= STUN_DURATION) {
            isStunned = false; // 기절 상태 해제
            speed = originalSpeed;// 원래 속도로 복원
        }
    }
    public boolean isStunned() {
        return isStunned;
    }

    public BufferedImage getImage() { //오리 이미지 반환
        return duckImg;
    }

    public int getScore() { return score; }

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