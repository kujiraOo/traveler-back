package fi.istrange.traveler.bundle;

import com.bendb.dropwizard.jooq.JooqBundle;
import com.bendb.dropwizard.jooq.JooqFactory;
import fi.istrange.traveler.TravelerConfiguration;
import io.dropwizard.db.DataSourceFactory;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;

/**
 * Created by aleksandr on 30.3.2017.
 */
@Service
@Singleton
public class ApplicationBundle {
    private final JooqBundle<TravelerConfiguration> jooqBundle;
    private TravelerConfiguration configuration;

    public ApplicationBundle() {
        jooqBundle = new JooqBundle<TravelerConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(TravelerConfiguration travelerConfiguration) {
                return travelerConfiguration.getDataSourceFactory();
            }

            @Override
            public JooqFactory getJooqFactory(TravelerConfiguration travelerConfiguration) {
                return travelerConfiguration.getJooqFactory();
            }
        };
    }

    public JooqBundle<TravelerConfiguration> getJooqBundle() { return jooqBundle; }

    public TravelerConfiguration getConfiguration() { return configuration; }

    public void setConfiguration(TravelerConfiguration configuration) { this.configuration = configuration; }
}
