package fi.istrange.traveler.resources.profile;

import com.google.common.collect.Sets;
import fi.istrange.traveler.api.*;
import fi.istrange.traveler.resources.AbstractResourceTest;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by rohan on 5/3/17.
 */
public class ProfileResourceTest extends AbstractResourceTest {

    ProfileResource pr = new ProfileResource(appBun);

    private static final String userName = "testUser";
    private static final String lastName = "lastName";
    private static final String firstName = "firstName";
    private static final String email = "email";
    private static final String phone = "phone";
    private static final String address = "address";
    private static final String city = "city";
    private static final String country = "country";
    private static final Date dob = Date.valueOf("1999-10-10");
    private static final String gender = "sex";
    private static UserProfileRes sampleRes;


    private void createUser(String userName) {
        createUser(userName, lastName, firstName, email, phone, address, city, country, dob, gender);
    }

    @Before
    public void setUpp() {
        createUser(userName);
        sampleRes = new UserProfileRes(
                userName, firstName, lastName, email, gender, dob, phone, address, city, country, new ArrayList<>()
        );
    }


    @Test
    public void getUserProfile() throws Exception {
        UserProfileRes res = pr.getUserProfile(new DefaultJwtCookiePrincipal(userName), db);
        assertTrue(res.equals(sampleRes));
    }

    @Test
    public void updateUserProfile() throws Exception {
        String firstName = "u";
        String lastName = "u";
        String email = "u";
        String phone = "u";
        String address = "u";
        String city = "u";
        String country = "u";
        Date dob = Date.valueOf("2999-12-12");
        String gender = "undefined";
        UserProfileRes res = pr.updateUserProfile(
                new DefaultJwtCookiePrincipal(userName),
                new UserProfileUpdateReq(firstName, lastName, email, gender, dob, phone, address, city, country),
                db
        );
        assertTrue(res.equals(
                new UserProfileRes(
                        userName, firstName, lastName, email, gender, dob, phone, address, city, country, new ArrayList<Long>()
                )
        ));
    }

    @Test
    public void changePassword() throws Exception {
        assertTrue(true);
    }

    @Test
    public void getPersonalCards_emptyList() throws Exception {
        List<PersonalCardRes> res = pr.getPersonalCards(new DefaultJwtCookiePrincipal(userName), false, 10, db);
        assertTrue(res.isEmpty());
    }

    @Test
    public void getPersonalCards_oneCard() throws Exception {
        Long cardid = createPersonalTravelCardForUser(userName);
        List<PersonalCardRes> res = pr.getPersonalCards(
                new DefaultJwtCookiePrincipal(userName), true, 0, db
        );
        assertTrue(res.size() == 1);
        assertTrue(res.get(0).getID().equals(cardid));
        assertTrue(res.get(0).getOwner().equals(sampleRes));
    }

    @Test
    public void getPersonalCards_manyCard() throws Exception {
        Long cardid1 = createPersonalTravelCardForUser(userName);
        Long cardid2 = createPersonalTravelCardForUser(userName);
        Long cardid3 = createPersonalTravelCardForUser(userName);
        List<PersonalCardRes> res = pr.getPersonalCards(
                new DefaultJwtCookiePrincipal(userName), true, 0, db
        );
        assertTrue(res.size() == 3);
        assertTrue(res.stream().map(r -> r.getID()).collect(Collectors.toSet())
                .equals(Sets.newHashSet(cardid1, cardid2, cardid3)));
        res.stream().forEach(
                r -> assertTrue(r.getOwner().equals(sampleRes))
        );
    }

    @Test
    public void updatePersonalCard() {
        Long cardId = createPersonalTravelCardForUser(userName);
        final String title = "newTitle";
        final String description = "newDescription";
        final Date startTime = Date.valueOf("2000-10-10");
        final Date endTime = Date.valueOf("2000-10-12");
        final BigDecimal lon = BigDecimal.TEN;
        final BigDecimal lah = BigDecimal.TEN;
        PersonalCardUpdateReq req = new PersonalCardUpdateReq(title, description, startTime, endTime, lon, lah);
        PersonalCardRes res = pr.updatePersonalCard(
                new DefaultJwtCookiePrincipal(userName),
                cardId, req, db
        );
        assertTrue(res.getDescription().equals(description));
        assertTrue(res.getTitle().equals(title));
        assertTrue(res.getStartTime().equals(startTime));
        assertTrue(res.getEndTime().equals(endTime));
        assertTrue(res.getLatitude().subtract(lon).compareTo(BigDecimal.valueOf(0)) == 0);
        assertTrue(res.getLatitude().subtract(lah).compareTo(BigDecimal.valueOf(0)) == 0);
        assertTrue(res.getOwner().equals(sampleRes));
    }

    @Test
    public void deactivatePersonalCard() throws Exception {
        Long cardid1 = createPersonalTravelCardForUser(userName);
        PersonalCardRes res = pr.deactivatePersonalCard(
                new DefaultJwtCookiePrincipal(userName), cardid1, db
        );
        System.out.println(res);
        assertFalse(res.getActive());
    }

    @Test
    public void uploadPersonalCardPhoto() throws Exception {

    }

    @Test
    public void getGroupCards_emptyList() throws Exception {
        List<PersonalCardRes> res = pr.getPersonalCards(new DefaultJwtCookiePrincipal(userName), false, 10, db);
        assertTrue(res.isEmpty());
    }

    @Test
    public void getGroupCards_one() throws Exception {
        Long cardid = createGroupTravelCardForUser(userName);
        List<GroupCardRes> res = pr.getGroupCards(
                new DefaultJwtCookiePrincipal(userName), true, 0, db
        );
        assertTrue(res.size() == 1);
        assertTrue(res.get(0).getID().equals(cardid));
        assertTrue(res.get(0).getOwner().equals(sampleRes));
    }

    @Test
    public void getGroupCards_many() throws Exception {
        Long cardid1 = createGroupTravelCardForUser(userName);
        Long cardid2 = createGroupTravelCardForUser(userName);
        Long cardid3 = createGroupTravelCardForUser(userName);
        List<GroupCardRes> res = pr.getGroupCards(
                new DefaultJwtCookiePrincipal(userName), true, 0, db
        );
        assertTrue(res.size() == 3);
        assertTrue(res.stream().map(r -> r.getID()).collect(Collectors.toSet())
                .equals(Sets.newHashSet(cardid1, cardid2, cardid3)));
        res.stream().forEach(
                r -> assertTrue(r.getOwner().equals(sampleRes))
        );
    }

    @Test
    public void updateGroupCard() throws Exception {
        Long cardId = createPersonalTravelCardForUser(userName);
        final String title = "newTitle";
        final String description = "newDescription";
        final Date startTime = Date.valueOf("2000-10-10");
        final Date endTime = Date.valueOf("2000-10-12");
        final BigDecimal lon = BigDecimal.TEN;
        final BigDecimal lah = BigDecimal.TEN;
        final List<String> participant = Arrays.asList();
        GroupCardUpdateReq req = new GroupCardUpdateReq(title, description, startTime, endTime, lon, lah, participant);
        GroupCardRes res = pr.updateGroupCard(
                new DefaultJwtCookiePrincipal(userName),
                cardId, req, db
        );
        assertTrue(res.getDescription().equals(description));
        assertTrue(res.getTitle().equals(title));
        assertTrue(res.getStartTime().equals(startTime));
        assertTrue(res.getEndTime().equals(endTime));
        assertTrue(res.getLatitude().subtract(lon).compareTo(BigDecimal.valueOf(0)) == 0);
        assertTrue(res.getLatitude().subtract(lah).compareTo(BigDecimal.valueOf(0)) == 0);
        assertTrue(res.getOwner().equals(sampleRes));
        assertTrue(new HashSet<>(participant).equals(new HashSet<>(res.getParticipants())));
    }

    @Test
    public void deactivateGroupCard() throws Exception {
        Long cardid1 = createGroupTravelCardForUser(userName);
        GroupCardRes res = pr.deactivateGroupCard(
                new DefaultJwtCookiePrincipal(userName), cardid1, db
        );
        System.out.println(res);
        assertFalse(res.getActive());
    }

    @Test
    public void uploadPhoto() throws Exception {

    }

}