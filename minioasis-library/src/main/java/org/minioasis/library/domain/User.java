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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

@Entity
@Audited
@Table(name = "user" , uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements Serializable {

	private static final long serialVersionUID = 1342272234788649814L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "user_id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 16)
	@Column(name = "username", unique=true, nullable = false, length = 16)
	private String username;

	@NotNull
	@Length(max = 90)
	@Column(name = "password", nullable = false, length = 90)
	private String password;
	
	@Transient
	private String confirmPassword;
	
	@NotNull
	@Column(name = "enabled", nullable = false)
	private boolean enabled;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login")
	private Date lastLogin = new Date();
	
	@ManyToMany(cascade=CascadeType.MERGE , fetch = FetchType.EAGER)
	@JoinTable(
			name				= "user_role",
			joinColumns			= @JoinColumn(name="user_id"),
			inverseJoinColumns	= @JoinColumn(name="role_id")
			)
	@BatchSize(size = 10)
	private Set<Role> roles = new HashSet<Role>(0);

	public User() {
	}
	
 	public User(Long id) {
		this.id = id;
	}

	public User(String username, String password, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public User(String username, String password, boolean enabled, Set<Role> roles) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}
 	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Transient
	public List<String> getRoleNames() {
		
		List<String> rs = new ArrayList<String>();
		for(Role r : this.roles){
			rs.add(r.getName());
		}
		
		Collections.sort(rs);
		
		return rs;
		
	}
	
	public void addRole(Role role){
		if(role == null)
			throw new IllegalArgumentException("Can't add a null role.");
		
		this.roles.add(role);
	}
	
	public void removeRole(Role role) {
		if(role == null)
			throw new IllegalArgumentException("Can't remove a null role.");
		
		this.roles.remove(role);
	}
	
	public void removeRoles(Set<Role> roles) {
		if (roles == null)
			throw new IllegalArgumentException("Can't add a null roles.");

		this.getRoles().removeAll(roles);

	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		final User other = (User) obj;
		
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

}
