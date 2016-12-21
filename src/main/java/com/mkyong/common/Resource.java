package com.mkyong.common;

import com.google.gson.Gson;
import com.mkyong.persistence.HibernateUtil;
import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("api")
public class Resource {

    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readData() {
        ArrayList<Object> data = new ArrayList<Object>();
        Session session1 = HibernateUtil.getSessionFactory().openSession();
        session1.beginTransaction();
        List list = session1.createQuery(" from Stock").list();
        Iterator itr = list.iterator();
        while (itr.hasNext()) {
            Object user = itr.next();
            data.add(user);
        }
        session1.getTransaction().commit();
        session1.close();
        Gson gson = new Gson();
        return Response.ok()
                .entity(gson.toJson(data))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*")
                .allow("OPTIONS").build();
    }
}
