/*
 * This file is generated by jOOQ.
*/
package org.minioasis.library.jooq.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;
import org.minioasis.library.jooq.tables.ItemStatus;


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
public class ItemStatusRecord extends UpdatableRecordImpl<ItemStatusRecord> implements Record4<Long, Boolean, String, Boolean> {

    private static final long serialVersionUID = -1435895855;

    /**
     * Setter for <code>PUBLIC.ITEM_STATUS.ID</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEM_STATUS.ID</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>PUBLIC.ITEM_STATUS.BORROWABLE</code>.
     */
    public void setBorrowable(Boolean value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEM_STATUS.BORROWABLE</code>.
     */
    public Boolean getBorrowable() {
        return (Boolean) get(1);
    }

    /**
     * Setter for <code>PUBLIC.ITEM_STATUS.NAME</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEM_STATUS.NAME</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>PUBLIC.ITEM_STATUS.RESERVABLE</code>.
     */
    public void setReservable(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEM_STATUS.RESERVABLE</code>.
     */
    public Boolean getReservable() {
        return (Boolean) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Long, Boolean, String, Boolean> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Long, Boolean, String, Boolean> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return ItemStatus.ITEM_STATUS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field2() {
        return ItemStatus.ITEM_STATUS.BORROWABLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return ItemStatus.ITEM_STATUS.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field4() {
        return ItemStatus.ITEM_STATUS.RESERVABLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean component2() {
        return getBorrowable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean component4() {
        return getReservable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value2() {
        return getBorrowable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value4() {
        return getReservable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStatusRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStatusRecord value2(Boolean value) {
        setBorrowable(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStatusRecord value3(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStatusRecord value4(Boolean value) {
        setReservable(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStatusRecord values(Long value1, Boolean value2, String value3, Boolean value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ItemStatusRecord
     */
    public ItemStatusRecord() {
        super(ItemStatus.ITEM_STATUS);
    }

    /**
     * Create a detached, initialised ItemStatusRecord
     */
    public ItemStatusRecord(Long id, Boolean borrowable, String name, Boolean reservable) {
        super(ItemStatus.ITEM_STATUS);

        set(0, id);
        set(1, borrowable);
        set(2, name);
        set(3, reservable);
    }
}