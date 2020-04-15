package chat7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class MultiServer {

	static ServerSocket serverSocket = null;
	static Socket socket = null;
	// 클라이언트 정보 저장을 위한 Map 컬렉션 정의
	Map<String, PrintWriter> clientMap;

	// 생성자
	public MultiServer() {
		// 클라이언트의 이름과 출력스트림을 저장할 HashMap 생성
		clientMap = new HashMap<String, PrintWriter>();
		// HashMap 동기화 설정. 쓰레드가 사용자 정보에 동시에 접근하는 것을 차단한다.
		Collections.synchronizedMap(clientMap);
	}

	// 서버의 초기화를 담당할 메소드
	public void init() {

		try {
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");

			while (true) {
				socket = serverSocket.accept();

				// 클라이언트의 메세지를 모든 클라이언트에게 전달하기 위한 쓰레드 생성 및 start.
				Thread mst = new MultiServerT(socket);
				mst.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		MultiServer ms = new MultiServer();
		ms.init();
	}

	// 접속된 모든 클라이언트에게 메세지를 전달하는 역할의 메소드
	public void sendAllMsg(String name, String msg) {

		// Map에 저장된 객체의 키값(이름)을 먼저 얻어온다.
		Iterator<String> it = clientMap.keySet().iterator();

		// 저장된 객체(클라이언트)의 갯수만큼 반복한다.
		while (it.hasNext()) {
			try {
				// 각 클라이언트의 PrintWriter객체를 얻어온다.
				PrintWriter it_out = (PrintWriter) clientMap.get(it.next());

				// 클라이언트에게 메세지를 전달한다.
				/*
				 매개변수 name이 있는경우에는 이름+메세지 
				 매개변수 name이 없는경우에는 메세지만 클라이언트로 전달한다.
				 */
				if (name.equals("")) {
					it_out.println(msg);
				} else {
					it_out.println("[" + name + "]:" + msg);
				}
			} catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}
	}//// sendAllMsg

	// 내부클래스
	class MultiServerT extends Thread {

		// 멤버변수
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;

		// 생성자 : Socket을 기반으로 입출력 스트림을 생성한다.
		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			} catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}
//		public void toWhisper(String fname, String wmsg) {//귓속말 1회용
//			// fname : 상대방이름 sname : 내이름 wmsg : 메세지
//			// Map에 저장된 객체의 키값(이름)을 먼저 얻어온다.
//			Iterator<String> it = clientMap.keySet().iterator();
//
//			// 저장된 객체(클라이언트)의 갯수만큼 반복한다.
//			while (it.hasNext()) {
//				try {
//					// 변수 sname에 it.next()를 통해 가져온 이름을 저장한다.
//					String sname = it.next();
//					// 각 클라이언트의 PrintWriter객체를 얻어온다.
//					PrintWriter it_out = (PrintWriter) clientMap.get(sname);// it.next()대신 sname을 넣어준다
//					// 왜냐하면 it.next()는 쓸때마다 다음 값을 반환하기 때문에
//					// it.next()를 두번 쓰면 안됨 !! sname 변수를 사용하여 값을 넣어준다.
//
//					if (fname.equals(sname)) {
//						it_out.println(wmsg);
//					}
//				} catch (Exception e) {
//					System.out.println("예외:" + e);
//				}
//			}
//		}
		public void toWhisper(String sendname,String msg) {// 귓속말 (1회용 & 고정) /to 대화명 대화내용
			// 보내는 사람 : sendname //받는사람 : receivename

			int start = msg.indexOf(" ");// 첫번째 blank// 시작값에 a를 넣으면 blank값이 들어감 그래서 a+1
			int end = msg.indexOf(" ", start + 1);// 두번째 blank //a+1부터 " "을 찾아줘ㅠㅠ
			Iterator<String> it;
			if (end != -1) {// 귓속말 1회용
				String receivename = msg.substring(start + 1, end);
				String whisper = msg.substring(end);
				it = clientMap.keySet().iterator();
				while (it.hasNext()) {
					try {
						// 변수 comparename에 it.next()를 통해 가져온 이름을 저장한다.
						String comparename = it.next();
						// 각 클라이언트의 PrintWriter객체를 얻어온다.
						PrintWriter it_out = (PrintWriter) clientMap.get(comparename);
						// it.next()대신 comparename을 넣어준다
						// 왜냐하면 it.next()는 쓸때마다 다음 값을 반환하기 때문에
						// it.next()를 두번 쓰면 안됨 !! comparename 변수를 사용하여 값을 넣어준다.

						if (receivename.equals(comparename)) {
							new InsertQuery(receivename, "귓속말"+whisper).execute();
							it_out.println(whisper);
						}
					} catch (Exception e) {
						System.out.println("예외:" + e);
					}
				}
			} else if (end == -1) {// 귓속말 고정
				try {
					while (true) {
						it = clientMap.keySet().iterator();
						String whisper = in.readLine();
						if(whisper.equalsIgnoreCase("x")) {
							break;
						}
						while (it.hasNext()) {
							String receivename = msg.substring(start + 1);
							String comparename = it.next();
							PrintWriter it_out = (PrintWriter) clientMap.get(comparename);
							if (receivename.equals(comparename)) {
								new InsertQuery(receivename, whisper).execute();
								it_out.println(whisper);
							}
						}
					}
				} catch (Exception e) {
					System.out.println("예외:" + e);
				}
			}
		}
		@Override
		public void run() {
			// 클라이언트로부터 전송된 "대화명"을 저장할 변수
			String name = "";
			// 메세지 저장용 변수
			String s = "";

			try {
				
				while (true) {
					// 클라이언트의 이름을 읽어와서 저장
					name = in.readLine();
					new InsertQuery(name).check();
					//System.out.println(comparename);
					break;
				}
				
				
				
				
				// 접속한 클라이언트에게 새로운 사용자의 입장을 알림.
				// 접속자를 제외한 나머지 클라이언트만 입장메세지를 받는다.
				sendAllMsg("", name + "님이 입장했습니다.");

				// 현재 접속한 클라이언트를 HashMap에 저장한다.
				clientMap.put(name, out);

				// HashMap에 저장된 객체의 수로 접속자수를 파악할 수 있다.
				System.out.println(name + " 접속");
				System.out.println("현재 접속자 수는 " + clientMap.size() + "명 입니다.");

				// 입력한 메세지는 모든 클라이언트에게 Echo 된다.
				while (in != null) {
					s = in.readLine();

					if (s == null) {
						break;
					}
					// 명령어 찾기 : /를 통해서
					if (s.charAt(0) == '/') {
						int a = s.indexOf(" ");// 첫번째 blank
						
						if (s.equalsIgnoreCase("/list")) {// 서버의 접속자 리스트 /list
							Iterator<String> it = clientMap.keySet().iterator();
							while (it.hasNext()) {
								System.out.println("접속자리스트 :" + it.next());
							}
						}
						else if(a==-1) {
							// /to 만 입력하고 enter쳤을때 null이 반복되는것을 막는 코드
						}
						else if (s.substring(0, a).equals("/to")) {// 귓속말 => /to(a)대화명(b)대화내용
							
							toWhisper(name,s);
//							if (!(b==-1)) {//망한 코드
//								String fname = s.substring(a + 1, b);// 시작값에 a를 넣으면 blank값이 들어감 그래서 a+1
//								String wmsg = s.substring(b);
//								toWhisper(fname, wmsg);//귓속말 1회용
//							}
//								else {
//								String fsname = s.substring(a + 1);
//								conWhisper(fsname);//귓속말 고정
//							}
						}
					} else {
						System.out.println(name + " >> " + s);
						// DB처리는 여기서!//클라이언트에게 Echo해준다.
						// s = URLDecoder.decode(name,"UTF-8");// 인코딩문 때문에 s가 if문에 못들어감 밑으로 쭉 내렸음
						new InsertQuery(name, s).execute();
						sendAllMsg(name, s);
					}
				}
			} catch (Exception e) {
				// System.out.println("예외:" + e);//퇴장시 예외뜨는것 주석처리
			} finally {
				/*
				 클라이언트가 접속을 종료하면 예외가 발생하게 되어 finally로 넘어오게 된다. 
				 이때 "대화명"을 통해 remove()시켜준다.
				 */
				clientMap.remove(name);
				sendAllMsg("", name + "님이 퇴장하셨습니다.");
				// 퇴장하는 클라이언트의 쓰레드명을 보여준다.->
				System.out.println(name + "퇴장");// <- thread명 메세지 삭제
				System.out.println("현재 접속자 수는 " + clientMap.size() + "명 입니다.");

				try {
					in.close();
					out.close();
					socket.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}//// end of run()
	}//// end of MultiServerT
}
