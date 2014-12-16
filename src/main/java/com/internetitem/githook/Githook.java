package com.internetitem.githook;

import com.internetitem.githook.dataModel.GitHubPush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Service
@Path("/hook")
public class Githook {

	private static Logger logger = LoggerFactory.getLogger(Githook.class);

	@GET
	@Produces({"application/json", "text/html"})
	public TestObject hello() {
		return new TestObject("adam", "1");
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response onHook(GitHubPush push, @HeaderParam("x-github-event") String eventName) {
		if (eventName == null) {
			return badRequest("Missing header x-github-event");
		}
		if (!eventName.equalsIgnoreCase("push")) {
			return badRequest("Invalid event name [" + eventName + "]");
		}
		if (push == null) {
			return badRequest("No request entity");
		}

		String before = push.getBefore();
		String after = push.getAfter();
		logger.info("Got push for [" + before + "] to [" + after + "]");
		return buildResponse("Got it!", Response.Status.ACCEPTED);
	}

	private Response badRequest(String message) {
		logger.warn(message);
		return buildResponse(message, Response.Status.BAD_REQUEST);
	}

	private Response buildResponse(String message, Response.Status status) {
		boolean success = (status.getFamily() == Response.Status.Family.SUCCESSFUL);
		return Response.status(status).entity(new RequestStatusEntity(message, success)).build();
	}
}
