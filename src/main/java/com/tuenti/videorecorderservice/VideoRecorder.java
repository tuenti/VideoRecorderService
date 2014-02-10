/*
 * Copyright (c) Tuenti Technologies. All rights reserved.
 * @author David Santiago Turiño <dsantiago@tuenti.com>
 */
package com.tuenti.videorecorderservice;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.nnsoft.guice.sli4j.core.InjectLogger;


@Path("/rec")
public class VideoRecorder {
	@InjectLogger
	Logger logger;

	private final TuentiScreenRecorder tuentiScreenRecorder;

	@Inject
	public VideoRecorder(TuentiScreenRecorder tuentiScreenRecorder) {
		this.tuentiScreenRecorder = tuentiScreenRecorder;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	@SuppressWarnings("UnusedReturnValue")
	@GET
	@Path("/start")
	public Response start() {
		int responseCode = HttpURLConnection.HTTP_OK;
		logger.info("Video recording start request received");
		try {
			tuentiScreenRecorder.start();
			logger.info("Video recording started");
		} catch (IOException e) {
			responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
		}

		return Response.status(responseCode).build();
	}

	@SuppressWarnings("UnusedReturnValue")
	@GET
	@Path("/stop")
	public Response stop() {
		logger.info("Video recording stop request received");
		int responseCode = HttpURLConnection.HTTP_OK;
		try {
			tuentiScreenRecorder.stop();
			logger.info("Video recording stopped");
		} catch (IOException e) {
			responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
		}

		return Response.status(responseCode).build();
	}

	@SuppressWarnings("UnusedReturnValue")
	@GET
	@Path("/save/{filename}")
	public Response save(@PathParam("filename") String filename) {
		int responseCode = HttpURLConnection.HTTP_OK;
		logger.info("Request to save current video as " + filename + " received");
		try {
			tuentiScreenRecorder.saveAs(filename);
			logger.info("Video saved!");
		} catch (IOException e) {
			responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
		}

		return Response.status(responseCode).build();
	}

}
