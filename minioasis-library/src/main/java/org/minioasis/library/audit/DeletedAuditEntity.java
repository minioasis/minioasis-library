package org.minioasis.library.audit;

import java.time.LocalDateTime;

public class DeletedAuditEntity {

	private long rev;
	
	private LocalDateTime timestamp;
	
    private String username;
    
    private long entityId;
    
    public DeletedAuditEntity() {
    }

	public long getRev() {
		return rev;
	}

	public void setRev(long rev) {
		this.rev = rev;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
    
}
