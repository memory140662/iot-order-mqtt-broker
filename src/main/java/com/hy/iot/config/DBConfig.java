package com.hy.iot.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DBConfig {

    private Dotenv env = Dotenv.configure().ignoreIfMissing().load();

    @Primary
    @Bean
    public DataSource dataSource() {
        val dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.get("JDBC_CLASSNAME", "com.mysql.cj.jdbc.Driver"));
        dataSource.setUrl(env.get("JDBC_URL"));
        dataSource.setUsername(env.get("JDBC_USERNAME"));
        dataSource.setPassword(env.get("JDBC_PASSWORD"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        val vendorAdapter = new HibernateJpaVendorAdapter();

        if (!StringUtils.equalsAnyIgnoreCase(env.get("ENV", "develop"), "production", "prod")) {
            vendorAdapter.setGenerateDdl(true);
            vendorAdapter.setShowSql(true);
        }

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.hy.iot.entity");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(new Properties() {{
            put("hibernate.jdbc.time_zone", env.get("JDBC_TIMEZONE", "Z"));
            put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            put("hibernate.ddl-auto", "update");
        }});
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        val txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

}
