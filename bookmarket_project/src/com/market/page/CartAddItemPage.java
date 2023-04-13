package com.market.page;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.market.commons.MarketFont;
import com.market.dao.BookDao;
import com.market.dao.CartDao;
import com.market.main.MainWindow;
import com.market.vo.BookVo;
import com.market.vo.CartVo;

import book_market2.CartMgm;

public class CartAddItemPage extends JPanel {

	ImageIcon imageBook;
	int mSelectRow = 0;

	CartMgm cm;
	CartDao cartDao;

	public CartAddItemPage(JPanel panel, CartDao cartDao, BookDao bookDao) {
		ArrayList<BookVo> booklist = bookDao.select();
//		this.cm = cm;
		this.cartDao = cartDao;

		setLayout(null);

		Rectangle rect = panel.getBounds();
		setPreferredSize(rect.getSize());

		JPanel imagePanel = new JPanel();
		imagePanel.setBounds(20, 0, 300, 400);
		imageBook = new ImageIcon("./images/"+booklist.get(0).getImg());
		imageBook.setImage(imageBook.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT));
		JLabel label = new JLabel(imageBook);
		imagePanel.add(label);
		add(imagePanel);

		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(300, 0, 700, 400);
		add(tablePanel);

//		ArrayList<BookVo> booklist = BookInIt.getmBookList();
		Object[] tableHeader = { "도서ID", "도서명", "가격", "저자", "설명", "분야", "출판일" };
		Object[][] content = new Object[booklist.size()][tableHeader.length];
		for (int i = 0; i < booklist.size(); i++) {
			BookVo bookitem = booklist.get(i);
			content[i][0] = bookitem.getIsbn();
			content[i][1] = bookitem.getTitle();
			content[i][2] = bookitem.getPrice();
			content[i][3] = bookitem.getAuthor();
			content[i][4] = bookitem.getIntro();
			content[i][5] = bookitem.getPart();
			content[i][6] = bookitem.getPdate();

		}

		JTable bookTable = new JTable(content, tableHeader);
		bookTable.setRowSelectionInterval(0, 0);
		bookTable.getSelectedColumn();
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setPreferredSize(new Dimension(600, 350));
		jScrollPane.setViewportView(bookTable);
		tablePanel.add(jScrollPane);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 400, 1000, 400);
		add(buttonPanel);
		JLabel buttonLabel = new JLabel("장바구니 담기");
		MarketFont.getFont(buttonLabel);
		JButton addButton = new JButton();
		addButton.add(buttonLabel);
		buttonPanel.add(addButton);

		/**마우스 이벤트 처리**/
		bookTable.addMouseListener(new MouseAdapter() { //MouseListener 인터페이스 대신 사용할 수 있음

			public void mouseClicked(MouseEvent e) {
				int row = bookTable.getSelectedRow();
				int col = bookTable.getSelectedColumn();
				mSelectRow = row; //마우스 커서가 위치한 행 index
				String str = booklist.get(mSelectRow).getImg();
//				Object value = bookTable.getValueAt(row, 0);
//				String str = value + ".jpg";

				imageBook = new ImageIcon("./images/" + str);
				imageBook.setImage(imageBook.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT));
				JLabel label = new JLabel(imageBook);
				imagePanel.removeAll();
				imagePanel.add(label);
				imagePanel.revalidate();
				imagePanel.repaint();
			}
		});

		/**장바구니 담기 이벤트 처리**/
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				CartVo cartVo = new CartVo();
				cartVo.setIsbn(booklist.get(mSelectRow).getIsbn().toUpperCase());
				cartVo.setMid(MainWindow.member.getMid().toUpperCase());
				
				int select = JOptionPane.showConfirmDialog(addButton, "장바구니에 추가하시겠습니까?");
				if (select == 0) { //yes 선택시
					
					if(cartDao.insertCheck(cartVo) == 0) {
						if(cartDao.insert(cartVo)==1) {
							JOptionPane.showMessageDialog(addButton, "장바구니에 추가되었습니다.");
						}
					} else {
						JOptionPane.showMessageDialog(addButton, "이미 등록된 도서입니다.");
					}
					
				}
			}
		});

	}
}