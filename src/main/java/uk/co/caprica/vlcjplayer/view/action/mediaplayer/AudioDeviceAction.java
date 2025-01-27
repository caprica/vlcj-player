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

package uk.co.caprica.vlcjplayer.view.action.mediaplayer;

import uk.co.caprica.vlcj.player.base.AudioDevice;

import java.awt.event.ActionEvent;

import static uk.co.caprica.vlcjplayer.Application.application;

public final class AudioDeviceAction extends MediaPlayerAction {

    private final AudioDevice audioDevice;

    public AudioDeviceAction(AudioDevice audioDevice) {
        super(audioDevice.getLongName());
        this.audioDevice = audioDevice;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        application().mediaPlayer().audio().setOutputDevice(audioDevice.getDeviceId());
    }

}
