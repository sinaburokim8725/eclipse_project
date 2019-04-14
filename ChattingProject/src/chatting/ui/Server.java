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
 *
 *이클립스에서 수정함
 * 인테리 j에서 수정함.
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
	private Vector vcRoomList;

	//예외발생시 메시지 창.
    private JOptionPane pane;


	public Server() {
		vcUserList = new Vector();
		vcRoomList = new Vector();
		//ui 초기 생성한다.
		init();
		//이벤트 등록
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
	private void init() {
		//예외발생 통보 메시지창
        pane = new JOptionPane();

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
		areMessage.setEnabled(false);
		areMessage.setDisabledTextColor(Color.blue);
		
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
                /**
				JOptionPane.showMessageDialog(null,
						"이미 포트가 열려있습니다.\n" + "PORT 번호 > " + port,
						"경고",
						JOptionPane.WARNING_MESSAGE);
                **/
                setMessage(JOptionPane.WARNING_MESSAGE, "경고",
                        "이미 포트가 열려있습니다.\n" + "PORT 번호 > " + port);
				
				e1.printStackTrace();
			} finally {
				
				if(serverSocket != null) {
					//포트필드 수정금지
					fldPort.setEnabled(false);
					fldPort.setDisabledTextColor(Color.red);
					//서버실행버튼 enable 속성 false
					btnServerRun.setEnabled(false);
					//서버중지버튼 enable 속성 true
					btnServerStop.setEnabled(true);
				}
			}
		} else if(e.getSource() == btnServerStop) {//서버중지
			System.out.println("서버중지");

			try {
				//접속된 사용자 전원 끊기
				closeUserSocket();
				//소켓을 닫음 (이후 접속할수 없어나 이전에 접속한 유저는 접속상태유지).
				serverSocket.close();
				vcUserList.removeAllElements();
				vcRoomList.removeAllElements();
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally {
				//화면내의 컴포넌트 속성변경
				fldPort.setEnabled(true);
				fldPort.setDisabledTextColor(Color.black);
				btnServerStop.setEnabled(false);
				btnServerRun.setEnabled(true);
			}
		}
	}

	/**
	 * 접속유저 연결끊기
	 */
	private void closeUserSocket() throws IOException {
		for (int i = 0; i < vcUserList.size(); i++) {
			((UserInfo)vcUserList.elementAt(i)).getUserSocket().close();
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
						/**
					    JOptionPane.showMessageDialog(null,
								"Accept Error 발생",
								"에러",
								JOptionPane.ERROR_MESSAGE);
						**/
                        setMessage(JOptionPane.ERROR_MESSAGE, "에러",
                                "Accept Error");

                        areMessage.append("서버포트닫힘... \n");


						e.printStackTrace();
						break;
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
		private String roomList;
		
		//생성자 
		public UserInfo(Socket userSocket) {
			
			this.userSocket = userSocket;
			
			stremConfig();
			
		}
		
		private void stremConfig( ) {
			try {

				is = userSocket.getInputStream();
				//ObjectInputStream ois = new ObjectInputStream(is);
				dis= new DataInputStream(is);
				os = userSocket.getOutputStream();
				dos= new DataOutputStream(os);
				//사용자 닉네임 setter로 세팅
				setNickName(dis.readUTF());
				
				//nickName = dis.readUTF();
				areMessage.append(getNickName() + "님  접속! ♬ \n");
				
				//추가 :  기존 접속된 사용자들에게 새로운 사용자 알림. 향후 별도의 메쏘드로 관리
				brodCast(NEW_USER + ";" + nickName);
				
				//신규유저에게 기존접속자 목록을 통보함.
				for(int i = 0; i < vcUserList.size(); i++) {
					
					UserInfo u = (UserInfo)vcUserList.elementAt(i);
					//System.out.println("기존접속자: "+ u.getNickName() + "\n");
					sendMessage(OLD_USER + ";" + u.getNickName().trim());
					
				}//noticeUserList();


				
				//추가 2018.3.4 : 기존접속자에게 자신의 접속을 알려준후 자신도 기존접속자 목록에 추가한다.
				vcUserList.add(this);
				//클라이언트 화면 리스트 갱신 요청;
				brodCast("USER_LIST_UPDATE;");

				//추가 2019.3.6 : 채팅방 목록을 송신한다.
				setRoomList();
				sendMessage(roomList);
				//채팅방목록 통지
				//brodCast(getRoomList());
				//JList 컴포넌트 업데이트
				sendMessage("ROOM_LIST_UPDATE");

			} catch (IOException e) {
				/**
			    JOptionPane.showMessageDialog(null,
						"stream 설정 에러",
						"에러",
						JOptionPane.ERROR_MESSAGE);
                **/

                setMessage(JOptionPane.ERROR_MESSAGE, "에러",
                        "Stream Config Error");

				e.printStackTrace();
			}
			
		}

		/**
		 *
		 */
		private void setRoomList() {
			//=====================
			roomList = "ROOM_LIST";
			//추가 2019.3.6 : 채팅방 목록을 송신한다.메소드화
			for (int i = 0; i < vcRoomList.size(); i++) {

				RoomInfo roomInfo = (RoomInfo) vcRoomList.elementAt(i);

				roomList += ";" + roomInfo.getRoomName();
			}
			System.out.println("채팅방리스트 > " + roomList);
		}
		/*
		 * 
		 */
		private void brodCast(String str) {
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
				
				if (reciveMessage()) {
					break;
				}
			}
			
		}
		
		private boolean reciveMessage() {
			//에러발생시 반복문 탈출코드
			boolean exitCode = false;

			try {
				
				String msg = "";
				//msg 접속한 사용자로부터 수신한 메시지
				msg = dis.readUTF();
				
				areMessage.append(nickName + "님의 채팅글 > " + msg + "\n");
				
				//클라이언트로 부터 들어오는 메시지를 헤드별로 구분해서 처리한다.
				if(msg != null) {
					if(msg.split(";")[0].equals("NOTE")) {
						searchNoteUser(msg);
					} else if (msg.split(";")[0].equals("CREATE_ROOM")) {//채팅방 만들기
						createRoom(msg);

					} else if (msg.split(";")[0].equals("CHAT")) {//참여한방 인원들에게 채팅 메시지 전송
						//================
						// 수신형식: CHAT;입장한방;전송자;전송내용
						//참여방 찾아서 그방속의 인원들에게 채팅글 전송
						for (int i = 0; i < vcRoomList.size(); i++) {
							RoomInfo roomInfo = (RoomInfo) vcRoomList.elementAt(i);
							String roomName = roomInfo.getRoomName();
							if (msg.split(";")[1].equals(roomName)) {
								for (int j = 0; j < roomInfo.getVcRoomUserList().size(); j++) {
									UserInfo userInfo = (UserInfo) roomInfo.getVcRoomUserList().elementAt(j);
									userInfo.sendMessage(msg);
								}
							}

						}

						//================
					} else if (msg.split(";")[0].equals("JOIN_ROOM")) {//채팅방 참여하기
						/**
						 * 1.접속한 방을 찾는다.
						 * 2.방인원에게 ID 입장을 알린다.
						 * 99.찾은 방에 사용자 정보를 등록한다.
						 * 3.참여인원 목록 송신
						 *
						 * <방만들기>
						 *     2.참여인원 제한하기
						 *     3.공개방 비공개방(비밀번호 설정)
						 *     1.채팅방만들때 성별체크하기
						 * </방만들기>
						 */
						//수신형식: JOIN_ROOM;방이름;ID
						//======================
						System.out.println("채팅방 참여하기 Server ==> 송신");
						String jRoom = msg.split(";")[1];
						String uId = msg.split(";")[2];
						//신규참여 인원 클라이언트 채팅화면 설정을위한 참여인원 목록 송신
						String joinUserList = "";

						System.out.println("참여방 > " + jRoom + "\t신규참가자 > " + uId);

						boolean isFind = false;
						for (int i = 0; i < vcRoomList.size(); i++) {

							RoomInfo rInfo = (RoomInfo) vcRoomList.elementAt(i);

							//접속방찾기
							if (rInfo.getRoomName().equals(jRoom)) {
								//id 입장하기 보내기
								for (int j = 0; j < rInfo.getVcRoomUserList().size(); j++) {
									//19.3.7 수정: i 를 j로 수정 항상주의할것
								    UserInfo u = (UserInfo) rInfo.getVcRoomUserList().elementAt(j);
									//참여구성원들에게 보내는 송신형식 : JOIN_NEW_USER;신규입장인원
                                    System.out.println("채팅방 참여인원 > " + u.getNickName());
									u.sendMessage("JOIN_NEW_USER" + ";" + uId);
									//채팅방 참여시 클라이언트 화면설정 정보
									//채팅방 참여인원
									joinUserList += u.getNickName()+ ";";

								}
								//찾은 방에 사용자 정보를 등록한다
                                rInfo.setVcRoomUserList(this);

								//방참여인원 채팅방 초기화면 설정용 정보 송신
								//참여한 자신의 화면에 보내는 송신형식:
								//SELF_SCREEN_CONFIG;참여방;채팅방인원
								System.out.println("SELF_SCREEN_CONFIG Server 송신 => ");
								sendMessage("SELF_SCREEN_CONFIG;"+jRoom + ";" +joinUserList);


								//for구문 탈출코드
								isFind = true;
							}
							if (isFind) {
								break;
							}
						}//for
						//========끝============
					}
				}
				
			} catch (IOException e) {
				//클라이언트 접속이 예고 없이 끊어졌을경우:
				try {
					/**
				    JOptionPane.showMessageDialog(null,
							"통신장애 사용자 접속끊김",
							"알림",
							JOptionPane.INFORMATION_MESSAGE);
                     **/
                    setMessage(JOptionPane.ERROR_MESSAGE, "에러",
                            "통신장애 "+getNickName()+"님 비정상 접속종료!\n");

                    //TextArea에 통보
					areMessage.append(getNickName()+ "님 비정상 접속종료!\n");
					dos.close();
					dis.close();
					is.close();
					os.close();
					userSocket.close();


				} catch (Exception e1) {
					e.printStackTrace();
				}finally {
					/**
					 * 접속이 끊김으로 해야할일
					 * 1.접속자 명단에서 삭제
					 * 2.전체접속자에게 통보
					 * 3.만일 채팅중이었다면
					 *   3.1 채팅방 참연인원중 한명이라면
					 *   채팅방 참여 명단에서 삭제
					 *   채팅방 참여 인원에게 삭제통보(님이 퇴장했습니다.)
					 *   3.2 방장이었고 참여인원이 있을경우
					 *   임의 방장위임(첫번째 멤버에게 ) 방장권한 자동위임.
					 *   명단에서 삭제
					 *   채팅인원에게 통보
					 *   방장권한 위임정보 통보.
					 *   3.3 방에 혼자일경우 방삭제
					 */
					vcUserList.remove(this);
					//송신형식 USER_OUT;접속끊긴유저
                    System.out.println("Server >> "+ "USER_OUT" + ";" + getNickName());
					brodCast("USER_OUT" + ";" + getNickName());
					brodCast("USER_LIST_UPDATE");

					exitCode = true;
				}
				//e.printStackTrace();

			}finally {

				return exitCode;
			}
		}
		//클라이언트의 다양한 요청은 헤드로 구분해서 헤드별 지침에 따라 작업한다.

		//기능별 서버작업 시작
		//===============================

		/**
		 *
		 * @param msg
		 */
		private void createRoom(String msg) {
            //수신정보:CREATE_ROOM;방이름
            System.out.println("채팅방 만들기 Server createRoom(ms) 수신정보:CREATE_ROOM;방이름");
		    String roomName = null;
			if (msg.split(";").length >= 2) {
				roomName = msg.split(";")[1];

			}
			boolean isRoom = false;
			//1.같은 방이존재하는지 확인
			for (int i = 0; i < vcRoomList.size(); i++) {
				RoomInfo ri = (RoomInfo) vcRoomList.elementAt(i);
				//
				if (ri.getRoomName().equals(roomName)) {//방이존재할경우
					isRoom = true;
					//sendMessage("CREATE_ROOM_FAIL" + ";");
					break;
				}
			}
			//채팅방 존재여부에 따른 작업
			if (isRoom) {//존재할경우
				sendMessage("CREATE_ROOM" + ";" + "HEAD_FAIL");
			} else {//존재하지 않을경우
				//새로운 채팅방 개설
				RoomInfo newRoom = new RoomInfo(roomName, this);
				//kim


				//나자신을 포함한 모든접속자들에게 채팅방 목록 통지
				brodCast("ROOM_LIST" + ";" + roomName );

				//채팅방 목록업데이트
				brodCast("ROOM_LIST_UPDATE");

				//개설자에게 방개설 통지
				sendMessage("CREATE_ROOM" + ";" + roomName);

				//Vector 채팅방 리스트에 추가 한다.
				boolean isAdd = vcRoomList.add(newRoom);
			}

		}

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
		//===============================


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

		public String getRoomList() {
			return roomList;
		}

		public void setRoomList(String roomList) {
			this.roomList = roomList;
		}

		public Socket getUserSocket() {
			return userSocket;
		}
		//===========================
	}//end class User


	//////////////////////////////////
	////내부클래스
	/////////////////////////////////

	/**
	 * 기능:
	 * 1.유저가 만든 채팅방 접속유저에게 통보 및 채팅방 목록란에 표시한다.
	 *
	 */
	class RoomInfo {

		//채팅방 이름
		private String roomName;
		//채팅방에 참여한 접속자들의 정보
		private Vector vcRoomUserList;
		//
		private UserInfo userInfo;

		//방장 : 처음에는 방을 만들 이지만
		//어떤 사유든지 방장을 넘겨줘야 할 사유가 생길수 있음
		//방장은 다음 방장을 선출할 권한이 있다. roomManager
		private String roomManager;

		public RoomInfo(String roomName, UserInfo useInfo) {
			this.roomName = roomName;
			this.userInfo = useInfo;
			vcRoomUserList = new Vector();
			config();
		}

		private void config() {
			//방장설정
			setRoomManager(userInfo.getNickName());
			//채팅방참여 접속자 설정
			setVcRoomUserList(userInfo);

		}

		public String getRoomName() {
			return roomName;
		}

		public void setRoomName(String roomName) {
			this.roomName = roomName;
		}

		public Vector getVcRoomUserList() {
			return vcRoomUserList;
		}

		public void setVcRoomUserList(UserInfo userInfo) {
			vcRoomUserList.add(userInfo);
		}

		public String getRoomManager() {
			return roomManager;
		}

		public void setRoomManager(String roomManager) {
			this.roomManager = roomManager;
		}
	}



}
