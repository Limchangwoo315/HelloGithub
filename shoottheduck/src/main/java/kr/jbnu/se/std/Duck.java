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

    // DuckState 객체를 포함
    private DuckState state;

    private DuckSpeedManager speedManager;  // DuckSpeedManager 객체 추가

    public Duck(int x, int y, int speed, int score, BufferedImage duckImg) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.duckImg = duckImg;
        this.state = new DuckState(); // DuckState 객체 초기화
        this.speedManager = new DuckSpeedManager(speed); // DuckSpeedManager 초기화
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(duckImg, x, y, null);
    }

    public void update() {
        state.updateStunStatus(speed); // 상태 업데이트

        if (state.isStunned()) {
            speed = 0; // 기절 상태라면 속도 0
        } else {
            adjustSpeedBasedOnY();
        }

        x += speed;
    }

    private void adjustSpeedBasedOnY() {
        double frameHeight = Framework.frameHeight;
        speedManager.adjustSpeedBasedOnY(y, frameHeight);  // 속도 조정은 DuckSpeedManager에 맡김
        speed = speedManager.getSpeed();  // 변경된 속도를 적용
    }

    public void stun() {
        state.stun(speed);
        speed = 0; // 기절 상태로 전환
    }

    public boolean isStunned() {
        return state.isStunned();
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
