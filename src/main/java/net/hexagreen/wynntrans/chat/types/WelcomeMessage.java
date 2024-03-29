package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.ICenterAligned;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelcomeMessage extends WynnChatText implements ICenterAligned {
    private static final Pattern REGEX_FES_COLOR = Pattern.compile("^ +§(.)");
    private static final Pattern REGEX_FES = Pattern.compile("^ +(....)(Festival.+)\\n");
    private static final Pattern REGEX_CRATE = Pattern.compile(" (§.[A-z]+) Crates");
    private static final Pattern REGEX_TIME = Pattern.compile("doors in (.+)\\.");
    private static final Pattern REGEX_TRADE = Pattern.compile("^ +§d§l(\\d+)§5 of your items sold");
    private static final Pattern REGEX_SALE_TIME = Pattern.compile("§(.)The sale ends in (.+)\\.");
    private static final Text LINK = Text.literal("§fplay.wynncraft.com §7-/-§f wynncraft.com")
            .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://wynncraft.com")));

    public WelcomeMessage(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "welcome";
    }

    @Override
    protected void build() {
        resultText = Text.literal("\n");

        Text welcome = newTranslate(parentKey);
        resultText.append(getCenterIndent(welcome)).append(welcome).append("\n");

        resultText.append(getCenterIndent(LINK)).append(LINK).append("\n\n");

        Matcher festival = REGEX_FES.matcher(getSibling(1).getString());
        if(festival.find()) {
            appendFestivalMessage(festival);
            return;
        }

        Matcher trade = REGEX_TRADE.matcher(getSibling(1).getString());
        if(trade.find()) {
            appendTradeMessage(trade);
            return;
        }

        for(int i = 1; inputText.getSiblings().size() - 1 > i; i++) {
            Matcher discountEvent = REGEX_SALE_TIME.matcher(getSibling(i).getString());
            if(discountEvent.find()) {
                appendSaleMessage(discountEvent);
                continue;
            }

            String valGuide = getSibling(i).getString().replaceAll("^ +", "");
            String keyGuide = "wytr.welcome." + DigestUtils.sha1Hex(valGuide).substring(0, 6);
            if(WTS.checkTranslationExist(keyGuide, valGuide)) {
                Text guide = newTranslate(keyGuide).setStyle(getStyle(i));
                resultText.append(getCenterIndent(guide)).append(guide).append("\n");
            }
            else {
                resultText.append(getCenterIndent(valGuide)).append(valGuide).append("\n");
            }
        }
    }

    private void appendTradeMessage(Matcher trade) {
        Text num = Text.literal(trade.group(1)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true));
        Text textTrade = newTranslate(parentKey + ".tradeAlarm", num).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
        resultText.append(getCenterIndent(textTrade)).append(textTrade).append("\n");
    }

    private void appendFestivalMessage(Matcher festival) {
        Matcher color = REGEX_FES_COLOR.matcher(getSibling(2).getString());
        Formatting colorFormat;
        if(color.find()) {
            colorFormat = Formatting.byCode(color.group(1).charAt(0));
        }
        else colorFormat = Formatting.WHITE;

        char[] format = festival.group(1).toCharArray();
        Formatting f1 = Formatting.byCode(format[1]);
        Formatting f2 = Formatting.byCode(format[3]);
        String keyFestival = "wytr.eventInfo.eventName." + DigestUtils.sha1Hex(festival.group(2)).substring(0, 4);
        Text textFestival;
        if(WTS.checkTranslationExist(keyFestival, festival.group(2))) {
            textFestival = newTranslate(keyFestival).setStyle(Style.EMPTY.withFormatting(f1, f2));
        }
        else {
            textFestival = Text.literal(festival.group(2)).setStyle(Style.EMPTY.withFormatting(f1, f2));
        }
        resultText.append(getCenterIndent(textFestival)).append(textFestival).append("\n");

        Text festivalGuide1 = newTranslate(parentKey + ".fesGuide.1").setStyle(Style.EMPTY.withColor(colorFormat));
        resultText.append(getCenterIndent(festivalGuide1)).append(festivalGuide1).append("\n");

        Matcher crate = REGEX_CRATE.matcher(getSibling(3).getString());
        if(crate.find()) {
            String strCrate = crate.group(1);
            Text festivalGuide2 = newTranslate(parentKey + ".fesGuide.2", strCrate).setStyle(Style.EMPTY.withColor(colorFormat));
            resultText.append(getCenterIndent(festivalGuide2)).append(festivalGuide2).append("\n\n");
        }
        else debugClass.writeTextAsJSON(inputText);

        Matcher time = REGEX_TIME.matcher(getSibling(5).getString());
        if(time.find()) {
            String strTime = time.group(1);
            Text festivalGuide3 = newTranslate(parentKey + ".fesGuide.3", strTime).setStyle(Style.EMPTY.withColor(colorFormat));
            resultText.append(getCenterIndent(festivalGuide3)).append(festivalGuide3).append("\n");
        }
    }

    private void appendSaleMessage(Matcher discount) {
        String duration = discount.group(2);
        Formatting color = Formatting.byCode(discount.group(1).charAt(0));
        Text saleTime = newTranslate(parentKey + ".sale", duration).setStyle(Style.EMPTY.withColor(color));
        resultText.append(getCenterIndent(saleTime)).append(saleTime).append("\n");
    }
}
