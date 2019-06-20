/*
 * This file is generated by jOOQ.
*/
package org.minioasis.library.jooq.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.minioasis.library.jooq.tables.Account;
import org.minioasis.library.jooq.tables.records.AccountRecord;


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
public class AccountDao extends DAOImpl<AccountRecord, org.minioasis.library.jooq.tables.pojos.Account, Long> {

    /**
     * Create a new AccountDao without any configuration
     */
    public AccountDao() {
        super(Account.ACCOUNT, org.minioasis.library.jooq.tables.pojos.Account.class);
    }

    /**
     * Create a new AccountDao with an attached configuration
     */
    public AccountDao(Configuration configuration) {
        super(Account.ACCOUNT, org.minioasis.library.jooq.tables.pojos.Account.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(org.minioasis.library.jooq.tables.pojos.Account object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Account> fetchById(Long... values) {
        return fetch(Account.ACCOUNT.ID, values);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public org.minioasis.library.jooq.tables.pojos.Account fetchOneById(Long value) {
        return fetchOne(Account.ACCOUNT.ID, value);
    }

    /**
     * Fetch records that have <code>CODE IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Account> fetchByCode(String... values) {
        return fetch(Account.ACCOUNT.CODE, values);
    }

    /**
     * Fetch a unique record that has <code>CODE = value</code>
     */
    public org.minioasis.library.jooq.tables.pojos.Account fetchOneByCode(String value) {
        return fetchOne(Account.ACCOUNT.CODE, value);
    }

    /**
     * Fetch records that have <code>NAME IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Account> fetchByName(String... values) {
        return fetch(Account.ACCOUNT.NAME, values);
    }

    /**
     * Fetch records that have <code>TYPE IN (values)</code>
     */
    public List<org.minioasis.library.jooq.tables.pojos.Account> fetchByType(Integer... values) {
        return fetch(Account.ACCOUNT.TYPE, values);
    }
}
