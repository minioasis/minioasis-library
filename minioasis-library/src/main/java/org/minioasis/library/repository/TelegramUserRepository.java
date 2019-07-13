package org.minioasis.library.repository;

import org.minioasis.library.domain.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> , TelegramUserRepositoryCustom {

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM TelegramUser u WHERE u.chatId = ?1")
	boolean isTelegramUserExist(Long chatId);
	
	@Query("SELECT u FROM TelegramUser u WHERE u.chatId = ?1")
	TelegramUser findTelegramUserByChatId(Long chatId);
}
