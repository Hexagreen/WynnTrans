package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.regex.Pattern;

public class CrateGetPersonal extends WynnChatText {
    private static final Pattern headRegex = Pattern.compile("You've gotten a(?: \\|\\|\\|)? (.+) (?:\\|\\|\\| )?reward!");
    private final Text grade;

    public CrateGetPersonal(Text text, Pattern ignoredRegex) {
        super(text, headRegex);
        this.grade = CrateGrades.findAndGet(matcher.group(1));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.crateReward.personal";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(grade);

        resultText.append("\n\n")
                .append(getSibling(2));

        resultText.append("\n")
                .append(RewardDescription.findAndGet(getSibling(3)));

        resultText.append("\n");
    }

    private static final Text BAR = Text.literal("|||").setStyle(Style.EMPTY.withObfuscated(true).withColor(Formatting.BLACK));
    private enum CrateGrades {
        COMMON("common", Text.translatable("wytr.crate.common"), Style.EMPTY.withColor(Formatting.WHITE).withBold(true)),
        RARE("rare", Text.translatable("wytr.crate.rare"), Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true)),
        EPIC("epic", Text.translatable("wytr.crate.epic"), Style.EMPTY.withColor(Formatting.GOLD).withBold(true)),
        GODLY("godly", Text.translatable("wytr.crate.godly"), Style.EMPTY.withColor(Formatting.RED).withBold(true)),
        BLACK_MARKET("Black Market",
                Text.empty().append(BAR).append(Text.translatable("wytr.crate.blackMarket")).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED).withBold(true)).append(BAR),
                Style.EMPTY.withColor(Formatting.DARK_RED).withBold(true));

        private final String gradeName;
        private final MutableText gradeText;
        private final Style gradeStyle;

        CrateGrades(String gradeName, MutableText gradeText, Style gradeStyle) {
            this.gradeName = gradeName;
            this.gradeText = gradeText;
            this.gradeStyle = gradeStyle;
        }

        private boolean match(String string) {
            return string.contains(this.gradeName);
        }

        private static CrateGrades find(String string) {
            return Arrays.stream(CrateGrades.values())
                    .filter(crateGrades -> crateGrades.match(string))
                    .findFirst().orElse(COMMON);
        }

        private static Text findAndGet(String string) {
            CrateGrades grades = find(string);
            if(grades != BLACK_MARKET) {
                Text text = grades.gradeText.setStyle(grades.gradeStyle);
                return Text.translatable(rootKey + "func.crateReward.personal", text).setStyle(grades.gradeStyle);
            }
            else return Text.translatable(rootKey + "func.crateReward.personal", BLACK_MARKET.gradeText).setStyle(grades.gradeStyle);
        }
    }

    private enum RewardDescription {
        PLAYER_EFFECT_1(Pattern.compile("Give your character (?:a|the) "),
                Text.translatable("wytr.crateReward.playerEffect").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
        PLAYER_EFFECT_2(Pattern.compile("effect around your character"),
                Text.translatable("wytr.crateReward.playerEffect").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
        HELMET(Pattern.compile("Disguise your helmet"),
                Text.translatable("wytr.crateReward.helmet").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
        WEAPON(Pattern.compile("Disguise your (wand|spear|bow|relik|dagger|staff)"),
                Text.translatable("wytr.crateReward.weapon").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
        DISGUISE(Pattern.compile("Disguise yourself as"),
                Text.translatable("wytr.crateReward.disguise").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
        ATTACK_EFFECT(Pattern.compile("effect whenever you hit"),
                Text.translatable("wytr.crateReward.attackEffect").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
        PET_TOKEN(Pattern.compile("This token can be redeemed for"),
                Text.translatable("wytr.crateReward.petToken").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
        NULL(null, null);

        private final Pattern descRegex;
        private final Text translatedText;

        RewardDescription(Pattern descRegex, Text translatedText) {
            this.descRegex = descRegex;
            this.translatedText = translatedText;
        }

        private static RewardDescription find(Text text) {
            return Arrays.stream(RewardDescription.values())
                    .filter(rewardDescription -> rewardDescription.descRegex.matcher(text.getString()).find())
                    .findFirst().orElse(NULL);
        }

        private static Text findAndGet(Text text) {
            RewardDescription rewardDescription = find(text);
            if(rewardDescription != NULL) return rewardDescription.translatedText;
            else return text;
        }
    }
}
