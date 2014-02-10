/*
 * Copyright (c) Tuenti Technologies. All rights reserved.
 * @author David Santiago Turiño <dsantiago@tuenti.com>
 */
package com.tuenti.videorecorderservice.ioc;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.VideoFormatKeys;
import org.monte.media.math.Rational;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.*;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletModule;
import com.tuenti.videorecorderservice.CustomScreenRecorder;
import com.tuenti.videorecorderservice.TuentiScreenRecorder;
import com.tuenti.videorecorderservice.log.Log4JTypeListener;

public class VideoRecorderModule extends ServletModule {

	private static final int MAX_RECORDING_TIME_SECS = 120000;
	private static final int FRAME_RATE_PER_SEC = 10;
	private static final int BIT_DEPTH = 16;
	private static final float QUALITY_RATIO = 0.97f;

	private String videoFolderPath;
	private boolean areFilesDistributed;

	public VideoRecorderModule(String videoFolderPath, boolean areFilesDistributed) {
		this.videoFolderPath = videoFolderPath;
		this.areFilesDistributed = areFilesDistributed;
	}

	@Override
	protected void configureServlets() {
		bindListener(Matchers.any(), new Log4JTypeListener());
	}

	@Provides
	@Singleton
	public TuentiScreenRecorder provideScreenRecorder() {
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration();

		File movieFolder = new File(videoFolderPath);
		String mimeType = FormatKeys.MIME_QUICKTIME;
		String videoFormatName = VideoFormatKeys.ENCODING_QUICKTIME_ANIMATION;
		String compressorName = VideoFormatKeys.COMPRESSOR_NAME_QUICKTIME_ANIMATION;
		Dimension outputDimension = gc.getBounds().getSize();
		int bitDepth = BIT_DEPTH;
		float quality = QUALITY_RATIO;
		int screenRate = FRAME_RATE_PER_SEC;
		long maxRecordingTime = MAX_RECORDING_TIME_SECS;

		CustomScreenRecorder sr;
		try {
			sr = new CustomScreenRecorder(gc, gc.getBounds(),
					getFileFormat(mimeType),
					getOutputFormatForScreenCapture(videoFormatName, compressorName, outputDimension,
							bitDepth, quality, screenRate),
					getMouseFormat(),
					getAudioFormat(),
					movieFolder);
			sr.setMaxRecordingTime(maxRecordingTime);
			sr.filesShouldBeDistributed(areFilesDistributed);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}

		return sr;
	}

	private Format getMouseFormat() {
		return null;
	}

	private Format getAudioFormat() {
		return null;
	}

	private Format getOutputFormatForScreenCapture(String videoFormatName, String compressorName,
			Dimension outputDimension, int bitDepth, float quality, int screenRate) {
		return new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, videoFormatName,
				CompressorNameKey, compressorName,
				WidthKey, outputDimension.width,
				HeightKey, outputDimension.height,
				DepthKey, bitDepth, FrameRateKey, Rational.valueOf(screenRate),
				QualityKey, quality,
				KeyFrameIntervalKey, screenRate * 60 // one keyframe per minute
		);
	}

	private Format getFileFormat(String mimeType) {
		return new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, mimeType);
	}

}
