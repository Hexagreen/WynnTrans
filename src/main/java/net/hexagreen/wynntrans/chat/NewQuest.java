package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NewQuest extends WynnChatText implements IFocusText {
    private final String keyQuestName;
    private final String valQuestName;
    private final Text fullText;
    private final boolean questlineMode;

    public NewQuest(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.questlineMode = getContentString().contains("Questline");
        this.valQuestName = getContentString(0);
        this.keyQuestName = parentKey + valQuestName
                .replace(" ", "").replace("'", "").replace("֎", "");
        this.fullText = text;
    }

    @Override
    protected String setParentKey() {
        return rootKey + "quest.";
    }

    @Override
    protected void build() {
        if(questlineMode) {
            resultText = newTranslate(rootKey + dirFunctional + "newQuestline").setStyle(getStyle())
                    .append(": ");
        }
        else{
            resultText = newTranslate(rootKey + dirFunctional + "newQuest").setStyle(getStyle())
                    .append(": ");
        }

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
