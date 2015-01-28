package uk.co.caprica.vlcjplayer.view.main;

import static uk.co.caprica.vlcjplayer.Application.application;

import java.awt.CardLayout;

import javax.swing.JPanel;

import uk.co.caprica.vlcjplayer.view.image.ImagePane;

final class VideoContentPane extends JPanel {

    private static final String NAME_DEFAULT = "default";

    private static final String NAME_VIDEO = "video";

    private final CardLayout cardLayout;

    VideoContentPane() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        add(new ImagePane(ImagePane.Mode.CENTER, getClass().getResource("/vlcj-logo.png"), 0.3f), NAME_DEFAULT);
        add(application().mediaPlayerComponent(), NAME_VIDEO);
    }

    public void showDefault() {
        cardLayout.show(this, NAME_DEFAULT);
    }

    public void showVideo() {
        cardLayout.show(this, NAME_VIDEO);
    }
}
