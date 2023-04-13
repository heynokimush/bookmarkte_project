package com.market.vo;

public class OrderVo {
	int rno, qty;
	String oid, odate, isbn, mid, name, phone, addr;
	int[] qtyList; //데이터추가
	String[] isbnList; //데이터추가
	
	public int getRno() {
		return rno;
	}
	public void setRno(int rno) {
		this.rno = rno;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOdate() {
		return odate;
	}
	public void setOdate(String odate) {
		this.odate = odate;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int[] getQtyList() {
		return qtyList;
	}
	public void setQtyList(int[] qtyList) {
		this.qtyList = qtyList;
	}
	public String[] getIsbnList() {
		return isbnList;
	}
	public void setIsbnList(String[] isbnList) {
		this.isbnList = isbnList;
	}
	
	

}
