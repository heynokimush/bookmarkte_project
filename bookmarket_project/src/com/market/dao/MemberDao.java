package com.market.dao;

import com.market.vo.MemberVo;

public class MemberDao extends DBConn {
	/*
	 * 로그인 체크
	 */
	public int select(String mid, String pw) {
		int result = 0;
		StringBuffer sb = new StringBuffer(100);
		sb.append("select count(*) from bookmarket_member");
		sb.append(" where mid=? and pw=?"); //그럼 id와 pw를 따로 체크할 수가 없어짐. count말고 pw를 가져와서 null이 아니면 값을 체크해서 확인하는 방식이 나을듯.
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, mid);
			pstmt.setString(2, pw);
			
			rs = pstmt.executeQuery();
			while(rs.next()) result = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	/*
	 * 고객정보 확인
	 */
	public MemberVo select(String mid) {
		MemberVo member = new MemberVo();
		StringBuffer sb = new StringBuffer(100);
		sb.append("select mid, pw, name, addr, phone, mdate from bookmarket_member");
		sb.append(" where mid=?"); //그럼 id와 pw를 따로 체크할 수가 없어짐. count말고 pw를 가져와서 null이 아니면 값을 체크해서 확인하는 방식이 나을듯.
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, mid.toUpperCase());
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				member.setMid(mid);
				member.setPw(rs.getString(2));
				member.setName(rs.getString(3));
				member.setAddr(rs.getString(4));
				member.setPhone(rs.getString(5));
				member.setMdate(rs.getString(6));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return member;
	}
}
