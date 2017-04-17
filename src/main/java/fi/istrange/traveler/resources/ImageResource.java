package fi.istrange.traveler.resources;

import fi.istrange.traveler.bundle.ApplicationBundle;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jooq.Configuration;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by arsenii on 4/17/17.
 */
@Path("/images")
@Produces("image/png")
@PermitAll
public class ImageResource {

    private final Connection connection;

    public ImageResource(ApplicationBundle applicationBundle) {
        connection = applicationBundle.getJooqBundle().getConfiguration().connectionProvider().acquire();
    }

    @GET
    @Path("/{oid}")
    public Response getImage(@PathParam("oid") long oid) throws SQLException {
        // All LargeObject API calls must be within a transaction block
        connection.setAutoCommit(false);

        try {
            LargeObjectManager lobj = connection.unwrap(org.postgresql.PGConnection.class).getLargeObjectAPI();
            LargeObject obj = lobj.open(oid, LargeObjectManager.READ);
            byte buf[] = new byte[obj.size()];
            obj.read(buf, 0, obj.size());
            obj.close();

            connection.commit();

            return Response.ok(new ByteArrayInputStream(buf)).build();

        } catch (Exception e) {
            connection.rollback();

            throw new NotFoundException();
        }
    }
}
