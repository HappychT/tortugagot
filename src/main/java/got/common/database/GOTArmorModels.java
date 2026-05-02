package got.common.database;

import java.util.*;

import got.client.model.thiefArmor.GOTModelThiefBoots;
import got.client.model.thiefArmor.GOTModelThiefChestplate;
import got.client.model.thiefArmor.GOTModelThiefHelmet;
import got.client.model.thiefArmor.GOTModelThiefLeggings;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.client.model.*;
import got.common.entity.other.GOTEntityNPC;
import got.common.item.other.*;
import got.common.item.weapon.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.handler.data.IAnimationData;
import noppes.npcs.api.item.IItemStack;

public class GOTArmorModels {
	public static GOTArmorModels INSTANCE;
	public Map<ModelBiped, Map<Item, ModelBiped>> specialArmorModels = new HashMap<>();

	public GOTArmorModels() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void copyBoxRotations(ModelRenderer target, ModelRenderer src) {
		target.rotationPointX = src.rotationPointX;
		target.rotationPointY = src.rotationPointY;
		target.rotationPointZ = src.rotationPointZ;
		target.rotateAngleX = src.rotateAngleX;
		target.rotateAngleY = src.rotateAngleY;
		target.rotateAngleZ = src.rotateAngleZ;
	}

	public void copyModelRotations(ModelBiped target, ModelBiped src) {
		copyBoxRotations(target.bipedHead, src.bipedHead);
		copyBoxRotations(target.bipedHeadwear, src.bipedHeadwear);
		copyBoxRotations(target.bipedBody, src.bipedBody);
		copyBoxRotations(target.bipedRightArm, src.bipedRightArm);
		copyBoxRotations(target.bipedLeftArm, src.bipedLeftArm);
		copyBoxRotations(target.bipedRightLeg, src.bipedRightLeg);
		copyBoxRotations(target.bipedLeftLeg, src.bipedLeftLeg);
	}

	public ModelBiped getRendererMainBiped(EntityLivingBase entity) {
		if (entity == null) {
			return null;
		}
		try {
			Render render = RenderManager.instance.getEntityRenderObject(entity);
			if (!(render instanceof RendererLivingEntity)) {
				return null;
			}
			Class<?> type = RendererLivingEntity.class;
			while (type != null) {
				try {
					java.lang.reflect.Field field = type.getDeclaredField("mainModel");
					field.setAccessible(true);
					Object value = field.get(render);
					if (value instanceof ModelBiped) {
						return (ModelBiped) value;
					}
					return null;
				} catch (NoSuchFieldException ignored) {
					type = type.getSuperclass();
				}
			}
		} catch (Throwable ignored) {
		}
		return null;
	}

	public void syncArmorModelWithRenderer(ModelBiped armorModel, EntityLivingBase entity) {
		ModelBiped rendererModel = getRendererMainBiped(entity);
		if (armorModel != null && rendererModel != null) {
			copyModelRotations(armorModel, rendererModel);
			armorModel.onGround = rendererModel.onGround;
			armorModel.isRiding = rendererModel.isRiding;
			armorModel.isChild = rendererModel.isChild;
			armorModel.isSneak = rendererModel.isSneak;
			armorModel.heldItemLeft = rendererModel.heldItemLeft;
			armorModel.heldItemRight = rendererModel.heldItemRight;
			armorModel.aimedBow = rendererModel.aimedBow;
		}
	}

	public int getEntityArmorModel(RendererLivingEntity renderer, ModelBiped mainModel, EntityLivingBase entity, ItemStack armor, int slot) {
		ModelBiped armorModel = getSpecialArmorModel(armor, slot, entity, mainModel);
		if (armorModel != null) {
			int color;
			Item armorItem;
			if (armor != null) {
				armorItem = armor.getItem();
			} else {
				armorItem = null;
			}
			if (armorItem instanceof ItemArmor) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(RenderBiped.getArmorResource(entity, armor, slot, null));
			}
			renderer.setRenderPassModel(armorModel);
			setupModelForRender(armorModel, mainModel, entity);
			if (armorItem instanceof ItemArmor && (color = ((ItemArmor) armorItem).getColor(armor)) != -1) {
				float r = (color >> 16 & 0xFF) / 255.0f;
				float g = (color >> 8 & 0xFF) / 255.0f;
				float b = (color & 0xFF) / 255.0f;
				GL11.glColor3f(r, g, b);
				if (armor.isItemEnchanted()) {
					return 31;
				}
				return 16;
			}
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			if (armor!= null && armor.isItemEnchanted()) {
				return 15;
			}
			return 1;
		}
		return 0;
	}

	@SubscribeEvent
	public void getPlayerArmorModel(RenderPlayerEvent.SetArmorModel event) {
		RenderPlayer renderer = event.renderer;
		ModelBiped mainModel = renderer.modelBipedMain;
		EntityPlayer entityplayer = event.entityPlayer;
		ItemStack armor = event.stack;
		int slot = event.slot;
		int result = getEntityArmorModel(renderer, mainModel, entityplayer, armor, 3 - slot);
		if (result > 0) {
			event.result = result;
		}
	}

	public ModelBiped getSpecialArmorModel(ItemStack itemstack, int slot, EntityLivingBase entity, ModelBiped mainModel) {
		if (itemstack == null) {
			return null;
		}
		Item item = itemstack.getItem();
		ModelBiped model = getSpecialModels(mainModel).get(item);
		if (model == null) {
			return null;
		}
		if (model instanceof GOTModelLeatherHat) {
			((GOTModelLeatherHat) model).setHatItem(itemstack);
		}
		if (model instanceof GOTModelRobes) {
			((GOTModelRobes) model).setRobeItem(itemstack);
		}
		if (model instanceof GOTModelPartyHat) {
			((GOTModelPartyHat) model).setHatItem(itemstack);
		}
		if (model instanceof GOTModelPlateHead) {
			((GOTModelPlateHead) model).setPlateItem(itemstack);
		}
		copyModelRotations(model, mainModel);
		setupArmorForSlot(model, slot);
		return model;
	}

	public Map<Item, ModelBiped> getSpecialModels(ModelBiped key) {
		Map<Item, ModelBiped> map = specialArmorModels.get(key);
		if (map == null) {
			map = new HashMap<>();
			map.put(GOTRegistry.targaryenHelmet, new GOTModelTargaryenHelmet());
			map.put(GOTRegistry.targaryenChestplate, new GOTModelTargaryenChestplate());
			map.put(GOTRegistry.asshaiHelmet, new GOTModelAsshaiHelmet(1.0f));
			map.put(GOTRegistry.bittersteelHelmet, new GOTModelSandorHelmet(1.0f));
			map.put(GOTRegistry.blackfyreHelmet, new GOTModelWingedHelmet(1.0f));
			map.put(GOTRegistry.braavosHelmet, new GOTModelBraavosHelmet(1.0f));
			map.put(GOTRegistry.ceramicPlate, new GOTModelPlateHead());
			map.put(GOTRegistry.gemsbokHelmet, new GOTModelGemsbokHelmet(1.0f));
			map.put(GOTRegistry.harpy, new GOTModelHarpy(1.0f));
			map.put(GOTRegistry.leatherHat, new GOTModelLeatherHat());
			map.put(GOTRegistry.lhazarHelmetLion, new GOTModelSandorHelmet(1.0f));
			map.put(GOTRegistry.partyHat, new GOTModelPartyHat(0.6f));
			map.put(GOTRegistry.plate, new GOTModelPlateHead());
			map.put(GOTRegistry.reachguardHelmet, new GOTModelReachHelmet(1.0f));
			map.put(GOTRegistry.robesBoots, new GOTModelRobes(1.0f));
			map.put(GOTRegistry.robesChestplate, new GOTModelRobes(1.0f));
			map.put(GOTRegistry.robesHelmet, new GOTModelTurban());
			map.put(GOTRegistry.robesLeggings, new GOTModelRobes(0.5f));
			map.put(GOTRegistry.royceHelmet, new GOTModelRoyceHelmet(1.0f));
			map.put(GOTRegistry.sothoryosHelmetChieftain, new GOTModelDeerHelmet(1.0f));
			map.put(GOTRegistry.sothoryosHelmetGold, new GOTModelWesterlandsHelmet(1.0f));
			map.put(GOTRegistry.summerChestplate, new GOTModelSummerChestplate(1.0f));
			map.put(GOTRegistry.hillmenHelmet, new GOTModelWingedHelmet(1.0f));
			map.put(GOTRegistry.unsulliedHelmet, new GOTModelUnsulliedHelmet(1.0f));
			map.put(GOTRegistry.valyrianHelmet, new GOTModelWingedHelmet(1.0f));
			map.put(GOTRegistry.victarionHelmet, new GOTModelWingedHelmet(1.0f));
			map.put(GOTRegistry.westkingHelmet, new GOTModelWesterlandsHelmet(1.0f));
			map.put(GOTRegistry.woodPlate, new GOTModelPlateHead());
			map.put(GOTRegistry.yitiHelmetSamurai, new GOTModelYiTiHelmet(1.0f, false));
			map.put(GOTRegistry.yitiHelmetShogune, new GOTModelYiTiHelmet(1.0f, true));
			map.put(GOTRegistry.thiefHelmet, new GOTModelThiefHelmet());
			map.put(GOTRegistry.thiefChestplate, new GOTModelThiefChestplate());
			map.put(GOTRegistry.thiefLeggings, new GOTModelThiefLeggings());
			map.put(GOTRegistry.thiefBoots, new GOTModelThiefBoots());
			for (ModelBiped armorModel : map.values()) {
				copyModelRotations(armorModel, key);
			}
			specialArmorModels.put(key, map);
		}
		return map;
	}

	@SubscribeEvent
	public void preRenderEntity(RenderLivingEvent.Pre event) {
		EntityLivingBase entity = event.entity;
		RendererLivingEntity renderer = event.renderer;
		if (entity instanceof EntityPlayer && renderer instanceof RenderPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entity;
			RenderPlayer renderplayer = (RenderPlayer) renderer;
			ModelBiped mainModel = renderplayer.modelBipedMain;
			ModelBiped armorModel1 = renderplayer.modelArmorChestplate;
			ModelBiped armorModel2 = renderplayer.modelArmor;
			setupModelForRender(mainModel, mainModel, entityplayer);
			setupModelForRender(armorModel1, mainModel, entityplayer);
			setupModelForRender(armorModel2, mainModel, entityplayer);
		}
	}

	public void setupArmorForSlot(ModelBiped model, int slot) {
		model.bipedHead.showModel = slot == 0;
		model.bipedHeadwear.showModel = slot == 0;
		model.bipedBody.showModel = slot == 1 || slot == 2;
		model.bipedRightArm.showModel = slot == 1;
		model.bipedLeftArm.showModel = slot == 1;
		model.bipedRightLeg.showModel = slot == 2 || slot == 3;
		model.bipedLeftLeg.showModel = slot == 2 || slot == 3;
	}

	public static boolean usesBowArmPose(ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() instanceof ItemBow;
	}

	public ICustomNpc<?> getCustomNpc(EntityLivingBase entity) {
		if (entity == null || !AbstractNpcAPI.IsAvailable()) {
			return null;
		}
		AbstractNpcAPI api = AbstractNpcAPI.Instance();
		if (api == null) {
			return null;
		}
		IEntity<?> wrapped = api.getIEntity(entity);
		if (wrapped instanceof ICustomNpc) {
			return (ICustomNpc<?>) wrapped;
		}
		return null;
	}

	public ItemStack unwrapItemStack(IItemStack itemStack) {
		if (itemStack == null) {
			return null;
		}
		return itemStack.getMCItemStack();
	}

	public boolean safeCustomNpcIsAttacking(ICustomNpc<?> customNpc) {
		try {
			return customNpc != null && customNpc.isAttacking();
		} catch (Throwable ignored) {
			return false;
		}
	}

	public byte safeCustomNpcAimType(ICustomNpc<?> customNpc) {
		try {
			return customNpc == null ? 0 : customNpc.getAimType();
		} catch (Throwable ignored) {
			return 0;
		}
	}

	public IAnimationData safeCustomNpcAnimationData(ICustomNpc<?> customNpc) {
		try {
			return customNpc == null ? null : customNpc.getAnimationData();
		} catch (Throwable ignored) {
			return null;
		}
	}

	public boolean safeAnimationActive(IAnimationData animationData) {
		try {
			return animationData != null && animationData.isActive();
		} catch (Throwable ignored) {
			return false;
		}
	}

	public ItemStack safeCustomNpcItem(IItemStack itemStack) {
		try {
			return unwrapItemStack(itemStack);
		} catch (Throwable ignored) {
			return null;
		}
	}

	public boolean isCustomNpcAiming(EntityLivingBase entity) {
		ICustomNpc<?> customNpc = getCustomNpc(entity);
		if (customNpc == null) {
			return false;
		}
		if (safeCustomNpcIsAttacking(customNpc)) {
			return true;
		}
		return safeCustomNpcAimType(customNpc) != 0 || safeAnimationActive(safeCustomNpcAnimationData(customNpc));
	}

	public ItemStack getHeldItemLeft(EntityLivingBase entity) {
		if (entity instanceof GOTEntityNPC) {
			return ((GOTEntityNPC) entity).getHeldItemLeft();
		}
		ICustomNpc<?> customNpc = getCustomNpc(entity);
		if (customNpc != null) {
			try {
				return safeCustomNpcItem(customNpc.getLeftItem());
			} catch (Throwable ignored) {
				return null;
			}
		}
		return null;
	}

	public ItemStack getHeldItemRight(EntityLivingBase entity) {
		ICustomNpc<?> customNpc = getCustomNpc(entity);
		if (customNpc != null) {
			try {
				return safeCustomNpcItem(customNpc.getRightItem());
			} catch (Throwable ignored) {
				return entity == null ? null : entity.getHeldItem();
			}
		}
		return entity == null ? null : entity.getHeldItem();
	}

	public void setupHeldItem(ModelBiped model, EntityLivingBase entity, ItemStack itemstack, boolean rightArm) {
		int value = 0;
		boolean aimBow = false;
		if (itemstack != null) {
			value = 1;
			Item item = itemstack.getItem();
			boolean isRanged = false;
			if (usesBowArmPose(itemstack)) {
				if (item instanceof GOTItemSpear) {
					isRanged = entity instanceof EntityPlayer;
				} else {
					isRanged = !(item instanceof ItemSword);
				}
			}
			if (item instanceof GOTItemSling) {
				isRanged = true;
			}
			if (isRanged) {
				boolean aiming = model.aimedBow;
				if (entity instanceof EntityPlayer && GOTItemCrossbow.isLoaded(itemstack)) {
					aiming = true;
				}
				if (entity instanceof GOTEntityNPC) {
					aiming = ((GOTEntityNPC) entity).clientCombatStance;
				}
				if (getCustomNpc(entity) != null) {
					aiming = isCustomNpcAiming(entity);
				}
				if (aiming) {
					value = 3;
					aimBow = true;
				}
			}
			if (item instanceof GOTItemBanner) {
				value = 3;
			}
			if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getItemInUseCount() > 0 && itemstack.getItemUseAction() == EnumAction.block) {
				value = 3;
			}
			if (entity instanceof GOTEntityNPC && ((GOTEntityNPC) entity).clientIsEating) {
				value = 3;
			}
		}
		if (rightArm) {
			model.heldItemRight = value;
			model.aimedBow = aimBow;
		} else {
			model.heldItemLeft = value;
		}
	}

	public void setupModelForRender(ModelBiped model, ModelBiped mainModel, EntityLivingBase entity) {
		if (mainModel != null) {
			model.onGround = mainModel.onGround;
			model.isRiding = mainModel.isRiding;
			model.isChild = mainModel.isChild;
			model.isSneak = mainModel.isSneak;
		} else {
			model.onGround = 0.0f;
			model.isRiding = false;
			model.isChild = false;
			model.isSneak = false;
		}
		if (entity instanceof GOTEntityNPC) {
			model.bipedHeadwear.showModel = ((GOTEntityNPC) entity).shouldRenderNPCHair();
		}
		if (mainModel != null) {
			model.aimedBow = mainModel.aimedBow;
		}
		if (entity instanceof EntityPlayer) {
			ItemStack heldRight = entity.getHeldItem();
			setupHeldItem(model, entity, heldRight, true);
		} else {
			ItemStack heldRight = getHeldItemRight(entity);
			ItemStack heldLeft = getHeldItemLeft(entity);
			setupHeldItem(model, entity, heldRight, true);
			setupHeldItem(model, entity, heldLeft, false);
		}
	}

	public static void preInit() {
		INSTANCE = new GOTArmorModels();
	}
}
