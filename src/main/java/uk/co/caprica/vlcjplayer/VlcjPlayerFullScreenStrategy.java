package uk.co.caprica.vlcjplayer;

import static uk.co.caprica.vlcjplayer.Application.application;

import java.awt.Window;

import uk.co.caprica.vlcj.player.embedded.AdaptiveRuntimeFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.x.XFullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeType;
import uk.co.caprica.vlcjplayer.event.AfterExitFullScreenEvent;
import uk.co.caprica.vlcjplayer.event.BeforeEnterFullScreenEvent;

final class VlcjPlayerFullScreenStrategy extends AdaptiveRuntimeFullScreenStrategy {

    VlcjPlayerFullScreenStrategy(Window window) {
        setStrategy(RuntimeType.NIX, new NixFullScreenStrategy(window));
        setStrategy(RuntimeType.WINDOWS, new WindowsFullScreenStrategy(window));
        // FIXME Mac?
    }

    private class NixFullScreenStrategy extends XFullScreenStrategy {

        private NixFullScreenStrategy(Window window) {
            super(window);
        }

        @Override
        protected void onBeforeEnterFullScreenMode() {
            beforeEnterFullScreen();
        }

        @Override
        protected void onAfterExitFullScreenMode() {
            afterExitFullScreen();
        }
    }

    private class WindowsFullScreenStrategy extends Win32FullScreenStrategy {

        private WindowsFullScreenStrategy(Window window) {
            super(window);
        }

        @Override
        protected void onBeforeEnterFullScreenMode() {
            beforeEnterFullScreen();
        }

        @Override
        protected void onAfterExitFullScreenMode() {
            afterExitFullScreen();
        }
    }

    private void beforeEnterFullScreen() {
        application().post(BeforeEnterFullScreenEvent.INSTANCE);
    }

    private void afterExitFullScreen() {
        application().post(AfterExitFullScreenEvent.INSTANCE);
    }
}
