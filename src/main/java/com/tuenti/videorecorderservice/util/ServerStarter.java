/*
 * Copyright (c) Tuenti Technologies. All rights reserved.
 * @author David Santiago Turiño <dsantiago@tuenti.com>
 */
package com.tuenti.videorecorderservice.util;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;

import com.google.inject.Injector;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;

public class ServerStarter {

	public static HttpServer start(URI baseURI, Injector injector) throws IOException {
		ResourceConfig rc = new PackagesResourceConfig("com.tuenti.videorecorderservice");
		IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(rc, injector);

		return GrizzlyServerFactory.createHttpServer(baseURI, rc, ioc);
	}
}
