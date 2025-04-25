package net.hexagreen.wynntrans.text.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hexagreen.wynntrans.WynnTransFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Rulebooks {
    public final NormalizerRulebook npcDialogRulebook;
    public final NormalizerRulebook selectionRulebook;
    public final NormalizerRulebook narrationRulebook;
    public final NormalizerRulebook displayRuleBook;

    public Rulebooks() {
        this.npcDialogRulebook = new NormalizerRulebook(readRules("rulebook/npc-dialog-rules.json"));
        this.selectionRulebook = new NormalizerRulebook(readRules("rulebook/selection-rules.json"));
        this.narrationRulebook = new NormalizerRulebook(readRules("rulebook/narration-rules.json"));
        this.displayRuleBook = new NormalizerRulebook(readRules("rulebook/display-rules.json"));
    }

    private List<NormalizerRule> readRules(String filePath) {
        List<NormalizerRule> rulebook = new ArrayList<>();

        JsonObject json = WynnTransFileManager.readJsonInResources(filePath);
        JsonArray rules = json != null ? json.getAsJsonArray("rules") : null;
        for(int i = 0, size = (rules != null ? rules.size() : 0); i < size; i++) {
            JsonObject rule = rules.get(i).getAsJsonObject();
            String selector = rule.get("selector").getAsString();
            String pattern = rule.get("pattern").getAsString();
            String replace = rule.get("replace").getAsString();
            JsonElement order = rule.get("order");
            int ruleNumber = order != null ? order.getAsInt() : 0;
            JsonElement register = rule.get("register");
            boolean registration = register != null && register.getAsBoolean();
            rulebook.add(new NormalizerRule(selector, pattern, replace, ruleNumber, registration));
        }

        return rulebook;
    }

    public record NormalizerRulebook(List<NormalizerRule> rules) {
        public NormalizerRule findRule(String target, int ruleNumber) {
            return rules.parallelStream()
                    .filter(rule -> rule.ruleNumber == ruleNumber)
                    .filter(rule -> rule.match(target))
                    .findAny().orElse(null);
        }
    }

    public record NormalizerRule(String selector, String pattern, String replace, int ruleNumber,
                                 boolean registration) {
        private boolean match(String target) {
            return Pattern.compile(selector).matcher(target).matches();
        }
    }
}
