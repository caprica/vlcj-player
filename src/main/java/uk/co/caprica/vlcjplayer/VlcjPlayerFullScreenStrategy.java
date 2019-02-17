package uk.co.caprica.vlcjplayer;

import static uk.co.caprica.vlcjplayer.Application.application;

import java.awt.Window;

import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;
import uk.co.caprica.vlcjplayer.event.AfterExitFullScreenEvent;
import uk.co.caprica.vlcjplayer.event.BeforeEnterFullScreenEvent;

final class VlcjPlayerFullScreenStrategy extends AdaptiveFullScreenStrategy {

    VlcjPlayerFullScreenStrategy(Window window) {
        super(window);
    }

    @Override
    protected void onBeforeEnterFullScreen() {
        application().post(BeforeEnterFullScreenEvent.INSTANCE);
    }

    @Override
    protected  void onAfterExitFullScreen() {
        application().post(AfterExitFullScreenEvent.INSTANCE);
    }

}
