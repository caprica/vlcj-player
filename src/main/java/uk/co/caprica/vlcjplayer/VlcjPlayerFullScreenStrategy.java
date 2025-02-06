package uk.co.caprica.vlcjplayer;

import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;
import uk.co.caprica.vlcjplayer.event.AfterExitFullScreenEvent;
import uk.co.caprica.vlcjplayer.event.BeforeEnterFullScreenEvent;

import java.awt.*;

import static uk.co.caprica.vlcjplayer.Application.application;

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
