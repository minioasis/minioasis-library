package org.minioasis.report.chart;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ChartData implements Serializable {

	private static final long serialVersionUID = 6840408870366117359L;
	
	private String title;
	private List<Series> series = new LinkedList<Series>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Series> getSeries() {
		return series;
	}
	public void setSeries(List<Series> series) {
		this.series = series;
	}

}
