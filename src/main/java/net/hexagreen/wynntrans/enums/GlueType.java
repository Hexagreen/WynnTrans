package net.hexagreen.wynntrans.enums;

import net.hexagreen.wynntrans.chat.glue.*;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.regex.Pattern;

public enum GlueType {
    QUEST_CLEAR(Pattern.compile("^ +\\[Quest Completed]$"), QuestGlue.class),
    CAVE_CLEAR(Pattern.compile("^ +\\[Cave Completed]$"), CaveGlue.class),
    OBJECTIVE_CLEAR(Pattern.compile("^ +\\[Objective Completed]$"), ObjectiveGlue.class),
    LEVELUP(Pattern.compile("^                                Level Up!$"), LevelUpGlue.class),
    CRATE_GET_PERSONAL(Pattern.compile("^You've gotten a .+ reward!"), CrateGlue.class),
    AREA_DISCOVERY(Pattern.compile("^ +Area Discovered: "), AreaDiscoveryGlue.class),

    NO_TYPE(null, null);

    private final Pattern regex;
    private final Class<? extends TextGlue> glue;

    GlueType(Pattern regex, Class<? extends TextGlue> glue) {
        this.regex = regex;
        this.glue = glue;
    }

    public boolean matchFullText(Text text) {
        if(this == NO_TYPE) return false;
        return this.regex.matcher(text.getString()).find();
    }

    public static GlueType findType(Text text) {
        return Arrays.stream(GlueType.values())
                .filter(glueType -> glueType.matchFullText(text))
                .findFirst().orElse(NO_TYPE);
    }

    public static TextGlue findAndGet(Text text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        GlueType find = findType(text);
        return find == NO_TYPE ? null : find.glue.cast(find.glue.getConstructor().newInstance());
    }
}
