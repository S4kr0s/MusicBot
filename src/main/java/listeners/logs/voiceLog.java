package listeners.logs;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class voiceLog extends ListenerAdapter {

    private String LogChannel = STATIC.VOICELOGCHANNEL;

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        event.getGuild().getTextChannelsByName(LogChannel, true).get(0).sendMessage(
                new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setAuthor(event.getMember().getUser().getName(), null, event.getMember().getUser().getAvatarUrl())
                        .setDescription("`" + event.getChannelJoined().getName() + "` beigetreten.")
                        .build()
        ).queue();
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event){
        event.getGuild().getTextChannelsByName(LogChannel, true).get(0).sendMessage(
                new EmbedBuilder()
                        .setColor(Color.YELLOW)
                        .setAuthor(event.getMember().getUser().getName(), null, event.getMember().getUser().getAvatarUrl())
                        .setDescription("Ging von `" + event.getChannelLeft().getName() + "` zu `" + event.getChannelJoined().getName() + "` durch `")
                        .build()
        ).queue();
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
        event.getGuild().getTextChannelsByName(LogChannel, true).get(0).sendMessage(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setAuthor(event.getMember().getUser().getName(), null, event.getMember().getUser().getAvatarUrl())
                        .setDescription("`" + event.getChannelLeft().getName() + "` verlassen.")
                        .build()
        ).queue();
    }
}
