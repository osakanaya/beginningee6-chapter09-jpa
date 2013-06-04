package org.beginningee6.book.chapter09.jpa.ex01;

/**
 * 在庫数量が0のときは数量を減らすことができないことを示すチェック例外。
 */
public class StockAvailabilityException extends Exception {

	private static final long serialVersionUID = 1L;

	public StockAvailabilityException() {
		super();
	}

	public StockAvailabilityException(String message, Throwable cause) {
		super(message, cause);
	}

	public StockAvailabilityException(String message) {
		super(message);
	}

	public StockAvailabilityException(Throwable cause) {
		super(cause);
	}
}
