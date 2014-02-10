/*
 * Copyright (c) Tuenti Technologies. All rights reserved.
 * @author David Santiago Turiño <dsantiago@tuenti.com>
 */
package com.tuenti.videorecorderservice;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.screenrecorder.ScreenRecorder;

public class CustomScreenRecorder extends ScreenRecorder implements TuentiScreenRecorder {
	private static final String TEMP_FILENAME_WITHOUT_EXTENSION = "currentRecording";
	private String currentTempExtension;
	private boolean areFilesDistributed = false;

	public CustomScreenRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
			Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder)
			throws IOException, AWTException {
		super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
	}

	@Override
	protected File createMovieFile(Format fileFormat) throws IOException {
		this.currentTempExtension = Registry.getInstance().getExtension(fileFormat);
		String tempFile = getTempFileName();

		File fileToWriteMovie = new File(tempFile);
		if (fileToWriteMovie.exists()){
			fileToWriteMovie.delete();
		}

		return fileToWriteMovie;
	}

	private String getTempFileName() {
		return System.getProperty("java.io.tmpdir") + File.separator +
					TEMP_FILENAME_WITHOUT_EXTENSION + "." + this.currentTempExtension;
	}

	public void saveAs(String filename) throws IOException {
		this.stop();

		File tempFile = this.getCreatedMovieFiles().get(0);

		File destFile = getDestinationFile(filename);
		tempFile.renameTo(destFile);
	}

	private File getDestinationFile(String filename) {
		String destFolderSuffix = "";

		if (areFilesDistributed) {
			destFolderSuffix = File.separator + filename.charAt(filename.length() - 2) + filename.charAt(filename
					.length() - 1);
		}

		File file = new File(this.movieFolder + destFolderSuffix + File.separator +
				filename + "." + this.currentTempExtension);

		return file;
	}

	public void filesShouldBeDistributed(boolean areFilesDistributed) {
		this.areFilesDistributed = areFilesDistributed;
	}

}
