package org.minioasis.library.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.domain.search.TelegramUserCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.minioasis.library.jooq.tables.TelegramUser.TELEGRAM_USER;

import java.util.List;
import java.util.Set;;

public class TelegramUserRepositoryImpl implements TelegramUserRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.TelegramUser tu = TELEGRAM_USER.as("tu");
	
	public Page<TelegramUser> findByCriteria(TelegramUserCriteria criteria, Pageable pageable){

		Table<?> table = tu;
	
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), TelegramUser.class);
		setBindParameterValues(q, jooqQuery);
		
		List<TelegramUser> list = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(list, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(TelegramUserCriteria criteria) {

		Table<?> table = tu;
		
        long total = dsl.fetchCount(
        						dsl.select(tu.ID)
        						.from(table)
        						.where(condition(criteria))
        );

        return total;
    }
	
	private static void setBindParameterValues(Query hibernateQuery, org.jooq.Query jooqQuery) {
	    List<Object> values = jooqQuery.getBindValues();
	    for (int i = 0; i < values.size(); i++) {
	        hibernateQuery.setParameter(i + 1, values.get(i));
	    }
	}
	
	private Condition condition(TelegramUserCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final String cardKey = criteria.getCardKey();
		final Long chatId = criteria.getChatId();

		final Set<Boolean> reminders = criteria.getReminders();
		final Set<Boolean> dontRemindAgains = criteria.getDontRemindAgains();
		final Set<Boolean> remindAgainInTheLastDays = criteria.getRemindAgainInTheLastDays();
		final Set<Boolean> sendMeEvents = criteria.getSendMeEvents();
		final Set<Boolean> sendMeNewReleases = criteria.getSendMeNewReleases();
		final Set<Boolean> sendMeAnnouncements = criteria.getSendMeAnnouncements();
		final Set<Boolean> sendMeArticles = criteria.getSendMeArticles();
		final Set<Boolean> sendMePromotions = criteria.getSendMePromotions();
		
	    if (cardKey != null) {
	    	condition = condition.and(tu.CARD_KEY.eq(cardKey));			
	    }
	    if (chatId != null) {
	    	condition = condition.and(tu.CHAT_ID.eq(chatId));			
	    }
		if(reminders != null && reminders.size() > 0){
			condition = condition.and(tu.REMINDER.in(reminders));
		}
		if(dontRemindAgains != null && dontRemindAgains.size() > 0){
			condition = condition.and(tu.DONT_REMIND_AGAIN.in(dontRemindAgains));
		}
		if(remindAgainInTheLastDays != null && remindAgainInTheLastDays.size() > 0){
			condition = condition.and(tu.REMIND_AGAIN_IN_THE_LAST_DAY.in(remindAgainInTheLastDays));
		}
		if(sendMeEvents != null && sendMeEvents.size() > 0){
			condition = condition.and(tu.SENDME_EVENTS.in(sendMeEvents));
		}
		if(sendMeNewReleases != null && sendMeNewReleases.size() > 0){
			condition = condition.and(tu.SENDME_NEW_RELEASE.in(sendMeNewReleases));
		}
		if(sendMeAnnouncements != null && sendMeAnnouncements.size() > 0){
			condition = condition.and(tu.SENDME_ANNOUCEMENT.in(sendMeAnnouncements));
		}
		if(sendMeArticles != null && sendMeArticles.size() > 0){
			condition = condition.and(tu.SENDME_ARTICLE.in(sendMeArticles));
		}
		if(sendMePromotions != null && sendMePromotions.size() > 0){
			condition = condition.and(tu.SENDME_PROMOTION.in(sendMePromotions));
		}
	
	    return condition;
	}
	
}
