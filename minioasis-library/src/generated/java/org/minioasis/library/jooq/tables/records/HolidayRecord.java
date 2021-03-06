/*
 * This file is generated by jOOQ.
 */
package org.minioasis.library.jooq.tables.records;


import java.sql.Date;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;
import org.minioasis.library.jooq.tables.Holiday;


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
public class HolidayRecord extends UpdatableRecordImpl<HolidayRecord> implements Record5<Long, Date, Boolean, String, Date> {

    private static final long serialVersionUID = -1630803363;

    /**
     * Setter for <code>HOLIDAY.ID</code>.
     */
    public HolidayRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>HOLIDAY.ID</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>HOLIDAY.END_DATE</code>.
     */
    public HolidayRecord setEndDate(Date value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>HOLIDAY.END_DATE</code>.
     */
    public Date getEndDate() {
        return (Date) get(1);
    }

    /**
     * Setter for <code>HOLIDAY.FINE</code>.
     */
    public HolidayRecord setFine(Boolean value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>HOLIDAY.FINE</code>.
     */
    public Boolean getFine() {
        return (Boolean) get(2);
    }

    /**
     * Setter for <code>HOLIDAY.NAME</code>.
     */
    public HolidayRecord setName(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>HOLIDAY.NAME</code>.
     */
    public String getName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>HOLIDAY.START_DATE</code>.
     */
    public HolidayRecord setStartDate(Date value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>HOLIDAY.START_DATE</code>.
     */
    public Date getStartDate() {
        return (Date) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, Date, Boolean, String, Date> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, Date, Boolean, String, Date> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Holiday.HOLIDAY.ID;
    }

    @Override
    public Field<Date> field2() {
        return Holiday.HOLIDAY.END_DATE;
    }

    @Override
    public Field<Boolean> field3() {
        return Holiday.HOLIDAY.FINE;
    }

    @Override
    public Field<String> field4() {
        return Holiday.HOLIDAY.NAME;
    }

    @Override
    public Field<Date> field5() {
        return Holiday.HOLIDAY.START_DATE;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Date component2() {
        return getEndDate();
    }

    @Override
    public Boolean component3() {
        return getFine();
    }

    @Override
    public String component4() {
        return getName();
    }

    @Override
    public Date component5() {
        return getStartDate();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Date value2() {
        return getEndDate();
    }

    @Override
    public Boolean value3() {
        return getFine();
    }

    @Override
    public String value4() {
        return getName();
    }

    @Override
    public Date value5() {
        return getStartDate();
    }

    @Override
    public HolidayRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public HolidayRecord value2(Date value) {
        setEndDate(value);
        return this;
    }

    @Override
    public HolidayRecord value3(Boolean value) {
        setFine(value);
        return this;
    }

    @Override
    public HolidayRecord value4(String value) {
        setName(value);
        return this;
    }

    @Override
    public HolidayRecord value5(Date value) {
        setStartDate(value);
        return this;
    }

    @Override
    public HolidayRecord values(Long value1, Date value2, Boolean value3, String value4, Date value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached HolidayRecord
     */
    public HolidayRecord() {
        super(Holiday.HOLIDAY);
    }

    /**
     * Create a detached, initialised HolidayRecord
     */
    public HolidayRecord(Long id, Date endDate, Boolean fine, String name, Date startDate) {
        super(Holiday.HOLIDAY);

        set(0, id);
        set(1, endDate);
        set(2, fine);
        set(3, name);
        set(4, startDate);
    }
}
