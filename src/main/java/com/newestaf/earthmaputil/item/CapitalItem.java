package com.newestaf.earthmaputil.item;

import com.newestaf.earthmaputil.EarthMapUtil;
import com.newestaf.earthmaputil.nation.NationArchetype;
import com.newestaf.earthmaputil.util.InventoryGUIFactory;
import com.newestaf.newestutil.gui.InventoryGUI;
import com.newestaf.newestutil.gui.InventoryGUIButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CapitalItem {

    private final ItemStack itemStack;
    private String nationName;
    private ChatColor nationColor;
    private NationArchetype nationArchetype;

    private InventoryGUI settingGUI;

    public CapitalItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        settingGUI = InventoryGUIFactory.createInventoryGUI(addColor("국가 설정", ChatColor.AQUA), 1);
        InventoryGUIButton nameSetting = new InventoryGUIButton(
                addColor("국가명 설정", ChatColor.AQUA),
                addColor("국가의 이름을 설정합니다.", ChatColor.RED),
                Material.NAME_TAG
        );
        InventoryGUIButton colorSetting = new InventoryGUIButton(
                addColor("국가 색 설정", ChatColor.AQUA),
                addColor("국가의 색깔을 설정합니다.", ChatColor.RED),
                Material.RED_DYE
        );
    }

    private String addColor(String text, ChatColor color) {
        return color + "" + ChatColor.BOLD + text;
    }


}
