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

package uk.co.caprica.vlcjplayer.view.effects.audio;

import static uk.co.caprica.vlcjplayer.Application.resources;

import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcjplayer.view.BasePanel;

public class AudioEffectsPanel extends BasePanel {

    private final JTabbedPane tabbedPane;

    private final EqualizerPanel equalizerPanel;

    public AudioEffectsPanel() {
        tabbedPane = new JTabbedPane();

        equalizerPanel = new EqualizerPanel();
        tabbedPane.add(equalizerPanel, resources().getString("dialog.effects.tabs.audio.equalizer"));

        setLayout(new MigLayout("fill", "grow", "grow"));
        add(tabbedPane, "grow");
    }
}
