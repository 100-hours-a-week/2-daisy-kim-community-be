package daisy.community_be.repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<String> getAllMemberNames() {
        String sql = "SELECT name FROM members";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
    }
}
