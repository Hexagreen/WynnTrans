package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ItemName extends WynnTooltipText {
    private boolean addiction = true;
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

    public ItemName noAddictionMode() {
        this.addiction = false;
        return this;
    }

    public ItemName returnNullMode() {
        this.returnNull = true;
        this.addiction = false;
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
        String normalizedName = normalizeStringForKey(itemName);
        Text uniformedItemName = UniformedItemNames.getTranslatedName(inputText);
        resultText = Text.empty();
        if(WTS.checkTranslationDoNotRegister("wytr.item.ingredient." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.ingredient." + normalizedName).setStyle(nameStyle));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.material." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.material." + normalizedName).setStyle(nameStyle));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.armour." + normalizedName).setStyle(nameStyle));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.weapon." + normalizedName).setStyle(nameStyle));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.accessory." + normalizedName).setStyle(nameStyle));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.tome." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.tome." + normalizedName).setStyle(nameStyle));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.charm." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.charm." + normalizedName).setStyle(nameStyle));
        }
        else if(uniformedItemName != null) {
            resultText.append(uniformedItemName);
        }
        else if(checkTranslation("wytr.item.misc." + normalizedName, itemName)) {
            resultText.append(Text.translatable("wytr.item.misc." + normalizedName).setStyle(nameStyle));
        }
        else {
            if(returnNull) resultText = null;
            else resultText.append(_itemName.copy());
        }
    }

    private boolean checkTranslation(String key, String val) {
        if(addiction) return WTS.checkTranslationExist(key, val);
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
