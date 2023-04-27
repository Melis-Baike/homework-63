package com.example.homework60.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class BasicDao {
    protected final JdbcTemplate jdbcTemplate;

    public abstract void createTable();
}
