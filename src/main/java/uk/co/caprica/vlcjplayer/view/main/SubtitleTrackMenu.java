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

import uk.co.caprica.vlcj.player.base.TextTrack;
import uk.co.caprica.vlcj.player.base.TrackList;
import uk.co.caprica.vlcjplayer.view.action.mediaplayer.SubtitleTrackAction;

import javax.swing.*;

import static uk.co.caprica.vlcjplayer.Application.application;
import static uk.co.caprica.vlcjplayer.view.action.Resource.resource;

final class SubtitleTrackMenu extends TrackMenu<TextTrack> {

    SubtitleTrackMenu() {
        super(resource("menu.subtitle.item.track"));
    }

    @Override
    protected Action createAction(TextTrack trackDescription) {
        return new SubtitleTrackAction(trackName(trackDescription), trackDescription);
    }

    @Override
    protected TrackList<TextTrack> onGetTrackDescriptions() {
        return application().mediaPlayer().tracks().textTracks();
    }

    @Override
    protected int onGetSelectedTrack() {
        TextTrack selectedTrack = application().mediaPlayer().tracks().selectedTextTrack();
        return selectedTrack != null ? selectedTrack.id() : -1;
    }
}
