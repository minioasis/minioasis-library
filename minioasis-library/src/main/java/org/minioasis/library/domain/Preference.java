package org.minioasis.library.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Preference implements Serializable {

	private static final long serialVersionUID = -2696610857283010537L;

	// due
	@Column(name = "reminder")
	private YesNo reminder = YesNo.N;
	
	// event
	@Column(name = "sendme_events")
	private YesNo sendMeEvent = YesNo.N;
	
	// new release
	@Column(name = "sendme_new_release")
	private YesNo sendMeNewRelease = YesNo.N;
	
	// announcement
	@Column(name = "sendme_annoucement")
	private YesNo sendMeAnnouncement = YesNo.N;
	
	// new article
	@Column(name = "sendme_article")
	private YesNo sendMeArticle = YesNo.N;
	
	// promotion
	@Column(name = "sendme_promotion")
	private YesNo sendMePromotion = YesNo.N;
	
	public Preference() {
	}
	
	public Preference(YesNo reminder, YesNo sendMeEvent, YesNo sendMeNewRelease, 
			YesNo sendMeAnnouncement, YesNo sendMeArticle, YesNo sendMePromotion) {
		super();
		this.reminder = reminder;
		this.sendMeEvent = sendMeEvent;
		this.sendMeNewRelease = sendMeNewRelease;
		this.sendMeAnnouncement = sendMeAnnouncement;
		this.sendMeArticle = sendMeArticle;
		this.sendMePromotion = sendMePromotion;
	}

	public YesNo getReminder() {
		return reminder;
	}

	public void setReminder(YesNo reminder) {
		this.reminder = reminder;
	}

	public YesNo getSendMeEvent() {
		return sendMeEvent;
	}

	public void setSendMeEvent(YesNo sendMeEvent) {
		this.sendMeEvent = sendMeEvent;
	}

	public YesNo getSendMeNewRelease() {
		return sendMeNewRelease;
	}

	public void setSendMeNewRelease(YesNo sendMeNewRelease) {
		this.sendMeNewRelease = sendMeNewRelease;
	}

	public YesNo getSendMeAnnouncement() {
		return sendMeAnnouncement;
	}

	public void setSendMeAnnouncement(YesNo sendMeAnnouncement) {
		this.sendMeAnnouncement = sendMeAnnouncement;
	}

	public YesNo getSendMeArticle() {
		return sendMeArticle;
	}

	public void setSendMeArticle(YesNo sendMeArticle) {
		this.sendMeArticle = sendMeArticle;
	}

	public YesNo getSendMePromotion() {
		return sendMePromotion;
	}

	public void setSendMePromotion(YesNo sendMePromotion) {
		this.sendMePromotion = sendMePromotion;
	}
	
}
