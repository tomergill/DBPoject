import DataLogic.DBManager;

import java.sql.ResultSet;

/**
 * Created by Tomer Gill on 08-Jul-17.
 */
public class Temp {

    public static void main(String[] args) {
        try {
            DBManager manager;

            manager = new DBManager("C:\\Users\\Tomer " +
                    "Gill\\Documents\\University\\Databases\\DBPoject\\src/conf.txt");


//            ResultSet set;
            System.out.println(manager.DDLQuery("INSERT INTO `mylt_db`.`students` " +
                    "(`firstname`, `lastname`, `age`) " +
                    "VALUES ('tomer', 'gill', '20');"));


//            System.out.println("*********************");
//            System.out.println(set);
//            System.out.println("*********************\n");
//
//            int cols = set.getMetaData().getColumnCount();
//
//            while (set.next()) {
//                for (int i = 1; i < cols; i++) {
//                    System.out.printf(set.getString(i) + ", ");
//                }
//                System.out.printf(set.getString(cols) + "\n");
//            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
