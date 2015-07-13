package gri.ppt.DirtyServerControl;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import gri.ppt.DirtyServerControl.command.AntiXrayCheck;
import gri.ppt.DirtyServerControl.listener.*;

public class DirtyServerControl extends JavaPlugin{
	public void onEnable(){
		getLogger().info("加载DirtyServerControl中,当前版本 " + this.getDescription().getVersion());
		boolean hookcp = HookCoreprotect();
		if(hookcp){
			this.getCommand("dsc").setExecutor(new AntiXrayCheck(this));
		}
		FakelistenerReg();
	}
	public void onDisable(){
		getLogger().info("卸载DirtyServerControl中.. ");
	}
	public void FakelistenerReg(){
		new NetherBedrockCheck(this);
	}
	public boolean HookCoreprotect(){
		Plugin cp = getServer().getPluginManager().getPlugin("CoreProtect");
		if(cp == null){
			getLogger().info("找不到CoreProtect插件,停用部分功能.");
			return false;
		}
		else{
			getLogger().info("成功匹配到CoreProtect!");
			return true;
		}
	}
}