package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.function.Function;

public class ItemName extends WynnTooltipText {
    private boolean addition = true;
    private boolean returnNull = false;

    public ItemName(List<Text> texts) {
        super(texts);
    }

    public ItemName(Text text) {
        this(textToList(text));
    }

    public ItemName(String itemName) {
        this(Text.literal(itemName));
    }

    public ItemName noAdditionMode() {
        this.addition = false;
        return this;
    }

    public ItemName returnNullMode() {
        this.returnNull = true;
        this.addition = false;
        return this;
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text _itemName = inputText.visit(((style, asString) -> {
            if(!asString.isBlank()) return Optional.of(Text.literal(asString).setStyle(style));
            return Optional.empty();
        }), Style.EMPTY).orElseGet(Text::empty);
        String itemName = _itemName.getString();
        Style nameStyle = _itemName.getStyle();
        Text uniformedItemName = UniformedItemNames.getTranslatedName(inputText);
        resultText = Text.empty();
        if(uniformedItemName != null) {
            resultText.append(uniformedItemName);
        }
        else {
            MutableText singularName = findSingularItemName(itemName);
            if(singularName != null) {
                resultText.append(singularName.setStyle(nameStyle));
                return;
            }
            MutableText foundName = findItemName(itemName, true);
            if(foundName != null) {
                resultText.append(foundName.setStyle(nameStyle));
                return;
            }
            if(returnNull) resultText = null;
            else resultText.append(_itemName.copy());
        }
    }

    private MutableText findSingularItemName(String itemName) {
        List<String> singularForms = new ArrayList<>();
        if(itemName.matches(".+s$")) singularForms.add(itemName.replaceFirst("s$", ""));
        if(itemName.matches(".+es$")) singularForms.add(itemName.replaceFirst("es$", ""));
        if(itemName.matches(".+ies$")) singularForms.add(itemName.replaceFirst("ies$", "y"));

        if(singularForms.isEmpty()) return null;
        for(String singularForm : singularForms) {
            MutableText name = findItemName(singularForm, false);
            if(name == null) continue;
            return name;
        }
        return null;
    }

    private MutableText findItemName(String itemName, boolean addition) {
        String normalizedName = normalizeStringForKey(itemName);

        if(WTS.checkTranslationDoNotRegister("wytr.item.ingredient." + normalizedName)) {
            return Text.translatable("wytr.item.ingredient." + normalizedName);
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.material." + normalizedName)) {
            return Text.translatable("wytr.item.material." + normalizedName);
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalizedName)) {
            return Text.translatable("wytr.item.armour." + normalizedName);
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalizedName)) {
            return Text.translatable("wytr.item.weapon." + normalizedName);
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalizedName)) {
            return Text.translatable("wytr.item.accessory." + normalizedName);
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.tome." + normalizedName)) {
            return Text.translatable("wytr.item.tome." + normalizedName);
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.charm." + normalizedName)) {
            return Text.translatable("wytr.item.charm." + normalizedName);
        }
        else if(!addition && WTS.checkTranslationDoNotRegister("wytr.item.misc." + normalizedName)) {
            return Text.translatable("wytr.item.misc." + normalizedName);
        }
        else if(addition && checkTranslation("wytr.item.misc." + normalizedName, itemName)) {
            return Text.translatable("wytr.item.misc." + normalizedName);
        }
        return null;
    }

    private boolean checkTranslation(String key, String val) {
        if(addition) return WTS.checkTranslationExist(key, val);
        return WTS.checkTranslationDoNotRegister(key);
    }

    private enum UniformedItemNames {
        IdentifiedEquipmentName(NormalEquipment::getTranslatedItemName),
        UnidentifiedEquipmentName(UnidentifiedEquipment::getTranslatedItemName),
        EmeraldPouchName(EmeraldPouch::getTranslatedItemName),
        MaterialName(Material::getTranslatedItemName),
        IngredientName(Ingredient::getTranslatedItemName),
        PowderName(ElementPowder::getTranslatedItemName),
        GatheringToolName(GatheringTool::getTranslatedItemName),
        PotionName(NormalPotion::getTranslatedItemName),
        PotionBundleName(PotionBundle::getTranslatedItemName),
        DungeonItemsName(DungeonItems::getTranslatedItemName),
        TeleportationScrollName(TeleportationScroll::getTranslatedItemName),
        CorkianAugmentName(CorkianAugment::getTranslatedItemName),
        Else(UniformedItemNames::processSimpleItemNames);

        private final Function<Text, MutableText> nameGetter;

        private static Text getTranslatedName(Text val) {
            return Arrays.stream(values())
                    .map(names -> names.nameGetter.apply(val))
                    .filter(Objects::nonNull)
                    .findFirst().orElse(null);
        }

        private static MutableText processSimpleItemNames(Text text) {
            String _string = text.getString();
            if(_string.equals("Character Info")) {
                return Text.translatable("wytr.tooltip.compassMenu")
                        .setStyle(Style.EMPTY.withColor(Formatting.AQUA));
            }
            else if(_string.equals("Content Book")) {
                return Text.translatable("wytr.tooltip.contentBook")
                        .setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
            }
            else return null;
        }

        UniformedItemNames(Function<Text, MutableText> nameGetter) {
            this.nameGetter = nameGetter;
        }
    }
}
