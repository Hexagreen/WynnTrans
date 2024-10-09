package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Dungeons;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonComplete extends WynnChatText {
    private static final Pattern HEAD = Pattern.compile("§6Great job! You've completed the (.+) Dungeon!");
    private static final Pattern EXP = Pattern.compile("^§7\\[\\+(\\d+) XP]");
    private static final Pattern FALLING_EMERALD = Pattern.compile("^§7\\[\\+(\\d+) falling emeralds]");
    private final Dungeons dungeon;
    private final String gainedXp;
    private final String gainedEmerald;

    public DungeonComplete(Text text, Pattern ignoredRegex) {
        super(text, HEAD);
        this.dungeon = Dungeons.getDungeons(matcher.group(1));
        Matcher m1 = EXP.matcher(getSibling(1).getString());
        Matcher m2 = FALLING_EMERALD.matcher(getSibling(2).getString());
        if(m1.find()) this.gainedXp = m1.group(1);
        else this.gainedXp = null;
        if(m2.find()) this.gainedEmerald = m2.group(1);
        else this.gainedEmerald = null;
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.dungeonCompleted";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, dungeon.getDungeonName()).setStyle(Style.EMPTY.withColor(Formatting.GOLD))).append("\n");
        if(gainedXp != null) resultText.append(attachBox(Text.translatable(parentKey + ".exp", gainedXp))).append("\n");
        if(gainedEmerald != null)
            resultText.append(attachBox(Text.translatable(parentKey + ".emerald", gainedEmerald))).append("\n");
        resultText.append(attachBox(dungeon.getDungeonBossReward())).append("\n");
        resultText.append(attachBox(dungeon.getDungeonFragment()));
    }

    private Text attachBox(Text text) {
        MutableText out = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.GRAY)).append("[+");
        out.append(text).append("]");
        return out;
    }
}
