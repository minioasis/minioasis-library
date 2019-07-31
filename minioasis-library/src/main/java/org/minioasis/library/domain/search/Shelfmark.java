package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.util.Objects;

public class Shelfmark implements Serializable {

	private static final long serialVersionUID = 2443499703871131301L;
	
	String barcode;
	String shelfmark;
	
	public Shelfmark(String barcode, String shelfmark) {
		super();
		this.barcode = barcode;
		this.shelfmark = shelfmark;
	}
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getShelfmark() {
		return shelfmark;
	}
	public void setShelfmark(String shelfmark) {
		this.shelfmark = shelfmark;
	}
	
    @Override	
	public boolean equals(Object other) {

		if(this == other)
			return true;
		if(other == null)
            return false;
		if(!(other instanceof Shelfmark))
			return false;
		final Shelfmark that = (Shelfmark)other;
		return Objects.equals(barcode, that.getBarcode());

	}
    
    @Override
	public int hashCode() {
		return Objects.hashCode(barcode);
	}

}
