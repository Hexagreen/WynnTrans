package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelcomeMessage extends WynnChatText implements ISpaceProvider {
    private static final Pattern REGEX_FES_COLOR = Pattern.compile("^ +§(.)");
    private static final Pattern REGEX_FES = Pattern.compile("^ +(....)(Festival.+)\\n");
    private static final Pattern REGEX_CRATE = Pattern.compile(" (§.[A-z]+) Crates");
    private static final Pattern REGEX_TIME = Pattern.compile("doors in (.+)\\.");
    private static final Pattern REGEX_TRADE = Pattern.compile("^ +§d§l(\\d+)§5 of your items sold");
    private static final Pattern REGEX_PROMOTION_TIME = Pattern.compile(".+ in (.+ (?:days|day|hours|hour|minutes|minute))\\.");
    private static final Text LINK = Text.literal("§fplay.wynncraft.com §7-/-§f wynncraft.com").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://wynncraft.com")));

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\n +....Welcome to Wynncraft!\\n").matcher(text.getString()).find();
    }

    public WelcomeMessage(Text text) {
        super(text);
        WynnTrans.refreshWynnPlayerName();
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.welcome";
    }

    @Override
    protected void build() {
        resultText = Text.literal("\n");

        Text welcome = Text.translatable(translationKey);
        resultText.append(centerAlign(welcome)).append("\n");

        resultText.append(centerAlign(LINK)).append("\n\n");

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

        for(int i = 1; getSiblings().size() - 1 > i; i++) {
            Matcher discountEvent = REGEX_PROMOTION_TIME.matcher(getSibling(i).getString());
            if(discountEvent.find()) {
                appendPromotionMessage(discountEvent);
                continue;
            }

            String valGuide = getSibling(i).getString().replaceAll("^ +", "");
            String keyGuide = "wytr.welcome." + DigestUtils.sha1Hex(valGuide).substring(0, 6);
            if(WTS.checkTranslationExist(keyGuide, valGuide)) {
                Text guide = Text.translatable(keyGuide).setStyle(getStyle(i));
                resultText.append(centerAlign(guide)).append("\n");
            }
            else {
                Text guide = Text.literal(valGuide).setStyle(getStyle(i));
                resultText.append(centerAlign(guide));
            }
        }
    }

    private void appendTradeMessage(Matcher trade) {
        Text num = Text.literal(trade.group(1)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true));
        Text textTrade = Text.translatable(translationKey + ".tradeAlarm", num).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
        resultText.append(centerAlign(textTrade)).append("\n");
    }

    private void appendFestivalMessage(Matcher festival) {
        Matcher color = REGEX_FES_COLOR.matcher(getSibling(2).getString());
        Style festDescStyle;
        if(color.find()) {
            festDescStyle = parseStyleCode(color.group(1));
        }
        else festDescStyle = Style.EMPTY;

        Style festTitleStyle = parseStyleCode(festival.group(1));
        String keyFestival = "wytr.eventInfo.eventName." + DigestUtils.sha1Hex(festival.group(2)).substring(0, 4);
        Text textFestival;
        if(WTS.checkTranslationExist(keyFestival, festival.group(2))) {
            textFestival = Text.translatable(keyFestival).setStyle(festTitleStyle);
        }
        else {
            textFestival = Text.literal(festival.group(2)).setStyle(festTitleStyle);
        }
        resultText.append(centerAlign(textFestival)).append("\n");

        Text festivalGuide1 = Text.translatable(translationKey + ".fesGuide.1").setStyle(festDescStyle);
        resultText.append(centerAlign(festivalGuide1)).append("\n");

        Matcher crate = REGEX_CRATE.matcher(getSibling(3).getString());
        if(crate.find()) {
            String strCrate = crate.group(1);
            Text festivalGuide2 = Text.translatable(translationKey + ".fesGuide.2", strCrate).setStyle(festDescStyle);
            resultText.append(centerAlign(festivalGuide2)).append("\n\n");
        }
        else debugClass.writeTextAsJSON(inputText);

        Matcher time = REGEX_TIME.matcher(getSibling(5).getString());
        if(time.find()) {
            String strTime = time.group(1);
            Style styleTime = parseStyleCode(strTime);
            Text textTime = ITime.translateTime(strTime.replaceAll("§.", "")).setStyle(styleTime);
            Text festivalGuide3 = Text.translatable(translationKey + ".fesGuide.3", textTime).setStyle(festTitleStyle.withBold(false));
            resultText.append(centerAlign(festivalGuide3)).append("\n");
        }
    }

    private void appendPromotionMessage(Matcher discount) {
        Style timeStyle = parseStyleCode(discount.group(1));
        Text duration = ITime.translateTime(discount.group(1)).setStyle(timeStyle);

        String body = discount.group().replaceFirst("^ +", "");
        Style textStyle = parseStyleCode(body);
        String valPromotion = body.replaceFirst("(§.)+", "")
                .replaceFirst(discount.group(1), "%1\\$s");
        String keyPromotion = "wytr.welcome." + DigestUtils.sha1Hex(valPromotion).substring(0, 6);
        if(WTS.checkTranslationExist(keyPromotion, valPromotion)) {
            Text guide = Text.translatable(keyPromotion, duration).setStyle(textStyle);
            resultText.append(centerAlign(guide)).append("\n");
        }
        else {
            Text guide = Text.literal(body);
            resultText.append(centerAlign(guide));
        }
    }
}
