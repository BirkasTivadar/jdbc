package jdbctemplate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class EmployeesDao {

    private final JdbcTemplate jdbcTemplate;

    public EmployeesDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public long saveEmployee(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into employees(emp_name) values (?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<String> listEmployeeNames() {
        return jdbcTemplate.query("select emp_name from employees order by emp_name",
                (resultSet, i) -> resultSet.getString("emp_name"));
    }

    public String findEmployeeById(long id) {
        return jdbcTemplate.queryForObject("select emp_name from employees where id = ?",
                (resultSet, i) -> resultSet.getString("emp_name"), id);
    }

    public void deleteById(long id) {
        jdbcTemplate.update("delete from employees where id = ?",
                id);
    }
}
