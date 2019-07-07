package org.minioasis.library.repository;

import org.minioasis.library.domain.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> , TelegramUserRepositoryCustom {

}
