package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.CratesTexts;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class CrateGetPersonal extends WynnChatText {
    private static final Pattern headRegex = Pattern.compile("You've gotten a(?: \\|\\|\\|)? (.+) (?:\\|\\|\\| )?reward!");
    private final CratesTexts.Crates grade;

    public CrateGetPersonal(Text text) {
        super(text, headRegex);
        this.grade = CratesTexts.Crates.find(matcher.group(1));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.crateReward.personal";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable("wytr.func.crateReward.personal", grade.getGradeText()).setStyle(grade.getGradeStyle().withBold(true)));

        resultText.append("\n\n").append(getSibling(2));

        resultText.append("\n").append(CratesTexts.RewardDescription.findAndGet(getSibling(3)));

        resultText.append("\n");
    }
}
