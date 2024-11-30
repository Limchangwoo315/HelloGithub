package kr.jbnu.se.std;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        // 로그인 화면 설정
        setTitle("로그인");
        setSize(400, 180);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 패널 생성
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        // 이메일 입력 필드
        panel.add(new JLabel("이메일:"));
        emailField = new JTextField();
        panel.add(emailField);

        // 비밀번호 입력 필드
        panel.add(new JLabel("비밀번호:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // 로그인 버튼
        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                loginUser(email, password);
            }
        });
        panel.add(loginButton);

        // 패널을 프레임에 추가
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Firebase 인증을 통한 로그인 처리
    private void loginUser(String email, String password) {
        try {
            // Firebase REST API URL
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyDJgZtL7mCJDT7nPi93IuF7dn2soLFo77c");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // JSON 요청 본문
            String jsonInputString = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

            // 요청 본문을 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 로그인 성공 후 게임 화면 열기
                JOptionPane.showMessageDialog(this, "로그인 성공!");

                // 로그인 후 Window (게임 화면) 띄우기
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Window window = new Window();  // 로그인 후 메인 게임 화면 열기
                        window.setLoginSuccess(true);  // 로그인 성공 플래그 설정
                        dispose();  // 현재 LoginFrame (로그인 화면) 닫기
                    }
                });
            } else {
                // 400번 오류 처리
                if (responseCode == 400) {
                    JOptionPane.showMessageDialog(this, "로그인 실패! 잘못된 요청입니다. 이메일과 비밀번호를 다시 확인하세요.");
                } else {
                    JOptionPane.showMessageDialog(this, "로그인 실패! 코드: " + responseCode);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "에러 발생: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginFrame();  // 로그인 화면 생성
    }
}
