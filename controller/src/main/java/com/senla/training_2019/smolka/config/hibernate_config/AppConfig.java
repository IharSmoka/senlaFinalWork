package com.senla.training_2019.smolka.config.hibernate_config;

import com.senla.training_2019.smolka.config.file_config.FileConfig;
import liquibase.integration.spring.SpringLiquibase;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan({
        "com.senla.training_2019.smolka.dao",
        "com.senla.training_2019.smolka.service",
        "com.senla.training_2019.smolka.mappers",
        "com.senla.training_2019.smolka.api.dao"
}
)
@EnableJpaRepositories(basePackages = { "com.senla.training_2019.smolka.api.dao" }, entityManagerFactoryRef = "emF", transactionManagerRef = "tm")
@EnableTransactionManagement
public class AppConfig {

    private static final String MODEL_PACKAGE = "com.senla.training_2019.smolka.model.entities";
    private static final String FILE_NAME = "config.properties";
    private static final String LIQUIBASE_CHANGELOG_PATH = "classpath://db.changelog-master.xml";
    private static final String PERSISTENCE_UNIT_NAME = "persistence";
    private static final String HIBERNATE_CONNECTION_CHARSET_KEY = "hibernate.connection.charSet";
    private static final String HIBERNATE_CONNECTION_CHARSET_ENCODING_KEY = "hibernate.connection.characterEncoding";
    private static final String HIBERNATE_CONNECTION_USE_UNICODE_KEY = "hibernate.connection.useUnicode";
    private static final String HIBERNATE_DIALECT_KEY = "hibernate.dialect";
    private static final String HIBERNATE_SHOW_SQL_KEY = "hibernate.show_sql";
    private static final String LIQUIBASE_CONTEXT = "dev";

    @Primary
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setLocation(new ClassPathResource(FILE_NAME));
        return propertySourcesPlaceholderConfigurer;
    }

    @Primary
    @Bean
    public FileConfig config() {
        return new FileConfig();
    }

    @Primary
    @Bean(name = "emF")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource());
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setPackagesToScan(MODEL_PACKAGE);
        localContainerEntityManagerFactoryBean.setJpaProperties(hibernateProperties());
        localContainerEntityManagerFactoryBean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(HIBERNATE_CONNECTION_CHARSET_KEY, config().getDataBaseCharSet());
        hibernateProperties.setProperty(HIBERNATE_CONNECTION_CHARSET_ENCODING_KEY, config().getDataBaseCharacterEncoding());
        hibernateProperties.setProperty(HIBERNATE_CONNECTION_USE_UNICODE_KEY, config().getUseUnicode().toString());
        hibernateProperties.setProperty(HIBERNATE_DIALECT_KEY, config().getHibernateDialectName());
        hibernateProperties.setProperty(HIBERNATE_SHOW_SQL_KEY, config().getShowSql().toString());
        return hibernateProperties;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(config().getDriverNameClass());
        String urlWithTimeZone = config().getDataBaseUrl()+"?serverTimezone="+ config().getTimeZone();
        dataSource.setUrl(urlWithTimeZone);
        dataSource.setUsername(config().getDataBaseUsername());
        dataSource.setPassword(config().getDataBasePassword());
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(LIQUIBASE_CHANGELOG_PATH);
        liquibase.setDataSource(dataSource());
        liquibase.setContexts(LIQUIBASE_CONTEXT);
        return liquibase;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setPackagesToScan(MODEL_PACKAGE);
        localSessionFactoryBean.setHibernateProperties(hibernateProperties());
        localSessionFactoryBean.setDataSource(dataSource());
        return localSessionFactoryBean;
    }

    @Bean(name = "tm")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactory().getObject());
        return manager;
    }
}
