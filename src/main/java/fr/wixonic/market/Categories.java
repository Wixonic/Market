package fr.wixonic.market;

import org.bukkit.Material;

import java.util.*;

public final class Categories {
	public final static Map<String, List<Material>> list = new HashMap<String, List<Material>>();
	public final static List<Material> illegalItems = new ArrayList<>();

	static {
		Collections.addAll(Categories.illegalItems,
			Material.BARRIER, Material.COMMAND_BLOCK,
			Material.COMMAND_BLOCK_MINECART,
			Material.CHAIN_COMMAND_BLOCK,
			Material.REPEATING_COMMAND_BLOCK,
			Material.LIGHT,
			Material.STRUCTURE_BLOCK,
			Material.STRUCTURE_VOID,
			Material.SPAWNER,
			Material.PLAYER_HEAD,
			Material.ENCHANTED_BOOK,
			Material.WRITTEN_BOOK,
			Material.POTION,
			Material.LINGERING_POTION,
			Material.SPLASH_POTION,
			Material.TIPPED_ARROW,
			Material.DEBUG_STICK,
			Material.KNOWLEDGE_BOOK,
			Material.GOAT_HORN,
			Material.MAP,
			Material.FILLED_MAP,
			Material.JIGSAW,
			Material.BUNDLE
		);

		List<Material> all = new ArrayList<>();
		List<Material> valuables = new ArrayList<>();
		List<Material> mobdrops = new ArrayList<>();
		List<Material> farming = new ArrayList<>();
		List<Material> blocks = new ArrayList<>();
		List<Material> special = new ArrayList<>();
		List<Material> other = new ArrayList<>();

		for (Material material : Material.values()) {
			String name = material.name().toLowerCase();

			if (name.endsWith("_spawn_egg") || material.isLegacy() || material.isAir() || !material.isItem()) {
				Categories.illegalItems.add(material);
			} else if (!Categories.illegalItems.contains(material) &&
				!name.endsWith("_sword") &&
				!name.endsWith("_pickaxe") &&
				!name.endsWith("_axe") &&
				!name.endsWith("_shovel") &&
				!name.endsWith("_hoe") &&
				!name.endsWith("_helmet") &&
				!name.endsWith("_chestplate") &&
				!name.endsWith("_leggings") &&
				!name.endsWith("_boots") &&
				!name.endsWith("_boat") &&
				!name.endsWith("_minecart")
			) {
				all.add(material);

				if (name.endsWith("_ingot") ||
					name.equals("ancient_debris") ||
					(name.endsWith("_log") && !name.startsWith("stripped_")) ||
					name.equals("lapis_lazuli") ||
					name.endsWith("coal") || // Includes charcoal
					name.equals("diamond") ||
					name.equals("emerald")
				) {
					valuables.add(material);
				} else if (name.startsWith("blaze_") ||
					name.equals("bone") ||
					name.equals("gunpowder") ||
					name.equals("white_wool") ||
					name.equals("ender_pearl") ||
					name.equals("ghast_tear") ||
					name.equals("rotten_flesh")
				) {
					mobdrops.add(material);
				} else if (name.endsWith("_sapling") ||
					name.startsWith("cook") // Without space because of Cookie
				) {
					farming.add(material);
				} else if (name.endsWith("_block") ||
					name.endsWith("_ore") ||
					name.equals("white_carpet") ||
					name.endsWith("_wood") ||
					name.startsWith("stripped_") ||
					name.endsWith("_planks") ||
					name.contains("coral") ||
					name.endsWith("_dirt") ||
					name.endsWith("_stone") ||
					name.equals("glass") ||
					name.equals("glass_pane") ||
					name.equals("white_bed") ||
					name.endsWith("_sign")
				) {
					blocks.add(material);
				} else if (name.endsWith("_head") ||
					name.endsWith("_skull") ||
					name.equals("ender_eye")
				) {
					special.add(material);
				} else if (!name.endsWith("_wool") &&
					!name.endsWith("_carpet") &&
					!name.endsWith("_glass") &&
					!name.endsWith("_glass_pane") &&
					!name.endsWith("_bed")
				) {
					other.add(material);
				}
			}
		}

		Categories.list.put("all", all);
		Categories.list.put("valuables", valuables);
		Categories.list.put("mobdrops", mobdrops);
		Categories.list.put("farming", farming);
		Categories.list.put("blocks", blocks);
		Categories.list.put("special", special);
		Categories.list.put("other", other);
	}

	public static List<Material> get(String category) {
		return Categories.list.getOrDefault(category, new ArrayList<Material>());
	}
}
