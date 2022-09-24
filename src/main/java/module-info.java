module uk.co.caprica.vlcj.player {
    requires com.google.common;
    requires com.miglayout.swing;
    requires glazedlists;
    requires java.desktop;
    requires java.prefs;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.nativestreams;

    exports uk.co.caprica.vlcjplayer.view to com.google.common;
    exports uk.co.caprica.vlcjplayer.view.debug to com.google.common;
    exports uk.co.caprica.vlcjplayer.view.effects to com.google.common;
    exports uk.co.caprica.vlcjplayer.view.main to com.google.common;
    exports uk.co.caprica.vlcjplayer.view.messages to com.google.common;

    opens uk.co.caprica.vlcjplayer.view.main to com.google.common;
}
