package me.pureheroky.pureplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.*;

public final class Pureplugin extends JavaPlugin implements Listener {
    HashMap<UUID, String> lastMessage = new HashMap<UUID, String>();
    HashMap<UUID, List<String>> Players = new HashMap<UUID, List<String>>();
    HashMap<UUID, Boolean> isLoggedIn = new HashMap<UUID, Boolean>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("pureheroky plugin has been started.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        System.out.println("A player " + player.getName() + " has joined the server");
        lastMessage.put(player.getUniqueId(), "");
        System.out.println(Players);
        if (!Players.containsKey(player.getUniqueId())){
            event.setJoinMessage("§c" + player.getName() + " you need to register");
            event.setJoinMessage("§c" + "type .reg <password> <repeat password>");
        } else {
            System.out.println(Players.get(player.getUniqueId()));
            event.setJoinMessage("§c"+ player.getName() + " you need to login");
            event.setJoinMessage("§c" + "type .log <password>");
        }
        WelcomeMessage(player);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uid = player.getUniqueId();
        isLoggedIn.put(uid, false);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        lastMessage.put(event.getPlayer().getUniqueId(), event.getMessage());
    }

    public void WelcomeMessage(Player player){
        player.sendMessage("§l§b" + "--------------------------------");
        player.sendMessage("§l§b" + "           Welcome to the server");
        player.sendMessage("§l§d" + "          LUCHSHIY SERVER NA ZEMLE");
        player.sendMessage("§o§a" + "                enjoy playing    ");
        player.sendMessage("§o§f" + "                  creators:      ");
        player.sendMessage("§o§6" + "                 pureheroky      ");
        player.sendMessage("§o§6" + "                 kataomoikiru     ");
        player.sendMessage("§o§6" + "                     fury         ");
        player.sendMessage("§l§b" + "--------------------------------");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location loc = new Location(Bukkit.getWorld("world"), 0, 84, 0);

        double fromX = event.getFrom().getX();
        double fromY = event.getFrom().getY();
        double fromZ = event.getFrom().getZ();

        double toX = 0, toY = 0, toZ = 0;
        if(event.getTo() != null){
            toX = event.getTo().getX();
            toY = event.getTo().getY();
            toZ = event.getTo().getZ();
        }

        UUID uid = player.getUniqueId();

        if ((int) fromX != (int) toX || (int) fromY != (int) toY || (int) fromZ != (int) toZ) {
            if (isLoggedIn.get(uid) == null || !isLoggedIn.get(uid)){
                if (lastMessage.get(uid) != null){
                    if (!Players.containsKey(uid)){
                        if (lastMessage.get(uid).contains(".reg")){
                            String[] message = lastMessage.get(uid).split(" ");
                            if (message.length == 3 && message[1] != null && message[2] != null){
                                if (Objects.equals(message[1], message[2])){
                                    System.out.println("PLAYER DATA");
                                    Players.put(uid, Arrays.asList(message));
                                } else {
                                    player.teleport(loc);
                                    player.sendMessage("§c" + "Passwords not equals. try again");
                                }
                            } else {
                                player.teleport(loc);
                                player.sendMessage("§c" + "type .reg <password> <repeat password>");
                            }
                        }
                    } if (Players.containsKey(uid)){
                        if (lastMessage.get(uid).contains(".log")){
                            String[] message = lastMessage.get(uid).split(" ");
                            if (message.length == 2 && message[1] != null){
                                if (Objects.equals(Players.get(uid).get(1), message[1])){
                                    Players.put(uid, Arrays.asList(message));
                                    WelcomeMessage(player);
                                    isLoggedIn.put(uid, true);
                                    player.teleport(new Location(Bukkit.getWorld("world"), -604, 66, 175));
                                } else {
                                    player.teleport(loc);
                                    player.sendMessage("§c" + "Password is wrong, try again");
                                }
                            } else {
                                player.teleport(loc);
                                player.sendMessage("§c" + "type .log <password>");
                            }
                        } else {
                            player.teleport(loc);
                            player.sendMessage("§c" + "type .log <password>");
                        }
                    } else {
                        player.teleport(loc);
                        player.sendMessage("§c" + "type .reg <password> <repeat password>");
                    }

                } else {
                    player.teleport(loc);
                    player.sendMessage("§c" + "type .reg <password> <repeat password>");
                }
            }
        }
    }
}