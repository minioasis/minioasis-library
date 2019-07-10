package org.minioasis.library.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "telegram_user")
public class TelegramUser implements Serializable {

	private static final long serialVersionUID = -579873276633955420L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull(message = "{notnull}")
	@Column(name = "chat_id", unique = true, nullable = false)
	private Long chatId;
	
	@NotNull(message = "{notnull}")	
	@Column(name = "card_key", unique = true, nullable = false)
	private String cardKey;
	
	@Valid
	private Preference preference;
	
	public TelegramUser() {
	}

	public TelegramUser(@NotNull(message = "{notnull}") Long chatId,
			@NotNull(message = "{notnull}") String cardKey, @Valid Preference preference) {
		super();
		this.chatId = chatId;
		this.cardKey = cardKey;
		this.preference = preference;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Preference getPreference() {
		return preference;
	}

	public void setPreference(Preference preference) {
		this.preference = preference;
	}

	public boolean equals(Object other) {
		
		if (this == other) return true;
		if (id == null)	return false;
		if (!(other instanceof TelegramUser))	return false;
		final TelegramUser that = (TelegramUser) other;

		return this.id.equals(that.getId());
		
	}

	public int hashCode() {

		return id == null ? System.identityHashCode(this) : id.hashCode();
	}	
	
}
