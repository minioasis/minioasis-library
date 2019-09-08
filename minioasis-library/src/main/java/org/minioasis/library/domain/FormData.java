package org.minioasis.library.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "form_data")
public class FormData implements Serializable {

	private static final long serialVersionUID = 5050561580060622111L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
    @NotNull
    @Column(name = "data_type", nullable = false , updatable = true , columnDefinition = "VARCHAR(30)")
	@Enumerated(EnumType.STRING)
	private DataType type;
    
	@NotNull
	@Column(name = "data", nullable = false)
	private String data;
	
	 public FormData() {}
			 
    public FormData( @NotNull String data, @NotNull DataType type) {
		this.data = data;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object other) {

		if(this == other)
			return true;
		if(other == null)
			return false;
		if (!(other instanceof FormData))
			return false;
		final FormData that = (FormData) other;
		return id != null && id.equals(that.getId());

	}

    @Override
    public int hashCode() {
        return 55;
    }

}
