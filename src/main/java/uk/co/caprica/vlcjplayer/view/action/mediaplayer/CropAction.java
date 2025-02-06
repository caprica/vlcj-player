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
 * Copyright 2015-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcjplayer.view.action.mediaplayer;

import uk.co.caprica.vlcjplayer.view.action.Resource;

import java.awt.event.ActionEvent;

import static uk.co.caprica.vlcjplayer.Application.application;

final class CropAction extends MediaPlayerAction {

    private final String cropGeometry;

    CropAction(Resource resource, String cropGeometry) {
        super(resource);
        this.cropGeometry = cropGeometry;
    }

    public CropAction(String name, String cropGeometry) {
        super(name);
        this.cropGeometry = cropGeometry;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // FIXME
//        application().mediaPlayer().video().setCropGeometry(cropGeometry);
    }

}
