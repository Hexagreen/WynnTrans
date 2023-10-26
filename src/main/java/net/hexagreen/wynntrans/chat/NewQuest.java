package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NewQuest extends WynnChatText implements IFocusText {
    private final String keyQuestName;
    private final String valQuestName;
    private final Text fullText;

    protected NewQuest(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.valQuestName = getContentLiteral(0);
        this.keyQuestName = parentKey + valQuestName.replace(" ", "");
        this.fullText = text;
    }

    public static NewQuest of(Text text, Pattern regex) {
        return new NewQuest(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "quest.";
    }

    @Override
    protected void build() {
        resultText = newTranslate(rootKey + dirFunctional + "newQuest").setStyle(getStyle())
                .append(": ");

        if(WTS.checkTranslationExist(keyQuestName, valQuestName)) {
            resultText.append(newTranslate(keyQuestName).setStyle(getSibling(0).getStyle()));
        }
        else {
            resultText.append(getSibling(0));
        }

        resultText = setToPressShift(resultText, fullText);
    }

    @Override
    public boolean print() {
        clearChat();
        return super.print();
    }
}
