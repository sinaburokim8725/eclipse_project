package chatting.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientLogin extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField fldServerIP;
	private JTextField fldServerPort;
	private JTextField fldUserID;
	protected JButton btnRequestConnect;
	private JOptionPane pane;
	//
	private static ClientLogin clientLogin;
	//네트워크 자원 
	private Socket socket;
	private String ip;
	private int    port;
	private String id;
	
	
	
	public ClientLogin() {
		init();
		start();
	}

	/**
	 *
	 * @param msgType
	 * @param title
	 * @param msg
	 */
	private void setMessage(int msgType, String title, String msg) {
		pane.setMessage(msg);
		pane.setMessageType(msgType);
		//pane.setOptionType(JOptionPane.YES_NO_CANCEL_OPTION);

		JDialog dialog = pane.createDialog(null, title);
		//false : 모달리스 창  ture : 모달창
		dialog.setModal(false);
		dialog.setVisible(true);
	}
	//
	private void init() {
		//예외사항 발생시 띄울 메시지 창 생성
		pane  = new JOptionPane();

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
		if(e.getSource() == btnRequestConnect) {//로그인클릭

			if (fldServerIP.getText().length() == 0) {
				fldServerIP.setText("Ip 를 입력하세요.");
				fldServerIP.requestFocus();
			} else if (fldServerPort.getText().length() == 0) {
				fldServerPort.setText("Port 번호를 입력하세요");
				fldServerPort.requestFocus();
			} else if (fldUserID.getText().length() == 0) {
				fldUserID.setText("ID 를 입력하세요.");
				fldUserID.requestFocus();
			} else {
				/////////////////////////////
				btnRequestConnect.setEnabled(false);
				//ip port id 서버전송 요청
				ip   = fldServerIP.getText().trim();
				port = Integer.valueOf(fldServerPort.getText()).intValue();
				id   = fldUserID.getText();

				System.out.println("서버접속요청 :" +
						"  serverIP : "     + ip +
						"  serverPort : " + port +
						"  userID : "     + id + "\n");

				//접속승인시 로그인 화면에서 채팅화면으로 전환
				//서버사이드 소켓요청
				Socket resSocket = clientSocket(ip, port);

				//resSocket = null;
				//채팅화면 호출
				if(resSocket != null) {//연결성공
					callChatClient(resSocket);
					btnRequestConnect.setEnabled(false);
					btnRequestConnect.setText("Connected");
				} else {//연결실패시
					btnRequestConnect.setText("Failed Connect Retry");
					btnRequestConnect.setEnabled(true);
				}
				//끝
			}
		}
	}
	private void callChatClient(Socket resSocket) {
		String id = fldUserID.getText().trim();
		new Client(resSocket,id,clientLogin);
		//로그인화면닫기 보이지않게 하는것이군
		setVisible(false);
	}
	
	private Socket clientSocket(String ip , int port) {
		
		try {
			//서버 연결 소켓
			socket = new Socket( ip, port);
			
		} catch (UnknownHostException e) {

			setMessage(JOptionPane.WARNING_MESSAGE,
					"경고","연결실패");

		} catch (IOException e) {

			setMessage(JOptionPane.WARNING_MESSAGE,
					"경고","연결실패");

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
					clientLogin =  new ClientLogin();
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
	}




}
