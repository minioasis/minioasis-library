package org.minioasis.library.telegram;

import org.minioasis.library.domain.Preference;
import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.service.LibraryService;
import org.minioasis.library.service.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
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
	
	private static String START = "*Welcome to the BOT !*\n"
								+ "/register : 1st time ser\n"
								+ "Other commands : /help";
	
	private static String HELP = "*Member :*\n"
								+ "/register : 1st time member\n"
								+ "/checkouts : my checkouts\n"
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
									+ "*#*0415*#*0124444333";
	
	private static String REGISTRATION_FAILED_MESSAGE = "*FAILED*\n"
														+ "> member not found\n"
														+ "> wrong member id or mobile no.";
	
	private static String REGISTRATION_FAILED = "registration failed !";
	
	private static String MOBILE_LENGTH_ERROR = "mobile length > 12  !";
	private static String MOBILE_NOT_NUMBER_ERROR = "mobile has to be number !";
	private static String VERIFICATION_SUCCESS = "verification success !";
	private static String ALREADY_REGISTERED = "already registered !";
	
	@Override
	public void onUpdateReceived(Update update) {	

		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			
			sendMessage("/start", update, START);
			
			sendMessage("/help", update, HELP);
			
			registerMessage("/register", update, REGISTER);

			registration(update);	

		} else if (update.hasCallbackQuery()) {

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
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + ALREADY_REGISTERED + " ] ");
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
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + MOBILE_LENGTH_ERROR + " ]");
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
						logger.info("TELEGRAM LOG : " + chat_id + " - [ " + MOBILE_NOT_NUMBER_ERROR + " ]");
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
						logger.info("TELEGRAM LOG : " + chat_id + " - [ " + VERIFICATION_SUCCESS + " ]");
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
					
				} catch (DataIntegrityViolationException eive) {	
					
					message.setText(ALREADY_REGISTERED);	
					
					try {
						execute(message);
						logger.info("TELEGRAM LOG : " + chat_id + " - [ " + ALREADY_REGISTERED + " ]");
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}			
				}
				
			}else {
				
				message.setText(REGISTRATION_FAILED_MESSAGE).setParseMode(ParseMode.MARKDOWN);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : " + chat_id + " - [ " + REGISTRATION_FAILED + " ]");
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
