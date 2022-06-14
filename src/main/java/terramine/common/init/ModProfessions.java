package terramine.common.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import terramine.TerraMine;

import java.util.function.Supplier;

public class ModProfessions {
    public static final Supplier<PoiType> GOBLIN_TINKERER_POI = register("goblin_tinkerer", 1, 1, ModBlocks.TINKERER_TABLE);
    public static final VillagerProfession GOBLIN_TINKERER = register("goblin_tinkerer", GOBLIN_TINKERER_POI, ModSoundEvents.DEMON_EYE_HURT);

    public static void fillTradeData() {
        //EmeraldForItems: i = getEmerald, j = maxUses, k = villagerXp
        //ItemsForEmeralds: i = costEmerald, j = numberOfItems, k = maxUses, l = villagerXp, f = priceMultiplier

        // GOBLIN_TINKERER TRADES
        VillagerTrades.ItemListing[] oceanographerLevel1 = new VillagerTrades.ItemListing[]{
                new VillagerTrades.ItemsForEmeralds(ModItems.ROCKET_BOOTS,10, 1,1,10),
                new VillagerTrades.EmeraldForItems(Items.DISPENSER, 5,15, 2)
        };
        VillagerTrades.ItemListing[] oceanographerLevel2 = new VillagerTrades.ItemListing[]{
                new VillagerTrades.ItemsForEmeralds(ModItems.STOPWATCH,5, 1,1,10),
                new VillagerTrades.EmeraldForItems(Items.STICKY_PISTON, 7,20, 4)
        };
        VillagerTrades.ItemListing[] oceanographerLevel3 = new VillagerTrades.ItemListing[]{
                new VillagerTrades.ItemsForEmeralds(ModItems.WEATHER_RADIO,7, 1,1,15),
                new VillagerTrades.ItemsForEmeralds(ModItems.SEXTANT,10, 1,1,15)
        };
        VillagerTrades.ItemListing[] oceanographerLevel4 = new VillagerTrades.ItemListing[]{
                new VillagerTrades.EmeraldForItems(Items.OBSERVER, 10,15, 15),
                new VillagerTrades.ItemsForEmeralds(ModItems.EXTENDO_GRIP,20, 1,1,15)
        };
        VillagerTrades.ItemListing[] oceanographerLevel5 = new VillagerTrades.ItemListing[]{
                new VillagerTrades.ItemsForEmeralds(ModItems.TOOLBELT,20, 1,1,30),
                new VillagerTrades.ItemsForEmeralds(ModItems.TOOLBOX,20, 1,1,30)
        };
        VillagerTrades.TRADES.put(GOBLIN_TINKERER,toIntMap(ImmutableMap.of(1,oceanographerLevel1,2,oceanographerLevel2,3,oceanographerLevel3,4,oceanographerLevel4,5,oceanographerLevel5)));

    }

    private static Supplier<PoiType> register(String name, int tickCount, int searchDistance, Block block) {
        return () -> PointOfInterestHelper.register(TerraMine.id(name), tickCount, searchDistance, block);
    }
    private static VillagerProfession register(String name, Supplier<PoiType> poi, SoundEvent sound) {
        var key = Registry.POINT_OF_INTEREST_TYPE.getResourceKey(poi.get()).orElseThrow();
        return Registry.register(Registry.VILLAGER_PROFESSION, TerraMine.id(name), VillagerProfessionBuilder.create().id(TerraMine.id(name)).workstation(holder -> holder.is(key)).jobSite(holder -> holder.is(key)).workSound(sound).build());
    }

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> trades) {
        return new Int2ObjectOpenHashMap<>(trades);
    }
}
