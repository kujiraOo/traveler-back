package fi.istrange.traveler.dao;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by rohan on 5/2/17.
 */
public class CardPhotoDaoTest extends AbstractDaoTest {

    CardPhotoDao cardPhotoDao = new CardPhotoDao(connectionProvider);
    ImageDao imageDao = new ImageDao(connectionProvider);

    private static final String userName = "testUser";
    private static final byte[] imageData01 = "beautifulimageshavingnakednature".getBytes();
    private static final byte[] imageData02 = "crazyimagethatononewanttosee".getBytes();
    private static final byte[] imageData03 = "thisimageiscursedyoushouldneverlookintoit".getBytes();

    @Before
    public void setUpp() {
        createUser(userName, Date.valueOf("1999-10-10"), "gay");
    }

    @Test
    public void fetchPhotoOidByCardId_invalidCardId() {
        try {
            List<Long> oids = cardPhotoDao.fetchPhotoOidByCardId(ran.nextLong(), db);
            // need documentation
            assertTrue(oids.isEmpty());
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchPhotoOidByCardId_validPersonalCardId_noPhoto() {
        Long cardId = createPersonalTravelCardForUser(userName);
        try {
            List<Long> oids = cardPhotoDao.fetchPhotoOidByCardId(cardId, db);
            // need documentation
            assertTrue(oids.isEmpty());
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchPhotoOidByCardId_validGroupCardId_noPhoto() {
        Long cardId = createPersonalTravelCardForUser(userName);
        try {
            List<Long> oids = cardPhotoDao.fetchPhotoOidByCardId(cardId, db);
            // need documentation
            assertTrue(oids.isEmpty());
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchPhotoOidByCardId_validGroupCardId_onePhoto() {
        Long cardId = createGroupTravelCardForUser(userName);
        try {
            createAndFetchPhotosTest(cardId, imageData01);
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchPhotoOidByCardId_validGroupCardId_severalPhoto() {
        Long cardId = createGroupTravelCardForUser(userName);
        try {
            createAndFetchPhotosTest(cardId, imageData01, imageData02, imageData03);
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchPhotoOidByCardId_validPersonalCardId_onePhoto() {
        Long cardId = createGroupTravelCardForUser(userName);
        try {
            createAndFetchPhotosTest(cardId, imageData01);
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchPhotoOidByCardId_validPersonalCardId_severalPhoto() {
        Long cardId = createPersonalTravelCardForUser(userName);
        try {
            createAndFetchPhotosTest(cardId, imageData01, imageData02, imageData03);
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchPhotoOidByCardId_severalDuplicatePhoto() {
        Long cardId = createPersonalTravelCardForUser(userName);
        try {
            createAndFetchPhotosTest(cardId, imageData01, imageData01, imageData01, imageData01, imageData01);
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchPhotoOidByCardId_zeroLengthPhoto() {
        Long cardId = createPersonalTravelCardForUser(userName);
        try {
            createAndFetchPhotosTest(cardId, new byte[0]);
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    @Test
    public void fetchPhotoOidByCardId_bigLengthPhoto() {
        Long cardId = createPersonalTravelCardForUser(userName);
        try {
            createAndFetchPhotosTest(cardId, new byte[10000]);
        } catch (Exception ex) {
            fail("test fail");
        }
    }

    private void createAndFetchPhotosTest(Long cardId, byte[]... images) {
        Set<Long> addedOids = addPhotoForCardAndTest(cardId, images);
        List<Long> retrivedOids = cardPhotoDao.fetchPhotoOidByCardId(cardId, db);
        // need documentation
        assertTrue(new HashSet<>(retrivedOids).equals(addedOids));
    }

    @Test
    public void addPhoto_personalCard() {
        Long cardId = createPersonalTravelCardForUser(userName);
        addPhotoForCardAndTest(cardId, imageData01);
    }

    @Test
    public void addPhoto_groupCard() {
        Long cardId = createGroupTravelCardForUser(userName);
        addPhotoForCardAndTest(cardId, imageData02);
    }

    private long addPhotoForCardAndTest(Long cardId, byte[] photo) {
        try {
            Long oid = cardPhotoDao.addPhoto(cardId, new ByteArrayInputStream(imageData01));
            // need documentation
            byte[] dataBack = imageDao.getImageBuffer(oid);
            assertTrue(Arrays.equals(dataBack, imageData01));
            return oid;
        } catch (Exception ex) {
            fail("test fail");
            throw new RuntimeException();
        }
    }

    private Set<Long> addPhotoForCardAndTest(Long cardId, byte[]... photos) {
        try {
            Set<Long> oids = new HashSet<>();
            for(byte[] photo : photos) {
                oids.add(addPhotoForCardAndTest(cardId, photo));
            }
            return oids;
        } catch (Exception ex) {
            fail("test fail");
            throw new RuntimeException();
        }
    }
}