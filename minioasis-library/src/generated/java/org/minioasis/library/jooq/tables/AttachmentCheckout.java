/*
 * This file is generated by jOOQ.
*/
package org.minioasis.library.jooq.tables;


import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.minioasis.library.jooq.Indexes;
import org.minioasis.library.jooq.Keys;
import org.minioasis.library.jooq.Public;
import org.minioasis.library.jooq.tables.records.AttachmentCheckoutRecord;


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
public class AttachmentCheckout extends TableImpl<AttachmentCheckoutRecord> {

    private static final long serialVersionUID = 551710559;

    /**
     * The reference instance of <code>PUBLIC.ATTACHMENT_CHECKOUT</code>
     */
    public static final AttachmentCheckout ATTACHMENT_CHECKOUT = new AttachmentCheckout();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AttachmentCheckoutRecord> getRecordType() {
        return AttachmentCheckoutRecord.class;
    }

    /**
     * The column <code>PUBLIC.ATTACHMENT_CHECKOUT.ID</code>.
     */
    public final TableField<AttachmentCheckoutRecord, Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.ATTACHMENT_CHECKOUT.CHECKOUT_DATE</code>.
     */
    public final TableField<AttachmentCheckoutRecord, Date> CHECKOUT_DATE = createField("CHECKOUT_DATE", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ATTACHMENT_CHECKOUT.DONE</code>.
     */
    public final TableField<AttachmentCheckoutRecord, Date> DONE = createField("DONE", org.jooq.impl.SQLDataType.DATE, this, "");

    /**
     * The column <code>PUBLIC.ATTACHMENT_CHECKOUT.STATE</code>.
     */
    public final TableField<AttachmentCheckoutRecord, String> STATE = createField("STATE", org.jooq.impl.SQLDataType.CHAR(20).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ATTACHMENT_CHECKOUT.ATTACHMENT_ID</code>.
     */
    public final TableField<AttachmentCheckoutRecord, Long> ATTACHMENT_ID = createField("ATTACHMENT_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ATTACHMENT_CHECKOUT.CHECKOUT_ID</code>.
     */
    public final TableField<AttachmentCheckoutRecord, Long> CHECKOUT_ID = createField("CHECKOUT_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ATTACHMENT_CHECKOUT.PATRON_ID</code>.
     */
    public final TableField<AttachmentCheckoutRecord, Long> PATRON_ID = createField("PATRON_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>PUBLIC.ATTACHMENT_CHECKOUT</code> table reference
     */
    public AttachmentCheckout() {
        this(DSL.name("ATTACHMENT_CHECKOUT"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.ATTACHMENT_CHECKOUT</code> table reference
     */
    public AttachmentCheckout(String alias) {
        this(DSL.name(alias), ATTACHMENT_CHECKOUT);
    }

    /**
     * Create an aliased <code>PUBLIC.ATTACHMENT_CHECKOUT</code> table reference
     */
    public AttachmentCheckout(Name alias) {
        this(alias, ATTACHMENT_CHECKOUT);
    }

    private AttachmentCheckout(Name alias, Table<AttachmentCheckoutRecord> aliased) {
        this(alias, aliased, null);
    }

    private AttachmentCheckout(Name alias, Table<AttachmentCheckoutRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.FK_ATTACHMENTCHECKOUT_ATTACHMENT_INDEX_6, Indexes.FK_ATTACHMENTCHECKOUT_CHECKOUT_INDEX_6, Indexes.FK_ATTACHMENTCHECKOUT_PATRON_INDEX_6, Indexes.PRIMARY_KEY_6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<AttachmentCheckoutRecord, Long> getIdentity() {
        return Keys.IDENTITY_ATTACHMENT_CHECKOUT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<AttachmentCheckoutRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_6;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<AttachmentCheckoutRecord>> getKeys() {
        return Arrays.<UniqueKey<AttachmentCheckoutRecord>>asList(Keys.CONSTRAINT_6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<AttachmentCheckoutRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<AttachmentCheckoutRecord, ?>>asList(Keys.FK_ATTACHMENTCHECKOUT_ATTACHMENT, Keys.FK_ATTACHMENTCHECKOUT_CHECKOUT, Keys.FK_ATTACHMENTCHECKOUT_PATRON);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AttachmentCheckout as(String alias) {
        return new AttachmentCheckout(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AttachmentCheckout as(Name alias) {
        return new AttachmentCheckout(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public AttachmentCheckout rename(String name) {
        return new AttachmentCheckout(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AttachmentCheckout rename(Name name) {
        return new AttachmentCheckout(name, null);
    }
}
