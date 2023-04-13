package com.market.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.market.bookitem.BookInIt;
import com.market.commons.MarketFont;
import com.market.dao.BookDao;
import com.market.dao.CartDao;
import com.market.dao.DBConn;
import com.market.dao.MemberDao;
import com.market.page.AdminLoginDialog;
import com.market.page.AdminPage;
import com.market.page.CartAddItemPage;
import com.market.page.CartItemListPage;
import com.market.page.CartShippingPage;
import com.market.page.GuestInfoPage;
import com.market.page.OrderListPage;
import com.market.vo.MemberVo;

import book_market2.CartMgm;

public class MainWindow extends JFrame {
	static JPanel mMenuPanel, mPagePanel;
	public static MemberVo member;
	MemberDao memberDao;
	BookDao bookDao;
	CartDao cartDao;
	MainWindow main = this; //main 변수에 자신의 주소 대입
	
	Map<String, DBConn> daoList;

	public MainWindow(Map param) {
		bookDao = new BookDao();
		cartDao = new CartDao();
		this.member = (MemberVo)param.get("member");
		this.memberDao = (MemberDao)param.get("memberDao");
		
		daoList = new HashMap<String, DBConn>();
		daoList.put("memberDao", memberDao);
		daoList.put("bookDao", bookDao);
		daoList.put("cartDao", cartDao);
		
		String title = (String)param.get("title");
		int x = (Integer)param.get("x"), 
				y=(Integer)param.get("y"), 
				width=(Integer)param.get("width"), 
				height=(Integer)param.get("height");
		initContainer(title, x, y, width, height);

		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon("./images/shop.png").getImage());
	}
	
	public MainWindow(String title, int x, int y, int width, int height, MemberVo member) {
		this.member = member;
		this.memberDao = memberDao;
		initContainer(title, x, y, width, height);

		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon("./images/shop.png").getImage());
	}

	private void initContainer(String title, int x, int y, int width, int height) {
		setTitle(title);
		setBounds(x, y, width, height);
		setLayout(null);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - 1000) / 2, (screenSize.height - 750) / 2);

		mMenuPanel = new JPanel();
		mMenuPanel.setBounds(0, 20, width, 130);
		menuIntroduction();
		add(mMenuPanel);

		mPagePanel = new JPanel();
		mPagePanel.setBounds(0, 150, width, height);
		add(mPagePanel);
		
		//장바구니 항목 추가 화면
		mPagePanel.removeAll();
		BookInIt.init();
		mPagePanel.add(new CartAddItemPage(mPagePanel, cartDao, bookDao));
		mPagePanel.revalidate();
		mPagePanel.repaint();
	}

	private void menuIntroduction() {
		/**고객 정보 확인**/
		JButton bt1 = new JButton("마이페이지", new ImageIcon("./images/1.png"));
		bt1.setBounds(0, 0, 100, 50);
		MarketFont.getFont(bt1);
		mMenuPanel.add(bt1);


		bt1.addActionListener(e -> {
				mPagePanel.removeAll();  

				mPagePanel.add(new GuestInfoPage(mPagePanel, member, memberDao)); 
				mPagePanel.revalidate(); 
				mPagePanel.repaint(); 
		});

		/**장바구니 상품목록 출력**/
		JButton bt2 = new JButton("장바구니", new ImageIcon("./images/2.png"));
		bt2.setBounds(0, 0, 100, 30);
		MarketFont.getFont(bt2);
		mMenuPanel.add(bt2);

		bt2.addActionListener(e -> {
			
			if (cartDao.getSize(member.getMid().toUpperCase()) == 0) //장바구니 사이즈 체크
				JOptionPane.showMessageDialog(bt2, "장바구니가 비어 있습니다", "message", JOptionPane.ERROR_MESSAGE);
			else {
				
				mPagePanel.removeAll();
				mPagePanel.add( new CartItemListPage(mPagePanel, daoList));
				mPagePanel.revalidate();
				mPagePanel.repaint();

			}
		});


		/**장바구니 항목 추가하기**/
		JButton bt4 = new JButton("판매도서", new ImageIcon("./images/3.png"));
		MarketFont.getFont(bt4);
		mMenuPanel.add(bt4);
		bt4.addActionListener(e -> {

				mPagePanel.removeAll();
				BookInIt.init();
				mPagePanel.add(new CartAddItemPage(mPagePanel, cartDao, bookDao));
				mPagePanel.revalidate();
				mPagePanel.repaint();
		});

		
		/**주문하기**/
		JButton bt7 = new JButton("주문하기", new ImageIcon("./images/4.png"));
		MarketFont.getFont(bt7);
		mMenuPanel.add(bt7);

		bt7.addActionListener(e -> {

				if (cartDao.getSize(member.getMid().toUpperCase()) == 0)
					JOptionPane.showMessageDialog(bt7, "장바구니가 비어있습니다", "message", JOptionPane.ERROR_MESSAGE);
				else {

					mPagePanel.removeAll();
					mPagePanel.add(new CartShippingPage(mPagePanel, main, daoList));
					mPagePanel.revalidate();
					mPagePanel.repaint();
				}
		});
		
		/**주문내역**/
		JButton bt10 = new JButton("주문내역", new ImageIcon("./images/5.png"));
		MarketFont.getFont(bt10);
		mMenuPanel.add(bt10);
		
		bt10.addActionListener(e -> {

			int select = JOptionPane.showConfirmDialog(bt10, "로그아웃 하시겠습니까? ");

			if (select == 0) {
				mPagePanel.removeAll();
//				mPagePanel.add(new OrderListPage(mPagePanel, main, daoList));
				mPagePanel.revalidate();
				mPagePanel.repaint();
			}
	});
		
		/**로그아웃**/
		JButton bt8 = new JButton("로그아웃", new ImageIcon("./images/5.png"));
		MarketFont.getFont(bt8);
		mMenuPanel.add(bt8);

		bt8.addActionListener(e -> {

				int select = JOptionPane.showConfirmDialog(bt8, "로그아웃 하시겠습니까? ");

				if (select == 0) {
					setVisible(false);
					new GuestWindow("온라인 서점", 0, 0, 1000, 750);
				}
		});

		if(member.getMid().equals("ADMIN")) {
			JButton bt9 = new JButton("관리자", new ImageIcon("./images/6.png"));
			MarketFont.getFont(bt9);
			mMenuPanel.add(bt9);

			bt9.addActionListener(e -> {
				mPagePanel.removeAll();
				mPagePanel.add(new AdminPage(mPagePanel, bookDao));
				mPagePanel.revalidate();
				mPagePanel.repaint();
			});
		}

	}
}