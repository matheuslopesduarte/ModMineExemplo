package com.example.examplemod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;


// O id do mod é definido no @Mod e deve ser o mesmo que o id do mod no arquivo mods.toml
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    //  id do mod
    public static final String MODID = "examplemod";

    // registra o logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // registra a lista de blocos
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    // registra a lista de itens
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    // registra a lista de abas criativas
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // registra um novo bloco chamado example_block
    public static final RegistryObject<Block> EXAMPLE_BLOCK = 
        BLOCKS.register("example_block", () -> 
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));

    // registra um novo item chamado example_block
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = 
        ITEMS.register("example_block", () -> 
            // define o bloco que o item representa
            new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));


    // registra o item example_item com propriedades de comida
    public static final RegistryObject<Item> EXAMPLE_ITEM = 
        ITEMS.register("example_item", 
            () -> new Item(
                new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEat().nutrition(1).saturationMod(2f).build())
                )
            );

    // registra uma nova aba criativa chamada example_tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // adiciona o item example_item na aba
            }).build());


    // exemplo de bloco com textura com som de zumbi e achiviment 
    // $$$$$$$$$$$$$$$

        // registra um novo bloco chamado bloco_com_textura
        public static final RegistryObject<Block> BLOCO_COM_TEXTURA = BLOCKS.register("bloco_com_textura", 
            () -> new Block(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GRASS) // cor do bloco no mapa, cor da grama
                    .strength(3.0f) // resistencia do bloco, 3.0f é a resistncia do bloco de pedra
                    .requiresCorrectToolForDrops() // somente ferramentas especificas podem dropar o bloco
                    )
                );

        // registra um novo item para o bloco_com_textura
        public static final RegistryObject<Item> ITEM_COM_TEXTURA = ITEMS.register("bloco_com_textura", 
            () -> new BlockItem(
                BLOCO_COM_TEXTURA.get(), 
                new Item.Properties()
                )
            );
    // $$$$$$$$$$$$$$$


    public ExampleMod()
    {
        // define o mod event bus
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // define o método que será chamado no evento de common setup

        // registra o Deferred Register para o mod event bus para que os blocos sejam registrados
        BLOCKS.register(modEventBus);

        // registra o Deferred Register para o mod event bus para que os itens sejam registrados
        ITEMS.register(modEventBus);

        // registra o Deferred Register para o mod event bus para que as abas criativas sejam registradas
        CREATIVE_MODE_TABS.register(modEventBus);

        // registra essa classe para que os eventos sejam chamados
        MinecraftForge.EVENT_BUS.register(this);

        // Registra métodos para eventos
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::commonSetup);


        // registra o arquivo de configuração do mod
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        MinecraftForge.EVENT_BUS.register(BlockBreakSoundHandler.class);
    }

    // método chamado no evento de common setup
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // método chamado para cada aba criativa
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        // se a aba criativa for a aba de blocos de construção, adiciona o item example_block
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
            event.accept(ITEM_COM_TEXTURA); // adiciona o bloco_com_textura na aba de blocos de construção
    }

    // método chamado no evento de server starting
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    // define que os metodos de ClientModEvents devem ser exclusivos para o lado do cliente
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        // método chamado no evento de client setup
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}

