package com.market.dao;

import java.util.ArrayList;

import com.market.vo.OrderVo;

public class OrderDao extends DBConn{
	ArrayList<OrderVo> orderList;
	
	/**데이터 추가 - Statement**/
	public int insert(OrderVo orderVo) {
		int result = 0;
		getStatement();
		
		try {
			for(int i=0; i<orderVo.getQtyList().length; i++) {
				StringBuffer sb = new StringBuffer(300);
				sb.append("insert into bookmarket_order values('");
				sb.append(orderVo.getOid()+"', '");
				sb.append(orderVo.getOdate()+"', ");
				sb.append(orderVo.getQtyList()[i]+", '");
				sb.append(orderVo.getIsbnList()[i]+"', '");
				sb.append(orderVo.getMid()+"', '");
				sb.append(orderVo.getName()+"', '");
				sb.append(orderVo.getPhone()+"', '");
				sb.append(orderVo.getAddr()+"')");
				
				result = stmt.executeUpdate(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	/**데이터 추가 - PreparedStatement**/
	public int insertPre(OrderVo orderVo) {
		int result = 0;
		StringBuffer sb = new StringBuffer(300);
		sb.append("insert into bookmarket_order values(?,?,?,?,?,?,?,?)");
		
		try {
			getPreparedStatement(sb.toString());
			for(int i=0; i<orderVo.getQtyList().length; i++) {
				pstmt.setString(1,orderVo.getOid());
				pstmt.setString(2,orderVo.getOdate());
				pstmt.setInt(3,orderVo.getQtyList()[i]);
				pstmt.setString(4,orderVo.getIsbnList()[i]);
				pstmt.setString(5,orderVo.getMid());
				pstmt.setString(6,orderVo.getName());
				pstmt.setString(7,orderVo.getPhone());
				pstmt.setString(8,orderVo.getAddr());
				pstmt.addBatch(); //지금까지 매핑했던 것 저장해둠
				pstmt.clearParameters(); //파라미터를 지워서 다른 데이터를 또 넣을 수 있도록 함
			}

			result = pstmt.executeBatch().length; //결국에는 Statement를 이용해서 하나씩 실행하기 때문에 PreparedStatement보다는 Statement를 사용하는 것이 나음
			pstmt.clearParameters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public ArrayList<OrderVo> select(){
		return orderList;
	}
}
