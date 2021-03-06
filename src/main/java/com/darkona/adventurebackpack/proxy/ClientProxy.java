package com.darkona.adventurebackpack.proxy;

import com.darkona.adventurebackpack.block.TileAdventureBackpack;
import com.darkona.adventurebackpack.block.TileCampfire;
import com.darkona.adventurebackpack.client.gui.GuiOverlay;
import com.darkona.adventurebackpack.client.models.ModelBackpackArmor;
import com.darkona.adventurebackpack.client.models.ModelCopterPack;
import com.darkona.adventurebackpack.client.models.ModelSteamJetpack;
import com.darkona.adventurebackpack.client.render.*;
import com.darkona.adventurebackpack.config.ConfigHandler;
import com.darkona.adventurebackpack.config.Keybindings;
import com.darkona.adventurebackpack.entity.EntityFriendlySpider;
import com.darkona.adventurebackpack.entity.EntityInflatableBoat;
import com.darkona.adventurebackpack.handlers.KeybindHandler;
import com.darkona.adventurebackpack.handlers.RenderHandler;
import com.darkona.adventurebackpack.init.ModBlocks;
import com.darkona.adventurebackpack.init.ModItems;
import com.darkona.adventurebackpack.playerProperties.BackpackProperty;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created on 10/10/2014
 *
 * @author Darkona
 */
public class ClientProxy implements IProxy
{

    public static RendererItemAdventureBackpack rendererItemAdventureBackpack;
    public static RendererItemAdventureHat rendererItemAdventureHat;
    public static RendererHose rendererHose;
    public static RendererWearableEquipped rendererWearableEquipped;
    public static RenderHandler renderHandler;
    public static RendererInflatableBoat renderInflatableBoat;
    public static RenderRideableSpider renderRideableSpider;
    public static RendererItemClockworkCrossbow renderCrossbow;
    public static ModelSteamJetpack modelSteamJetpack = new ModelSteamJetpack();
    public static ModelBackpackArmor modelAdventureBackpack = new ModelBackpackArmor();
    public static ModelCopterPack modelCopterPack = new ModelCopterPack();


    public void init()
    {
        initRenderers();
        registerKeybindings();
        MinecraftForge.EVENT_BUS.register(new GuiOverlay(Minecraft.getMinecraft()));
    }

    public void initNetwork()
    {

    }

    @Override
    public void joinPlayer(EntityPlayer player)
    {
    }

    @Override
    public void synchronizePlayer(int id, NBTTagCompound properties)
    {
        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(id);
        if(entity != null && entity instanceof EntityPlayer)
        {

            EntityPlayer player = (EntityPlayer)entity;
            if(BackpackProperty.get(player) == null) BackpackProperty.register(player);
            BackpackProperty.get(player).loadNBTData(properties);
            if(player == Minecraft.getMinecraft().thePlayer)
            {
                /*
                if (player.openContainer != null && player.openContainer instanceof IWearableContainer)
                {
                    player.openContainer.detectAndSendChanges();
                }
                */
            }
        }
    }

    public void initRenderers()
    {
        renderHandler = new RenderHandler();
        MinecraftForge.EVENT_BUS.register(renderHandler);
        rendererWearableEquipped = new RendererWearableEquipped();

        rendererItemAdventureBackpack = new RendererItemAdventureBackpack();
        MinecraftForgeClient.registerItemRenderer(ModItems.adventureBackpack, rendererItemAdventureBackpack);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockBackpack), rendererItemAdventureBackpack);
        ClientRegistry.bindTileEntitySpecialRenderer(TileAdventureBackpack.class, new RendererAdventureBackpackBlock());

        rendererItemAdventureHat = new RendererItemAdventureHat();
        MinecraftForgeClient.registerItemRenderer(ModItems.adventureHat, rendererItemAdventureHat);

        if(!ConfigHandler.TANKS_OVERLAY)
        {
            rendererHose = new RendererHose();
            MinecraftForgeClient.registerItemRenderer(ModItems.hose, rendererHose);
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire.class, new RendererCampFire());

        renderInflatableBoat = new RendererInflatableBoat();
        RenderingRegistry.registerEntityRenderingHandler(EntityInflatableBoat.class, renderInflatableBoat);
        renderRideableSpider = new RenderRideableSpider();
        RenderingRegistry.registerEntityRenderingHandler(EntityFriendlySpider.class, renderRideableSpider);

        renderCrossbow = new RendererItemClockworkCrossbow();
        MinecraftForgeClient.registerItemRenderer(ModItems.cwxbow, renderCrossbow);
    }

    public void registerKeybindings()
    {
        ClientRegistry.registerKeyBinding(Keybindings.openBackpack);
        ClientRegistry.registerKeyBinding(Keybindings.toggleHose);
        FMLCommonHandler.instance().bus().register(new KeybindHandler());

    }

}
