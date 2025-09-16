package net.hexagreen.wynntrans.text.chat;

import com.google.gson.Gson;
import net.hexagreen.wynntrans.WynnTransFileManager;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Rulebooks {
    private static final Gson gson = WynnTransFileManager.gson;
    public final NormalizerRulebook npcDialogRulebook;
    public final NormalizerRulebook selectionRulebook;
    public final NormalizerRulebook narrationRulebook;
    public final NormalizerRulebook displayRuleBook;
    public final ItemTooltipKeyBook itemTooltipKeyBook;

    public Rulebooks() {
        this.npcDialogRulebook = new NormalizerRulebook().fromResource("rulebook/npc-dialog-rules.json");
        this.selectionRulebook = new NormalizerRulebook().fromResource("rulebook/selection-rules.json");
        this.narrationRulebook = new NormalizerRulebook().fromResource("rulebook/narration-rules.json");
        this.displayRuleBook = new NormalizerRulebook().fromResource("rulebook/display-rules.json");
        this.itemTooltipKeyBook = new ItemTooltipKeyBook().fromResource("rulebook/item-translations.json");
    }

    abstract static class TranslationTemplate<T extends TranslationTemplate<T>> {

        protected abstract Class<T> getTemplateClass();

        public T fromResource(String filePath) {
            try(Reader reader = WynnTransFileManager.getReaderInResources(filePath)) {
                return gson.fromJson(reader, getTemplateClass());
            }
            catch(IOException e) {
                throw new RuntimeException("Failed to read translation template: " + filePath, e);
            }
        }
    }

    public static class NormalizerRulebook extends TranslationTemplate<NormalizerRulebook> {
        public List<NormalizerRule> rules = new ArrayList<>();

        public NormalizerRule findRule(String target, int ruleNumber) {
            return rules.stream()
                    .filter(rule -> rule.order == ruleNumber)
                    .filter(rule -> rule.match(target))
                    .findAny().orElse(null);
        }

        @Override
        protected Class<NormalizerRulebook> getTemplateClass() {
            return NormalizerRulebook.class;
        }

        public record NormalizerRule(String selector, String pattern, String replace, int order, boolean register) {
            private boolean match(String target) {
                return Pattern.compile(selector).matcher(target).matches();
            }
        }
    }

    public static class ItemTooltipKeyBook extends TranslationTemplate<ItemTooltipKeyBook> {
        public List<ItemTooltipTranslationKey> items = new ArrayList<>();

        public ItemTooltipTranslationKey findItem(String target) {
            return items.stream()
                    .filter(item -> item.match(target))
                    .findAny().orElse(null);
        }

        @Override
        protected Class<ItemTooltipKeyBook> getTemplateClass() {
            return ItemTooltipKeyBook.class;
        }

        public record ItemTooltipTranslationKey(String itemName, String key, boolean cleanMode,
                                                List<ItemTooltipLine> lines) {
            private boolean match(String target) {
                return Pattern.compile(itemName).matcher(target).matches();
            }
        }

        public record ItemTooltipLine(int lineNumber, String key, List<Integer> preservedSiblings) {
            public ItemTooltipLine {
                if(preservedSiblings == null) preservedSiblings = List.of();
            }
        }
    }
}
