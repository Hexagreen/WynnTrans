package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EquipQuestRequirement extends WynnSystemText {
    private static final Style RED = Style.EMPTY.withColor(Formatting.RED);
    private final String questName;
    private final String equipName;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() < 4) return false;
        return " You must complete the ".equals(text.getSiblings().get(1).getString()) && text.getSiblings().get(3).getString().contains("quest");
    }

    public EquipQuestRequirement(Text text) {
        super(text);
        this.questName = getSibling(1).getString();
        this.equipName = getSibling(3).getString();
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.equipQuestReq";
    }

    @Override
    protected void build() {
        Text questName = Text.translatableWithFallback("wytr.quest." + normalizeStringForKey(this.questName), this.questName).setStyle(RED);
        Text equipName = new ItemName(this.equipName).noAddictionMode().textAsMutable().setStyle(RED);

        resultText = Text.translatable(translationKey, questName, equipName).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
    }
}
