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

package uk.co.caprica.vlcjplayer.view.messages;

import ca.odell.glazedlists.gui.TableFormat;

final class NativeLogTableFormat implements TableFormat<NativeLogMessage> {

    private static final String[] COLUMN_NAMES = {
        "Module",
        "Object",
        "Level",
        "Message"
    };

    private static final int COLUMN_MODULE = 0;

    private static final int COLUMN_OBJECT = 1;

    private static final int COLUMN_LEVEL = 2;

    private static final int COLUMN_MESSAGE = 3;

    NativeLogTableFormat() {
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getColumnValue(NativeLogMessage message, int column) {
        Object result;
        switch(column) {
            case COLUMN_MODULE:
                result = message.getModule();
                break;
            case COLUMN_OBJECT:
                result = message.getName();
                break;
            case COLUMN_LEVEL:
                result = message.getLevel();
                break;
            case COLUMN_MESSAGE:
                result = message.getMessage();
                break;
            default:
                result = null;
                break;
        }
        return result;
    }
}
