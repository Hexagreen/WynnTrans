package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        String itemName = getSibling(0).getString();
        String normalizedName = normalizeStringForKey(itemName);
        Text uniformedItemName = UniformedItemNames.getTranslatedName(getSibling(0));
        resultText = Text.empty();
        if(WTS.checkTranslationDoNotRegister("wytr.item.ingredient." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.ingredient." + normalizedName).setStyle(getStyle(0)));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.material." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.material." + normalizedName).setStyle(getStyle(0)));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.armour." + normalizedName).setStyle(getStyle(0)));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.weapon." + normalizedName).setStyle(getStyle(0)));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.accessory." + normalizedName).setStyle(getStyle(0)));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.tome." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.tome." + normalizedName).setStyle(getStyle(0)));
        }
        else if(WTS.checkTranslationDoNotRegister("wytr.item.charm." + normalizedName)) {
            resultText.append(Text.translatable("wytr.item.charm." + normalizedName).setStyle(getStyle(0)));
        }
        else if(uniformedItemName != null) {
            resultText.append(uniformedItemName);
        }
        else if(checkTranslation("wytr.item.misc." + normalizedName, itemName)) {
            resultText.append(Text.translatable("wytr.item.misc." + normalizedName).setStyle(getStyle(0)));
        }
        else {
            Text _val = getSibling(0);

            if(returnNull) resultText = null;
            else resultText.append(_val.copy());
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
        EmeraldName(Emeralds::getTranslatedItemName),
        MaterialName(Material::getTranslatedItemName),
        IngredientName(Ingredient::getTranslatedItemName),
        PowderName(ElementPowder::getTranslatedItemName),
        GatheringToolName(GatheringTool::getTranslatedItemName),
        PotionName(NormalPotion::getTranslatedItemName),
        PotionBundleName(PotionBundle::getTranslatedItemName),
        DungeonItemsName(DungeonItems::getTranslatedItemName),
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
