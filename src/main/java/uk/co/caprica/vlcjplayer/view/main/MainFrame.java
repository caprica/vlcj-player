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

package uk.co.caprica.vlcjplayer.view.main;

import static uk.co.caprica.vlcjplayer.Application.application;
import static uk.co.caprica.vlcjplayer.Application.resources;
import static uk.co.caprica.vlcjplayer.view.action.Resource.resource;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.Logo;
import uk.co.caprica.vlcj.player.Marquee;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcjplayer.event.AfterExitFullScreenEvent;
import uk.co.caprica.vlcjplayer.event.BeforeEnterFullScreenEvent;
import uk.co.caprica.vlcjplayer.event.PausedEvent;
import uk.co.caprica.vlcjplayer.event.PlayingEvent;
import uk.co.caprica.vlcjplayer.event.ShowDebugEvent;
import uk.co.caprica.vlcjplayer.event.ShowEffectsEvent;
import uk.co.caprica.vlcjplayer.event.ShowMessagesEvent;
import uk.co.caprica.vlcjplayer.event.SnapshotImageEvent;
import uk.co.caprica.vlcjplayer.event.StoppedEvent;
import uk.co.caprica.vlcjplayer.view.BaseFrame;
import uk.co.caprica.vlcjplayer.view.MouseMovementDetector;
import uk.co.caprica.vlcjplayer.view.action.StandardAction;
import uk.co.caprica.vlcjplayer.view.action.mediaplayer.MediaPlayerActions;
import uk.co.caprica.vlcjplayer.view.snapshot.SnapshotView;

import com.google.common.eventbus.Subscribe;

@SuppressWarnings("serial")
public final class MainFrame extends BaseFrame {

    private static final String ACTION_EXIT_FULLSCREEN = "exit-fullscreen";

    private static final KeyStroke KEYSTROKE_ESCAPE = KeyStroke.getKeyStroke("ESCAPE");

    private static final KeyStroke KEYSTROKE_TOGGLE_FULLSCREEN = KeyStroke.getKeyStroke("F11");

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final Action mediaOpenAction;
    private final Action mediaQuitAction;

    private final StandardAction videoFullscreenAction;
    private final StandardAction videoAlwaysOnTopAction;

    private final Action subtitleAddSubtitleFileAction;

    private final Action toolsEffectsAction;
    private final Action toolsMessagesAction;
    private final Action toolsDebugAction;

    private final StandardAction viewStatusBarAction;

    private final Action helpAboutAction;

    private final JMenuBar menuBar;

    private final JMenu mediaMenu;
    private final JMenu mediaRecentMenu;

    private final JMenu playbackMenu;
    private final JMenu playbackTitleMenu;
    private final JMenu playbackChapterMenu;
    private final JMenu playbackSpeedMenu;

    private final JMenu audioMenu;
    private final JMenu audioTrackMenu;
    private final JMenu audioDeviceMenu;
    private final JMenu audioStereoMenu;

    private final JMenu videoMenu;
    private final JMenu videoTrackMenu;
    private final JMenu videoZoomMenu;
    private final JMenu videoAspectRatioMenu;
    private final JMenu videoCropMenu;

    private final JMenu subtitleMenu;
    private final JMenu subtitleTrackMenu;

    private final JMenu toolsMenu;

    private final JMenu viewMenu;

    private final JMenu helpMenu;

    private final JFileChooser fileChooser;

    private final PositionPane positionPane;

    private final ControlsPane controlsPane;

    private final StatusBar statusBar;

    private final VideoContentPane videoContentPane;

    private final JPanel bottomPane;

    private final MouseMovementDetector mouseMovementDetector;

    public MainFrame() {
        super("vlcj player");

        this.mediaPlayerComponent = application().mediaPlayerComponent();

        MediaPlayerActions mediaPlayerActions = application().mediaPlayerActions();

        mediaOpenAction = new StandardAction(resource("menu.media.item.openFile")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(MainFrame.this)) {
                    File file = fileChooser.getSelectedFile();
                    String mrl = file.getAbsolutePath();
                    application().addRecentMedia(mrl);
                    mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
                }
            }
        };

        mediaQuitAction = new StandardAction(resource("menu.media.item.quit")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        };

        videoFullscreenAction = new StandardAction(resource("menu.video.item.fullscreen")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.getMediaPlayer().toggleFullScreen();
            }
        };

        videoAlwaysOnTopAction = new StandardAction(resource("menu.video.item.alwaysOnTop")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean onTop;
                Object source = e.getSource();
                if (source instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)source;
                    onTop = menuItem.isSelected();
                }
                else {
                    throw new IllegalStateException("Don't know about source " + source);
                }
                setAlwaysOnTop(onTop);
            }
        };

        subtitleAddSubtitleFileAction = new StandardAction(resource("menu.subtitle.item.addSubtitleFile")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(MainFrame.this)) {
                    File file = fileChooser.getSelectedFile();
                    mediaPlayerComponent.getMediaPlayer().setSubTitleFile(file);
                }
            }
        };

        toolsEffectsAction = new StandardAction(resource("menu.tools.item.effects")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().post(ShowEffectsEvent.INSTANCE);
            }
        };

        toolsMessagesAction = new StandardAction(resource("menu.tools.item.messages")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().post(ShowMessagesEvent.INSTANCE);
            }
        };

        toolsDebugAction = new StandardAction(resource("menu.tools.item.debug")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().post(ShowDebugEvent.INSTANCE);
            }
        };

        viewStatusBarAction = new StandardAction(resource("menu.view.item.statusBar")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean visible;
                Object source = e.getSource();
                if (source instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)source;
                    visible = menuItem.isSelected();
                }
                else {
                    throw new IllegalStateException("Don't know about source " + source);
                }
                statusBar.setVisible(visible);
                bottomPane.invalidate();
                bottomPane.revalidate();
                bottomPane.getParent().invalidate();
                bottomPane.getParent().revalidate();
                MainFrame.this.invalidate();
                MainFrame.this.revalidate();
            }
        };

        helpAboutAction = new StandardAction(resource("menu.help.item.about")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog dialog = new AboutDialog(MainFrame.this);
                dialog.setLocationRelativeTo(MainFrame.this);

                dialog.setVisible(true);
            }
        };

        menuBar = new JMenuBar();

        mediaMenu = new JMenu(resource("menu.media").name());
        mediaMenu.setMnemonic(resource("menu.media").mnemonic());
        mediaMenu.add(new JMenuItem(mediaOpenAction));
        mediaRecentMenu = new RecentMediaMenu(resource("menu.media.item.recent")).menu();
        mediaMenu.add(mediaRecentMenu);
        mediaMenu.add(new JSeparator());
        mediaMenu.add(new JMenuItem(mediaQuitAction));
        menuBar.add(mediaMenu);

        playbackMenu = new JMenu(resource("menu.playback").name());
        playbackMenu.setMnemonic(resource("menu.playback").mnemonic());

        playbackTitleMenu = new TitleTrackMenu().menu();

        // Chapter could be an "on-demand" menu too, and it could be with radio buttons...

        playbackMenu.add(playbackTitleMenu);

        playbackChapterMenu = new ChapterMenu().menu();

        playbackMenu.add(playbackChapterMenu);
        playbackMenu.add(new JSeparator());
        playbackSpeedMenu = new JMenu(resource("menu.playback.item.speed").name());
        playbackSpeedMenu.setMnemonic(resource("menu.playback.item.speed").mnemonic());
        for (Action action : mediaPlayerActions.playbackSpeedActions()) {
            playbackSpeedMenu.add(new JMenuItem(action));
        }
        playbackMenu.add(playbackSpeedMenu);
        playbackMenu.add(new JSeparator());
        for (Action action : mediaPlayerActions.playbackSkipActions()) {
            playbackMenu.add(new JMenuItem(action));
        }
        playbackMenu.add(new JSeparator());
        for (Action action : mediaPlayerActions.playbackChapterActions()) {
            playbackMenu.add(new JMenuItem(action));
        }
        playbackMenu.add(new JSeparator());
        for (Action action : mediaPlayerActions.playbackControlActions()) {
            playbackMenu.add(new JMenuItem(action) { // FIXME need a standardmenuitem that disables the tooltip like this, very poor show...
                @Override
                public String getToolTipText() {
                    return null;
                }
            });
        }
        menuBar.add(playbackMenu);

        audioMenu = new JMenu(resource("menu.audio").name());
        audioMenu.setMnemonic(resource("menu.audio").mnemonic());

        audioTrackMenu = new AudioTrackMenu().menu();

        audioMenu.add(audioTrackMenu);
        audioDeviceMenu = new AudioDeviceMenu().menu();
        audioMenu.add(audioDeviceMenu);
        audioStereoMenu = new JMenu(resource("menu.audio.item.stereoMode").name());
        audioStereoMenu.setMnemonic(resource("menu.audio.item.stereoMode").mnemonic());
        for (Action action : mediaPlayerActions.audioStereoModeActions()) {
            audioStereoMenu.add(new JRadioButtonMenuItem(action));
        }
        audioMenu.add(audioStereoMenu);
        audioMenu.add(new JSeparator());
        for (Action action : mediaPlayerActions.audioControlActions()) {
            audioMenu.add(new JMenuItem(action));
        }
        menuBar.add(audioMenu);

        videoMenu = new JMenu(resource("menu.video").name());
        videoMenu.setMnemonic(resource("menu.video").mnemonic());

        videoTrackMenu = new VideoTrackMenu().menu();

        videoMenu.add(videoTrackMenu);
        videoMenu.add(new JSeparator());
        videoMenu.add(new JCheckBoxMenuItem(videoFullscreenAction));
        videoMenu.add(new JCheckBoxMenuItem(videoAlwaysOnTopAction));
        videoMenu.add(new JSeparator());
        videoZoomMenu = new JMenu(resource("menu.video.item.zoom").name());
        videoZoomMenu.setMnemonic(resource("menu.video.item.zoom").mnemonic());
        addActions(mediaPlayerActions.videoZoomActions(), videoZoomMenu/*, true*/); // FIXME how to handle zoom 1:1 and fit to window - also, probably should not use addActions to select
        videoMenu.add(videoZoomMenu);
        videoAspectRatioMenu = new JMenu(resource("menu.video.item.aspectRatio").name());
        videoAspectRatioMenu.setMnemonic(resource("menu.video.item.aspectRatio").mnemonic());
        addActions(mediaPlayerActions.videoAspectRatioActions(), videoAspectRatioMenu, true);
        videoMenu.add(videoAspectRatioMenu);
        videoCropMenu = new JMenu(resource("menu.video.item.crop").name());
        videoCropMenu.setMnemonic(resource("menu.video.item.crop").mnemonic());
        addActions(mediaPlayerActions.videoCropActions(), videoCropMenu, true);
        videoMenu.add(videoCropMenu);
        videoMenu.add(new JSeparator());
        videoMenu.add(new JMenuItem(mediaPlayerActions.videoSnapshotAction()));
        menuBar.add(videoMenu);

        subtitleMenu = new JMenu(resource("menu.subtitle").name());
        subtitleMenu.setMnemonic(resource("menu.subtitle").mnemonic());
        subtitleMenu.add(new JMenuItem(subtitleAddSubtitleFileAction));

        subtitleTrackMenu = new SubtitleTrackMenu().menu();

        subtitleMenu.add(subtitleTrackMenu);
        menuBar.add(subtitleMenu);

        toolsMenu = new JMenu(resource("menu.tools").name());
        toolsMenu.setMnemonic(resource("menu.tools").mnemonic());
        toolsMenu.add(new JMenuItem(toolsEffectsAction));
        toolsMenu.add(new JMenuItem(toolsMessagesAction));
        toolsMenu.add(new JSeparator());
        toolsMenu.add(new JMenuItem(toolsDebugAction));
        menuBar.add(toolsMenu);

        viewMenu = new JMenu(resource("menu.view").name());
        viewMenu.setMnemonic(resource("menu.view").mnemonic());
        viewMenu.add(new JCheckBoxMenuItem(viewStatusBarAction));
        menuBar.add(viewMenu);

        helpMenu = new JMenu(resource("menu.help").name());
        helpMenu.setMnemonic(resource("menu.help").mnemonic());
        helpMenu.add(new JMenuItem(helpAboutAction));
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        videoContentPane = new VideoContentPane();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(videoContentPane, BorderLayout.CENTER);
        contentPane.setTransferHandler(new MediaTransferHandler() {
            @Override
            protected void onMediaDropped(String[] uris) {
                mediaPlayerComponent.getMediaPlayer().playMedia(uris[0]);
            }
        });

        setContentPane(contentPane);

        fileChooser = new JFileChooser();

        bottomPane = new JPanel();
        bottomPane.setLayout(new BorderLayout());

        JPanel bottomControlsPane = new JPanel();
        bottomControlsPane.setLayout(new MigLayout("fill, insets 0 n n n", "[grow]", "[]0[]"));

        positionPane = new PositionPane(mediaPlayerComponent.getMediaPlayer());
        bottomControlsPane.add(positionPane, "grow, wrap");

        controlsPane = new ControlsPane(mediaPlayerActions);
        bottomPane.add(bottomControlsPane, BorderLayout.CENTER);
        bottomControlsPane.add(controlsPane, "grow");

        statusBar = new StatusBar();
        bottomPane.add(statusBar, BorderLayout.SOUTH);

        contentPane.add(bottomPane, BorderLayout.SOUTH);

        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                videoContentPane.showVideo();
                mouseMovementDetector.start();
                application().post(PlayingEvent.INSTANCE);
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
                mouseMovementDetector.stop();
                application().post(PausedEvent.INSTANCE);
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                mouseMovementDetector.stop();
                videoContentPane.showDefault();
                application().post(StoppedEvent.INSTANCE);
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                videoContentPane.showDefault();
                mouseMovementDetector.stop();
                application().post(StoppedEvent.INSTANCE);
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                videoContentPane.showDefault();
                mouseMovementDetector.stop();
                application().post(StoppedEvent.INSTANCE);
                JOptionPane.showMessageDialog(MainFrame.this, MessageFormat.format(resources().getString("error.errorEncountered"), fileChooser.getSelectedFile().toString()), resources().getString("dialog.errorEncountered"), JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        statusBar.setTitle(mediaPlayer.getMediaMeta().getTitle());
                    }
                });
            }

            @Override
            public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        positionPane.setDuration(newDuration);
                        statusBar.setDuration(newDuration);
                    }
                });
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        positionPane.setTime(newTime);
                        statusBar.setTime(newTime);
                    }
                });
            }

            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                Logo logo = Logo.logo()
                    .file("src/main/resources/vlcj-logo.png")
                    .position(libvlc_logo_position_e.top_left)
                    .opacity(0.5f)
                    .enable();

                mediaPlayer.setLogo(logo);

                Marquee marquee = Marquee.marquee()
                    .text(String.format("vlcj-player"))
                    .colour(Color.white.getRGB())
                    .size(20)
                    .position(libvlc_marquee_position_e.bottom_right)
                    .location(10,5)
                    .enable();

                mediaPlayer.setMarquee(marquee);
            }
        });

        getActionMap().put(ACTION_EXIT_FULLSCREEN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.getMediaPlayer().toggleFullScreen();
                videoFullscreenAction.select(false);
            }
        });

        applyPreferences();

        mouseMovementDetector = new VideoMouseMovementDetector(mediaPlayerComponent.getVideoSurface(), 500, mediaPlayerComponent);

        setMinimumSize(new Dimension(370, 240));
    }

    private ButtonGroup addActions(List<Action> actions, JMenu menu, boolean selectFirst) {
        ButtonGroup buttonGroup = addActions(actions, menu);
        if (selectFirst) {
            Enumeration<AbstractButton> en = buttonGroup.getElements();
            if (en.hasMoreElements()) {
                StandardAction action = (StandardAction) en.nextElement().getAction();
                action.select(true);
            }
        }
        return buttonGroup;
    }

    private ButtonGroup addActions(List<Action> actions, JMenu menu) {
        ButtonGroup buttonGroup = new ButtonGroup();
        for (Action action : actions) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(action);
            buttonGroup.add(menuItem);
            menu.add(menuItem);
        }
        return buttonGroup;
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(MainFrame.class);
        setBounds(
            prefs.getInt("frameX"     , 100),
            prefs.getInt("frameY"     , 100),
            prefs.getInt("frameWidth" , 800),
            prefs.getInt("frameHeight", 600)
        );
        boolean alwaysOnTop = prefs.getBoolean("alwaysOnTop", false);
        setAlwaysOnTop(alwaysOnTop);
        videoAlwaysOnTopAction.select(alwaysOnTop);
        boolean statusBarVisible = prefs.getBoolean("statusBar", false);
        statusBar.setVisible(statusBarVisible);
        viewStatusBarAction.select(statusBarVisible);
        fileChooser.setCurrentDirectory(new File(prefs.get("chooserDirectory", ".")));
        String recentMedia = prefs.get("recentMedia", "");
        if (recentMedia.length() > 0) {
            List<String> mrls = Arrays.asList(prefs.get("recentMedia", "").split("\\|"));
            Collections.reverse(mrls);
            for (String mrl : mrls) {
                application().addRecentMedia(mrl);
            }
        }
    }

    @Override
    protected void onShutdown() {
        if (wasShown()) {
            Preferences prefs = Preferences.userNodeForPackage(MainFrame.class);
            prefs.putInt    ("frameX"          , getX     ());
            prefs.putInt    ("frameY"          , getY     ());
            prefs.putInt    ("frameWidth"      , getWidth ());
            prefs.putInt    ("frameHeight"     , getHeight());
            prefs.putBoolean("alwaysOnTop"     , isAlwaysOnTop());
            prefs.putBoolean("statusBar"       , statusBar.isVisible());
            prefs.put       ("chooserDirectory", fileChooser.getCurrentDirectory().toString());

            String recentMedia;
            List<String> mrls = application().recentMedia();
            if (!mrls.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String mrl : mrls) {
                    if (sb.length() > 0) {
                        sb.append('|');
                    }
                    sb.append(mrl);
                }
                recentMedia = sb.toString();
            }
            else {
                recentMedia = "";
            }
            prefs.put("recentMedia", recentMedia);
        }
    }

    @Subscribe
    public void onBeforeEnterFullScreen(BeforeEnterFullScreenEvent event) {
        menuBar.setVisible(false);
        bottomPane.setVisible(false);
        // As the menu is now hidden, the shortcut will not work, so register a temporary key-binding
        registerEscapeBinding();
    }

    @Subscribe
    public void onAfterExitFullScreen(AfterExitFullScreenEvent event) {
        deregisterEscapeBinding();
        menuBar.setVisible(true);
        bottomPane.setVisible(true);
    }

    @Subscribe
    public void onSnapshotImage(SnapshotImageEvent event) {
        new SnapshotView(event.image());
    }

    private void registerEscapeBinding() {
        getInputMap().put(KEYSTROKE_ESCAPE, ACTION_EXIT_FULLSCREEN);
        getInputMap().put(KEYSTROKE_TOGGLE_FULLSCREEN, ACTION_EXIT_FULLSCREEN);
    }

    private void deregisterEscapeBinding() {
        getInputMap().remove(KEYSTROKE_ESCAPE);
        getInputMap().remove(KEYSTROKE_TOGGLE_FULLSCREEN);
    }

    private InputMap getInputMap() {
        JComponent c = (JComponent) getContentPane();
        return c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private ActionMap getActionMap() {
        JComponent c = (JComponent) getContentPane();
        return c.getActionMap();
    }
}
