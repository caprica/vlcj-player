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
import static uk.co.caprica.vlcjplayer.Application.resources;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.support.Info;

final class AboutDialog extends JDialog {

    AboutDialog(Window owner) {
        super(owner, resources().getString("dialog.about"), Dialog.ModalityType.DOCUMENT_MODAL);

        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/application.properties"));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load build.properties", e);
        }

        setLayout(new MigLayout("insets 30, fillx", "[shrink]30[shrink][grow]", "[]30[]10[]10[]30[]10[]10[]10[]0[]"));
        getContentPane().setBackground(Color.white);

        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(getClass().getResource("/vlcj-logo.png")));

        JLabel applicationLabel = new JLabel();
        applicationLabel.setFont(applicationLabel.getFont().deriveFont(30.0f));
        applicationLabel.setText(resources().getString("dialog.about.application"));

        JLabel blurb1Label = new JLabel();
        blurb1Label.setText(resources().getString("dialog.about.blurb1"));

        JLabel blurb2Label = new JLabel();
        blurb2Label.setText(resources().getString("dialog.about.blurb2"));

        JLabel attribution1Label = new JLabel();
        attribution1Label.setText(resources().getString("dialog.about.attribution1"));

        JLabel applicationVersionLabel = new JLabel();
        applicationVersionLabel.setText(resources().getString("dialog.about.applicationVersion"));

        JLabel applicationVersionValueLabel = new ValueLabel();
        applicationVersionValueLabel.setText(properties.getProperty("application.version"));

        JLabel vlcjVersionLabel = new JLabel();
        vlcjVersionLabel.setText(resources().getString("dialog.about.vlcjVersion"));

        JLabel vlcjVersionValueLabel = new ValueLabel();
        vlcjVersionValueLabel.setText(Info.getInstance().vlcjVersion().toString());

        JLabel vlcVersionLabel = new JLabel();
        vlcVersionLabel.setText(resources().getString("dialog.about.vlcVersion"));

        JLabel nativeLibraryPathLabel = new JLabel();
        nativeLibraryPathLabel.setText(resources().getString("dialog.about.nativeLibraryPath"));

        JLabel nativeLibraryPathValueLabel = new ValueLabel();
        nativeLibraryPathValueLabel.setText(application().mediaPlayerComponent().mediaPlayerFactory().nativeLibraryPath());

        JLabel vlcVersionValueLabel = new ValueLabel();
        vlcVersionValueLabel.setText(application().mediaPlayerComponent().mediaPlayerFactory().application().version());

        JLabel vlcChangesetValueLabel = new ValueLabel();
        vlcChangesetValueLabel.setText(application().mediaPlayerComponent().mediaPlayerFactory().application().changeset());

        add(logoLabel, "shrink, top, spany 8");
        add(applicationLabel, "grow, spanx 2, wrap");
        add(blurb1Label, "grow, spanx 2, wrap");
        add(blurb2Label, "grow, spanx 2, wrap");
        add(attribution1Label, "grow, spanx 2, wrap");
        add(applicationVersionLabel, "");
        add(applicationVersionValueLabel, "wrap");
        add(vlcjVersionLabel);
        add(vlcjVersionValueLabel, "wrap");
        add(nativeLibraryPathLabel, "");
        add(nativeLibraryPathValueLabel, "wrap");
        add(vlcVersionLabel);
        add(vlcVersionValueLabel, "wrap");
        add(vlcChangesetValueLabel, "skip 2");

        getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        pack();
        setResizable(false);
    }

    private class ValueLabel extends JLabel {

        public ValueLabel() {
            setFont(getFont().deriveFont(Font.BOLD));
        }
    }
}
