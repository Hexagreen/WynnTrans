package net.hexagreen.wynntrans.mixin;

import com.wynntils.core.text.StyledText;
import com.wynntils.models.activities.type.ActivityInfo;
import com.wynntils.models.activities.type.ActivityRewardType;
import com.wynntils.models.activities.type.ActivityTrackingState;
import com.wynntils.screens.activities.ContentBookHolder;
import com.wynntils.screens.activities.WynntilsContentBookScreen;
import com.wynntils.utils.type.Pair;
import net.hexagreen.wynntrans.text.WynntilsCompatible;
import net.hexagreen.wynntrans.text.tooltip.types.ContentBookNodes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(WynntilsContentBookScreen.class)
public abstract class MixinWynntilsContentBookScreen {
    @Unique
    private ActivityInfo trackedActivityInfoCache = null;

    @Redirect(method = "reloadContentBookWidgets", at = @At(value = "INVOKE", target = "Lcom/wynntils/screens/activities/ContentBookHolder;getActivities()Ljava/util/Map;"), remap = false)
    public Map<?, ?> getActivitiesMixin(ContentBookHolder instance) {
        Map<?, ?> original = instance.getActivities();
        Map<Integer, Pair<ItemStack, ActivityInfo>> modified = new TreeMap<>();

        for(Map.Entry<?, ?> entry : original.entrySet()) {
            if(entry.getKey() instanceof Integer key
                    && entry.getValue() instanceof Pair<?, ?>(ItemStack i, ActivityInfo a)) {
                ActivityInfo modifiedActivityInfo = modifyActivityInfo(a);

                Pair<ItemStack, ActivityInfo> modifiedPair = Pair.of(i, modifiedActivityInfo);
                modified.put(key, modifiedPair);
            }
        }

        return modified;
    }

    @Redirect(method = "reloadContentBookWidgets", at = @At(value = "INVOKE", target = "Lcom/wynntils/screens/activities/ContentBookHolder;getTrackedActivityInfo()Ljava/util/Optional;"), remap = false)
    public Optional<ActivityInfo> getTrackedActivityInfoMixin(ContentBookHolder instance) {
        if(trackedActivityInfoCache != null) return Optional.of(trackedActivityInfoCache);
        else return Optional.empty();
    }

    @Unique
    private ActivityInfo modifyActivityInfo(ActivityInfo activityInfo) {
        List<Text> dummy = new ArrayList<>();
        dummy.add(Text.of("dummy"));

        ContentBookNodes contentBookNodes = new ContentBookNodes(dummy);
        Map<ActivityRewardType, List<StyledText>> rewards = activityInfo.rewards();
        StyledText description = activityInfo.description().orElse(null);
        List<StyledText> accessRewards = rewards.getOrDefault(ActivityRewardType.ACCESS, null);
        List<StyledText> itemRewards = rewards.getOrDefault(ActivityRewardType.ITEM, null);
        ContentBookNodes.WynntilsContentBookNode node =
                contentBookNodes.processWynntilsContentNode(
                        activityInfo.type().getDisplayName(),
                        activityInfo.name(),
                        description == null ? null : WynntilsCompatible.getTextFromWynntilsText(description),
                        accessRewards == null ? null : accessRewards.stream().map(WynntilsCompatible::getTextFromWynntilsText).toList(),
                        itemRewards == null ? null : itemRewards.stream().map(WynntilsCompatible::getTextFromWynntilsText).toList());

        Map<ActivityRewardType, List<StyledText>> modifiedRewards = new TreeMap<>();
        for(Map.Entry<ActivityRewardType, List<StyledText>> entry : activityInfo.rewards().entrySet()) {
            if(entry.getKey().equals(ActivityRewardType.ACCESS)) {
                modifiedRewards.put(ActivityRewardType.ACCESS, node.accessReward().stream().map(WynntilsCompatible::getWynntilsTextFromText).toList());
            }
            else if(entry.getKey().equals(ActivityRewardType.ITEM)) {
                modifiedRewards.put(ActivityRewardType.ITEM, node.itemReward().stream().map(WynntilsCompatible::getWynntilsTextFromText).toList());
            }
            else modifiedRewards.put(entry.getKey(), entry.getValue());
        }

        ActivityInfo modifiedActivityInfo = new ActivityInfo(activityInfo.type(), node.name(), activityInfo.status(),
                activityInfo.specialInfo(), Optional.ofNullable(WynntilsCompatible.getWynntilsTextFromText(node.description())),
                activityInfo.length(), activityInfo.lengthInfo(), activityInfo.distance(), activityInfo.distanceInfo(),
                activityInfo.difficulty(), activityInfo.requirements(), modifiedRewards, activityInfo.trackingState());

        if(activityInfo.trackingState().equals(ActivityTrackingState.TRACKED))
            trackedActivityInfoCache = modifiedActivityInfo;
        return modifiedActivityInfo;
    }
}
