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

package uk.co.caprica.vlcjplayer.view.effects;

import static uk.co.caprica.vlcjplayer.Application.resources;

import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcjplayer.event.ShowEffectsEvent;
import uk.co.caprica.vlcjplayer.view.BaseFrame;
import uk.co.caprica.vlcjplayer.view.effects.audio.AudioEffectsPanel;
import uk.co.caprica.vlcjplayer.view.effects.video.VideoEffectsPanel;

import com.google.common.eventbus.Subscribe;

@SuppressWarnings("serial")
public class EffectsFrame extends BaseFrame {

    private final JTabbedPane tabbedPane;

    private final AudioEffectsPanel audioEffectsPanel;
    private final VideoEffectsPanel videoEffectsPanel;

    public EffectsFrame() {
        super(resources().getString("dialog.effects"));

        tabbedPane = new JTabbedPane();

        audioEffectsPanel = new AudioEffectsPanel();
        tabbedPane.addTab(resources().getString("dialog.effects.tabs.audio"), audioEffectsPanel);

        videoEffectsPanel = new VideoEffectsPanel();
        tabbedPane.addTab(resources().getString("dialog.effects.tabs.video"), videoEffectsPanel);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(4,  4,  4,  4));
        contentPane.setLayout(new MigLayout("fill", "[grow]", "[grow]"));
        contentPane.add(tabbedPane, "grow");

        setContentPane(tabbedPane);

        applyPreferences();
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(EffectsFrame.class);
        setBounds(
            prefs.getInt("frameX"     , 300),
            prefs.getInt("frameY"     , 300),
            prefs.getInt("frameWidth" , 500),
            prefs.getInt("frameHeight", 500)
        );
    }

    @Override
    protected void onShutdown() {
        if (wasShown()) {
            Preferences prefs = Preferences.userNodeForPackage(EffectsFrame.class);
            prefs.putInt("frameX"      , getX     ());
            prefs.putInt("frameY"      , getY     ());
            prefs.putInt("frameWidth"  , getWidth ());
            prefs.putInt("frameHeight" , getHeight());
        }
    }

    @Subscribe
    public void onShowEffects(ShowEffectsEvent event) {
        setVisible(true);
    }
}
