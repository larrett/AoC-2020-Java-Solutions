package uk.oczadly.karl.aoc20.day4;

import uk.oczadly.karl.aoc20.Helper;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Karl Oczadly
 */
public class Day4Part1 {
    
    static final Pattern FIELD_PATTERN = Pattern.compile("(\\w+):(\\S+)");
    
    public static void main(String[] args) throws Exception {
        int valid = 0;
        EnumSet<FieldType> presentFields = EnumSet.noneOf(FieldType.class);
        
        List<String> data = Helper.loadInput(4);
        data.add(""); // Add empty line to process final group
        for (String ln : data) {
            if (ln.isEmpty()) { // Empty line
                if (!presentFields.isEmpty() && isValid(presentFields))
                    valid++;
                presentFields.clear();
            } else {
                Matcher m = FIELD_PATTERN.matcher(ln);
                while (m.find())
                    presentFields.add(FieldType.ofCodename(m.group(1)));
            }
        }
    
        System.out.printf("Valid passports: %d%n", valid);
    }
    
    /** Returns true if the set contains all the required fields. */
    public static boolean isValid(Set<FieldType> fields) {
        for (FieldType p : FieldType.values()) {
            if (p.required && !fields.contains(p))
                return false;
        }
        return true;
    }
    
    enum FieldType {
        BIRTH_YEAR      ("byr", true),
        ISSUE_YEAR      ("iyr", true),
        EXPIRATION_YEAR ("eyr", true),
        HEIGHT          ("hgt", true),
        HAIR_COLOUR     ("hcl", true),
        EYE_COLOUR      ("ecl", true),
        PASSPORT_ID     ("pid", true),
        COUNTRY_ID      ("cid", false);
        
        final String codename;
        final boolean required;
        FieldType(String codename, boolean required) {
            this.codename = codename;
            this.required = required;
        }
        
        public static FieldType ofCodename(String codename) {
            for (FieldType p : values()) {
                if (p.codename.equalsIgnoreCase(codename))
                    return p;
            }
            throw new IllegalArgumentException("Unknown property codename.");
        }
    }
    
}
