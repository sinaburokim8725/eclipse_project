package chatting.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalIconFactory;

import org.omg.CORBA.ObjectHolder;

import chatting.ui.Server.UserInfo;
import compont.jlist.IconListRenderer;

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
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
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
	//사용자 id
	private String id;
	private String test;
	
	//소켓프로그램
	private Socket 				resSocket;//서버연결소켓
	private String 				ip;			
	private int    				port;
	private InputStream 		is;
	private OutputStream 		os;
	private DataInputStream 	dis;
	private DataOutputStream	dos;
	
	//전체 젒속자란에 표시할 접속자들의 리스트를 서버로부터 수신 목록을 담아둔다.
	//CCU(Concurrent Users): 동시접속자
	Vector vcCCUList;
	
	Vector vcRoomList;
	DefaultListModel listModel; 
	
	//시작
	//기타설정
	private String CONST_ME = "나";
	//서버의 송신 메시지 헤드 (조건)  내용으로 구분
	private String head = null,body = null;
	
	//송신 메시지 헤드
	private static String NEW_USER = "NEW_USER";
	private static String OLD_USER = "OLD_USER";
	
	//기타설정
	StringTokenizer st;

	//내가 참여한 방이름
	private String myRoom;
	//끝
	 
	public Client() {
		/*
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					init(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		**/
	}
	
	public Client(Socket resSocket,String id) {
		this.resSocket = resSocket;
		this.id = id;
		//this.test = "나" + this.id; 
		vcCCUList  = new Vector();
		
		vcRoomList = new Vector();
		listModel  = new DefaultListModel();
		
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
			
			//최초 접속시 id 전송
			sendMessage(id);
			//전체접속자 란에 자기를 표시한다.id 대신에 자기를 표현할 나 로 한다.
			vcCCUList.add(id);

			//추가 2019.3.6
			//fix00001
			//lstAllContactPerson.setListData(vcCCUList);
			
			//debug
			//System.out.println("vcCCUList.size() : " + vcCCUList.size());
			/**
			for(int i = 0 ; i < vcCCUList.size(); i++) {
				String u = (String)vcCCUList.elementAt(i);
				System.out.println("접속자목록 :" + u + "\n");
			}
			**/
			
			//listModel.addElement(vcCCUList);
			//jList1.setModel(listModel);
			//lstAllContactPerson.setModel(listModel);
			
			//수정:텍스트 영역에 뿌려줌
			areTalkPart.append("관리자  > " + id + "님 접속을 환영합니다.!!♡" + "\n");
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					//서버메시지 수신 대기(언제올지 모르니 무한대기한다.
					while (true) {
						reciveMessage();
						
					}
				}
			});//th
			th.start();
		}//if
	}
	//송신 메시지 
	private void sendMessage(String str){
		
		try {
			//서버에 메시지 송신할 상태 만듬
			os = resSocket.getOutputStream();
			dos= new DataOutputStream(os);
			
			dos.writeUTF(str);
			//dos.flush();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	//수신 메시지
	private void reciveMessage() {
		
		try {
			//서버메시지 수신할 상태 만듬
			is = resSocket.getInputStream();
			dis= new DataInputStream(is);
			
			String msg = "";
			msg = dis.readUTF();
			
			//시작
			//추가 2019.3.4 월요일 :전체 접속자 란에 추가할 접속자명
			//Prepar => preparation:준비작업 
			//전체접속자 목록 사전작업
			//setPreparMessage(msg);
			/**
			 * 개선사항 : 1.향후 서버의 송신 메시지에 따라 분류해서 처리할 필요있음.
			 * 기능별분리  1.전체접속자 목록출력  2.수신한 쪽지 내용 출력 
			 */
			if(msg != null) {
				System.out.println("헤드문자 수신 Client>> " + msg.split(";")[0]);
				
				if(msg.split(";")[0].equals("NEW_USER") || msg.split(";")[0].equals(OLD_USER)) {//접속자목록 출력작업

					//접속자 목록을 출력한다.
					setContactUserList(msg);
					
				}else if(msg.split(";")[0].equals("NOTE")) {//수신된 쪽지 내용 출력

					//수신 쪽지 내용 출력
					showNote(msg);
				}else if (msg.split(";")[0].equals("ROOM_LIST")) {

					//채팅방 목록을 파싱해서 채팅방 목록 vector에 추가한다.
					setRoomList(msg);

				} else if (msg.split(";")[0].equals("CREATE_ROOM")) {//방개설
					if (msg.split(";").length >= 2) {

						if (msg.split(";")[1].equals("HEAD_FAIL")) {

							JOptionPane.showMessageDialog(null, "개설된 방입니다.", "알림", JOptionPane.ERROR_MESSAGE);
						} else {//성공적으로 방개설 되었을때
							/*
							고려사항:
							1.채팅방 입장했다면 화면전환이 필요할것이다.
							1.1 좌측 상단 전체접속자란 변경사항없음
							1.2 쪽지보내기 변경사항없음
							1.3 좌측 하단 개설된방이름 > 방참여 인원란으로
							1.4 방개설 버튼 > 참여인원들중에서 선택 비밀글 보내기
							1.5 참여하기 > 나가기 버튼(방장일경우 방장 권한 넘긴후 나가기
							1.6 만일 방장 혼자일 때 나가기 버튼 클릭시:
							개설된 방은 없어지고 접속화면으로 나온다.
							1.7 참여인원들 중에 방장 권한 넘기기(방장권한)
							 */
							//개설된 방이름: 채팅글전송시 서버에서
							//채팅방 참여인원에게 채팅글 전송하기위한 용도.
							myRoom = msg.split(";")[1];

							//최초 채팅방 개설자가 자신이 만든 채팅방에
							//입장한 상태를 상정해서 메시지를 뿌린다.
							//채팅창 초기화(개설된 방의 채팅창
							areTalkPart.setText("");

							//관리자
							areTalkPart.append("관리자 > " + id + "님께서 만드신 " + myRoom + "으로 이동했습니다." + "\n");
							areTalkPart.append("관리자 > " + "비매너 채팅인원에게는 방장으로써 퇴장조치 할 수있는 권한이 있습니다." + "\n");



						}

					}
				} else if (msg.split(";")[0].equals("CHAT")) {//채팅글일경우
					//수신형식 => CHAT;입장한방;전송자;전송내용
					String id = msg.split(";")[2];
					String m  = msg.split(";")[3];
					areTalkPart.append(id + " > " + m + "\n");
					//채팅글 필드 전송메시지 삭제
					fldMessage.setText("");


				} else if (msg.split(";")[0].equals("USER_LIST_UPDATE")) {//추가 fix00001

					//동작이 안되는 경우가 있다. 학인해볼것
					//lstAllContactPerson.updateUI();
					lstAllContactPerson.setListData(vcCCUList);

				} else if (msg.split(";")[0].equals("ROOM_LIST_UPDATE")) {
					//JList 컴포넌트를 업데이트 한다.
					lstChatRoomList.setListData(vcRoomList);
				} else if (msg.split(";")[0].equals("JOIN_NEW_USER")) {//방참여
					//수신형식: JOIN_NEW_USER;ID
					/**
					 * 1.id님께서 입장하셨습니다.
					 * 2.채팅중인데 새로입장 메시지가 채팅 중간 와서는안된다.
					 *
					 */
					System.out.println("JOIN_NEW_USER Client");
					System.out.println("신규유저 > " + msg);
					String m = msg.split(";")[1];
					areTalkPart.insert(m + "님께서 입장하셨습니다.\n", 0);

				} else if (msg.split(";")[0].equals("SELF_SCREEN_CONFIG")) {//채팅방 최초 입장후 화면설정
					System.out.println("채팅방 참여인원 > " + msg);

					//수신형식: SELF_SCREEN_CONFIG;참여방;채팅방인원
					myRoom = msg.split(";")[1];
					/**
					String[] temp = msg.split(";");
					String[] joinPeople = new String[0];

					//참여인원목록
					int j = 0;
					for (int i = 2; i < temp.length; i++) {
						System.out.println("temp[" + i + "] " + temp[i]);
						if(temp[i] != null){
							joinPeople[j] = temp[i];
							j++;
						}
					}
 **/
					//채팅방접속인원 vector가필요하다.
					//채팅방 목록에 참여인원 리스트를 뿌리고
					//Jlabel 텍스트도 >> 참 여 인 원 으로 바꾼다.

					//채팅필드 세팅
					areTalkPart.setText("");
					areTalkPart.append("관리자 > " + id + "님은 " + myRoom + "채팅방으로 이동하셨습니다.\n");
				}
				
			}
			//끝
			
			
			//수정:텍스트 영역에 뿌려줌
			//areTalkPart.append("관리자  > " + body + "님 접속을 환영합니다.!!♡" + "\n");
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	/**
	 *
	 */
	private void setRoomList(String msg) {
		String[] list;
		list = msg.split(";");
		for (int i = 1; i < list.length; i++) {
			vcRoomList.addElement(list[i]);
		}
		System.out.println("개설된 방수 > " + (list.length - 1));
	}
	/**
	 * 
	 * @param msg
	 */
	private void showNote(String msg) {
		//head + ";" + fromId + ";" + note
		areTalkPart.append("관리자  > " + msg.split(";")[1] + "님으로 부터 쪽지가 도착했습니다." + "\n");
		areTalkPart.append("관리자  > " + "쪽지 내용 : "+ msg.split(";")[2] + "\n");
		
		//메시지 다이얼로그에 메시지를 뿌린다.
		JOptionPane.showMessageDialog(null, msg.split(";")[2] , msg.split(";")[1]+"님으로 부터 쪽지가 도착했습니다.",JOptionPane.CLOSED_OPTION);
	}
	/**
	 * 추가:2019.3.4 by 김영호
	 * 기능:서버의 송신 메시지 처리
	 * @param msg : 서버의 송신 메시지.
	 */
	private void setContactUserList(String msg) {
		//구분자 분리 
		if(msg != null) {
			head = msg.split(";")[0];
			body = msg.split(";")[1];
			
			if(head.equals(NEW_USER)) {
				//추가 19.3.4 : 전체접속란에 목록 추가한다.
				vcCCUList.add(body);
				//추가 fix00001 가끔씩 리스트가 보이지 않는 오류 해결? 두고보장
				//lstAllContactPerson.setListData(vcCCUList);
				
				//lstAllContactPerson.updateUI();
				//수정:텍스트 영역에 뿌려줌
				areTalkPart.append("관리자  > " + body + "님 접속을 환영합니다.!!♡" + "\n");
				
			} else if(head.equals(OLD_USER)) {
				System.out.println("OLD_USER :" + body + "\n");
				vcCCUList.add(body);
				//추가 fix00001 : 리스트 아이템 보이지 않을경우
				//lstAllContactPerson.setListData(vcCCUList);
			} else if(head.equals("ADMIM")) {
				
			}//if
			
		}//if
	}
	/**
	private void setPreparMessage(String msg) {
		//구분자 분리 
		if(msg != null) {
			
			st = new StringTokenizer(msg, "|");
			
			for (int i = 0; st.hasMoreElements(); i++) {
				//System.out.println("i : " + i);
				switch (i) {
					case 0:
						head = st.nextToken();
						break;
					case 1:
						body = st.nextToken();
						break;
					default:
						break;
				}
			}//for
			//
			if(head.equals(NEW_USER)) {
				//추가 19.3.4 : 전체접속란에 목록 추가한다.
				vcCCUList.add(body);
				lstAllContactPerson.setListData(vcCCUList);
				
				//수정:텍스트 영역에 뿌려줌
				areTalkPart.append("관리자  > " + body + "님 접속을 환영합니다.!!♡" + "\n");
				
			} else if(head.equals(OLD_USER)) {
				System.out.println("OLD_USER :" + body + "\n");
				vcCCUList.add(body);
				//for (int i = 0; i < vcCCUList.size(); i++) {
					
				//}
				lstAllContactPerson.setListData(vcCCUList);
			} else if(head.equals("ADMIM")) {
				
			}//if
			
		}//if
	}
	**/
	
	
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
		/*JList 테스트 목록
		String[] personList = {
				"알랑이","딸랑이","말랑이","최순실","박근혜",
				"박정희","전두환","노태우","이승만","황교완",
				"박정희","전두환","노태우","이승만","황교완"
		};
		*/
		
		lstAllContactPerson = new JList();
		//리스트의 경계선 추가
		lstAllContactPerson.setBorder(BorderFactory.createLineBorder(Color.GREEN,1));
		//리스트의 단일선택모드변경
		lstAllContactPerson.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(lstAllContactPerson);
		//추가 fix00001
		lstAllContactPerson.setListData(vcCCUList);
		
		
		btnMemoSend = new JButton("쪽 지 보 내 기");
		btnMemoSend.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		btnMemoSend.setBounds(12, 237, 160, 35);
		contentPane.add(btnMemoSend);
		
		JLabel label = new JLabel("채  팅  방  목  록");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		label.setBounds(12, 282, 145, 25);
		contentPane.add(label);
		
		Map<Object, Icon> icons = new HashMap<Object, Icon>();
		icons.put("monkey", new ImageIcon("resource/image/monky.png"));
		icons.put("Penguin", new ImageIcon("resource/image/Penguin.png"));
		icons.put("pig", new ImageIcon("resource/image/pig.png"));
		icons.put("bell", new ImageIcon("resource/image/iconfinder_005_-_Bell_2792960.png"));
		icons.put("bell2", new ImageIcon("resource/image/blog_icon02.png"));
		//iconfinder_005_-_Bell_2792960
		//icons.put("folder",  MetalIconFactory.getTreeFolderIcon());
		//icons.put("computer",MetalIconFactory.getTreeComputerIcon());
		
		Object[] ob = new Object[] {
				"bell2","bell2","bell2","bell2","bell2",
				"bell2","bell2","bell2","bell2","bell2"
		};
				
		//roomList
		lstChatRoomList = new JList(ob);
		// create a cell renderer to add the appropriate icon
		lstChatRoomList.setCellRenderer(new IconListRenderer(icons));
		//
		lstChatRoomList.setCellRenderer(new IconListRenderer(icons));
		
		//Icon icon =new ImageIcon("resource/image/monky.png");
		
	
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 304, 160, 194);
		scrollPane_2.setViewportView(lstChatRoomList);
		contentPane.add(scrollPane_2);
		//추가 fix00001
		//lstChatRoomList.setListData(vcRoomList);
		
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
			//System.out.println(message);
			//전송규칙 CHAT;입장한방;전송자;전송내용
			System.out.println("채팅글 >" + "CHAT" + ";" + myRoom + ";"+ id + ";" + message);

			sendMessage("CHAT" + ";" + myRoom + ";"+ id + ";" + message);

			
			
			//수신메시지 메시지창 출력
			//areTalkPart.append("전송자 > " + message + "\n");
			
			
		}else if(e.getSource() == btnRoomCreate) { //채팅방 만들기
			/**
			 * 개선사항:
			 * 1.채팅방을 만들때
			 */
			System.out.println("채팅방 만들기");

			//채팅방 만들기 다이얼로그 박스띄우기
			String roomName = JOptionPane.showInputDialog("채팅방 이름");
			if (roomName != null) {
				//헤드정보:CREATE_ROOM
				sendMessage("CREATE_ROOM" + ";" + roomName);
			}
			
		}else if(e.getSource() == btnMemoSend) { //쪽지보내기
			//선택된 접속자에게 쪽지보내기
			//System.out.println(selectedPerson + "님에게 쪽지가 도착했습니다.\n");
			
			//선택된 접속자는  : selectedPerson
			
			//쪽지를 보낼 다이얼로그 박스를 띄운다.
			String note = JOptionPane.showInputDialog("보낼 메시지");
			
			//쪽지내용이 있을때 서버에 보낸다 양식은 > 헤드;fromid;toId;메시지
			if(note != null ) {
				//
				String fromId = this.id;
				String toId   = selectedPerson;
				//System.out.println("fromId : " + fromId + " toId : " + toId + " 쪽지내용 : " + note );
				sendMessage("NOTE"+";" + fromId + ";" + toId + ";" + note);
				
			}
			
		}else if(e.getSource() == btnJoinChatRoom) {//채팅방 참여하기
			//선택된 채팅방은 널값이 되어선 안된다.
			System.out.println("채팅방 참여하기 Client ==> 송신");
			//선택한 방으로 입장하기
			//송신형식: JOIN_ROOM;방이름;ID
			sendMessage("JOIN_ROOM" + ";" + selectedJoinRoom + ";" + id);
		}
		
	}
	//리스트 아이템 선택 이벤트
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == lstAllContactPerson ){
			//선택된 접속자 아이템 얻기
			selectedPerson = (String)lstAllContactPerson.getSelectedValue();

		}else if(e.getSource() == lstChatRoomList){
			//선택된 방가져오기
			selectedJoinRoom = (String)lstChatRoomList.getSelectedValue();
		}

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
