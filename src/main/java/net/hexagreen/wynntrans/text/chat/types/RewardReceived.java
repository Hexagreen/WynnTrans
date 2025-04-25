package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RewardReceived extends WynnChatText {
    private static final Text STORE = Text.literal("wynncraft.com/store").setStyle(Style.EMPTY.withColor(Formatting.AQUA).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://wynncraft.com/store")));
    private final Text rewards;

    public RewardReceived(Text text) {
        super(text);
        this.rewards = initRewards();
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "bomb.reward";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        if(hasStoreLink()) {
            resultText.append(Text.translatable(translationKey, rewards, STORE).setStyle(parseStyleCode(getSibling(0).getString())));
        }
        else {
            resultText.append(Text.translatable(rootKey + "func.rewardReceived", rewards).setStyle(parseStyleCode(getSibling(0).getString())));
        }
    }

    private Text initRewards() {
        MutableText rewards = Text.empty();
        for(int i = 1; i < getSiblings().size() - (hasStoreLink() ? 1 : 0); i++) {
            rewards.append("\n").append(getSibling(i));
        }
        return rewards;
    }

    private boolean hasStoreLink() {
        Text last = getSiblings().getLast();
        return !last.getString().replaceAll("§.", "").matches(" ?- .+");
    }
}