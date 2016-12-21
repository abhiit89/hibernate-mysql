package com.mkyong.common;

import com.google.gson.Gson;
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

    public static void main(String[] ars) throws Exception {
        new Resource().readData();
        System.out.println("read the data");
    }

    static final String TABLE_NAME = "STOCK";
    private static Connection conn = null;
    private static Statement stmt = null;

    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readData() throws Exception {

        createConnection();
        try {
            dropTable();
        } catch (Exception ex) {
            System.out.println("Drop table exception ignored");
        }
        createTable();

        insertStocks("1", "4715", "KEANU");
        insertStocks("2", "1234", "NEO");
        insertStocks("3", "234", "BLADE");
        insertStocks("6", "3456", "MATRIX");
        JSONArray data = new JSONArray();
        stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("select * from " + TABLE_NAME);
        data = convertToJSON(results);
        Gson gson = new Gson();
        JSONObject json = new JSONObject();
        return Response.ok()
                .entity(data.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*")
                .allow("OPTIONS").build();
    }

    public static JSONArray convertToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            JSONObject obj = new JSONObject();
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    private static void createConnection() {
        try {
            Class.forName(EmbeddedDriver.class.getName()).newInstance();
            //Get a connection
            conn = DriverManager.getConnection("jdbc:derby:.\\cis.db;create=true");
            stmt = conn.createStatement();
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    private static void insertStocks(String STOCK_ID, String STOCK_CODE, String STOCK_NAME) {
        try {
            stmt.execute("insert into " + TABLE_NAME + " values ('" +
                    STOCK_ID + "','" + STOCK_CODE + "','" + STOCK_NAME + "')");
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }

    private static void createTable() throws SQLException {
        stmt.execute("create table STOCK (STOCK_ID varchar(10), STOCK_CODE varchar(10), STOCK_NAME varchar(10))");
    }

    private static void dropTable() throws SQLException {
        stmt.execute("drop table STOCK");
    }



}