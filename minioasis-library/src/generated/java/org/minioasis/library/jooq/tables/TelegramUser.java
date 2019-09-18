/*
 * This file is generated by jOOQ.
 */
package org.minioasis.library.jooq.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row11;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.minioasis.library.jooq.DefaultSchema;
import org.minioasis.library.jooq.Indexes;
import org.minioasis.library.jooq.Keys;
import org.minioasis.library.jooq.tables.records.TelegramUserRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TelegramUser extends TableImpl<TelegramUserRecord> {

    private static final long serialVersionUID = -1024127292;

    /**
     * The reference instance of <code>TELEGRAM_USER</code>
     */
    public static final TelegramUser TELEGRAM_USER = new TelegramUser();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TelegramUserRecord> getRecordType() {
        return TelegramUserRecord.class;
    }

    /**
     * The column <code>TELEGRAM_USER.ID</code>.
     */
    public final TableField<TelegramUserRecord, Long> ID = createField(DSL.name("ID"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>TELEGRAM_USER.CARD_KEY</code>.
     */
    public final TableField<TelegramUserRecord, String> CARD_KEY = createField(DSL.name("CARD_KEY"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>TELEGRAM_USER.CHAT_ID</code>.
     */
    public final TableField<TelegramUserRecord, Long> CHAT_ID = createField(DSL.name("CHAT_ID"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>TELEGRAM_USER.DONT_REMIND_AGAIN</code>.
     */
    public final TableField<TelegramUserRecord, Integer> DONT_REMIND_AGAIN = createField(DSL.name("DONT_REMIND_AGAIN"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>TELEGRAM_USER.REMIND_AGAIN_IN_THE_LAST_DAY</code>.
     */
    public final TableField<TelegramUserRecord, Integer> REMIND_AGAIN_IN_THE_LAST_DAY = createField(DSL.name("REMIND_AGAIN_IN_THE_LAST_DAY"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>TELEGRAM_USER.REMINDER</code>.
     */
    public final TableField<TelegramUserRecord, Integer> REMINDER = createField(DSL.name("REMINDER"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>TELEGRAM_USER.SENDME_ANNOUCEMENT</code>.
     */
    public final TableField<TelegramUserRecord, Integer> SENDME_ANNOUCEMENT = createField(DSL.name("SENDME_ANNOUCEMENT"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>TELEGRAM_USER.SENDME_ARTICLE</code>.
     */
    public final TableField<TelegramUserRecord, Integer> SENDME_ARTICLE = createField(DSL.name("SENDME_ARTICLE"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>TELEGRAM_USER.SENDME_EVENTS</code>.
     */
    public final TableField<TelegramUserRecord, Integer> SENDME_EVENTS = createField(DSL.name("SENDME_EVENTS"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>TELEGRAM_USER.SENDME_NEW_RELEASE</code>.
     */
    public final TableField<TelegramUserRecord, Integer> SENDME_NEW_RELEASE = createField(DSL.name("SENDME_NEW_RELEASE"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>TELEGRAM_USER.SENDME_PROMOTION</code>.
     */
    public final TableField<TelegramUserRecord, Integer> SENDME_PROMOTION = createField(DSL.name("SENDME_PROMOTION"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>TELEGRAM_USER</code> table reference
     */
    public TelegramUser() {
        this(DSL.name("TELEGRAM_USER"), null);
    }

    /**
     * Create an aliased <code>TELEGRAM_USER</code> table reference
     */
    public TelegramUser(String alias) {
        this(DSL.name(alias), TELEGRAM_USER);
    }

    /**
     * Create an aliased <code>TELEGRAM_USER</code> table reference
     */
    public TelegramUser(Name alias) {
        this(alias, TELEGRAM_USER);
    }

    private TelegramUser(Name alias, Table<TelegramUserRecord> aliased) {
        this(alias, aliased, null);
    }

    private TelegramUser(Name alias, Table<TelegramUserRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TelegramUser(Table<O> child, ForeignKey<O, TelegramUserRecord> key) {
        super(child, key, TELEGRAM_USER);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PRIMARY_KEY_B, Indexes.UK_8FJRX8Y7KIFVM13XIF72SY5BF_INDEX_B, Indexes.UK_ABIUVF67GFNDN35NGQDPKQ7DY_INDEX_B);
    }

    @Override
    public Identity<TelegramUserRecord, Long> getIdentity() {
        return Keys.IDENTITY_TELEGRAM_USER;
    }

    @Override
    public UniqueKey<TelegramUserRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_B;
    }

    @Override
    public List<UniqueKey<TelegramUserRecord>> getKeys() {
        return Arrays.<UniqueKey<TelegramUserRecord>>asList(Keys.CONSTRAINT_B, Keys.UK_ABIUVF67GFNDN35NGQDPKQ7DY, Keys.UK_8FJRX8Y7KIFVM13XIF72SY5BF);
    }

    @Override
    public TelegramUser as(String alias) {
        return new TelegramUser(DSL.name(alias), this);
    }

    @Override
    public TelegramUser as(Name alias) {
        return new TelegramUser(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TelegramUser rename(String name) {
        return new TelegramUser(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TelegramUser rename(Name name) {
        return new TelegramUser(name, null);
    }

    // -------------------------------------------------------------------------
    // Row11 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row11<Long, String, Long, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row11) super.fieldsRow();
    }
}
