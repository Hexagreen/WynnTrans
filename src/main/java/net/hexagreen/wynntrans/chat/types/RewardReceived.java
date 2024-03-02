package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class RewardReceived extends WynnChatText {
    private final Text rewards;
    private static final Text STORE = Text.literal("wynncraft.com/store")
            .setStyle(Style.EMPTY.withColor(Formatting.AQUA)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://wynncraft.com/store")));

    public RewardReceived(Text text, Pattern regex) {
        super(text, regex);
        this.rewards = getRewards();
    }

    @Override
    protected String setParentKey() {
        return rootKey + "bomb.reward";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, rewards, STORE).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
    }

    private Text getRewards() {
        MutableText rewards = Text.empty().append("\n");
        for(int i = 1; i < inputText.getSiblings().size() - 1; i++) {
            rewards.append(getSibling(i)).append("\n");
        }
        return rewards;
    }
}