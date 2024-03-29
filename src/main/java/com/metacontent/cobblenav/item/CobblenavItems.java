package com.metacontent.cobblenav.item;

import com.metacontent.cobblenav.Cobblenav;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CobblenavItems {
    public static final Item POKENAV_ITEM = registerPokenavItem(null);
    public static final Item POKENAV_ITEM_WHITE = registerPokenavItem("white");
    public static final Item POKENAV_ITEM_LIGHT_GRAY = registerPokenavItem("light_gray");
    public static final Item POKENAV_ITEM_GRAY = registerPokenavItem("gray");
    public static final Item POKENAV_ITEM_BLACK = registerPokenavItem("black");
    public static final Item POKENAV_ITEM_BROWN = registerPokenavItem("brown");
    public static final Item POKENAV_ITEM_RED = registerPokenavItem("red");
    public static final Item POKENAV_ITEM_ORANGE = registerPokenavItem("orange");
    public static final Item POKENAV_ITEM_YELLOW = registerPokenavItem("yellow");
    public static final Item POKENAV_ITEM_LIME = registerPokenavItem("lime");
    public static final Item POKENAV_ITEM_GREEN = registerPokenavItem("green");
    public static final Item POKENAV_ITEM_CYAN = registerPokenavItem("cyan");
    public static final Item POKENAV_ITEM_LIGHT_BLUE = registerPokenavItem("light_blue");
    public static final Item POKENAV_ITEM_BLUE = registerPokenavItem("blue");
    public static final Item POKENAV_ITEM_PURPLE = registerPokenavItem("purple");
    public static final Item POKENAV_ITEM_MAGENTA = registerPokenavItem("magenta");
    public static final Item POKENAV_ITEM_PINK = registerPokenavItem("pink");

    public static final ItemGroup POKENAV_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(Cobblenav.ID, "pokenav_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(POKENAV_ITEM))
                    .displayName(Text.translatable("itemGroup.cobblenav.pokenav_group"))
                    .entries(((displayContext, entries) -> {
                        entries.add(POKENAV_ITEM);
                        entries.add(POKENAV_ITEM_WHITE);
                        entries.add(POKENAV_ITEM_LIGHT_GRAY);
                        entries.add(POKENAV_ITEM_GRAY);
                        entries.add(POKENAV_ITEM_BLACK);
                        entries.add(POKENAV_ITEM_BROWN);
                        entries.add(POKENAV_ITEM_RED);
                        entries.add(POKENAV_ITEM_ORANGE);
                        entries.add(POKENAV_ITEM_YELLOW);
                        entries.add(POKENAV_ITEM_LIME);
                        entries.add(POKENAV_ITEM_GREEN);
                        entries.add(POKENAV_ITEM_CYAN);
                        entries.add(POKENAV_ITEM_LIGHT_BLUE);
                        entries.add(POKENAV_ITEM_BLUE);
                        entries.add(POKENAV_ITEM_PURPLE);
                        entries.add(POKENAV_ITEM_MAGENTA);
                        entries.add(POKENAV_ITEM_PINK);
                    }))
                    .build());

    private static Item registerPokenavItem(String color) {
        return registerItem("pokenav_item" + (color != null ? "_" + color : ""),
                new PokenavItem(new FabricItemSettings().maxCount(1)));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Cobblenav.ID, name), item);
    }

    public static void registerCobblenavItems() {
        Cobblenav.LOGGER.info("Registering items for " + Cobblenav.ID);
    }
}
