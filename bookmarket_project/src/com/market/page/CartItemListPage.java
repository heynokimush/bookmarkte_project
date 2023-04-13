package com.market.page;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.market.commons.MarketFont;
import com.market.dao.BookDao;
import com.market.dao.CartDao;
import com.market.dao.DBConn;
import com.market.dao.MemberDao;
import com.market.main.MainWindow;
import com.market.vo.CartVo;

import book_market2.CartItemVo;
import book_market2.CartMgm;

public class CartItemListPage extends JPanel {
	JTable cartTable;
	Object[] tableHeader = { "번호", "도서ID", "도서명", "단가", "수량", "총가격" };

	CartDao cartDao;
	BookDao bookDao;
	MemberDao memberDao;
	public static int mSelectRow = -1;
	ArrayList<CartVo> cartItemList;
	
	JLabel totalPricelabel;
	
	public CartItemListPage(JPanel panel, Map<String, DBConn> daoList) {
		this.cartDao = (CartDao)daoList.get("cartDao");
		this.bookDao = (BookDao)daoList.get("bookDao");
		this.memberDao = (MemberDao)daoList.get("memberDao");
		this.setLayout(null);

		Rectangle rect = panel.getBounds();
		System.out.println(rect);
		this.setPreferredSize(rect.getSize());

		JPanel bookPanel = new JPanel();
		bookPanel.setBounds(0, 0, 1000, 400);
		add(bookPanel);

		cartItemList = cartDao.select(MainWindow.member.getMid().toUpperCase());
		Object[][] content = new Object[cartItemList.size()][tableHeader.length];
		Integer totalPrice = 0;
		for (int i = 0; i < cartItemList.size(); i++) {
			CartVo item = cartItemList.get(i);
			content[i][0] = item.getRno();
			content[i][1] = item.getIsbn();
			content[i][2] = item.getTitle();
			content[i][3] = item.getSprice(); //단가
			content[i][4] = item.getQty();
			content[i][5] = item.getStotal_price(); //총액
			totalPrice += item.getTotal_price(); //장바구니 총합
		}

		cartTable = new JTable(content, tableHeader);
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setPreferredSize(new Dimension(600, 350));
		jScrollPane.setViewportView(cartTable);
		bookPanel.add(jScrollPane);

		JPanel totalPricePanel = new JPanel();
		totalPricePanel.setBounds(0, 400, 1000, 50);
		totalPricelabel = new JLabel("총금액: " + priceFormat(totalPrice) + " 원");
		totalPricelabel.setForeground(Color.red);
		MarketFont.getFont(totalPricelabel);
		totalPricePanel.add(totalPricelabel);
		add(totalPricePanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBounds(0, 450, 1000, 50);
		add(buttonPanel);

		JLabel buttonLabel = new JLabel("장바구니 비우기");
		MarketFont.getFont(buttonLabel);
		JButton clearButton = new JButton();
		clearButton.add(buttonLabel);
		buttonPanel.add(clearButton);

		/**장바구니 비우기 버튼 이벤트 처리**/
		clearButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int select = JOptionPane.showConfirmDialog(clearButton, "정말로 삭제하시겠습니까? ");
				if (select == 0) {
					TableModel tableModel = new DefaultTableModel(new Object[0][0], tableHeader);
					cartTable.setModel(tableModel);
					totalPricelabel.setText("총금액: " + 0 + " 원");

					cartDao.delete(MainWindow.member.getMid().toUpperCase(), "mid");
					JOptionPane.showMessageDialog(clearButton, "삭제가 완료되었습니다");

				}
			}
		});

		/**장바구니 항목 삭제 버튼 이벤트 처리**/
		JLabel removeLabel = new JLabel("장바구니 항목 삭제하기");
		MarketFont.getFont(removeLabel);
		JButton removeButton = new JButton();
		removeButton.add(removeLabel);
		buttonPanel.add(removeButton);

		removeButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (mSelectRow == -1) //현재 커서가 아무 아이템도 선택하고 있지 않은 상태
					JOptionPane.showMessageDialog(clearButton, "삭제할 항목을 선택해주세요");
				else {
					int select = JOptionPane.showConfirmDialog(clearButton, "정말로 삭제하시겠습니까? ");
					if(select == 0) {
						int result = cartDao.delete(cartItemList.get(mSelectRow).getCid().toUpperCase(), "cid");
						if(result==1) {
							showList();
							mSelectRow = -1; //마우스 커서 해제
							JOptionPane.showMessageDialog(clearButton, "삭제가 완료되었습니다");
						}
					}
				}
			}
		});

		/**테이블 마우스 클릭 이벤트**/
		cartTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = cartTable.getSelectedRow();
				mSelectRow = row;
			}

		});

		/**장바구니 항목 수정하기: 증가(+)**/
		JLabel updateQtyLabel1 = new JLabel("장바구니 항목 수량(+)");
		MarketFont.getFont(updateQtyLabel1);
		JButton updateQtyButton1 = new JButton();
		updateQtyButton1.add(updateQtyLabel1);
		buttonPanel.add(updateQtyButton1);

		updateQtyButton1.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(mSelectRow == -1){
					JOptionPane.showMessageDialog(null, "수정할 항목을 선택해주세요");
				} else {
					int qty = cartItemList.get(mSelectRow).getQty();
					if(qty >= 1) {
						cartDao.updateQty(cartItemList.get(mSelectRow).getCid().toUpperCase(), "+");
						showList();
						mSelectRow = -1;
					} else {
						JOptionPane.showMessageDialog(null, "2개 이상인 경우에만 수정이 가능합니다");
					}
					
				}
			}
		});
		
		/**장바구니 항목 수정하기: 감소(-)**/
		JLabel updateQtyLabel2 = new JLabel("장바구니 항목 수량(-)");
		MarketFont.getFont(updateQtyLabel2);
		JButton updateQtyButton2 = new JButton();
		updateQtyButton2.add(updateQtyLabel2);
		buttonPanel.add(updateQtyButton2);
		
		updateQtyButton2.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(mSelectRow == -1){
					JOptionPane.showMessageDialog(null, "수정할 항목을 선택해주세요");
				} else {
					int qty = cartItemList.get(mSelectRow).getQty();
					if(qty > 1) {
						cartDao.updateQty(cartItemList.get(mSelectRow).getCid().toUpperCase(), "-");
						showList();
						mSelectRow = -1;
					} else {
						JOptionPane.showMessageDialog(null, "2개 이상인 경우에만 수정이 가능합니다");
					}
					
				}
			}
		});
	}//Constructor
	
	public void showList() {
		cartItemList = cartDao.select(MainWindow.member.getMid().toUpperCase()); //삭제 후 객체를 다시 불러옴
		Object[][] content = new Object[cartItemList.size()][tableHeader.length];
		Integer totalPrice = 0;
		for (int i = 0; i < cartItemList.size(); i++) {
			CartVo item = cartItemList.get(i);
			content[i][0] = item.getRno();
			content[i][1] = item.getIsbn();
			content[i][2] = item.getTitle();
			content[i][3] = item.getSprice(); //단가
			content[i][4] = item.getQty();
			content[i][5] = item.getStotal_price(); //총액
			totalPrice += item.getTotal_price(); //장바구니 총합
		}
		TableModel tableModel = new DefaultTableModel(content, tableHeader);
		totalPricelabel.setText("총금액: " + priceFormat(totalPrice) + " 원");
		cartTable.setModel(tableModel);
	}
	
	public static String priceFormat(long price) {
		DecimalFormat df = new DecimalFormat("###,###");
		String sprice = df.format(price);
		return df.format(price);
	}
}//class