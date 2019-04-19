package org.minioasis.library.bootstrap;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.minioasis.library.domain.Address;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.BiblioType;
import org.minioasis.library.domain.Binding;
import org.minioasis.library.domain.Contact;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.ItemDuration;
import org.minioasis.library.domain.ItemState;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Language;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.Publisher;
import org.minioasis.library.domain.Series;
import org.minioasis.library.domain.Volume;
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
		
		Biblio b1 = new Biblio();
		b1.setActive(YesNo.Y);
		b1.setLanguage(Language.cn);
		b1.setBinding(Binding.H);
		b1.setBiblioType(BiblioType.BOOK);
		b1.setTitle("X-Man");
		b1.setAuthor("Steven Spy");
		b1.setIsbn("985412365214");
		b1.setClassMark("532.12");
		b1.setPublisher(pb3);
		b1.setSeries(s2);
		biblioRepository.save(b1);
						
		Biblio b2 = new Biblio();
		b2.setActive(YesNo.Y);
		b2.setLanguage(Language.my);
		b2.setBinding(Binding.S);
		b2.setBiblioType(BiblioType.BOOK);
		b2.setTitle("Spider Man");
		b2.setAuthor("Ken Robinson");
		b2.setIsbn("985484165164");
		b2.setClassMark("532.12");
		b2.setPublisher(pb2);
		b2.setSeries(s2);
		biblioRepository.save(b2);
		
		Biblio b3 = new Biblio();
		b3.setActive(YesNo.Y);
		b3.setLanguage(Language.cn);
		b3.setBinding(Binding.H);
		b3.setBiblioType(BiblioType.BOOK);
		b3.setTitle("Iron Man");
		b3.setAuthor("Robert");
		b3.setIsbn("985481111164");
		b3.setClassMark("511.12");
		b3.setPublisher(pb2);
		b3.setSeries(s3);
		biblioRepository.save(b3);

		// Item Duration
		ItemDuration d1 = new ItemDuration("1 Week", 7);
		ItemDuration d2 = new ItemDuration("2 Weeks", 14);
		ItemDuration d3 = new ItemDuration("1 Month", 30);
		itemDurationRepository.save(d1);
		itemDurationRepository.save(d2);
		itemDurationRepository.save(d3);
		
		// Item Status
		ItemStatus is1 = new ItemStatus("Available",true,true);
		ItemStatus is2 = new ItemStatus("Lost",false,false);
		ItemStatus is3 = new ItemStatus("New Book Display",false,true);
		itemStatusRepository.save(is1);
		itemStatusRepository.save(is2);
		itemStatusRepository.save(is3);
		
		// Location
		Location l1 = new Location("Kepong");
		Location l2 = new Location("Sri Petaling");
		Location l3 = new Location("Jalan Ipoh");
		locationRepository.save(l1);
		locationRepository.save(l2);
		locationRepository.save(l3);
		
		// Item
		Item i1 = new Item();
		i1.setActive(YesNo.Y);
		i1.setBarcode("111");
		i1.setBiblio(b2);
		i1.setChecked(YesNo.N);
		i1.setCreated(date("2015-10-10"));
		i1.setExpired(date("2016-10-10"));
		i1.setFirstCheckin(date("2015-10-10"));
		i1.setItemDuration(d1);
		i1.setState(ItemState.CHECKOUT);
		i1.setItemStatus(is1);
		i1.setLastCheckin(date("2015-10-10"));
		i1.setLastFullRenewPerson("Moon");
		i1.setLocation(l1);
		i1.setPrice(new BigDecimal(12.50));
		i1.setShelfMark("435.78 HY1");
		i1.setSource("Shiang Hua");
		itemRepository.save(i1);
		
		Item i2 = new Item();
		i2.setActive(YesNo.Y);
		i2.setBarcode("222");
		i2.setBiblio(b3);
		i2.setChecked(YesNo.N);
		i2.setCreated(date("2015-10-10"));
		i2.setExpired(date("2016-10-10"));
		i2.setFirstCheckin(date("2015-10-10"));
		i2.setItemDuration(d1);
		i2.setState(ItemState.IN_LIBRARY);
		i2.setItemStatus(is1);
		i2.setLastCheckin(date("2015-10-10"));
		i2.setLastFullRenewPerson("Thai");
		i2.setLocation(l2);
		i2.setPrice(new BigDecimal(12.50));
		i2.setShelfMark("435.78 HY2");
		i2.setSource("Shiang Hua");
		itemRepository.save(i2);
	
		Item i3 = new Item();
		i3.setActive(YesNo.N);
		i3.setBarcode("333");
		i3.setBiblio(b1);
		i3.setChecked(YesNo.N);
		i3.setCreated(date("2015-10-10"));
		i3.setExpired(date("2016-10-10"));
		i3.setFirstCheckin(date("2015-10-10"));
		i3.setItemDuration(d1);
		i3.setState(ItemState.IN_LIBRARY);
		i3.setItemStatus(is1);
		i3.setLastCheckin(date("2015-10-10"));
		i3.setLastFullRenewPerson("Karuna");
		i3.setLocation(l1);
		i3.setPrice(new BigDecimal(12.50));
		i3.setShelfMark("530.78 HY1");
		i3.setSource("Karuna");
		i3.setVolume(new Volume(date("2015-10-10"), "234"));
		itemRepository.save(i3);	

		Item i4 = new Item();
		i4.setActive(YesNo.Y);
		i4.setBarcode("444");
		i4.setBiblio(b1);
		i4.setChecked(YesNo.Y);
		i4.setCreated(date("2015-10-10"));
		i4.setExpired(date("2016-10-10"));
		i4.setFirstCheckin(date("2015-10-10"));
		i4.setItemDuration(d1);
		i4.setState(ItemState.IN_LIBRARY);
		i4.setItemStatus(is3);
		i4.setLastCheckin(date("2015-10-10"));
		i4.setLastFullRenewPerson("Karuna");
		i4.setLocation(l1);
		i4.setPrice(new BigDecimal(12.50));
		i4.setShelfMark("530.78 HY2");
		i4.setSource("Karuna");
		i4.setVolume(new Volume(date("2015-10-10"), "234"));
		itemRepository.save(i4);
		
		Item i5 = new Item();
		i5.setActive(YesNo.Y);
		i5.setBarcode("555");
		i5.setBiblio(b1);
		i5.setChecked(YesNo.N);
		i5.setCreated(date("2015-10-10"));
		i5.setExpired(date("2016-10-10"));
		i5.setFirstCheckin(date("2015-10-10"));
		i5.setItemDuration(d1);
		i5.setState(ItemState.IN_LIBRARY);
		i5.setItemStatus(is1);
		i5.setLastCheckin(date("2015-10-10"));
		i5.setLastFullRenewPerson("Karuna");
		i5.setLocation(l1);
		i5.setPrice(new BigDecimal(12.50));
		i5.setShelfMark("530.78 HY3");
		i5.setSource("Karuna");
		i5.setVolume(new Volume(date("2015-10-10"), "234"));
		itemRepository.save(i5);
		
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
		
	}
	
}
