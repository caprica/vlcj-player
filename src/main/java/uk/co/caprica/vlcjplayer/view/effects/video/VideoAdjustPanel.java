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

package uk.co.caprica.vlcjplayer.view.effects.video;

import static uk.co.caprica.vlcjplayer.Application.application;
import static uk.co.caprica.vlcjplayer.Application.resources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.player.base.LibVlcConst;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcjplayer.view.BasePanel;

// Note that LibVLC 3.0.0 currently has a problem that the video adjust filter needs to be disabled then enabled again after changing ANY property

public class VideoAdjustPanel extends BasePanel {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final JCheckBox enableCheckBox;
    private final JLabel hueLabel;
    private final JSlider hueSlider;
    private final JLabel brightnessLabel;
    private final JSlider brightnessSlider;
    private final JLabel contrastLabel;
    private final JSlider contrastSlider;
    private final JLabel saturationLabel;
    private final JSlider saturationSlider;
    private final JLabel gammaLabel;
    private final JSlider gammaSlider;

    public VideoAdjustPanel() {
        this.mediaPlayerComponent = application().mediaPlayerComponent();

        enableCheckBox = new JCheckBox(resources().getString("dialog.effects.tabs.video.adjust.enable"));

        hueLabel = new JLabel();
        hueLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.hue"));

        hueSlider = new JSlider();
        hueSlider.setMinimum(Math.round(LibVlcConst.MIN_HUE * 100.0f));
        hueSlider.setMaximum(Math.round(LibVlcConst.MAX_HUE * 100.0f));
        hueLabel.setLabelFor(hueSlider);

        brightnessLabel = new JLabel();
        brightnessLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.brightness"));

        brightnessSlider = new JSlider();
        brightnessSlider.setMinimum(Math.round(LibVlcConst.MIN_BRIGHTNESS * 100.0f));
        brightnessSlider.setMaximum(Math.round(LibVlcConst.MAX_BRIGHTNESS * 100.0f));
        brightnessLabel.setLabelFor(brightnessSlider);

        contrastLabel = new JLabel();
        contrastLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.contrast"));

        contrastSlider = new JSlider();
        contrastSlider.setMinimum(Math.round(LibVlcConst.MIN_CONTRAST * 100.0f));
        contrastSlider.setMaximum(Math.round(LibVlcConst.MAX_CONTRAST * 100.0f));
        contrastSlider.setPaintLabels(true);
        contrastSlider.setPaintTicks(true);
        contrastLabel.setLabelFor(contrastSlider);

        saturationLabel = new JLabel();
        saturationLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.saturation"));

        saturationSlider = new JSlider();
        saturationSlider.setMinimum(Math.round(LibVlcConst.MIN_SATURATION * 100.0f));
        saturationSlider.setMaximum(Math.round(LibVlcConst.MAX_SATURATION * 100.0f));
        saturationLabel.setLabelFor(saturationSlider);

        gammaLabel = new JLabel();
        gammaLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.gamma"));

        gammaSlider = new JSlider();
        gammaSlider.setMinimum(Math.round(LibVlcConst.MIN_GAMMA * 100.0f));
        gammaSlider.setMaximum(Math.round(LibVlcConst.MAX_GAMMA * 100.0f));

        MediaPlayer mediaPlayer = mediaPlayerComponent.mediaPlayer();

        enableCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enable = enableCheckBox.isSelected();
                enableControls(enable);
                mediaPlayer.video().setAdjustVideo(enable);
            }
        });

        hueSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                mediaPlayer.video().setHue(source.getValue() / 100.0f);
            }
        });

        brightnessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                mediaPlayer.video().setBrightness(source.getValue() / 100.0f);
            }
        });

        contrastSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                mediaPlayer.video().setContrast(source.getValue() / 100.0f);
            }
        });

        saturationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                mediaPlayer.video().setSaturation(source.getValue() / 100.0f);
            }
        });

        gammaSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                mediaPlayer.video().setGamma(source.getValue() / 100.0f);
            }
        });

        contrastSlider.setValue(Math.round(mediaPlayer.video().brightness() * 100.0f));
        brightnessSlider.setValue(Math.round(mediaPlayer.video().contrast() * 100.0f));
        hueSlider.setValue(Math.round(mediaPlayer.video().hue() * 100.0f));
        saturationSlider.setValue(Math.round(mediaPlayer.video().saturation() * 100.0f));
        gammaSlider.setValue(Math.round(mediaPlayer.video().gamma() * 100.0f));

        setLayout(new MigLayout("fill", "[shrink]rel[grow]", ""));
        add(enableCheckBox, "span 2, wrap");
        add(hueLabel, "shrink");
        add(hueSlider, "wrap");
        add(brightnessLabel);
        add(brightnessSlider, "wrap");
        add(contrastLabel);
        add(contrastSlider, "wrap");
        add(saturationLabel);
        add(saturationSlider, "wrap");
        add(gammaLabel);
        add(gammaSlider, "wrap");

        enableControls(false);
    }

    private void enableControls(boolean enable) {
        hueSlider.setEnabled(enable);
        brightnessSlider.setEnabled(enable);
        contrastSlider.setEnabled(enable);
        saturationSlider.setEnabled(enable);
        gammaSlider.setEnabled(enable);
    }
}
