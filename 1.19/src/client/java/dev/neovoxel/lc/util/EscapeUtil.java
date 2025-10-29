package dev.neovoxel.lc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EscapeUtil {
    private static final Pattern UNICODE_PATTERN = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
    private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\\\(.)");

    public static String unescapeAll(String input) {
        if (input == null) return null;

        // 先处理Unicode转义
        Matcher unicodeMatcher = UNICODE_PATTERN.matcher(input);
        StringBuffer unicodeBuffer = new StringBuffer();
        while (unicodeMatcher.find()) {
            String hex = unicodeMatcher.group(1);
            char unicodeChar = (char) Integer.parseInt(hex, 16);
            unicodeMatcher.appendReplacement(unicodeBuffer, Matcher.quoteReplacement(String.valueOf(unicodeChar)));
        }
        unicodeMatcher.appendTail(unicodeBuffer);

        // 处理其他转义序列
        Matcher escapeMatcher = ESCAPE_PATTERN.matcher(unicodeBuffer.toString());
        StringBuffer finalBuffer = new StringBuffer();
        while (escapeMatcher.find()) {
            String replacement = switch (escapeMatcher.group(1)) {
                case "n" -> "\n";
                case "t" -> "\t";
                case "r" -> "\r";
                case "b" -> "\b";
                case "f" -> "\f";
                case "\"" -> "\"";
                case "'" -> "'";
                case "\\" -> "\\";
                default -> escapeMatcher.group(0); // 未知转义，保持原样
            };
            escapeMatcher.appendReplacement(finalBuffer, Matcher.quoteReplacement(replacement));
        }
        escapeMatcher.appendTail(finalBuffer);

        return finalBuffer.toString();
    }
}