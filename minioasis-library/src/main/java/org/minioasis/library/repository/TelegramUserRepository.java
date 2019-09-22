package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

public interface TelegramUserRepository extends RevisionRepository<TelegramUser, Long, Integer>, JpaRepository<TelegramUser, Long> , TelegramUserRepositoryCustom {

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM TelegramUser u WHERE u.chatId = ?1")
	boolean isTelegramUserExist(Long chatId);
	
	@Query("SELECT u FROM TelegramUser u WHERE u.chatId = ?1")
	TelegramUser findTelegramUserByChatId(Long chatId);
	
	@Query("SELECT u FROM TelegramUser u WHERE u.cardKey = ?1")
	TelegramUser findTelegramUserByCardKey(String cardKey);
	
	@Query("SELECT u FROM TelegramUser u WHERE u.preference.sendMeAnnouncement = 'Y'")
	List<TelegramUser> findAllTelegramUsersByAnnoucementOn();
}
