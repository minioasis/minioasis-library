/*
 * This file is generated by jOOQ.
 */
package org.minioasis.library.jooq.tables;


import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row17;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.minioasis.library.jooq.DefaultSchema;
import org.minioasis.library.jooq.Indexes;
import org.minioasis.library.jooq.Keys;
import org.minioasis.library.jooq.tables.records.PatronTypeRecord;


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
public class PatronType extends TableImpl<PatronTypeRecord> {

    private static final long serialVersionUID = -383245556;

    /**
     * The reference instance of <code>PATRON_TYPE</code>
     */
    public static final PatronType PATRON_TYPE = new PatronType();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PatronTypeRecord> getRecordType() {
        return PatronTypeRecord.class;
    }

    /**
     * The column <code>PATRON_TYPE.ID</code>.
     */
    public final TableField<PatronTypeRecord, Long> ID = createField(DSL.name("ID"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>PATRON_TYPE.BIBLIO_LIMIT</code>.
     */
    public final TableField<PatronTypeRecord, Integer> BIBLIO_LIMIT = createField(DSL.name("BIBLIO_LIMIT"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.CODE</code>.
     */
    public final TableField<PatronTypeRecord, String> CODE = createField(DSL.name("CODE"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.DEPOSIT</code>.
     */
    public final TableField<PatronTypeRecord, BigDecimal> DEPOSIT = createField(DSL.name("DEPOSIT"), org.jooq.impl.SQLDataType.DECIMAL(12, 2).nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.DURATION</code>.
     */
    public final TableField<PatronTypeRecord, Integer> DURATION = createField(DSL.name("DURATION"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.EXPIRY_DATE</code>.
     */
    public final TableField<PatronTypeRecord, Date> EXPIRY_DATE = createField(DSL.name("EXPIRY_DATE"), org.jooq.impl.SQLDataType.DATE.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.FINE_RATE</code>.
     */
    public final TableField<PatronTypeRecord, BigDecimal> FINE_RATE = createField(DSL.name("FINE_RATE"), org.jooq.impl.SQLDataType.DECIMAL(12, 2).nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.MAX_CANT_RESERVE_PERIOD</code>.
     */
    public final TableField<PatronTypeRecord, Integer> MAX_CANT_RESERVE_PERIOD = createField(DSL.name("MAX_CANT_RESERVE_PERIOD"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.MAX_COLLECTABLE_PERIOD</code>.
     */
    public final TableField<PatronTypeRecord, Integer> MAX_COLLECTABLE_PERIOD = createField(DSL.name("MAX_COLLECTABLE_PERIOD"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.MAX_NO_OF_RENEW</code>.
     */
    public final TableField<PatronTypeRecord, Integer> MAX_NO_OF_RENEW = createField(DSL.name("MAX_NO_OF_RENEW"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.MAX_NO_OF_RESERVATIONS</code>.
     */
    public final TableField<PatronTypeRecord, Integer> MAX_NO_OF_RESERVATIONS = createField(DSL.name("MAX_NO_OF_RESERVATIONS"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.MAX_UNCOLLECTED_NO</code>.
     */
    public final TableField<PatronTypeRecord, Integer> MAX_UNCOLLECTED_NO = createField(DSL.name("MAX_UNCOLLECTED_NO"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.MEMBER_FEE</code>.
     */
    public final TableField<PatronTypeRecord, BigDecimal> MEMBER_FEE = createField(DSL.name("MEMBER_FEE"), org.jooq.impl.SQLDataType.DECIMAL(12, 2).nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.MIN_RENEWABLE_PERIOD</code>.
     */
    public final TableField<PatronTypeRecord, Integer> MIN_RENEWABLE_PERIOD = createField(DSL.name("MIN_RENEWABLE_PERIOD"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.NAME</code>.
     */
    public final TableField<PatronTypeRecord, String> NAME = createField(DSL.name("NAME"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.RESUME_BORROWABLE_PERIOD</code>.
     */
    public final TableField<PatronTypeRecord, Integer> RESUME_BORROWABLE_PERIOD = createField(DSL.name("RESUME_BORROWABLE_PERIOD"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PATRON_TYPE.START_DATE</code>.
     */
    public final TableField<PatronTypeRecord, Date> START_DATE = createField(DSL.name("START_DATE"), org.jooq.impl.SQLDataType.DATE.nullable(false), this, "");

    /**
     * Create a <code>PATRON_TYPE</code> table reference
     */
    public PatronType() {
        this(DSL.name("PATRON_TYPE"), null);
    }

    /**
     * Create an aliased <code>PATRON_TYPE</code> table reference
     */
    public PatronType(String alias) {
        this(DSL.name(alias), PATRON_TYPE);
    }

    /**
     * Create an aliased <code>PATRON_TYPE</code> table reference
     */
    public PatronType(Name alias) {
        this(alias, PATRON_TYPE);
    }

    private PatronType(Name alias, Table<PatronTypeRecord> aliased) {
        this(alias, aliased, null);
    }

    private PatronType(Name alias, Table<PatronTypeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> PatronType(Table<O> child, ForeignKey<O, PatronTypeRecord> key) {
        super(child, key, PATRON_TYPE);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PRIMARY_KEY_5, Indexes.UK_T54V4235XB53JP0OYG9QXYBBM_INDEX_5);
    }

    @Override
    public Identity<PatronTypeRecord, Long> getIdentity() {
        return Keys.IDENTITY_PATRON_TYPE;
    }

    @Override
    public UniqueKey<PatronTypeRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_5;
    }

    @Override
    public List<UniqueKey<PatronTypeRecord>> getKeys() {
        return Arrays.<UniqueKey<PatronTypeRecord>>asList(Keys.CONSTRAINT_5, Keys.UK_T54V4235XB53JP0OYG9QXYBBM);
    }

    @Override
    public PatronType as(String alias) {
        return new PatronType(DSL.name(alias), this);
    }

    @Override
    public PatronType as(Name alias) {
        return new PatronType(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PatronType rename(String name) {
        return new PatronType(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PatronType rename(Name name) {
        return new PatronType(name, null);
    }

    // -------------------------------------------------------------------------
    // Row17 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row17<Long, Integer, String, BigDecimal, Integer, Date, BigDecimal, Integer, Integer, Integer, Integer, Integer, BigDecimal, Integer, String, Integer, Date> fieldsRow() {
        return (Row17) super.fieldsRow();
    }
}
