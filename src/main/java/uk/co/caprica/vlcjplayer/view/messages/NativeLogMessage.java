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

import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;

final class NativeLogMessage {

    private final String module;
    private final String name;
    private final libvlc_log_level_e level;
    private final String message;

    NativeLogMessage(String module, String name, libvlc_log_level_e level, String message) {
        this.module = module;
        this.name = name;
        this.level = level;
        this.message = message;
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public libvlc_log_level_e getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }
}
