package jdbc.employees.insertwithparameter;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeesMain {

    public static void main(String[] args) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/employees?useUnicode=true");
        dataSource.setUser("employees");
        dataSource.setPassword("employees");

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("insert into employees(emp_name) values (?)")
        ) {
            statement.setString(1, "Jane Doe");
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new IllegalStateException("Error by insert", sqlException);
        }
    }
}
