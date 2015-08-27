package net.redstonelamp.plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import net.redstonelamp.plugin.PluginLoader.PluginState;
import net.redstonelamp.plugin.java.JavaPluginManager;

public class PluginSystem {
	/**
	 * Contains all plugin managers
	 */
	@Getter private static final PluginManager[] pluginManagers = new PluginManager[]{new JavaPluginManager()};
	@Getter private static Logger logger = null;
	
	/**
	 * Initializes the plugin system. Has to be called from the main server loop!
	 * @param log Logger to print out information (maybe with a [PluginSystem] tag?)
	 */
	public void init(Logger log){
		logger = log;
		logger.log(Level.INFO, "Initializing plugin system...");
		PluginManager.init();
	}
	/**
	 * Loads all plugins and initializes them
	 */
	public void loadPlugins(){
		logger.log(Level.INFO, "Loading & initializing plugins...");
		for(PluginManager mgr : pluginManagers)mgr.loadPlugins();
		for(PluginManager mgr : pluginManagers){
			for(PluginLoader l : mgr.getPluginLoaders()){
				initPlugin(l);
			}
		}
	}
	/**
	 * Enables all plugins
	 */
	public void enablePlugins(){
		logger.log(Level.INFO, "Enabling plugins...");
		for(PluginManager mgr : pluginManagers){
			for(PluginLoader l : mgr.getPluginLoaders()){
				enablePlugin(l);
			}
		}
	}
	/**
	 * Disables all plugins
	 */
	public void disablePlugins(){
		logger.log(Level.INFO, "Disabling plugins...");
		for(PluginManager mgr : pluginManagers){
			for(PluginLoader l : mgr.getPluginLoaders()){
				disablePlugin(l);
			}
		}
	}
	/**
	 * Checks dependencies & initializes the specific plugin & loader
	 * @param loader
	 */
	private void initPlugin(PluginLoader loader) {
		//If this plugin already got initialized, return
		if(loader.getState()==PluginState.INITIALIZED)return;
		//Check if all dependencies are loaded and load them if not
		//If a dependency is missing, return to stop loading this plugin
		for(String depend : loader.getDependencies()){
			PluginLoader dependency = getPluginLoader(depend);
			if(dependency==null||dependency.getState()==PluginState.UNLOADED){
				logger.log(Level.WARNING, loader.getName()+" v"+loader.getVersion()+" is missing dependency "+depend+"! Disabling!");
				loader.setState(PluginState.LOADED);
				return;
			}
			if(dependency.getState()!=PluginState.INITIALIZED){
				initPlugin(dependency);
			}
		}
		//Check if all soft dependencies are loaded and initialize them
		//If a soft dependency is missing, continue checking for other soft dependencies
		for(String depend : loader.getSoftDependencies()){
			PluginLoader dependency = getPluginLoader(depend);
			if(dependency==null||dependency.getState()==PluginState.UNLOADED){
				continue;
			}
			if(dependency.getState()!=PluginState.INITIALIZED){
				initPlugin(dependency);
			}
		}
		//Dependencies are initialized, initialize the plugin itself
		loader.initPlugin();
	}
	/**
	 * Enables the specific plugin & loader
	 * @param loader
	 */
	private void enablePlugin(PluginLoader loader) {
		//If this plugin didn't get initialized, return
		if(loader.getState()!=PluginState.INITIALIZED&&loader.getState()!=PluginState.DISABLED)return;
		//Check if all dependencies are enabled and enable them if not
		//If a dependency is missing, return to stop loading this plugin (should never happen)
		for(String depend : loader.getDependencies()){
			PluginLoader dependency = getPluginLoader(depend);
			if(dependency==null||dependency.getState()==PluginState.UNLOADED||dependency.getState()==PluginState.LOADED){
				logger.log(Level.WARNING, loader.getName()+" v"+loader.getVersion()+" is missing dependency "+depend+"! Disabling!");
				loader.setState(PluginState.DISABLED);
				return;
			}
			if(dependency.getName().equalsIgnoreCase(loader.getName())){
				logger.log(Level.WARNING, loader.getName()+" v"+loader.getVersion()+" is depending on itself! Disabling!");
				loader.setState(PluginState.DISABLED);
				return;
			}
			if(dependency.dependsOn(loader.getName())||dependency.softDependsOn(loader.getName())){
				logger.log(Level.WARNING, loader.getName()+" v"+loader.getVersion()+" could not be loaded, because its dependency \""+dependency.getName()+"\" is depending on the plugin itself! (Both plugins depend on each other)");
				loader.setState(PluginState.DISABLED);
				return;
			}
			if(dependency.getState()==PluginState.INITIALIZED){
				enablePlugin(dependency);
			}
		}
		//Check if all soft dependencies are enabled and enable them
		//If a soft dependency is missing, continue checking for other soft dependencies
		for(String depend : loader.getSoftDependencies()){
			PluginLoader dependency = getPluginLoader(depend);
			if(dependency==null){
				continue;
			}
			if(dependency.getName().equalsIgnoreCase(loader.getName())){
				logger.log(Level.WARNING, loader.getName()+" v"+loader.getVersion()+" is soft depending on itself! Disabling!");
				loader.setState(PluginState.DISABLED);
				return;
			}
			if(dependency.dependsOn(loader.getName())||dependency.softDependsOn(loader.getName())){
				logger.log(Level.WARNING, loader.getName()+" v"+loader.getVersion()+" could not be loaded, because its soft dependency \""+dependency.getName()+"\" is depending on the plugin itself! (Both plugins depend on each other)");
				loader.setState(PluginState.DISABLED);
				return;
			}
			if(dependency.getState()==PluginState.INITIALIZED){
				enablePlugin(dependency);
			}
		}
		//Dependencies are enabled, enable the plugin itself
		loader.enablePlugin();
	}
	/**
	 * Disables the specific plugin & loader
	 * @param loader
	 */
	private void disablePlugin(PluginLoader loader) {
		//If this plugin didn't get enabled, return
		if(loader.getState()!=PluginState.ENABLED)return;
		//Disable plugins depending on this plugin first
		for(PluginManager mgr : pluginManagers){
			for(PluginLoader l : mgr.getPluginLoaders()){
				if(l.dependsOn(loader.getName())){
					disablePlugin(l);
				}
			}
		}
		//Dependent plugins are disabled, disable the plugin itself
		loader.disablePlugin();
	}
	/**
	 * Gets the correct plugin manager by a given plugin name
	 * @param name Name of plugin
	 * @returns PluginManager or null if not found
	 */
	public PluginManager getPluginManager(String name){
		PluginLoader l = getPluginLoader(name);
		if(l==null)return null;
		return l.getPluginManager();
	}
	/**
	 * Gets the correct plugin loader by a given plugin name
	 * @param name Name of plugin
	 * @returns PluginLoader or null if not found
	 */
	public PluginLoader getPluginLoader(String name){
		for(PluginManager mgr : pluginManagers){
			for(PluginLoader l : mgr.getPluginLoaders()){
				if(l.getName().equalsIgnoreCase(name)){
					return l;
				}
			}
		}
		return null;
	}
	/**
	 * Gets the correct plugin by a given plugin name
	 * @param name Name of plugin
	 * @returns Plugin or null if not found
	 */
	public Plugin getPlugin(String name){
		for(PluginManager mgr : pluginManagers){
			for(PluginLoader l : mgr.getPluginLoaders()){
				if(l.getName().equalsIgnoreCase(name)){
					return l.getPlugin();
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the current instance of the JavaPluginManager
	 * @returns JavaPluginManager
	 */
	public JavaPluginManager getJavaPluginManager(){
		return (JavaPluginManager) pluginManagers[0];
	}
}
