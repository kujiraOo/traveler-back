package fi.istrange.traveler.resources;

import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.dao.ImageDao;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;

/**
 * Created by arsenii on 4/17/17.
 */
@Path("/images")
@Produces("image/png")
@PermitAll
public class ImageResource {

    private final ImageDao imageDao;

    public ImageResource(ApplicationBundle applicationBundle) {
        imageDao = new ImageDao(applicationBundle.getJooqBundle().getConfiguration().connectionProvider());
    }

    @GET
    @Path("/{oid}")
    public Response getImage(@PathParam("oid") long oid) throws SQLException {

        byte[] imageBuffer = imageDao.getImageBuffer(oid);

        if (imageBuffer == null) {
            throw new NotFoundException();
        }

        return Response.ok(new ByteArrayInputStream(imageBuffer)).build();
    }
}
