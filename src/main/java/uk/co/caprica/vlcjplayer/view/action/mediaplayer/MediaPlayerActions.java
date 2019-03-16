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

package uk.co.caprica.vlcjplayer.view.action.mediaplayer;

import com.google.common.collect.ImmutableList;
import uk.co.caprica.vlcj.player.base.AudioChannel;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcjplayer.view.action.Resource.resource;

// FIXME i think none of these actions need be public now?
//       the dynamic ones currently are unfortunately... for now... (e.g. videotrack)

// FIXME the play action here could listen to the mediaplayer and change its icon accordingly

public final class MediaPlayerActions {

    private final List<Action> playbackSpeedActions;
    private final List<Action> playbackSkipActions;
    private final List<Action> playbackChapterActions;
    private final List<Action> playbackControlActions;

    private final List<Action> audioStereoModeActions;
    private final List<Action> audioControlActions;

    private final List<Action> videoZoomActions;
    private final List<Action> videoAspectRatioActions;
    private final List<Action> videoCropActions;

    private final Action       playbackPlayAction;
    private final Action       playbackStopAction;

    private final Action       videoSnapshotAction;

    public MediaPlayerActions(MediaPlayer mediaPlayer) {
        playbackSpeedActions    = newPlaybackSpeedActions   (mediaPlayer);
        playbackSkipActions     = newPlaybackSkipActions    (mediaPlayer);
        playbackChapterActions  = newPlaybackChapterActions (mediaPlayer);
        playbackControlActions  = newPlaybackControlActions (mediaPlayer);
        audioStereoModeActions  = newAudioStereoModeActions (mediaPlayer);
        audioControlActions     = newAudioControlActions    (mediaPlayer);
        videoZoomActions        = newVideoZoomActions       (mediaPlayer);
        videoAspectRatioActions = newVideoAspectRatioActions(mediaPlayer);
        videoCropActions        = newVideoCropActions       (mediaPlayer);

        playbackPlayAction      = new PlayAction    (resource("menu.playback.item.play" ));
        playbackStopAction      = new StopAction    (resource("menu.playback.item.stop" ));
        videoSnapshotAction     = new SnapshotAction(resource("menu.video.item.snapshot"));
    }

    private List<Action> newPlaybackSpeedActions(MediaPlayer mediaPlayer) {
        List<Action> actions = new ArrayList<>();
        actions.add(new RateAction(resource("menu.playback.item.speed.item.x4"    ), 4f));
        actions.add(new RateAction(resource("menu.playback.item.speed.item.x2"    ), 2f));
        actions.add(new RateAction(resource("menu.playback.item.speed.item.normal"), 1f));
        actions.add(new RateAction(resource("menu.playback.item.speed.item./2"    ), 0.5f));
        actions.add(new RateAction(resource("menu.playback.item.speed.item./4"    ), 0.25f));
        return ImmutableList.copyOf(actions);
    }

    private List<Action> newPlaybackSkipActions(MediaPlayer mediaPlayer) {
        List<Action> actions = new ArrayList<>();
        actions.add(new SkipAction(resource("menu.playback.item.skipForward" ), 10000));
        actions.add(new SkipAction(resource("menu.playback.item.skipBackward"), -10000));
        return ImmutableList.copyOf(actions);
    }

    private List<Action> newPlaybackChapterActions(MediaPlayer mediaPlayer) {
        List<Action> actions = new ArrayList<>();
        actions.add(new PreviousChapterAction(resource("menu.playback.item.previousChapter")));
        actions.add(new NextChapterAction    (resource("menu.playback.item.nextChapter"    )));
        return ImmutableList.copyOf(actions);
    }

    private List<Action> newPlaybackControlActions(MediaPlayer mediaPlayer) {
        List<Action> actions = new ArrayList<>();
        actions.add(new PlayAction(resource("menu.playback.item.play")));
        actions.add(new StopAction(resource("menu.playback.item.stop")));
        return ImmutableList.copyOf(actions);
    }

    private List<Action> newAudioStereoModeActions(MediaPlayer mediaPlayer) {
        List<Action> actions = new ArrayList<>();
        actions.add(new StereoModeAction(resource("menu.audio.item.stereoMode.item.unset"     ), AudioChannel.UNSET));
        actions.add(new StereoModeAction(resource("menu.audio.item.stereoMode.item.stereo"    ), AudioChannel.STEREO));
        actions.add(new StereoModeAction(resource("menu.audio.item.stereoMode.item.left"      ), AudioChannel.LEFT));
        actions.add(new StereoModeAction(resource("menu.audio.item.stereoMode.item.right"     ), AudioChannel.RIGHT));
        actions.add(new StereoModeAction(resource("menu.audio.item.stereoMode.item.reverse"   ), AudioChannel.RSTEREO));
        actions.add(new StereoModeAction(resource("menu.audio.item.stereoMode.item.dolbys"    ), AudioChannel.DOLBYS));
        actions.add(new StereoModeAction(resource("menu.audio.item.stereoMode.item.headphones"), AudioChannel.HEADPHONES));
        actions.add(new StereoModeAction(resource("menu.audio.item.stereoMode.item.mono"      ), AudioChannel.MONO));
        return ImmutableList.copyOf(actions);
    }

    private List<Action> newAudioControlActions(MediaPlayer mediaPlayer) {
        List<Action> actions = new ArrayList<>();
        actions.add(new VolumeAction(resource("menu.audio.item.increaseVolume"),  10));
        actions.add(new VolumeAction(resource("menu.audio.item.decreaseVolume"), -10));
        actions.add(new MuteAction  (resource("menu.audio.item.mute"          )           ));
        return ImmutableList.copyOf(actions);
    }

    private List<Action> newVideoZoomActions(MediaPlayer mediaPlayer) {
        List<Action> actions = new ArrayList<>();
        actions.add(new ZoomAction(resource("menu.video.item.zoom.item.quarter" ), 0.25f));
        actions.add(new ZoomAction(resource("menu.video.item.zoom.item.half"    ), 0.50f));
        actions.add(new ZoomAction(resource("menu.video.item.zoom.item.original"), 1.00f));
        actions.add(new ZoomAction(resource("menu.video.item.zoom.item.double"  ), 2.00f));
        // FIXME maybe need a zoom default of 0.0 (or is this just fit window?)
        return ImmutableList.copyOf(actions);
    }

    private List<Action> newVideoAspectRatioActions(MediaPlayer mediaPlayer) {
        List<Action> actions = new ArrayList<>();
        actions.add(new AspectRatioAction(resource("menu.video.item.aspectRatio.item.default"), null));
        actions.add(new AspectRatioAction("16:9"  , "16:9"   ));
        actions.add(new AspectRatioAction("4:3"   , "4:3"    ));
        actions.add(new AspectRatioAction("1:1"   , "1:1"    ));
        actions.add(new AspectRatioAction("16:10" , "16:10"  ));
        actions.add(new AspectRatioAction("2.21:1", "221:100"));
        actions.add(new AspectRatioAction("2.35:1", "235:100"));
        actions.add(new AspectRatioAction("2.39:1", "239:100"));
        actions.add(new AspectRatioAction("5:4"   , "5:4"    ));
        return ImmutableList.copyOf(actions);
    }

    private List<Action> newVideoCropActions(MediaPlayer mediaPlayer) {
        List<Action> actions = new ArrayList<>();
        actions.add(new CropAction(resource("menu.video.item.crop.item.default"), null));
        actions.add(new CropAction("16:10" , "16:10"  ));
        actions.add(new CropAction("16:9"  , "16:9"   ));
        actions.add(new CropAction("4:3"   , "4:3"    ));
        actions.add(new CropAction("1.85:1", "185:100"));
        actions.add(new CropAction("2.21:1", "221:100"));
        actions.add(new CropAction("2.35:1", "235:100"));
        actions.add(new CropAction("2.39:1", "239:100"));
        actions.add(new CropAction("5:3"   , "5:3"    ));
        actions.add(new CropAction("5:4"   , "5:4"    ));
        actions.add(new CropAction("1:1"   , "1:1"    ));
        return ImmutableList.copyOf(actions);
    }

    public List<Action> playbackSpeedActions() {
        return playbackSpeedActions;
    }

    public List<Action> playbackSkipActions() {
        return playbackSkipActions;
    }

    public List<Action> playbackChapterActions() {
        return playbackChapterActions;
    }

    public List<Action> playbackControlActions() {
        return playbackControlActions;
    }

    public List<Action> audioStereoModeActions() {
        return audioStereoModeActions;
    }

    public List<Action> audioControlActions() {
        return audioControlActions;
    }

    public List<Action> videoZoomActions() {
        return videoZoomActions;
    }

    public List<Action> videoAspectRatioActions() {
        return videoAspectRatioActions;
    }

    public List<Action> videoCropActions() {
        return videoCropActions;
    }

    public Action playbackPlayAction() {
        return playbackPlayAction;
    }

    public Action playbackStopAction() {
        return playbackStopAction;
    }

    public Action videoSnapshotAction() {
        return videoSnapshotAction;
    }

}
