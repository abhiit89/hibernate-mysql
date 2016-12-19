package com.mkyong.common;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ResourceConfig config = new ResourceConfig().register(JacksonFeature.class);
        config.packages("com.mkyong.common");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");
        try {
            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
