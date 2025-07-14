package net.hexagreen.wynntrans.text.tooltip;

import net.hexagreen.wynntrans.text.tooltip.types.*;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public enum TooltipType {
    DIALOG_HISTORY(DialogHistory::new, DialogHistory::typeChecker),
    CONTENT_BOOK(ContentBook::new, ContentBook::typeChecker),
    CONTENT_BOOK_FILTER_AND_SORT(ContentBookFilterAndSort::new, ContentBookFilterAndSort::typeChecker),
    CONTENT_BOOK_CONTENT_PROGRESS(ContentBookProgress::new, ContentBookProgress::typeChecker),
    CONTENT_BOOK_CONTENTS(ContentBookNodes::new, ContentBookNodes::typeChecker),
    COMPASS_MENU(Compass::new, Compass::typeChecker),
    COMPASS_ITEMS(CompassItems::new, CompassItems::typeChecker),
    RECRUIT_PAGE(FriendRecruit::new, FriendRecruit::typeChecker),
    MASTERY_TOME_PAGE(MasteryTomeSlot::new, MasteryTomeSlot::typeChecker),
    BLACKSMITH(Blacksmith::new, Blacksmith::typeChecker),
    IDENTIFIER(Identifier::new, Identifier::typeChecker),
    POWDER(ElementPowder::new, ElementPowder::typeChecker),
    INGREDIENT_POUCH(IngredientPouch::new, IngredientPouch::typeChecker),
    EMERALD_POUCH(EmeraldPouch::new, EmeraldPouch::typeChecker),
    EMERALDS(Emeralds::new, Emeralds::typeChecker),
    MATERIAL(Material::new, Material::typeChecker),
    INGREDIENT(Ingredient::new, Ingredient::typeChecker),
    GATHERING_TOOL(GatheringTool::new, GatheringTool::typeChecker),
    ABILITY_SHARD(AbilityShard::new, AbilityShard::typeChecker),
    ABILITY_TREE_NODE(AbilityTreeNode::new, AbilityTreeNode::typeChecker),
    ABILITY_TREE_ORB(AbilityTreeOrb::new, AbilityTreeOrb::typeChecker),
    ABILITY_TREE_ARCHETYPES(AbilityTreeArchetypes::new, AbilityTreeArchetypes::typeChecker),
    ABILITY_TREE_SELECTOR(AbilityTreeSelector::new, AbilityTreeSelector::typeChecker),
    ABILITY_TREE_RESET(AbilityTreeReset::new, AbilityTreeReset::typeChecker),
    ABILITY_TREE_ASPECT(AbilityTreeAspectSocket::new, AbilityTreeAspectSocket::typeChecker),
    NORMAL_EQUIPMENT(NormalEquipment::new, NormalEquipment::typeChecker),
    UNIDENTIFIED_EQUIPMENT(UnidentifiedEquipment::new, UnidentifiedEquipment::typeChecker),
    CRAFTED_EQUIPMENT(CraftedEquipment::new, CraftedEquipment::typeChecker),
    NORMAL_POTION(NormalPotion::new, NormalPotion::typeChecker),
    CRAFTED_POTION(CraftedPotion::new, CraftedPotion::typeChecker),
    MASTERY_TOME(MasteryTome::new, MasteryTome::typeChecker),
    POTION_BUNDLE(PotionBundle::new, PotionBundle::typeChecker),
    DUNGEON_ITEMS(DungeonItems::new, DungeonItems::typeChecker),
    RUNES(Rune::new, Rune::typeChecker),
    TELEPORTATION_SCROLL(TeleportationScroll::new, TeleportationScroll::typeChecker),
    HORSE(Horse::new, Horse::typeChecker),
    CORKIAN_AUGMENT(CorkianAugment::new, CorkianAugment::typeChecker),

    NO_TYPE(SimpleTooltip::new, null);

    private final Function<List<Text>, WynnTooltipText> wtt;
    private final Predicate<List<Text>> typeChecker;

    private static TooltipType findType(List<Text> text) {
        return Arrays.stream(values())
                .filter(tooltipType -> Objects.nonNull(tooltipType.typeChecker))
                .filter(tooltipType -> tooltipType.typeChecker.test(text))
                .findFirst().orElse(NO_TYPE);
    }

    public static List<Text> findAndRun(List<Text> text) {
        TooltipType find = findType(text);
        if(find == null) return text;
        return find.wtt.apply(text).text();
    }

    TooltipType(Function<List<Text>, WynnTooltipText> wtt, Predicate<List<Text>> typeChecker) {
        this.wtt = wtt;
        this.typeChecker = typeChecker;
    }
}
