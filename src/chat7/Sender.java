package chat7;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread {//서버
	Socket socket;
	PrintWriter out = null;
	String name;

	public Sender(Socket socket, String name) {
		this.socket = socket;
		try {
			out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
			this.name = name;
		} catch (Exception e) {
			System.out.println("예외>Sender>생성자:" + e);
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		String s2;
		try {
			out.println(name);

			while (out != null) {
				try {

					while (true) {//enter 입력시 null값이 무한 출력되는 에러를 해결
						Scanner s = new Scanner(System.in);

						s2 = s.nextLine();
						if (!(s2.isEmpty())) {
							break;
						}
					}
					if (s2.equalsIgnoreCase("Q")) {// q 누르면 채팅 탈출
						break;
					} else {
						out.println(s2);
					}

				} catch (Exception e) {
					System.out.println("예외>Sender>run1:" + e);
					e.printStackTrace();
				}
			}
			out.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("예외>Sender>run2:" + e);
			e.printStackTrace();
		}
	}
}
