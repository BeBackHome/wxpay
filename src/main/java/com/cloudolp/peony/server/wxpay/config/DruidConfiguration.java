/**
 * Copyright(c) Cloudolp Technology Co.,Ltd.
 * All Rights Reserved.
 * <p>
 * This software is the confidential and proprietary information of Cloudolp
 * Technology Co.,Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Cloudolp Technology Co.,Ltd.
 * For more information about Cloudolp, welcome to http://www.cloudolp.com
 * <p>
 * project: indent-order-service
 * <p>
 * Revision History:
 * Date		    Version		Name				Description
 * 3/16/2017	1.0			Franklin			Creation File
 */
package com.cloudolp.peony.server.wxpay.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Description:
 *
 *
 * @author Franklin
 * @date 3/16/2017 5:19 PM
 */
/*@Configuration*/
public class DruidConfiguration {
    private Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean(name = "druidDataSource")
    @Primary
    public DataSource dataSource() {
        logger.info("Initialize druid dataSource.");
        DruidDataSource d = new DruidDataSource();
        d.setDriverClassName(dataSourceProperties.getDriverClassName());
        d.setUrl(dataSourceProperties.getUrl());
        d.setUsername(dataSourceProperties.getUsername());
        d.setPassword(dataSourceProperties.getPassword());
        d.setMaxWait(6000);
        d.setMaxPoolPreparedStatementPerConnectionSize(50);
        d.setTimeBetweenEvictionRunsMillis(90000);
        d.setMinEvictableIdleTimeMillis(1800000);
        d.setRemoveAbandoned(true);
        d.setRemoveAbandonedTimeout(3600);
        d.setLogAbandoned(true);
        d.setMaxActive(100);
        d.setMinIdle(30);
        try {
            d.setFilters("stat, wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(DataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }
    @Bean
    public ServletRegistrationBean druidServlet() {
        return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    }
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
