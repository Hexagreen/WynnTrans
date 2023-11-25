package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaveCompleted extends WynnChatText implements ICenterAligned {
    private static final Pattern REGEX_EXP = Pattern.compile("^\\+(\\d+) Experience Points$");
    private static final Pattern REGEX_EME = Pattern.compile("^ +- \\+(\\d+) Emeralds$");
    private static final String func = rootKey + dirFunctional;
    private final String keyCaveName;
    private final String valCaveName;

    public CaveCompleted(Text text, Pattern regex) {
        super(text, regex);
        this.valCaveName = getSibling(2).getSiblings().get(1).getString();
        this.keyCaveName = parentKey + valCaveName.replace(" ", "");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "cave.";
    }

    @Override
    protected void build() {
        resultText = Text.empty()
                .append(getCenterIndent(func + "caveCompleted"))
                .append(newTranslate(func + "caveCompleted").setStyle(getSibling(1).getSiblings().get(0).getStyle())).append("\n")
                .append(getCenterIndent(keyCaveName));

        if(WTS.checkTranslationExist(keyCaveName, valCaveName)) {
            resultText.append(newTranslate(keyCaveName).setStyle(getSibling(2).getSiblings().get(1).getStyle()));
        }
        else {
            Text origin = getSibling(2).getSiblings().get(1);
            resultText.append(getCenterIndent(origin)).append(origin);
        }

        resultText.append("\n\n")
                .append(newTranslate(func + "reward").setStyle(getSibling(4).getSiblings().get(0).getStyle())).append("\n");


        for(int i = 5; inputText.getSiblings().size() > i; i++) {
            Matcher m1 = REGEX_EXP.matcher(getSibling(i).getSiblings().get(1).getString());
            if(m1.find()) {
                resultText.append(getSibling(i).getSiblings().get(0))
                        .append(newTranslate(func + "reward.experience", m1.group(1)).setStyle(getSibling(i).getSiblings().get(1).getStyle()))
                        .append("\n");
                continue;
            }
            Matcher m2 = REGEX_EME.matcher(getSibling(i).getSiblings().get(1).getString());
            if(m2.find()) {
                resultText.append(getSibling(i).getSiblings().get(0))
                        .append(newTranslate(func + "reward.emerald", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GREEN)))
                        .append("\n");
                continue;
            }
            String valExclusiveReward = getSibling(i).getString();
            String hash = DigestUtils.sha1Hex(valExclusiveReward).substring(0, 4);
            String keyExclusiveReward = keyCaveName + ".reward_" + hash;
            if(WTS.checkTranslationExist(keyExclusiveReward, valExclusiveReward)) {
                resultText.append(getSibling(i).getSiblings().get(0));
                resultText.append(newTranslate(keyExclusiveReward).setStyle(getSibling(i).getSiblings().get(1).getStyle()));
            }
            else {
                resultText.append(getSibling(i));
            }
            resultText.append("\n");
        }
    }
}
