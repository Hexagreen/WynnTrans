package net.hexagreen.wynntrans.text.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.hexagreen.wynntrans.WynnTransFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Rulebooks {
    public final NormalizerRulebook npcDialogRulebook;
    public final NormalizerRulebook selectionRulebook;
    public final NormalizerRulebook narrationRulebook;

    public Rulebooks() {
        this.npcDialogRulebook = new NormalizerRulebook(readRules("WynnTrans/rulebook/NpcDialogRules.json"));
        this.selectionRulebook = new NormalizerRulebook(readRules("WynnTrans/rulebook/SelectionRules.json"));
        this.narrationRulebook = new NormalizerRulebook(readRules("WynnTrans/rulebook/NarrationRules.json"));
    }

    private List<NormalizerRule> readRules(String filePath) {
        List<NormalizerRule> rulebook = new ArrayList<>();

        JsonObject json = WynnTransFileManager.readJson(filePath);
        JsonArray rules = json != null ? json.getAsJsonArray("rules") : null;
        for(int i = 0, size = (rules != null ? rules.size() : 0); i < size; i++) {
            JsonObject rule = rules.get(i).getAsJsonObject();
            String selector = rule.get("selector").getAsString();
            String pattern = rule.get("pattern").getAsString();
            String replace = rule.get("replace").getAsString();
            rulebook.add(new NormalizerRule(selector, pattern, replace));
        }

        return rulebook;
    }

    public record NormalizerRulebook(List<NormalizerRule> rules) {
        public NormalizerRule findRule(String target) {
            return rules.parallelStream()
                    .filter(rule -> rule.match(target))
                    .findAny().orElse(null);
        }
    }

    public record NormalizerRule(String selector, String pattern, String replace) {
        private boolean match(String target) {
            return Pattern.compile(selector).matcher(target).matches();
        }
    }
}
