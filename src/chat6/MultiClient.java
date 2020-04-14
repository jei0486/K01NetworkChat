package chat6;
 
import java.net.Socket;
import java.util.Scanner;

import chat7.Sender;

public class MultiClient {

	public static void main(String[] args) {
		
		System.out.print("이름을 입력하세요");
		Scanner scanner = new Scanner(System.in);
		String s_name = scanner.nextLine();
		
		//Sender가 기능을 가져가므로 여기서는 필요없음
		//PrintWriter out = null;
		
		//Receiver가 기능을 가져가므로 여기선 불필요함
		//BufferedReader in = null;
		
		try {
			/*
			 c:\> java 패키지명.MultiClient 접속할 IP주소
			 	=> 위와같이 하면 해당 IP주소로 접속할 수 있다.
			 	만약 IP주소가 없다면 localhost(127.0.0.1)로 접속된다.
			 */
			String SeverIP = "localhost";
			//클라이언트 실행시 매개변수가 있는경우 아이피로 설정함
			if(args.length > 0) {
				SeverIP = args[0];
			}
			Socket socket = new Socket(SeverIP,9999);
			System.out.println("서버와 연결되었습니다...");
			
			//서버에서 보내는 Echo메세지를 클라이언트에 출력하기 위한 쓰레드 생성
			Thread receiver = new Receiver(socket);
			receiver.start();
			
			//클라이언트의 메세지를 서버로 전송해주는 쓰레드 생성
			Thread sender = new Sender(socket, s_name);
			sender.start();
			
		}catch (Exception e) {
			System.out.println("예외발생[MultiClient]"+e);
		}
	}
}
