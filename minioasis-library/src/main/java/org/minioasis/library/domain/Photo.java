package org.minioasis.library.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "photo")
public class Photo implements java.io.Serializable {

	private static final long serialVersionUID = 6116618221818155805L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Length(max = 64)
	private String name;
	
	@Length(max = 64)
	private String description;
	
	@Length(max = 128)
	private String link;
	
	private int size;
	
	@Lob
	private byte[] img;
	
	@OneToOne(mappedBy = "photo")
	private Patron patron;

	public Photo(){}
	
	public Photo(String name, String description, String link, int size, byte[] img) {
		super();
		this.name = name;
		this.description = description;
		this.link = link;
		this.size = size;
		this.img = img;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public Patron getPatron() {
		return patron;
	}

	public void setPatron(Patron patron) {
		this.patron = patron;
	}
	
	public boolean equals(Object other) {

		if (this == other)
			return true;
		if (!(other instanceof Photo))
			return false;
		final Photo that = (Photo) other;
		return this.id.equals(that.getId());

	}

	public int hashCode() {
		return id == null ? System.identityHashCode(this) : id.hashCode();
	}

}
