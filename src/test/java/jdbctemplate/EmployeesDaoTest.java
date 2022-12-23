package jdbctemplate;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testSaveAndSelectEmployee() {
        employeesDao.saveEmployee("Lili");
        employeesDao.saveEmployee("Eszti");

        List<String> names = employeesDao.listEmployeeNames();

        assertEquals(2, names.size());
        assertEquals("Eszti", names.get(0));
    }

    @Test
    void testSelectEmployeeById() {
        employeesDao.saveEmployee("Lili");
        employeesDao.saveEmployee("Eszti");

        String name = employeesDao.findEmployeeById(2);
        assertEquals("Eszti", name);
    }

    @Test
    void testDeleteEmployeeById() {
        employeesDao.saveEmployee("Lili");
        employeesDao.saveEmployee("Eszti");

        List<String> names = employeesDao.listEmployeeNames();
        assertEquals(2, names.size());

        employeesDao.deleteById(1);
        names = employeesDao.listEmployeeNames();
        assertEquals(1, names.size());
        assertEquals("Eszti", names.get(0));
    }
}