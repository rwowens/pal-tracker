package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    public static final RowMapper<TimeEntry> MAPPER = new RowMapper<TimeEntry>() {
        @Override
        public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TimeEntry(rs.getLong("id"),
                    rs.getLong("project_id"),
                    rs.getLong("user_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getInt("hours"));
        }
    };

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setString(3, timeEntry.getDate().format(DateTimeFormatter.BASIC_ISO_DATE));
            ps.setInt(4, timeEntry.getHours());
            return ps;
        };
        getJdbcTemplate().update(psc, keyHolder);
        return new TimeEntry(keyHolder.getKey().longValue(),
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours());
    }

    @Override
    public TimeEntry find(Long id) {
        try {
            return getJdbcTemplate().queryForObject("SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?", MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        try {
            return getJdbcTemplate().query("SELECT id, project_id, user_id, date, hours FROM time_entries", MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        getJdbcTemplate().update("UPDATE time_entries SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate().format(DateTimeFormatter.BASIC_ISO_DATE),
                timeEntry.getHours(),
                id);
        return find(id);
    }

    @Override
    public void delete(Long id) {
        getJdbcTemplate().update("DELETE FROM time_entries WHERE id = ?", id);
    }

    private DataSource getDataSource() {
        return dataSource;
    }

    private JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
