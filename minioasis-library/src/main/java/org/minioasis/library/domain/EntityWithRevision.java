package org.minioasis.library.domain;

import org.minioasis.library.audit.AuditRevisionEntity;

public class EntityWithRevision <T> {

    private AuditRevisionEntity revision;

    private T entity;

    public EntityWithRevision(AuditRevisionEntity revision, T entity) {
        this.revision = revision;
        this.entity = entity;
    }

    public AuditRevisionEntity getRevision() {
        return revision;
    }

    public T getEntity() {
        return entity;
    }
}
