package chatting.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextField;
import javax.swing.JButton;

public class ClientLogin extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField fldServerIP;
	private JTextField fldServerPort;
	private JTextField fldUserID;
	private JButton btnRequestConnect;
	
	//네트워크 자원 
	private Socket socket;
	private String ip;
	private int    port;
	private String id;
	
	
	
	public ClientLogin() {
		init();
		start();
	}
	
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 381, 514);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Server IP");
		lblNewLabel.setFont(new Font("Consolas", Font.BOLD, 12));
		lblNewLabel.setBounds(52, 224, 86, 25);
		contentPane.add(lblNewLabel);
		
		JLabel lblServerPort = new JLabel("Server Port ");
		lblServerPort.setFont(new Font("Consolas", Font.BOLD, 12));
		lblServerPort.setBounds(52, 292, 105, 25);
		contentPane.add(lblServerPort);
		
		JLabel lblId = new JLabel("ID");
		lblId.setFont(new Font("Consolas", Font.BOLD, 12));
		lblId.setBounds(52, 357, 105, 25);
		contentPane.add(lblId);
		
		fldServerIP = new JTextField();
		fldServerIP.setText("127.0.0.1");
		fldServerIP.setBounds(139, 224, 173, 21);
		contentPane.add(fldServerIP);
		fldServerIP.setColumns(10);
		
		fldServerPort = new JTextField();
		fldServerPort.setText("12345");
		fldServerPort.setColumns(10);
		fldServerPort.setBounds(139, 292, 173, 21);
		contentPane.add(fldServerPort);
		
		fldUserID = new JTextField();
		fldUserID.setText("1_chatUser");
		fldUserID.setColumns(10);
		fldUserID.setBounds(139, 357, 173, 21);
		contentPane.add(fldUserID);
		
		btnRequestConnect = new JButton("접  속  요  청");
		btnRequestConnect.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
		btnRequestConnect.setBounds(52, 407, 260, 30);
		contentPane.add(btnRequestConnect);
		this.setVisible(true);
	}
	
	private void start() {
		btnRequestConnect.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnRequestConnect) {
			btnRequestConnect.setEnabled(false);
			//ip port id 서버전송 요청
			ip   = fldServerIP.getText().trim();
			port = Integer.valueOf(fldServerPort.getText()).intValue();
			id   = fldUserID.getText();
			
			System.out.println("  serverIP : "     + ip +
					           "  serverPort : " + port +
							   "  userID : "     + id + "\n서버접속요청");
			
			//접속승인시 로그인 화면에서 채팅화면으로 전환
			//서버사이드 소켓요청
			Socket resSocket = clientSocket(ip, port);
			
			//resSocket = null;
			//채팅화면 호출
			if(resSocket != null) {//연결성공
				callChatClient(resSocket);
				
				btnRequestConnect.setText("Connected");
			} else {//연결실패시 
				btnRequestConnect.setText("Failed Connect Retry");
				btnRequestConnect.setEnabled(true);
			}
			
			
			//로그인화면 close
			//this.setDefaultCloseOperation(HIDE_ON_CLOSE);
			//this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			//this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			
			
		}
		
	}
	private void callChatClient(Socket resSocket) {
		String id = fldUserID.getText().trim();
		new Client(resSocket,id);
	}
	
	private Socket clientSocket(String ip , int port) {
		
		try {
			//서버 연결 소켓
			socket = new Socket( ip, port);
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally {
			return socket;
		}
	}
	
	//=======================
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ClientLogin();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}



	
}
