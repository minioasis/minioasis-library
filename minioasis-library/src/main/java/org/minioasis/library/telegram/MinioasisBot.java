package org.minioasis.library.telegram;

import static java.lang.Math.toIntExact;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.ItemState;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.Photo;
import org.minioasis.library.domain.Preference;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationResult;
import org.minioasis.library.domain.ReservationState;
import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.domain.search.BiblioCriteria;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.library.repository.PhotoRepository;
import org.minioasis.library.service.LibraryService;
import org.minioasis.library.service.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MinioasisBot extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(MinioasisBot.class);
	
	@Value("${telegrambot.token}")
	private String token;
	
	@Value("${telegrambot.username}")
	private String username;
	
	@Autowired
	private TelegramService telegramService;
	
	@Autowired
	private LibraryService libraryService;
	
	@Autowired
	private PhotoRepository photoRepository;
	
	private static int pageSize = 3;
	
	private static String START = "*Welcome to the BOT !*\n"
								+ "/register : 1st time user\n"
								+ "Other commands : /help";
	
	private static String HELP = "*Member :*\n"
								+ "/register : 1st time member\n"
								+ "/due : my checkouts\n"
								+ "/renew : renew my books\n"
								+ "/reservation : my reservations"
								+ "\n\n"
								+ "*Public User :*\n"
								+ "/search : search books by title, author\n"
								+ "/recommendation : recommendation of book"
								+ "\n\n"
								+ "*Library Information :*\n"
								+ "/openinghours : opening hours\n"
								+ "/holidays : library holidays\n"
								+ "/news : library news\n"
								+ "/releases : new book releases\n"
								+ "/annoucements : new annoucements\n"
								+ "/events : library events\n"
								+ "/articles : blog articles\n"
								+ "/bookstoread : books recommended by library\n"
								+ "/promotions : library promotions"
								+ "\n\n"
								+ "*Settings :*\n"
								+ "/settings : settings";
	
	private static String REGISTER = "Key in your\n" 
									+ "*1) 4-digit member id*\n"
									+ "*2) mobile no.*\n"
									+ "in the following format :"
									+ "\n\n" 
									+ "#*memberId*#*mobile*\n"
									+ "example: #*0415*#*0124444333*";
	
	private static String REGISTRATION_FAILED_MESSAGE = "*FAILED*\n"
														+ "> member with this mobile not found";
	
	private static String REGISTRATION_FAILED = "registration failed !";
	
	private static String MOBILE_LENGTH_ERROR = "mobile length > 12  !";
	private static String MOBILE_NOT_NUMBER_ERROR = "mobile has to be number !";
	private static String VERIFICATION_SUCCESS = "verification success !";
	private static String ALREADY_REGISTERED = "already registered !";
	private static String MEMBER_NOT_FOUND = "member not found !";
	
	@Override
	public void onUpdateReceived(Update update) {	

		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			
			sendMessage("/start", update, START);
			
			sendMessage("/help", update, HELP);
			
			registerMessage("/register", update, REGISTER);

			registrationVerification(update);
			
			checkouts("/due", update);
			
			renewAll("/renew", update);
			
			reservations("/reservation", update);
			
			search("/search", update);

		} else if (update.hasCallbackQuery()) {

			long chat_id = update.getCallbackQuery().getMessage().getChatId();
			long message_id = update.getCallbackQuery().getMessage().getMessageId();
			
			String call_data = update.getCallbackQuery().getData();
			
			if(call_data.startsWith("/p^g^^g?")) {
				pagingCallBack(call_data, chat_id, message_id);
			}
			
			if(call_data.startsWith("/biblioinfo")) {
				biblioInfoCallBack(call_data, chat_id, message_id);
			}

			if(call_data.startsWith("/r^s^rv^?")) {
				reserveCallBack(call_data, chat_id, message_id);
			}
			
		}	
	}

	// [/reservation]
	private void reservations(String command, Update update) {
		
		if(update.getMessage().getText().equals(command)){
			
			Long chat_id = update.getMessage().getChatId();	
			
			SendMessage message = new SendMessage().setChatId(chat_id);
			TelegramUser telegramUser = telegramService.findTelegramUserByChatId(chat_id);

			if(telegramUser == null) {
				
				message.setText(MEMBER_NOT_FOUND);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + command + " ] " + MEMBER_NOT_FOUND);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				
			}else {
				
				String cardKey = telegramUser.getCardKey();
	
				List<Reservation> rs = libraryService.findByCardKeyAndStates(cardKey, ReservationState.getActives());
				
				message.setText(reservationsView(cardKey, rs))
					   .setParseMode(ParseMode.MARKDOWN);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + command + " ] ");
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	// [/reservation] reservation view
	private static String reservationsView(String cardKey, List<Reservation> reservations) {
		
		Integer total = reservations.size();
		int i = 1;
		
		StringBuffer s = new StringBuffer();
		
		if(total < 1) {
			s.append("You have no reservation.");
		}else {
			
			Reservation r1 = reservations.get(0);

			LocalDate end = r1.getPatron().getEndDate();
			
			s.append("-------------------------------------------------------------\n");
			s.append("_Member :_ *" + cardKey + "* _[ Exp : " + end + " ]_\n");
			s.append("-------------------------------------------------------------\n");
			s.append("_Total : " + total + "                 Date : " + LocalDate.now() + "_\n");
			s.append("\n");
			
			for(Reservation r : reservations) {

				String title = r.getBiblio().getTitle();
				LocalDateTime reservationDate = r.getReservationDate();
				
				s.append(i + ". _" + title + "_\n");
				s.append("    _Reservation : " + reservationDate.toLocalDate() + "_\n");
				i++;
			}
		}
		
		return s.toString();
	}
	
	// [/search] view
	private static String searchView(Page<Biblio> page) {

		long total = page.getTotalElements();
		List<Biblio> biblios = page.getContent();
		
		int i = 1;
		
		StringBuffer s = new StringBuffer();
		
		if(total < 1) {
			s.append("No book found.");
		}else {
			s.append("_Total books found : " + total + "_\n");
			s.append("-------------------------------------------------------\n");
			
			for(Biblio b : biblios) {
				String title = b.getTitle();
				s.append(i + ". _" + title + "_\n");
				i++;
			}
		}
		
		return s.toString(); 
	}
	
	// [reserve button]
	private InlineKeyboardMarkup createInlineReserveButton(long bid) {
		
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		
		rowInline.add(new InlineKeyboardButton().setText("RESERVE THE BOOK").setCallbackData("/r^s^rv^?" + bid));
		rowsInline.add(rowInline);
		
		// Add it to the message
		markupInline.setKeyboard(rowsInline);
		
		return markupInline;
	}
	
	// [create reserve callback buttons]
	private void reserveCallBack(String call_data, long chat_id, long message_id) {

		String bid = StringUtils.substringAfter(call_data, "/r^s^rv^?");

		TelegramUser telegramUser = telegramService.findTelegramUserByChatId(chat_id);
		String cardKey = telegramUser.getCardKey();

		final LocalDateTime nowLDT = LocalDateTime.now();
		final LocalDate now = nowLDT.toLocalDate();

		Biblio biblio = this.libraryService.getBiblioFetchItems(Long.parseLong(bid));

		Patron patron = this.libraryService.preparingPatronForCirculation(cardKey, now);

		ReservationResult result = null;
		SendMessage new_message = new SendMessage().setChatId(chat_id);

		try {

			result = libraryService.reserve(patron, biblio, nowLDT, now.plusDays(90));

			if (result.getReservation() != null) {
				new_message.setText("RESERVED ! /reservation");
			}

		} catch (LibraryException lex) {
			new_message.setText("reservation FAILED !");
		}

		try {
			execute(new_message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

	}
	
	// [/search] create inline buttons
	private InlineKeyboardMarkup createInlinePagingButtons(Page<Biblio> biblioPage, String keyword) {
		
		List<Biblio> biblios = biblioPage.getContent();
		int page = biblioPage.getPageable().getPageNumber();
		long total = biblioPage.getTotalElements();
		int pageSize = biblioPage.getSize();
		
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		
		rowsInline.add(rowInline);
		
		// parameters format (with order) : arrow--page--total--keyword , e.g. >--2--45--feymman
		if(page != 0) {
			rowInline.add(new InlineKeyboardButton().setText("<").setCallbackData("/p^g^^g?" + "<" + "--" + (page-1) + "--" + keyword));
		}

		for (int i = 0; i < biblios.size(); i++) {
			if(page*pageSize + (i-1) <= total) {
				String num = String.valueOf(page*pageSize + (i+1));
				rowInline.add(new InlineKeyboardButton().setText(num).setCallbackData("/biblioinfo." + biblios.get(i).getIsbn()));
			}
		}
		
		if((page + 1)*pageSize  < total) {
			rowInline.add(new InlineKeyboardButton().setText(">").setCallbackData("/p^g^^g?" + ">" + "--" + (page+1) + "--" + keyword));
		}
		
		// Add it to the message
		markupInline.setKeyboard(rowsInline);
		
		return markupInline;
	}

	// [/search] create callback buttons -> <previous,next> 
	private void pagingCallBack(String call_data, long chat_id, long message_id) {
		
		String extractedPagingParameters = StringUtils.substringAfter(call_data,"/p^g^^g?");
		String parameters[] = extractedPagingParameters.split("--");
		
		String arrow = parameters[0];
		
		if(arrow.equals("<") || arrow.equals(">")) {

			int page = Integer.parseInt(parameters[1]);
			String keyword = parameters[2];
			
			BiblioCriteria criteria = new BiblioCriteria();			
			criteria.setKeyword1(keyword);
			
			Pageable pageable = PageRequest.of(page, pageSize);
			
			Page<Biblio> biblioPage = libraryService.findByCriteria(criteria, pageable);
			
			EditMessageText new_message = new EditMessageText()
					.setChatId(chat_id)
					.setMessageId(toIntExact(message_id));

			new_message.setText(searchView(biblioPage))
						.setParseMode(ParseMode.MARKDOWN);
			
			new_message.setReplyMarkup(createInlinePagingButtons(biblioPage,keyword));			

			try {
				execute(new_message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	// [/search] create callback buttons -> 1,2,3,4,5.. 
	private void biblioInfoCallBack(String call_data, long chat_id, long message_id) {
		
		String isbn = StringUtils.substringAfter(call_data,"/biblioinfo.");
		
		Biblio biblio = libraryService.findByIsbn(isbn);
		
		String[] states = new String[1];
		states[0] = ItemState.IN_LIBRARY.toString();
		List<Item> items = libraryService.findItemsByIsbn(isbn);

		biblio.setItems(items);

		try {
			
			Photo photo = getPhoto(isbn);
			
			if(photo != null) {
				
				URL imgUrl = new URL(photo.getUrl());
				InputStream in = imgUrl.openStream();

				SendPhoto new_message = new SendPhoto()
										.setChatId(chat_id)
										.setPhoto(isbn, in)
										.setCaption(biblioView(biblio))
										.setParseMode(ParseMode.MARKDOWN);
				
				new_message.setReplyMarkup(createInlineReserveButton(biblio.getId()));	
				
				try {
					execute(new_message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ /biblioinfo : "+ isbn + " ] ");
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}

				
			}else {
				
				SendMessage new_message = new SendMessage()
						.setChatId(chat_id)
						.setText(biblioView(biblio))
						.setParseMode(ParseMode.MARKDOWN);
				
				try {
					execute(new_message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ /biblioinfo : "+ isbn + " ] ");
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
			
		}catch(IOException ex) {

			logger.info("TELEGRAM LOG : " + chat_id + " - [ /biblioinfo : IOException ] ");
			
		}
	}
	
	// [/search] button 1,2,3,4,5's biblio view
	private static String biblioView(Biblio biblio) {

		List<Item> items = biblio.getItems();
		
		StringBuffer s = new StringBuffer();
		
		s.append("_*" + biblio.getTitle() + "*_\n");
		s.append("\n");
		s.append("*Author*\n");
		s.append("_" + biblio.getAuthor() + "_\n");
		s.append("\n");
		s.append("*Publisher*\n");
		s.append("_" + biblio.getPublisher().getName() + "_\n");
		s.append("\n");
		s.append("*Status*\n");
		
		String state = "";
		
		for (int i = 0; i < items.size(); i++) {
			state = items.get(i).getState().getState();
			s.append((i+1) + ". " + state.replaceAll("_", " ") + "\n");
		}
		return s.toString();
	}
	
	private Photo getPhoto(String isbn){
		
		Photo photo = null;
		
		try {
			photo = this.photoRepository.findBiblioThumbnailByIsbn(isbn);
		} catch(ConnectException cex) {
			logger.info("MINIO LOG : Connection failed !");
		} catch (Exception ex) {
			return photo;
		}
		
		return photo;
	}
	
	// [/search]
	private void search(String command, Update update) {
		
		//there must be a SPACE between the /search command and search keyword !
		
		String message = update.getMessage().getText();
		
		boolean searchCommand = message.startsWith(command);

		if(searchCommand && (message.length() > 7)){

			String keyword = message.substring(8);
			
			if(!keyword.equals("")) {
				
				BiblioCriteria criteria = new BiblioCriteria();			
				criteria.setKeyword1(keyword);
				
				int page = 0;
				
				Pageable pageable = PageRequest.of(page, pageSize);
				
				Page<Biblio> biblioPage = libraryService.findByCriteria(criteria, pageable);

				Long chat_id = update.getMessage().getChatId();	
				SendMessage new_message = new SendMessage().setChatId(chat_id);
				
				new_message.setText(searchView(biblioPage))
						.setParseMode(ParseMode.MARKDOWN);
				
				new_message.setReplyMarkup(createInlinePagingButtons(biblioPage,keyword));
				
				try {
					
					execute(new_message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ /search ] ");
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}			
			}			
		} 
	}
	
	// [/renew] 
	private void renewAll(String command, Update update) {
		
		if(update.getMessage().getText().equals(command)){
			
			Long chat_id = update.getMessage().getChatId();	
			SendMessage message = new SendMessage().setChatId(chat_id);
			TelegramUser telegramUser = telegramService.findTelegramUserByChatId(chat_id);

			if(telegramUser == null) {
				
				message.setText(MEMBER_NOT_FOUND);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + command + " ] " + MEMBER_NOT_FOUND);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				
			}else {
				String cardKey = telegramUser.getCardKey();
				final LocalDate now = LocalDate.now();
				
				Patron patron = this.libraryService.preparingPatronForCirculation(cardKey, now);
				libraryService.renewAll(patron, now);
				
				List<Checkout> checkouts = patron.getCheckouts();
				
				message.setText(checkoutsView(cardKey, checkouts))
				   .setParseMode(ParseMode.MARKDOWN);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + command + " ] ");
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}	
			}
		}
	}
	
	// [/due]
	private void checkouts(String command, Update update) {
		
		if(update.getMessage().getText().equals(command)){
			
			Long chat_id = update.getMessage().getChatId();	
			
			SendMessage message = new SendMessage().setChatId(chat_id);
			TelegramUser telegramUser = telegramService.findTelegramUserByChatId(chat_id);

			if(telegramUser == null) {
				
				message.setText(MEMBER_NOT_FOUND);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + command + " ] " + MEMBER_NOT_FOUND);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				
			}else {
				
				String cardKey = telegramUser.getCardKey();
	
				List<Checkout> checkouts = libraryService.findAllActiveCheckoutsByCardKey(cardKey);
				
				message.setText(checkoutsView(cardKey, checkouts))
					   .setParseMode(ParseMode.MARKDOWN);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + command + " ] ");
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// [/due] checkout view
	private static String checkoutsView(String cardKey, List<Checkout> checkouts) {
		
		Integer total = checkouts.size();
		int i = 1;
		
		StringBuffer s = new StringBuffer();
		
		if(total < 1) {
			s.append("You have no borrowing.");
		}else {
			
			Checkout c1 = checkouts.get(0);

			LocalDate end = c1.getPatron().getEndDate();
			
			s.append("-------------------------------------------------------------\n");
			s.append("_Member :_ *" + cardKey + "* _[ Exp : " + end + " ]_\n");
			s.append("-------------------------------------------------------------\n");
			s.append("_Total : " + total + "                 Date : " + LocalDate.now() + "_\n");
			s.append("\n");
			
			for(Checkout c : checkouts) {

				String title = c.getItem().getBiblio().getTitle();
				LocalDate dueDate = c.getDueDate();
				
				s.append(i + ". _" + title + "_\n");
				s.append("    _Due: " + dueDate + "_\n");
				i++;
			}
		}
		
		return s.toString();
	}
	
	// [/help]
	private void sendMessage(String command, Update update, String response) {

		if(update.getMessage().getText().equals(command)){
			
			Long chat_id = update.getMessage().getChatId();		
			SendMessage message = new SendMessage()
					.setChatId(chat_id)
					.setText(response)
					.setParseMode(ParseMode.MARKDOWN);
			
			try {
				execute(message);
				logger.info("TELEGRAM LOG : " + chat_id + " - [ " + command + " ] ");
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	// [/register]
	private void registerMessage(String command, Update update, String response) {

		if (update.getMessage().getText().equals(command)) {

			Long chat_id = update.getMessage().getChatId();
			SendMessage message = new SendMessage().setChatId(chat_id);
			boolean exist = telegramService.isTelegramUserExist(chat_id);

			if (exist) {

				message.setText(ALREADY_REGISTERED);

				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + command + " ] " + ALREADY_REGISTERED);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}

			} else {

				message.setText(response).setParseMode(ParseMode.MARKDOWN);

				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + command + " ] ");
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// registration verification
	private void registrationVerification(Update update) {
		
		Long chat_id = update.getMessage().getChatId();
		
		// registration
		if (update.getMessage().getText().startsWith("#") && 
				update.getMessage().getText().startsWith("#", 5)) {

			SendMessage message = new SendMessage().setChatId(chat_id);
			
			String msg = update.getMessage().getText();
			String cardKey = msg.substring(1,5);
			String mobile = msg.substring(6);
			
			// validation
			if(mobile.length() > 12) {
				
				message.setText(MOBILE_LENGTH_ERROR);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [  ] " + MOBILE_LENGTH_ERROR);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				
			}else {
				
				try {
					
					Integer.valueOf(mobile);
					
				}catch(NumberFormatException nfex) {
					
					message.setText(MOBILE_NOT_NUMBER_ERROR);
					
					try {
						execute(message);
						logger.info("TELEGRAM LOG : " + chat_id + " - [  ] " + MOBILE_NOT_NUMBER_ERROR);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}			
				}				
			}
			
			boolean exist = libraryService.match(cardKey, mobile);		
			TelegramUser telegramUser = new TelegramUser(chat_id, cardKey, new Preference(true,false,false,false,false,false,false,false));
					
			if(exist) {
				
				try {
					
					telegramService.save(telegramUser);
					message.setText(VERIFICATION_SUCCESS);
					
					try {
						execute(message);
						logger.info("TELEGRAM LOG : " + chat_id + " - [  ] " + VERIFICATION_SUCCESS);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
					
				} catch (DataIntegrityViolationException eive) {	
					
					message.setText(ALREADY_REGISTERED);	
					
					try {
						execute(message);
						logger.info("TELEGRAM LOG : " + chat_id + " - [  ] " + ALREADY_REGISTERED);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}			
				}
				
			}else {
				
				message.setText(REGISTRATION_FAILED_MESSAGE).setParseMode(ParseMode.MARKDOWN);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [  ] " + REGISTRATION_FAILED);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}	
			}
		}	
	}

	@Override
	public String getBotUsername() {
		return username;
	}

	@Override
	public String getBotToken() {
		return token;
	}

}
