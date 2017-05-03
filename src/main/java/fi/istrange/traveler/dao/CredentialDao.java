package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.pojos.UserCredentials;
import org.jooq.DSLContext;

import java.util.Optional;

/**
 * Created by aleksandr on 18.4.2017.
 */
public class CredentialDao {
    public static Optional<UserCredentials> fetchByUsername(
            String username,
            DSLContext database
    ) {
        return database.select()
                .from(Tables.USER_CREDENTIALS)
                .where(Tables.USER_CREDENTIALS.USERNAME.equal(username))
                .fetch().map(p -> new UserCredentials(
                        p.get(Tables.USER_CREDENTIALS.USERNAME),
                        p.get(Tables.USER_CREDENTIALS.PASSWORD),
                        p.get(Tables.USER_CREDENTIALS.ACTIVE)
                )).stream().findFirst();
    }

    public static void addUser(
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

    public static void deactivateUser(
            String username,
            DSLContext database
    ) {
        database.update(Tables.USER_CREDENTIALS)
                .set(Tables.USER_CREDENTIALS.ACTIVE, false)
                .where(Tables.USER_CREDENTIALS.USERNAME.equal(username))
                .execute();
    }

    public static void updatePassword(
            String username,
            String password,
            DSLContext database
    ) {
        database.update(Tables.USER_CREDENTIALS)
                .set(Tables.USER_CREDENTIALS.PASSWORD, password)
                .where(Tables.USER_CREDENTIALS.USERNAME.equal(username))
                .execute();
    }
}
