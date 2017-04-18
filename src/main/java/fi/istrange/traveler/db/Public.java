/*
 * This file is generated by jOOQ.
*/
package fi.istrange.traveler.db;


import fi.istrange.traveler.db.tables.CardUser;
import fi.istrange.traveler.db.tables.GroupCard;
import fi.istrange.traveler.db.tables.PersonalCard;
import fi.istrange.traveler.db.tables.TravelerUser;
import fi.istrange.traveler.db.tables.UserCredentials;
import fi.istrange.traveler.db.tables.UserPhoto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


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
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1519723164;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.card_user</code>.
     */
    public final CardUser CARD_USER = fi.istrange.traveler.db.tables.CardUser.CARD_USER;

    /**
     * The table <code>public.group_card</code>.
     */
    public final GroupCard GROUP_CARD = fi.istrange.traveler.db.tables.GroupCard.GROUP_CARD;

    /**
     * The table <code>public.personal_card</code>.
     */
    public final PersonalCard PERSONAL_CARD = fi.istrange.traveler.db.tables.PersonalCard.PERSONAL_CARD;

    /**
     * The table <code>public.traveler_user</code>.
     */
    public final TravelerUser TRAVELER_USER = fi.istrange.traveler.db.tables.TravelerUser.TRAVELER_USER;

    /**
     * The table <code>public.user_credentials</code>.
     */
    public final UserCredentials USER_CREDENTIALS = fi.istrange.traveler.db.tables.UserCredentials.USER_CREDENTIALS;

    /**
     * The table <code>public.user_photo</code>.
     */
    public final UserPhoto USER_PHOTO = fi.istrange.traveler.db.tables.UserPhoto.USER_PHOTO;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            CardUser.CARD_USER,
            GroupCard.GROUP_CARD,
            PersonalCard.PERSONAL_CARD,
            TravelerUser.TRAVELER_USER,
            UserCredentials.USER_CREDENTIALS,
            UserPhoto.USER_PHOTO);
    }
}
