/*
 * Copyright (c) Tuenti Technologies. All rights reserved.
 * @author David Santiago Turiño <dsantiago@tuenti.com>
 */
package com.tuenti.videorecorderservice;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.tuenti.videorecorderservice.ioc.VideoRecorderModule;
import com.tuenti.videorecorderservice.util.ServerStarter;


public class Main extends GuiceServletContextListener {
	Logger logger = Logger.getLogger(getClass().getName());

	private static final String DEFAULT_PORT = "9998";
	private static final String DEFAULT_BINDING_ADDRESS = "0.0.0.0";
	private static final String DEFAULT_VIDEOS_FOLDER = System.getProperty("java.io.tmpdir");
	private static final String PORT_PROPERTY_NAME = "port";
	private static final String DESTINATION_FOLDER_PROPERTY_NAME = "destFolder";
	private static final String FILE_DISTRIBUTION_PROPERTY_NAME = "distributeFiles";


	private URI getBaseURI() {
		String port = System.getProperty(PORT_PROPERTY_NAME);
		if (port == null) {
			port = DEFAULT_PORT;
		}

		return UriBuilder.fromUri("http://" + DEFAULT_BINDING_ADDRESS + "/").port(Integer.parseInt(port)).build();
	}

	private HttpServer startServer() throws IOException {
		HttpServer httpServer = ServerStarter.start(getBaseURI(), getInjector());
		logger.info("Destination folder for recorded videos: " + getVideoFolderPath());

		return httpServer;
	}

	public static void main(String[] args) throws IOException {
		HttpServer httpServer = new Main().startServer();
		System.in.read();
		httpServer.stop();
	}

	String getVideoFolderPath() {
		String videoFolderPath = System.getProperty(DESTINATION_FOLDER_PROPERTY_NAME);
		if (videoFolderPath == null) {
			videoFolderPath = DEFAULT_VIDEOS_FOLDER;
		}

		return videoFolderPath;
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new VideoRecorderModule(getVideoFolderPath(), areFilesDistributed()));
	}

	private boolean areFilesDistributed() {
		return Boolean.parseBoolean(System.getProperty(FILE_DISTRIBUTION_PROPERTY_NAME, "false"));
	}

}
