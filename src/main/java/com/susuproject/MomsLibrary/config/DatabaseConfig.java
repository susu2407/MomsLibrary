package com.susuproject.MomsLibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Configurable;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

@Configurable
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:momslibrary.db");
        return dataSource;
    }

}
