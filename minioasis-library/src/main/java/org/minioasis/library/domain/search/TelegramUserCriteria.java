package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class TelegramUserCriteria implements Serializable {

	private static final long serialVersionUID = 968113969064430885L;
	
	private Long chatId;
	private String cardKey;
	
	private Set<Boolean> reminders = new HashSet<Boolean>();
	private Set<Boolean> dontRemindAgains = new HashSet<Boolean>();
	private Set<Boolean> remindAgainInTheLastDays = new HashSet<Boolean>();	
	// event
	private Set<Boolean> sendMeEvents = new HashSet<Boolean>();
	// new release
	private Set<Boolean> sendMeNewReleases = new HashSet<Boolean>();
	// announcement
	private Set<Boolean> sendMeAnnouncements = new HashSet<Boolean>();
	// new article
	private Set<Boolean> sendMeArticles = new HashSet<Boolean>();
	// promotion
	private Set<Boolean> sendMePromotions = new HashSet<Boolean>();
	
	public Long getChatId() {
		return chatId;
	}
	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}
	public String getCardKey() {
		return cardKey;
	}
	public void setCardKey(String cardKey) {
		this.cardKey = cardKey;
	}
	public Set<Boolean> getReminders() {
		return reminders;
	}
	public void setReminders(Set<Boolean> reminders) {
		this.reminders = reminders;
	}
	public Set<Boolean> getDontRemindAgains() {
		return dontRemindAgains;
	}
	public void setDontRemindAgains(Set<Boolean> dontRemindAgains) {
		this.dontRemindAgains = dontRemindAgains;
	}
	public Set<Boolean> getRemindAgainInTheLastDays() {
		return remindAgainInTheLastDays;
	}
	public void setRemindAgainInTheLastDays(Set<Boolean> remindAgainInTheLastDays) {
		this.remindAgainInTheLastDays = remindAgainInTheLastDays;
	}
	public Set<Boolean> getSendMeEvents() {
		return sendMeEvents;
	}
	public void setSendMeEvents(Set<Boolean> sendMeEvents) {
		this.sendMeEvents = sendMeEvents;
	}
	public Set<Boolean> getSendMeNewReleases() {
		return sendMeNewReleases;
	}
	public void setSendMeNewReleases(Set<Boolean> sendMeNewReleases) {
		this.sendMeNewReleases = sendMeNewReleases;
	}
	public Set<Boolean> getSendMeAnnouncements() {
		return sendMeAnnouncements;
	}
	public void setSendMeAnnouncements(Set<Boolean> sendMeAnnouncements) {
		this.sendMeAnnouncements = sendMeAnnouncements;
	}
	public Set<Boolean> getSendMeArticles() {
		return sendMeArticles;
	}
	public void setSendMeArticles(Set<Boolean> sendMeArticles) {
		this.sendMeArticles = sendMeArticles;
	}
	public Set<Boolean> getSendMePromotions() {
		return sendMePromotions;
	}
	public void setSendMePromotions(Set<Boolean> sendMePromotions) {
		this.sendMePromotions = sendMePromotions;
	}

}
