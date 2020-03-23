
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class JerkSONParser {

    public String parse(String jerkSONText) {
        //replace all  @, ^, *, % symbols with :
        String cleanValue = searchAndReplace(jerkSONText, "[%*^!@,]", ":");
        //replace '##' with  ','
        cleanValue = searchAndReplace(cleanValue, "##", "");

        List<Item> items = processData(cleanValue);
        return createReport(items);

    }

    private String createReport(List<Item> items) {
        Map<String, Integer> nameCountMap = new HashMap<String, Integer>();
        Map<String, Integer> namePriceCountMap = new HashMap<String, Integer>();

        for (Item item : items) {
            if (nameCountMap.containsKey(item.getName())) {
                int count = nameCountMap.get(item.getName());
                nameCountMap.put(item.getName(), count + 1);

            } else {
                nameCountMap.put(item.getName(), 1);
            }
            if (namePriceCountMap.containsKey(item.getName() + item.getPrice())) {
                int count = namePriceCountMap.get(item.getName() + item.getPrice());
                namePriceCountMap.put(item.getName() + item.getPrice(), count + 1);
            } else {
                namePriceCountMap.put(item.getName() + item.getPrice(), 1);

            }

        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : nameCountMap.entrySet()) {
            if ((entry.getKey() != null) && (entry.getKey().trim() != "")) {

                sb.append("name: " + entry.getKey() + "\t seen: " + entry.getValue() + " times\n");
            }
        }
        return sb.toString();
    }

    private List<Item> processData(String cleanValue) {
        String[] lines = cleanValue.split("\n");
        List<Item> itemList = new ArrayList<Item>();
        for (String line : lines) {
            Item item = new Item();
            String nameValue = getValueForKey(line.toLowerCase(), "name");
            String priceValue = getValueForKey(line.toLowerCase(), "price");
            String typeValue = getValueForKey(line.toLowerCase(), "type");
            String expirationValue = getValueForKey(line.toLowerCase(), "expiration");
            if (nameValue != null) {
                item.setName(nameValue.replaceAll("0", "o"));
            }
            item.setPrice(priceValue);
            item.setType(typeValue);
            item.setExpiration(expirationValue);
            itemList.add(item);
        }
        return itemList;
    }


    private String wordWithNumberCleanup(String jerkSONText) {
        Pattern regex = Pattern.compile("\b.*[A-Za-z0].*\b");
        Matcher regexMatcher = regex.matcher(jerkSONText);
        while (regexMatcher.find()) {
            String word = regexMatcher.group();
            System.out.println(word);
        }
        return null;
    }

    private String searchAndReplace(String textToCleanUp, String regexString, String replaceWith) {
        String resultString = null;

        try {
            Pattern regex = Pattern.compile(regexString);
            Matcher regexMatcher = regex.matcher(textToCleanUp);
            try {
                resultString = regexMatcher.replaceAll(replaceWith);

            } catch (IllegalArgumentException ex) {

            } catch (IndexOutOfBoundsException ex) {

            }
        } catch (PatternSyntaxException ex) {

        }

        return resultString;
    }

    public String getValueForKey(String line, String key) {
        if ((line.indexOf(key + ":")) == -1) {
            return null;
        }
        int startPositionOfColon = line.indexOf(key + ":") + (key + ":").length();
        int endPosition = line.indexOf(";", startPositionOfColon);
        if (endPosition == -1) {
            endPosition = line.length();
        }
        return line.substring(startPositionOfColon, endPosition);
    }




}
