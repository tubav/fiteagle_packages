package org.fiteagle.delivery.rest.fiteagle;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * this is just a test class for jersey integration in tomcat/liferay
 * @author ozanoo
 *
 */
@Path("/hello/")
public class Hello {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		
		System.out.println("sayHello method is succesfully called!!!");
		
		return "Hello Jersey";
	}
}     
