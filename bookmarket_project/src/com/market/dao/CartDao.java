package com.market.dao;

import java.util.ArrayList;

import com.market.vo.CartVo;
import com.market.vo.OrderVo;

public class CartDao extends DBConn{
	
	public int insert(CartVo cartVo) {
		int result=0;
		
		StringBuffer sb = new StringBuffer(300);
		sb.append("insert into bookmarket_cart ");
		sb.append("values('C_'||trim(to_char(sequ_bookmarket_cart_cid.nextval,'0000')), sysdate, 1, ?, ?)");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, cartVo.getIsbn());
			pstmt.setString(2, cartVo.getMid());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int insertCheck(CartVo cartVo) {
		int result=0;
		
		StringBuffer sb = new StringBuffer(100);
		sb.append("select count(*) from bookmarket_cart where isbn=? and mid=?");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, cartVo.getIsbn().toUpperCase());
			pstmt.setString(2, cartVo.getMid().toUpperCase());
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public ArrayList<CartVo> select(String mid) {
		ArrayList<CartVo> cartItemList = new ArrayList<CartVo>();
		
		StringBuffer sb = new StringBuffer(300);
		sb.append("select rownum, isbn, title, price, sprice, qty, total_price, stotal_price, cid ");
		sb.append("from(select c.isbn, title, price, to_char(price,'999,999') sprice, qty, price*qty total_price, to_char(price*qty,'999,999') stotal_price, cid ");
		sb.append("from bookmarket_book b, bookmarket_cart c "); 
		sb.append("where b.isbn = c.isbn and mid=?");
		sb.append("order by cdate desc)");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, mid);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CartVo cartVo = new CartVo();
				
				cartVo.setRno(rs.getInt(1));
				cartVo.setIsbn(rs.getString(2));
				cartVo.setTitle(rs.getString(3));
				cartVo.setPrice(rs.getInt(4));
				cartVo.setSprice(rs.getString(5));
				cartVo.setQty(rs.getInt(6));
				cartVo.setTotal_price(rs.getInt(7));
				cartVo.setStotal_price(rs.getString(8));
				cartVo.setCid(rs.getString(9));
				
				cartItemList.add(cartVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cartItemList;
	}
	
	public int getSize(String mid) {
		int result = 0;
		
		StringBuffer sb = new StringBuffer(100);
		sb.append("select count(*) from bookmarket_cart where mid=?");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, mid);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int delete(String id, String func) {
		int result = 0;
		
		StringBuffer sb = new StringBuffer(100);
		sb.append("delete from bookmarket_cart ");
		if(func.equals("cid")) {
			sb.append("  where cid=?");
		} else {
			sb.append("  where mid=?");
		}
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
//	public int delete() {
//		int result = 0;
//		
//		StringBuffer sb = new StringBuffer(100);
//		sb.append("delete from bookmarket_cart");
//		
//		try {
//			getPreparedStatement(sb.toString());
//			
//			result = pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return result;
//	}
	
	public int updateQty(String cid, String func) {
		int result = 0;
		
		StringBuffer sb = new StringBuffer(100);
		sb.append("update bookmarket_cart set ");
		if(func.equals("+")) {
			sb.append(" qty=qty+1 ");
		} else {
			sb.append(" qty=qty-1 ");
		}
		sb.append(" where cid=?");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, cid);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public OrderVo getOrderVo(String mid) {
		OrderVo orderVo = new OrderVo();
		
		StringBuffer sb = new StringBuffer(50);
		sb.append("select qty, isbn from bookmarket_cart where mid=? order by cdate desc");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, mid);
			rs = pstmt.executeQuery();
			rs.last(); //rs의 마지막 행으로 커서 이동 - 디폴트로 못하도록 막혀있어서 에러가 발생함 => ResultSet의 커서 이동을 할 수 있도록 DBConn의 PreparedStatement를 생성할 때 허용해야함
			int[] qtyList = new int[rs.getRow()]; //현재 커서가 위치한 마지막 행번호를 가져와서 행 수를 알 수 있도록 함
			String[] isbnList = new String[rs.getRow()];
			rs.beforeFirst();
			int idx=0;
			while(rs.next()) {
				qtyList[idx] = rs.getInt(1);
				isbnList[idx] = rs.getString(2);
				
				idx++;
			}
			orderVo.setQtyList(qtyList);
			orderVo.setIsbnList(isbnList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return orderVo;
	}

}
