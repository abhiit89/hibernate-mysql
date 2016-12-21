package com.mkyong.common;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;

@Path("api")
public class Resource {

    public static void main(String[] ars) {
        new Resource().readData();
        System.out.println("read the data");
    }

    static final String DB_NAME = "cis";
    static final String USER = ";create=true;user=me;password=mine";
    static final String DB_URL = "jdbc:derby:.\\test;create=true";
    static final String TABLE_NAME = "STOCK";
    private static Connection conn = null;
    private static Statement stmt = null;
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readData() {

        createConnection();
        insertStocks("1", "4715", "KEANU");
        insertStocks("2", "1234", "NEO");
        insertStocks("3", "234", "BLADE");
        insertStocks("6", "3456", "MATRIX");
        fetchStocks();
        shutdown();
        JSONArray data = new JSONArray();
        return Response.ok()
                .entity(data)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*")
                .allow("OPTIONS").build();
    }

    public static JSONArray convertToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                JSONObject obj = new JSONObject();
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
                jsonArray.put(obj);
                System.out.println(obj.toString());
            }
        }
        return jsonArray;
    }

    private static void createConnection()
    {
        try
        {
            Class.forName(EmbeddedDriver.class.getName()).newInstance();
            //Get a connection
            conn = DriverManager.getConnection("jdbc:derby:.\\BD\\nombrebasededatos.db;create=true");
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }

    private static void insertStocks(String STOCK_ID, String STOCK_CODE, String STOCK_NAME)
    {
        try
        {
            stmt = conn.createStatement();
            stmt.execute("drop table STOCK");
            stmt.execute("create table STOCK (ID varchar(10), Code varchar(10), Name varchar(10))");
            stmt.execute("insert into " + TABLE_NAME + " values (" +
                    STOCK_ID + ",'" + STOCK_CODE + "','" + STOCK_NAME +"')");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }

    private static void fetchStocks()
    {
        try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + TABLE_NAME);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                int id = results.getInt(1);
                String restName = results.getString(2);
                String cityName = results.getString(3);
                System.out.println(id + "\t\t" + restName + "\t\t" + cityName);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }

    private static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(DB_URL + ";shutdown=true");
                conn.close();
            }
        }
        catch (SQLException sqlExcept)
        {

        }

    }
}
