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

package uk.co.caprica.vlcjplayer.view.effects.audio;

import static uk.co.caprica.vlcjplayer.Application.application;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.player.base.LibVlcConst;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.base.Equalizer;
import uk.co.caprica.vlcjplayer.view.BasePanel;
import uk.co.caprica.vlcjplayer.view.SliderControl;
import uk.co.caprica.vlcjplayer.view.StandardLabel;

public class EqualizerPanel extends BasePanel implements ChangeListener, ItemListener, ActionListener {

    private static final String BAND_INDEX_PROPERTY = "equalizerBandIndex";

    private final String dbFormat = "%.1f dB";

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private final Equalizer equalizer;

    private final SliderControl preampControl;
    private final SliderControl[] bandControls;

    private final JCheckBox enableCheckBox;
    private final JComboBox<String> presetComboBox;

    private boolean applyingPreset;

    public EqualizerPanel() {
        this.mediaPlayerComponent = application().mediaPlayerComponent();

        this.equalizer = mediaPlayerComponent.mediaPlayerFactory().equalizer().newEqualizer();

        List<Float> list = mediaPlayerComponent.mediaPlayerFactory().equalizer().bands();
        List<String> presets = mediaPlayerComponent.mediaPlayerFactory().equalizer().presets();

        JPanel bandsPane = new JPanel();
        bandsPane.setLayout(new GridLayout(1, 1 + list.size(), 2, 0));

        preampControl = new SliderControl("Preamp", (int)LibVlcConst.MIN_GAIN, (int)LibVlcConst.MAX_GAIN, 0, dbFormat);
        preampControl.getSlider().addChangeListener(this);
        bandsPane.add(preampControl);

        bandControls = new SliderControl[list.size()];
        for(int i = 0; i < list.size(); i++) {
            bandControls[i] = new SliderControl(formatFrequency(list.get(i)), (int)LibVlcConst.MIN_GAIN, (int)LibVlcConst.MAX_GAIN, 0, dbFormat);
            bandControls[i].getSlider().putClientProperty(BAND_INDEX_PROPERTY, i);
            bandControls[i].getSlider().addChangeListener(this);
            bandsPane.add(bandControls[i]);
        }

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(bandsPane, BorderLayout.CENTER);

        JPanel controlsPane = new JPanel();
        controlsPane.setLayout(new MigLayout("fill", "[]push[]rel[]", "[]"));

        enableCheckBox = new JCheckBox("Enable");
        enableCheckBox.setMnemonic('e');
        controlsPane.add(enableCheckBox);

        JLabel presetLabel = new StandardLabel();
        presetLabel.setText("Preset:");
        presetLabel.setDisplayedMnemonic('p');
        controlsPane.add(presetLabel);

        presetComboBox = new JComboBox<String>();
        presetLabel.setLabelFor(presetComboBox);
        DefaultComboBoxModel<String> presetModel = (DefaultComboBoxModel<String>) presetComboBox.getModel();
        presetModel.addElement(null);
        for(String presetName : presets) {
            presetModel.addElement(presetName);
        }
        controlsPane.add(presetComboBox, "width pref");
        contentPane.add(controlsPane, BorderLayout.NORTH);
        setLayout(new MigLayout("fill", "[]20[]0[]0[]0[]0[]0[]0[]0[]0[]0[]", ""));
        setLayout(new BorderLayout());
        add(contentPane, BorderLayout.CENTER);
        enableCheckBox.addActionListener(this);
        presetComboBox.addItemListener(this);
        enableControls(false);
    }

    private void enableControls(boolean enable) {
        presetComboBox.setEnabled(enable);
        preampControl.setEnabled(enable);
        for (SliderControl sliderControl : bandControls) {
            sliderControl.setEnabled(enable);
        }
    }

    private String formatFrequency(float hz) {
        if(hz < 1000.0f) {
            return String.format("%.0f Hz", hz);
        }
        else {
            return String.format("%.0f kHz", hz / 1000f);
        }
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        boolean enable = enableCheckBox.isSelected();
        application().mediaPlayer().audio().setEqualizer(enable ? equalizer : null);
        enableControls(enable);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() instanceof JSlider) {
            JSlider slider = (JSlider)e.getSource();
            Integer index = (Integer)slider.getClientProperty(BAND_INDEX_PROPERTY);
            int value = slider.getValue();
            // Band...
            if(index != null) {
                equalizer.setAmp(index, value / 100f);
            }
            // Preamp...
            else {
                equalizer.setPreamp(value / 100f);
            }
            if(!applyingPreset) {
                presetComboBox.setSelectedItem(null);
            }
        }
    }

    @Override
    public final void itemStateChanged(ItemEvent e) {
        String presetName = (String) presetComboBox.getSelectedItem();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (presetName != null) {
                Equalizer presetEqualizer = mediaPlayerComponent.mediaPlayerFactory().equalizer().newEqualizer(presetName);
                if (presetEqualizer != null) {
                    applyingPreset = true;
                    preampControl.getSlider().setValue((int) (presetEqualizer.preamp() * 100f));
                    float[] amps = presetEqualizer.amps();
                    for (int i = 0; i < amps.length; i++) {
                        bandControls[i].getSlider().setValue((int) (amps[i] * 100f));
                    }
                    applyingPreset = false;
                }
            }
        }
    }
}
