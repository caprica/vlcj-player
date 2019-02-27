package uk.co.caprica.vlcjplayer.event;

import uk.co.caprica.vlcj.player.renderer.RendererItem;

public final class RendererAddedEvent {

    private final RendererItem rendererItem;

    public RendererAddedEvent(RendererItem rendererItem) {
        this.rendererItem = rendererItem;
    }

    public RendererItem rendererItem() {
        return rendererItem;
    }

}
