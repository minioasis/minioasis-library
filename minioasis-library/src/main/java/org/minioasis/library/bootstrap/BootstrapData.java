package org.minioasis.library.bootstrap;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.minioasis.library.domain.Address;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.BiblioType;
import org.minioasis.library.domain.Contact;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Language;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.Publisher;
import org.minioasis.library.domain.Series;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.repository.AttachmentRepository;
import org.minioasis.library.repository.BiblioRepository;
import org.minioasis.library.repository.CheckoutRepository;
import org.minioasis.library.repository.GroupRepository;
import org.minioasis.library.repository.HolidayRepository;
import org.minioasis.library.repository.ItemDurationRepository;
import org.minioasis.library.repository.ItemRepository;
import org.minioasis.library.repository.ItemStatusRepository;
import org.minioasis.library.repository.LocationRepository;
import org.minioasis.library.repository.PatronRepository;
import org.minioasis.library.repository.PatronTypeRepository;
import org.minioasis.library.repository.PublisherRepository;
import org.minioasis.library.repository.SeriesRepository;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class BootstrapData implements CommandLineRunner {

	private final LibraryService service;

	@Autowired
	private AttachmentRepository attachmentRepository;
	@Autowired
	private BiblioRepository biblioRepository;
	@Autowired
	private CheckoutRepository checkoutRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ItemDurationRepository itemDurationRepository;
	@Autowired
	private ItemStatusRepository itemStatusRepository;
	@Autowired
	private LocationRepository locationRepository;	
	@Autowired
	private PatronRepository patronRepository;
	@Autowired
	private PatronTypeRepository patronTypeRepository;
	@Autowired
	private PublisherRepository publisherRepository;
	@Autowired
	private SeriesRepository seriesRepository;
	
	public BootstrapData(LibraryService service) {
		this.service = service;
	}
	
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	private Date date(String date) {
		Date d = null;	
		 try{
			 d = format.parse(date + "T00:00:00Z");
		 }catch(ParseException ex){
			 ex.printStackTrace();
		 }
		return d;	
	}
	
	public void run(String... args) throws Exception {
		
		Biblio b1 = new Biblio();
		b1.setTitle("X-Man");
		b1.setActive(YesNo.Y);
		b1.setBiblioType(BiblioType.BOOK);
		b1.setLanguage(Language.cn);
		service.save(b1);
		
		Biblio b2 = new Biblio();
		b2.setTitle("Spider Man");
		b2.setActive(YesNo.Y);
		b2.setBiblioType(BiblioType.BOOK);
		b2.setLanguage(Language.cn);
		service.save(b2);
		
		Biblio b3 = new Biblio();
		b3.setTitle("Iron Man");
		b3.setActive(YesNo.Y);
		b3.setBiblioType(BiblioType.BOOK);
		b3.setLanguage(Language.cn);
		service.save(b3);
		
		// Patron Type
		PatronType pt1 = new PatronType();
		pt1.setName("Student");
		pt1.setStartDate(date("2015-01-02"));
		pt1.setExpiryDate(date("2016-12-12"));
		pt1.setMemberFee(new BigDecimal(100.00));
		pt1.setDeposit(new BigDecimal(200.00));
		pt1.setBiblioLimit(5);
		pt1.setDuration(6);
		pt1.setMaxNoOfReservations(3);
		pt1.setMaxCollectablePeriod(7);
		pt1.setMaxUncollectedNo(3);
		pt1.setMaxCantReservePeriod(14);
		pt1.setMaxNoOfRenew(2);
		pt1.setMinRenewablePeriod(4);
		pt1.setResumeBorrowablePeriod(7);
		pt1.setFineRate(new BigDecimal(0.50));	
		patronTypeRepository.save(pt1);

		PatronType pt2 = new PatronType();
		pt2.setName("Teacher");
		pt2.setStartDate(date("2015-01-02"));
		pt2.setExpiryDate(date("2016-12-12"));
		pt2.setMemberFee(new BigDecimal(100.00));
		pt2.setDeposit(new BigDecimal(200.00));
		pt2.setBiblioLimit(5);
		pt2.setDuration(6);
		pt2.setMaxNoOfReservations(3);
		pt2.setMaxCollectablePeriod(7);
		pt2.setMaxUncollectedNo(3);
		pt2.setMaxCantReservePeriod(14);
		pt2.setMaxNoOfRenew(2);
		pt2.setMinRenewablePeriod(4);
		pt2.setResumeBorrowablePeriod(7);
		pt2.setFineRate(new BigDecimal(0.50));	
		patronTypeRepository.save(pt2);
		
		// Group
		Group g1 = new Group();
		g1.setCode("4S1");
		g1.setName("Senior Middle 1 Science 1");
		groupRepository.save(g1);
		
		Group g2 = new Group();
		g2.setCode("4S2");
		g2.setName("Senior Middle 1 Science 2");
		groupRepository.save(g2);
		
		Group g3 = new Group();
		g3.setCode("5A2");
		g3.setName("Senior Middle 2 Art 2");
		groupRepository.save(g3);
		
		// Address
		Address a1 = new Address();
		a1.setAddress1("No.12, Jalan Indah 1");
		a1.setAddress2("Taman Indah");
		a1.setCity("Kuala Lumpur");
		a1.setState("WP");
		a1.setPostcode("51200");
		a1.setCountry("Malaysia");

		Address a2 = new Address();
		a2.setAddress1("No.231, Jalan Jambu air Laut");
		a2.setAddress2("Jinjang Selatan");
		a2.setCity("Kuala Lumpur");
		a2.setState("WP");
		a2.setPostcode("52000");
		a2.setCountry("Malaysia");
		
		// Contact
		Contact c1 = new Contact();
		c1.setTel("03-62774545");
		c1.setMobile("012-3111020");
		c1.setEmail("moan70@hotmail.com");
		c1.setAddress(a1);
		
		Contact c2 = new Contact();
		c2.setTel("03-666566626");
		c2.setMobile("012-9568452");
		c2.setEmail("feijai33@hotmail.com");
		c2.setAddress(a2);

		Contact c3 = new Contact();
		c3.setTel("03-666566626");
		c3.setMobile("012-112132323");
		c3.setEmail("hello33@hotmail.com");
		c3.setAddress(a2);
		
		// Patron
		Patron p1 = new Patron();
		p1.setActive(YesNo.Y);
		p1.setName("Moan Ah Meng");
		p1.setName2("Mr. Moon");
		p1.setStartDate(date("2015-01-03"));
		p1.setEndDate(date("2016-12-12"));
		p1.setCardKey("11111");
		p1.setEntangled("entangled1");
		p1.setPatronType(pt1);
		p1.setGroup(g1);
		p1.setGender("M");
		p1.setIc("700819-10-9565");
		p1.setContact(c1);
		patronRepository.save(p1);
		
		Patron p2 = new Patron();
		p2.setActive(YesNo.Y);
		p2.setName("Moan Wai Loong");
		p2.setName2("Mr. Loong");
		p2.setStartDate(date("2015-01-01"));
		p2.setEndDate(date("2016-12-12"));
		p2.setCardKey("22222");
		p2.setEntangled("entangled2");
		p2.setPatronType(pt1);
		p2.setGroup(g2);
		p2.setGender("M");
		p2.setIc("800719-10-7777");
		p2.setContact(c2);
		patronRepository.save(p2);
		
		Patron p3 = new Patron();
		p3.setActive(YesNo.N);
		p3.setName("R.P. Feynman");
		p3.setName2("Prof Feynman");
		p3.setStartDate(date("2015-01-15"));
		p3.setEndDate(date("2016-12-12"));
		p3.setCardKey("33333");
		p3.setEntangled("entangled3");
		p3.setPatronType(pt2);
		p3.setGroup(g3);
		p3.setGender("F");
		p3.setIc("44415-10-7777");
		p3.setContact(c3);
		patronRepository.save(p3);
		
		// publisher
		Publisher pb1 = new Publisher();
		pb1.setName("Pearson Education");
		publisherRepository.save(pb1);
		
		Publisher pb2 = new Publisher();
		pb2.setName("Oxford University Press");
		publisherRepository.save(pb2);

		Publisher pb3 = new Publisher();
		pb3.setName("John Wiley & Sons");
		publisherRepository.save(pb3);
		
		// series
		Series s1 = new Series();
		s1.setName("Happy Boy Series");
		seriesRepository.save(s1);
		
		Series s2 = new Series();
		s2.setName("Harry Potter Series");
		seriesRepository.save(s2);
		
		Series s3 = new Series();
		s3.setName("Bad Ass Series");
		seriesRepository.save(s3);
		
	}
	
}
