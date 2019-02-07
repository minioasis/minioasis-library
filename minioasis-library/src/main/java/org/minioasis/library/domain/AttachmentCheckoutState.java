package org.minioasis.library.domain;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum AttachmentCheckoutState {

	// active state
	CHECKOUT("Checkout"),

	// completed state
	LOST("Lost"),
	RETURN("Return"),
	DAMAGE("Damage");
	
	private final String description;

	private AttachmentCheckoutState(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public static List<AttachmentCheckoutState> getActives(){
		return new ArrayList<AttachmentCheckoutState>(EnumSet.range(CHECKOUT, CHECKOUT));
	}

}
