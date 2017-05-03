package fi.istrange.traveler.dao;

import fi.istrange.traveler.util.TestUtils;
import org.jooq.ConnectionProvider;
import org.jooq.impl.DefaultConnectionProvider;

import java.util.Random;

/**
 * Created by rohan on 5/2/17.
 */
public class AbstractDaoTest extends TestUtils {
    protected final ConnectionProvider connectionProvider = new DefaultConnectionProvider(conn);
    protected final Random ran = new Random();
}
