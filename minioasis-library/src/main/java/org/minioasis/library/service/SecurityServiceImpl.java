package org.minioasis.library.service;

import java.util.List;
import java.util.Optional;

import org.minioasis.library.domain.Role;
import org.minioasis.library.domain.User;
import org.minioasis.library.repository.RoleRepository;
import org.minioasis.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SecurityServiceImpl implements SecurityService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	// User
	public void add(User entity){
		this.userRepository.save(entity);
	}
	public void edit(User entity){
		this.userRepository.save(entity);
	}
	public Optional<User> getUser(long id){
		return this.userRepository.findById(id);
	}
	public void deleteUser(long id){
		this.userRepository.deleteById(id);
	}
	public User findByUsername(String username){
		return this.userRepository.findByUsername(username);
	}
	public List<User> findAllUsers(Sort sort){	
		return this.userRepository.findAll(sort);
	}
	@Override
	public Page<User> findAllUsers(Pageable pageable){
		Page<User> page = this.userRepository.findAll(pageable);
		return page;
	}
	public Page<User> findAllUsersByUsernameContaining(String username , Pageable pageable){
		Page<User> page = this.userRepository.findByUsernameContaining(username, pageable);
		return page;
	}

	// Role
	public Role getRole(long id){
		return this.roleRepository.getOne(id);
	}

	public List<Role> findAllRoles(Sort sort){
		return this.roleRepository.findAll(sort);
	}
	
}