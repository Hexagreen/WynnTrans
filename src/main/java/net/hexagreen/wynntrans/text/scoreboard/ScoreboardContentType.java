package net.hexagreen.wynntrans.text.scoreboard;

import net.hexagreen.wynntrans.text.scoreboard.types.*;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public enum ScoreboardContentType {
    CONTENT_TRACKING(ContentTracking::new, ContentTracking::typeChecker),
    DAILY_OBJECTIVE(DailyObjective::new, DailyObjective::typeChecker),
    GUILD_OBJECTIVE(GuildObjective::new, GuildObjective::typeChecker),
    STARTER_OBJECTIVE(StarterObjective::new, StarterObjective::typeChecker),
    WORLD_EVENT(WorldEvent::new, WorldEvent::typeChecker),
    LOOTRUN(Lootrun::new, Lootrun::typeChecker),
    PARTY(Party::new, Party::typeChecker),
    NO_TYPE(SimpleScoreboardContent::new, null);


    private static final List<ScoreboardContentType> CACHED_CHECKER =
            Arrays.stream(ScoreboardContentType.values())
                    .filter(e -> e.typeChecker != null)
                    .toList();
    private final Function<List<Text>, WynnScoreboardText> wst;
    private final Predicate<List<Text>> typeChecker;

    private static ScoreboardContentType findType(List<Text> text) {
        for(ScoreboardContentType c : CACHED_CHECKER) {
            if(c.typeChecker.test(text)) return c;
        }
        return NO_TYPE;
    }

    public static List<Text> findAndRun(List<Text> text) {
        ScoreboardContentType find = findType(text);
        if(find == null) return text;
        return find.wst.apply(text).text();
    }

    ScoreboardContentType(Function<List<Text>, WynnScoreboardText> wst, Predicate<List<Text>> typeChecker) {
        this.wst = wst;
        this.typeChecker = typeChecker;
    }
}
