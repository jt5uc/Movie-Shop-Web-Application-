
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class export {
    public static void main(String[] args) {
        String filename ="Desktop:test.csv";
        try {
            FileWriter fw = new FileWriter(filename);
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb", "root", "asdrtyjkl4110");
            String query = "select * from movies";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int cols = rs.getMetaData().getColumnCount();

            for(int i = 1; i <= cols; i ++){
               fw.append(rs.getMetaData().getColumnLabel(i));
               if(i < cols) fw.append(',');
               else fw.append('\n');
            }

            while (rs.next()) {

               for(int i = 1; i <= cols; i ++){
                   fw.append(rs.getString(i));
                   if(i < cols) fw.append(',');
               }
               fw.append('\n');
           }
            fw.flush();
            fw.close();
            conn.close();
            System.out.println("CSV File is created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}