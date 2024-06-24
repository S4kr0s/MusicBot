package listeners.actionListener;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;

public class giveawayReactionAction extends ListenerAdapter{

    public void onMessageReactionAdd(MessageReactionAddEvent event){

        Message message = event.getTextChannel().getMessageById(event.getTextChannel().getLatestMessageId()).complete();

        message.addReaction(event.getGuild().getEmotesByName("\uD83C\uDF89", true).get(0)).queue();

        if(event.getTextChannel().equals(event.getGuild().getTextChannelsByName("giveaway", true).get(0))) {
            if(event.getReactionEmote().equals(event.getGuild().getEmotesByName("\uD83C\uDF89", true).get(0))) {
                event.getTextChannel().sendMessage(event.getMember().getNickname()).queue();

            } else {
                // Nothin
            }
        } else {
            // Nothin
        }
    }
}
