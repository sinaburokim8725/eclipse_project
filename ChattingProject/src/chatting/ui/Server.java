package chatting.ui;
/**
 * 개발 노트
 * 목적:
 * 멀티쓰레드 환경에서 다자간 채팅 기능 구현
 * 
 * 진척상항
 * 1.멀티쓰레드 구현
 * 2.채팅화면에서 전체 접속자  목록 보여주기 구현
 * 	2.1 현상:
 * 		1 JList 목록뵤여주기에서 간혹 접속자가 목록에 나타나지 않을 경우가 있음
 *	2.2 방안:
 *		1.아직은 완전한 해결을  하지 못함 JList의 특성을 좀덕 파악할 필요가 있음
 *3.개발 계획:
 *	1.전체 접속자 목록에서 선택한 접속자에게 쪽지 보내는 기능을 구현한다.(초기완료)
 *	2.채팅방 만들기 기능을 구현 한다.
 *	3.채팅방에 참여 기능 구현.
 *	기타 향후 개발하면서 필요한 기능들은 추가한다. 
 *4.개선사항
 *	1.메시지를 구분자와 함께 문자열로 만들어서 데이터 스트림을 쏘는 데 오브젝트로 스트림을 쏘고싶다 구분자로 받아서 잘라서 비교하고 전송할때 다시 구분넣고 하는 것이 비효율적이다.<string.split이 아주뛰어난 기능을한다.> 
 *5.issue
 *00001. JList에 리스트 추가할때 가끔씩 화면에 리스트가 보이지 않는다.
 *6.fix
 *0001.초기 리스트 추가할필요 없는 곳에 컴포넌트에 아이템을 추가하는 것을 
 *업데이할때 한꺼번에 하게 수정했다. 
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Server extends JFrame implements ActionListener{

	private JPanel contentPane;
	
	//fld : JTextField
	private JTextField fldPort;
	//are : TextArea
	JTextArea areMessage;
	
	//btn : JButton
	JButton   btnServerRun; 
	JButton   btnServerStop;
	
	
	//NetWork 자원
	private ServerSocket serverSocket;
	private Socket reqSocket;
	
	
	//송신 메시지 헤드
	private static String NEW_USER = "NEW_USER";
	private static String OLD_USER = "OLD_USER";
	
	private Vector vcUserList;
	
	public Server() {
		vcUserList = new Vector();
		//ui 초기 생성한다.
		init();
		//이벤트 등록
		start();	
	}
	
	private void init() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 371, 506);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 331, 348);
		contentPane.add(scrollPane);
		
		areMessage = new JTextArea();
		scrollPane.setViewportView(areMessage);
		
		JLabel label = new JLabel("포토번호");
		label.setBounds(12, 379, 55, 27);
		contentPane.add(label);
		
		fldPort = new JTextField();
		fldPort.setText("12345");
		fldPort.setBounds(82, 382, 261, 24);
		contentPane.add(fldPort);
		fldPort.setColumns(10);
		
		btnServerRun = new JButton("서버실행");
		btnServerRun.setBounds(12, 416, 165, 23);
		contentPane.add(btnServerRun);
		
		btnServerStop = new JButton("서버중지");
		btnServerStop.setEnabled(false);
		btnServerStop.setBounds(178, 416, 165, 23);
		contentPane.add(btnServerStop);
		this.setVisible(true);
	}
	
	private void start() {
		btnServerRun.addActionListener(this);
		btnServerStop.addActionListener(this);

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnServerRun) { //서버실행
			//System.out.println("서버실행 ");
			//System.out.println("포토번호 :" + port);
			//int Integer.parseInt(s);
			int port = Integer.valueOf(fldPort.getText().trim()).intValue();
			try {
				runServerSocket(port);
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
			} finally {
				
				if(serverSocket != null) {
					btnServerRun.setEnabled(false);
					btnServerStop.setEnabled(true);
				}
			}
		} else if(e.getSource() == btnServerStop) {//서버중지
			System.out.println("서버중지");
			
			btnServerStop.setEnabled(false);
			//btnServerRun.setEnabled(true);
		}
	}
	
	//소켓연결
	private void runServerSocket(int port) throws IOException{
		//매개변수:포트번호
		serverSocket = new ServerSocket(port);
		
		//ServerSocket 역할:1.소캣생성 ,2.소캣주소 할당,3.연결요청 대기상태
		if(serverSocket != null) {//정상적으로 포토가 열렸을경우
			
			Connection();
			
		}
	}
	private void Connection() {
		
		
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {//메인쓰레드 외의 동시작업할 것을 기재한다.
				
				while (true) {//멀티채팅을위해서 클라이언트 접속요청을 무한대기 하면서 연결소켓을 만들어준다. 
					//클라이언트의 연경요청을 허용한다. 
					//여기서 주목:현재 접속승인을 위해 클라이언트의 연결요청을 무한정 대기중이다.
					//메인쓰레드만 사용할경우 하나의 작업이 끝이나야 다른작업을 할수있는데 여기선 무한대기중이라
					//ui작업(실행시켜 서버실행버튼누른후화면정지현상)을 하지못한다. 해결방안 : 메인쓰레드가 ui 작업을을 하게하고 
					//대기작업을 별도의 쓰레드에서 작업하게 한다.
					try {
						areMessage.append("접속요청 대기중...... \n");
						//4.연결허용여부 확인후 결정
						reqSocket = serverSocket.accept();
						areMessage.append("접속허용 ★ \n");
						
						UserInfo userInfo = new UserInfo(reqSocket);
						userInfo.start();//각유저별 쓰레드를 생성한다.
								
						
						
						//communMessage();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
				}
			}
		});
		
		th.start();
	}
	/**삭제
	private void communMessage() {
		
			
		reciveMessage();
		//sendMessage("접속확인했습니다 --관리자!♥");
	}
	**/
	
	//======================
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Server();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//=======================================
	////////////////////////////////////////
	//     내부클래스 : 사용자 정보
	///////////////////////////////////////
	class UserInfo extends Thread {
		private Socket userSocket;
		private String nickName;
		private InputStream is;
		private DataInputStream dis;
		private OutputStream os;
		private DataOutputStream dos;
		
		
		//생성자 
		public UserInfo(Socket userSocket) {
			
			this.userSocket = userSocket;
			
			netConfig();
			
		}
		
		private void netConfig( ) {
			try {
				is = userSocket.getInputStream();
				dis= new DataInputStream(is);
				os = userSocket.getOutputStream();
				dos= new DataOutputStream(os);
				//사용자 닉네임 setter로 세팅
				setNickName(dis.readUTF());
				//nickName = dis.readUTF();
				areMessage.append(getNickName() + "님  접속! ♬ \n");
				
				//추가 :  기존 접속된 사용자들에게 새로운 사용자 알림. 향후 별도의 메쏘드로 관리
				brodcast(NEW_USER + ";" + nickName);
				
				//신규유저에게 기존접속자 목록을 통보함.
				
				for(int i = 0; i < vcUserList.size(); i++) {
					UserInfo u = (UserInfo)vcUserList.elementAt(i);
					System.out.println("기존접속자: "+ u.getNickName() + "\n");
					sendMessage(OLD_USER + ";" + u.getNickName().trim());
					
				}
				
				//noticeUserList();
				
				//추가 2018.3.4 : 기존접속자에게 자신의 접속을 알려준후 자신도 기존접속자 목록에 추가한다.
				vcUserList.add(this);
				brodcast("USER_LIST_UPDATE;");
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		/*
		 * 
		 */
		private void brodcast(String str) {
			//추가 19.3.4:기존 접속된 사용자들에게 새로운 사용자 알림. 향후 별도의 메쏘드로 관리
			for (int i = 0; i < vcUserList.size(); i++) {
				//
				UserInfo u = (UserInfo)vcUserList.elementAt(i);
				//구분자 : ; <== 로한다.
				u.sendMessage(str);
				
			}
			
			//끝
		}
		
		//notice
		private void noticeUserList() {
			
			for(int i = 0; i < vcUserList.size(); i++) {
				UserInfo u = (UserInfo)vcUserList.elementAt(i);
				//System.out.println("기존접속자: "+ u.getNickName() + "\n");
				this.sendMessage(OLD_USER + ";" + u.getNickName().trim());
				
			}
			//System.out.println("noticeUserList() end ");
			/*
			 * 개선사항:
			 * 1.새로운 접속이 생길때마다 기준접속자 수만큼 소켓통신을한다.
			 * 
			 * 해결방안 :
			 * 1.새로운 접속이 생길때마다 기준접속자 목록을 한꺼번에 보낸다.
			 * 즉 서버의 오버헤드를 줄이고 클라이언트의 자원 사용하게끔 한다.
			*/
		}
		
		@Override
		public void run() {//쓰레드에서 처리할 내용
			
			while(true) {//클라이언트메시지 수신 무한대기
				
				reciveMessage();
			}
			
		}
		
		private void reciveMessage() {
			try {
				
				
				String msg = "";
				//msg 접속한 사용자로부터 수신한 메시지
				msg = dis.readUTF();
				
				areMessage.append(nickName + "님의 말쌈 > " + msg + "\n");
				
				//클아이언트로 부터 들어오는 메시지를 헤드별로 구분해서 처리한다.
				if(msg != null) {
					if(msg.split(";")[0].equals("NOTE")) {
						searchNoteUser(msg);
					}
				}
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		//클라이언트의 다양한 요청은 헤드로 구분해서 헤드별 지침에 따라 분류한다.
		//구분하다 : Distinguish
		private void searchNoteUser(String msg) {
			//양식은 > 헤드;fromid;toId;메시지
			String head = null,fromId = null,toId = null,note = null;
			
			if(msg != null) {
				head   = msg.split(";")[0];
				fromId = msg.split(";")[1];
				toId   = msg.split(";")[2];
				note   = msg.split(";")[3];
				//System.out.println("head : " + head +" fromId : " + fromId + " toId : " + toId + " 쪽지내용 : " + note );
				//다음단계: to 유저에게 메시지를 전달한다.
				for (int i = 0; i < vcUserList.size(); i++ ){
					UserInfo u = (UserInfo)vcUserList.elementAt(i);
					
					System.out.println("쪽지 수신 > " + u.getNickName() + "   toId > " + toId ) ;
					if(u.getNickName().equals(toId)) {
						
						u.sendMessage(head + ";" + fromId + ";" + note);
					}
				}
			}
		}
		/**
		 * 
		 * @param str : 송신할 메시지
		 */
		private void sendMessage(String str) {
			try {
				
				
				dos.writeUTF(str);
				dos.flush();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}

//////////////////////////////
//get set 메쏘드
/////////////////////////////
		
		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		
		//===========================
	}//end class UserInfo
}
