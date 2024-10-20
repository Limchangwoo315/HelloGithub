package kr.jbnu.se.std;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame {

    private boolean isLoggedIn = false;  // 로그인 상태 추적용 변수

    public Window() {
        // 윈도우 제목 설정
        this.setTitle("Shoot the Duck");

        // 윈도우 크기 설정
        if (false) { // 전체화면 모드
            this.setUndecorated(true);
            this.setExtendedState(this.MAXIMIZED_BOTH);
        } else {
            this.setSize(800, 600);  // 기본 크기 800x600
            this.setLocationRelativeTo(null);  // 화면의 중앙에 위치
            this.setResizable(false);  // 크기 조정 불가
        }

        // 창 닫을 때 프로그램 종료
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 게임 UI 화면 설정 (프레임워크 클래스에 게임 관련 내용을 넣음)
        this.setContentPane(new Framework());

        // 로그인 및 회원가입 버튼을 보이도록 설정
        if (!isLoggedIn) {
            addLoginAndRegisterButtons();
        } else {
            removeLoginAndRegisterButtons();
        }

        // 레이아웃을 사용하지 않음 (위치 수동 설정)
        this.setLayout(null);
        this.setVisible(true);
    }

    // 로그인 성공 시 로그인/회원가입 버튼을 없애고 게임 화면으로 전환
    private void removeLoginAndRegisterButtons() {
        // 로그인 및 회원가입 버튼을 화면에서 제거
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JButton) {
                this.remove(component);
            }
        }
        this.repaint();  // 버튼이 제거된 후 화면 업데이트
    }

    // 로그인 및 회원가입 버튼 추가
    private void addLoginAndRegisterButtons() {
        // 회원가입 버튼
        JButton registerButton = new JButton("회원가입");
        registerButton.setBounds(650, 500, 120, 30);
        this.add(registerButton);

        // 회원가입 버튼 클릭 시 처리
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterFrame registerFrame = new RegisterFrame();
                registerFrame.setVisible(true);
            }
        });

        // 로그인 버튼
        JButton loginButton = new JButton("로그인");
        loginButton.setBounds(650, 450, 120, 30);  // 위치 설정
        this.add(loginButton);

        // 로그인 버튼 클릭 시 처리
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();  // 로그인 화면 객체 생성
                loginFrame.setVisible(true);  // 로그인 창 띄우기
                dispose();  // 로그인 창을 열고 현재 Window를 닫음
            }
        });
    }

    // 로그인 상태를 설정하는 메서드
    public void setLoginSuccess(boolean success) {
        this.isLoggedIn = success;
        // 로그인 성공 시 UI 갱신
        if (success) {
            removeLoginAndRegisterButtons();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}
