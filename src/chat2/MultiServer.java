package chat2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
	
	public static void main(String[] args) {
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String s = "";//클라이언트의 메세지를 저장
		String name = "";//클라이언트의 이름을 저장

		try {
			//9999번으로 포트번호를 설정하여 서버를 생성하고 클라이언트의 접속을 기다린다.
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");

			////....접속대기중....
			
			//클라이언트가 접속요청을 하면 accept() 메소드를 통해 받아들인다.
			socket = serverSocket.accept();
			//서버->클라이언트 측으로 메세지를 전송(출력)하기 위한 스트림을 생성
			out = new PrintWriter(socket.getOutputStream(), true);
			//클라이언트 로부터 메세지를 받기위한 스트림을 생성
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			/*
			 클라이언트가 서버로 전송하는 최초의 메세지는 "대화명" 이므로 
			 메세지를 읽은후 변수에 저장하고 클라이언트 쪽으로 Echo해준다.
			 */
			if(in != null) {
				name = in.readLine();
				System.out.println(name+"접속");
				out.println("> "+name+"님이 접속했습니다.");
			}
/*
 두번째 메세지부터는 실제 대화내용이므로 읽어와서 로그로 출력하고 
 동시에 클라이언트로 Echo한다.
 */
			while (in != null) {
				// 클라이언트가 보낸 메세지를 라인단위로 읽어온다.
				s = in.readLine();
				if (s == null) {
					break;
				}
				System.out.println(name + " ==>" + s);
				out.println("> " + name + " ==>" + s);
			}
			// 콘솔에 종료메세지를 출력
			System.out.println("Bye...!!!");
		} catch (Exception e) {
			System.out.println("예외1:"+e);
		} finally {
			try {
				//입출력스트림 종료
				in.close();
				out.close();
				//소켓종료(=자원반납)
				socket.close();
				serverSocket.close();

			} catch (Exception e) {
				System.out.println("예외2:"+e);
			}
		}
	}
}
