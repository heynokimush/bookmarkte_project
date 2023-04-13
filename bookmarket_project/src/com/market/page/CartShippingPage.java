package com.market.page;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.market.commons.MarketFont;
import com.market.dao.CartDao;
import com.market.dao.DBConn;
import com.market.dao.MemberDao;
import com.market.main.MainWindow;
import com.market.vo.MemberVo;

import book_market2.CartMgm;

public class CartShippingPage extends JPanel {

	MemberVo orderMember;
	JPanel shippingPanel;
	JPanel radioPanel;
	MainWindow main;
	CartDao cartDao;
	MemberDao memberDao;
	Map<String,DBConn> daoList;

	public CartShippingPage(JPanel panel, MainWindow main, Map<String,DBConn> daoList) {
		this.main = main;
		this.daoList = daoList;
		this.cartDao = (CartDao)daoList.get("cartDao");
		this.memberDao = (MemberDao)daoList.get("memberDao");
		orderMember = memberDao.select(MainWindow.member.getMid().toUpperCase());

		setLayout(null);

		Rectangle rect = panel.getBounds();
		System.out.println(rect);
		setPreferredSize(rect.getSize());

		radioPanel = new JPanel();
		radioPanel.setBounds(300, 0, 700, 50);
		radioPanel.setLayout(new FlowLayout());
		add(radioPanel);
		JLabel radioLabel = new JLabel("배송받을 분의 고객정보와 동일합니까?");
		MarketFont.getFont(radioLabel);
		ButtonGroup group = new ButtonGroup(); //radio버튼은 그룹을 만들어 추가해놓아라
		JRadioButton radioOk = new JRadioButton("예");
		MarketFont.getFont(radioOk);
		JRadioButton radioNo = new JRadioButton("아니오");
		MarketFont.getFont(radioNo);
		group.add(radioOk);
		group.add(radioNo);
		radioPanel.add(radioLabel);
		radioPanel.add(radioOk);
		radioPanel.add(radioNo);

		shippingPanel = new JPanel();
		shippingPanel.setBounds(200, 50, 700, 500);
		shippingPanel.setLayout(null);
		add(shippingPanel);

		radioOk.setSelected(true);
		radioNo.setSelected(false);
		UserShippingInfo("yes");

		radioOk.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

				if (radioOk.isSelected()) {
					shippingPanel.removeAll();
					UserShippingInfo("yes");
					shippingPanel.revalidate();
					shippingPanel.repaint();
					radioNo.setSelected(false);
				}
			}
		});

		radioNo.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

				if (radioNo.isSelected()) {
					shippingPanel.removeAll();
					UserShippingInfo("no");
					shippingPanel.revalidate();
					shippingPanel.repaint();
					radioOk.setSelected(false);
				}
			}
		});
	}

	public void UserShippingInfo(String select) {

		JPanel namePanel = new JPanel();
		namePanel.setBounds(0, 100, 700, 50);
		JLabel nameLabel = new JLabel("고객명 : ");
		JTextField nameLabel2 = new JTextField(20);
		JPanel phonePanel = new JPanel();
		JLabel phoneLabel = new JLabel("연락처 : ");
		JTextField phoneLabel2 = new JTextField(20);
		JPanel addressPanel = new JPanel();
		JLabel label = new JLabel("배송지 : ");
		JTextField addressText = new JTextField(20);
		
		if (select=="yes") {
			
			MarketFont.getFont(nameLabel);
			namePanel.add(nameLabel);

			MarketFont.getFont(nameLabel2);
			nameLabel2.setBackground(Color.LIGHT_GRAY);
			nameLabel2.setText(orderMember.getName());
			namePanel.add(nameLabel2);
			shippingPanel.add(namePanel);

			phonePanel.setBounds(0, 150, 700, 50);
			MarketFont.getFont(phoneLabel);
			phonePanel.add(phoneLabel);

			MarketFont.getFont(phoneLabel2);
			phoneLabel2.setBackground(Color.LIGHT_GRAY);
			phoneLabel2.setText(orderMember.getPhone());
			phonePanel.add(phoneLabel2);
			shippingPanel.add(phonePanel);
			
			
			addressPanel.setBounds(0, 200, 700, 50);
			MarketFont.getFont(label);
			addressPanel.add(label);

			MarketFont.getFont(addressText);
			addressText.setBackground(Color.LIGHT_GRAY);
			addressText.setText(orderMember.getAddr());
			addressPanel.add(addressText);
			shippingPanel.add(addressPanel);
		}else {
			MarketFont.getFont(nameLabel);
			namePanel.add(nameLabel);

			MarketFont.getFont(nameLabel2);
			namePanel.add(nameLabel2); //주문고객명
			shippingPanel.add(namePanel);

			phonePanel.setBounds(0, 150, 700, 50);
			MarketFont.getFont(phoneLabel);
			phonePanel.add(phoneLabel);

			MarketFont.getFont(phoneLabel2);
			phonePanel.add(phoneLabel2); //주문고객 연락처
			shippingPanel.add(phonePanel);
			
			addressPanel.setBounds(0, 200, 700, 50);
			MarketFont.getFont(label);
			addressPanel.add(label);

			MarketFont.getFont(addressText);
			addressPanel.add(addressText); //주문고객 배송지
			shippingPanel.add(addressPanel);
		}

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 300, 700, 100);

		JLabel buttonLabel = new JLabel("주문완료");
		MarketFont.getFont(buttonLabel);
		JButton orderButton = new JButton();
		orderButton.add(buttonLabel);
		buttonPanel.add(orderButton);
		shippingPanel.add(buttonPanel);

		orderButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				String name = nameLabel2.getText();
				String phone = phoneLabel2.getText();
				String address = addressText.getText();
				System.out.println(name + "," + phone + ","+address);
				
				orderMember.setName(name);
				orderMember.setPhone(phone);
				orderMember.setAddr(address);
				
				radioPanel.removeAll();
				radioPanel.revalidate();
				radioPanel.repaint();
				shippingPanel.removeAll();

				shippingPanel.add(new CartOrderBillPage(shippingPanel, main, orderMember, cartDao));
				
				shippingPanel.revalidate();
				shippingPanel.repaint();

			}
		});
	}

}