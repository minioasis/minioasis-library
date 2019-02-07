package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
	private Set<Biblio> biblios = new HashSet<Biblio>(0);

	public PublicationType() {
	}

	public PublicationType(String name) {
		this.name = name;
	}

	public PublicationType(String name, Set<Biblio> biblios) {
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

	public Set<Biblio> getBiblios() {
		return biblios;
	}

	public void setBiblios(Set<Biblio> biblios) {
		this.biblios = biblios;
	}
	
	public boolean equals(Object other) {
		
		if (this == other) return true;
		if ( !(other instanceof PublicationType) ) return false;
		final PublicationType that = (PublicationType) other;
		
		return this.id.equals(that.getId());
		
	}

	public int hashCode() {
		return id == null ? System.identityHashCode(this) : id.hashCode();
	}

}
