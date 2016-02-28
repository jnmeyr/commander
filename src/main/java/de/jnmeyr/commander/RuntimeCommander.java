package de.jnmeyr.commander;

import de.jnmeyr.commander.arguments.BooleanArgument;
import de.jnmeyr.commander.arguments.IntegerArgument;
import de.jnmeyr.commander.exceptions.IllegalArgumentsException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RuntimeCommander {

    private static final Pattern PATTERN = Pattern.compile("^([a-zA-Z_$][a-zA-Z_$0-9]*)(=(\\S+))?$");

    public RuntimeCommander(final String[] strings) {
        final Map<String, String> arguments = new HashMap<String, String>();

        if (strings != null) {
            for (final String string : strings) {
                final Matcher matcher = PATTERN.matcher(string);
                if (matcher.find()) {
                    final String key = matcher.group(1);
                    final String value = matcher.group(3);
                    arguments.put(key, value);
                }
            }
        }

        for (final Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(BooleanArgument.class)) {
                final BooleanArgument booleanArgument = field.getAnnotation(BooleanArgument.class);
                final boolean value;
                if (arguments.containsKey(field.getName())) {
                    value = arguments.get(field.getName()) == null || Boolean.parseBoolean(arguments.get(field.getName()));
                } else {
                    value = booleanArgument.fallback();
                }
                try {
                    field.setBoolean(this, value);
                } catch (final IllegalAccessException illegalAccessException) {
                    throw new IllegalArgumentsException(illegalAccessException);
                }
            } else if (field.isAnnotationPresent(IntegerArgument.class)) {
                final IntegerArgument integerArgument = field.getAnnotation(IntegerArgument.class);
                int value;
                try {
                    value = Integer.parseInt(arguments.get(field.getName()));
                } catch (final NumberFormatException numberFormatException) {
                    value = integerArgument.fallback();
                }
                try {
                    field.setInt(this, value);
                } catch (final IllegalAccessException illegalAccessException) {
                    throw new IllegalArgumentsException(illegalAccessException);
                }
            }
        }
    }

}
