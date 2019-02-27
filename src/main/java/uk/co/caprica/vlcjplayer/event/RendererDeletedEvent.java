package uk.co.caprica.vlcjplayer.event;

import uk.co.caprica.vlcj.player.renderer.RendererItem;

public final class RendererDeletedEvent {

    private final RendererItem rendererItem;

    public RendererDeletedEvent(RendererItem rendererItem) {
        this.rendererItem = rendererItem;
    }

    public RendererItem rendererItem() {
        return rendererItem;
    }

}
