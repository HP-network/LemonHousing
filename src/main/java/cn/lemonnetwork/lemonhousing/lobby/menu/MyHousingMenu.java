package cn.lemonnetwork.lemonhousing.lobby.menu;

import cn.lemonnetwork.lemonhousing.LemonHousing;
import cn.lemonnetwork.lemonhousing.database.MongoDB;
import cn.lemonnetwork.lemonsugar.utils.menu.*;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyHousingMenu {
    public static void openMenu(Player player) {
        InventoryBuilder builder = new InventoryBuilder("我的家园", 27);
        UUID uuid = player.getUniqueId();
        Document playerData = MongoDB.getPlayerData(uuid);
        List<String> ownedHouses = (playerData != null && playerData.containsKey("owned_houses"))
                ? playerData.getList("owned_houses", String.class)
                : new ArrayList<>();

        int firstSlot = 10;
        if (!ownedHouses.isEmpty()) {
            Document houseData = MongoDB.getHouseData(ownedHouses.get(0));
            if (houseData != null) {
                List<String> lore = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.CHINA);
                lore.add("§8没有服务器");
                lore.add("");
                lore.add(ChatColor.GRAY + "创建时间: §a" + sdf.format(houseData.getDate("created_at")));
                lore.add(ChatColor.GRAY + "曲奇: §6" + playerData.getInteger("cookies", 0)); // 从玩家数据获取
                lore.add("");
                lore.add(ChatColor.YELLOW + "点击加入！");
                lore.add(ChatColor.YELLOW + "右键点击以管理！");

                builder.addContentInfo(new ContentInfo(new InventoryButton("HouseInfo") {
                    @Override
                    public ItemStack getItemStack() {
                        return new ItemEditor()
                                .name(ChatColor.GREEN + houseData.getString("name"))
                                .lore(lore)
                                .tag("inventory button","HouseInfo")
                                .material(Material.GRASS)
                                .build();
                    }

                    @Override
                    public void onClick(ClickType click) {
                        if (click.isLeftClick()) {
                            Bukkit.getRedisManager().set("housing", player.getUniqueId().toString(), houseData.getString("house_uuid"));                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Connect");
                            out.writeUTF("G_Housing");
                            player.sendPluginMessage(LemonHousing.getInstance(), "BungeeCord", out.toByteArray());
                        } else if (click.isRightClick()) {
                            open(player, houseData.getString("house_uuid"));
                        }
                    }
                }, firstSlot));
            }
        } else {
            List<String> lore = new ArrayList<>();
            lore.add("§8可用");
            lore.add("");
            lore.add("§e点击创建!");

            builder.addContentInfo(new ContentInfo(new InventoryButton("CreateHouse") {
                @Override
                public ItemStack getItemStack() {
                    return new ItemEditor()
                            .name(ChatColor.YELLOW + "空槽位")
                            .lore(lore)
                            .tag("inventory button","CreateHouse")
                            .material(Material.WOOD_BUTTON)
                            .build();
                }

                @Override
                public void onClick() {
                    try {
                        MongoDB.createNewHouse(player.getUniqueId(), "家园");
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                        openMenu(player);
                    } catch (IllegalStateException e) {
                        player.sendMessage(ChatColor.RED + e.getMessage());
                    }
                }
            }, firstSlot));
        }
        for (int i = 11; i <= 16; i++) {
            List<String> lockedLore = new ArrayList<>();
            lockedLore.add("§8可购买");
            lockedLore.add("");
            lockedLore.add("§7通过游戏内商城购买");

            builder.addContentInfo(new ContentInfo(new InventoryButton("Locked") {
                @Override
                public ItemStack getItemStack() {
                    return new ItemEditor()
                            .name("§c已锁定槽位")
                            .lore(lockedLore)
                            .tag("inventory button","Locked")
                            .material(Material.BEDROCK)
                            .build();
                }

                @Override
                public void onClick() {
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                    player.sendMessage("§c需要购买槽位才可使用.");
                }
            }, i));
        }

        new InventorySession(player, builder) {
            @Override public void onStart() {}
            @Override public void onClose() {}
        }.start();
    }
    private static void open(Player player,String houseUUID) {
        InventoryBuilder builder = new InventoryBuilder("管理家园", 36);
        List<String> safeMode = new ArrayList<>();
        safeMode.add("§7使用安全模式加入这个家园");
        safeMode.add("");
        safeMode.add("§e点击加入！");
        builder.addContentInfo(new ContentInfo(new InventoryButton("SafeMode") {
            @Override
            public ItemStack getItemStack() {
                return new ItemEditor()
                        .name("§a安全模式")
                        .lore(safeMode)
                        .tag("inventory button", "SafeMode")
                        .material(Material.COMMAND)
                        .build();
            }

            @Override
            public void onClick() {

            }
        }, 10));
        List<String> changeIcon = new ArrayList<>();
        changeIcon.add("§7更改显示此家园时使用的图标。");
        changeIcon.add("");
        changeIcon.add("§e点击打开！");
        builder.addContentInfo(new ContentInfo(new InventoryButton("ChangeIcon") {
            @Override
            public ItemStack getItemStack() {
                return new ItemEditor()
                        .name("§a更改图标")
                        .lore(changeIcon)
                        .tag("inventory button", "ChangeIcon")
                        .material(Material.GRASS)
                        .build();
            }

            @Override
            public void onClick() {

            }
        }, 12));
        List<String> copyHouse = new ArrayList<>();
        copyHouse.add("§7复制此房屋的最后保存点以及其世界、设置和区域。");
        copyHouse.add("");
        copyHouse.add("§7未包含:");
        copyHouse.add("§f · 曲奇");
        copyHouse.add("§f · 访问规则");
        copyHouse.add("");
        copyHouse.add("§e点击打开！");
        builder.addContentInfo(new ContentInfo(new InventoryButton("CopyHouse") {
            @Override
            public ItemStack getItemStack() {
                return new ItemEditor()
                        .name("§a复制家园")
                        .lore(copyHouse)
                        .tag("inventory button", "CopyHouse")
                        .material(Material.PRISMARINE)
                        .build();
            }

            @Override
            public void onClick() {

            }
        }, 14));
        List<String> deleteHouse = new ArrayList<>();
        deleteHouse.add("§7删除此家园.");
        deleteHouse.add("");
        deleteHouse.add("§e点击删除！");
        builder.addContentInfo(new ContentInfo(new InventoryButton("DeleteHouse") {
            @Override
            public ItemStack getItemStack() {
                return new ItemEditor()
                        .name("§c删除家园")
                        .lore(deleteHouse)
                        .tag("inventory button", "DeleteHouse")
                        .material(Material.STAINED_CLAY)
                        .durability(14)
                        .build();
            }

            @Override
            public void onClick() {
                deleteServerMenu(player, houseUUID);
            }
        }, 16));
        builder.addContentInfo(new ContentInfo(new InventoryButton("Back") {
            @Override
            public ItemStack getItemStack() {
                return new ItemEditor()
                        .name("§a返回")
                        .lore(Collections.singletonList("§7至我的家园"))
                        .tag("inventory button", "Back")
                        .material(Material.ARROW)
                        .build();
            }

            @Override
            public void onClick() {
                openMenu(player);
            }
        }, 31));
        new InventorySession(player, builder) {
            @Override public void onStart() {}
            @Override public void onClose() {}
        }.start();
    }
    public static void deleteServerMenu(Player player,String houseUUID) {
        InventoryBuilder builder = new InventoryBuilder("你确定吗?", 27);
        builder.addContentInfo(new ContentInfo(new InventoryButton("Cancel") {
            public ItemStack getItemStack() {
                return (new ItemEditor())
                        .name("§a取消")
                        .material(Material.STAINED_CLAY)
                        .durability(5)
                        .tag("inventory button", "Cancel")
                        .build();
            }

            public void onClick() {
                open(player, houseUUID);
            }
        }, 15));
        builder.addContentInfo(new ContentInfo(new InventoryButton("True") {
            public ItemStack getItemStack() {
                return (new ItemEditor())
                        .name("§c确认")
                        .material(Material.STAINED_CLAY)
                        .durability(14)
                        .tag("inventory button", "True")
                        .build();
            }
            @Override
            public void onClick() {
                MongoDB.deleteHouse(player.getUniqueId(), houseUUID);
                openMenu(player);
                player.sendMessage("§c已删除家园！");
            }
        }, 11));
        new InventorySession(player, builder) {
            @Override public void onStart() {}
            @Override public void onClose() {}
        }.start();
    }
}