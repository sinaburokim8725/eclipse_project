package chatting.ui;

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
	private Vector vc = new Vector();
	
	
	
	public Server() {
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
			int port = Integer.valueOf(fldPort.getText()).intValue();
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
				//사용자 닉네임
				nickName = dis.readUTF();
				areMessage.append(nickName + "님  접속! ♬ \n");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
				msg = dis.readUTF();
				
				areMessage.append(nickName + "님의 말쌈 > " + msg + "\n");
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
		}
		
		private void sendMessage(String str) {
			try {
				
				
				dos.writeUTF(str);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
	}
	
	
}
