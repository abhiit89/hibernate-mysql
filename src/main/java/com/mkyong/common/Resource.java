package com.mkyong.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mkyong.persistence.HibernateUtil;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.List;

/**
 * Created by abhinavnathgupta on 19/12/16.
 */
@Path("api")
public class Resource {

    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readData() throws JSONException, JsonProcessingException {
        StringBuilder apiReadString = new StringBuilder();
        JSONObject apiReadObject = new JSONObject();
        Session session1 = HibernateUtil.getSessionFactory().openSession();
        session1.beginTransaction();
        List list = session1.createQuery(" from Stock").list();
        Iterator itr = list.iterator();
        while(itr.hasNext())
        {
            Stock user = (Stock)itr.next();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(user);
            apiReadString.append(json);
            apiReadString.append(",");
        }
        final int length = apiReadString.length();
        if ( length > 0 ) {
            // We remove the last character.
            apiReadString.deleteCharAt( length - 1 );
        }
        session1.getTransaction().commit();
        session1.close();
        apiReadObject.append("data", apiReadString.toString());
        return Response.ok() //200
                .entity(apiReadObject.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*")
                .allow("OPTIONS").build();
    }
}
