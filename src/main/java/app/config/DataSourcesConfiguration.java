package app.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)

public class DataSourcesConfiguration {
    @Primary
    @Bean(name = "mariadbStores")
    @ConfigurationProperties(prefix = "spring.datasource.stores")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "mariadbStoresJdbcTemplate")
    public JdbcTemplate jdbcTemplate1(@Qualifier("mariadbStores") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "mariadbOrders")
    @ConfigurationProperties(prefix = "spring.datasource.orders")
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mariadbOrdersJdbcTemplate")
    public JdbcTemplate jdbcTemplate2(@Qualifier("mariadbOrders") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "mariadbUsers")
    @ConfigurationProperties(prefix = "spring.datasource.users")
    public DataSource dataSource3() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mariadbUsersJdbcTemplate")
    public JdbcTemplate jdbcTemplate3(@Qualifier("mariadbUsers") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    @Bean(name = "mariadbCustomers")
    @ConfigurationProperties(prefix = "spring.datasource.customers")
    public DataSource dataSource4() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mariadbCustomersJdbcTemplate")
    public JdbcTemplate jdbcTemplate4(@Qualifier("mariadbCustomers") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    @Bean(name = "mariadbInvoices")
    @ConfigurationProperties(prefix = "spring.datasource.invoices")
    public DataSource dataSource5() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mariadbInvoicesJdbcTemplate")
    public JdbcTemplate jdbcTemplate5(@Qualifier("mariadbInvoices") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    @Bean(name = "mariadbProducts")
    @ConfigurationProperties(prefix = "spring.datasource.products")
    public DataSource dataSource6() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mariadbProductsJdbcTemplate")
    public JdbcTemplate jdbcTemplate6(@Qualifier("mariadbProducts") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    @Bean(name = "mariadbTimekeeping")
    @ConfigurationProperties(prefix = "spring.datasource.timekeeping")
    public DataSource dataSource7() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mariadbTimekeepingJdbcTemplate")
    public JdbcTemplate jdbcTemplate7(@Qualifier("mariadbTimekeeping") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    @Bean(name = "mariadbOrders2")
    @ConfigurationProperties(prefix = "spring.datasource2.orders")
    public DataSource dataSource8() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mariadbOrders2JdbcTemplate")
    public JdbcTemplate jdbcTemplate8(@Qualifier("mariadbOrders2") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
