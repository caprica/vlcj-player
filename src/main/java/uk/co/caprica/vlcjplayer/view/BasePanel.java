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

import javax.swing.JPanel;

import uk.co.caprica.vlcjplayer.event.ShutdownEvent;

import com.google.common.eventbus.Subscribe;

public abstract class BasePanel extends JPanel {

    public BasePanel() {
        application().subscribe(this);
    }

    @Subscribe
    public final void onShutdown(ShutdownEvent event) {
        onShutdown();
    }

    /**
     * Override, e.g. to save component preferences.
     */
    protected final void onShutdown() {
    }
}
