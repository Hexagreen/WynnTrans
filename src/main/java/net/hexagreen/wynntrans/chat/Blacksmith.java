package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Blacksmith extends WynnChatText {
    private final Matcher soldMatcher;

    public Blacksmith(Text text, Pattern regex) {
        super(text, regex);
        this.soldMatcher = Pattern.compile("^You sold me: (\\w+)$").matcher(getContentLiteral(1));

    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "blacksmith";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        resultText.append(newTranslate(parentKey).setStyle(getStyle(0)))
                .append(Text.literal(": ").setStyle(getStyle(0)));

        if(getContentLiteral(1).contains("I can't buy")) {
            resultText.append(newTranslate(parentKey + ".no").setStyle(getStyle(1)));
        }
        else if(getContentLiteral(1).contains("I can only")) {
            resultText.append(newTranslate(parentKey + ".over").setStyle(getStyle(1)));
        }
        else if(getContentLiteral(1).contains("You need more scrap")) {
            resultText.append(newTranslate(parentKey + ".moreScrap").setStyle(getStyle(1)));
        }
        else if(getContentLiteral(1).contains("I can't scrap that")) {
            resultText.append(newTranslate(parentKey + ".cantScrap").setStyle(getStyle(1)));
        }
        else if(getContentLiteral(1).contains("You sold me")) {
            String soldItem = soldMatcher.find() ? soldMatcher.group(1) : "";

            resultText.append(newTranslate(parentKey + ".sold_1", soldItem).setStyle(getStyle(1)));

            int lastItemIndex = inputText.getSiblings().size() - 4;
            for(int index = 2; lastItemIndex >= index; index++) {
                if(getSibling(index).contains(Text.literal(" and "))) {
                    resultText.append(newTranslate(parentKey + ".and").setStyle(getStyle(1)));
                    continue;
                }
                resultText.append(getSibling(index));
            }
            resultText.append(newTranslate(parentKey + ".total").setStyle(getStyle(1)))
                    .append(getSibling(lastItemIndex + 2))
                    .append(newTranslate(parentKey + ".sold_2").setStyle(getStyle(1)));
        }
        else if(getContentLiteral(1).contains("You scrapped")) {
            resultText.append(newTranslate(parentKey + ".scrap_1").setStyle(getStyle(1)));

            int lastItemIndex = inputText.getSiblings().size() - 4;
            for(int index = 2; lastItemIndex >= index; index++) {
                if(getSibling(index).contains(Text.literal(" and "))) {
                    resultText.append(newTranslate(parentKey + ".and").setStyle(getStyle(1)));
                    continue;
                }
                resultText.append(getSibling(index));
            }
            resultText.append(newTranslate(parentKey + ".total").setStyle(getStyle(1)))
                    .append(getSibling(lastItemIndex + 2))
                    .append(newTranslate(parentKey + ".scrap_2").setStyle(getStyle(1)));
        }
    }
}
