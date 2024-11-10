package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PartyInvited extends WynnChatText implements ISpaceProvider {
    private final Text playerName;

    public PartyInvited(Text text) {
        super(text);
        this.playerName = getPlayerName();
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.partyInvited";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text t0 = Text.translatable(parentKey, playerName).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
        Text t1 = Text.translatable(parentKey + ".command").setStyle(getSibling(1).getSiblings().get(1).getStyle().withParent(getStyle(1)));

        resultText = Text.empty().append("\n");
        resultText.append(getCenterIndent(t0).append(t0)).append("\n");
        resultText.append(getCenterIndent(t1).append(t1)).append("\n");
    }

    private Text getPlayerName() {
        String str = getSibling(0).getString().replaceAll(".+to join ", "").replaceAll("'s party!\\n", "");
        return Text.literal(str);
    }
}
