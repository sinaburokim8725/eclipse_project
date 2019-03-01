package chatting.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Color;
//ListSelectionListener
public class Client extends JFrame implements ActionListener,ListSelectionListener{
	
	private JPanel contentPane;
	//전송메세지
	private JTextField fldMessage;
	
	//대화영역
	JTextArea areTalkPart;
	
	//전  체  접  속  자
	JList lstAllContactPerson;
	
	//채  팅  방  목  록 JList : lst
	JList lstChatRoomList;
		
	//전송
	JButton btnDataSend;
		
	//방  만  들  기
	JButton btnRoomCreate;
	
	//채  팅  방  참  여 Participation <파티서 페이션
	JButton btnJoinChatRoom;
	
	//쪽 지 보 내 기
	JButton btnMemoSend;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	
	//선택된 접속자명
	private String selectedPerson;
	//선택된 채팅방
	private String selectedJoinRoom;
	
	
	//소켓프로그램
	private Socket 				resSocket;//서버연결소켓
	private String 				ip;			
	private int    				port;
	private InputStream 		is;
	private OutputStream 		os;
	private DataInputStream 	dis;
	private DataOutputStream	dos;
	
	 
	public Client() {
		
	}
	
	public Client(Socket resSocket) {
		this.resSocket = resSocket;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					init();
					start();
					connection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void connection() {
		if(resSocket != null) {
			try {
				sendMessage("클라이언트에서 접속했습니다.");
				reciveMessage();
				
				
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			
		}
	}
	//송신 메시지 
	private void sendMessage(String str) throws IOException{
		//서버에 메시지 송신할 상태 만듬
		os = resSocket.getOutputStream();
		dos= new DataOutputStream(os);
		
		dos.writeUTF(str);
	}
	//수신 메시지
	private void reciveMessage() throws IOException{
		//서버메시지 수신할 상태 만듬
		is = resSocket.getInputStream();
		dis= new DataInputStream(is);
		
		String msg = "";
		msg = dis.readUTF();
		areTalkPart.append(msg + "\n");
	}
	
 	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 702, 639);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("전  체  접  속  자");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		lblNewLabel.setBounds(12, 10, 145, 25);
		contentPane.add(lblNewLabel);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 33, 160, 194);
		contentPane.add(scrollPane_1);
		
		String[] personList = {
				"알랑이","딸랑이","말랑이","최순실","박근혜",
				"박정희","전두환","노태우","이승만","황교완",
				"박정희","전두환","노태우","이승만","황교완"
		};
		
		
		lstAllContactPerson = new JList(personList);
		//리스트의 경계선 추가
		lstAllContactPerson.setBorder(BorderFactory.createLineBorder(Color.GREEN,1));
		//리스트의 단일선택모드변경
		lstAllContactPerson.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(lstAllContactPerson);
		
		btnMemoSend = new JButton("쪽 지 보 내 기");
		btnMemoSend.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		btnMemoSend.setBounds(12, 237, 160, 35);
		contentPane.add(btnMemoSend);
		
		JLabel label = new JLabel("채  팅  방  목  록");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		label.setBounds(12, 282, 145, 25);
		contentPane.add(label);
		
		String[] roomList = {
				"고돌방","정치방","문화방","브라","브라브라",
				"박정희","전두환","노태우","이승만","황교완",
				"박정희","전두환","노태우","이승만","황교완"
		};
		
		//JList 에 이미지표시되고 옆에 문자도 같이 목록에 넣을 수 있는지 이것을 지원하는 다른 컴포넌트가 있는지 알아본다.
		ImageIcon[] images = {
				new ImageIcon("resource/image/blog_icon02.png","딸랑이"),
				new ImageIcon("resource/image/blog_icon02.png","딸랑이"),
				new ImageIcon("resource/image/blog_icon02.png","딸랑이"),
				new ImageIcon("resource/image/blog_icon02.png","딸랑이"),
				new ImageIcon("resource/image/blog_icon02.png","딸랑이"),
				new ImageIcon("resource/image/blog_icon02.png","딸랑이"),
				new ImageIcon("resource/image/blog_icon02.png","딸랑이"),
				new ImageIcon("resource/image/blog_icon02.png","딸랑이"),
				new ImageIcon("resource/image/blog_icon02.png","딸랑이"),
				new ImageIcon("resource/image/blog_icon02.png","딸랑이")
		};
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 304, 160, 194);
		contentPane.add(scrollPane_2);
		
		//roomList
		lstChatRoomList = new JList(images);
		scrollPane_2.setViewportView(lstChatRoomList);
		
		
		btnJoinChatRoom = new JButton("채  팅  방  참  여");
		btnJoinChatRoom.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		btnJoinChatRoom.setBounds(12, 508, 160, 35);
		contentPane.add(btnJoinChatRoom);
		
		btnRoomCreate = new JButton("방  만  들  기");
		btnRoomCreate.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		btnRoomCreate.setBounds(12, 553, 160, 36);
		contentPane.add(btnRoomCreate);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(184, 30, 491, 514);
		contentPane.add(scrollPane);
		
		areTalkPart = new JTextArea();
		areTalkPart.setText("건전한 채팅문화는 우리들의 상식  --관리자 올림\n");
		areTalkPart.append("김석균 > 머하너!♠\n");
		areTalkPart.append("김영호 > 딸친다.\n");
		scrollPane.setViewportView(areTalkPart);
		
		fldMessage = new JTextField();
		fldMessage.setText("전송할 채팅메시지");
		fldMessage.setBackground(new Color(255, 255, 153));
		fldMessage.setBounds(184, 554, 358, 35);
		contentPane.add(fldMessage);
		fldMessage.setColumns(10);
		
		btnDataSend = new JButton("전송");
		btnDataSend.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		btnDataSend.setBounds(554, 553, 121, 36);
		contentPane.add(btnDataSend);
		this.setVisible(true);
	}
	
	private void start() {
		//전송버튼
		btnDataSend.addActionListener(this);
		//방만들기
		btnRoomCreate.addActionListener(this);
		//쪽지보내기
		btnMemoSend.addActionListener(this);
		//채팅방참여
		btnJoinChatRoom.addActionListener(this);
		
		//리스트 아이템 선택이벤트 등록
		lstAllContactPerson.addListSelectionListener(this);
		lstChatRoomList.addListSelectionListener(this);
	}
	//버튼클릭이벤트
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnDataSend) { //전송
			//서버전송
			String message = fldMessage.getText();
			System.out.println(message);
			try {
				sendMessage(message);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			//수신메시지 메시지창 출력
			areTalkPart.append("전송자 > " + message + "\n");
			
			
		}else if(e.getSource() == btnRoomCreate) { //채팅방 만들기
			
			System.out.println("채탱방 만들기");
			
		}else if(e.getSource() == btnMemoSend) { //쪽지보내기
			//선택된 접속자에게 쪽지보내기
			System.out.println(selectedPerson + "님에게 쪽지가 도착했습니다.\n");
			
		}else if(e.getSource() == btnJoinChatRoom) {
			
			System.out.println(selectedJoinRoom + " 방으로 이동합니다.");
		}
		
	}
	//리스트 아이템 선택 이벤트
	@Override
	public void valueChanged(ListSelectionEvent e) {
		//선택된 접속자 아이템 얻기
		selectedPerson = (String)lstAllContactPerson.getSelectedValue();
		//선택된 방가져오기
		selectedJoinRoom = lstChatRoomList.getName();
	}
	
	
	
	//====================================================
	/**
	 * Launch the application.
	 */
	/**
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Client();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	**/

	

}
