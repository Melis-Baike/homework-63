package com.example.homework60.dao;

import com.example.homework60.entity.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Optional;

@Component
public class UserDao extends BasicDao{
    public UserDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users(" +
                "id BIGSERIAL PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "publications INTEGER DEFAULT 0," +
                "subscriptions INTEGER DEFAULT 0," +
                "followers INTEGER DEFAULT 0," +
                "enabled BOOL DEFAULT true,"+
                "roles TEXT NOT NULL" +
                ");");
    }

    public void insertUser(User user){
        String sql = "INSERT INTO users (name, email, password, publications, subscriptions, followers, enabled, roles) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getPublications());
            ps.setInt(5, user.getSubscriptions());
            ps.setInt(6, user.getFollowers());
            ps.setBoolean(7, user.isEnabled());
            ps.setString(8, user.getRoles());
            return ps;
        });
    }

    public Optional<User> findUserByName(String name) {
        String sql =
                "SELECT * " +
                        "FROM users " +
                        "WHERE name = ?";
        return Optional.ofNullable(DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), name)));
    }

    public Optional<User> findByNameAndEmail(String name, String email) {
        String sql =
                "SELECT * " +
                        "FROM users " +
                        "WHERE name = ? AND email = ?";
        return Optional.ofNullable(DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), name, email)));
    }

    public Optional<User> findUserByEmail(String email) {
        String sql =
                "SELECT * " +
                        "FROM users " +
                        "WHERE email = ?";
        return Optional.ofNullable(DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email)));
    }

    public boolean checkExistingOfUser(String email) {
        String sql =
                "SELECT * " +
                        "FROM users " +
                        "WHERE email = ?";
        Optional<User> user = Optional.ofNullable(DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email)));
        return user.isPresent();
    }


}
