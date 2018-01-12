package uk.nwolverson.mapex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class Config {
    @Bean
    public StravaContext stravaContext() {
        return new StravaContext();
    }

    @Bean
    public Db db(@Value("${DB_CONN}") String conn) {
        return new Db(conn);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer configurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
