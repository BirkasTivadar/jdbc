package jdbc.employees.selecwithparameter;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EmployeesMain {

    public static void main(String[] args) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/employees?useUnicode=true");
        dataSource.setUser("employees");
        dataSource.setPassword("employees");

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("select emp_name from employees where id=?");
        ) {
            statement.setLong(1, 1L);
            try (
                    ResultSet rs = statement.executeQuery();
            ) {
                if (rs.next()) {
//                    String name = rs.getString(1);
                    String name = rs.getString("emp_name");
                    System.out.println(name);
                } else throw new IllegalArgumentException("Wrong id");
            } catch (SQLException sqlException) {
                throw new IllegalStateException("Cannot select", sqlException);
            }
        } catch (SQLException sqlException) {
            throw new IllegalStateException("Cannot select", sqlException);
        }
    }
}
