/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2015 Caprica Software Limited.
 */

package uk.co.caprica.vlcjplayer;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.nativestreams.NativeStreams;
import uk.co.caprica.vlcj.binding.LibC;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent;
import uk.co.caprica.vlcj.player.renderer.RendererDiscoverer;
import uk.co.caprica.vlcj.player.renderer.RendererDiscovererDescription;
import uk.co.caprica.vlcj.player.renderer.RendererDiscovererEventListener;
import uk.co.caprica.vlcj.player.renderer.RendererItem;
import uk.co.caprica.vlcj.support.Info;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.exclusivemode.ExclusiveModeFullScreenStrategy;
import uk.co.caprica.vlcjplayer.event.AfterExitFullScreenEvent;
import uk.co.caprica.vlcjplayer.event.BeforeEnterFullScreenEvent;
import uk.co.caprica.vlcjplayer.event.RendererAddedEvent;
import uk.co.caprica.vlcjplayer.event.RendererDeletedEvent;
import uk.co.caprica.vlcjplayer.event.ShutdownEvent;
import uk.co.caprica.vlcjplayer.view.debug.DebugFrame;
import uk.co.caprica.vlcjplayer.view.effects.EffectsFrame;
import uk.co.caprica.vlcjplayer.view.main.MainFrame;
import uk.co.caprica.vlcjplayer.view.messages.NativeLogFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcjplayer.Application.application;

/**
 * Application entry-point.
 */
public class VlcjPlayer implements RendererDiscovererEventListener {

    static {
//        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/disks/data/build/install/libs-3");
//        LibC.INSTANCE.setenv("VLC_PLUGIN_PATH", "/disks/data/build/install/libs-3/plugins", 1);
    }

    private static VlcjPlayer app;

    private static final NativeStreams nativeStreams;

    // Redirect the native output streams to files, useful since VLC can generate a lot of noisy native logs we don't care about
    // (on the other hand, if we don't look at the logs we might won't see errors)
    static {
//        if (RuntimeUtil.isNix()) {
//            nativeStreams = new NativeStreams("stdout.log", "stderr.log");
//        }
//        else {
            nativeStreams = null;
//        }
    }

    private final List<RendererDiscoverer> rendererDiscoverers = new ArrayList<>();

    private final JFrame mainFrame;

    @SuppressWarnings("unused")
    private final JFrame messagesFrame;

    @SuppressWarnings("unused")
    private final JFrame effectsFrame;

    @SuppressWarnings("unused")
    private final JFrame debugFrame;

    private final NativeLog nativeLog;

    public static void main(String[] args) throws InterruptedException {
        Info info = Info.getInstance();

        System.out.printf("vlcj             : %s%n", info.vlcjVersion() != null ? info.vlcjVersion() : "<version not available>");
        System.out.printf("os               : %s%n", val(info.os()));
        System.out.printf("java             : %s%n", val(info.javaVersion()));
        System.out.printf("java.home        : %s%n", val(info.javaHome()));
        System.out.printf("jna.library.path : %s%n", val(info.jnaLibraryPath()));
        System.out.printf("java.library.path: %s%n", val(info.javaLibraryPath()));
        System.out.printf("PATH             : %s%n", val(info.path()));
        System.out.printf("VLC_PLUGIN_PATH  : %s%n", val(info.pluginPath()));

        if (RuntimeUtil.isNix()) {
            System.out.printf("LD_LIBRARY_PATH  : %s%n", val(info.ldLibraryPath()));
        } else if (RuntimeUtil.isMac()) {
            System.out.printf("DYLD_LIBRARY_PATH          : %s%n", val(info.dyldLibraryPath()));
            System.out.printf("DYLD_FALLBACK_LIBRARY_PATH : %s%n", val(info.dyldFallbackLibraryPath()));
        }

        setLookAndFeel();

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
                app = new VlcjPlayer();
                app.start();
//            }
//        });
    }

    private static String val(String val) {
        return val != null ? val : "<not set>";
    }

    private static void setLookAndFeel() {
        String lookAndFeelClassName;
        if (RuntimeUtil.isNix()) {
            lookAndFeelClassName = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        }
        else {
            lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        }
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        }
        catch(Exception e) {
            // Silently fail, it doesn't matter
        }
    }

    public VlcjPlayer() {
        EmbeddedMediaPlayerComponent mediaPlayerComponent = application().mediaPlayerComponent();
        CallbackMediaPlayerComponent callbackMediaPlayerComponent = application().callbackMediaPlayerComponent();

        prepareDiscoverers();

        mainFrame = new MainFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Stop audio as soon as possible
                application().mediaPlayer().controls().stop();

                // Avoid the visible delay disposing everything
                mainFrame.setVisible(false);

                for (RendererDiscoverer discoverer : rendererDiscoverers) {
                    discoverer.stop();
                }

                mediaPlayerComponent.release();
                callbackMediaPlayerComponent.release();

                if (nativeStreams != null) {
                    nativeStreams.release();
                }

                application().post(ShutdownEvent.INSTANCE);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }
        });
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        application().mediaPlayerComponent().mediaPlayer().fullScreen().strategy(new VlcjPlayerFullScreenStrategy(mainFrame));
        application().callbackMediaPlayerComponent().mediaPlayer().fullScreen().strategy(new VlcjPlayerFullScreenStrategy(mainFrame));

        nativeLog = mediaPlayerComponent.mediaPlayerFactory().application().newLog();

        messagesFrame = new NativeLogFrame(nativeLog);
        effectsFrame = new EffectsFrame();
        debugFrame = new DebugFrame();
    }

    private void start() {
        mainFrame.setVisible(true);

        for (RendererDiscoverer discoverer : rendererDiscoverers) {
            discoverer.start();
        }
    }

    private void prepareDiscoverers() {
        EmbeddedMediaPlayerComponent mediaPlayerComponent = application().mediaPlayerComponent();
        for (RendererDiscovererDescription descriptions : mediaPlayerComponent.mediaPlayerFactory().renderers().discoverers()) {
            RendererDiscoverer discoverer = mediaPlayerComponent.mediaPlayerFactory().renderers().discoverer(descriptions.name());
            discoverer.events().addRendererDiscovererEventListener(this);
            rendererDiscoverers.add(discoverer);
        }
    }

    @Override
    public void rendererDiscovererItemAdded(RendererDiscoverer rendererDiscoverer, RendererItem itemAdded) {
        application().post(new RendererAddedEvent(itemAdded));
    }

    @Override
    public void rendererDiscovererItemDeleted(RendererDiscoverer rendererDiscoverer, RendererItem itemDeleted) {
        application().post(new RendererDeletedEvent(itemDeleted));
    }

}
