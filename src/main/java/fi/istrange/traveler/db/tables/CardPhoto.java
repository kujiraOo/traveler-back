/*
 * This file is generated by jOOQ.
*/
package fi.istrange.traveler.db.tables;


import fi.istrange.traveler.db.Keys;
import fi.istrange.traveler.db.Public;
import fi.istrange.traveler.db.tables.records.CardPhotoRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CardPhoto extends TableImpl<CardPhotoRecord> {

    private static final long serialVersionUID = -257916689;

    /**
     * The reference instance of <code>public.card_photo</code>
     */
    public static final CardPhoto CARD_PHOTO = new CardPhoto();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CardPhotoRecord> getRecordType() {
        return CardPhotoRecord.class;
    }

    /**
     * The column <code>public.card_photo.card_id</code>.
     */
    public final TableField<CardPhotoRecord, Long> CARD_ID = createField("card_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.card_photo.photo</code>.
     */
    public final TableField<CardPhotoRecord, Long> PHOTO = createField("photo", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * Create a <code>public.card_photo</code> table reference
     */
    public CardPhoto() {
        this("card_photo", null);
    }

    /**
     * Create an aliased <code>public.card_photo</code> table reference
     */
    public CardPhoto(String alias) {
        this(alias, CARD_PHOTO);
    }

    private CardPhoto(String alias, Table<CardPhotoRecord> aliased) {
        this(alias, aliased, null);
    }

    private CardPhoto(String alias, Table<CardPhotoRecord> aliased, Field<?>[] parameters) {
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
    public List<ForeignKey<CardPhotoRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<CardPhotoRecord, ?>>asList(Keys.CARD_PHOTO__CARD_PHOTO_FKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CardPhoto as(String alias) {
        return new CardPhoto(alias, this);
    }

    /**
     * Rename this table
     */
    public CardPhoto rename(String name) {
        return new CardPhoto(name, null);
    }
}