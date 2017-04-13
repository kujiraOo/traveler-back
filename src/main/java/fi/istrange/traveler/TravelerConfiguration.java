package fi.istrange.traveler;

import com.bendb.dropwizard.jooq.JooqFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Configuration;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookieAuthConfiguration;

import javax.validation.constraints.NotNull;
import javax.validation.Valid;

/**
 * Created by aleksandr on 26.3.2017.
 */
public class TravelerConfiguration extends Configuration {
    @NotNull
    private String defaultName;

    @NotNull
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) { this.defaultName = defaultName; }

    @Valid
    @NotNull
    private JwtCookieAuthConfiguration jwtCookieAuth = new JwtCookieAuthConfiguration();

    public JwtCookieAuthConfiguration getJwtCookieAuth() {
        return jwtCookieAuth;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    @JsonProperty("jooq")
    private JooqFactory jooqFactory;

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    @JsonProperty("swagger")
    public void setSwaggerBundleConfiguration (SwaggerBundleConfiguration conf) {
        this.swaggerBundleConfiguration = conf;
    }

    public JooqFactory getJooqFactory() { return this.jooqFactory; }
}
