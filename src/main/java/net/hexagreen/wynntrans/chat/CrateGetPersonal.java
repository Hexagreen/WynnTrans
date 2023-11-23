package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class CrateGetPersonal extends WynnChatText {

    protected CrateGetPersonal(Text text, Pattern regex) {
        super(text, regex);
    }

    public static CrateGetPersonal of(Text text, Pattern regex) {
        return new CrateGetPersonal(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "crateReward.personal";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(getSibling(1));

        resultText.append("\n\n")
                .append(getSibling(3));

        resultText.append("\n")
                .append(getSibling(4));

        resultText.append("\n");
    }
}