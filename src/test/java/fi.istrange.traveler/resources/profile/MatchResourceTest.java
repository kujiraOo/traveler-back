package fi.istrange.traveler.resources.profile;

import com.bendb.dropwizard.jooq.JooqBundle;
import fi.istrange.traveler.TravelerConfiguration;
import fi.istrange.traveler.api.MatchForCardRes;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.Tables;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.junit.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static fi.istrange.traveler.db.Tables.*;

/**
 * Created by rohan on 4/25/17.
 */
public class MatchResourceTest {

    static ApplicationBundle appBun = mock(ApplicationBundle.class);
    static JooqBundle<TravelerConfiguration> jooqBundle = mock(JooqBundle.class);

    private static Connection conn;
    private static DSLContext db;
    private static Configuration configuration;

    @BeforeClass
    public static void setUp() {
        conn = getDatabaseConnection();
        db = getDSLContext();
        configuration = new DefaultConfiguration();
        configuration.set(conn);
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

    private static Long id_a;

    @Before
    public void setUpp() {
        // TODO populate database
        tearDown();
        db.insertInto(
                Tables.TRAVELER_USER,
                TRAVELER_USER.USERNAME, TRAVELER_USER.BIRTH, TRAVELER_USER.GENDER
        ).values("a", new Date(1), "gay").execute();

        db.insertInto(
                Tables.TRAVELER_USER,
                TRAVELER_USER.USERNAME, TRAVELER_USER.BIRTH, TRAVELER_USER.GENDER
        ).values("b", new Date(1), "less").execute();

        id_a = db.insertInto(
                Tables.CARD,
                CARD.ACTIVE, CARD.START_TIME, CARD.END_TIME, CARD.LAT, CARD.LON, CARD.OWNER_FK
        ).values(
                true,
                new Date(1), new Date(2),
                new BigDecimal(1.1234567), new BigDecimal(1.1234567),
                "a"
        ).returning(CARD.ID).fetchOne().getValue(CARD.ID);

        Long id_b = db.insertInto(
                Tables.CARD,
                CARD.ACTIVE, CARD.START_TIME, CARD.END_TIME, CARD.LAT, CARD.LON, CARD.OWNER_FK
        ).values(
                true,
                new Date(1), new Date(2),
                new BigDecimal(1.1234567), new BigDecimal(1.1234567),
                "b"
        ).returning(CARD.ID).fetchOne().getValue(CARD.ID);
        db.insertInto(
                Tables.PERSONAL_CARD,
                PERSONAL_CARD.ID
        ).values(id_a).values(id_b).execute();
    }

    @After
    public void tearDownn() {
        db.delete(Tables.PERSONAL_CARD).execute();
        db.delete(Tables.CARD).execute();
        db.delete(Tables.CARD_USER).execute();
        db.delete(Tables.GROUP_CARD).execute();
        db.delete(Tables.TRAVELER_USER).execute();
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

    @Test
    public void like() throws Exception {
        MatchResource matchResource = new MatchResource(appBun);
        MatchForCardRes res = matchResource.getMatching(new DefaultJwtCookiePrincipal("a"), Long.valueOf(id_a), db);
        assertTrue(res.getMatchedGroupCard().isEmpty());
        assertTrue(res.getMatchedPersonalCard().isEmpty());
    }

    @Test
    public void dislike() throws Exception {

    }

    @Test
    public void getMatching() throws Exception {

    }

    @Test
    public void isAMatch() throws Exception {

    }
}