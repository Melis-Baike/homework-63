package com.example.homework60.dao;

import com.example.homework60.entity.Comment;
import com.example.homework60.entity.Publication;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class CommentDao extends BasicDao{
    public CommentDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS comments(" +
                "id BIGSERIAL PRIMARY KEY," +
                "userID BIGSERIAL NOT NULL," +
                "publicationID BIGSERIAL NOT NULL," +
                "text TEXT NOT NULL," +
                "time TIMESTAMP WITHOUT TIME ZONE NOT NULL," +
                "CONSTRAINT fk_publication " +
                "FOREIGN KEY (publicationID) " +
                "REFERENCES publications(id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE RESTRICT," +
                "CONSTRAINT fk_user " +
                "FOREIGN KEY (userID) " +
                "REFERENCES users(id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE RESTRICT" +
                ");");
    }

    public String insertComment(Long publicationID, String comment, Long userID){
        String secondSql = "SELECT * " +
                "FROM publications " +
                "WHERE id = ?";
        Optional<Publication> publication = Optional.ofNullable(DataAccessUtils.singleResult(jdbcTemplate
                .query(secondSql, new BeanPropertyRowMapper<>(Publication.class), publicationID)));
        if (publication.isPresent()) {
            String thirdSql = "INSERT INTO comments (userID, publicationID, text, time) " +
                    "VALUES(?,?,?,?)";
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(thirdSql);
                ps.setLong(1, userID);
                ps.setLong(2, publicationID);
                ps.setString(3, comment);
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                return ps;
            });
            return "You have successfully added comment under the publication!";
        } else {
            return "There is no any publication with this id " + publicationID;
        }
    }

    public Optional<List<Comment>> getComments(Long publicationID){
        String sql = "SELECT * FROM comments " +
                "WHERE publicationID = ?";
        return Optional.of(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), publicationID));
    }
}
