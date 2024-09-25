import java.util.Scanner;  // Scanner 클래스를 사용하기 위해 임포트

public class Main {
    public static void main(String[] args) {
        // Scanner 객체 생성
        Scanner scanner = new Scanner(System.in);

        // 환영 메시지 출력
        System.out.print("Hello and welcome! ");

        // 정수 입력받기
        int a = scanner.nextInt();

        // 입력받은 정수 출력
        System.out.print(a);

        // Scanner 닫기
        scanner.close();
    }
}