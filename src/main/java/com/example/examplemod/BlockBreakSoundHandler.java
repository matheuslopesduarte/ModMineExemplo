package com.example.examplemod;

import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class BlockBreakSoundHandler {

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        
        Block brokenBlock = event.getState().getBlock();
        if (brokenBlock == ExampleMod.BLOCO_COM_TEXTURA.get()) {
            
            event.getLevel().playSound(
                null, 
                event.getPos(), 
                SoundEvents.ZOMBIE_AMBIENT, 
                net.minecraft.sounds.SoundSource.BLOCKS, 
                1.0f, 
                1.0f
            );
        }
    }

}
