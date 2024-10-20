package kr.jbnu.se.std;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public RegisterFrame() {
        // 회원가입 화면 설정
        setTitle("회원가입");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // 회원가입 버튼
        JButton registerButton = new JButton("회원가입");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                registerUser(email, password);
            }
        });
        panel.add(registerButton);

        // 패널을 프레임에 추가
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Firebase 인증을 통한 회원가입 처리
    private void registerUser(String email, String password) {
        try {
            // Firebase REST API URL
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyDJgZtL7mCJDT7nPi93IuF7dn2soLFo77c");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // 이메일과 비밀번호가 제대로 입력되었는지 확인
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "이메일과 비밀번호를 모두 입력해주세요.");
                return;
            }

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
                // 회원가입 성공
                JOptionPane.showMessageDialog(this, "회원가입 성공!");
                dispose();  // 회원가입 창 닫기
            } else {
                // 400번 오류 처리
                if (responseCode == 400) {
                    JOptionPane.showMessageDialog(this, "회원가입 실패! 잘못된 요청입니다. 이메일과 비밀번호를 다시 확인하세요.");
                } else {
                    JOptionPane.showMessageDialog(this, "회원가입 실패! 코드: " + responseCode);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "에러 발생: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegisterFrame();  // 회원가입 화면 생성
    }
}
