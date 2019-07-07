package org.minioasis.library.repository;

import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.domain.search.TelegramUserCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TelegramUserRepositoryCustom {

	Page<TelegramUser> findByCriteria(TelegramUserCriteria criteria, Pageable pageable);
}
