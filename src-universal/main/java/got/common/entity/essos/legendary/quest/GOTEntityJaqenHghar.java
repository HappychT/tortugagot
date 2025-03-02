package got.common.entity.essos.legendary.quest;

import java.util.*;

import got.common.*;
import got.common.database.*;
import got.common.entity.ai.*;
import got.common.entity.other.*;
import got.common.faction.GOTFaction;
import got.common.quest.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GOTEntityJaqenHghar extends GOTEntityHumanBase {
	public GOTEntityJaqenHghar(World world) {
		super(world);
		isImmuneToFrost = true;
		canBeMarried = false;
		addTargetTasks(false);
		setSize(0.6f, 1.8f);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
	}

	public boolean addMQOfferFor(EntityPlayer entityplayer) {
		GOTMiniQuestWelcome quest;
		GOTPlayerData pd = GOTLevelData.getData(entityplayer);
		if (pd.getMiniQuestsForEntity(this, true).isEmpty() && ((GOTMiniQuest) (quest = new GOTMiniQuestWelcome(null, this))).canPlayerAccept(entityplayer)) {
			questInfo.setPlayerSpecificOffer(entityplayer, quest);
			return true;
		}
		return false;
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22);
	}

	public void arriveAt(EntityPlayer entityplayer) {
		ArrayList<EntityPlayer> msgPlayers = new ArrayList<>();
		if (entityplayer != null) {
			msgPlayers.add(entityplayer);
		}
		List worldPlayers = worldObj.playerEntities;
		for (Object obj : worldPlayers) {
			EntityPlayer player = (EntityPlayer) obj;
			if (msgPlayers.contains(player)) {
				continue;
			}
			double d = 64.0;
			double dSq = d * d;
			if (getDistanceSqToEntity(player) >= dSq) {
				continue;
			}
			msgPlayers.add(player);
		}
		for (EntityPlayer player : msgPlayers) {
			GOTSpeech.sendSpeechAndChatMessage(player, this, "legendary/jaqen_arrive");
		}
		doJaqenFX();
	}

	public void depart() {
		ArrayList<EntityPlayer> msgPlayers = new ArrayList<>();
		List worldPlayers = worldObj.playerEntities;
		for (Object obj : worldPlayers) {
			EntityPlayer player = (EntityPlayer) obj;
			if (msgPlayers.contains(player)) {
				continue;
			}
			double d = 64.0;
			double dSq = d * d;
			if (getDistanceSqToEntity(player) >= dSq) {
				continue;
			}
			msgPlayers.add(player);
		}
		for (EntityPlayer player : msgPlayers) {
			GOTSpeech.sendSpeechAndChatMessage(player, this, "legendary/jaqen_depart");
		}
		doJaqenFX();
		setDead();
	}

	public void doJaqenFX() {
		playSound("random.pop", 2.0f, 0.5f + rand.nextFloat() * 0.5f);
		worldObj.setEntityState(this, (byte) 16);
	}

	@Override
	public float getAlignmentBonus() {
		return 300.0f;
	}

	@Override
	public GOTFaction getFaction() {
		return GOTFaction.HIGH_POWER;
	}

	@Override
	public GOTAchievement getKillAchievement() {
		return GOTAchievement.killJaqenHghar;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			return "legendary/jaqen_friendly";
		}
		return "legendary/jaqen_hostile";
	}

	@Override
	public int getTotalArmorValue() {
		return 15;
	}

	@Override
	public void onArtificalSpawn() {
		GOTJaqenHgharTracker.addNewWanderer(getUniqueID());
		arriveAt(null);
	}

	@Override
	public void onAttackModeChange(GOTEntityNPC.AttackMode mode, boolean mounted) {
		if (mode == GOTEntityNPC.AttackMode.IDLE) {
			setCurrentItemOrArmor(0, npcItemsInv.getIdleItem());
		} else {
			setCurrentItemOrArmor(0, npcItemsInv.getMeleeWeapon());
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!worldObj.isRemote && !GOTJaqenHgharTracker.isWandererActive(getUniqueID()) && getAttackTarget() == null) {
			depart();
		}
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(GOTRegistry.essosSword));
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		return data;
	}

	@Override
	public void setupNPCGender() {
		familyInfo.setMale(true);
	}

}
