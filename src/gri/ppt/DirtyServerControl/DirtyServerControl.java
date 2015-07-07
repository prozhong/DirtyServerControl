package gri.ppt.DirtyServerControl;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.*;

import gri.ppt.DirtyServerControl.listener.*;

public class DirtyServerControl extends JavaPlugin{
	public void onEnable(){
		getLogger().info("加载DirtyServerControl中,当前版本 " + this.getDescription().getVersion());
		FakelistenerReg();
	}
	public void onDisable(){
		getLogger().info("卸载DirtyServerControl中.. ");
	}
	public void FakelistenerReg(){
		
	}
	public void Regself(){
    	Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    public void run() {
		    	NBCheck();
		    }
		    }, 0, 100);
    }
}