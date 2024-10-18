package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * Cloud class for fog effect.
 */
public class Cloud {
    private BufferedImage cloudImage;
    private int x, y;
    private boolean isVisible; // 구름의 표시 여부
    private boolean visible;

    public Cloud() {
        loadCloudImage();
        this.x = Framework.frameWidth / 2 - cloudImage.getWidth() / 2;
        this.y = Framework.frameHeight / 2 - cloudImage.getHeight() / 2;
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

    public void draw(Graphics2D g2d) {
        if (isVisible) { // 구름이 보일 때만 그리기
            System.out.println("Drawing cloud at: " + x + ", " + y); // 디버깅용 로그

            // 구름의 투명도 설정
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f)); // 70% 투명도
            g2d.drawImage(cloudImage, x, y, null);

            // 원래 투명도로 되돌리기
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

    }

    // 구름의 표시 여부를 설정하는 메서드
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
}
