package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "groups")
public class Group implements Serializable {

	private static final long serialVersionUID = -497523418404719743L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 64)
	@Column(name = "code", unique = true)
	private String code;

	@NotNull
	@Length(max = 128)
	private String name;
	
    @OneToMany(mappedBy="group")
	private Set<Patron> patron = new HashSet<Patron>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getFullname() {
		
		if(name == null){
			return code;
		} else {
			return code + " " + "[ " + name + " ]";
		}		
		
	}

	public Set<Patron> getPatron() {
		return patron;
	}

	public void setPatron(Set<Patron> patron) {
		this.patron = patron;
	}
	
    @Override	
	public boolean equals(Object other) {

		if(this == other)
			return true;
		if(other == null)
            return false;
		if(!(other instanceof Group))
			return false;
		final Group that = (Group)other;
		return Objects.equals(code, that.getCode());

	}
    
    @Override
	public int hashCode() {
		return Objects.hashCode(code);
	}
	

}
