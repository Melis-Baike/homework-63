package com.example.homework60.dao;

import com.example.homework60.entity.PublicationImage;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class PublicationImageDao extends BasicDao{
    public PublicationImageDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS publication_image\n" +
                "(\n" +
                "    id          BIGSERIAL PRIMARY KEY,\n" +
                "    link VARCHAR\n" +
                ");");
    }

    public Optional<List<PublicationImage>> getAll(){
        String sql = "SELECT * FROM publication_image";
        return Optional.of(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PublicationImage.class)));
    }

    public Optional<PublicationImage> findById(Long id) {
        String sql = "SELECT * " +
                "FROM publication_image " +
                "WHERE id = ?";
        return Optional.ofNullable(DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PublicationImage.class), id)
        ));
    }

    public Long save(PublicationImage image) {
        String sql = "INSERT INTO publication_image(link) " +
                "VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, image.getLink());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
