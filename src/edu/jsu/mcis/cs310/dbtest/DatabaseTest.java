package edu.jsu.mcis.cs310.dbtest;

import java.sql.*;
import org.json.simple.*;

public class DatabaseTest {

    public JSONArray getJSONData() {
         
        JSONArray records = new JSONArray();
        
      
        
        // insert your code here
        Connection conn;
        ResultSetMetaData metadata;
        ResultSet resultset = null;        
        PreparedStatement pstSelect = null, pstUpdate = null;
        
         
        String query, key; 
         
        
        boolean hasresults;  
        int resultCount, columnCount, updateCount;
        
        try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "CS488";
            System.out.println("Connecting to " + server + "...");
            
            /* Open Connection (MySQL JDBC driver must be on the classpath!) */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (conn.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
                   
         
                /* Prepare Select Query */
                
                query = "SELECT * FROM p2_test.people";
                pstSelect = conn.prepareStatement(query);
                
                /* Execute Select Query */
                
                System.out.println("Submitting Query ...");
                
                hasresults = pstSelect.execute();                
                
                /* Get Results */
                
                System.out.println("Getting Results ...");
                while( hasresults || pstSelect.getUpdateCount() != -1){
                if ( hasresults ) {

                    /* Get ResultSet Metadata */

                    resultset = pstSelect.getResultSet();
                    metadata = resultset.getMetaData();
                    columnCount = metadata.getColumnCount();

                    /* Get Column Names; Print as Table Header */
                    //JSONObject json = new JSONObject();
                    //for (int i = 1; i <= columnCount; i++) {

                      //  key = metadata.getColumnLabel(i);

                        //System.out.format("%20s", key);
                    //}
                    //record.add(json);

                    /* Get Data; Print as Table Rows */

                    while(resultset.next()) {
                       
                        JSONObject json = new JSONObject();
                       
                        //Loop the resultset. 
                        for(int i = 2; i <= columnCount; i++){
                            key = metadata.getColumnLabel(i);
                            json.put(key, resultset.getString(i));
                                   
                        }
                        records.add(json);
                          
                    }

                 }
                 else {

                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }
                }    
                
                  hasresults = pstSelect.getMoreResults();
                }
                
            }
            
            System.out.println();
            
            /* Close Database Connection */
            
            conn.close();
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultset != null) { try { resultset.close(); } catch (Exception e) { e.printStackTrace(); } }
            
            if (pstSelect != null) { try { pstSelect.close(); } catch (Exception e) { e.printStackTrace(); } }
            
            if (pstUpdate != null) { try { pstUpdate.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return records;
        
 }
 //public String toString(){
   // json.append("\"").append(this.json).append(" "); 
 //}

  
    public static void main(String[] args) {
        
        DatabaseTest dbtest = new DatabaseTest();
        JSONArray results =  dbtest.getJSONData();
        //JSONArray results = getJSONData();
        
        System.out.println( JSONValue.toJSONString(results) );
        //System.out.println(results);
        
    
    }
}