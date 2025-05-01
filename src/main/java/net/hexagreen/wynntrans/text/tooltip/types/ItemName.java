package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Text;

import java.util.List;

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
            Text normalEquip = NormalEquipment.getTranslatedItemName(_val);
            Text unidentifiedEquip = UnidentifiedEquipment.getTranslatedItemName(_val);
            Text ingredient = Ingredient.getTranslatedItemName(_val);
            if(normalEquip != null) {
                resultText.append(normalEquip);
                return;
            }
            else if(unidentifiedEquip != null) {
                resultText.append(unidentifiedEquip);
                return;
            }
            else if(ingredient != null) {
                resultText.append(ingredient);
                return;
            }

            if(returnNull) resultText = null;
            else resultText.append(getSibling(0).copy());
        }
    }

    private boolean checkTranslation(String key, String val) {
        if(addiction) return WTS.checkTranslationExist(key, val);
        return WTS.checkTranslationDoNotRegister(key);
    }
}
