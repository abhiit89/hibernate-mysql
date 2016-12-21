package com.mkyong.common;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@Path("api")
public class Resource {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_NAME = "cis";
    static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    static final String TABLE_NAME = "stock";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "mysql";

    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readData() {
        JSONArray data = null;
        //ArrayList<Object> data = new ArrayList<Object>();
        Gson gson = new Gson();
//        Session session1 = HibernateUtil.getSessionFactory().openSession();
//        session1.beginTransaction();
//        List list = session1.createQuery(" from Stock").list();
//        Iterator itr = list.iterator();
//        while (itr.hasNext()) {
//            Object user = itr.next();
//            data.add(user);
//        }
//        session1.getTransaction().commit();
//        session1.close();

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM " + TABLE_NAME;
            ResultSet rs = stmt.executeQuery(sql);
            data = convertToJSON(rs);
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

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
}
