package org.minioasis.report.chart;

import java.io.Serializable;

public class Series implements Serializable {

	private static final long serialVersionUID = 7169056679656770188L;
	
	private String name = null;
	private int[] data = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getData() {
		return data;
	}
	public void setData(int[] data) {
		this.data = data;
	}

}
