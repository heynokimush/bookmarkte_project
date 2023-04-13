package book_market2;

import java.util.ArrayList;
import java.util.Scanner;

public class CartMgm {
	//field
	ArrayList<CartItemVo> cartItemList;
	
	public CartMgm() {
		cartItemList = new ArrayList<CartItemVo>();
	}
	
	public ArrayList<CartItemVo> getCartItemList(){
		return cartItemList;
	}
	
	public int getSize() {
		return cartItemList.size();
	}
	
	public void add(boolean check, CartItemVo item, BookVo book) {
		if(check) {
			item.setQty(item.getQty()+1);
			item.setTotalPrice(book.getPrice());
		}else {
			CartItemVo item2 = new CartItemVo();
			item2.setIsbn(book.getIsbn());
			item2.setTitle(book.getTitle());
			item2.setQty(1);
			item2.setTotalPrice(book.getPrice());
			
			cartItemList.add(item2);
		}
	}
	
	/**장바구니에 book 추가**/
	public boolean insert(BookVo book) {
		boolean result = false;
//		result = cartList.add(book);
		
		if(cartItemList.size() != 0) {						
			//for(�⺻)�� ����ϸ� book�� isbn�� cartItemList�� isbn ��
			boolean check = false;
			CartItemVo currItem = null;
			for(int i=0;i<cartItemList.size();i++) {
				CartItemVo item = cartItemList.get(i);
				if(item.getIsbn().equals(book.getIsbn())) {					
					currItem = item;
					check= true;
					i=cartItemList.size();
				}
			}//for
			
			add(check, currItem, book);	  //cartItemList�� �߰�
			result = true;
			
		}else {
			CartItemVo item = new CartItemVo();
			item.setIsbn(book.getIsbn());
			item.setTitle(book.getTitle());
			item.setQty(1);
			item.setTotalPrice(book.getPrice());
			
			cartItemList.add(item);
			result = true;
		}
		
		return result;
	}
	

	public void remove() {
			cartItemList.clear();
	}
	
	public boolean remove(int idx) {
		return cartItemList.remove(cartItemList.get(idx));
	}
	
	public void updateQty(String isbn) {
		CartItemVo currItem = null;
		for(int i=0; i<getSize(); i++) {
			CartItemVo item = cartItemList.get(i);
			if(isbn.equals(item.getIsbn())) {
				currItem = item;
				i = getSize();
			}
		}
		
		if(currItem != null) {
			currItem.setQty(currItem.getQty() - 1);
		}
	}

}
