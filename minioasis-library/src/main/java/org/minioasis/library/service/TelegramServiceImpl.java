package org.minioasis.library.service;

import java.util.List;

import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.domain.search.TelegramUserCriteria;
import org.minioasis.library.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public class TelegramServiceImpl implements TelegramService{

	@Autowired
	private TelegramUserRepository telegramUserRepository;
	
	public boolean isTelegramUserExist(Long chatId) {
		return this.telegramUserRepository.isTelegramUserExist(chatId);
	}
	public void save(TelegramUser entity){
		this.telegramUserRepository.save(entity);
	}
	public TelegramUser getTelegramUser(long id) {
		return this.telegramUserRepository.getOne(id);
	}
	public TelegramUser findTelegramUserByChatId(Long chatId) {
		return this.telegramUserRepository.findTelegramUserByChatId(chatId);
	}
	public TelegramUser findTelegramUserByCardKey(String cardKey) {
		return this.telegramUserRepository.findTelegramUserByCardKey(cardKey);
	}
	public void delete(TelegramUser entity) {
		this.telegramUserRepository.delete(entity);
	}
	public void deleteTelegramUser(long id) {
		this.telegramUserRepository.deleteById(id);
	}
	public List<TelegramUser> findAllTelegramUsersByAnnoucementOn(){
		return this.telegramUserRepository.findAllTelegramUsersByAnnoucementOn();
	}
	public Page<TelegramUser> findAllTelegramUsers(Pageable pageable){
		return this.telegramUserRepository.findAll(pageable);
	}
	public Page<TelegramUser> findByCriteria(TelegramUserCriteria criteria, Pageable pageable){
		return this.telegramUserRepository.findByCriteria(criteria, pageable);
	}
}
