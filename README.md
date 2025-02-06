*You are currently looking at the development branch of vlcj-player for vlcj-5.0.0, if you want a stable version of
vlcj-player that works with vlcj-4 you should switch to the
[vlcj-4.x branch](https://github.com/caprica/vlcj-player/tree/vlcj-4.x).*

vlcj-player
===========

The vlcj-player is a media player application built using vlcj with a Swing
rich-client user interface. 

The main goal of the project is to provide an extensive demo application 
showing how to build media players with vlcj, and to include as many features
of vlcj as possible.

Generally the vlcj-player tries to match the Qt interface of VLC with as many
of the same features implemented as possible.

However, it is not possible to get a 100% like-for-like implementation since
LibVLC, used by vlcj, exposes only a sub-set of the total functionality of VLC. 

Screenshot
----------

![vlcj-player](https://github.com/caprica/vlcj-player/raw/master/doc/vlcj-player.png "vlcj-player")

Features
--------

 - audio player
 - video player
 - full-screen
 - audio equalizer
 - video adjustments
 - title selection
 - chapter navigation
 - audio track selection
 - video track selection
 - subtitle track selection
 - load external subtitle file
 - change audio device
 - change audio stereo mode
 - change playback speed
 - capture and display native logs
 - capture and display video surface debug messages (e.g. to test mouse and keyboard events still work)
 - volume controls
 - mute
 - zoom/scale
 - aspect ratio
 - crop
 - logo/marquee
 - always on top
 - video snapshots
 - drag and drop local files to the main window
 - drag and drop URLs from web browsers to the main window (e.g. to play a YouTube video)
 - redirect native output streams (on Linux)

...and a whole bunch of other nifty stuff.

Embedded vs Callback Rendering
------------------------------

This demo application can render with an embedded native media player, this requires heavyweight AWT support which is
not available on macOS anymore.

Alternatively, "callback" rendering can be enabled by tweaking the `videoOutput` flag in the `Application.java` class.

Status
------

This project is perpetually a work-in-progress. It is a demo application showing a lot of vlcj features.

Building & Running
------------------

If you execute "mvn install" or "mvn package", you will get a distribution package that you can unpack. This will give
you the vlcj-player application jar and all of the dependencies - you can then simply execute
java -jar vlcj-player-2.0.0-SNAPSHOT.jar` and the application should start.

You can also launch from the command-line using maven:

```shell
mvn clean compile exec:java
```

License
-------

The vlcj-player project is provided under the GPL, version 3 or later.
