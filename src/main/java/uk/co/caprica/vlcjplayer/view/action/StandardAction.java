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

package uk.co.caprica.vlcjplayer.view.action;

import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class StandardAction extends AbstractAction {

    public StandardAction(Resource resource) {
        putValue(Action.NAME             , resource.name      ());
        putValue(Action.MNEMONIC_KEY     , resource.mnemonic  ());
        putValue(Action.ACCELERATOR_KEY  , resource.shortcut  ());
        putValue(Action.SHORT_DESCRIPTION, resource.tooltip   ());
        putValue(Action.SMALL_ICON       , resource.menuIcon  ());
        putValue(Action.LARGE_ICON_KEY   , resource.buttonIcon());
    }

    public StandardAction(String name) {
        putValue(Action.NAME, name);
    }

    public final void select(boolean select) {
        putValue(Action.SELECTED_KEY, select);
    }
}
