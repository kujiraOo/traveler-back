package fi.istrange.traveler.util;

import fi.istrange.traveler.db.Tables;
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

/**
 * Created by rohan on 4/25/17.
 */
public class TestUtils {
    public static void deleteAllTablesContent(DSLContext db) {
        db.delete(Tables.PERSONAL_CARD).execute();
        db.delete(Tables.CARD_USER).execute();
        db.delete(Tables.GROUP_CARD).execute();
        db.delete(Tables.MATCH).execute();
        db.delete(CARD).execute();
        db.delete(Tables.USER_CREDENTIALS).execute();
        db.delete(Tables.TRAVELER_USER).execute();
    }

    protected static Connection conn;
    protected static DSLContext db;
    protected static Configuration configuration;

    @BeforeClass
    public static void setUp() {
        conn = getDatabaseConnection();
        db = getDSLContext();
        configuration = new DefaultConfiguration();
        configuration.set(conn);
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
        deleteAllTablesContent(db);
    }

    private static final String userName = "postgres";
    private static final String password = "";
    private static final String url = "jdbc:postgresql://127.0.0.1:5032/traveler";

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
                CARD,
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
