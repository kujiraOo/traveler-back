package fi.istrange.traveler;

import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.resources.*;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookieAuthBundle;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 * Created by aleksandr on 26.3.2017.
 */
public class TravelerApplication extends Application<TravelerConfiguration> {
    private ApplicationBundle applicationBundle;

    public static void main(final String[] args) throws Exception {
        new TravelerApplication().run(args);
    }

    @Override
    public void run(TravelerConfiguration configuration, Environment environment) {
        applicationBundle.setConfiguration(configuration);

        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(new AuthResource(applicationBundle));
        environment.jersey().register(new PersonalCardResource(applicationBundle));
        environment.jersey().register(new GroupCardResource(applicationBundle));
        environment.jersey().register(new UserResource(applicationBundle));
        environment.jersey().register(new ProfileResource(applicationBundle));
        environment.jersey().register(new ImageResource(applicationBundle));
    }

    @Override
    public void initialize(Bootstrap<TravelerConfiguration> bootstrap) {
        applicationBundle = new ApplicationBundle();

        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new AssetsBundle());

        bootstrap.addBundle(new MigrationsBundle<TravelerConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(TravelerConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(new SwaggerBundle<TravelerConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(TravelerConfiguration portalConfiguration) {
                return portalConfiguration.getSwaggerBundleConfiguration();
            }
        });


        bootstrap.addBundle(JwtCookieAuthBundle.getDefault().withConfigurationSupplier((Configuration configuration) ->
                ((TravelerConfiguration) configuration).getJwtCookieAuth()));

        bootstrap.addBundle(applicationBundle.getJooqBundle());
    }
}
