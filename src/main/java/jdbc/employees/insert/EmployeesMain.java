package jdbc.employees.insert;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeesMain {


    public static void main(String[] args) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/employees?useUnicode=true");
        dataSource.setUser("employees");
        dataSource.setPassword("employees");

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("insert into employees(emp_name) values ('John Doe')");
            statement.executeUpdate("insert into employees(emp_name) values ('Jack Doe')");
        } catch (SQLException sqlException) {
            throw new IllegalStateException("Error by insert", sqlException);
        }
    }
}
