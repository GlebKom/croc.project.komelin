package ru.komelin.crocprojectkomelin.dao;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class UniqueNumberDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UniqueNumberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void init() {
        jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS unique_number");
    }

    public BigDecimal getUniqueNumber() {
        BigDecimal decimal = jdbcTemplate.query("select nextval('unique_number')",
                rs -> {
                    if (rs.next()) {
                        return rs.getBigDecimal(1);
                    } else {
                        throw new SQLException();
                    }
                });

        Objects.requireNonNull(decimal);
        return decimal;
    }
}
