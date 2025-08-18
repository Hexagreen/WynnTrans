package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiniQuestInfo extends WynnChatText {
    private static final Pattern LOCKED_QUEST = Pattern.compile("You need to be (.+) Lv\\. (\\d+) to start this Mini-Quest");
    private final boolean gatheringQuest;
    private final boolean lockedQuest;

    public MiniQuestInfo(Text text) {
        super(flatTextToFilled(colorCodedToStyled(text)));
        this.gatheringQuest = text.getString().contains("Gathering");
        this.lockedQuest = text.getString().contains("You need to be");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.miniQuestInfo";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        if(lockedQuest) {
            Matcher m = LOCKED_QUEST.matcher(getContentString());
            if(m.find()) {
                String profName = m.group(1).replaceAll("§.", "");
                Style profNameStyle = parseStyleCode(m.group(1));
                String level = m.group(2);
                String _condition;
                if(profName.contains("Combat")) {
                    _condition = Text.translatable("wytr.display.post.slay.req", level).getString();
                }
                else {
                    Text prof = Profession.getProfession(profName).getText();
                    _condition = Text.translatable("wytr.display.post.gather.req", prof, level).getString();
                }
                _condition = _condition.replaceAll("[\\[\\]]", "");
                Text condition = Text.literal(_condition).setStyle(profNameStyle);
                resultText.append(Text.translatable(translationKey + ".blocked", condition).formatted(Formatting.DARK_RED));
            }
            else resultText = inputText;
        }
        else if(gatheringQuest) {
            Text _item0 = getSibling(0);
            Text _item1 = getSibling(2);
            String[] split0 = _item0.getString().replaceAll("[\\[\\]]", "").split(" ", 2);
            String[] split1 = _item1.getString().replaceAll("[\\[\\]]", "").split(" ", 2);
            Text item0 = Text.translatable("wytr.func.questingItem", split0[0], new ItemName(split0[1]).textAsMutable()).setStyle(_item0.getStyle());
            Text item1 = Text.translatable("wytr.func.questingItem", split1[0], new ItemName(split1[1]).textAsMutable()).setStyle(_item1.getStyle());
            Text prof = Profession.getProfession(getContentString(4).split(" ", 2)[0].replace("[", "")).getText();
            String level = getSibling(4).getString().replaceAll("\\D", "");
            Text postLevel = Text.translatable("wytr.display.post.gather.req", prof, level).setStyle(getStyle(4));

            resultText.append(Text.translatable(translationKey + ".gathering", item0, item1, postLevel)).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
        }
        else {
            Text _item = getSibling(0);
            String[] split = _item.getString().replaceAll("[\\[\\]]", "").split(" ", 2);
            Text item = Text.translatable("wytr.func.questingItem", split[0], new ItemName(split[1].trim()).textAsMutable()).setStyle(_item.getStyle());
            String level = getContentString(2).replaceAll("\\D", "");
            Text postLevel = Text.translatable("wytr.display.post.slay.req", level).setStyle(getStyle(2));

            resultText.append(Text.translatable(translationKey + ".slaying", item, postLevel).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        }
    }
}
