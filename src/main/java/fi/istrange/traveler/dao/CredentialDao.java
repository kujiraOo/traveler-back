package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.pojos.UserCredentials;
import org.jooq.DSLContext;

import java.util.Optional;

/**
 * Created by aleksandr on 18.4.2017.
 */
public class CredentialDao {
    public Optional<UserCredentials> fetchByUsername(
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
}
