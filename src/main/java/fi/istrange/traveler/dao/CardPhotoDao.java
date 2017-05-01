package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by aleksandr on 30.4.2017.
 */
public class CardPhotoDao {
    private final Connection connection;

    public CardPhotoDao(Configuration configuration) {
        connection = configuration.connectionProvider().acquire();
    }

    public List<Long> fetchById(
            Long id,
            DSLContext database
    ) {
        return database.select(Tables.CARD_PHOTO.PHOTO)
                .from(Tables.CARD_PHOTO)
                .where(Tables.CARD_PHOTO.CARD_ID.equal(id))
                .fetch()
                .map(p -> p.getValue(Tables.CARD_PHOTO.PHOTO)
                );
    }

    public void addPhoto(Long id, InputStream uploadedPhotoStream) throws SQLException, IOException {

        // All LargeObject API calls must be within a transaction block
        connection.setAutoCommit(false);
        // Get the Large Object Manager to perform operations with
        LargeObjectManager lobj = connection.unwrap(org.postgresql.PGConnection.class).getLargeObjectAPI();

        // Create a new large object
        long oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);

        // Open the large object for writing
        LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);

        try {
            // Copy the data from the file to the large object
            byte buf[] = new byte[2048];
            int s, tl = 0;
            while ((s = uploadedPhotoStream.read(buf, 0, 2048)) > 0) {
                obj.write(buf, 0, s);
                tl += s;
            }

            // Close the large object
            obj.close();

            // Insert the row into user_photo
            PreparedStatement ps = connection.prepareStatement("INSERT INTO card_photo VALUES (?, ?)");
            ps.setLong(1, id);
            ps.setLong(2, oid);
            ps.executeUpdate();
            ps.close();
            uploadedPhotoStream.close();

            // Commit the transaction
            connection.commit();
        } catch (Exception e) {

            // Rollback the transaction if saving of the photo failed
            connection.rollback();

            throw (e);
        }
    }
}
