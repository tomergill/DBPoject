package DataLogic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomer Gill on 06-Jul-17.
 */
public class DBManager implements IDBManager {
    private Connection con;
    private static List<String> DDLReturnAnswer = null;

    public DBManager(String path) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String url = reader.readLine();
        String uname = reader.readLine();
        String pass = reader.readLine();
        this.con = DriverManager.getConnection(url, uname, pass);

        if (DDLReturnAnswer == null) {
            DDLReturnAnswer = new ArrayList<>();
            DDLReturnAnswer.add("SHOW");
            DDLReturnAnswer.add("DESCRIBE");
        }
    }

    @Override
    public String DMLQuery(String query)
    {
        /*
        ResultSet result;
        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            if (answer)
            {
                String table = "";
                int colNum = result.getMetaData().getColumnCount();

                //put columns headers
                int i;
                for (i = 1; i < colNum - 1; i++) {
                    table += result.getMetaData().getColumnName(i) + ",";
                }
                if (i == colNum - 1)
                    table += result.getMetaData().getColumnName(i) + "\n";

                //get each row's data
                while (result.next()) {
                    for (i = 1; i < colNum - 1; i++) {
                        table += result + ",";
                    }
                    if (i == colNum - 1)
                        table += result.getMetaData().getColumnName(i) + "\n";
                }
            }
            else
            {

            }
        } catch (SQLException sql) {
            return errMsgFromException(sql);
        }
        */return "";
    }

    @Override
    public String DDLQuery(String query)
    {
        /*
        boolean answer = false;
        for (String command: DDLReturnAnswer) {
            if (answer = query.startsWith(command))
                break;
        }

        ResultSet result;
        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            if (answer)
            {
                String table = "";
                int colNum = result.getMetaData().getColumnCount();

                //put columns headers
                int i;
                for (i = 1; i < colNum - 1; i++) {
                    table += result.getMetaData().getColumnName(i) + ",";
                }
                if (i == colNum - 1)
                    table += result.getMetaData().getColumnName(i) + "\n";

                //get each row's data
                while (result.next()) {
                    for (i = 1; i < colNum - 1; i++) {
                        table += result + ",";
                    }
                    if (i == colNum - 1)
                        table += result.getMetaData().getColumnName(i) + "\n";
                }
            }
            else
            {

            }
        } catch (SQLException sql) {
            return errMsgFromException(sql);
        }

        */return "";
    }

    private String errMsgFromException(SQLException e)
    {
        String result;
        if (e.toString().contains("syntax"))
            result = "WRONG QUERY STRUCTURE";
        else
            result = "LOGICAL ERROR";

        result += "\n" + e.toString();
        return result;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = con.createStatement();
        return statement.executeQuery(query);
    }

    public int executeCommand(String query) throws SQLException
    {
        Statement statement = con.createStatement();
        return statement.executeUpdate(query);
    }
}
