package com.example.homework60.dao;

import com.example.homework60.entity.Comment;
import com.example.homework60.entity.ILike;
import com.example.homework60.entity.Publication;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Optional;

@Component
public class LikeDao extends BasicDao{
    public LikeDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS likes(" +
                "id BIGSERIAL PRIMARY KEY," +
                "userID BIGSERIAL NOT NULL," +
                "publicationID BIGINT NULL," +
                "commentID BIGINT NULL," +
                "time TIMESTAMP WITHOUT TIME ZONE NOT NULL," +
                "CONSTRAINT fk_user " +
                "FOREIGN KEY (userID) " +
                "REFERENCES users(id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE RESTRICT," +
                "CONSTRAINT fk_publication " +
                "FOREIGN KEY (publicationID) " +
                "REFERENCES publications(id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE RESTRICT," +
                "CONSTRAINT fk_comment " +
                "FOREIGN KEY (commentID) " +
                "REFERENCES comments(id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE RESTRICT" +
                ");");
    }

    public Optional<ILike<Publication>> checkPublicationLike(Long userID, Long publicationID){
        String sql = "SELECT * FROM likes WHERE userID = ? AND publicationID = ?";
        return Optional.ofNullable(DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ILike.class), userID, publicationID)));
    }

    public void insertPublicationLike(ILike<Publication> iLike){
        String sql = "INSERT INTO likes (userID, publicationID, time) " +
                "VALUES(?,?,?)";
        insertLike(iLike, sql);
    }

    public void insertCommentLike(ILike<Comment> iLike){
        String sql = "INSERT INTO likes (userID, commentID, time) " +
                "VALUES(?,?,?)";
        insertLike(iLike, sql);
    }

    private void insertLike(ILike iLike, String sql){
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, iLike.getUserID());
            ps.setLong(2, iLike.getLikeObjectID());
            ps.setTimestamp(3, Timestamp.valueOf(iLike.getTime()));
            return ps;
        });
    }

    public void removePublicationLike(Long userID, Long publicationID){
        String sql = "DELETE FROM likes WHERE userID = ? AND publicationID = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, userID);
            ps.setLong(2, publicationID);
            return ps;
        });
    }
}
