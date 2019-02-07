package org.minioasis.library.domain;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@FilterDef(
		name = "attachmentStateFilter",
		defaultCondition= "state in (:effectiveAttachmentState)" ,
		parameters = {
			@ParamDef(name = "effectiveAttachmentState", type = "string")
		}
	)
public enum AttachmentState {
	
	// active
	CHECKOUT("Checkout"),
	
	// non-active
	LOST("Lost"),
	IN_LIBRARY("In library"),
	DAMAGE("Damage");
	
	private final String description;

	private AttachmentState(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
