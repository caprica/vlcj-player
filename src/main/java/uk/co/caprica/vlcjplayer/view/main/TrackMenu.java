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

import java.util.List;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import uk.co.caprica.vlcj.player.base.TrackDescription;
import uk.co.caprica.vlcjplayer.view.OnDemandMenu;
import uk.co.caprica.vlcjplayer.view.action.Resource;

abstract class TrackMenu extends OnDemandMenu {

    private static final String KEY_TRACK_DESCRIPTION = "track-description";

    TrackMenu(Resource resource) {
        super(resource, true);
    }

    @Override
    protected final void onPrepareMenu(JMenu menu) {
        ButtonGroup buttonGroup = new ButtonGroup();
        int selectedTrack = onGetSelectedTrack();
        for (TrackDescription trackDescription : onGetTrackDescriptions()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(createAction(trackDescription));
            menuItem.putClientProperty(KEY_TRACK_DESCRIPTION, trackDescription);
            buttonGroup.add(menuItem);
            menu.add(menuItem);
            if (selectedTrack == trackDescription.id()) {
                menuItem.setSelected(true);
            }
        }
    }

    protected abstract Action createAction(TrackDescription trackDescription);

    protected abstract List<TrackDescription> onGetTrackDescriptions();

    protected abstract int onGetSelectedTrack();
}
