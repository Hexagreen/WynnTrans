package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class RewardReceived extends WynnChatText {
    private static final Text STORE = Text.literal("wynncraft.com/store").setStyle(Style.EMPTY.withColor(Formatting.AQUA).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://wynncraft.com/store")));
    private final Text rewards;

    public RewardReceived(Text text, Pattern regex) {
        super(text, regex);
        this.rewards = initRewards();
    }

    @Override
    protected String setParentKey() {
        return rootKey + "bomb.reward";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, rewards, STORE).setStyle(parseStyleCode(getSibling(0).getString())));
    }

    private Text initRewards() {
        MutableText rewards = Text.empty().append("\n");
        for(int i = 1; i < getSiblings().size() - 1; i++) {
            rewards.append(getSibling(i)).append("\n");
        }
        return rewards;
    }
}