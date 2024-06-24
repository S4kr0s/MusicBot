package listeners.logs;

import core.Main;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;

import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;

public class commandsLog extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        event.getGuild().getController().addSingleRoleToMember(event.getMember(), event.getGuild().getRoleById(STATIC.MEMBERROLEID)).complete();
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        event.getGuild().getTextChannelById(STATIC.CHATTEXTCHANNEL).sendMessage(event.getMember().getNickname() + " hat uns leider verlassen.").queue();
    }
}
