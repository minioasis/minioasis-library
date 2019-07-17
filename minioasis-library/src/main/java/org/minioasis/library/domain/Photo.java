package org.minioasis.library.domain;

public class Photo implements java.io.Serializable {

	private static final long serialVersionUID = 6116618221818155805L;
	
	private String name;
	
	private String description;
	
	private String url;
	
	private long size;

	public Photo(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
}
