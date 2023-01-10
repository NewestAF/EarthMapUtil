package com.newestaf.earthmaputil.nation;

import com.newestaf.earthmaputil.EarthMapUtil;
import com.newestaf.earthmaputil.util.ItemStackJsonUtil;
import com.newestaf.enhancedenchantment.util.ItemUtil;
import com.newestaf.newestutil.config.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Nation {
    enum NationStage {
        Infant,
        Local,
        Great
    }

    private final ConfigurationManager configManager;

    private String name;
    private final int id;
    private final Block coreBlock;
    private UUID ownerUUID;
    private final ArrayList<UUID> members;
    private final ArrayList<UUID> invitedPlayers;
    private Inventory sharingInventory;
    private NationStage stage;

    public Nation(String name, Block coreBlock, Player owner) {
        this.configManager = EarthMapUtil.getConfigurationManager();
        this.coreBlock = coreBlock;
        NationManager nationManager = EarthMapUtil.getNationManager();
        this.name = name;
        this.id = nationManager.nextNationID();
        this.ownerUUID = owner.getUniqueId();
        this.members = new ArrayList<>();
        this.invitedPlayers = new ArrayList<>();
        stage = NationStage.Infant;
        sharingInventory = Bukkit.createInventory(null, 9 * (int) configManager.get("infant.inventoryRaw"));
        nationManager.addNation(this);
    }

    public Nation(JSONObject jsonNation) {
        this.configManager = EarthMapUtil.getConfigurationManager();
        this.name = jsonNation.get("name").toString();
        this.id = Integer.parseInt(jsonNation.get("id").toString());
        this.ownerUUID = UUID.fromString(jsonNation.get("ownerUUID").toString());
        this.coreBlock = ((Location) jsonNation.get("coreLocation")).getBlock();
        this.members = new ArrayList<>();
        JSONArray membersArray = ((JSONArray) jsonNation.get("members"));
        for (Object member : membersArray) {
            this.members.add(UUID.fromString(member.toString()));
        }
        this.invitedPlayers = new ArrayList<>();
        this.stage = Enum.valueOf(NationStage.class, (String) jsonNation.get("stage"));

        int inventorySize = 9;
        switch (stage) {
            case Infant -> inventorySize = 9 * (int) configManager.get("infant.inventoryRaw");
            case Local -> inventorySize = 9 * (int) configManager.get("local.inventoryRaw");
            case Great -> inventorySize = 9 * (int) configManager.get("great.inventoryRaw");
        }

        sharingInventory = Bukkit.createInventory(null, inventorySize);
        JSONArray itemArray = (JSONArray) jsonNation.get("inventory");
        for (int i = 0; i < inventorySize; i++) {
            String itemData = (String) itemArray.get(i);
            ItemStack itemStack;
            if (Objects.equals(itemData, "air")) {
                itemStack = ItemUtil.AIR;
            }
            else {
                itemStack = ItemStackJsonUtil.fromJson(itemData);
            }sharingInventory.setItem(i, itemStack);
        }
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
        if (!player.isOnline()) {
            return;
        }
        player.sendMessage(ChatColor.GREEN + "You have been invited to join " + this.name + "! To join, type /nation join [name]");
        ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(1);
        executorService.schedule(() -> {
            invitedPlayers.remove(uuid);
        }, 1, TimeUnit.MINUTES);
    }


    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject jsonNation = new JSONObject();
        jsonNation.put("name", this.name);
        jsonNation.put("id", String.valueOf(this.id));
        jsonNation.put("ownerUUID", this.ownerUUID.toString());
        jsonNation.put("coreLocation", this.coreBlock.getLocation());
        JSONArray membersArray = new JSONArray();
        for (UUID member : this.members) {
            membersArray.add(member.toString());
        }
        jsonNation.put("members", membersArray);
        jsonNation.put("stage", stage.name());
        JSONArray jsonInventory = new JSONArray();
        for (int i = 0; i < sharingInventory.getSize(); i++) {
            ItemStack itemStack = sharingInventory.getItem(i);
            if (itemStack != null) {
                jsonInventory.add(i, ItemStackJsonUtil.toJson(itemStack));
            }
            else jsonInventory.add(i, "air");
        }
        jsonNation.put("inventory", jsonInventory);
        return jsonNation;
    }

}
