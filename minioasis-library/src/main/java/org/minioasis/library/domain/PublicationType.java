package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "publication_type")
public class PublicationType implements Serializable {

	private static final long serialVersionUID = -3508479636950594515L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 64)
	@Column(name = "name", unique = true , nullable = false)
	private String name;
	
    @OneToMany(mappedBy="publicationType" , fetch = FetchType.LAZY)
	private List<Biblio> biblios = new ArrayList<Biblio>(0);

	public PublicationType() {
	}

	public PublicationType(String name) {
		this.name = name;
	}

	public PublicationType(String name, List<Biblio> biblios) {
		this.name = name;
		this.biblios = biblios;
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

	public List<Biblio> getBiblios() {
		return biblios;
	}

	public void setBiblios(List<Biblio> biblios) {
		this.biblios = biblios;
	}

    @Override
	public boolean equals(Object other) {
		
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof PublicationType))
			return false;
		final PublicationType that = (PublicationType)other;
		return Objects.equals(name, that.getName());
		
	}

    @Override
	public int hashCode() {
    	return Objects.hashCode(name);
	}

}
