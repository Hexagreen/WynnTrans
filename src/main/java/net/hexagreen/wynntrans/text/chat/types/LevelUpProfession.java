package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelUpProfession extends WynnChatText implements ISpaceProvider {
    private static final Pattern REGEX_LEVELUP = Pattern.compile("You are now level (\\d+) in §f(.)");
    private static final Pattern REGEX_NEXTFEATURE = Pattern.compile("^§5Only §d(\\d+) more levels? §5until you can .+?(?:(.) Gathering.+T(\\d+))?$");

    public LevelUpProfession(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.levelUp";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        Text t1 = Text.translatable(translationKey).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.GOLD));
        resultText.append(getCenterIndent(t1).append(t1).append("\n"));

        Matcher m2 = REGEX_LEVELUP.matcher(getSibling(2).getString());
        if(m2.find()) {
            Text prof = Profession.getProfession(m2.group(2).charAt(0)).getTextWithIcon().setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
            Text t2 = Text.translatable(translationKey + ".nowOnIn", m2.group(1), prof).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
            resultText.append(getCenterIndent(t2).append(t2).append("\n"));
        }

        resultText.append("\n");

        for(int i = 4; getSiblings().size() > i; i++) {
            if(getSibling(i).getString().contains("+ Faster Gathering")) {
                resultText.append(Text.translatable(translationKey + ".gatheringSpeed").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(getSibling(i).getString().substring(23)).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ Higher Refining")) {
                resultText.append(Text.translatable(translationKey + ".gatheringRate").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(getSibling(i).getString().substring(30)).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Tool")) {
                String strToolName = getSibling(i).getString().substring(15);
                Text toolName = Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)).append(Profession.getProfession(strToolName.charAt(1)).getTool(strToolName.replaceFirst(".+ T(\\d+)", "$1"))).append("]");
                resultText.append(Text.translatable(translationKey + ".newTool").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(toolName).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Resource")) {
                resultText.append(Text.translatable(translationKey + ".newResource").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(getSibling(i).getString().substring(19)).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ Higher Recipe")) {
                resultText.append(Text.translatable(translationKey + ".higherRecipe").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(Text.literal(getSibling(i).getString().substring(24)).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Recipe")) {
                resultText.append(Text.translatable(translationKey + ".newRecipe").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(Text.literal(getSibling(i).getString().substring(15)).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n");
                continue;
            }
            if(getSibling(i).getString().equals("\n")) {
                resultText.append("\n");
                continue;
            }

            Matcher m3 = REGEX_NEXTFEATURE.matcher(getSibling(i).getString());
            if(m3.find()) {
                resultText.append("\n");
                Text moreLevel = Text.translatable(translationKey + ".moreLevel", m3.group(1)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
                if(m3.group(3) != null) {
                    Text toolName = Profession.getProfession(m3.group(2).charAt(0)).getTool(m3.group(3)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
                    Text nextTool = Text.translatable(translationKey + ".nextTool", moreLevel, toolName).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
                    resultText.append(getCenterIndent(nextTool)).append(nextTool);
                }
                else {
                    Text resourceName = Text.literal(getSibling(i).getString().replaceAll(".+ gather from §d", "")).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
                    Text nextResource = Text.translatable(translationKey + ".nextResource", moreLevel, resourceName).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
                    resultText.append(getCenterIndent(nextResource)).append(nextResource);
                }
                resultText.append("\n");
                return;
            }

            resultText.append(getSibling(i));
        }
    }
}
