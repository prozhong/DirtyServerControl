package gri.ppt.DirtyServerControl.listener;

import gri.ppt.DirtyServerControl.DirtyServerControl;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class NetherBedrockCheck
{
    private DirtyServerControl plugin;
    
    public NetherBedrockCheck(final DirtyServerControl plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)plugin, new Runnable() {
		    public void run() {
		    	NBCheck();
		    }
		    }, 0, 20);
    }
    
    public int NetherPlayersNum(String world){
	   int num = plugin.getServer().getWorld(world).getPlayers().size();
	   return num;
    }
   
    public void NBCheck(){
	   
	   if(!plugin.getServer().getAllowNether()){
       	return;
       }
	   if(plugin.getServer().getWorld("world_nether").getPlayers().isEmpty()){
		   return;
	   }
       for(World w1:plugin.getServer().getWorlds()){
         if(w1.getName().endsWith("nether") && NetherPlayersNum(w1.getName())>0){
       		for(Player player:w1.getPlayers()){
               	if(!player.isOp()&&player.getLocation().getY()>127.5){
               		player.teleport(Bukkit.getWorld("world").getSpawnLocation());
               		player.sendMessage("地狱很危险，少年你还是回地球吧!");
               		}
                }
       	 }
       }
    }
    
}
