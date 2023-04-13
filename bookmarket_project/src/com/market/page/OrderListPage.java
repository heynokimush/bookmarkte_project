package com.market.page;

import java.awt.Rectangle;

import javax.swing.JPanel;

import com.market.dao.OrderDao;
import com.market.vo.MemberVo;
import com.market.vo.OrderVo;

public class OrderListPage extends JPanel{
	JPanel OrderPanel;
	OrderVo orderVo;
	OrderDao orderDao;
	MemberVo orderMember;
	
	public OrderListPage(JPanel panel, OrderVo orderVo, OrderDao orderDao, MemberVo orderMember) {
		 this.orderVo = orderVo;
		 this.orderDao = orderDao;
		 this.orderMember = orderMember;
		 
		 setLayout(null);

		 Rectangle rect = panel.getBounds();
		System.out.println(rect);
		setPreferredSize(rect.getSize());
			
		OrderPanel = new JPanel();
		OrderPanel.setBounds(0, 0, 700, 500);
		OrderPanel.setLayout(null);
		panel.add(OrderPanel);
	}
}
