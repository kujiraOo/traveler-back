package fi.istrange.traveler;

import fi.istrange.traveler.auth.AuthorizedUser;
import fi.istrange.traveler.auth.UserAuthenticator;
import fi.istrange.traveler.resources.TokenResource;
import fi.istrange.traveler.resources.GroupCardResource;
import fi.istrange.traveler.resources.PersonalCardResource;
import fi.istrange.traveler.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.configuration.*;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import fi.istrange.traveler.bundle.ApplicationBundle;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookieAuthBundle;
import org.glassfish.hk2.api.ServiceLocator;

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

//        environment.jersey().register(new AuthDynamicFeature(
//                new OAuthCredentialAuthFilter.Builder<AuthorizedUser>()
//                        .setAuthenticator(new UserAuthenticator())
//                        .setPrefix("Bearer")
//                        .buildAuthFilter()));
//
//        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(AuthorizedUser.class));

        // TODO pass DAOs to resources
        environment.jersey().register(new PersonalCardResource());
//        environment.jersey().register(new GroupCardResource());
//        environment.jersey().register(new UserResource());
        environment.jersey().register(new TokenResource());
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


        bootstrap.addBundle(JwtCookieAuthBundle.getDefault().withConfigurationSupplier((Configuration configuration) -> ((TravelerConfiguration) configuration).getJwtCookieAuth()));
//        bootstrap.addBundle(JwtCookieAuthBundle.getDefault());


        // remove the commets when the actual DB can be instantiated
        // bootstrap.addBundle(applicationBundle.getJooqBundle());
    }
}
