package kr.jbnu.se.std;

import java.awt.Graphics2D;
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
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.60), -1, 20},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.65), -2, 30},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.70), -3, 40},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.78), -3, 50}   //맨 아래 오리가 너무 빨라 숫자 조정
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
    private int speed;

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



    public Duck(int x, int y, int speed, int score, BufferedImage duckImg) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.score = score;
        this.duckImg = duckImg;
    }

    /**
     * Move the duck.
     */
    public void Update() {
        if (!isStunned) {
            x += speed; // 속도에 따라 왼쪽으로 이동
        }
        updateStunStatus();
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

    public void stun() { //기절
        isStunned = true;
        stunnedStartTime = System.nanoTime();
    }

    public void updateStunStatus() {
        if (isStunned && System.nanoTime() - stunnedStartTime >= STUN_DURATION) {
            isStunned = false;
        }
    }

    public BufferedImage getImage() { //오리 이미지 반환
        return duckImg;
    }
}

