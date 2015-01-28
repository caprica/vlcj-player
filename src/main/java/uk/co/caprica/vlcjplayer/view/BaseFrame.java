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

package uk.co.caprica.vlcjplayer.view;

import static uk.co.caprica.vlcjplayer.Application.application;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import uk.co.caprica.vlcjplayer.event.ShutdownEvent;

import com.google.common.eventbus.Subscribe;

@SuppressWarnings("serial")
public abstract class BaseFrame extends JFrame {

    private boolean wasShown;

    public BaseFrame(String title) {
        super(title);
        try {
            setIconImage(ImageIO.read(getClass().getResource("/vlcj-logo-frame.png")));
        }
        catch (IOException e) {
        }
        application().subscribe(this);
    }

    @Override
    public final void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            this.wasShown = true;
        }
    }

    @Subscribe
    public final void onShutdown(ShutdownEvent event) {
        onShutdown();
    }

    protected final boolean wasShown() {
        return wasShown;
    }

    /**
     * Override, e.g. to save component preferences.
     */
    protected void onShutdown() {
    }
}
