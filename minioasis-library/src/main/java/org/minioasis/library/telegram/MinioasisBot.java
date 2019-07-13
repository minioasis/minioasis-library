package org.minioasis.library.telegram;

import static java.lang.Math.toIntExact;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.Preference;
import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.domain.search.BiblioCriteria;
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
	
	private static int pageSize = 2;
	
	private static String START = "*Welcome to the BOT !*\n"
								+ "/register : 1st time user\n"
								+ "Other commands : /help";
	
	private static String HELP = "*Member :*\n"
								+ "/register : 1st time member\n"
								+ "/due : my checkouts\n"
								+ "/renew : renew my books\n"
								+ "/reservation : reservation of book"
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

			registration(update);
			
			checkouts("/due", update);
			
			search(update);

		} else if (update.hasCallbackQuery()) {

			long chat_id = update.getCallbackQuery().getMessage().getChatId();
			long message_id = update.getCallbackQuery().getMessage().getMessageId();
			
			String call_data = update.getCallbackQuery().getData();
			
			if(call_data.startsWith("/p^g^^g?")) {
				
				String extractedParameters = StringUtils.substringAfter(call_data,"/p^g^^g?");
				String parameters[] = extractedParameters.split("--");
				
				String arrow = parameters[0];
				
				if(arrow.equals("<") || arrow.equals(">")) {
					
					int page = Integer.parseInt(parameters[1]);
					String keyword = parameters[2];
					
					BiblioCriteria criteria = new BiblioCriteria();			
					criteria.setKeyword1(keyword);
					
					Pageable pageable = PageRequest.of(page, pageSize);
					
					Page<Biblio> biblioPage = libraryService.findByCriteria(criteria, pageable);
					List<Biblio> biblios = biblioPage.getContent();
					int total = biblios.size();
					
					EditMessageText new_message = new EditMessageText()
							.setChatId(chat_id)
							.setMessageId(toIntExact(message_id));
					
					String view = searchView(biblios);
					new_message.setText(view)
								.setParseMode(ParseMode.MARKDOWN);
					
					System.out.println(view);
					
					new_message.setReplyMarkup(createInlinePagingButtons(page,total,keyword));			

					try {
						execute(new_message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
					
				}else {
					

				}			
			}
		}	
	}
	
	private InlineKeyboardMarkup createInlinePagingButtons(int page, long total, String keyword) {
		
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		
		rowsInline.add(rowInline);
		
		// parameters format (with order) : arrow--page--total--keyword , e.g. >--2--45--feymman
		if(page != 0) {
			rowInline.add(new InlineKeyboardButton().setText("<").setCallbackData("/p^g^^g?" + "<" + "--" + (page-1) + "--" + keyword));
		}

		for(int i = 1; i < pageSize + 1; i++) {
			String num = String.valueOf(page*pageSize + i);
			rowInline.add(new InlineKeyboardButton().setText(num).setCallbackData("/p^g^^g?" + num + "--" + page + "--" + keyword));
		}

		if(page*pageSize < total) {
			rowInline.add(new InlineKeyboardButton().setText(">").setCallbackData("/p^g^^g?" + ">" + "--" + (page+1) + "--" + keyword));
		}
		
		// Add it to the message
		markupInline.setKeyboard(rowsInline);
		
		return markupInline;
	}
	
	private static String checkoutsView(String cardKey, List<Checkout> checkouts) {
		
		Integer total = checkouts.size();
		int i = 1;
		
		StringBuffer s = new StringBuffer();
		
		if(total < 1) {
			s.append("You have no borrowing.");
		}else {
			
			Checkout c1 = checkouts.get(0);

			LocalDate end = c1.getPatron().getEndDate();
			
			s.append("_Member :_ *" + cardKey + "*    _Exp : " + end + "_\n");
			s.append("_Total : " + total + "_\n");
			s.append("-------------------------------------------------------\n");

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
	
	private static String searchView(List<Biblio> biblios) {

		Integer total = biblios.size();
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
	
	private void search(Update update) {
		
		//there must be a SPACE between the /search command and search keyword !
		
		String message = update.getMessage().getText();
		
		boolean searchCommand = message.startsWith("/search ");

		if(searchCommand){
			
			String keyword = message.substring(8);
			
			if(!keyword.equals("")) {
				
				BiblioCriteria criteria = new BiblioCriteria();			
				criteria.setKeyword1(keyword);
				
				int page = 0;
				
				Pageable pageable = PageRequest.of(page, pageSize);
				
				Page<Biblio> biblioPage = libraryService.findByCriteria(criteria, pageable);
				List<Biblio> biblios = biblioPage.getContent();
				long total = biblioPage.getTotalElements();

				Long chat_id = update.getMessage().getChatId();	
				SendMessage new_message = new SendMessage().setChatId(chat_id);
				
				new_message.setText(searchView(biblios))
						.setParseMode(ParseMode.MARKDOWN);
				
				new_message.setReplyMarkup(createInlinePagingButtons(page,total,keyword));
				
				try {
					
					execute(new_message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ /search ] ");
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}			
			}			
		} 
	}
	
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
	
	private void registration(Update update) {
		
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
