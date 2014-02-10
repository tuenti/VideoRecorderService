/*
 * Copyright (c) Tuenti Technologies. All rights reserved.
 * @author David Santiago Turiño <dsantiago@tuenti.com>
 */
package com.tuenti.videorecorderservice;

import java.io.IOException;

public interface TuentiScreenRecorder {

	public void start() throws IOException;
	public void stop() throws IOException;
	public void saveAs(String fileName) throws IOException;

}
