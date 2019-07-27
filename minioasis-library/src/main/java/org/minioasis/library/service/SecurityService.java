package org.minioasis.library.service;

import java.util.List;
import java.util.Optional;

import org.minioasis.library.domain.Role;
import org.minioasis.library.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface SecurityService {

	// User
	void add(User entity);
	void edit(User entity);
	Optional<User> getUser(long id);
	void deleteUser(long id);
	User findByUsername(String username);
	List<User> findAllUsers(Sort sort);
	Page<User> findAllUsers(Pageable pageable);
	Page<User> findAllUsersByUsernameContaining(String username, Pageable pageable);
	
	// Role
	
	Role getRole(long id);
	List<Role> findAllRoles(Sort sort);
}
