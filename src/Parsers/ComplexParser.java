package Parsers;

import Numbers.ComplexNumbers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexParser {

    public ComplexNumbers parse(String text) {


        String normalized = text
                .replaceAll("\\s+", "")
                .replace("*", "");

        Pattern pattern = Pattern.compile(
                "^([+-]?\\d+)([+-])(\\d*)i$"
        );

        Matcher matcher = pattern.matcher(normalized);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Numar complex invalid: " + text
            );
        }

        int realPart =
                Integer.parseInt(matcher.group(1));

        String sign = matcher.group(2);
        String coefficient = matcher.group(3);

        int imaginaryPart =
                coefficient.isEmpty()
                        ? 1
                        : Integer.parseInt(coefficient);

        if (sign.equals("-")) {
            imaginaryPart = -imaginaryPart;
        }

        return new ComplexNumbers(
                realPart,
                imaginaryPart
        );
    }
}