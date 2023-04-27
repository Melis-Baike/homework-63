package com.example.homework60.dao;

import com.example.homework60.entity.Bookmark;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Optional;

@Component
public class BookmarkDao extends BasicDao{
    public BookmarkDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS bookmarks(" +
                "id BIGSERIAL PRIMARY KEY," +
                "userID BIGINT NOT NULL," +
                "publicationID BIGINT NOT NULL," +
                "CONSTRAINT fk_user " +
                "FOREIGN KEY (userID) " +
                "REFERENCES users(id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE RESTRICT," +
                "CONSTRAINT fk_publication " +
                "FOREIGN KEY (publicationID) " +
                "REFERENCES publications(id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE RESTRICT" +
                ");");
    }

    public Optional<Bookmark> checkBookmark(Long userID, Long publicationID){
        String sql = "SELECT * FROM bookmarks WHERE userID = ? AND publicationID = ?";
        return Optional.ofNullable(DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Bookmark.class), userID, publicationID)
        ));
    }

    public void insertBookmark(Long userID, Long publicationID){
        String sql = "INSERT INTO bookmarks (userID, publicationID) " +
                "VALUES(?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, userID);
            ps.setLong(2, publicationID);
            return ps;
        });
    }

    public void removeBookmark(Long userID, Long publicationID){
        String sql = "DELETE FROM bookmarks WHERE userID = ? AND publicationID = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, userID);
            ps.setLong(2, publicationID);
            return ps;
        });
    }
}
