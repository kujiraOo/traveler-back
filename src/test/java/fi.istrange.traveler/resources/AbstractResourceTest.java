package fi.istrange.traveler.resources;

import com.bendb.dropwizard.jooq.JooqBundle;
import fi.istrange.traveler.TravelerConfiguration;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.util.TestUtils;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

import static fi.istrange.traveler.db.Tables.*;
import static fi.istrange.traveler.db.Tables.CARD_USER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by rohan on 4/26/17.
 */
public class AbstractResourceTest extends TestUtils{
    protected static ApplicationBundle appBun = mock(ApplicationBundle.class);
    private static JooqBundle<TravelerConfiguration> jooqBundle = mock(JooqBundle.class);


    @BeforeClass
    public static void setUp() {
        TestUtils.setUp();
        when(appBun.getJooqBundle()).thenReturn(jooqBundle);
        when(jooqBundle.getConfiguration()).thenReturn(configuration);
    }

    @AfterClass
    public static void tearDown() {
        try {
            conn.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            db.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @After
    public void tearDownn() {
        TestUtils.deleteAllTablesContent(db);
    }

    private static final String userName = "postgres";
    private static final String password = "";
    private static final String url = "jdbc:postgresql://127.0.0.1:5432/traveler";

    private static Connection getDatabaseConnection() {
        try {
            return DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            throw new RuntimeException("Failure");
        }
    }

    private static DSLContext getDSLContext() {
        try {
            return DSL.using(getDatabaseConnection(), SQLDialect.POSTGRES_9_5);
        } catch (Exception e) {
            throw new RuntimeException("Failure");
        }
    }


    protected Long createCard(
            String userName,
            boolean active,
            Date startTime,
            Date endTime,
            BigDecimal lat,
            BigDecimal lon
    ) {
        return db.insertInto(
                Tables.CARD,
                CARD.ACTIVE, CARD.START_TIME, CARD.END_TIME, CARD.LAT, CARD.LON, CARD.OWNER_FK
        ).values(
                active,
                startTime, endTime,
                lat, lon,
                userName
        ).returning(CARD.ID).fetchOne().getValue(CARD.ID);
    }

    protected Long createUserWithGroupTravelCard(
            String userName,
            Date birthDate,
            String sex,
            boolean active,
            Date startTime,
            Date endTime,
            BigDecimal lat,
            BigDecimal lon
    ) {
        createUser(userName, birthDate, sex);
        return createGroupTravelCardForUser(userName, active, startTime, endTime, lat, lon);
    }

    protected Long createUserWithPersonalTravelCard(
            String userName,
            Date birthDate,
            String sex,
            boolean active,
            Date startTime,
            Date endTime,
            BigDecimal lat,
            BigDecimal lon
    ) {
        createUser(userName, birthDate, sex);
        return createPersonalTravelCardForUser(userName, active, startTime, endTime, lat, lon);
    }

    protected void createUser(String userName, Date birthDate, String sex) {
        db.insertInto(
                Tables.TRAVELER_USER,
                TRAVELER_USER.USERNAME, TRAVELER_USER.BIRTH, TRAVELER_USER.GENDER
        ).values(userName, birthDate, sex).execute();
    }

    protected Long createPersonalTravelCardForUser(
            String userName,
            Boolean active,
            Date startDate,
            Date endDate,
            BigDecimal lat,
            BigDecimal lon
    ) {
        Long id = createCard(userName, active, startDate, endDate, lat, lon);
        db.insertInto(
                Tables.PERSONAL_CARD,
                PERSONAL_CARD.ID
        ).values(id).execute();
        return id;
    }

    protected Long createGroupTravelCardForUser(
            String userName,
            Boolean active,
            Date startDate,
            Date endDate,
            BigDecimal lat,
            BigDecimal lon
    ) {
        Long id = createCard(userName, active, startDate, endDate, lat, lon);

        db.insertInto(
                Tables.GROUP_CARD,
                GROUP_CARD.ID
        ).values(id).execute();
        return id;
    }

    protected Long createUserWithPersonalTravelCard(String userName) {
        return createUserWithPersonalTravelCard(
                userName, Date.valueOf("2017-10-04"), "gay",
                true,
                Date.valueOf("2017-10-05"), Date.valueOf("2017-10-14"),
                BigDecimal.ONE, BigDecimal.TEN
        );
    }

    protected Long createUserWithGroupTravelCard(String userName) {
        return createUserWithGroupTravelCard(
                userName, Date.valueOf("2017-10-04"), "gay",
                true,
                Date.valueOf("2017-10-05"), Date.valueOf("2017-10-14"),
                BigDecimal.ONE, BigDecimal.TEN
        );
    }

    protected Long createPersonalTravelCardForUser(String userName) {
        return createPersonalTravelCardForUser(
                userName,
                true,
                Date.valueOf("2018-10-04"),
                Date.valueOf("2018-10-05"),
                BigDecimal.ONE, BigDecimal.TEN
        );
    }

    protected Long createGroupTravelCardForUser(String userName) {
        return createGroupTravelCardForUser(
                userName,
                true,
                Date.valueOf("2018-10-04"),
                Date.valueOf("2018-10-05"),
                BigDecimal.ONE, BigDecimal.TEN
        );
    }

    protected void addUserToGroupCard(String userName, Long groupCardId) {
        db.insertInto(Tables.CARD_USER, CARD_USER.USERNAME, CARD_USER.CARD_ID)
                .values(userName, groupCardId).execute();
    }

}
