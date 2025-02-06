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

package uk.co.caprica.vlcjplayer.view.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * A panel that paints a background image.
 */
public final class ImagePane extends JComponent {

    private static final long serialVersionUID = 1L;

    public enum Mode {
        DEFAULT,
        CENTER,
        FIT
    }

    private final Mode mode;
    private final float opacity;

    private BufferedImage sourceImage;

    private BufferedImage image;

    private int lastWidth;
    private int lastHeight;

    public ImagePane(Mode mode, URL imageUrl, float opacity) {
        this.mode = mode;
        this.opacity = opacity;
        newImage(imageUrl);
        prepareImage();
    }

    public ImagePane(Mode mode, BufferedImage image, float opacity) {
        this.mode = mode;
        this.opacity = opacity;
        this.sourceImage = image;
        prepareImage();
    }

    @Override
    public Dimension getPreferredSize() {
        return sourceImage != null ? new Dimension(sourceImage.getWidth(), sourceImage.getHeight()) : super.getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        prepareImage();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.black);
        g2.fill(getBounds());
        if (image != null) {
            int x = 0;
            int y = 0;
            if (mode != Mode.DEFAULT) {
                x = (getWidth() - image.getWidth()) / 2;
                y = (getHeight() - image.getHeight()) / 2;
            }
            Composite oldComposite = g2.getComposite();
            if (opacity != 1.0f) {
                g2.setComposite(AlphaComposite.SrcOver.derive(opacity));
            }
            g2.drawImage(image, null, x, y);
            g2.setComposite(oldComposite);
        }
        g2.dispose();
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    private void newImage(URL imageUrl) {
        image = null;
        if (imageUrl != null) {
            try {
                sourceImage = ImageIO.read(imageUrl);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareImage() {
        if (lastWidth != getWidth() || lastHeight != getHeight()) {
            lastWidth = getWidth();
            lastHeight = getHeight();
            if (sourceImage != null) {
                switch (mode) {
                    case DEFAULT:
                    case CENTER:
                        image = sourceImage;
                        break;
                    case FIT:
                        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2 = image.createGraphics();
                        AffineTransform at = AffineTransform.getScaleInstance((double)image.getWidth() / sourceImage.getWidth(), (double)image.getHeight() / sourceImage.getHeight());
                        g2.drawRenderedImage(sourceImage, at);
                        g2.dispose();
                        break;
                }
            }
            else {
                image = null;
            }
        }
    }
}
