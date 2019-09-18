package org.minioasis.library.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.Role;
import org.minioasis.library.domain.User;
import org.minioasis.library.repository.FormDataRepository;
import org.minioasis.library.repository.GroupRepository;
import org.minioasis.library.repository.ItemStatusRepository;
import org.minioasis.library.repository.LocationRepository;
import org.minioasis.library.repository.PatronTypeRepository;
import org.minioasis.library.repository.RoleRepository;
import org.minioasis.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FirstTimeBootstrapData implements CommandLineRunner {

	@Autowired
	FormDataRepository formDataRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private ItemStatusRepository itemStatusRepository;
	@Autowired
	private LocationRepository locationRepository;	
	@Autowired
	private PatronTypeRepository patronTypeRepository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public void run(String... args) throws Exception {
		
		// Item Status
//		ItemStatus is1 = new ItemStatus("Available",true,true);
//		ItemStatus is2 = new ItemStatus("Lost",false,false);
//		ItemStatus is3 = new ItemStatus("New Book Display",false,true);
//		ItemStatus is4 = new ItemStatus("Reference",false,false);
//		ItemStatus is5 = new ItemStatus("Weeded",false,false);
//		itemStatusRepository.save(is1);
//		itemStatusRepository.save(is2);
//		itemStatusRepository.save(is3);
//		itemStatusRepository.save(is4);
//		itemStatusRepository.save(is5);
//		
//		// Location
//		Location location = new Location("Main Library");
//		locationRepository.save(location);
//		
//		// Group
//		Group g = new Group();
//		g.setCode("NP");
//		g.setName("Normal Patron");
//		groupRepository.save(g);
//		
//		// Patron Type
//		PatronType pt = new PatronType();
//		pt.setCode("NORMAL");
//		pt.setName("Normal Patron");
//		pt.setStartDate(LocalDate.parse("2019-01-01"));
//		pt.setExpiryDate(LocalDate.parse("2030-12-12"));
//		pt.setMemberFee(new BigDecimal(0.00));
//		pt.setDeposit(new BigDecimal(0.00));
//		pt.setBiblioLimit(3);
//		pt.setDuration(14);
//		pt.setMaxNoOfReservations(3);
//		pt.setMaxCollectablePeriod(7);
//		pt.setMaxUncollectedNo(3);
//		pt.setMaxCantReservePeriod(14);
//		pt.setMaxNoOfRenew(2);
//		pt.setMinRenewablePeriod(4);
//		pt.setResumeBorrowablePeriod(7);
//		pt.setFineRate(new BigDecimal(0.40));	
//		patronTypeRepository.save(pt);
//
//		// role
//		Role role_admin = new Role();
//		role_admin.setName("ROLE_ADMIN");
//		roleRepository.save(role_admin);
//
//		// user
//		String encodedPassword = passwordEncoder.encode("admin");
//
//		User admin = new User();
//		admin.setUsername("admin");
//		admin.setPassword(encodedPassword);
//		admin.setEnabled(true);
//		userRepository.save(admin);
//
//		Set<Role> roles = new HashSet<Role>();
//		roles.add(roleRepository.findById(1l).orElse(null));
//		admin.setRoles(roles);
//		userRepository.save(admin);
		
	}
}
