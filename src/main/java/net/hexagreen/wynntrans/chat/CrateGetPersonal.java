package net.hexagreen.wynntrans.chat;

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
        return rootKey + dirFunctional + "crateReward.personal";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(grade);

        resultText.append("\n\n")
                .append(getSibling(3));

        resultText.append("\n")
                .append(getSibling(4));

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
            return this.gradeName.equals(string);
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
                return Text.translatable(rootKey + dirFunctional + "crateReward.personal", text).setStyle(grades.gradeStyle);
            }
            else return Text.translatable(rootKey + dirFunctional + "crateReward.personal", BLACK_MARKET.gradeText).setStyle(grades.gradeStyle);
        }
    }
}
