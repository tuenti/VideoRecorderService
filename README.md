
VideoRecorderService
====================

VideoRecorderService is a lightweight web-based service that offers a REST interface to easily manage the
recording of screen videos.

Its main motivation was the need for recording videos of the execution of our webdriver browser
tests suite at Tuenti, in order to reduce the time devoted to troubleshoot them. More info on the setup we use was
provided at http://corporate.tuenti.com/en/dev/blog/video-recordings-of-browser-tests. Having videos of the execution
 of such tests allowed us to fix them or the test framework itself by providing debug information that we'd never
 have obtained from stack traces and screenshots alone.

The service relies on the screen recorder included in the great Monte Media Library developed by Werner Randelshofer
(http://http://www.randelshofer.ch/monte/).


Build
-----

Build is done using maven.

First of all you'll need to install the Monte Media Library jar artifact in your repository as it's not
currently available at
Maven Central. Download http://www.randelshofer.ch/monte/files/MonteMedia-0.7.7src-cc.zip and uncompress it.

Then install the library by running:

    $ mvn install:install-file -Dfile=/the/path/to/monte-cc.jar -DgroupId=org.monte -DartifactId=screen-recorder
    -Dversion=0.7.7 -Dpackaging=jar

Now you are ready to build the video service by running:

    $ mvn package

That generates the jar file in the `target` directory.


Run
---

The generated Java .jar file is self-executable. It will start a web container where the service will be waiting for
requests
by just running:

    $ java -jar /path/to/generated/VideoRecorderService-current_version.jar

It will listen on port 9998 by default and will be stopped by sending ENTER or Ctrl-C.


Usage
-----

You can request a new video recording by sending a GET request to /rec/start. This will start recording the screen of
 the host where the service is running.

Whenever you want to stop the ongoing recording and save the video to a file just send a GET request to /rec/save/xxx
where
xxx is the name you want for the file.

If you need to stop an ongoing recording in order to discard it, send a GET request to /rec/stop.


FAQ
---
- Whenever I ask for a video to be saved with a given file name, where is that file stored?
By default it will be placed at the temp folder (which varies depending on the OS), of the host where the service is
running. This can be fine for testing out the service but most of the time you will want to provide a different
folder to safely store recorded videos (see next question).

- Can I change the folder where videos are stored?
Yes. You can set a different destination folder with -DdestFolder=/path/to/custom/folder when launching the service.

- But if I start storing many videos in a given folder I may have problems at an OS level accessing them. What about
distributing them somehow in the filesystem?
- Yes, you can. You can enable a "distribution" mode for the storage of recorded videos that will use the last two
characters of the filename requested to save video to place it in a subfolder. By default,
if you want to save a video with the name, say, videofile20987 you will end up with a the file stored at:

    /path/to/dest/folder/videofile20987.mov

If you had enabled the distribution mode with -DdistributeFiles=true the video would be stored at:

    /path/to/dest/folder/87/videofile20987.mov

which will scale better. Keep in mind that you will have to create the subfolders beforehand or the service will fail
 to write the files.

- Can I change the port where the service is listening?
Yes. You can set a different port with -Dport=xxxx when launching the service.

- Any limitation on the maximum recording time?
Yes. Currently it's limited to a maximum of 2 minutes.

- What format is used for the videos?
Videos are encoded using the Apple QuickTime RLE codec.
