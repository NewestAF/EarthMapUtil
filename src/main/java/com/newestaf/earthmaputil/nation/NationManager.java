package com.newestaf.earthmaputil.nation;

import java.util.HashMap;

public class NationManager {

    private final HashMap<Integer, Nation> nations;

    private int nextNationID;

    public NationManager() {
        this.nations = new HashMap<>();
        this.nextNationID = -1;
    }

    public HashMap<Integer, Nation> getNations() {
        return this.nations;
    }

    public void addNation(Nation newNation) {
        this.nations.put(newNation.getID(), newNation);
        if (newNation.getID() > this.nextNationID) {
            this.nextNationID = newNation.getID();
        }
    }

    public void removeNation(Nation nation) {
        this.nations.remove(nation.getID());
    }

    public Nation getNationByID(int nationID) {
        return this.nations.get(nationID);
    }

    public Nation getNationByName(String name) {
        for (Nation nation : this.nations.values()) {
            if (nation.getName().toLowerCase().equals(name.toLowerCase())) return nation;
        }
        return null;
    }

    public int nextNationID() {
        this.nextNationID++;
        return this.nextNationID;
    }

    public int getNextNationID() {
        return this.nextNationID;
    }

    public void setNextNationID(int nextNationID) {
        this.nextNationID = nextNationID;
    }


}
