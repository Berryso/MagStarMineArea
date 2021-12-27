package xyz.magstar.minearea;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabHandler implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        if (!(sender instanceof Player)){
            return null;
        }
        else if (args.length >= 2){
            return null;
        }
        else if (args.length == 1 && args[0].equals("")){
            List com = new ArrayList();
            com.add("help");
            com.add("reload");
            com.add("drop");
            com.add("job");
            return com;
        }
        else if (args.length == 1 &&args[0].equals("h")){
            return Collections.singletonList("help");
        }
        else if (args.length == 1 &&args[0].equals("he")){
            return Collections.singletonList("help");
        }
        else if (args.length == 1 &&args[0].equals("hel")){
            return Collections.singletonList("help");
        }
        else if (args.length == 1 &&args[0].equals("help")){
            return Collections.singletonList("help");
        }
        else if (args.length == 1 && args[0].equals("r")){
            return Collections.singletonList("reload");
        }
        else if (args.length == 1 && args[0].equals("re")){
            return Collections.singletonList("reload");
        }
        else if (args.length == 1 && args[0].equals("rel")){
            return Collections.singletonList("reload");
        }
        else if (args.length == 1 && args[0].equals("relo")){
            return Collections.singletonList("reload");
        }
        else if (args.length == 1 && args[0].equals("reloa")){
            return Collections.singletonList("reload");
        }
        else if (args.length == 1 && args[0].equals("reload")){
            return Collections.singletonList("reload");
        }
        else if (args.length == 1 && args[0].equals("j")){
            return Collections.singletonList("job");
        }
        else if (args.length == 1 && args[0].equals("jo")){
            return Collections.singletonList("job");
        }
        else if (args.length == 1 && args[0].equals("job")){
            return Collections.singletonList("job");
        }
        else if (args.length == 1 && args[0].equals("d")){
            return Collections.singletonList("drop");
        }
        else if (args.length == 1 && args[0].equals("dr")){
            return Collections.singletonList("drop");
        }
        else if (args.length == 1 && args[0].equals("dro")){
            return Collections.singletonList("drop");
        }
        else if (args.length == 1 && args[0].equals("drop")){
            return Collections.singletonList("drop");
        }
        return null;
    }
}
