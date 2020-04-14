package chat7;

import java.net.Socket;
import java.util.Scanner;

public class MultiClient {

	public static void main(String[] args) {

		String s_name;

		while (true) {//enter 입력시 null값이 무한 출력되는 에러를 해결
			Scanner scanner = new Scanner(System.in);
			System.out.print("이름을 입력하세요");
			s_name = scanner.nextLine();

			if (!(s_name.isEmpty())) {
				break;
			}
		}

		try {
			String SeverIP = "localhost";
			// 클라이언트 실행시 매개변수가 있는경우 아이피로 설정함
			if (args.length > 0) {
				SeverIP = args[0];
			}
			Socket socket = new Socket(SeverIP, 9999);
			System.out.println("서버와 연결되었습니다...");

			// 서버에서 보내는 Echo메세지를 클라이언트에 출력하기 위한 쓰레드 생성
			Thread receiver = new Receiver(socket);
			receiver.start();

			// 클라이언트의 메세지를 서버로 전송해주는 쓰레드 생성
			Thread sender = new Sender(socket, s_name);
			sender.start();

		} catch (Exception e) {
			System.out.println("예외발생[MultiClient]" + e);
		}
	}
}
