package jdbc.employees.transaction;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeesDaoTest {

    private EmployeesDao employeesDao;

    @BeforeEach
    void init() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/employees?useUnicode=true");
        dataSource.setUser("employees");
        dataSource.setPassword("employees");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();

        flyway.clean();
        flyway.migrate();

        employeesDao = new EmployeesDao(dataSource);
    }

    @Test
    void testSaveCorrectEmployees() {
        List<String> names = Arrays.asList("John Doe", "Jack Doe", "Jane Doe");

        employeesDao.saveEmployees(names);

        List<String> namesForDatabase = employeesDao.listEmployeeNames();

        assertEquals(3, namesForDatabase.size());
    }

    @Test
    void testSaveEmployeesRollback() {
        List<String> names = Arrays.asList("John Doe", "xJack Doe", "Jane Doe");

        employeesDao.saveEmployees(names);

        List<String> namesForDatabase = employeesDao.listEmployeeNames();
        assertEquals(Collections.emptyList(), namesForDatabase);
    }
}