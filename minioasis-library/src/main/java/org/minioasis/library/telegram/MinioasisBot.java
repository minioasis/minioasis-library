package org.minioasis.library.telegram;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	public void onUpdateReceived(Update update) {
		
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {

			Long chat_id = update.getMessage().getChatId();
			
			if (update.getMessage().getText().equals("/start")) {
				logger.info("getMessageId ----------------" + update.getMessage().getMessageId());
				// Create a message object object
				SendMessage message = new SendMessage()
											.setChatId(chat_id)
											.setText("*Welcome to the BOT !*\n"
													+ "/register : 1st time ser\n"
													+ "Other commands : /help")
											.setParseMode(ParseMode.MARKDOWN);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : [/start] "+ chat_id);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} 
			
			if (update.getMessage().getText().equals("/help")) {
				// Create a message object object
				SendMessage message = new SendMessage()
											.setChatId(chat_id)
											.setText("*Member :*\n"
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
													+ "/settings : settings")
											.setParseMode(ParseMode.MARKDOWN);
				
				try {
					execute(message);
					logger.info("TELEGRAM LOG : [/help] "+ chat_id);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
			
			if (update.getMessage().getText().equals("/register")) {
				
				// Create a message object object
				SendMessage message = new SendMessage()
											.setChatId(chat_id)
											.setText("Key in your\n"
													+ "*1) 4-digit member id*\n"
													+ "*2) mobile no.*\n"
													+ "in the following format :"
													+ "\n\n"
													+ "*#*0415*#*0124444333")
											.setParseMode(ParseMode.MARKDOWN);
				
				try {
					execute(message);
					
					logger.info("TELEGRAM LOG : [/register] "+ chat_id);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} 
			
			if (update.getMessage().getText().startsWith("#") && 
					update.getMessage().getText().startsWith("#", 5)) {
				
				String msg = update.getMessage().getText();
				String cardKey = msg.substring(1,5);
				String mobile = msg.substring(6);
				
				boolean exist = libraryService.match(cardKey, mobile);
				
				TelegramUser telegramUser = new TelegramUser(chat_id, cardKey, new Preference(true,false,false,false,false,false,false,false));

				// Create a message object object
				SendMessage message = new SendMessage().setChatId(chat_id);
						
				if(exist) {
					
					
					
					try {
						
						telegramService.save(telegramUser);
						message.setText("verification success !");
						
						try {
							execute(message);
							logger.info("Telegram INFO : [verification success !] " + chat_id);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
						
					} catch (DataIntegrityViolationException eive) {
						
						message.setText("already registered !");
						
						try {
							execute(message);
							logger.info("Telegram INFO : [already registered !] " + chat_id);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
						
					}
					
				}else {
					
					message.setText("not successful\n"
									+ "1) no such member\n"
									+ "2) wrong member id or mobile no. !");
					
					try {
						execute(message);
						logger.info("Telegram INFO : [not successful] " + chat_id);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
					
				}

			}
			

		} else if (update.hasCallbackQuery()) {

			String call_data = update.getCallbackQuery().getData();
			long message_id = update.getCallbackQuery().getMessage().getMessageId();
			long chat_id = update.getCallbackQuery().getMessage().getChatId();

			if (call_data.equals("callback_1")) {
				
				String answer = " Hi....WOWOWOWOWOW";
				
				EditMessageText new_message = new EditMessageText()
													.setChatId(chat_id)
													.setMessageId(toIntExact(message_id))
													.setText(answer);
				try {
					execute(new_message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
			
			if (call_data.equals("callback_2")) {
				
				String answer = " Hi.... This is the 2nd button";
				
				EditMessageText new_message = new EditMessageText()
													.setChatId(chat_id)
													.setMessageId(toIntExact(message_id))
													.setText(answer);
				try {
					execute(new_message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
			
			if (call_data.equals("callback_3")) {
				
				String answer = " Hi.... This is the 3th button";
				
				EditMessageText new_message = new EditMessageText()
													.setChatId(chat_id)
													.setMessageId(toIntExact(message_id))
													.setText(answer);
				try {
					execute(new_message);
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
