/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  The ASF licenses this file to You
* under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.  For additional information regarding
* copyright in this work, please see the NOTICE file in the top level
* directory of this distribution.
*/

package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
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
@Table(name = "publisher")
public class Publisher implements Serializable {

	private static final long serialVersionUID = -1730646322073379545L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 128)
	@Column(name = "name", unique = true , nullable = false)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "publisher", orphanRemoval = true)
    private Set<Biblio> biblios = new HashSet<Biblio>();

	public Publisher() {
	}

	public Publisher(String name) {
		this.name = name;
	}

	public Long getId() {
		return this.id;
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
	
	public void addBiblio(Biblio biblio) {
		this.biblios.add(biblio);
		biblio.setPublisher(this);
	}

	public void removeBiblio(Biblio biblio) {
		biblios.remove(biblio);
		biblio.setPublisher(null);
	}
	 
	@Override
	public boolean equals(Object other) {
		
		if(this == other)
			return true;
		if(other == null)
			return false;
		if (!(other instanceof Publisher))
			return false;
		final Publisher that = (Publisher)other;
		return Objects.equals(name, that.getName());
		
	}

    @Override
	public int hashCode() {
    	return Objects.hashCode(name);
	}

}
