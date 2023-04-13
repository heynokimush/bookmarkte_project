package com.market.dao;

import java.util.ArrayList;

import com.market.vo.BookVo;

public class BookDao extends DBConn {
	ArrayList<BookVo> blist;
	
	/*
	 * 도서 전체 리스트 조회
	 */
	public ArrayList<BookVo> select(){
		blist = new ArrayList<BookVo>();
		StringBuffer sb = new StringBuffer(100);
		sb.append("select rownum rno, isbn, title, price, author, intro, part, pdate, img, bdate\r\n" + 
				"from(select isbn, title, price, author, intro, part, pdate, img, bdate\r\n" + 
				"    from bookmarket_book\r\n" + 
				"    order by bdate desc)");
		
		try {
			getPreparedStatement(sb.toString());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BookVo book = new BookVo();
				book.setRno(rs.getInt(1));
				book.setIsbn(rs.getString(2));
				book.setTitle(rs.getString(3));
				book.setPrice(rs.getInt(4));
				book.setAuthor(rs.getString(5));
				book.setIntro(rs.getString(6));
				book.setPart(rs.getString(7));
				book.setPdate(rs.getString(8));
				book.setImg(rs.getString(9));
				book.setBdate(rs.getString(10));
				
				blist.add(book);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return blist;
	}
	
	/**도서등록**/
	public int insert(BookVo bookVo) {
		int result=0;
		StringBuffer sb = new StringBuffer(100);
		sb.append("insert into bookmarket_book "); 
		sb.append("values('ISBN_'||ltrim(to_char(sequ_bookmarket_book_isbn.nextval,'0000')), "); 
		sb.append("?, ?, ?, ?, ?, ?, ?, sysdate "); 
		sb.append(")");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, bookVo.getTitle());
			pstmt.setInt(2, bookVo.getPrice());
			pstmt.setString(3, bookVo.getAuthor());
			pstmt.setString(4, bookVo.getIntro());
			pstmt.setString(5, bookVo.getPart());
			pstmt.setString(6, bookVo.getPdate());
			pstmt.setString(7, bookVo.getImg());
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
