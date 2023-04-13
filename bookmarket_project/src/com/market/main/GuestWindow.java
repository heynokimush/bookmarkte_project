package com.market.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.market.commons.MarketFont;
import com.market.dao.MemberDao;
import com.market.vo.MemberVo;

public class GuestWindow extends JFrame implements ActionListener {
	MemberDao memberDao;
	JTextField midField;
	JPasswordField pwField;
	JButton enterButton, resetButton, exitButton;

	public GuestWindow(String title, int x, int y, int width, int height) {
		memberDao = new MemberDao();
		initContainer(title, x, y, width, height);
		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //X를 누르면 jvm이 자동으로 종료해줌 - 시간 텀이 걸림
		setIconImage(new ImageIcon("./images/shop.png").getImage());
	}

	private void initContainer(String title, int x, int y, int width, int height) {
		setTitle(title);
		setBounds(x, y, width, height);
		setLayout(null); //borderlayout 그대로 사용하겠다.
		getContentPane().setBackground(Color.PINK);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //dimension: 정 가운데 띄우기
		setLocation((screenSize.width - 1000) / 2, (screenSize.height - 750) / 2); //위치 결정, 띄우기

		JPanel userPanel = new JPanel();
		userPanel.setBounds(0, 100, 1000, 256);
		userPanel.setBackground(Color.PINK);

		ImageIcon imageIcon = new ImageIcon("./images/mainicon.png");
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
		JLabel userLabel = new JLabel(imageIcon);
		userPanel.add(userLabel);
		add(userPanel);

		JPanel titlePanel = new JPanel();
		titlePanel.setBounds(0, 350, 1000, 50);
		titlePanel.setBackground(Color.PINK);
		add(titlePanel);

		JLabel titleLabel = new JLabel("로그인 정보를 입력해주세요");
		MarketFont.getFont(titleLabel);
		titleLabel.setForeground(Color.WHITE);
		titlePanel.add(titleLabel);

		JPanel namePanel = new JPanel();
		namePanel.setBounds(0, 400, 1000, 50);
		namePanel.setBackground(Color.PINK);
		add(namePanel);

		JLabel nameLabel = new JLabel("아 이 디 : ");
		MarketFont.getFont(nameLabel);
		namePanel.add(nameLabel);

		midField = new JTextField(10);
		MarketFont.getFont(midField);
		namePanel.add(midField);

		JPanel phonePanel = new JPanel();
		phonePanel.setBounds(0, 450, 1000, 50);
		phonePanel.setBackground(Color.PINK);
		add(phonePanel);

		JLabel phoneLabel = new JLabel("패스워드: ");
		MarketFont.getFont(phoneLabel);
		phonePanel.add(phoneLabel);

		pwField = new JPasswordField(10);
		MarketFont.getFont(pwField);
		phonePanel.add(pwField);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 500, 1000, 100);
		buttonPanel.setBackground(Color.PINK);
		add(buttonPanel);
		
		ImageIcon retryIcon = new ImageIcon("images/retry.png");
		Image reimg = retryIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		retryIcon = new ImageIcon(reimg);
		JLabel buttonLabel2 = new JLabel("  다시쓰기  ", retryIcon, JLabel.LEFT);
		MarketFont.getFont(buttonLabel2);
		resetButton = new JButton();
		resetButton.add(buttonLabel2);
		buttonPanel.add(resetButton);
		
		ImageIcon loginIcon = new ImageIcon("images/login.png");
		Image loimg = loginIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		loginIcon = new ImageIcon(loimg);
		JLabel buttonLabel1 = new JLabel("  로그인  ", loginIcon, JLabel.LEFT);
		MarketFont.getFont(buttonLabel1);
		enterButton = new JButton();
		enterButton.add(buttonLabel1);
		buttonPanel.add(enterButton);
		
		ImageIcon exitIcon = new ImageIcon("images/exit.png");
		Image eximg = exitIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		exitIcon = new ImageIcon(eximg);
		JLabel buttonLabel3 = new JLabel("  쇼핑종료  ", exitIcon, JLabel.LEFT);
		MarketFont.getFont(buttonLabel3);
		exitButton = new JButton();
		exitButton.add(buttonLabel3);
		buttonPanel.add(exitButton);
		
		pwField.addActionListener(this);
		enterButton.addActionListener(this);
		resetButton.addActionListener(this);
		exitButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==pwField || obj==enterButton) {
			JLabel message = new JLabel("사용자 정보 에러");
			MarketFont.getFont(message);

			if (midField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요");
				midField.requestFocus();
			}else if(pwField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "패스워드를 입력해주세요");
				pwField.requestFocus();
			}else {
				String mid = midField.getText().trim().toUpperCase();
				String pw = pwField.getText().trim().toLowerCase();
				
				if(memberDao.select(mid, pw)==1) {
					MemberVo member = new MemberVo();
					member.setMid(midField.getText().trim());
					member.setPw(pwField.getText().trim());
					dispose();
					setVisible(false);
					
					Map param = new HashMap();
					param.put("title", "온라인 서점");
					param.put("x", 0);
					param.put("y", 0);
					param.put("width", 1000);
					param.put("height", 750);
					param.put("member", member);
					param.put("memberDao", memberDao);
					
					new MainWindow(param);
				} else {
					//로그인실패
					String msg = "아이디 또는 패스워드가 다릅니다.\n다시 입력해주세요.";
					JOptionPane.showMessageDialog(null, msg);
					midField.setText("");
					pwField.setText("");
					midField.requestFocus();
				}
		
			}
		} else if(obj==resetButton) {
			midField.setText("");
			pwField.setText("");
			midField.requestFocus();
			
		} else if(obj==exitButton) {
			int select = JOptionPane.showConfirmDialog(exitButton, "쇼핑을 종료하시겠습니까?");
			if(select==0) System.exit(0);
		}
	}

}