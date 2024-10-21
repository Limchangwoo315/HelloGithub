package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The duck class.
 *
 * @author www.gametutorial.net
 */

public class Duck {

    /**
     * How much time must pass in order to create a new duck?
     */
    public static long timeBetweenDucks = Framework.secInNanosec / 2;

    /**
     * Last time when the duck was created.
     */
    public static long lastDuckTime = 0;

    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */


    public static int[][] duckLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.15)},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.30)},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.60)}, // 제일 위쪽 오리
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.65)}, // 그 다음 오리
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.70)}, // 평균 속도
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.78)}  // 맨 아래 오리
    };



    /**
     * Indicate which is next duck line.
     */
    public static int nextDuckLines = 0;

    /**
     * X coordinate of the duck.
     */
    public int x;

    /**
     * Y coordinate of the duck.
     */
    public int y;

    /**
     * How fast the duck should move? And to which direction?
     */
    protected int speed;

    /**
     * How many points this duck is worth?
     */
    public int score;

    /**
     * kr.jbnu.se.std.Duck image.
     */
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

    private boolean isStunned = false; // 기절 여부를 나타내는 변수
    private long stunnedStartTime = 0; // 기절 시작 시간
    private static final long STUN_DURATION = 1500000000L; // 1.5초 (나노초)
    private int originalSpeed; // 원래 속도를 저장하는 변수



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
    public void Update() {
        updateStunStatus(); // 기절 상태 업데이트

        if (!isStunned) {
            adjustSpeedBasedOnY(); // 기절 상태일 때 속도를 0으로 설정
        } else {
            speed = 0; // 기절 상태에 상관없이 y값에 따라 속도 조정
        }

        x += speed; // 속도에 따라 위치 이동
    }

    public void adjustSpeedBasedOnY() {
        if (y < Framework.frameHeight * 0.15){
            speed = -5;
        } else if (y < Framework.frameHeight * 0.30){
            speed = -4;
        } else if (y < Framework.frameHeight * 0.58) {
            speed = -4; // 제일 위쪽 오리의 속도를 더 빠르게
        } else if (y < Framework.frameHeight * 0.65) {
            speed = -3; // 그 다음 오리의 속도를 조금 빠르게
        } else if (y < Framework.frameHeight * 0.7) {
            speed = -3; // 평균 속도
        } else {
            speed = -2; // 맨 아래 오리의 속도를 느리게
        }
    }


    /**
     * Draw the duck to the screen.
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d) {
        g2d.drawImage(duckImg, x, y, null);
    }

    // (최고 점수) 메서드: 이 메서드는 현재 점수를 반환합니다.
    public int getScore() { // (최고 점수)
        return score; // (최고 점수)
    } // (최고 점수)

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

    public Rectangle getHitBox() {
        return new Rectangle(x, y, duckImg.getWidth(), duckImg.getHeight());
    }
}

