package org.minioasis.library.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.minioasis.library.domain.Account;
import org.minioasis.library.domain.AccountType;
import org.minioasis.library.domain.Address;
import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.AttachmentState;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.BiblioType;
import org.minioasis.library.domain.Binding;
import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.Contact;
import org.minioasis.library.domain.DataType;
import org.minioasis.library.domain.FormData;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Holiday;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.ItemDuration;
import org.minioasis.library.domain.ItemState;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Language;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.Preference;
import org.minioasis.library.domain.Publisher;
import org.minioasis.library.domain.Series;
import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.domain.Volume;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.repository.AccountRepository;
import org.minioasis.library.repository.AttachmentRepository;
import org.minioasis.library.repository.BiblioRepository;
import org.minioasis.library.repository.CheckoutRepository;
import org.minioasis.library.repository.FormDataRepository;
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
import org.minioasis.library.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class BootstrapData implements CommandLineRunner {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AttachmentRepository attachmentRepository;
	@Autowired
	private BiblioRepository biblioRepository;
	@Autowired
	private CheckoutRepository checkoutRepository;
	@Autowired
	FormDataRepository formDataRepository;
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
	@Autowired
	private TelegramUserRepository telegramUserRepository;
	
	public void run(String... args) throws Exception {

		// holiday
		Holiday h1 = new Holiday();
		h1.setName("Hari Aidilfitri 2019");
		h1.setFine(false);
		h1.setStartDate(LocalDate.parse("2019-06-04"));
		h1.setEndDate(LocalDate.parse("2019-06-04"));
		holidayRepository.save(h1);
		
		Holiday h2 = new Holiday();
		h2.setName("Labour Day 2019");
		h2.setFine(false);
		h2.setStartDate(LocalDate.parse("2019-05-01"));
		h2.setEndDate(LocalDate.parse("2019-05-01"));
		holidayRepository.save(h2);
		
		Holiday h3 = new Holiday();
		h3.setName("Minioasis Holiday April");
		h3.setFine(false);
		h3.setStartDate(LocalDate.parse("2019-04-16"));
		h3.setEndDate(LocalDate.parse("2019-04-16"));
		holidayRepository.save(h3);
		
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
		
		Biblio b4 = new Biblio();
		b4.setActive(YesNo.Y);
		b4.setLanguage(Language.cn);
		b4.setBinding(Binding.H);
		b4.setBiblioType(BiblioType.BOOK);
		b4.setTitle("Feynman Lecture On Physics");
		b4.setAuthor("Feynman R.P");
		b4.setIsbn("845151512232");
		b4.setClassMark("564.12");
		b4.setPublisher(pb2);
		b4.setSeries(s3);
		biblioRepository.save(b4);

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
		i1.setBiblio(b1);
		i1.setChecked(YesNo.N);
		i1.setCreated(LocalDate.parse("2015-10-10"));
		i1.setExpired(LocalDateTime.parse("2016-10-10T00:00:00"));
		i1.setFirstCheckin(LocalDate.parse("2015-10-10"));
		i1.setItemDuration(d1);
		i1.setState(ItemState.CHECKOUT);
		i1.setItemStatus(is1);
		i1.setLastCheckin(LocalDateTime.parse("2015-10-10T00:00:00"));
		i1.setLastFullRenewPerson("Moon");
		i1.setLocation(l1);
		i1.setPrice(new BigDecimal(12.50));
		i1.setShelfMark("435.78 HY1");
		i1.setSource("Shiang Hua");
		itemRepository.save(i1);
		
		Item i2 = new Item();
		i2.setActive(YesNo.Y);
		i2.setBarcode("222");
		i2.setBiblio(b2);
		i2.setChecked(YesNo.N);
		i2.setCreated(LocalDate.parse("2015-10-10"));
		i2.setExpired(LocalDateTime.parse("2016-10-10T00:00:00"));
		i2.setFirstCheckin(LocalDate.parse("2015-10-10"));
		i2.setItemDuration(d1);
		i2.setState(ItemState.CHECKOUT);
		i2.setItemStatus(is1);
		i2.setLastCheckin(LocalDateTime.parse("2015-10-10T00:00:00"));
		i2.setLastFullRenewPerson("Thai");
		i2.setLocation(l2);
		i2.setPrice(new BigDecimal(12.50));
		i2.setShelfMark("435.78 HY2");
		i2.setSource("Shiang Hua");
		itemRepository.save(i2);
	
		Item i3 = new Item();
		i3.setActive(YesNo.Y);
		i3.setBarcode("333");
		i3.setBiblio(b3);
		i3.setChecked(YesNo.N);
		i3.setCreated(LocalDate.parse("2015-10-10"));
		i3.setExpired(LocalDateTime.parse("2016-10-10T00:00:00"));
		i3.setFirstCheckin(LocalDate.parse("2015-10-10"));
		i3.setItemDuration(d1);
		i3.setState(ItemState.CHECKOUT);
		i3.setItemStatus(is1);
		i3.setLastCheckin(LocalDateTime.parse("2015-10-10T00:00:00"));
		i3.setLastFullRenewPerson("Karuna");
		i3.setLocation(l1);
		i3.setPrice(new BigDecimal(12.50));
		i3.setShelfMark("530.78 HY1");
		i3.setSource("Karuna");
		i3.setVolume(new Volume(LocalDate.parse("2015-10-10"), "234"));
		itemRepository.save(i3);	

		Item i4 = new Item();
		i4.setActive(YesNo.Y);
		i4.setBarcode("444");
		i4.setBiblio(b3);
		i4.setChecked(YesNo.Y);
		i4.setCreated(LocalDate.parse("2015-10-10"));
		i4.setExpired(LocalDateTime.parse("2016-10-10T00:00:00"));
		i4.setFirstCheckin(LocalDate.parse("2015-10-10"));
		i4.setItemDuration(d1);
		i4.setState(ItemState.IN_LIBRARY);
		i4.setItemStatus(is1);
		i4.setLastCheckin(LocalDateTime.parse("2015-10-10T00:00:00"));
		i4.setLastFullRenewPerson("Karuna");
		i4.setLocation(l1);
		i4.setPrice(new BigDecimal(12.50));
		i4.setShelfMark("530.78 HY2");
		i4.setSource("Karuna");
		i4.setVolume(new Volume(LocalDate.parse("2015-10-10"), "234"));
		itemRepository.save(i4);
		
		Item i5 = new Item();
		i5.setActive(YesNo.Y);
		i5.setBarcode("555");
		i5.setBiblio(b4);
		i5.setChecked(YesNo.N);
		i5.setCreated(LocalDate.parse("2015-10-10"));
		i5.setExpired(LocalDateTime.parse("2016-10-10T00:00:00"));
		i5.setFirstCheckin(LocalDate.parse("2015-10-10"));
		i5.setItemDuration(d1);
		i5.setState(ItemState.IN_LIBRARY);
		i5.setItemStatus(is1);
		i5.setLastCheckin(LocalDateTime.parse("2015-10-10T00:00:00"));
		i5.setLastFullRenewPerson("Karuna");
		i5.setLocation(l1);
		i5.setPrice(new BigDecimal(12.50));
		i5.setShelfMark("530.78 HY3");
		i5.setSource("Karuna");
		i5.setVolume(new Volume(LocalDate.parse("2015-10-10"), "234"));
		itemRepository.save(i5);
		
		// attachments
		Attachment at1 = new Attachment();
		at1.setBarcode("A111");
		at1.setBorrowable(YesNo.N);
		at1.setCallNo("1111");
		at1.setDescription("description");
		at1.setFirstCheckin(LocalDate.parse("2019-01-01"));
		at1.setItem(i1);
		at1.setLastCheckin(LocalDate.parse("2019-04-04"));
		at1.setState(AttachmentState.IN_LIBRARY);
		attachmentRepository.save(at1);
		
		Attachment at2 = new Attachment();
		at2.setBarcode("A222");
		at2.setBorrowable(YesNo.Y);
		at2.setCallNo("2222");
		at2.setDescription("description");
		at2.setFirstCheckin(LocalDate.parse("2019-02-02"));
		at2.setItem(i2);
		at2.setLastCheckin(LocalDate.parse("2019-05-05"));
		at2.setState(AttachmentState.IN_LIBRARY);
		attachmentRepository.save(at2);
		
		Attachment at3 = new Attachment();
		at3.setBarcode("A333");
		at3.setBorrowable(YesNo.Y);
		at3.setCallNo("3333");
		at3.setDescription("description");
		at3.setFirstCheckin(LocalDate.parse("2019-03-03"));
		at3.setItem(i3);
		at3.setLastCheckin(LocalDate.parse("2019-06-06"));
		at3.setState(AttachmentState.IN_LIBRARY);
		attachmentRepository.save(at3);
		
		// Patron Type
		PatronType pt1 = new PatronType();
		pt1.setCode(1);
		pt1.setName("3-books");
		pt1.setStartDate(LocalDate.parse("2019-01-02"));
		pt1.setExpiryDate(LocalDate.parse("2030-12-12"));
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
		pt2.setCode(2);
		pt2.setName("5-books");
		pt2.setStartDate(LocalDate.parse("2019-01-02"));
		pt2.setExpiryDate(LocalDate.parse("2030-12-12"));
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
		c1.setMobile("0121111111");
		c1.setEmail("moan70@hotmail.com");
		c1.setAddress(a1);
		
		Contact c2 = new Contact();
		c2.setTel("03-666566626");
		c2.setMobile("0122222222");
		c2.setEmail("feijai33@hotmail.com");
		c2.setAddress(a2);

		Contact c3 = new Contact();
		c3.setTel("03-666566626");
		c3.setMobile("0123333333");
		c3.setEmail("hello33@hotmail.com");
		c3.setAddress(a2);
		
		// Patron
		Patron p1 = new Patron();
		p1.setActive(YesNo.Y);
		p1.setName("Moan Ah Meng");
		p1.setName2("Mr. Moon");
		p1.setStartDate(LocalDate.parse("2019-01-03"));
		p1.setEndDate(LocalDate.parse("2019-12-12"));
		p1.setCardKey("1111");
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
		p2.setStartDate(LocalDate.parse("2019-05-01"));
		p2.setEndDate(LocalDate.parse("2019-12-12"));
		p2.setCardKey("2222");
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
		p3.setStartDate(LocalDate.parse("2018-07-15"));
		p3.setEndDate(LocalDate.parse("2019-07-12"));
		p3.setCardKey("3333");
		p3.setEntangled("entangled3");
		p3.setPatronType(pt2);
		p3.setGroup(g3);
		p3.setGender("F");
		p3.setIc("44415-10-7777");
		p3.setContact(c3);
		patronRepository.save(p3);
		
		Patron p4 = new Patron();
		p4.setActive(YesNo.N);
		p4.setName("R.P. Feynman2");
		p4.setName2("Prof Feynman2");
		p4.setStartDate(LocalDate.parse("2018-07-15"));
		p4.setEndDate(LocalDate.parse("2019-07-12"));
		p4.setCardKey("4444");
		p4.setEntangled("entangled4");
		p4.setPatronType(pt1);
		p4.setGroup(g3);
		p4.setGender("F");
		p4.setIc("44415-10-8888");
		p4.setContact(c3);
		patronRepository.save(p4);
		
		//checkout
		Checkout co1 = new Checkout(LocalDate.parse("2019-01-10"), LocalDate.parse("2019-02-10"), new Integer(0), CheckoutState.CHECKOUT, p1, i1);
		checkoutRepository.save(co1);
		
		Checkout co2 = new Checkout(LocalDate.parse("2019-04-20"), LocalDate.parse("2019-05-10"), new Integer(0), CheckoutState.CHECKOUT, p2, i2);
		checkoutRepository.save(co2);
		
		Checkout co3 = new Checkout(LocalDate.parse("2019-04-25"), LocalDate.parse("2019-05-15"), new Integer(0), CheckoutState.CHECKOUT, p4, i3);
		checkoutRepository.save(co3);
		
		// Account
		Account ac1 = new Account();
		ac1.setCode("1111");
		ac1.setName("Moan Ah Meng");
		ac1.setType(AccountType.PATRON);
		accountRepository.save(ac1);
		
		Account ac2 = new Account();
		ac2.setCode("2222");
		ac2.setName("Moan Wai Loong");
		ac2.setType(AccountType.PATRON);
		accountRepository.save(ac2);
		
		Account ac3 = new Account();
		ac3.setCode("3333");
		ac3.setName("R.P. Feynman");
		ac3.setType(AccountType.PATRON);
		accountRepository.save(ac3);
		
		Account ac4 = new Account();
		ac4.setCode("4444");
		ac4.setName("R.P. Feynman2");
		ac4.setType(AccountType.PATRON);
		accountRepository.save(ac4);
		
		// FormData
		FormData fd1 = new FormData("l love you", DataType.JOURNAL_ENTRY_DESP);
		formDataRepository.save(fd1);
		
		FormData fd2 = new FormData("new member", DataType.JOURNAL_ENTRY_DESP);
		formDataRepository.save(fd2);
		
		FormData fd3 = new FormData("renew", DataType.JOURNAL_ENTRY_DESP);
		formDataRepository.save(fd3);
		
		// TelegramUser
		Preference preference1 = new Preference(true,true,true,true,true,true,true,true);
		Preference preference2 = new Preference(true,true,true,false,false,true,true,true);
		Preference preference3 = new Preference(false,false,false,true,false,false,true,true);

		TelegramUser tu1 = new TelegramUser(1111l,"1111", preference1);
		TelegramUser tu2 = new TelegramUser(2222l,"2222", preference2);
		TelegramUser tu3 = new TelegramUser(3333l,"3333", preference2);
		TelegramUser tu4 = new TelegramUser(4444l,"4444", preference3);
		
		telegramUserRepository.save(tu1);
		telegramUserRepository.save(tu2);
		telegramUserRepository.save(tu3);
		telegramUserRepository.save(tu4);

	}
	
}
