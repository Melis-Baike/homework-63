package com.example.homework60.dao;

import com.example.homework60.entity.Publication;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
public class PublicationDao extends BasicDao{
    public PublicationDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }
    @Override
    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS publications(" +
                "id BIGSERIAL PRIMARY KEY," +
                "userID BIGSERIAL NOT NULL," +
                "imageID INTEGER NOT NULL," +
                "description TEXT," +
                "time TIMESTAMP WITHOUT TIME ZONE NOT NULL," +
                "CONSTRAINT fk_user " +
                "FOREIGN KEY (userID) " +
                "REFERENCES users(id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE RESTRICT" +
                ");");
    }

    public List<Publication> selectAllPublications(){
        String sql = "SELECT * FROM publications";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Publication.class));
    }

    public String insertPublication(Publication publication){
        String sql = "INSERT INTO publications (userID, imageID, description, time) " +
                "VALUES(?,?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, publication.getUserID());
            ps.setLong(2, publication.getImageID());
            ps.setString(3, publication.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(publication.getTime()));
            return ps;
        });
        return String.valueOf(selectAllPublications().size());
    }

    public Optional<Publication> getPublication(Long id){
        String sql = "SELECT * FROM publications " +
                "WHERE id = ?";
        return Optional.ofNullable(DataAccessUtils.singleResult(jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(Publication.class), id)));
    }
}
