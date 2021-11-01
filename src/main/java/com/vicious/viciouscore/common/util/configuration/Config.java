package com.vicious.viciouscore.common.util.configuration;


import com.vicious.viciouscore.common.util.file.FileUtil;
import com.vicious.viciouscore.common.util.tracking.JSONTrackable;
import com.vicious.viciouscore.common.util.tracking.values.TrackableValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Config extends JSONTrackable<Config> {
    public static Map<Path,Config> temp = new HashMap<>();
    public static Map<String,Config> namedInstances = new HashMap<>();

    public Config(Path f) {
        super(f, ()->{
            temp.get(f).overWriteFile();
            temp.remove(f);
        });
        temp.put(f,this);
    }
    public Config(Path f, String modid, String configName) {
        super(f, ()->{
            temp.get(f).overWriteFile();
            temp.remove(f);
        });
        temp.put(f,this);
        namedInstances.put(modid + ":" + configName,this);
    }

    public void save() {
        overWriteFile();
    }
    public void overWriteFile() {
        StringWriter writer = new StringWriter();
        writer.write("{");
        TrackableValue<?>[] vals = values.values().toArray(new TrackableValue<?>[0]);
        for (int i = 0; i < vals.length; i++) {
            try {
                TrackableValue<?> value = vals[i];
                writer.append("\n");
                if (value instanceof ConfigurationValue<?>) {
                    writer.append(((ConfigurationValue<?>) value).getTab() + '"' + value.name + '"' + ": ");
                    writeValue(writer,((ConfigurationValue<?>) value).getStopValue(),0,0);
                } else {
                    writer.append('"' + value.name + '"' + ": ");
                    writeValue(writer,value.value(),0,0);
                }
                if (i < vals.length - 1) writer.append(",");
            } catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        writer.append("\n}");
        FileUtil.createOrWipe(PATH);
        try {
            Files.write(PATH, writer.toString().getBytes(), StandardOpenOption.WRITE);
        } catch(IOException e){
            System.out.println("Failed to save a Config " + getClass().getCanonicalName() + " caused by: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // ALL OF THE FOLLOWING
    // Copied from JSONObject
    static final Writer writeValue(Writer writer, Object value,
                                   int indentFactor, int indent) throws JSONException, IOException {
        if (value == null || value.equals(null)) {
            writer.write("null");
        } else if (value instanceof JSONString) {
            Object o;
            try {
                o = ((JSONString) value).toJSONString();
            } catch (Exception e) {
                throw new JSONException(e);
            }
            writer.write(o != null ? o.toString() : quote(value.toString()));
        } else if (value instanceof Number) {
            // not all Numbers may match actual JSON Numbers. i.e. fractions or Imaginary
            final String numberAsString = numberToString((Number) value);
            try {
                // Use the BigDecimal constructor for it's parser to validate the format.
                @SuppressWarnings("unused")
                BigDecimal testNum = new BigDecimal(numberAsString);
                // Close enough to a JSON number that we will use it unquoted
                writer.write(numberAsString);
            } catch (NumberFormatException ex){
                // The Number value is not a valid JSON number.
                // Instead we will quote it as a string
                quote(numberAsString, writer);
            }
        } else if (value instanceof Boolean) {
            writer.write(value.toString());
        } else if (value instanceof Enum<?>) {
            writer.write(quote(((Enum<?>)value).name()));
        } else if (value instanceof JSONObject) {
            ((JSONObject) value).write(writer, indentFactor, indent);
        } else if (value instanceof JSONArray) {
            ((JSONArray) value).write(writer, indentFactor, indent);
        } else if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            new JSONObject(map).write(writer, indentFactor, indent);
        } else if (value instanceof Collection) {
            Collection<?> coll = (Collection<?>) value;
            new JSONArray(coll).write(writer, indentFactor, indent);
        } else if (value.getClass().isArray()) {
            new JSONArray(value).write(writer, indentFactor, indent);
        } else {
            quote(value.toString(), writer);
        }
        return writer;
    }
    public static String quote(String str){
        return '"' + str + '"';
    }
    public static String numberToString(Number number) throws JSONException {
        if (number == null) {
            throw new JSONException("Null pointer");
        }
        testValidity(number);

        // Shave off trailing zeros and decimal point, if possible.

        String string = number.toString();
        if (string.indexOf('.') > 0 && string.indexOf('e') < 0
                && string.indexOf('E') < 0) {
            while (string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }
            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }
    public static void testValidity(Object o) throws JSONException {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
                    throw new JSONException(
                            "JSON does not allow non-finite numbers.");
                }
            } else if (o instanceof Float) {
                if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
                    throw new JSONException(
                            "JSON does not allow non-finite numbers.");
                }
            }
        }
    }
    public static Writer quote(String string, Writer w) throws IOException {
        if (string == null || string.length() == 0) {
            w.write("\"\"");
            return w;
        }

        char b;
        char c = 0;
        String hhhh;
        int i;
        int len = string.length();

        w.write('"');
        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    w.write('\\');
                    w.write(c);
                    break;
                case '/':
                    if (b == '<') {
                        w.write('\\');
                    }
                    w.write(c);
                    break;
                case '\b':
                    w.write("\\b");
                    break;
                case '\t':
                    w.write("\\t");
                    break;
                case '\n':
                    w.write("\\n");
                    break;
                case '\f':
                    w.write("\\f");
                    break;
                case '\r':
                    w.write("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
                            || (c >= '\u2000' && c < '\u2100')) {
                        w.write("\\u");
                        hhhh = Integer.toHexString(c);
                        w.write("0000", 0, 4 - hhhh.length());
                        w.write(hhhh);
                    } else {
                        w.write(c);
                    }
            }
        }
        w.write('"');
        return w;
    }
}
