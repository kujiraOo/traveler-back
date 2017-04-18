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

import javax.annotation.Generated;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.card_user</code>.
     */
    public static final CardUser CARD_USER = fi.istrange.traveler.db.tables.CardUser.CARD_USER;

    /**
     * The table <code>public.group_card</code>.
     */
    public static final GroupCard GROUP_CARD = fi.istrange.traveler.db.tables.GroupCard.GROUP_CARD;

    /**
     * The table <code>public.personal_card</code>.
     */
    public static final PersonalCard PERSONAL_CARD = fi.istrange.traveler.db.tables.PersonalCard.PERSONAL_CARD;

    /**
     * The table <code>public.traveler_user</code>.
     */
    public static final TravelerUser TRAVELER_USER = fi.istrange.traveler.db.tables.TravelerUser.TRAVELER_USER;

    /**
     * The table <code>public.user_credentials</code>.
     */
    public static final UserCredentials USER_CREDENTIALS = fi.istrange.traveler.db.tables.UserCredentials.USER_CREDENTIALS;

    /**
     * The table <code>public.user_photo</code>.
     */
    public static final UserPhoto USER_PHOTO = fi.istrange.traveler.db.tables.UserPhoto.USER_PHOTO;
}
