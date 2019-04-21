/*
 * This file is generated by jOOQ.
*/
package org.minioasis.library.jooq.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.minioasis.library.jooq.tables.Photo;
import org.minioasis.library.jooq.tables.records.PhotoRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.8"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PhotoDao extends DAOImpl<PhotoRecord, org.minioasis.library.jooq.tables.pojos.Photo, Long> {

    /**
     * Create a new PhotoDao without any configuration
     */
    public PhotoDao() {
        super(Photo.PHOTO, org.minioasis.library.jooq.tables.pojos.Photo.class);
    }

    /**
     * Create a new PhotoDao with an attached configuration
     */
    public PhotoDao(Configuration configuration) {
        super(Photo.PHOTO, org.minioasis.library.jooq.tables.pojos.Photo.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(org.minioasis.library.jooq.tables.pojos.Photo object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Photo> fetchById(Long... values) {
        return fetch(Photo.PHOTO.ID, values);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public org.minioasis.library.jooq.tables.pojos.Photo fetchOneById(Long value) {
        return fetchOne(Photo.PHOTO.ID, value);
    }

    /**
     * Fetch records that have <code>DESCRIPTION IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Photo> fetchByDescription(String... values) {
        return fetch(Photo.PHOTO.DESCRIPTION, values);
    }

    /**
     * Fetch records that have <code>IMG IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Photo> fetchByImg(byte[]... values) {
        return fetch(Photo.PHOTO.IMG, values);
    }

    /**
     * Fetch records that have <code>LINK IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Photo> fetchByLink(String... values) {
        return fetch(Photo.PHOTO.LINK, values);
    }

    /**
     * Fetch records that have <code>NAME IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Photo> fetchByName(String... values) {
        return fetch(Photo.PHOTO.NAME, values);
    }

    /**
     * Fetch records that have <code>SIZE IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Photo> fetchBySize(Integer... values) {
        return fetch(Photo.PHOTO.SIZE, values);
    }
}