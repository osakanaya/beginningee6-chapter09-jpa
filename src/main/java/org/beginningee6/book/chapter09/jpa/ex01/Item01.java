package org.beginningee6.book.chapter09.jpa.ex01;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Item01エンティティ。
 * 
 * 商品の在庫数量を表すavailableInStockフィールドでは、
 * 直接数量を任意の値にセットすることのできるsetterだけでなく、
 * 数量をひとつ増やす、または、数量をひとつ減らす
 * メソッドを定義している。
 *
 */
@Entity
@Table(name = "item_ex01")
@NamedQuery(name = "Item01.findAllItems", query = "SELECT i FROM Item01 i")
public class Item01 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;
	private String title;
	private Float price;
	private String description;
	
	// エンティティを初期化した時は在庫数量を0として初期化する
	private Integer availableInStock = Integer.valueOf(0);

    public Item01()	 {}

	public Item01(String title, Float price, String description,
			Integer availableInStock) {
		this.title = title;
		this.price = price;
		this.description = description;
		this.availableInStock = availableInStock;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAvailableInStock() {
		return availableInStock;
	}

	public void setAvailableInStock(Integer availableInStock) {
		this.availableInStock = availableInStock;
	}

	public Long getId() {
		return id;
	}
	
	/**
	 * 在庫数量をひとつ減らす。ただし、在庫数量が0になったら、在庫数量を
	 * 減らすことはできない。
	 * 
	 * @throws StockAvailabilityException 在庫数量が0の時でこのメソッドを呼び出したとき
	 */
	public void decreaseAvailableStock() throws StockAvailabilityException {
		if (availableInStock == 0) {
			// チェック例外をスローする
			throw new StockAvailabilityException("There is no stock for this item!");
		}
		
		availableInStock--;
	}
	
	/**
	 * 在庫数量をひとつ増やす
	 */
	public void increaseAvailableStock() {
		availableInStock++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((availableInStock == null) ? 0 : availableInStock.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item01 other = (Item01) obj;
		if (availableInStock == null) {
			if (other.availableInStock != null)
				return false;
		} else if (!availableInStock.equals(other.availableInStock))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item01 [id=" + id + ", title=" + title + ", price=" + price
				+ ", description=" + description + ", availableInStock="
				+ availableInStock + "]";
	}
    
}
