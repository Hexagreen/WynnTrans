package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CraftingStation extends WynnDisplayText {
    private final Profession profession;

    public static boolean typeChecker(Text text) {
        return text.getString().contains("\n§7Crafting Station\n§7");
    }

    public CraftingStation(Text text) {
        super(text);
        this.profession = Profession.getProfession(text.getString().replaceFirst("§f. §6§l", "").replaceAll("§r§f(.|\\n)+", ""));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.craftingStation";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(GRAY);
        resultText.append(getHead()).append("\n").append(Text.translatable(translationKey)).append("\n").append(Text.translatable(translationKey + "." + profession.getKey()));
    }

    private Text getHead() {
        MutableText head = Text.empty();
        head.append(profession.getIcon()).append(" ").append(profession.getText().setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true))).append(" ").append(profession.getIcon());
        return head;
    }
}
