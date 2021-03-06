package gri.ppt.DirtyServerControl.command;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import gri.ppt.DirtyServerControl.DirtyServerControl;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.CoreProtectAPI.ParseResult;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AntiXrayCheck implements CommandExecutor{
    
    public DirtyServerControl plugin;
    
    public AntiXrayCheck(DirtyServerControl plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args) {
        if(!(sender instanceof Player)) return false;
        if(args.length!=3) return false;
        if(!sender.isOp()||!sender.hasPermission("dirtyservercontrol.antixray.use")){
            sender.sendMessage("你没有权限这么做.");
            return false;
        }
        if(label.equalsIgnoreCase("dsc")){
            if(!isInteger(args[2])){
                sender.sendMessage("/dsc xray <name> <days> 请给出时间,单位为天.");
                return false;
            }
            else if(!args[0].equalsIgnoreCase("xray")){
                sender.sendMessage("/dsc xray <name> <days> 请给出玩家名.");
                return false;
            }
            else{
                Player ps = (Player) sender;
                AXC_cmd(ps,args[1],args[2]);
                return true;
            }
        }
        return false;
    }
    
    private CoreProtectAPI getCoreProtect(){
        Plugin cp = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");
        if(cp == null ||!(cp instanceof CoreProtect)){
            return null;
        }
        CoreProtectAPI cpapi = ((CoreProtect)cp).getAPI();
        if(!cpapi.isEnabled()){
            return null;
        }
        if(cpapi.APIVersion()< 2){
            return null;
        }
        return cpapi;
    }
    
    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }
    
    private String AXC_msg(ChatColor color,String name,int num,int under){
        return color+name+": "+num+" "+"  |  "+new DecimalFormat("#0.00").format((double)num/(double)under*100)+"%";
    }
    
    public boolean AXC_cmd(Player p,String playername,String time){

        List<Object> oresList = asList(Material.STONE, Material.GOLD_ORE, 
                                                Material.IRON_ORE, Material.COAL_ORE,
                                                Material.LAPIS_ORE, Material.DIAMOND_ORE,
                                                Material.REDSTONE_ORE, Material.GLOWING_REDSTONE_ORE,
                                                Material.EMERALD_ORE);
        
        int Diamonds = 0, Iron = 0, Gold = 0, Redstone = 0, Coal = 0, Emerald = 0, Stone = 0, Lapis = 0;
        CoreProtectAPI CPapi = getCoreProtect();
        List<String[]> lookup = CPapi.performLookup(Integer.parseInt(time)*86400, Arrays.asList(playername), null, oresList, null, Arrays.asList(0), 0, null);

        int total = lookup.size();
        for (String[] value : lookup){
        	
            ParseResult result = CPapi.parseResult(value);
            if(result.getActionId() == 0)
            switch(result.getType()){
                case STONE : Stone++; break;
                case GOLD_ORE : Gold++; break;
                case IRON_ORE : Iron ++; break;
                case COAL_ORE : Coal++; break;
                case LAPIS_ORE : Lapis++; break;
                case DIAMOND_ORE : Diamonds++; break;
                case REDSTONE_ORE : Redstone++; break;
                case GLOWING_REDSTONE_ORE : Redstone++; break;
                case EMERALD_ORE : Emerald++; break;
			default:
				break;
            }

        }
        int under = Stone+Diamonds+Gold+Iron+Coal+Lapis+Emerald+Redstone;
        p.sendMessage(ChatColor.DARK_PURPLE+"--------------------------------");
        p.sendMessage(ChatColor.YELLOW+"          DSC反矿追");
        p.sendMessage(ChatColor.YELLOW+"--------------------------------");
        p.sendMessage(ChatColor.BLUE+"玩家  "+ playername +":");
        p.sendMessage(ChatColor.YELLOW+"--------------------------------");
        p.sendMessage("总计: "+total);
        p.sendMessage("矿物总计: " + under);
        p.sendMessage(AXC_msg(ChatColor.GREEN, "绿宝石", Emerald, under));
        p.sendMessage(AXC_msg(ChatColor.AQUA, "钻石", Diamonds, under));
        p.sendMessage(AXC_msg(ChatColor.GOLD, "金矿", Gold, under));
        p.sendMessage(AXC_msg(ChatColor.DARK_GRAY, "铁矿", Iron, under));
        p.sendMessage(AXC_msg(ChatColor.RED, "红石", Redstone, under));
        p.sendMessage(AXC_msg(ChatColor.DARK_BLUE, "青金石", Lapis, under));
        p.sendMessage(AXC_msg(ChatColor.BLACK, "煤矿", Coal, under));
        p.sendMessage(AXC_msg(ChatColor.GRAY, "石头", Stone, under));
        p.sendMessage(AXC_msg(ChatColor.LIGHT_PURPLE, "钻石比", Diamonds, Stone));
        p.sendMessage(ChatColor.DARK_PURPLE+"--------------------------------");
        return true;
    }
}
