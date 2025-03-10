package net.hexagreen.wynntrans.text.scoreboard;

import net.hexagreen.wynntrans.text.scoreboard.types.*;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public enum ScoreboardContentType {
    CONTENT_TRACKING(ContentTracking::new, ContentTracking::typeChecker),
    DAILY_OBJECTIVE(DailyObjective::new, DailyObjective::typeChecker),
    GUILD_OBJECTIVE(GuildObjective::new, GuildObjective::typeChecker),
    STARTER_OBJECTIVE(StarterObjective::new, StarterObjective::typeChecker),
    WORLD_EVENT(WorldEvent::new, WorldEvent::typeChecker),
    PARTY(Party::new, Party::typeChecker),
    NO_TYPE(SimpleScoreboardContent::new, null);
    private final Function<List<Text>, WynnScoreboardText> wst;
    private final Predicate<List<Text>> typeChecker;

    private static ScoreboardContentType findType(List<Text> text) {
        return Arrays.stream(values())
                .filter(tooltipType -> Objects.nonNull(tooltipType.typeChecker))
                .filter(tooltipType -> tooltipType.typeChecker.test(text))
                .findFirst().orElse(NO_TYPE);
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
