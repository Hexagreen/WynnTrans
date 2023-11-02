package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.enums.Profession;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelUpProfession extends WynnChatText implements ICenterAligned {
    private static final Pattern REGEX_LEVELUP = Pattern.compile("^ +You are now level (\\d+) in (.)");
    private static final Pattern REGEX_NEXTFEATURE = Pattern.compile("^Only (\\d+) more levels? until you can .+(?:(.) Gathering)?.+(?:T(\\d+))?$");

    protected LevelUpProfession(Text text, Pattern regex) {
        super(text, regex);
    }

    public static LevelUpProfession of(Text text, Pattern regex) {
        return new LevelUpProfession(text, regex);
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
        Text prof = Profession.getProfession(m2.group(2).charAt(0)).getText().setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
        Text t2 = newTranslate(parentKey + ".nowOnIn", m2.group(1), prof).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
        resultText.append(Text.literal(getCenterIndent(t2)).append(t2).append("\n"));

        resultText.append("\n");

        for(int i = 4; inputText.getSiblings().size() > i; i++) {
            if(getSibling(i).getString().contains("+ Faster Gathering")) {
                resultText.append(newTranslate(parentKey + ".gatheringSpeed").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(getSibling(i).getSiblings().get(1))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ Higher Refining")) {
                resultText.append(newTranslate(parentKey + ".gatheringRate").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(getSibling(i).getSiblings().get(1))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Tool")) {
                String strToolName = getSibling(i).getSiblings().get(1).getString();
                Text toolName = Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))
                        .append(Profession.getProfession(strToolName.charAt(1)).getTool(strToolName.charAt(strToolName.length() - 2)))
                        .append("]");
                resultText.append(newTranslate(parentKey + ".newTool").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(toolName)
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Resource")) {
                resultText.append(newTranslate(parentKey + ".newResource").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(getSibling(i).getSiblings().get(1))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ Higher Recipe")) {
                resultText.append(newTranslate(parentKey + ".higherRecipe").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(Text.literal(getSibling(i).getString().substring(22)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Recipe")) {
                resultText.append(newTranslate(parentKey + ".newRecipe").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(Text.literal(getSibling(i).getString().substring(13)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().equals("\n")) {
                resultText.append("\n");
                continue;
            }

            Matcher m3 = REGEX_NEXTFEATURE.matcher(getSibling(i).getString());
            if(m3.find()) {
                resultText.append("\n");
                Text moreLevel = newTranslate(parentKey + ".moreLevel", m3.group(1)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
                if (m3.group(3) != null) {
                    Text toolName = Profession.getProfession(m3.group(2).charAt(0)).getTool(m3.group(3).charAt(0)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
                    Text nextTool = newTranslate(parentKey + ".nextTool", moreLevel, toolName).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
                    resultText.append(getCenterIndent(nextTool)).append(nextTool);
                }
                else {
                    Text resourceName = getSibling(i).getSiblings().get(3);
                    Text nextResource = newTranslate(parentKey + ".nextResource", moreLevel, resourceName).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
                    resultText.append(getCenterIndent(nextResource)).append(nextResource);
                }
                return;
            }

            resultText.append(getSibling(i));
            debugClass.writeString2File(getSibling(i).getString(), "getString.txt", "ProfLevelUp");
            debugClass.writeTextAsJSON(getSibling(i));
        }
    }
}
