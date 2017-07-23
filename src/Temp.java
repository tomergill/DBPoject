import DataLogic.DBManager;

import java.sql.ResultSet;

/**
 * Created by Tomer Gill on 08-Jul-17.
 */
public class Temp {

    public static void main(String[] args) {
        try {
            DBManager manager;

            manager = new DBManager("C:\\Users\\michal\\IdeaProjects\\DBPoject\\src\\conf.txt");


//            ResultSet set;
            System.out.println(manager.executeScript("C:\\Users\\michal\\IdeaProjects\\DBPoject\\src\\script.txt"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
