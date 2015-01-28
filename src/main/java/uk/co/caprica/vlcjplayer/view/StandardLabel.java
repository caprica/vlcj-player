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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;

import javax.swing.JLabel;

public class StandardLabel extends JLabel {

    private final String template;

    public StandardLabel() {
        this(null);
    }

    public StandardLabel(String template) {
        this.template = template;
    }

    @Override
    public Dimension getMinimumSize() {
        Insets insets = getInsets();
        int w = insets.left + insets.right;
        int h = insets.top + insets.bottom;
        FontMetrics fontMetrics = getFontMetrics(getFont());
        h += fontMetrics.getHeight();
        String text = getText();
        if (template != null) {
            w += fontMetrics.stringWidth(template);
        }
        else {
            w += text != null && text.length() > 0 ? fontMetrics.stringWidth(text) : 32;
        }
        return new Dimension(w, h);
    }
}
