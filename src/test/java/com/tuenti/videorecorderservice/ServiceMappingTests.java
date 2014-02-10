/*
 * Copyright (c) Tuenti Technologies. All rights reserved.
 * @author David Santiago Turiño <dsantiago@tuenti.com>
 */
package com.tuenti.videorecorderservice;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.tuenti.videorecorderservice.ioc.VideoRecorderModule;
import com.tuenti.videorecorderservice.util.ServerStarter;


public class ServiceMappingTests {

	private HttpServer server;
	private static final boolean irrelevantFilesDistribution = true;
	private WebResource service;

	private URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/").port(9999).build();
	}

	@Before
	public void startServer() throws IOException {
		Injector injector = Guice.createInjector(new TestModule());

		server = ServerStarter.start(getBaseURI(), injector);
		service = newServiceInstance();
	}

	private class TestModule extends VideoRecorderModule {
		public TestModule() {
			super("unusedVideoFolderPath", irrelevantFilesDistribution);
		}

		@Override
		public TuentiScreenRecorder provideScreenRecorder() {
			return new DoesNothingTuentiScreenRecorder();
		}
	}

	@Test
	public void startRecordingActionIsCorrectlyMapped() throws IOException {
		ClientResponse resp = service.path("rec").path("start")
				.get(ClientResponse.class);

		assertEquals(HttpURLConnection.HTTP_OK, resp.getStatus());
	}

	private WebResource newServiceInstance() {
		Client client = Client.create(new DefaultClientConfig());
		return client.resource(getBaseURI());
	}

	@Test
	public void stopRecordingActionIsCorrectlyMapped() throws IOException {
		ClientResponse resp = service.path("rec").path("stop")
				.get(ClientResponse.class);

		assertEquals(HttpURLConnection.HTTP_OK, resp.getStatus());
	}

	@Test
	public void saveFileActionIsCorrectlyMapped() throws IOException {
		ClientResponse resp = service.path("rec").path("save").path("anyFileName")
				.get(ClientResponse.class);

		assertEquals(HttpURLConnection.HTTP_OK, resp.getStatus());
	}

	@After
	public void stopServer() {
		server.stop();
	}

	private static class DoesNothingTuentiScreenRecorder implements TuentiScreenRecorder {

		@Override
		public void start() throws IOException {
		}

		@Override
		public void stop() throws IOException {
		}

		@Override
		public void saveAs(String filename) throws IOException {
		}
	}

}
