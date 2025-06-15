package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ItemName extends WynnTooltipText {
    private boolean addiction = true;
    private boolean returnNull = false;

    public static boolean typeChecker(List<Text> texts) {
        return false;
    }

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
        else if(checkTranslation("wytr.item.misc." + normalizedName, itemName)) {
            resultText.append(Text.translatable("wytr.item.misc." + normalizedName).setStyle(getStyle(0)));
        }
        else {
            Text _val = getSibling(0);

            Text uniformedItemName = UniformedItemNames.getTranslatedName(_val);
            if(uniformedItemName != null) {
                resultText.append(uniformedItemName);
                return;
            }

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
        EmeraldName(Emeralds::getTranslatedItemName),
        MaterialName(Material::getTranslatedItemName),
        IngredientName(Ingredient::getTranslatedItemName),
        PowderName(ElementPowder::getTranslatedItemName),
        GatheringToolName(GatheringTool::getTranslatedItemName);

        private final Function<Text, MutableText> nameGetter;

        private static Text getTranslatedName(Text val) {
            return Arrays.stream(values())
                    .map(names -> names.nameGetter.apply(val))
                    .filter(Objects::nonNull)
                    .findFirst().orElse(null);
        }

        UniformedItemNames(Function<Text, MutableText> nameGetter) {
            this.nameGetter = nameGetter;
        }
    }
}
