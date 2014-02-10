/*
 * Copyright (c) Tuenti Technologies. All rights reserved.
 * @author David Santiago Turiño <dsantiago@tuenti.com>
 */
package com.tuenti.videorecorderservice;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class VideoRecorderTests {

	@Mock
	TuentiScreenRecorder tuentiScreenRecorder;
	private VideoRecorder videoRecorder;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		videoRecorder = newVideoRecorderInstance(tuentiScreenRecorder);
	}

	@Test
	public void shouldStartRecordingWhenAskedToDoSo() throws Exception {
		videoRecorder.start();

		verify(tuentiScreenRecorder).start();
	}

	@Test
	public void shouldStopRecordingWhenAskedToDoSo() throws Exception {
		videoRecorder.stop();

		verify(tuentiScreenRecorder).stop();
	}

	@Test
	public void shouldSaveVideoRecordedWhenAskedToDoSo() throws Exception {
		String requestedVideoFileName = "whateverTheFileName";
		videoRecorder.save(requestedVideoFileName);

		verify(tuentiScreenRecorder).saveAs(requestedVideoFileName);
	}

	private VideoRecorder newVideoRecorderInstance(TuentiScreenRecorder tuentiScreenRecorder) {
		VideoRecorder videoRecorder = new VideoRecorder(tuentiScreenRecorder);
		videoRecorder.setLogger(Logger.getAnonymousLogger());

		return videoRecorder;
	}

}
