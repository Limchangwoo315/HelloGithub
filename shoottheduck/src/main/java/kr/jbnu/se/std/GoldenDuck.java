package kr.jbnu.se.std;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class GoldenDuck extends Duck {
    private static final int GOLDEN_DUCK_SCORE = 100; // 황금오리의 점수
    private boolean isCaptured; // 황금오리의 포획 여부

    public GoldenDuck(int x, int y, int speed, BufferedImage duckImg) {
        super(x, y, speed, GOLDEN_DUCK_SCORE, duckImg); // 부모 클래스 Duck의 생성자 호출
        isCaptured = false; // 초기 상태는 포획되지 않은 상태
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void capture() {
        isCaptured = true; // 황금오리를 포획
    }

    @Override
    public Rectangle getHitBox() {
        // Duck 클래스의 x와 y를 사용해 충돌 영역을 반환
        return new Rectangle(this.x, this.y, this.getImage().getWidth(), this.getImage().getHeight());
    }
}
