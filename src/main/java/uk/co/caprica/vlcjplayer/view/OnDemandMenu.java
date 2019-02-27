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

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import uk.co.caprica.vlcjplayer.view.action.Resource;

public abstract class OnDemandMenu implements MenuListener {

    private final JMenu menu;

    private final boolean clearOnPrepare;

    public OnDemandMenu(Resource resource) {
        this(resource, false);
    }

    public OnDemandMenu(Resource resource, boolean clearOnPrepare) {
        this.menu = new JMenu(resource.name());
        this.menu.setMnemonic(resource.mnemonic());
        this.menu.addMenuListener(this);
        this.clearOnPrepare = clearOnPrepare;
        onCreateMenu(this.menu);
    }

    public final JMenu menu() {
        return menu;
    }

    @Override
    public final void menuSelected(MenuEvent e) {
        if (clearOnPrepare) {
            menu.removeAll();
        }
        onPrepareMenu(menu);
        menu.setEnabled(menu.getItemCount() > 0);
    }

    @Override
    public final void menuDeselected(MenuEvent e) {
    }

    @Override
    public final void menuCanceled(MenuEvent e) {
    }

    protected void onCreateMenu(JMenu menu) {
    }

    protected abstract void onPrepareMenu(JMenu menu);
}
