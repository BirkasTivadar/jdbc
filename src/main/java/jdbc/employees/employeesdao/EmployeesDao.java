package jdbc.employees.employeesdao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeesDao {

    private static final String MESSAGE_CAN_NOT_SELECT = "Cannot select";
    private static final String MESSAGE_CAN_NOT_INSERT = "Cannot insert";
    private static final String MESSAGE_WRONG_ID = "Wrong id";
    private static final String MESSAGE_ERROR_BY_DELETE = "Error by delete";

    private static final String SQL_EMPLOYEE_NAME_COLUMN = "emp_name";
    private static final String SQL_INSERT_EMPLOYEE = "insert into employees(emp_name) values (?)";
    private static final String SQL_SELECT_ALL_NAMES = "select emp_name from employees";
    private static final String SQL_SELECT_NAME_BY_ID = "select emp_name from employees where id=?";
    private static final String SQL_DELETE_BY_ID = "delete from employees where id=?";


    private final DataSource dataSource;

    public EmployeesDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void saveEmployee(String name) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_INSERT_EMPLOYEE)
        ) {
            statement.setString(1, name);
            statement.executeUpdate();

        } catch (SQLException sqlException) {
            throw new IllegalStateException(MESSAGE_CAN_NOT_INSERT, sqlException);
        }
    }

    public List<String> listEmployeeNames() {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(SQL_SELECT_ALL_NAMES)
        ) {
            return getNames(rs);
        } catch (SQLException sqlException) {
            throw new IllegalStateException(MESSAGE_CAN_NOT_SELECT, sqlException);
        }
    }

    private List<String> getNames(ResultSet rs) throws SQLException {
        List<String> names = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString(SQL_EMPLOYEE_NAME_COLUMN);
            names.add(name);
        }
        return names;
    }

    public String selectEmployeeNameById(long id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_SELECT_NAME_BY_ID);
        ) {
            statement.setLong(1, id);
            return getName(statement);
        } catch (SQLException sqlException) {
            throw new IllegalStateException(MESSAGE_CAN_NOT_SELECT, sqlException);
        }
    }

    private String getName(PreparedStatement statement) {
        try (
                ResultSet rs = statement.executeQuery();
        ) {
            if (rs.next()) {
                return rs.getString(SQL_EMPLOYEE_NAME_COLUMN);
            }
            throw new IllegalArgumentException(MESSAGE_WRONG_ID);
        } catch (SQLException sqlException) {
            throw new IllegalStateException(MESSAGE_CAN_NOT_SELECT, sqlException);
        }
    }

    public void deleteById(long id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)
        ) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException sqlException) {
            throw new IllegalStateException(MESSAGE_ERROR_BY_DELETE, sqlException);
        }
    }
}

