package org.minioasis.library.domain.search;

import java.io.Serializable;

import org.minioasis.library.domain.DataType;

public class FormDataCriteria implements Serializable {

	private static final long serialVersionUID = -2499971247990122707L;
	private String data;
	private DataType type;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	
}
