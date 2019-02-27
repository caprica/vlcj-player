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

package uk.co.caprica.vlcjplayer.view.debug;

import static uk.co.caprica.vlcjplayer.Application.application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcjplayer.event.ShowDebugEvent;
import uk.co.caprica.vlcjplayer.view.BaseFrame;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;

import com.google.common.eventbus.Subscribe;

@SuppressWarnings("serial")
public final class DebugFrame extends BaseFrame {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final EventList<DebugMessage> eventList;

    private final AdvancedTableModel<DebugMessage> eventTableModel;

    private final Action clearAction;

    private final JButton clearButton;

    private final JTable table;
    private final JScrollPane scrollPane;

    private final MouseAdapter mouseEventHandler;

    public DebugFrame() {
        super("Debug Messages");
        this.mediaPlayerComponent = application().mediaPlayerComponent();
        this.mouseEventHandler = new MouseEventHandler();
        this.mediaPlayerComponent.videoSurfaceComponent().addMouseListener(mouseEventHandler);
        this.mediaPlayerComponent.videoSurfaceComponent().addMouseMotionListener(mouseEventHandler);
        this.mediaPlayerComponent.videoSurfaceComponent().addMouseWheelListener(mouseEventHandler);

        this.mediaPlayerComponent.videoSurfaceComponent().addKeyListener(new KeyEventHandler());

        this.clearAction = new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventList.clear();
            }
        };

        clearButton = new JButton();
        clearButton.setAction(clearAction);

        JPanel controlsPane = new JPanel();
        controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.X_AXIS));
        controlsPane.add(Box.createHorizontalGlue());
        controlsPane.add(clearButton);

        this.eventList = new BasicEventList<>();
        this.eventTableModel = GlazedListsSwing.eventTableModelWithThreadProxyList(eventList, new DebugMessageTableFormat());

        table = new JTable();
        table.setModel(eventTableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(table);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controlsPane, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        applyPreferences();
    }

    @Override
    public void dispose() {
        this.mediaPlayerComponent.videoSurfaceComponent().removeMouseListener(mouseEventHandler);
        this.mediaPlayerComponent.videoSurfaceComponent().removeMouseMotionListener(mouseEventHandler);
        this.mediaPlayerComponent.videoSurfaceComponent().removeMouseWheelListener(mouseEventHandler);
    }

    private class MouseEventHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            addMessage(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            addMessage(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            addMessage(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            addMessage(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            addMessage(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            addMessage(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            addMessage(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            addMessage(e);
        }
    }

    private class KeyEventHandler extends KeyAdapter {

        @Override
        public void keyTyped(KeyEvent e) {
            addMessage(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            addMessage(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            addMessage(e);
        }
    }

    private void addMessage(InputEvent message) {
        addMessage(message != null ? message.toString() : "EVENT WAS NULL");
    }

    private void addMessage(String message) {
        int bra = message.indexOf('[');
        int ket = message.indexOf(']');
        if (bra != -1 && ket != -1) {
            message = message.substring(bra+1, ket);
        }
        eventList.add(new DebugMessage(message));
        int lastRow = table.convertRowIndexToView(table.getModel().getRowCount()-1);
        table.scrollRectToVisible(table.getCellRect(lastRow, 0, true));
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(DebugFrame.class);
        setBounds(
            prefs.getInt("frameX"     , 300),
            prefs.getInt("frameY"     , 300),
            prefs.getInt("frameWidth" , 500),
            prefs.getInt("frameHeight", 300)
        );
    }

    @Override
    protected void onShutdown() {
        if (wasShown()) {
            Preferences prefs = Preferences.userNodeForPackage(DebugFrame.class);
            prefs.putInt("frameX"     , getX     ());
            prefs.putInt("frameY"     , getY     ());
            prefs.putInt("frameWidth" , getWidth ());
            prefs.putInt("frameHeight", getHeight());
        }
    }

    @Subscribe
    public void onShowMessages(ShowDebugEvent event) {
        setVisible(true);
    }
}
