package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.enums.Bombs;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BombStart extends WynnSystemText implements ISpaceProvider {

    public BombStart(Text text) {
        super(text, true);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.bombStart";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        Text playerName = getSibling(1).getSiblings().get(1);
        Text _bomb = getSibling(0).getSiblings().getLast();
        Bombs bomb = Bombs.findBomb(_bomb.getString());
        if(bomb == null) throw new TextTranslationFailException("BombStart.class");

        Text _thanks = getSibling(-1).getSiblings().getLast();
        Text thanks = Text.translatable("wytr.func.bombThanks", playerName.getString()).setStyle(_thanks.getStyle());

        Text head = bomb.getName().setStyle(_bomb.getStyle());
        Text body;
        Text tail;

        switch(bomb) {
            case LOOT, LOOT_CHEST, COMBAT_XP -> {
                body = Text.translatable(translationKey + ".durational", playerName, bomb.getName(), bomb.getDesc(), bomb.getTime())
                        .formatted(Formatting.GRAY);
                tail = Text.empty().append(getSystemTextCenterIndent(thanks)).append(thanks);
            }
            case INGREDIENT, ITEM -> {
                body = Text.translatable(translationKey + ".instant", playerName, bomb.getName(), bomb.getDesc())
                        .formatted(Formatting.GRAY);
                tail = Text.empty().append(SYSTEM_TEXT_ADJUSTMENT).append(twoColumnAlign(bomb.getTime(), thanks));
            }
            case DUNGEON -> {
                body = Text.translatable(translationKey, playerName, bomb.getName(), bomb.getDesc(), bomb.getTime())
                        .formatted(Formatting.GRAY);
                tail = Text.empty().append(getSystemTextCenterIndent(thanks)).append(thanks);
            }
            default -> {
                debugClass.writeTextAsJSON(inputText, "UnknownBomb");
                resultText = inputText;
                return;
            }
        }

        resultText.append(Text.empty().append(getSystemTextCenterIndent(head)).append(head)).append("\n");
        wrapLine(body, (int) CHAT_HUD_WIDTH)
                .forEach(t -> resultText.append(Text.empty().append(getSystemTextCenterIndent(t)).append(t).append("\n")));
        resultText.append("\n").append(tail).append("\n");
    }

}
