package fi.istrange.traveler.dao;

import org.junit.Test;

import java.sql.SQLException;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by rohan on 5/2/17.
 */
public class ImageDaoTest extends AbstractDaoTest {

    ImageDao imageDao = new ImageDao(connectionProvider);
    private static final int TEST_ITERATION = 100;

    @Test
    public void getImageBuffer_invalidOid() throws SQLException {
        // need method documentation
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    try {
                        assertNull(imageDao.getImageBuffer(ran.nextLong()));
                    } catch (Exception ex) {
                        throw new RuntimeException("failure", ex);
                    }
                }
        );
    }
}