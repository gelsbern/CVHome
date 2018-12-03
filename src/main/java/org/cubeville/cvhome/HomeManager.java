package org.cubeville.cvhome;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HomeManager {

    private Plugin plugin;
    private List<Home> playerHomes;
    private static HomeManager instance;
    
    public HomeManager(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    public static HomeManager getInstance() {
        return instance;
    }
    
    @SuppressWarnings("unchecked")
    public void start() {
        this.playerHomes = (List<Home>) this.plugin.getConfig().get("Homes");
        if(this.playerHomes == null) { this.playerHomes = new ArrayList<Home>(); }
    }
    
    public void updatePlayerName(Player player) {
        UUID playerId = player.getUniqueId();
        String playerName = player.getName();
        if(doesPlayerHomeExist(player)) {
            Home playerHome = getPlayerHome(player);
            if(!playerHome.getPlayerName().equals(playerName)) {
                playerHome.setPlayerName(playerName);
                updatePlayerHome(playerId, playerHome);
            }
        }
        save();
    }
    
    public void stop() {
        save();
    }
    
    public boolean doesPlayerHomeExist(Player player) {
        if(getPlayerHome(player) != null) { return true; }
        else { return false; }
    }
    
    public boolean doesPlayerHomeExist(String playerName) {
        if(getPlayerHome(playerName) != null) { return true; }
        else { return false; }
    }
    
    public void updatePlayerMaxHomes(Player player) {
        if(!doesPlayerHomeExist(player)) { return; }
        int maxHomes = 1;
        if(player.hasPermission("cvhome.max.4")) { maxHomes = 4; }
        else if(player.hasPermission("cvhome.max.3")) { maxHomes = 3; }
        else if(player.hasPermission("cvhome.max.2")) { maxHomes = 2; }
        UUID playerId = player.getUniqueId();
        Home playerHome = getPlayerHome(player);
        playerHome.setMaxHomes(maxHomes);
        updatePlayerHome(playerId, playerHome);
        save();
    }
    
    public void setPlayerHome(Player player, int homeNumber, Location location) {
        if(!doesPlayerHomeExist(player)) { return; }
        Home playerHome = getPlayerHome(player);
        playerHome.setHome(location, homeNumber);
        updatePlayerHome(player.getUniqueId(), playerHome);
        save();
    }
    
    public void setPlayerHome(String playerName, int homeNumber, Location location) {
        if(!doesPlayerHomeExist(playerName)) { return; }
        Home playerHome = getPlayerHome(playerName);
        playerHome.setHome(location, homeNumber);
        updatePlayerHome(playerName, playerHome);
        save();
    }
    
    public void addPlayerHome(Home playerHome) {
        this.playerHomes.add(playerHome);
        save();
    }
    
    public Location getPlayerHomeForTeleport(Player player, int homeNumber) {
        return getPlayerHomeLocationGeneric(player, homeNumber);
    }
    
    public Location getPlayerHomeForTeleport(String playerName, int homeNumber) {
        return getPlayerHomeLocationGeneric(playerName, homeNumber);
    }
    
    public String getProperPlayerName(String playerName) {
        if(!doesPlayerHomeExist(playerName)) { return "NO HOME EXISTS"; }
        return getPlayerHome(playerName).getPlayerName();
    }
    
    public Location getPlayerHomeForInfo(Player player, int homeNumber) {
        return getPlayerHomeLocationGeneric(player, homeNumber);
    }
    public Location getPlayerHomeForInfo(String playerName, int homeNumber) {
        return getPlayerHomeLocationGeneric(playerName, homeNumber);
    }
    
    public int getMaxPlayerHomes(Player player) {
        if(!doesPlayerHomeExist(player)) { return 1; }
        return getPlayerHome(player).getMaxHomes();
    }
    
    public int getMaxPlayerHomes(String playerName) {
        if(!doesPlayerHomeExist(playerName)) { return 1; }
        return getPlayerHome(playerName).getMaxHomes();
    }
    
    private Location getPlayerHomeLocationGeneric(Player player, int homeNumber) {
        if(!doesPlayerHomeExist(player)) { return null; }
        return getPlayerHome(player).getHome(homeNumber);
    }
    
    private Location getPlayerHomeLocationGeneric(String playerName, int homeNumber) {
        if(!doesPlayerHomeExist(playerName)) { return null; }
        return getPlayerHome(playerName).getHome(homeNumber);
    }
    
    private void updatePlayerHome(UUID playerId, Home playerHome) {
        for(int i = 0; i < this.playerHomes.size(); i++) {
            if(this.playerHomes.get(i).getPlayerId().equals(playerId)) {
                this.playerHomes.set(i, playerHome);
                break;
            }
        }
    }
    
    private void updatePlayerHome(String playerName, Home playerHome) {
        for(int i = 0; i < this.playerHomes.size(); i++) {
            if(this.playerHomes.get(i).getPlayerName().equalsIgnoreCase(playerName)) {
                this.playerHomes.set(i, playerHome);
                break;
            }
        }
    }
    
    private Home getPlayerHome(Player player) {
        UUID playerId = player.getUniqueId();
        for(int i = 0; i < this.playerHomes.size(); i++) {
            if(this.playerHomes.get(i).getPlayerId().equals(playerId)) {
                return this.playerHomes.get(i);
            }
        }
        return null;
    }
    
    private Home getPlayerHome(String playerName) {
        for(int i = 0; i < this.playerHomes.size(); i++) {
            if(this.playerHomes.get(i).getPlayerName().equalsIgnoreCase(playerName)) {
                return this.playerHomes.get(i);
            }
        }
        return null;
    }
    
    private void save() {
        this.plugin.getConfig().set("Homes", this.playerHomes);
        this.plugin.saveConfig();
    }
}