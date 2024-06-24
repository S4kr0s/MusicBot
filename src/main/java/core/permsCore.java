package core;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;
import java.util.Arrays;

public class permsCore {

   public static int check(MessageReceivedEvent event) {

       for ( Role r : event.getGuild().getMember(event.getAuthor()).getRoles() ) {
           if (Arrays.stream(STATIC.FULLPERMISSIONS).parallel().anyMatch(r.getName()::contains))
               return 2;
           else if (Arrays.stream(STATIC.PERMISSIONS).parallel().anyMatch(r.getName()::contains))
               return 1;
           else
               event.getTextChannel().sendMessage("⚠ Du hast leider nicht die nötige Berechtigung dafür. " + event.getAuthor().getAsMention()).queue();
       }
       return 0;
   }


}
