package baguchi.mob_enchant_with_tome;

import baguchi.mob_enchant_with_tome.goal.DeathTomeEnchantGoal;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import twilightforest.entity.monster.DeathTome;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MobEnchantWithTomeCore.MODID)
public class MobEnchantWithTomeCore {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mob_enchant_with_tome";

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MobEnchantWithTomeCore(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void joinEvent(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof DeathTome deathTome) {
            deathTome.goalSelector.addGoal(1, new DeathTomeEnchantGoal(deathTome));
        }
    }

}
