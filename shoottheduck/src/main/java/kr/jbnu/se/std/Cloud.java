package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Cloud class for fog effect.
 */
public class Cloud {
    private BufferedImage cloudImage;
    private int x, y;
    private int speed;
    private boolean isVisible; // 구름의 표시 여부
    private Random random;

    public Cloud(int frameWidth, int frameHeight) {
        loadCloudImage();
        random = new Random();
        resetPosition(frameWidth, frameHeight);
        this.isVisible = false; // 초기에는 구름이 보이지 않도록 설정
    }

    private void loadCloudImage() {
        try {
            URL cloudImageUrl = this.getClass().getResource("/images/fog.png");
            cloudImage = ImageIO.read(cloudImageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (isVisible) {
            x -= speed; // 구름을 왼쪽으로 이동
            if (x < -cloudImage.getWidth()) {
                // 화면을 벗어나면 다시 오른쪽에서 시작
                resetPosition(Framework.frameWidth, Framework.frameHeight);
            }
        }
    }

    public void draw(Graphics2D g2d) {
        if (isVisible) { // 구름이 보일 때만 그리기
            // 구름의 투명도 설정
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f)); // 70% 투명도
            g2d.drawImage(cloudImage, x, y, null);

            // 원래 투명도로 되돌리기
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    // 구름의 위치를 재설정하는 메서드
    public void resetPosition(int frameWidth, int frameHeight) {
        // 화면 오른쪽 밖에서 시작
        x = frameWidth + random.nextInt(500);
        // 화면 상단 절반에 위치
        y = random.nextInt(frameHeight / 2);
        // 속도 범위 설정 (1~3)
        speed = 1 + random.nextInt(3);
    }

    // 구름의 표시 여부를 설정하는 메서드
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
}
