package com.tortugagot.togcore.technology;

import com.tortugagot.togcore.network.TOGPacketHandler;
import com.tortugagot.togcore.network.TOGPacketTechnologySync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TOGTechnologyPlayerData implements IExtendedEntityProperties {
    public static final String EXT_PROP_NAME = "TOGTechnologyData";

    private final EntityPlayer player;
    private int masteryPoints;
    private final Set<String> unlockedTechnologies = new LinkedHashSet<String>();
    private final Map<String, Integer> spentTechnologyPoints = new LinkedHashMap<String, Integer>();
    private boolean persistentLoaded;

    public TOGTechnologyPlayerData(EntityPlayer player) {
        this.player = player;
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(EXT_PROP_NAME, new TOGTechnologyPlayerData(player));
    }

    public static TOGTechnologyPlayerData get(EntityPlayer player) {
        return (TOGTechnologyPlayerData) player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        if (persistentLoaded) {
            saveToPersistentStore();
        }
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger("MasteryPoints", masteryPoints);
        NBTTagList unlocked = new NBTTagList();
        for (String id : unlockedTechnologies) {
            unlocked.appendTag(new NBTTagString(id));
        }
        properties.setTag("Unlocked", unlocked);
        NBTTagList spent = new NBTTagList();
        for (Map.Entry<String, Integer> entry : spentTechnologyPoints.entrySet()) {
            NBTTagCompound spentTag = new NBTTagCompound();
            spentTag.setString("Id", entry.getKey());
            spentTag.setInteger("Cost", Math.max(0, entry.getValue()));
            spent.appendTag(spentTag);
        }
        properties.setTag("SpentTechnologyPoints", spent);
        compound.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        masteryPoints = 0;
        unlockedTechnologies.clear();
        spentTechnologyPoints.clear();
        persistentLoaded = false;
        if (!compound.hasKey(EXT_PROP_NAME)) {
            return;
        }
        NBTTagCompound properties = compound.getCompoundTag(EXT_PROP_NAME);
        masteryPoints = Math.max(0, properties.getInteger("MasteryPoints"));
        NBTTagList unlocked = properties.getTagList("Unlocked", 8);
        for (int i = 0; i < unlocked.tagCount(); i++) {
            String id = unlocked.getStringTagAt(i);
            if (TOGTechnologyRegistry.get(id) != null) {
                unlockedTechnologies.add(id);
            }
        }
        if (properties.hasKey("SpentTechnologyPoints")) {
            NBTTagList spent = properties.getTagList("SpentTechnologyPoints", 10);
            for (int i = 0; i < spent.tagCount(); i++) {
                NBTTagCompound spentTag = spent.getCompoundTagAt(i);
                String id = spentTag.getString("Id");
                if (unlockedTechnologies.contains(id)) {
                    spentTechnologyPoints.put(id, Math.max(0, spentTag.getInteger("Cost")));
                }
            }
        } else {
            rebuildLegacySpentTechnologyPoints();
        }
        for (String id : unlockedTechnologies) {
            if (!spentTechnologyPoints.containsKey(id)) {
                spentTechnologyPoints.put(id, 0);
            }
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public void copyFrom(TOGTechnologyPlayerData oldData) {
        oldData.ensurePersistentLoaded();
        masteryPoints = oldData.masteryPoints;
        unlockedTechnologies.clear();
        unlockedTechnologies.addAll(oldData.unlockedTechnologies);
        spentTechnologyPoints.clear();
        spentTechnologyPoints.putAll(oldData.spentTechnologyPoints);
        persistentLoaded = true;
        saveToPersistentStore();
    }

    public int getMasteryPoints() {
        ensurePersistentLoaded();
        return masteryPoints;
    }

    public void setMasteryPoints(int masteryPoints) {
        ensurePersistentLoaded();
        this.masteryPoints = Math.max(0, masteryPoints);
        saveToPersistentStore();
    }

    public void addMasteryPoints(int amount) {
        ensurePersistentLoaded();
        if (amount <= 0) {
            return;
        }
        if (masteryPoints > Integer.MAX_VALUE - amount) {
            setMasteryPoints(Integer.MAX_VALUE);
            return;
        }
        setMasteryPoints(masteryPoints + amount);
    }

    public int removeMasteryPoints(int amount) {
        ensurePersistentLoaded();
        int removed = Math.min(masteryPoints, Math.max(0, amount));
        masteryPoints -= removed;
        saveToPersistentStore();
        return removed;
    }

    public Set<String> getUnlockedTechnologies() {
        ensurePersistentLoaded();
        return new LinkedHashSet<String>(unlockedTechnologies);
    }

    public int getSpentTechnologyPoints() {
        ensurePersistentLoaded();
        int spent = 0;
        for (Integer cost : spentTechnologyPoints.values()) {
            if (cost != null && cost > 0) {
                spent += cost;
            }
        }
        return spent;
    }

    public int getUnlockedCount() {
        ensurePersistentLoaded();
        return unlockedTechnologies.size();
    }

    public boolean hasUnlocked(String id) {
        ensurePersistentLoaded();
        return unlockedTechnologies.contains(id);
    }

    public int getUnlockCost(String id) {
        ensurePersistentLoaded();
        TOGTechnology technology = TOGTechnologyRegistry.get(id);
        return technology != null ? technology.getUnlockCost(getUnlockedCount()) : 0;
    }

    public String getBlockedReason(String id) {
        ensurePersistentLoaded();
        TOGTechnology technology = TOGTechnologyRegistry.get(id);
        if (technology == null) {
            return "Технология не найдена.";
        }
        if (hasUnlocked(id)) {
            return "Технология уже открыта.";
        }
        String exclusiveChoiceId = TOGTechnologyRules.getExclusiveStaminaChoiceId(id);
        if (exclusiveChoiceId != null && hasUnlocked(exclusiveChoiceId)) {
            return "Уже выбран альтернативный узел стамины: " + getTechnologyName(exclusiveChoiceId) + ". Второй вариант доступен только после полного сброса древа.";
        }
        int unlockCost = getUnlockCost(id);
        if (masteryPoints < unlockCost) {
            return "Недостаточно очков мастерства: нужно " + unlockCost + ".";
        }
        for (String prerequisiteId : technology.getPrerequisites()) {
            if (!hasUnlocked(prerequisiteId)) {
                TOGTechnology prerequisite = TOGTechnologyRegistry.get(prerequisiteId);
                String name = prerequisite != null ? prerequisite.getName() : prerequisiteId;
                return "Не открыта предыдущая технология: " + name + ".";
            }
        }
        String[] requiredChoiceGroup = TOGTechnologyRules.getRequiredStaminaChoiceGroup(id);
        if (requiredChoiceGroup != null && !hasUnlocked(requiredChoiceGroup[0]) && !hasUnlocked(requiredChoiceGroup[1])) {
            return "Сначала выберите один из узлов " + TOGTechnologyRules.getRequiredStaminaChoiceName(id) + ": " + getTechnologyName(requiredChoiceGroup[0]) + " или " + getTechnologyName(requiredChoiceGroup[1]) + ".";
        }
        return null;
    }

    public boolean unlock(String id) {
        ensurePersistentLoaded();
        String reason = getBlockedReason(id);
        if (reason != null) {
            return false;
        }
        int unlockCost = getUnlockCost(id);
        masteryPoints -= unlockCost;
        unlockedTechnologies.add(id);
        spentTechnologyPoints.put(id, unlockCost);
        saveToPersistentStore();
        return true;
    }

    public boolean openTechnology(String id) {
        ensurePersistentLoaded();
        if (TOGTechnologyRegistry.get(id) == null || hasUnlocked(id)) {
            return false;
        }
        unlockedTechnologies.add(id);
        spentTechnologyPoints.put(id, 0);
        saveToPersistentStore();
        return true;
    }

    public int openAllTechnologies() {
        ensurePersistentLoaded();
        int opened = 0;
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            if (openTechnology(technology.getId())) {
                opened++;
            }
        }
        return opened;
    }

    public int closeTechnologyTree(String id) {
        ensurePersistentLoaded();
        if (!hasUnlocked(id)) {
            return 0;
        }
        Set<String> toClose = new LinkedHashSet<String>();
        collectTechnologyAndDependents(id, toClose);
        int closed = 0;
        for (String closeId : toClose) {
            if (unlockedTechnologies.remove(closeId)) {
                spentTechnologyPoints.remove(closeId);
                closed++;
            }
        }
        if (closed > 0) {
            saveToPersistentStore();
        }
        return closed;
    }

    public int resetTechnologies() {
        ensurePersistentLoaded();
        int refund = getSpentTechnologyPoints();
        addMasteryPoints(refund);
        unlockedTechnologies.clear();
        spentTechnologyPoints.clear();
        saveToPersistentStore();
        return refund;
    }

    public void sync() {
        ensurePersistentLoaded();
        saveToPersistentStore();
        if (player instanceof EntityPlayerMP) {
            TOGPacketHandler.networkWrapper.sendTo(new TOGPacketTechnologySync(masteryPoints, unlockedTechnologies), (EntityPlayerMP) player);
        }
    }

    void applyPersistentState(int masteryPoints, Set<String> unlockedTechnologies, Map<String, Integer> spentTechnologyPoints) {
        this.masteryPoints = Math.max(0, masteryPoints);
        this.unlockedTechnologies.clear();
        this.spentTechnologyPoints.clear();
        if (unlockedTechnologies != null) {
            for (String id : unlockedTechnologies) {
                if (TOGTechnologyRegistry.get(id) != null) {
                    this.unlockedTechnologies.add(id);
                }
            }
        }
        if (spentTechnologyPoints != null) {
            for (Map.Entry<String, Integer> entry : spentTechnologyPoints.entrySet()) {
                String id = entry.getKey();
                if (this.unlockedTechnologies.contains(id)) {
                    Integer cost = entry.getValue();
                    this.spentTechnologyPoints.put(id, cost != null ? Math.max(0, cost) : 0);
                }
            }
        }
        for (String id : this.unlockedTechnologies) {
            if (!this.spentTechnologyPoints.containsKey(id)) {
                this.spentTechnologyPoints.put(id, 0);
            }
        }
    }

    public void loadFromPersistentStore() {
        if (persistentLoaded) {
            return;
        }
        if (player == null || player.worldObj == null || player.worldObj.isRemote) {
            persistentLoaded = true;
            return;
        }
        TOGTechnologySavedData savedData = TOGTechnologySavedData.get(player.worldObj);
        if (savedData != null && savedData.loadPlayer(player.getUniqueID(), this)) {
            persistentLoaded = true;
            return;
        }
        persistentLoaded = true;
        saveToPersistentStore();
    }

    private void ensurePersistentLoaded() {
        if (!persistentLoaded) {
            loadFromPersistentStore();
        }
    }

    private void saveToPersistentStore() {
        if (player == null || player.worldObj == null || player.worldObj.isRemote) {
            return;
        }
        TOGTechnologySavedData savedData = TOGTechnologySavedData.get(player.worldObj);
        if (savedData != null) {
            savedData.savePlayer(player.getUniqueID(), masteryPoints, unlockedTechnologies, spentTechnologyPoints);
        }
    }

    private void rebuildLegacySpentTechnologyPoints() {
        int unlockedIndex = 0;
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            if (unlockedTechnologies.contains(technology.getId())) {
                spentTechnologyPoints.put(technology.getId(), technology.getUnlockCost(unlockedIndex));
                unlockedIndex++;
            }
        }
    }

    private void collectTechnologyAndDependents(String id, Set<String> toClose) {
        if (!toClose.add(id)) {
            return;
        }
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            if (hasUnlocked(technology.getId()) && (technology.getPrerequisites().contains(id) || TOGTechnologyRules.isRequiredStaminaChoiceFor(id, technology.getId()))) {
                collectTechnologyAndDependents(technology.getId(), toClose);
            }
        }
    }

    private String getTechnologyName(String id) {
        TOGTechnology technology = TOGTechnologyRegistry.get(id);
        return technology != null ? technology.getName() : id;
    }
}
