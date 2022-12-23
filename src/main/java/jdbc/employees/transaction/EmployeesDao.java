package jdbc.employees.transaction;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeesDao {

    private static final String MESSAGE_CAN_NOT_SELECT = "Cannot select";
    private static final String MESSAGE_CAN_NOT_INSERT = "Cannot insert";
    private static final String MESSAGE_CAN_NOT_GET_ID = "Cannot get id";
    private static final String MESSAGE_INVALID_NAME = "Invalid name";
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

    public void saveEmployees(List<String> names) {
        try (
                Connection connection = dataSource.getConnection()
        ) {
            executeTransaction(names, connection);

        } catch (SQLException sqlException) {
            throw new IllegalStateException(MESSAGE_CAN_NOT_INSERT, sqlException);
        }
    }

    private static void executeTransaction(List<String> names, Connection connection) throws SQLException {
        connection.setAutoCommit(false);

        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_EMPLOYEE)) {
            names.forEach(name -> {
                try {
                    if (name.startsWith("x")) throw new IllegalArgumentException(MESSAGE_INVALID_NAME);
                    statement.setString(1, name);
                    statement.executeUpdate();
                } catch (SQLException sqlException) {
                    throw new IllegalStateException(MESSAGE_CAN_NOT_INSERT);
                }
            });

            connection.commit();
        } catch (IllegalArgumentException illegalArgumentException) {
            connection.rollback();
        }
    }


    public long saveEmployee(String name) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        SQL_INSERT_EMPLOYEE,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setString(1, name);
            statement.executeUpdate();

            return getIdByStatement(statement);
        } catch (SQLException sqlException) {
            throw new IllegalStateException(MESSAGE_CAN_NOT_INSERT, sqlException);
        }
    }

    private long getIdByStatement(PreparedStatement statement) {
        try (
                ResultSet resultSet = statement.getGeneratedKeys()
        ) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            throw new IllegalStateException(MESSAGE_CAN_NOT_GET_ID);
        } catch (SQLException sqlException) {
            throw new IllegalStateException(MESSAGE_CAN_NOT_GET_ID, sqlException);
        }
    }

    public List<String> listEmployeeNames() {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(SQL_SELECT_ALL_NAMES)
        ) {
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString(SQL_EMPLOYEE_NAME_COLUMN);
                names.add(name);
            }
            return names;
        } catch (SQLException sqlException) {
            throw new IllegalStateException(MESSAGE_CAN_NOT_SELECT, sqlException);
        }
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


