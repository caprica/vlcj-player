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

import uk.co.caprica.vlcj.player.base.Track;
import uk.co.caprica.vlcj.player.base.VideoTrackList;
import uk.co.caprica.vlcjplayer.view.action.mediaplayer.TitleAction;

import javax.swing.*;

import static uk.co.caprica.vlcjplayer.Application.application;
import static uk.co.caprica.vlcjplayer.view.action.Resource.resource;

final class TitleTrackMenu extends TrackMenu {

    TitleTrackMenu() {
        super(resource("menu.playback.item.title"));
    }

    @Override
    protected Action createAction(Track trackDescription) {
        return new TitleAction(trackDescription.description(), trackDescription.id());
    }

    @Override
    protected VideoTrackList onGetTrackDescriptions() {
        // FIXME for now I'll just convert the list... but it should be List<TitleDescription>
//        List<TitleDescription> titles = application().mediaPlayer().titles().titleDescriptions();
//        List<TrackDescription> result = new ArrayList<TrackDescription>(titles.size());
//        int id = 0;
//        for (TitleDescription title : titles) {
//            if (!title.isMenu()) {
//                String name = title.name();
//                if (name == null) {
//                    name = String.format("Feature %d", id+1);
//                }
//                // FIXME use Duration from TitleDescription
//                result.add(new TrackDescription(id++, name));
//            }
//        }
//        return result;
//        return new VideoTrackList(null);
        return null;
    }

    @Override
    protected int onGetSelectedTrack() {
        return application().mediaPlayer().titles().title();
    }
}
