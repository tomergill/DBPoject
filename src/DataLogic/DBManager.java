package DataLogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.sql.DriverManager;
import java.util.Scanner;

/**
 * Class for querying a remote DB using SQL.
 * Please look at IDBManager's javadoc for format explaining.
 */
public class DBManager implements IDBManager {
    private Connection con; //DB connection
    private static List<String> DDLReturnAnswer = null; //DML commands returning table as answer
    private static List<String> DMLAffectingRows = null; //DDL commands returning no. of rows affe.

    /**
     * Ctor - Opens a connection to the DB and inserts commands to the static lists.
     * @param path Path to the config file, consisting of 3 lines: URL of the DB (server's
     *             adress, db name and port), User name and Password.
     * @throws IOException Error opening the config file.
     * @throws SQLException Error opening the connection.
     */
    public DBManager(String path) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String url = reader.readLine();
        String uname = reader.readLine();
        String pass = reader.readLine();
        this.con = DriverManager.getConnection(url, uname, pass);

        if (DDLReturnAnswer == null) {
            DDLReturnAnswer = new LinkedList<>();
            DDLReturnAnswer.add("SHOW");
            DDLReturnAnswer.add("DESCRIBE");
        }
        if (DMLAffectingRows == null) {
            DMLAffectingRows = new LinkedList<>();
            DMLAffectingRows.add("UPDATE");
            DMLAffectingRows.add("INSERT");
            DMLAffectingRows.add("DELETE");

        }
    }

    /**
     * Executes a DML query/update to the DB.
     * @param query The query to execute on the DB.
     * @return Either a table, the number of rows affected or an error message, in the format
     *      specified in the interface's javadoc.
     */
    @Override
    public String DMLQuery(String query) {
        boolean answer = false;
        for (String command : DMLAffectingRows) {
            if (query.startsWith(command)) {
                answer = true;
                break;
            }
        }
        try {
            if (!answer) { //returns a table
                return executeQuery(query);
            } else {
                return executeUpdate(query);
            }
        } catch (SQLException sql) {
            return errMsgFromException(sql);
        }
    }

    /**
     * Executes a DDL query/update to the DB.
     * @param query The query to execute on the DB.
     * @return Either a table, the number of rows affected or an error message, in the format
     *      specified in the interface's javadoc.
     */
    @Override
    public String DDLQuery(String query) {

        boolean returnsAnswer = false;
        for (String command : DDLReturnAnswer) {
            if (query.startsWith(command))
            {
                returnsAnswer = true;
                break;
            }
        }

        try {
            if (returnsAnswer) {
                return executeQuery(query);
            } else {
                return executeUpdate(query);
            }
        } catch (SQLException sql) {
            return errMsgFromException(sql);
        }
    }

    /**
     * Closes the connection to the DB.
     * @return true if the connection closed successfully, otherwise returns false.
     */
    @Override
    public boolean closeConnection() {
        try {
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error disconnecting from SQL server:");
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * Executes the script query by query.
     * @param scriptPath Path to script file to read from.
     * @return A list of answers, one answer to each query in the script file.
     * @throws IOException In case opening the script is unsuccessful.
     */
    @Override
    public List<String> executeScript(String scriptPath) throws IOException {
        Scanner scanner = new Scanner(new File(scriptPath));
        scanner.useDelimiter(";");

        String command;
        List<String> answer = new LinkedList<>();
        while (scanner.hasNext()) {
            command = scanner.next();
            String commandType = command.trim().split(" ")[0];
            try {
                if (!DMLAffectingRows.contains(commandType) //returns table
                        || DDLReturnAnswer.contains(commandType)) {
                    answer.add(executeQuery(command));
                } else { //returns number of rows affected
                    answer.add(executeUpdate(command));
                }
            } catch (SQLException e) {
                answer.add(errMsgFromException(e));
            }
        }
        return answer;
    }

    /**
     * Formatting an exception to an error message.
     * @param e Exception to format.
     * @return The error message from the exception, in the interface's format.
     */
    private String errMsgFromException(SQLException e) {
        String result;
        if (e.toString().contains("syntax"))
            result = "WRONG QUERY STRUCTURE";
        else
            result = "LOGICAL ERROR";

        result += "\n" + e.getMessage() + "\n";
        return result;
    }

    /**
     * Execute a query - returns a table in the format specified in the interface's javadoc.
     * @param query Query to execute.
     * @return The table returned by the DB.
     * @throws SQLException If there was an error in the query.
     */
    private String executeQuery(String query) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet result = statement.executeQuery(query);

        StringBuilder info = new StringBuilder("");
        int colNum = result.getMetaData().getColumnCount();

        //put columns headers
        int i;
        for (i = 1; i < colNum; i++) {
            info.append(result.getMetaData().getColumnName(i));
            info.append(",");
        }
        if (i == colNum) {
            info.append(result.getMetaData().getColumnName(i));
            info.append("\n");
        }

        //get each row's data
        while (result.next()) {
            for (i = 1; i < colNum; i++) {
                info.append(result.getString(i));
                info.append(",");
            }
            if (i == colNum){
                info.append(result.getString(i));
                info.append("\n");
            }
        }

        return info.toString();
    }

    /**
     * Execute an update to the table - returns the number of rows affected in the format specified
     *      in the interface's javadoc.
     * @param query Query to execute.
     * @return The number of affected rows returned by the DB.
     * @throws SQLException If there was an error in the query.
     */
    private String executeUpdate(String query) throws SQLException {
        Statement statement = con.createStatement();
        return "Rows affected :" + statement.executeUpdate(query) + "\n";
    }
}
