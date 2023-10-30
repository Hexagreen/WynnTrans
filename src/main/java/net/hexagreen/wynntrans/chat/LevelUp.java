package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelUp extends WynnChatText implements ICenterAligned {
    private static final Pattern REGEX_LEVELUP = Pattern.compile("^ +You are now combat level (\\d+)");
    private static final Pattern REGEX_NEXTAP = Pattern.compile("^ +Only (\\d+) more levels? until your next Ability Point");

    protected LevelUp(Text text, Pattern regex) {
        super(text, regex);
    }

    public static LevelUp of(Text text, Pattern regex) {
        return new LevelUp(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "levelUp";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        Text t1 = newTranslate(parentKey).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.GOLD));
        resultText.append(Text.literal(getCenterIndent(t1)).append(t1).append("\n"));

        Matcher m2 = REGEX_LEVELUP.matcher(getSibling(2).getString());
        if(!m2.find()) {
            debugClass.writeTextAsJSON(inputText);
            return;
        }
        Text t2 = newTranslate(parentKey + ".nowOn", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
        resultText.append(Text.literal(getCenterIndent(t2)).append(t2).append("\n"));

        resultText.append("\n");

        Matcher m3 = REGEX_NEXTAP.matcher(getSibling(4).getString());
        if(!m3.find()) {
            debugClass.writeTextAsJSON(inputText);
            return;
        }
        Text num = Text.literal(m3.group(1)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
        Text t3 = newTranslate(parentKey + ".nextAP", num);
        resultText.append(Text.literal(getCenterIndent(t3)).append(t3).append("\n"));

        resultText.append("\n");

        for(int i = 6; inputText.getSiblings().size() > i; i++) {
            if(getSibling(i).getString().contains("+1 Ability Point")) {
                resultText.append(newTranslate(parentKey + ".ap").setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
                        .append(newTranslate(parentKey + ".ap_guide").setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+2 Skill Points")) {
                resultText.append(newTranslate(parentKey + ".sp").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(newTranslate(parentKey + ".sp_guide").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+5 Maximum HP")) {
                resultText.append(newTranslate(parentKey + ".hp").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Quest")) {
                String valQuestName = getSibling(i).getSiblings().get(1).getString().replace("[", "").replace("]", "");
                String keyQuestName = "wytr.quest." + valQuestName.replace(" ", "");
                Text questText;
                if(WTS.checkTranslationExist(keyQuestName, valQuestName)) {
                    questText = Text.literal("[" + newTranslate(keyQuestName).getString() + "]").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
                }
                else {
                    questText = getSibling(i).getSiblings().get(1);
                }

                resultText.append(newTranslate(parentKey + ".newQuest").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(questText)
                        .append("\n");
                continue;
            }
            resultText.append(getSibling(i));
            debugClass.writeString2File(getSibling(i).getString(), "getString.txt", "LevelUp");
            debugClass.writeTextAsJSON(getSibling(i));
        }
    }
}