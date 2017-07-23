import DataLogic.DBManager;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Tomer Gill on 08-Jul-17.
 */
public class Temp {

    public static void main(String[] args) {
        try {
            DBManager manager;
            System.out.println("hello\n".split("\n").length);
            manager = new DBManager("C:\\Users\\michal\\IdeaProjects\\DBPoject\\src\\conf.txt");


//            ResultSet set;
            List<String> list = manager.executeScript("C:\\Users\\michal\\IdeaProjects\\DBPoject\\src\\script.txt");
            for (String answer:
                 list) {
                if (answer.equals("\n") || answer.equals(""))
                {
                    System.out.println("bulbul");
                    continue;
                }
                System.out.printf(answer);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
