package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import org.jooq.DSLContext;

/**
 * Created by aleksandr on 16.4.2017.
 */
public class UserCredentialDao {

    public void addUser(
            String username,
            String password,
            DSLContext database
    ) {
        database.insertInto(
                Tables.USER_CREDENTIALS,
                Tables.USER_CREDENTIALS.USERNAME,
                Tables.USER_CREDENTIALS.PASSWORD,
                Tables.USER_CREDENTIALS.ACTIVE
                )
                .values(username, password, true)
                .execute();
    }

    public void deactivateUser(
            String username,
            DSLContext database
    ) {
        database.update(Tables.USER_CREDENTIALS)
                .set(Tables.USER_CREDENTIALS.ACTIVE, false)
                .where(Tables.USER_CREDENTIALS.USERNAME.equal(username))
                .execute();
    }
}
