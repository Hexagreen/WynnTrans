package net.hexagreen.wynntrans.text.scoreboard;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.text.Text;

import java.util.*;

public class ScoreboardSidebarHandler {
    private final LinkedList<ScoreboardEntry> separator;
    private final Map<List<ScoreboardEntry>, List<ScoreboardEntry>> segmentCache;
    private List<ScoreboardEntry> wholeCacheKey;
    private List<ScoreboardEntry> wholeCacheVal;

    public ScoreboardSidebarHandler() {
        this.wholeCacheKey = new ArrayList<>();
        this.wholeCacheVal = new ArrayList<>();
        this.segmentCache = new LinkedHashMap<>(12, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<List<ScoreboardEntry>, List<ScoreboardEntry>> eldest) {
                return size() > 8;
            }
        };
        this.separator = new LinkedList<>() {
            @Override
            public ScoreboardEntry getFirst() {
                ScoreboardEntry e = super.getFirst();
                removeFirst();
                addLast(e);
                return e;
            }
        };

        this.separator.add(new ScoreboardEntry("s0", 0, Text.literal("À"), null));
        this.separator.add(new ScoreboardEntry("s1", 0, Text.literal("À"), null));
        this.separator.add(new ScoreboardEntry("s2", 0, Text.literal("À"), null));
        this.separator.add(new ScoreboardEntry("s3", 0, Text.literal("À"), null));
        this.separator.add(new ScoreboardEntry("s4", 0, Text.literal("À"), null));
        this.separator.add(new ScoreboardEntry("s5", 0, Text.literal("À"), null));
        this.separator.add(new ScoreboardEntry("s6", 0, Text.literal("À"), null));
        this.separator.add(new ScoreboardEntry("s7", 0, Text.literal("À"), null));
    }

    public void clearCache() {
        this.wholeCacheKey = new ArrayList<>();
        this.wholeCacheVal = new ArrayList<>();
        this.segmentCache.clear();
    }

    public List<ScoreboardEntry> translateScoreboardSidebar(List<ScoreboardEntry> entries) {
        try {
            return getCacheOrTranslate(entries);
        }
        catch(Exception e) {
            WynnTrans.LOGGER.error("Exception thrown in translating scoreboard texts\n", e);
            for(ScoreboardEntry entry : entries)
                debugClass.writeString2File(entry.owner(), "json.txt", "ScoreboardException");
            return entries;
        }

    }

    private List<ScoreboardEntry> getCacheOrTranslate(List<ScoreboardEntry> entries) {
        if(!entries.equals(wholeCacheKey)) {
            List<ScoreboardEntry> modified = splitEntry(entries).stream()
                    .map(segments -> {
                        List<ScoreboardEntry> value = segmentCache.get(segments);
                        if(value == null) {
                            List<ScoreboardEntry> translated = translateEntries(segments);
                            segmentCache.put(segments, translated);
                            return translated;
                        }
                        return value;
                    })
                    .reduce(new ArrayList<>(), this::concatEntry);
            wholeCacheKey = entries;
            wholeCacheVal = modified;
        }
        return wholeCacheVal;
    }

    private List<List<ScoreboardEntry>> splitEntry(List<ScoreboardEntry> entries) {
        List<ScoreboardEntry> copy = new ArrayList<>(entries);

        List<List<ScoreboardEntry>> segments = new ArrayList<>();
        List<ScoreboardEntry> segment = new ArrayList<>();
        for(ScoreboardEntry entry : copy) {
            if(entry.owner().matches("À+")) {
                if(!segment.isEmpty()) segments.add(segment);
                segment = new ArrayList<>();
            }
            else {
                segment.add(entry);
            }
        }
        if(!segment.isEmpty()) segments.add(segment);

        return segments;
    }

    private List<ScoreboardEntry> concatEntry(List<ScoreboardEntry> entry0, List<ScoreboardEntry> entry1) {
        entry0.add(separator.getFirst());
        entry0.addAll(entry1);
        return entry0;
    }

    private List<ScoreboardEntry> translateEntries(List<ScoreboardEntry> entries) {
        List<String> ids = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<NumberFormat> visuals = new ArrayList<>();

        for(ScoreboardEntry entry : entries) {
            ids.add(entry.owner());
            scores.add(entry.value());
            visuals.add(entry.numberFormatOverride());
        }

        List<Text> translatedTexts = ScoreboardContentType.findAndRun(ids.stream().map(Text::literal).map(t -> (Text) t).toList());

        List<ScoreboardEntry> result = new ArrayList<>();
        for(int i = 0, k = 0, size = translatedTexts.size(), limit = ids.size(); i < size; i++) {
            k = i < limit ? i : k;
            String id = i < limit ? ids.get(k) : ids.get(k) + i;
            int score = scores.get(k);
            Text translatedText = translatedTexts.get(i);
            NumberFormat visual = visuals.get(k);
            result.add(new ScoreboardEntry(id, score, translatedText, visual));
        }

        return result;
    }
}
