package fi.istrange.traveler;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.configuration.*;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import fi.istrange.traveler.bundle.ApplicationBundle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import javax.inject.Inject;
/**
 * Created by aleksandr on 26.3.2017.
 */
public class TravelerApplication extends Application<TravelerConfiguration> {
    private ApplicationBundle applicationBundle;

    private ServiceLocator serviceLocator;

    public static void main(final String[] args) throws Exception {
        new TravelerApplication().run(args);
    }

    @Override
    public void run(TravelerConfiguration configuration, Environment environment) {
        applicationBundle.setConfiguration(configuration);

        // TODO: add Jersey resources here
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
        // remove the commets when the actual DB can be instantiated
        // bootstrap.addBundle(applicationBundle.getJooqBundle());
    }
}
