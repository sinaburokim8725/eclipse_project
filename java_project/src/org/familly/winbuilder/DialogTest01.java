package org.familly.winbuilder;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DialogTest01 extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JButton button;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DialogTest01 frame = new DialogTest01();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DialogTest01() {
		init();
		start();
	}
	private void init(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 270, 196);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		button = new JButton("모달리스다이얼로그뛰우기");
		button.setBounds(32, 63, 196, 23);
		contentPane.add(button);
	}

	private void start() {
		button.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * JOptionPane pane = new JOptionPane(message,
		 *             JOptionPane.PLAIN_MESSAGE,
		 *             JOptionPane.YES_NO_CANCEL_OPTION);
		 */

		JOptionPane pane = new JOptionPane();

		pane.setMessage("연결실패");
		pane.setMessageType(JOptionPane.WARNING_MESSAGE);
		//pane.setOptionType(JOptionPane.OK_OPTION);


		JDialog dialog = pane.createDialog(null, "경고");
		/**
		 * true : 모달 방식이므로 자신이 닫히지 않을경우 다른곳에서 이벤트를 발수할수 없다.
		 * false : 모달리스 방식으로 자신이 닫히지않아도 다른곳의 이벤트를 가로채지 않는다.
		 */
		dialog.setModal(false);//모달리스 만드는것
		//dialog.setDefaultCloseOperation(JOptionPane.ERROR_MESSAGE);
		//dialog.setDefaultCloseOperation(JOptionPane.QUESTION_MESSAGE);
		dialog.setVisible(true);
	}
}
