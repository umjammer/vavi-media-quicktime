/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * The XML parser component ('pars', 'xml ') in Java.
 * <p>
 * Documents are built in the heap as the Java classes read them:
 * XMLDoc: storage(4) root XMLElement(16);
 * XMLElement: identifier(4, -1 unrecognized) name(4) attributes(4) contents(4);
 * XMLAttribute (22 bytes, arrays end with a zero identifier): identifier(4) name(4) valueKind(4) value(6) valueString(4);
 * XMLElementContent (20 bytes, arrays end with kind 0): kind(4, 1 element, 2 character data) element or string pointer(4).
 */
public final class XMLParserManager {

    private XMLParserManager() {
    }

    static final int pars = Bytes.fourCC("pars");
    static final int xml = Bytes.fourCC("xml ");

    static final short noErr = 0;
    static final short paramErr = -50;
    static final short ioErr = -36;
    static final short fnfErr = -43;
    static final short badComponentInstance = (short) 0x8001;
    /** the parse failed, the details are in XMLParseGetDetailedParseError */
    static final short xmlParseErr = -2020;

    static final int xmlIdentifierUnrecognized = -1;
    static final int xmlContentTypeElement = 1;
    static final int xmlContentTypeCharData = 2;

    // parse flags
    static final int xmlParseFlagAllowUppercase = 1;
    static final int xmlParseFlagAllowUnquotedAttributeValues = 2;
    static final int xmlParseFlagEventParseOnly = 4;

    // attribute value kinds
    static final int attributeValueKindCharString = 0;
    static final int attributeValueKindInteger = 1;
    static final int attributeValueKindPercent = 2;
    static final int attributeValueKindBoolean = 4;
    static final int attributeValueKindOnOff = 8;
    static final int attributeValueKindColor = 16;
    static final int attributeValueKindEnum = 32;
    static final int attributeValueKindCaseSensEnum = 64;

    static final int ELEMENT = 16;
    static final int ATTRIBUTE = 22;
    static final int CONTENT = 20;

    static void register() {
        ComponentManager.register(pars, xml, 0, "XML Parser", "XML parser", c -> new Parser());
    }

    /** a registered attribute */
    static final class Attr {
        final int id;
        final String name;
        int kinds;
        List<String> enumValues = new ArrayList<>();
        boolean caseSensitiveEnum;

        Attr(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /** a parser instance */
    static final class Parser {
        final Map<String, Integer> elements = new HashMap<>();
        final Map<Integer, Map<String, Attr>> attributes = new HashMap<>();
        final Map<String, Integer> nameSpaces = new HashMap<>();
        int nextElement = 1;
        int nextAttribute = 1;
        int nextNameSpace = 1;
        long offset;
        long limit = -1;
        int errorLine;
        String errorDescription = "";
        int startDocument, endDocument, startElement, endElement, charData, comment, preprocess;
        final Map<Integer, List<Integer>> documents = new HashMap<>();
    }

    static Parser parser(int ci) {
        ComponentManager.InstanceRec i = ComponentManager.instance(ci);
        return i != null && i.impl instanceof Parser p ? p : null;
    }

    private static String cString(byte[] b) {
        int n = 0;
        while (n < b.length && b[n] != 0) {
            n++;
        }
        return new String(b, 0, n, StandardCharsets.UTF_8);
    }

    // ---- registration

    public static short XMLParseAddElement(int ci, byte[] elementName, int nameSpaceID, int[] elementID, int elementFlags) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        String name = cString(elementName);
        int id = elementID[0] != 0 ? elementID[0] : p.nextElement++;
        p.nextElement = Math.max(p.nextElement, id + 1);
        p.elements.put(key(nameSpaceID, name), id);
        elementID[0] = id;
        return noErr;
    }

    private static String key(int nameSpaceID, String name) {
        return nameSpaceID + ":" + name;
    }

    public static short XMLParseAddAttribute(int ci, int elementID, int nameSpaceID, byte[] attributeName, int[] attributeID) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        String name = cString(attributeName);
        int id = attributeID[0] != 0 ? attributeID[0] : p.nextAttribute++;
        p.nextAttribute = Math.max(p.nextAttribute, id + 1);
        p.attributes.computeIfAbsent(elementID, k -> new HashMap<>()).put(key(nameSpaceID, name), new Attr(id, name));
        attributeID[0] = id;
        return noErr;
    }

    public static short XMLParseAddAttributeAndValue(int ci, int elementID, int nameSpaceID, byte[] attributeName, int[] attributeID, int valueKind,
                                                     byte[] valueKindInfo) {
        short e = XMLParseAddAttribute(ci, elementID, nameSpaceID, attributeName, attributeID);
        if (e != noErr) {
            return e;
        }
        return XMLParseAddAttributeValueKind(ci, elementID, attributeID[0], valueKind, valueKindInfo);
    }

    public static short XMLParseAddAttributeValueKind(int ci, int elementID, int attributeID, int valueKind, byte[] valueKindInfo) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        Map<String, Attr> m = p.attributes.get(elementID);
        Attr a = null;
        if (m != null) {
            for (Attr x : m.values()) {
                if (x.id == attributeID) {
                    a = x;
                }
            }
        }
        if (a == null) {
            return paramErr;
        }
        a.kinds |= valueKind;
        if ((valueKind & (attributeValueKindEnum | attributeValueKindCaseSensEnum)) != 0 && valueKindInfo != null) {
            a.caseSensitiveEnum = (valueKind & attributeValueKindCaseSensEnum) != 0;
            int s = 0;
            for (int i = 0; i < valueKindInfo.length; i++) {
                if (valueKindInfo[i] == 0) {
                    if (i == s) {
                        break;
                    }
                    a.enumValues.add(new String(valueKindInfo, s, i - s, StandardCharsets.UTF_8));
                    s = i + 1;
                }
            }
        }
        return noErr;
    }

    public static short XMLParseAddNameSpace(int ci, byte[] nameSpaceURL, int[] nameSpaceID) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        String url = cString(nameSpaceURL);
        Integer id = p.nameSpaces.get(url);
        if (id == null) {
            id = nameSpaceID[0] != 0 ? nameSpaceID[0] : p.nextNameSpace++;
            p.nameSpaces.put(url, id);
        }
        nameSpaceID[0] = id;
        return noErr;
    }

    public static short XMLParseSetOffsetAndLimit(int ci, int offset, int limit) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        p.offset = offset & 0xffff_ffffL;
        p.limit = limit == 0 || limit == -1 ? -1 : limit & 0xffff_ffffL;
        return noErr;
    }

    public static short XMLParseSetStartDocumentHandler(int ci, int proc) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        p.startDocument = proc;
        return noErr;
    }

    public static short XMLParseSetEndDocumentHandler(int ci, int proc) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        p.endDocument = proc;
        return noErr;
    }

    public static short XMLParseSetStartElementHandler(int ci, int proc) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        p.startElement = proc;
        return noErr;
    }

    public static short XMLParseSetEndElementHandler(int ci, int proc) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        p.endElement = proc;
        return noErr;
    }

    public static short XMLParseSetCharDataHandler(int ci, int proc) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        p.charData = proc;
        return noErr;
    }

    public static short XMLParseSetCommentHandler(int ci, int proc) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        p.comment = proc;
        return noErr;
    }

    public static short XMLParseSetPreprocessInstructionHandler(int ci, int proc) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        p.preprocess = proc;
        return noErr;
    }

    /** the error line, and the description as a Pascal string */
    public static short XMLParseGetDetailedParseError(int ci, int[] errorLine, byte[] errDesc) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        if (errorLine != null && errorLine.length > 0) {
            errorLine[0] = p.errorLine;
        }
        if (errDesc != null) {
            Bytes.putPString(errDesc, 0, p.errorDescription, Math.min(256, errDesc.length));
        }
        return noErr;
    }

    // ---- parsing

    /** a parsed element */
    static final class Node {
        final String name;
        final List<String[]> attributes = new ArrayList<>();
        final List<Object> contents = new ArrayList<>();

        Node(String name) {
            this.name = name;
        }
    }

    /** the tokenizer, building nodes and sending events */
    static final class Reader {
        final String s;
        final int flags;
        final Parser parser;
        final boolean events;
        int p;
        boolean stopped;

        Reader(String s, int flags, Parser parser) {
            this.s = s;
            this.flags = flags;
            this.parser = parser;
            this.events = (flags & xmlParseFlagEventParseOnly) != 0;
        }

        IOException error(String message) {
            int line = 1;
            for (int i = 0; i < Math.min(p, s.length()); i++) {
                if (s.charAt(i) == '\n') {
                    line++;
                }
            }
            parser.errorLine = line;
            parser.errorDescription = message;
            return new IOException(message + " at line " + line);
        }

        boolean starts(String t) {
            return s.startsWith(t, p);
        }

        void skipSpace() {
            while (p < s.length() && Character.isWhitespace(s.charAt(p))) {
                p++;
            }
        }

        String name() throws IOException {
            int b = p;
            while (p < s.length()) {
                char c = s.charAt(p);
                if (Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.' || c == ':') {
                    p++;
                } else {
                    break;
                }
            }
            if (p == b) {
                throw error("name expected");
            }
            String n = s.substring(b, p);
            if ((flags & xmlParseFlagAllowUppercase) == 0 && !n.equals(n.toLowerCase(Locale.ROOT))) {
                throw error("uppercase name: " + n);
            }
            return (flags & xmlParseFlagAllowUppercase) != 0 ? n.toLowerCase(Locale.ROOT) : n;
        }

        String entities(String t) throws IOException {
            if (t.indexOf('&') < 0) {
                return t;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < t.length(); i++) {
                char c = t.charAt(i);
                if (c != '&') {
                    sb.append(c);
                    continue;
                }
                int e = t.indexOf(';', i);
                if (e < 0) {
                    throw error("bad entity");
                }
                String n = t.substring(i + 1, e);
                switch (n) {
                    case "lt" -> sb.append('<');
                    case "gt" -> sb.append('>');
                    case "amp" -> sb.append('&');
                    case "quot" -> sb.append('"');
                    case "apos" -> sb.append('\'');
                    default -> {
                        if (n.startsWith("#x") || n.startsWith("#X")) {
                            sb.appendCodePoint(Integer.parseInt(n.substring(2), 16));
                        } else if (n.startsWith("#")) {
                            sb.appendCodePoint(Integer.parseInt(n.substring(1)));
                        } else {
                            sb.append('&').append(n).append(';');
                        }
                    }
                }
                i = e;
            }
            return sb.toString();
        }

        List<String[]> attributes(String end) throws IOException {
            List<String[]> l = new ArrayList<>();
            while (true) {
                skipSpace();
                if (p >= s.length()) {
                    throw error("unexpected end");
                }
                if (starts(end) || starts("/>") || starts(">")) {
                    return l;
                }
                String n = name();
                skipSpace();
                String v = "";
                if (starts("=")) {
                    p++;
                    skipSpace();
                    char q = p < s.length() ? s.charAt(p) : 0;
                    if (q == '"' || q == '\'') {
                        int e = s.indexOf(q, p + 1);
                        if (e < 0) {
                            throw error("unterminated attribute value");
                        }
                        v = entities(s.substring(p + 1, e));
                        p = e + 1;
                    } else if ((flags & xmlParseFlagAllowUnquotedAttributeValues) != 0) {
                        int b = p;
                        while (p < s.length() && !Character.isWhitespace(s.charAt(p)) && s.charAt(p) != '>' && !starts("/>") && !starts(end)) {
                            p++;
                        }
                        v = entities(s.substring(b, p));
                    } else {
                        throw error("unquoted attribute value");
                    }
                }
                l.add(new String[] {n, v});
            }
        }

        /** @return non 0 if the handler stopped the parse */
        boolean send(int proc, Object... args) {
            if (proc == 0 || stopped) {
                return stopped;
            }
            Object r = Closures.call(proc, args);
            if (r instanceof Number n && n.intValue() != 0) {
                stopped = true;
            }
            return stopped;
        }

        /** @return a pointer to a C string, freed after the event */
        int string(String t, List<Integer> temps) {
            byte[] b = t.getBytes(StandardCharsets.UTF_8);
            int ptr = Heap.newPtr(b.length + 1);
            Heap.write(ptr, b, 0, b.length);
            temps.add(ptr);
            return ptr;
        }

        int strings(List<String[]> attrs, List<Integer> temps) {
            int a = Heap.newPtr((attrs.size() * 2 + 1) * 4);
            temps.add(a);
            for (int i = 0; i < attrs.size(); i++) {
                Heap.putInt(a + i * 8, string(attrs.get(i)[0], temps));
                Heap.putInt(a + i * 8 + 4, string(attrs.get(i)[1], temps));
            }
            return a;
        }

        void event(int proc, Object... texts) {
            if (proc == 0 || stopped) {
                return;
            }
            List<Integer> temps = new ArrayList<>();
            try {
                Object[] args = new Object[texts.length + 1];
                for (int i = 0; i < texts.length; i++) {
                    args[i] = texts[i] instanceof String t ? (Object) string(t, temps)
                            : texts[i] instanceof List<?> l ? (Object) strings(castAttrs(l), temps) : texts[i];
                }
                args[texts.length] = 0;
                send(proc, args);
            } finally {
                for (int t : temps) {
                    Heap.free(t);
                }
            }
        }

        @SuppressWarnings("unchecked")
        private static List<String[]> castAttrs(List<?> l) {
            return (List<String[]>) l;
        }

        /** parses the document, @return the root element */
        Node document() throws IOException {
            if (events) {
                event(parser.startDocument);
            }
            Node root = null;
            while (p < s.length() && !stopped) {
                skipSpace();
                if (p >= s.length()) {
                    break;
                }
                if (starts("<?")) {
                    instruction();
                } else if (starts("<!--")) {
                    comment();
                } else if (starts("<!")) {
                    // DOCTYPE and the like, nested brackets included
                    int depth = 0;
                    while (p < s.length()) {
                        char c = s.charAt(p++);
                        if (c == '[') {
                            depth++;
                        } else if (c == ']') {
                            depth--;
                        } else if (c == '>' && depth <= 0) {
                            break;
                        }
                    }
                } else if (starts("<")) {
                    if (root != null) {
                        throw error("more than one root element");
                    }
                    root = element();
                } else {
                    throw error("content outside of the root element");
                }
            }
            if (root == null && !stopped) {
                throw error("no root element");
            }
            if (events) {
                event(parser.endDocument);
            }
            return root;
        }

        void instruction() throws IOException {
            p += 2;
            String n = name();
            List<String[]> attrs = attributes("?>");
            skipSpace();
            if (!starts("?>")) {
                // free form instructions
                int e = s.indexOf("?>", p);
                if (e < 0) {
                    throw error("unterminated processing instruction");
                }
                p = e;
            }
            p += 2;
            if (events) {
                event(parser.preprocess, n, attrs);
            }
        }

        String comment() throws IOException {
            int e = s.indexOf("-->", p + 4);
            if (e < 0) {
                throw error("unterminated comment");
            }
            String c = s.substring(p + 4, e);
            p = e + 3;
            if (events) {
                event(parser.comment, c);
            }
            return c;
        }

        Node element() throws IOException {
            p++;
            String n = name();
            List<String[]> attrs = attributes(">");
            Node node = new Node(n);
            node.attributes.addAll(attrs);
            if (events) {
                event(parser.startElement, n, attrs);
            }
            if (starts("/>")) {
                p += 2;
                if (events) {
                    event(parser.endElement, n);
                }
                return node;
            }
            p++;
            StringBuilder text = new StringBuilder();
            while (!stopped) {
                if (p >= s.length()) {
                    throw error("unterminated element: " + n);
                }
                if (starts("</")) {
                    flush(node, text);
                    p += 2;
                    String e = name();
                    if (!e.equals(n)) {
                        throw error("mismatched end tag: " + e + " for " + n);
                    }
                    skipSpace();
                    if (!starts(">")) {
                        throw error("> expected");
                    }
                    p++;
                    if (events) {
                        event(parser.endElement, n);
                    }
                    return node;
                } else if (starts("<!--")) {
                    flush(node, text);
                    comment();
                } else if (starts("<![CDATA[")) {
                    int e = s.indexOf("]]>", p);
                    if (e < 0) {
                        throw error("unterminated CDATA");
                    }
                    text.append(s, p + 9, e);
                    p = e + 3;
                } else if (starts("<?")) {
                    flush(node, text);
                    instruction();
                } else if (starts("<")) {
                    flush(node, text);
                    node.contents.add(element());
                } else {
                    int e = s.indexOf('<', p);
                    if (e < 0) {
                        e = s.length();
                    }
                    text.append(entities(s.substring(p, e)));
                    p = e;
                }
            }
            return node;
        }

        void flush(Node node, StringBuilder text) {
            if (text.isEmpty()) {
                return;
            }
            String t = text.toString();
            text.setLength(0);
            if (t.isBlank()) {
                return;
            }
            node.contents.add(t);
            if (events) {
                event(parser.charData, t);
            }
        }
    }

    // ---- documents in the heap

    /** allocations of a document */
    private static int alloc(List<Integer> blocks, int size) {
        int p = Heap.newPtr(size);
        blocks.add(p);
        return p;
    }

    private static int cStr(List<Integer> blocks, String t) {
        byte[] b = t.getBytes(StandardCharsets.UTF_8);
        int p = alloc(blocks, b.length + 1);
        Heap.write(p, b, 0, b.length);
        return p;
    }

    /** @return the identifier of the name, xmlIdentifierUnrecognized if not registered */
    private static int elementID(Parser parser, String name) {
        Integer id = parser.elements.get(key(0, name));
        if (id == null) {
            String local = name.contains(":") ? name.substring(name.indexOf(':') + 1) : name;
            for (Map.Entry<String, Integer> e : parser.elements.entrySet()) {
                if (e.getKey().endsWith(":" + local)) {
                    return e.getValue();
                }
            }
            return xmlIdentifierUnrecognized;
        }
        return id;
    }

    /** writes the element into the struct at the address */
    private static void element(Parser parser, Node n, int at, List<Integer> blocks) throws IOException {
        int id = elementID(parser, n.name);
        Heap.putInt(at, id);
        Heap.putInt(at + 4, cStr(blocks, n.name));
        if (!n.attributes.isEmpty()) {
            int a = alloc(blocks, (n.attributes.size() + 1) * ATTRIBUTE);
            Map<String, Attr> known = id != xmlIdentifierUnrecognized ? parser.attributes.getOrDefault(id, Map.of()) : Map.of();
            for (int i = 0; i < n.attributes.size(); i++) {
                attribute(parser, known, n.attributes.get(i), a + i * ATTRIBUTE, blocks);
            }
            Heap.putInt(at + 8, a);
        }
        if (!n.contents.isEmpty()) {
            int c = alloc(blocks, (n.contents.size() + 1) * CONTENT);
            for (int i = 0; i < n.contents.size(); i++) {
                Object o = n.contents.get(i);
                int e = c + i * CONTENT;
                if (o instanceof Node child) {
                    int ce = alloc(blocks, ELEMENT);
                    element(parser, child, ce, blocks);
                    Heap.putInt(e, xmlContentTypeElement);
                    Heap.putInt(e + 4, ce);
                } else {
                    Heap.putInt(e, xmlContentTypeCharData);
                    Heap.putInt(e + 4, cStr(blocks, (String) o));
                }
            }
            Heap.putInt(at + 12, c);
        }
    }

    private static void attribute(Parser parser, Map<String, Attr> known, String[] nv, int at, List<Integer> blocks) throws IOException {
        Attr a = known.get(key(0, nv[0]));
        if (a == null) {
            String local = nv[0].contains(":") ? nv[0].substring(nv[0].indexOf(':') + 1) : nv[0];
            for (Map.Entry<String, Attr> e : known.entrySet()) {
                if (e.getKey().endsWith(":" + local)) {
                    a = e.getValue();
                }
            }
        }
        // attributes registered for no element apply to all
        if (a == null) {
            Map<String, Attr> any = parser.attributes.get(0);
            if (any != null) {
                a = any.get(key(0, nv[0]));
            }
        }
        String v = nv[1];
        Heap.putInt(at, a != null ? a.id : xmlIdentifierUnrecognized);
        Heap.putInt(at + 4, cStr(blocks, nv[0]));
        Heap.putInt(at + 18, cStr(blocks, v));
        int kinds = a != null ? a.kinds : 0;
        String t = v.trim();
        String lower = t.toLowerCase(Locale.ROOT);
        if ((kinds & attributeValueKindInteger) != 0 && t.matches("[-+]?\\d+")) {
            Heap.putInt(at + 8, attributeValueKindInteger);
            Heap.putInt(at + 12, Integer.parseInt(t.startsWith("+") ? t.substring(1) : t));
        } else if ((kinds & attributeValueKindPercent) != 0 && t.matches("[-+]?\\d+%")) {
            Heap.putInt(at + 8, attributeValueKindPercent);
            Heap.putInt(at + 12, Integer.parseInt(t.substring(t.startsWith("+") ? 1 : 0, t.length() - 1)));
        } else if ((kinds & attributeValueKindBoolean) != 0 && (lower.equals("true") || lower.equals("false"))) {
            Heap.putInt(at + 8, attributeValueKindBoolean);
            Heap.putInt(at + 12, lower.equals("true") ? 1 : 0);
        } else if ((kinds & attributeValueKindOnOff) != 0 && (lower.equals("on") || lower.equals("off"))) {
            Heap.putInt(at + 8, attributeValueKindOnOff);
            Heap.putInt(at + 12, lower.equals("on") ? 1 : 0);
        } else if ((kinds & attributeValueKindColor) != 0 && color(t) != null) {
            Heap.putInt(at + 8, attributeValueKindColor);
            byte[] c = color(t);
            Heap.write(at + 12, c, 0, 6);
        } else if ((kinds & (attributeValueKindEnum | attributeValueKindCaseSensEnum)) != 0 && enumIndex(a, t) >= 0) {
            Heap.putInt(at + 8, a.caseSensitiveEnum ? attributeValueKindCaseSensEnum : attributeValueKindEnum);
            Heap.putInt(at + 12, enumIndex(a, t));
        } else {
            Heap.putInt(at + 8, attributeValueKindCharString);
            Heap.putInt(at + 12, cStr(blocks, v));
        }
    }

    /** @return the RGBColor of #rgb, #rrggbb or a basic color name, null if none */
    static byte[] color(String t) {
        String s = t.toLowerCase(Locale.ROOT);
        int rgb;
        if (s.matches("#[0-9a-f]{6}")) {
            rgb = Integer.parseInt(s.substring(1), 16);
        } else if (s.matches("#[0-9a-f]{3}")) {
            int r = Integer.parseInt(s.substring(1, 2), 16), g = Integer.parseInt(s.substring(2, 3), 16), b = Integer.parseInt(s.substring(3, 4), 16);
            rgb = (r * 17) << 16 | (g * 17) << 8 | b * 17;
        } else {
            Map<String, Integer> names = Map.of("black", 0, "white", 0xffffff, "red", 0xff0000, "green", 0x008000, "blue", 0x0000ff,
                    "yellow", 0xffff00, "cyan", 0x00ffff, "magenta", 0xff00ff, "gray", 0x808080);
            Integer v = names.get(s);
            if (v == null) {
                return null;
            }
            rgb = v;
        }
        byte[] c = new byte[6];
        Bytes.putShort(c, 0, ((rgb >> 16) & 0xff) * 0x101);
        Bytes.putShort(c, 2, ((rgb >> 8) & 0xff) * 0x101);
        Bytes.putShort(c, 4, (rgb & 0xff) * 0x101);
        return c;
    }

    private static int enumIndex(Attr a, String t) {
        for (int i = 0; i < a.enumValues.size(); i++) {
            String v = a.enumValues.get(i);
            if (a.caseSensitiveEnum ? v.equals(t) : v.equalsIgnoreCase(t)) {
                return i + 1;
            }
        }
        return -1;
    }

    /** @return the text of the source, from the offset to the limit */
    private static String text(Parser p, byte[] b) {
        int from = (int) Math.min(b.length, p.offset);
        int to = p.limit < 0 ? b.length : (int) Math.min(b.length, p.limit);
        int n = Math.max(0, to - from);
        // a BOM or an encoding declaration of Latin 1 aside, documents are UTF-8
        String head = new String(b, from, Math.min(n, 100), StandardCharsets.ISO_8859_1).toLowerCase(Locale.ROOT);
        if (n >= 3 && (b[from] & 0xff) == 0xef && (b[from + 1] & 0xff) == 0xbb && (b[from + 2] & 0xff) == 0xbf) {
            return new String(b, from + 3, n - 3, StandardCharsets.UTF_8);
        }
        if (head.contains("iso-8859-1") || head.contains("latin")) {
            return new String(b, from, n, StandardCharsets.ISO_8859_1);
        }
        return new String(b, from, n, StandardCharsets.UTF_8);
    }

    private static short parse(int ci, byte[] data, int flags, int[] document) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        p.errorLine = 0;
        p.errorDescription = "";
        Reader r = new Reader(text(p, data), flags, p);
        Node root;
        try {
            root = r.document();
        } catch (IOException | RuntimeException e) {
            if (p.errorDescription.isEmpty()) {
                p.errorDescription = String.valueOf(e.getMessage());
            }
            return xmlParseErr;
        }
        if ((flags & xmlParseFlagEventParseOnly) != 0 || document == null) {
            return noErr;
        }
        List<Integer> blocks = new ArrayList<>();
        try {
            int doc = Heap.newPtr(4 + ELEMENT);
            Heap.putInt(doc, doc);
            if (root != null) {
                element(p, root, doc + 4, blocks);
            }
            p.documents.put(doc, blocks);
            document[0] = doc;
            return noErr;
        } catch (IOException e) {
            for (int b : blocks) {
                Heap.free(b);
            }
            return xmlParseErr;
        }
    }

    public static short XMLParseFile(int ci, byte[] fsspec, int flags, int[] document) {
        String path = FileManager.specToPath(fsspec);
        if (path == null || !new File(path).exists()) {
            return fnfErr;
        }
        try {
            return parse(ci, Files.readAllBytes(new File(path).toPath()), flags, document);
        } catch (IOException e) {
            return ioErr;
        }
    }

    public static short XMLParseDataRef(int ci, int dataRef, int dataRefType, int flags, int[] document) {
        try {
            return parse(ci, GraphicsImportManager.dataRefBytes(dataRef, dataRefType), flags, document);
        } catch (IOException | RuntimeException e) {
            return -2014;
        }
    }

    public static short XMLParseDisposeXMLDoc(int ci, int document) {
        Parser p = parser(ci);
        if (p == null) {
            return badComponentInstance;
        }
        List<Integer> blocks = p.documents.remove(document);
        if (blocks == null) {
            return paramErr;
        }
        for (int b : blocks) {
            Heap.free(b);
        }
        Heap.free(document);
        return noErr;
    }
}
