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
 * Created by Tomer Gill on 06-Jul-17.
 */
public class DBManager implements IDBManager {
    private Connection con;
    private static List<String> DDLReturnAnswer = null;
    private static List<String> DMLAffectingRows = null;

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

    @Override
    public String executeScript(String scriptPath) throws IOException {
        Scanner scanner = new Scanner(new File(scriptPath));
        scanner.useDelimiter(";");

        String command;
        StringBuilder answer = new StringBuilder("");
        while (scanner.hasNext()) {
            command = scanner.next();
            String commandType = command.trim().split(" ")[0];
            try {
                if (!DMLAffectingRows.contains(commandType) //returns table
                        || DDLReturnAnswer.contains(commandType)) {
                    answer.append(executeQuery(command));
                } else { //returns number of rows affected
                    answer.append(executeUpdate(command));
                }
            } catch (SQLException e) {
                answer.append(errMsgFromException(e));
            } finally {
                if (scanner.hasNext())
                    answer.append("\n");
            }
        }
        return answer.toString();
    }

    private String errMsgFromException(SQLException e) {
        String result;
        if (e.toString().contains("syntax"))
            result = "WRONG QUERY STRUCTURE";
        else
            result = "LOGICAL ERROR";

        result += "\n" + e.getMessage() + "\n";
        return result;
    }

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

    private String executeUpdate(String query) throws SQLException {
        Statement statement = con.createStatement();
        return "Rows affected :" + statement.executeUpdate(query) + "\n";
    }
}
