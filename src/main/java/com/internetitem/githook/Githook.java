package com.internetitem.githook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.*;
import java.util.Map;

@Service
@Path("/hook")
public class Githook {

	private static Logger logger = LoggerFactory.getLogger(Githook.class);

	@GET
	@Produces("application/json")
	public TestObject hello() {
		return new TestObject("adam", "1");
	}

	@POST
	@Consumes("application/json")
	public void onHook(Map<String, Object> obj) {
		logger.info("hooked: " + obj);
	}
}
