package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * BossDuck class represents a larger, tougher duck that requires multiple hits to defeat.
 */
public class BossDuck extends Duck {

    private int health = 5; // 보스 체력 (10번 클릭해야 죽음)
    private double yOffset = 0;  // 위아래 움직임을 위한 오프셋
    private double ySpeed = 0.01; // 위아래 움직임 속도 (느리게 조정)
    private int amplitude = 30;   // 위아래 이동 범위 (작게 조정)

    /**
     * BossDuck 생성자.
     */
    public BossDuck(int x, int y, int speed, BufferedImage duckImg) {
        super(x, y, speed, 500, duckImg);  // 보스몹 점수는 500점
    }

    /**
     * 보스 체력을 줄임.
     */
    public void takeDamage() {
        health--;
        System.out.println("Boss hit! Remaining health: " + health);
    }

    /**
     * 보스의 남은 체력 반환.
     */
    public int getHealth() {
        return health;
    }

    /**
     * 보스 상태 업데이트: Y축으로 위아래로 천천히 움직임.
     */
    @Override
    public void Update() {
        // 보스가 왼쪽으로 이동
        x += speed;

        // 화면 경계 검사: 왼쪽 경계를 넘으면 오른쪽 끝에서 다시 시작
        if (x < 0 - getImage().getWidth() * 3) {
            x = Framework.frameWidth;  // 오른쪽 끝에서 시작
        }

        // 위아래로 천천히 이동: 삼각함수를 사용하여 오프셋 계산
        yOffset = Math.sin(System.nanoTime() * ySpeed) * amplitude;
        y += (int) yOffset;  // Y 좌표에 오프셋 적용

        // 위아래 경계 체크: 화면 위아래 경계를 넘지 않도록 제한
        int maxY = Framework.frameHeight - getImage().getHeight() * 3;
        if (y < 0) {
            y = 0; // 상단 경계에 맞춤
        } else if (y > maxY) {
            y = maxY; // 하단 경계에 맞춤
        }
    }

    /**
     * 보스 그리기 (크기 3배 확대).
     */
    @Override
    public void Draw(Graphics2D g2d) {
        int scaledWidth = getImage().getWidth() * 3;
        int scaledHeight = getImage().getHeight() * 3;
        g2d.drawImage(getImage(), x, y, scaledWidth, scaledHeight, null);
    }

    /**
     * 보스의 히트박스 반환 (크기 3배 확대).
     */
    @Override
    public Rectangle getHitBox() {
        int scaledWidth = getImage().getWidth() * 3;
        int scaledHeight = getImage().getHeight() * 3;
        return new Rectangle(x, y, scaledWidth, scaledHeight);
    }
}
