package com.healthMonitor.fall2020.config;

import com.healthMonitor.fall2020.orm.MysqlTemplateImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;


@Configuration
@Slf4j
public class DbConfig {


    /*
    @Bean(name = "dataSource")

    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSource() {
       // return DataSourceBuilder.create().build();
        log.info("0000000000");
       return  new DruidDataSource();
    }


     */



    @Resource
    DataSource dataSource;

    @Resource
    JdbcTemplate jdbcTemplate;

    @Bean(name = "iDao")
    public MysqlTemplateImpl iDao() {
        //public MysqlTemplateImpl iDao(@Qualifier("dataSource") DataSource dataSource) {

        MysqlTemplateImpl dao=new MysqlTemplateImpl();

        dao.setDataSource(dataSource);
        return dao;
    }




}
