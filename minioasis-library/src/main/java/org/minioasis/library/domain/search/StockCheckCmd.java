package org.minioasis.library.domain.search;

import java.io.Serializable;

import org.minioasis.library.domain.Item;

public class StockCheckCmd implements Serializable {

	private static final long serialVersionUID = 2474785145183316075L;
	
	private String barcode;
	private Item item = new Item();

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
}
