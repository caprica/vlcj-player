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

import uk.co.caprica.vlcj.player.base.TrackList;
import uk.co.caprica.vlcj.player.base.VideoTrack;
import uk.co.caprica.vlcjplayer.view.action.mediaplayer.VideoTrackAction;

import javax.swing.*;

import static uk.co.caprica.vlcjplayer.Application.application;
import static uk.co.caprica.vlcjplayer.view.action.Resource.resource;

final class VideoTrackMenu extends TrackMenu<VideoTrack> {

    VideoTrackMenu() {
        super(resource("menu.video.item.track"));
    }

    @Override
    protected Action createAction(VideoTrack trackDescription) {
        return new VideoTrackAction(trackDescription.description(), trackDescription);
    }

    @Override
    protected TrackList<VideoTrack> onGetTrackDescriptions() {
        return application().mediaPlayer().tracks().videoTracks();
    }

    @Override
    protected int onGetSelectedTrack() {
        return application().mediaPlayer().tracks().selectedVideoTrack().id();
    }
}
