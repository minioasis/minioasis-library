package org.minioasis.library.service;

import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.domain.search.TelegramUserCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TelegramService {

	// TelegramUser
	boolean isTelegramUserExist(Long chatId);
	void save(TelegramUser entity);
	TelegramUser getTelegramUser(long id);
	void delete(TelegramUser entity);
	void deleteTelegramUser(long id);
	Page<TelegramUser> findAllTelegramUsers(Pageable pageable);
	Page<TelegramUser> findByCriteria(TelegramUserCriteria criteria, Pageable pageable);
	
}
