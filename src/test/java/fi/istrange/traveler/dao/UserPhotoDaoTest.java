package fi.istrange.traveler.dao;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rohan on 5/2/17.
 */
public class UserPhotoDaoTest extends AbstractDaoTest {

    UserPhotoDao userPhotoDao = new UserPhotoDao(connectionProvider);
    ImageDao imageDao = new ImageDao(connectionProvider);

    private static final String userName = "testUser";
    private static final String invalidUserName = "dumpUser";
    private static final byte[] imageData01 = "beautifulimageshavingnakednature".getBytes();
    private static final byte[] imageData02 = "crazyimagethatononewanttosee".getBytes();
    private static final byte[] imageData03 = "thisimageiscursedyoushouldneverlookintoit".getBytes();

    @Before
    public void setUpp() {
        createUser(userName, Date.valueOf("1999-10-10"), "gay");
    }

    @Test
    public void fetchByUsername_invalidUserName() {
        try {
            List<Long> oids = userPhotoDao.fetchPhotoOidByUsername(invalidUserName, db);
            // need documentation
            assertTrue(oids.isEmpty());
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchByUsername_validUserName_emptyResult() {
        try {
            List<Long> oids = userPhotoDao.fetchPhotoOidByUsername(userName, db);
            // need documentation
            assertTrue(oids.isEmpty());
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchByUsername_validUserName_oneOids() {
        try {
            Long oid = userPhotoDao.addPhoto(userName, new ByteArrayInputStream(imageData01));
            List<Long> oids = userPhotoDao.fetchPhotoOidByUsername(userName, db);
            // need documentation
            assertTrue(oids.size() == 1);
            assertTrue(oids.contains(oid));
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchByUsername_validUserName_manyOids() {
        try {
            Long oid1 = userPhotoDao.addPhoto(userName, new ByteArrayInputStream(imageData01));
            Long oid2 = userPhotoDao.addPhoto(userName, new ByteArrayInputStream(imageData02));
            Long oid3 = userPhotoDao.addPhoto(userName, new ByteArrayInputStream(imageData03));
            List<Long> oids = userPhotoDao.fetchPhotoOidByUsername(userName, db);
            // need documentation
            assertTrue(oids.size() == 3);
            assertTrue(new HashSet<>(oids).equals(Sets.newHashSet(oid1, oid2, oid3)));
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void addPhoto() {
        try {
            Long oid = userPhotoDao.addPhoto(userName, new ByteArrayInputStream(imageData01));
            // need documentation
            byte[] dataBack = imageDao.getImageBuffer(oid);
            assertTrue(Arrays.equals(dataBack, imageData01));
        } catch (Exception ex) {
            fail("test fail");
        }
    }
}