package com.newestaf.earthmaputil.nation;

import com.newestaf.earthmaputil.EarthMapUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Nation {

    private String name;
    private int id;
    private UUID ownerUUID;
    private final ArrayList<UUID> members;
    private final ArrayList<UUID> invitedPlayers;

    public Nation(String name, Player owner, int id) {
        this.name = name;
        this.id = id;
        this.ownerUUID = owner.getUniqueId();
        this.members = new ArrayList<>();
        this.invitedPlayers = new ArrayList<>();
        EarthMapUtil.getNationManager().addNation(this);
    }

    public Nation(JSONObject jsonNation) {
        this.name = jsonNation.get("name").toString();
        this.id = Integer.parseInt(jsonNation.get("id").toString());
        this.ownerUUID = UUID.fromString(jsonNation.get("ownerUUID").toString());
        this.members = new ArrayList<>();
        JSONArray membersArray = ((JSONArray) jsonNation.get("members"));
        for (Object member : membersArray) {
            this.members.add(UUID.fromString(member.toString()));
        }
        this.invitedPlayers = new ArrayList<>();
        EarthMapUtil.getNationManager().addNation(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public int getID() {
        return this.id;
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public void setOwnerUUID(UUID newOwnerUUID) {
        this.ownerUUID = newOwnerUUID;
    }

    public ArrayList<UUID> getMemberUUIDs() {
        return this.members;
    }

    public boolean isMember(Player player) {
        return this.members.contains(player.getUniqueId());
    }

    public void addMember(UUID uuid) {
        this.members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        this.members.remove(uuid);
        this.members.trimToSize();
    }

    public ArrayList<UUID> getInvitedPlayers() {
        return this.invitedPlayers;
    }

    public void addInvitedPlayer(UUID uuid) {
        this.invitedPlayers.add(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        if (player.isOnline()) {
            player.sendMessage(ChatColor.GREEN + "You have been invited to join " + this.name + "! To join, type /nation join [name]");
            ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(1);
            executorService.schedule(() -> {
                invitedPlayers.remove(uuid);
                // TODO
            }, 1, TimeUnit.MINUTES);
        }
        else {
            player.sendMessage(ChatColor.RED + "That player is not online");
        }
    }


    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject jsonNation = new JSONObject();
        jsonNation.put("name", this.name);
        jsonNation.put("id", String.valueOf(this.id));
        jsonNation.put("ownerUUID", this.ownerUUID.toString());
        JSONArray membersArray = new JSONArray();
        for (UUID member : this.members) {
            membersArray.add(member.toString());
        }
        jsonNation.put("members", membersArray);
        return jsonNation;
    }

}
