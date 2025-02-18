package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ILootrun;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LootrunBeacon extends WynnChatText implements ILootrun {

    public static MutableText getBeaconNameText(String beaconNameString) {
        Text vibrant;
        if(beaconNameString.contains("Vibrant")) vibrant = Text.translatable("wytr.lootrun.beacon.vibrant");
        else vibrant = Text.empty();

        Text color = Text.translatable("wytr.lootrun.beacon." + getColorStringForKey(beaconNameString));

        MutableText beaconName;
        beaconName = Text.translatable("wytr.lootrun.beacon", vibrant, color);
        return beaconName;
    }

    private static String getColorStringForKey(String beaconNameString) {
        return beaconNameString.replaceFirst("(?:§.)*(?:Vibrant )?(.+) Beacon", "$1")
                .replaceAll(" ", "_")
                .toLowerCase(Locale.ENGLISH);
    }

    public LootrunBeacon(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.lootrun.chooseBeacon";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text title = Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true));
        Text desc = Text.translatable(translationKey + ".desc").setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        Text reroll = null;
        Text rerollCount = null;

        List<List<Text>> textChunks = getTextChunks(getSiblings());
        textChunks.removeFirst();
        if(textChunks.getLast().getFirst().getString().contains("reroll")) {
            reroll = Text.translatable(translationKey + ".reroll").setStyle(textChunks.getLast().getFirst().getSiblings().getLast().getStyle());
            String count = textChunks.getLast().getLast().getSiblings().getLast().getString().replaceFirst(".+(\\d) reroll.+", "$1");
            rerollCount = Text.translatable(translationKey + ".reroll.count", count).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
            textChunks.removeLast();
        }

        List<Text> beacons = placeTextToBiSection(textChunks.stream()
                .map(this::mergeTextFromBiSection)
                .flatMap(Arrays::stream)
                .map(this::translateBeacon)
                .filter(Objects::nonNull)
                .toList());

        resultText = Text.empty()
                .append(centerAlign(title)).append("\n")
                .append(centerAlign(desc)).append("\n")
                .append("\n");

        beacons.forEach(t -> resultText.append(t).append("\n"));

        if(reroll != null) {
            resultText.append("\n")
                    .append(centerAlign(reroll)).append("\n")
                    .append(centerAlign(rerollCount));
        }
    }

    private List<Text> translateBeacon(Text text) {
        if(text.getString().isEmpty()) return null;
        List<Text> siblings = text.getSiblings();

        String beaconNameString = siblings.getFirst().getString();
        Style beaconNameStyle = parseStyleCode(beaconNameString);

        Text beaconName = getBeaconNameText(beaconNameString).setStyle(beaconNameStyle);
        if(beaconNameString.contains("Rainbow")) beaconName = rainbowDecoration(beaconName);

        String beaconDescString = siblings.getLast().getString();

        Style numberStyle;
        if(beaconDescString.contains("§b")) numberStyle = Style.EMPTY.withColor(Formatting.AQUA);
        else numberStyle = Style.EMPTY;

        Matcher numberMatcher = Pattern.compile("\\d+").matcher(beaconDescString.replaceAll("§.", ""));
        List<Text> numbers = new ArrayList<>();
        while(numberMatcher.find()) {
            numbers.add(Text.literal(numberMatcher.group()).setStyle(numberStyle));
        }

        Text beaconDesc = Text.translatable("wytr.lootrun.beacon." + getColorStringForKey(beaconNameString) + ".desc", numbers.toArray(Object[]::new))
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY));

        List<Text> result = wrapLine(beaconDesc, (int) CHAT_HUD_WIDTH / 3);
        result.addFirst(beaconName);

        return result;
    }
}
