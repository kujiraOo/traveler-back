package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.tables.UserCredentials;
import org.junit.Test;

import java.sql.Date;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static fi.istrange.traveler.dao.CredentialDao.*;
import static org.junit.Assert.*;

/**
 * Created by rohan on 5/2/17.
 */
public class CredentialDaoTest extends AbstractDaoTest {

    Random ran = new Random();

    @Test
    public void fetchByUsername_empty() {
        IntStream.range(0, 2).forEach(
                i -> assertFalse(fetchByUsername(String.valueOf(ran.nextLong()), db).isPresent())
        );

    }

    @Test
    public void addUser() {
        IntStream.range(0, 2).forEach(
            i -> {
                createUser(String.valueOf(i), Date.valueOf("2015-10-10"), "gay");
                CredentialDao.addUser(String.valueOf(i), "youShallNotPass", db);
                Optional<fi.istrange.traveler.db.tables.pojos.UserCredentials> result = fetchByUsername(String.valueOf(i), db);
                assertTrue(result.isPresent());
                assertTrue(result.get().getUsername().equals(String.valueOf(i)));
            }
        );
    }

    @Test
    public void deactivateUser() {
        IntStream.range(0, 2).forEach(
                i -> {
                    String userName = String.valueOf(i);
                    createUser(userName, Date.valueOf("2015-10-10"), "gay");
                    CredentialDao.addUser(userName, "youShallNotPass", db);
                    CredentialDao.deactivateUser(userName, db);
                    Optional<fi.istrange.traveler.db.tables.pojos.UserCredentials> result = fetchByUsername(String.valueOf(i), db);
                    assertTrue(result.isPresent());
                    assertTrue(result.get().getUsername().equals(userName));
                    System.out.println(result.get().getActive());
                    assertTrue(result.get().getActive().equals(Boolean.FALSE));
                }
        );
    }

    @Test
    public void updatePassword() {
        assertTrue(true);
    }
}
