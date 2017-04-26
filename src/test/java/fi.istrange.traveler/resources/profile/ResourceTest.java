package fi.istrange.traveler.resources.profile;

import com.bendb.dropwizard.jooq.JooqBundle;
import fi.istrange.traveler.TravelerConfiguration;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.Tables;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by rohan on 4/26/17.
 */
public class ResourceTest {
    protected static ApplicationBundle appBun = mock(ApplicationBundle.class);
    private static JooqBundle<TravelerConfiguration> jooqBundle = mock(JooqBundle.class);

    private static Connection conn;
    protected static DSLContext db;
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

    @After
    public void tearDownn() {
        db.delete(Tables.PERSONAL_CARD).execute();
        db.delete(Tables.CARD_USER).execute();
        db.delete(Tables.GROUP_CARD).execute();
        db.delete(Tables.MATCH).execute();
        db.delete(Tables.CARD).execute();
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

}
