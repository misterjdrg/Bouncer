package com.nokia.mid.appl.boun;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Localisation {
    private static Localisation instance = null;
    private static DataInputStream dis = null;
    public static final String locale = System.getProperty("microedition.locale");

    private Localisation() {
    }

    private static String replayFirst(String haystack, String needle, String replacement) {
        int indexOfNeedle = haystack.indexOf(needle);
        return indexOfNeedle >= 0 ? haystack.substring(0, indexOfNeedle) + replacement + haystack.substring(indexOfNeedle + needle.length()) : haystack;
    }

    public static synchronized String getString(int index) {
        return getFormatedString(index, (String[]) null);
    }

    public static synchronized String getFormatedString(int index, String[] formatArgs) {
        try {
            if (instance == null) {
                instance = new Localisation();
            }

            if (dis == null) {
                InputStream is = instance.getClass().getResourceAsStream("/lang." + locale);
                if (is == null) {
                    is = instance.getClass().getResourceAsStream("/lang.xx");
                }

                if (is == null) {
                    return "NoLang";
                }

                dis = new DataInputStream(is);
                dis.mark(512);
            }

            dis.skipBytes(index * 2);
            short var7 = dis.readShort();
            dis.skipBytes(var7 - index * 2 - 2);
            String value = dis.readUTF();

            try {
                dis.reset();
            } catch (IOException e) {
                dis.close();
                dis = null;
            }

            if (formatArgs != null) {
                if (formatArgs.length == 1) {
                    value = replayFirst(value, "%U", formatArgs[0]);
                } else {
                    for (int i = 0; i < formatArgs.length; ++i) {
                        value = replayFirst(value, "%" + i + "U", formatArgs[i]);
                    }
                }
            }

            return value;
        } catch (IOException e) {
            return "Err";
        }
    }
}
