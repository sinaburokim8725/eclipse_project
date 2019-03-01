package chatting.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Server extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
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
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 371, 506);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 331, 348);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JLabel label = new JLabel("포토번호");
		label.setBounds(12, 379, 55, 27);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setText("12345");
		textField.setBounds(82, 382, 261, 24);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("서버실행");
		button.setBounds(12, 416, 165, 23);
		contentPane.add(button);
		
		JButton button_1 = new JButton("서버중지");
		button_1.setEnabled(false);
		button_1.setBounds(178, 416, 165, 23);
		contentPane.add(button_1);
	}
}
