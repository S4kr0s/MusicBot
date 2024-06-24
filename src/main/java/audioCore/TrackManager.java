package audioCore;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import util.STATIC;

import java.awt.*;
import java.sql.Time;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter {

    Timer timer;

    private final AudioPlayer PLAYER;
    private final Queue<AudioInfo> queue;
    private static int count = 0;
    private String titleBeforeCheck = null;
    private String titleAfterCheck = null;

    /**
     * Erstellt eine Instanz der Klasse TrackManager.
     * @param player
     */
    public TrackManager(AudioPlayer player) {
        this.PLAYER = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Reiht den übergebenen Track in die Queue ein.
     * @param track AudioTrack
     * @param author Member, der den Track eingereiht hat
     */
    public void queue(AudioTrack track, Member author) {
        AudioInfo info = new AudioInfo(track, author);
        queue.add(info);

        if (PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(track);
        }
    }

    /**
     * Returnt die momentane Queue als LinkedHashSet.
     * @return Queue
     */
    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    /**
     * Returnt AudioInfo des Tracks aus der Queue.
     * @param track AudioTrack
     * @return AudioInfo
     */
    public AudioInfo getInfo(AudioTrack track) {
        return queue.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);
    }

    /**
     * Leert die gesammte Queue.
     */
    public void purgeQueue() {
        queue.clear();
    }

    /**
     * Shufflet die momentane Queue.
     */
    public void shuffleQueue() {
        List<AudioInfo> cQueue = new ArrayList<>(getQueue());
        AudioInfo current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        queue.addAll(cQueue);
    }

    /**
     * PLAYER EVENT: TRACK STARTET
     * Wenn Einreiher nicht im VoiceChannel ist, wird der Player gestoppt.
     * Sonst connectet der Bot in den Voice Channel des Einreihers.
     * @param player AudioPlayer
     * @param track AudioTrack
     */

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {

        AudioInfo info = queue.element();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();

        if (vChan == null)
            player.stopTrack();
        else
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);

        /*
        AudioInfo info = queue.element();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();
        String ChannelID = info.getAuthor().getGuild().getTextChannelsByName("music", false).get(0).getId();
        long duration = (track.getDuration() / 8);
        titleBeforeCheck = info.getTrack().getInfo().title;
        titleAfterCheck = null;
        String NoPlayingTrack = ":pause_button:  Aktuell wird kein Track abgespielt!";
        String PlayingTrack = ":arrow_forward: spielt folgenden Track: **" + track.getInfo().title + "** von **" + info.getAuthor().getAsMention() + "**.";
        if (vChan == null) {

            info.getAuthor().getGuild().getTextChannelsByName("music", false).get(0).getManager().setTopic(NoPlayingTrack).queue();
            player.stopTrack();

        }else{

            titleAfterCheck = info.getTrack().getInfo().title;
            titleBeforeCheck = info.getTrack().getInfo().title;

            EmbedBuilder ActualTrack = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setDescription("Track gestartet: `" + track.getInfo().title + "` von `" + track.getInfo().author + "` durch **" + info.getAuthor().getAsMention() + "**");

            info.getAuthor().getGuild().getTextChannelsByName(STATIC.MUSICCHANNEL, false).get(0).getManager().setTopic(PlayingTrack).queue();
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);

            info.getAuthor().getGuild().getTextChannelById(ChannelID).sendMessage(ActualTrack.build()).queue();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    count++;
                    if (track.getPosition() >= duration * 8 || info.getTrack().getState().equals(AudioTrackState.FINISHED) || !titleBeforeCheck.equals(titleAfterCheck)) {
                        timer.cancel();
                    }else{

                        titleBeforeCheck = info.getTrack().getInfo().title;

                        System.out.print("\n" + info.getTrack().getState().toString());
                        System.out.print("\n" + info.getTrack());

                        String ausgabe = "";
                        String testausgabe = "";
                        long duration = (track.getDuration() / 8);

                        if (track.getPosition() <= duration)
                            ausgabe = ":radio_button:▬▬▬▬▬▬▬";
                        else if (track.getPosition() <= duration * 2)
                            ausgabe = "▬:radio_button:▬▬▬▬▬▬";
                        else if (track.getPosition() <= duration * 3)
                            ausgabe = "▬▬:radio_button:▬▬▬▬▬";
                        else if (track.getPosition() <= duration * 4)
                            ausgabe = "▬▬▬:radio_button:▬▬▬▬";
                        else if (track.getPosition() <= duration * 5)
                            ausgabe = "▬▬▬▬:radio_button:▬▬▬";
                        else if (track.getPosition() <= duration * 6)
                            ausgabe = "▬▬▬▬▬:radio_button:▬▬";
                        else if (track.getPosition() <= duration * 7)
                            ausgabe = "▬▬▬▬▬▬:radio_button:▬";
                        else if (track.getPosition() < duration * 8)
                            ausgabe = "▬▬▬▬▬▬▬:radio_button:";
                        else if (track.getPosition() >= duration * 8)
                            ausgabe = "Track erfolgreich beendet.";

                        if (!ausgabe.equals(testausgabe)){
                            info.getAuthor().getGuild().getTextChannelsByName(STATIC.MUSICCHANNEL, false).get(0).getManager().setTopic(ausgabe).queue();

                            testausgabe = ausgabe;
                        }
                    }
                }
            }, 0, 500);
            System.out.print("FINISHED!");
        }
        */


    }

    /**
     * PLAYER EVENT: TRACK ENDE
     * Wenn die Queue zuende ist, verlässt der Bot den Audio Channel.
     * Sonst wird der nächste Track in der Queue wiedergegeben.
     * @param player
     * @param track
     * @param endReason
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        AudioInfo info = queue.element();
        String NoPlayingTrack = ":pause_button:  Aktuell wird kein Track abgespielt!";
        String PlayingTrack = ":arrow_forward: spielt folgenden Track: **" + track.getInfo().title + "** von **" + info.getAuthor().getAsMention() + "**.";
        Guild g = queue.poll().getAuthor().getGuild();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (queue.isEmpty())
                    g.getAudioManager().closeAudioConnection();

            }
        };
        if (queue.isEmpty()) {
            Timer timer = new Timer();
            timer.schedule(timerTask ,1000,1000);
            info.getAuthor().getGuild().getTextChannelsByName("music", false).get(0).getManager().setTopic(NoPlayingTrack).queue();
        }
        else
            player.playTrack(queue.element().getTrack());
            info.getAuthor().getGuild().getTextChannelsByName("music", false).get(0).getManager().setTopic(PlayingTrack).queue();
        return;
    }


}
