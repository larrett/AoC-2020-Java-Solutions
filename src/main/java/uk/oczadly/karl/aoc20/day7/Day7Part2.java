package uk.oczadly.karl.aoc20.day7;

import uk.oczadly.karl.aoc20.Helper;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Karl Oczadly
 */
public class Day7Part2 {
    
    static final String TARGET_COLOUR = "shiny gold";
    static final Pattern RULE_PATTERN = Pattern.compile(
            "(?:(^\\w+ \\w+) bags contain )|(?:\\G(\\d+) (\\w+ \\w+) bags?(?:, |.))");
    
    
    public static void main(String[] args) {
        // Load bag rules in
        Map<String, Set<ContainingBagRule>> bagRules = loadBagRules();
    
        // Calculate bag counts
        int count = calculateBagCount(TARGET_COLOUR, bagRules, new HashMap<>());
        
        System.out.printf("Bags held within '%s': %d%n", TARGET_COLOUR, count);
    }
    
    
    /**
     * Recursive method to calculate the number of contained bags.
     * The 'cache' map is used to store already computed values, meaning they don't need to be re-calculated.
     * Note that the cache is likely useless for this puzzle, given the small input size; with a larger number of bag
     * types, this would greatly help to reduce the calculation time.
     */
    private static int calculateBagCount(String colour, Map<String, Set<ContainingBagRule>> bagRules,
                                         Map<String, Integer> cache) {
        if (cache.containsKey(colour)) return cache.get(colour); // Use cached value if available
        // Count the number of bags held inside this bag colour
        int count = 0;
        for (ContainingBagRule rule : bagRules.get(colour))
            count += (calculateBagCount(rule.colour, bagRules, cache) + 1) * rule.count;
        cache.put(colour, count); // Store in the cache for later use
        return count;
    }
    
    /** Loads all of the bag rules from the input data. */
    private static Map<String, Set<ContainingBagRule>> loadBagRules() {
        Map<String, Set<ContainingBagRule>> bagRules = new HashMap<>();
        Helper.streamInput(7)
                .map(RULE_PATTERN::matcher)
                .forEach(matcher -> {
                    String name = null;
                    Set<ContainingBagRule> contains = new HashSet<>();
                    while (matcher.find()) {
                        if (name == null) {
                            // First match is the colour of the bag
                            name = matcher.group(1).toLowerCase();
                        } else {
                            // Subsequent matches are what the bag can contain
                            contains.add(new ContainingBagRule(
                                    matcher.group(3).toLowerCase(), Integer.parseInt(matcher.group(2))));
                        }
                    }
                    bagRules.put(name, contains);
                });
        return bagRules;
    }
    
    
    static class ContainingBagRule {
        final String colour;
        final int count;
        
        public ContainingBagRule(String colour, int count) {
            this.colour = colour;
            this.count = count;
        }
    }
    
}
