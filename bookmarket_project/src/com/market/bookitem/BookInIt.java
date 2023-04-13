package com.market.bookitem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import book_market2.BookVo;

public class BookInIt {
	private static ArrayList<BookVo> mBookList;
	private static int mTotalBook = 0;

	public static void init() {
		mTotalBook = totalFileToBookList();
		mBookList = new ArrayList<BookVo>();
		setFileToBookList(mBookList);
	}

	public static int totalFileToBookList() {
		try {
			FileReader fr = new FileReader("book.txt");
			BufferedReader reader = new BufferedReader(fr);

			String str;
			int num = 0;
			while ((str = reader.readLine()) != null) {
				if (str.contains("ISBN"))
					++num;
			}
			reader.close();
			fr.close();
			return num;
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static void setFileToBookList(ArrayList<BookVo> booklist) {
		try {
			FileReader fr = new FileReader("book.txt");
			BufferedReader reader = new BufferedReader(fr);

			String str2;
			String[] readBook = new String[7];

			while ((str2 = reader.readLine()) != null) {
				BookVo book = new BookVo();
				if (str2.contains("ISBN")) {
					book.setIsbn(str2);
					book.setTitle(reader.readLine());
					book.setPrice(Integer.parseInt(reader.readLine()));
					book.setAuthor(reader.readLine());
					book.setDesc(reader.readLine());
					book.setBfield(reader.readLine());
					book.setPdate(reader.readLine());
				}

				booklist.add(book);
			}
			reader.close();
			fr.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static ArrayList<BookVo> getmBookList() {
		return mBookList;
	}

	public static void setmBookList(ArrayList<BookVo> mBookList) {
		BookInIt.mBookList = mBookList;
	}

	public static int getmTotalBook() {
		return mTotalBook;
	}

	public static void setmTotalBook(int mTotalBook) {
		BookInIt.mTotalBook = mTotalBook;
	}
}