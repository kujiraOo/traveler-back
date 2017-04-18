package fi.istrange.traveler.dao;

import org.jooq.ConnectionProvider;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by arsenii on 4/18/17.
 */
public class ImageDao {

    private final ConnectionProvider connectionProvider;

    public ImageDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public byte[] getImageBuffer(long oid) throws SQLException {

        byte buf[] = null;
        Connection connection = connectionProvider.acquire();

        // All LargeObject API calls must be within a transaction block
        connection.setAutoCommit(false);

        try {
            LargeObjectManager lobj = connection.unwrap(org.postgresql.PGConnection.class).getLargeObjectAPI();
            LargeObject obj = lobj.open(oid, LargeObjectManager.READ);
            buf = new byte[obj.size()];
            obj.read(buf, 0, obj.size());
            obj.close();

            connection.commit();
        } catch (Exception e) {
            connection.rollback();
        } finally {
            connectionProvider.release(connection);
        }

        return buf;
    }
}
