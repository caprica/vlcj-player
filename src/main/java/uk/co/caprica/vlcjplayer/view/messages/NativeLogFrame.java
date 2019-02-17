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

import static uk.co.caprica.vlcjplayer.Application.resources;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.log.LogLevel;
import uk.co.caprica.vlcj.log.LogEventListener;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcjplayer.event.ShowMessagesEvent;
import uk.co.caprica.vlcjplayer.view.BaseFrame;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;

import com.google.common.eventbus.Subscribe;

@SuppressWarnings("serial")
public final class NativeLogFrame extends BaseFrame implements LogEventListener {

    private final NativeLog nativeLog;

    private final EventList<NativeLogMessage> eventList;

    private final AdvancedTableModel<NativeLogMessage> eventTableModel;

    private final Action clearAction;
    private final Action saveAsAction;

    private final JButton clearButton;

    private final JLabel levelLabel;
    private final JComboBox<LogLevel> levelComboBox;
    private final JLabel filterLabel;
    private final JTextField filterTextField;
    private final JButton saveAsButton;

    private final JTable table;
    private final JScrollPane scrollPane;

    public NativeLogFrame(NativeLog nativeLog) {
        super(resources().getString("dialog.messages"));

        this.nativeLog = nativeLog;

        this.eventList = new BasicEventList<>();
        this.eventTableModel = GlazedListsSwing.eventTableModelWithThreadProxyList(eventList, new NativeLogTableFormat());

        // FIXME Resource
        clearAction = new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventList.clear(); // FIXME this definitely glitched once when messing about with verbosity combo then clearing (some items were left in table until table resized)
            }
        };

        saveAsAction = new AbstractAction("Save as...") {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };

        // FIXME proper Resource with mnemonic etc

        clearButton = new JButton();
        clearButton.setAction(clearAction);

        levelLabel = new JLabel();
        levelLabel.setText(resources().getString("dialog.messages.level"));

        levelComboBox = new JComboBox<>();
        levelComboBox.setModel(new LogLevelComboBoxModel());

        levelLabel.setLabelFor(levelComboBox);

        filterLabel = new JLabel();
        filterLabel.setText(resources().getString("dialog.messages.filter"));

        filterTextField = new JTextField();

        filterLabel.setLabelFor(filterTextField);

        saveAsButton = new JButton();
        saveAsButton.setAction(saveAsAction);

        JPanel topPane = new JPanel();
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.X_AXIS));
        topPane.add(Box.createHorizontalGlue());
        topPane.add(clearButton);

        JPanel controlsPane = new JPanel(new MigLayout("fill", "[]rel[]rel[][grow][]", ""));
        controlsPane.add(levelLabel);
        controlsPane.add(levelComboBox);
        controlsPane.add(filterLabel);
        controlsPane.add(filterTextField, "grow");
        controlsPane.add(saveAsButton);

        table = new JTable();
        table.setModel(eventTableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(table);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(topPane, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(controlsPane, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // FIXME use filter with glazed lists

        levelComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    LogLevel level = (LogLevel) event.getItem();
                    nativeLog.setLevel(level);
                }
            }
        });

        applyPreferences();

        this.nativeLog.addLogListener(this);
    }

    @Override
    public void log(final LogLevel level, final String module, final String file, final Integer line, final String name, final String header, final Integer id, final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                eventList.add(new NativeLogMessage(module, name, level, message));
                int lastRow = table.convertRowIndexToView(table.getModel().getRowCount()-1);
                table.scrollRectToVisible(table.getCellRect(lastRow, 0, true));
            }
        });
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(NativeLogFrame.class);
        setBounds(
            prefs.getInt("frameX"     , 200),
            prefs.getInt("frameY"     , 200),
            prefs.getInt("frameWidth" , 600),
            prefs.getInt("frameHeight", 400)
        );
        table.getColumnModel().getColumn(0).setPreferredWidth(prefs.getInt("column0Width", 100));
        table.getColumnModel().getColumn(1).setPreferredWidth(prefs.getInt("column1Width", 100));
        table.getColumnModel().getColumn(2).setPreferredWidth(prefs.getInt("column2Width", 100));
        table.getColumnModel().getColumn(3).setPreferredWidth(prefs.getInt("column3Width", 250));
        LogLevel level = LogLevel.level(prefs.getInt("level", LogLevel.ERROR.intValue()));
        levelComboBox.setSelectedItem(level);
        nativeLog.setLevel(level);
    }

    @Override
    protected void onShutdown() {
        nativeLog.removeLogListener(this);
        if (wasShown()) {
            Preferences prefs = Preferences.userNodeForPackage(NativeLogFrame.class);
            prefs.putInt("frameX"      , getX     ());
            prefs.putInt("frameY"      , getY     ());
            prefs.putInt("frameWidth"  , getWidth ());
            prefs.putInt("frameHeight" , getHeight());
            prefs.putInt("column0Width", table.getColumnModel().getColumn(0).getWidth());
            prefs.putInt("column1Width", table.getColumnModel().getColumn(1).getWidth());
            prefs.putInt("column2Width", table.getColumnModel().getColumn(2).getWidth());
            prefs.putInt("column3Width", table.getColumnModel().getColumn(3).getWidth());
            prefs.putInt("level"       , nativeLog.getLevel().intValue());
        }
    }

    @Subscribe
    public void onShowMessages(ShowMessagesEvent event) {
        setVisible(true);
    }
}
