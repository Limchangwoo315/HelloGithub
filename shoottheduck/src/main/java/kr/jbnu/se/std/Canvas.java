package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Create a JPanel on which we draw and listen for keyboard and mouse events.
 *
 * @author www.gametutorial.net
 */

public abstract class Canvas extends JPanel implements KeyListener, MouseListener {

    // 인스턴스 필드로 변경
    private boolean[] keyboardState = new boolean[525];  // 크기를 적절하게 설정
    private static boolean[] mouseState = new boolean[3];  // 마우스 버튼 상태 저장 (static 필드로 유지)

    public Canvas() {
        // Double buffering
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);

        // 커서 처리 (마우스 커서 숨김)
        if (true) {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
            this.setCursor(blankCursor);
        }

        // 이벤트 리스너 추가
        this.addKeyListener(this);
        this.addMouseListener(this);
    }

    // 추상 메서드, 구체적인 화면 그리기 로직은 자식 클래스에서 정의
    public abstract void draw(Graphics2D g2d);

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        draw(g2d);
    }

    // 키보드 상태
    public boolean keyboardKeyState(int key) {
        return keyboardState[key];
    }

    // 키보드 이벤트 리스너
    @Override
    public void keyPressed(KeyEvent e) {
        keyboardState[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyboardState[e.getKeyCode()] = false;
        keyReleasedFramework(e);
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    // 키가 해제되었을 때의 동작을 처리하는 추상 메서드
    public abstract void keyReleasedFramework(KeyEvent e);

    // 마우스 상태
    public static boolean mouseButtonState(int button) {
        return mouseState[button - 1];  // 1, 2, 3에 맞는 인덱스를 반환
    }

    // 마우스 버튼 상태 설정
    private static void mouseKeyStatus(MouseEvent e, boolean status) {
        if (e.getButton() == MouseEvent.BUTTON1)
            mouseState[0] = status;
        else if (e.getButton() == MouseEvent.BUTTON2)
            mouseState[1] = status;
        else if (e.getButton() == MouseEvent.BUTTON3)
            mouseState[2] = status;
    }

    // 마우스 이벤트 리스너
    @Override
    public void mousePressed(MouseEvent e) {
        mouseKeyStatus(e, true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseKeyStatus(e, false);
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
