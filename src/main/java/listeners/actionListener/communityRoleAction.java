package listeners.actionListener;

import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;

public class communityRoleAction extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event){

        MessageReaction.ReactionEmote userEmote = event.getReactionEmote();
        String reactionChannelId = STATIC.welcomeChannelId;
        String eventChannelId = event.getChannel().getId();
        User eventUser = event.getUser();

        if(eventChannelId.equals(reactionChannelId)){
            if(!event.getMember().getRoles().contains(event.getGuild().getRoleById(STATIC.communityRoleId))){
                event.getGuild().getController().addSingleRoleToMember(event.getGuild().getMemberById(eventUser.getId()), event.getGuild().getRoleById(STATIC.communityRoleId)).queue();
            }
        }
    }
}