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
import static uk.co.caprica.vlcjplayer.view.action.Resource.resource;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import uk.co.caprica.vlcj.player.base.TitleDescription;
import uk.co.caprica.vlcj.player.base.TrackDescription;
import uk.co.caprica.vlcjplayer.view.action.mediaplayer.TitleAction;

final class TitleTrackMenu extends TrackMenu {

    TitleTrackMenu() {
        super(resource("menu.playback.item.title"));
    }

    @Override
    protected Action createAction(TrackDescription trackDescription) {
        return new TitleAction(trackDescription.description(), trackDescription.id());
    }

    @Override
    protected List<TrackDescription> onGetTrackDescriptions() {
        // FIXME for now I'll just convert the list... but it should be List<TitleDescription>
        List<TitleDescription> titles = application().mediaPlayer().titles().titleDescriptions();
        List<TrackDescription> result = new ArrayList<TrackDescription>(titles.size());
        int id = 0;
        for (TitleDescription title : titles) {
            if (!title.isMenu()) {
                String name = title.name();
                if (name == null) {
                    name = String.format("Feature %d", id+1);
                }
                // FIXME use Duration from TitleDescription
                result.add(new TrackDescription(id++, name));
            }
        }
        return result;
    }

    @Override
    protected int onGetSelectedTrack() {
        return application().mediaPlayer().titles().title();
    }
}
