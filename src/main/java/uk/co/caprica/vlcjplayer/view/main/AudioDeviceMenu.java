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

import java.awt.Component;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import uk.co.caprica.vlcj.player.base.AudioDevice;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcjplayer.view.OnDemandMenu;
import uk.co.caprica.vlcjplayer.view.action.mediaplayer.AudioDeviceAction;

public final class AudioDeviceMenu extends OnDemandMenu {

    private static final String KEY_AUDIO_DEVICE = "audio-device";

    public AudioDeviceMenu() {
        super(resource("menu.audio.item.device"));
    }

    @Override
    protected void onCreateMenu(JMenu menu) {
        MediaPlayer mediaPlayer = application().mediaPlayer();
        ButtonGroup buttonGroup = new ButtonGroup();
        for (AudioDevice audioDevice : mediaPlayer.audio().outputDevices()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(new AudioDeviceAction(audioDevice));
            menuItem.putClientProperty(KEY_AUDIO_DEVICE, audioDevice);
            buttonGroup.add(menuItem);
            menu.add(menuItem);
        }
    }

    @Override
    protected void onPrepareMenu(JMenu menu) {
        String audioDeviceId = application().mediaPlayer().audio().outputDevice();
        for (Component c : menu.getMenuComponents()) {
            JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) c;
            AudioDevice audioDevice = (AudioDevice) menuItem.getClientProperty(KEY_AUDIO_DEVICE);
            if (audioDevice.getDeviceId().equals(audioDeviceId)) {
                menuItem.setSelected(true);
                break;
            }
        }
    }
}
