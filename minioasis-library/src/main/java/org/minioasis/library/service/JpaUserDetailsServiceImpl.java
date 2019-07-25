package org.minioasis.library.service;

import org.minioasis.library.domain.User;
import org.minioasis.security.adapter.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jpaUserDetailsService")
@Transactional(readOnly = true)
public class JpaUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private SecurityService service;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		User user = service.findByUsername(username);
		
		if(user == null){
			throw new UsernameNotFoundException(username);
		}
		
		return new SecurityUser(user);

	}

}
