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

package uk.co.caprica.vlcjplayer.view.snapshot;

import static uk.co.caprica.vlcjplayer.Application.resources;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcjplayer.view.image.ImagePane;
import uk.co.caprica.vlcjplayer.view.image.ImagePane.Mode;

import com.google.common.io.Files;

/**
 * Simple frame implementation that shows a buffered image.
 */
public class SnapshotView extends JFrame {

    private static final String DEFAULT_FILE_EXTENSION = "png";

    private final JFileChooser fileChooser = new JFileChooser();

    private final BufferedImage image;

    public SnapshotView(BufferedImage image) {
        this.image = image;
        setTitle("vlcj-player snapshot");
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new ImagePane(Mode.DEFAULT, image, 1.0f), BorderLayout.CENTER);
        contentPane.add(new ActionPane(), BorderLayout.SOUTH);
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void onSave() {
        if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this)) {
            File file = fileChooser.getSelectedFile();
            try {
                String ext = Files.getFileExtension(file.getName()).toLowerCase();
                if (ext == null) {
                    file = new File(file.getAbsolutePath() + DEFAULT_FILE_EXTENSION);
                    ext = DEFAULT_FILE_EXTENSION;
                }
                // FIXME should warn about overwriting if the file exists
                boolean wrote = ImageIO.write(image, ext, file);
                if (!wrote) {
                    JOptionPane.showMessageDialog(this, MessageFormat.format(resources().getString("error.saveImage"), file.toString(), MessageFormat.format(resources().getString("error.saveImageFormat"), ext)), resources().getString("dialog.saveImage"), JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(this, MessageFormat.format(resources().getString("error.saveImage"), file.toString(), e.getMessage()), resources().getString("dialog.saveImage"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ActionPane extends JPanel {

        private ActionPane() {
            setLayout(new MigLayout("fillx", "push[]", "[]"));
            JButton saveButton = new JButton("Save");
            add(saveButton);
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSave();
                }
            });
        }
    }
}
