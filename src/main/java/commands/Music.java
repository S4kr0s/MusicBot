package commands;

import audioCore.AudioInfo;
import audioCore.PlayerSendHandler;
import audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Music implements Command {


    private static final int PLAYLIST_LIMIT = 10;
    private static Guild guild;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();

    public Music() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }

    private AudioPlayer createPlayer(Guild g) {
        AudioPlayer p = MANAGER.createPlayer();
        TrackManager m = new TrackManager(p);
        p.addListener(m);

        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(p));

        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));

        return p;
    }
    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }
    private AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g).getKey();
        else
            return createPlayer(g);
    }
    private TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }
    private boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }

    private void loadTrack(String identifier, Member author, Message msg) {

        Guild guild = author.getGuild();
        getPlayer(guild);

        MANAGER.setFrameBufferDuration(5000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    getManager(guild).queue(playlist.getTracks().get(i), author);
                }

                /*
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {

                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                }
                 */


            }
            @Override
            public void noMatches() {
            }
            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });

    }

    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }

    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }
    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }
    private void sendErrorMsg(MessageReceivedEvent event, String content) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.orange)
                        .setTitle("Hilfemenü", "https://wwww.youtube.com/c/Sakros")
                        .setDescription("Befehlsübersicht zur Musik:")
                        .addBlankField(false)
                        .addField("-m play <link>","Das Lied wird in die Warteschlange aufgenommen." , false)
                        .addField("-m skip","Überspringt das aktuelle Lied." , false)
                        .addField("-m queue", "Zeigt dir die Warteschlange an.", false)
                        .addField("-m info", "Gibt dir verschiedene Information zur Musik.", false)
                        .addField("-m volume <1-100>", "Stellt die Lautstärke des Bots ein.", false)
                        .addField("-m purgequeue", "Lösche die Warteschlange.", false)
                        .build()
        ).queue();
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {


        guild = event.getGuild();

        if (args.length < 1) {
            sendErrorMsg(event, help());
            return;
        }

        switch (args[0].toLowerCase()) {

            case "play":
            case "p":
            case "abspielen":

                if (args.length < 2) {
                    sendErrorMsg(event, "Bitte füge einen Link hinzu!");
                    return;
                }

                String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                if (!(input.startsWith("http://") || !(input.startsWith("https://")))){
                    input = "ytsearch: " + input;
                }

                loadTrack(input, event.getMember(), event.getMessage());

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        event.getTextChannel().sendMessage(
                                new EmbedBuilder()
                                        .setColor(Color.green)
                                        .addField("Danke " + event.getMessage().getAuthor().getName() + "!", "Das Lied wurde erfolgreich in die Warteschlange gesetzt!", false)
                                        //.addField("Lied:", getPlayer(guild).getPlayingTrack().getInfo().title, false)
                                        .build()
                        ).queue();
                    }
                }, 3500);
                break;


            case "starwars":
            case "sw":
                String SWinput = "https://www.youtube.com/watch?v=_D0ZQPqeJkk";
                loadTrack(SWinput, event.getMember(), event.getMessage());

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        event.getTextChannel().sendMessage(
                                new EmbedBuilder()
                                        .setColor(Color.green)
                                        .addField("Danke " + event.getMessage().getAuthor().getName() + "!", "Das Lied wurde erfolgreich in die Warteschlange gesetzt!", false)
                                        .addField("Lied:", getPlayer(guild).getPlayingTrack().getInfo().title, false)
                                        .build()
                        ).queue();
                    }
                }, 3500);
                break;

            case "volume":
                String newVolume = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);
                int newVolumeInt = Integer.parseInt(newVolume);

                if(newVolumeInt <= 100 && newVolumeInt >= 1){
                    getPlayer(guild).setVolume(newVolumeInt);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            event.getTextChannel().sendMessage(
                                    new EmbedBuilder()
                                            .setColor(Color.green)
                                            .addField("Okay " + event.getMessage().getAuthor().getName() + "!", "Die Lautstärke wurde auf " + newVolumeInt + "% verändert.", false)
                                            .build()
                            ).queue();
                        }
                    }, 3500);
                }else{
                    event.getTextChannel().sendMessage("Fehler! Gib bitte eine Nummer zwischen 1(%) und 100(%) an!").queue();
                }
                break;
            case "pause":
                if(getPlayer(guild).getPlayingTrack() != null){
                    if(!getPlayer(guild).isPaused()){
                        getPlayer(guild).setPaused(true);
                    }else{
                        getPlayer(guild).setPaused(false);
                    }
                }else{
                    event.getTextChannel().sendMessage("Fehler! Es wird gerade kein Track gespielt der Pausiert werden kann.").queue();
                }

                break;
            case "skip":
            case "s":

                if (isIdle(guild)) return;
                for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i == 1; i--) {
                    skip(guild);
                }

                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.ORANGE)
                                .addField("Okay", "Das aktuelle Lied wurde übersprungen!", false)
                                .build()
                ).queue();

                break;


            case "stop":

                if (isIdle(guild)) return;
                getManager(guild).purgeQueue();
                skip(guild);
                guild.getAudioManager().closeAudioConnection();

                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .addField("Okay", "Die Musikwiedergabe wurde gestoppt!", false)
                                .build()
                ).queue();

                break;


            case "random":

                if (isIdle(guild)) return;
                getManager(guild).shuffleQueue();

                break;

            case "info":

                if (isIdle(guild)) return;

                AudioTrack track1 = getPlayer(guild).getPlayingTrack();
                AudioTrackInfo info1 = track1.getInfo();


                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setDescription("**Aktuelles Lied:**")
                                .addField("Titel:", info1.title, false)
                                .addField("Artist:", info1.author, false)
                                .addField("Dauer:", "`[ " + getTimestamp(track1.getPosition()) + "/ " + getTimestamp(track1.getDuration()) + " ]`", false)
                                .build()
                ).queue();

                break;

            case "queue":
                if (isIdle(guild)) return;
                int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;

                List<String> tracks = new ArrayList<>();
                List<String> trackSublist;

                getManager(guild).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                if (tracks.size() > 20)
                    trackSublist = tracks.subList((sideNumb-1)*20, (sideNumb-1)*20+20);
                else

                    trackSublist = tracks;
                String out = trackSublist.stream().collect(Collectors.joining("\n"));
                int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;
                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setDescription(
                                        "**AKTUELLE PLAYLIST:**\n" +
                                                "*[" + getManager(guild).getQueue().stream().count() + " Tracks | Seite " + sideNumb + " / " + sideNumbAll + "]*" +
                                                out
                                )
                                .build()
                ).queue();
                break;

            case "purgequeue":
                if (isIdle(guild)) return;
                getManager(guild).purgeQueue();
                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .addField("Okay!", "Die Queue wurde geleert.", false)
                                .build()
                ).queue();
                break;
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
    }

    @Override
    public String help() {
        return null;
    }
}