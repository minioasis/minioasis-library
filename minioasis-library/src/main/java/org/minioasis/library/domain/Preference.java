package org.minioasis.library.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Preference implements Serializable {

	private static final long serialVersionUID = -2696610857283010537L;

	// due
	@Column(name = "reminder")
	private boolean reminder = false;
	
	@Column(name = "dont_remind_again")
	private boolean dontRemindAgain = false;
	
	@Column(name = "remind_again_in_the_last_day")
	private boolean remindAgainInTheLastDay = false;	
	
	// event
	@Column(name = "sendme_events")
	private boolean sendMeEvent = false;
	
	// new release
	@Column(name = "sendme_new_release")
	private boolean sendMeNewRelease = false;
	
	// announcement
	@Column(name = "sendme_annoucement")
	private boolean sendMeAnnouncement = false;
	
	// new article
	@Column(name = "sendme_article")
	private boolean sendMeArticle = false;
	
	// promotion
	@Column(name = "sendme_promotion")
	private boolean sendMePromotion = false;
	
	public boolean isReminder() {
		return reminder;
	}
	public void setReminder(boolean reminder) {
		this.reminder = reminder;
	}
	public boolean isDontRemindAgain() {
		return dontRemindAgain;
	}
	public void setDontRemindAgain(boolean dontRemindAgain) {
		this.dontRemindAgain = dontRemindAgain;
	}
	public boolean isRemindAgainInTheLastDay() {
		return remindAgainInTheLastDay;
	}
	public void setRemindAgainInTheLastDay(boolean remindAgainInTheLastDay) {
		this.remindAgainInTheLastDay = remindAgainInTheLastDay;
	}
	public boolean isSendMeEvent() {
		return sendMeEvent;
	}
	public void setSendMeEvent(boolean sendMeEvent) {
		this.sendMeEvent = sendMeEvent;
	}
	public boolean isSendMeNewRelease() {
		return sendMeNewRelease;
	}
	public void setSendMeNewRelease(boolean sendMeNewRelease) {
		this.sendMeNewRelease = sendMeNewRelease;
	}
	public boolean isSendMeAnnouncement() {
		return sendMeAnnouncement;
	}
	public void setSendMeAnnouncement(boolean sendMeAnnouncement) {
		this.sendMeAnnouncement = sendMeAnnouncement;
	}
	public boolean isSendMeArticle() {
		return sendMeArticle;
	}
	public void setSendMeArticle(boolean sendMeArticle) {
		this.sendMeArticle = sendMeArticle;
	}
	public boolean isSendMePromotion() {
		return sendMePromotion;
	}
	public void setSendMePromotion(boolean sendMePromotion) {
		this.sendMePromotion = sendMePromotion;
	}
	
}
