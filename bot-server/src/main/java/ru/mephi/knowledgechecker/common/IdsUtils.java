package ru.mephi.knowledgechecker.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mephi.knowledgechecker.common.Constants.SEMICOLON;

public class IdsUtils {
    public static List<Long> parseIds(String string) {
        try {
            return Arrays.stream(string.split(SEMICOLON))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }

    public static String concatIt(String previously, Long nextOne) {
        if (previously.length() == 0) {
            return nextOne.toString();
        }
        return previously + SEMICOLON + nextOne;
    }
}
