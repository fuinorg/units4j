/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.units4j;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;
import org.fuin.utils4j.JaxbUtils;
import org.fuin.utils4j.Utils4J;

/**
 * Utilities for use in tests.
 */
public final class Units4JUtils {

    /** Standard XML prefix with UTF-8 encoding. */
    public static final String XML_PREFIX = JaxbUtils.XML_PREFIX;

    private Units4JUtils() {
        throw new UnsupportedOperationException(
                "It's not allowed to create an instance of a utility class");
    }

    /**
     * Serializes the given object. A <code>null</code> argument returns
     * <code>null</code>.
     * 
     * @param obj
     *            Object to serialize.
     * 
     * @return Serialized object.
     * 
     * @deprecated Use {@link Utils4J#serialize(Object)}
     */
    @Deprecated
    public static byte[] serialize(final Object obj) {
        return Utils4J.serialize(obj);
    }

    /**
     * Deserializes a byte array to an object. A <code>null</code> argument
     * returns <code>null</code>.
     * 
     * @param data
     *            Byte array to deserialize.
     * 
     * @return Object created from data.
     * 
     * @param <T>
     *            Type of returned data.
     * 
     * @deprecated Use {@link Utils4J#deserialize(byte[])}
     */
    @Deprecated
    public static <T> T deserialize(final byte[] data) {
        return Utils4J.deserialize(data);
    }

    /**
     * Marshals the given data. A <code>null</code> data argument returns
     * <code>null</code>.
     * 
     * @param data
     *            Data to serialize or <code>null</code>.
     * @param classesToBeBound
     *            List of java classes to be recognized by the
     *            {@link JAXBContext}.
     * 
     * @return XML data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the data.
     * 
     * @deprecated Use {@link JaxbUtils#marshal(Object, Class...)}
     */
    @Deprecated
    public static <T> String marshal(final T data,
            @NotNull final Class<?>... classesToBeBound) {
        return JaxbUtils.marshal(data, classesToBeBound);
    }

    /**
     * Marshals the given data. A <code>null</code> data argument returns
     * <code>null</code>.
     * 
     * @param data
     *            Data to serialize or <code>null</code>.
     * @param adapters
     *            Adapters to associate with the marshaller or <code>null</code>
     *            .
     * @param classesToBeBound
     *            List of java classes to be recognized by the
     *            {@link JAXBContext}.
     * 
     * @return XML data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the data.
     * 
     * @deprecated Use {@link JaxbUtils#marshal(Object, XmlAdapter[], Class...)}
     */
    @Deprecated
    public static <T> String marshal(final T data,
            final XmlAdapter<?, ?>[] adapters,
            @NotNull final Class<?>... classesToBeBound) {
        return JaxbUtils.marshal(data, adapters, classesToBeBound);
    }

    /**
     * Marshals the given data using a given context. A <code>null</code> data
     * argument returns <code>null</code>.
     * 
     * @param ctx
     *            Context to use.
     * @param data
     *            Data to serialize or <code>null</code>.
     * 
     * @return XML data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the data.
     * 
     * @deprecated Use {@link JaxbUtils#marshal(JAXBContext, Object)}
     */
    @Deprecated
    public static <T> String marshal(@NotNull final JAXBContext ctx,
            final T data) {
        return JaxbUtils.marshal(marshal(ctx, data));
    }

    /**
     * Marshals the given data using a given context. A <code>null</code> data
     * argument returns <code>null</code>.
     * 
     * @param ctx
     *            Context to use.
     * @param data
     *            Data to serialize or <code>null</code>.
     * @param adapters
     *            Adapters to associate with the marshaller or <code>null</code>
     *            .
     * 
     * @return XML data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the data.
     * 
     * @deprecated Use
     *             {@link JaxbUtils#marshal(JAXBContext, Object, XmlAdapter[])}
     */
    @Deprecated
    public static <T> String marshal(@NotNull final JAXBContext ctx,
            final T data, final XmlAdapter<?, ?>[] adapters) {
        return JaxbUtils.marshal(ctx, data, adapters);
    }

    /**
     * Unmarshals the given data. A <code>null</code> XML data argument returns
     * <code>null</code>.
     * 
     * @param xmlData
     *            XML data or <code>null</code>.
     * @param classesToBeBound
     *            List of java classes to be recognized by the
     *            {@link JAXBContext}.
     * 
     * @return Data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the expected data.
     * 
     * @deprecated Use {@link JaxbUtils#unmarshal(String, Class...)}
     */
    @Deprecated
    public static <T> T unmarshal(final String xmlData,
            @NotNull final Class<?>... classesToBeBound) {
        return JaxbUtils.unmarshal(xmlData, classesToBeBound);
    }

    /**
     * Unmarshals the given data. A <code>null</code> XML data argument returns
     * <code>null</code>.
     * 
     * @param xmlData
     *            XML data or <code>null</code>.
     * @param adapters
     *            Adapters to associate with the unmarshaller or
     *            <code>null</code>.
     * @param classesToBeBound
     *            List of java classes to be recognized by the
     *            {@link JAXBContext}.
     * 
     * @return Data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the expected data.
     * 
     * @deprecated Use
     *             {@link JaxbUtils#unmarshal(String, XmlAdapter[], Class...)}
     */
    @Deprecated
    public static <T> T unmarshal(final String xmlData,
            final XmlAdapter<?, ?>[] adapters,
            @NotNull final Class<?>... classesToBeBound) {
        return JaxbUtils.unmarshal(xmlData, adapters, classesToBeBound);
    }

    /**
     * Unmarshals the given data using a given context. A <code>null</code> XML
     * data argument returns <code>null</code>.
     * 
     * @param ctx
     *            Context to use.
     * @param xmlData
     *            XML data or <code>null</code>.
     * @param adapters
     *            Adapters to associate with the unmarshaller or
     *            <code>null</code>.
     * 
     * @return Data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the expected data.
     * 
     * @deprecated Use
     *             {@link JaxbUtils#unmarshal(JAXBContext, String, XmlAdapter[])}
     */
    @Deprecated
    public static <T> T unmarshal(@NotNull final JAXBContext ctx,
            final String xmlData, final XmlAdapter<?, ?>[] adapters) {
        return JaxbUtils.unmarshal(ctx, xmlData, adapters);
    }

    /**
     * Sets a private field in an object by using reflection.
     * 
     * @param obj
     *            Object with the attribute to set.
     * @param name
     *            Name of the attribute to set.
     * @param value
     *            Value to set for the attribute.
     */
    public static void setPrivateField(final Object obj, final String name,
            final Object value) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (final Exception ex) {
            throw new RuntimeException("Couldn't set field '" + name
                    + "' in class '" + obj.getClass() + "'", ex);
        }
    }

    /**
     * Verifies that the cause of an exception contains an expected message.
     * 
     * @param ex
     *            Exception with the cause to check,
     * @param expectedMessage
     *            Message of the cause.
     */
    public static void assertCauseMessage(final Throwable ex,
            final String expectedMessage) {
        assertThat(ex.getCause()).isNotNull();
        assertThat(ex.getCause().getMessage()).isEqualTo(expectedMessage);
    }

    /**
     * Verifies that the cause of a cause of an exception contains an expected
     * message.
     * 
     * @param ex
     *            Exception with the cause/cause to check,
     * @param expectedMessage
     *            Message of the cause/cause.
     */
    public static void assertCauseCauseMessage(final Throwable ex,
            final String expectedMessage) {
        assertThat(ex.getCause()).isNotNull();
        assertThat(ex.getCause().getCause()).isNotNull();
        assertThat(ex.getCause().getCause().getMessage()).isEqualTo(
                expectedMessage);
    }

    /**
     * Validates the given object by using a newly created validator.
     * 
     * @param obj
     *            Object to validate using the given scopes.
     * @param scopes
     *            Scopes or <code>null</code> for the default scope.
     * 
     * @return Constraint violations.
     */
    public static Set<ConstraintViolation<Object>> validate(final Object obj,
            final Class<?>... scopes) {
        if (scopes == null) {
            return validator().validate(obj, Default.class);
        }
        return validator().validate(obj, scopes);
    }

    /**
     * Convenience method that creates a validator using the default factory.
     * 
     * @return New validator instance.
     */
    public static Validator validator() {
        final ValidatorFactory factory = Validation
                .buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    /**
     * Replaces the content of one or more XML attributes.
     * 
     * @param xml
     *            Xml with content to replace.
     * @param keyValues
     *            Attribute name and new value.
     * 
     * @return Replaced content.
     */
    public static String replaceXmlAttr(final String xml, final KV... keyValues) {

        final List<String> searchList = new ArrayList<String>();
        final List<String> replacementList = new ArrayList<String>();

        for (final KV kv : keyValues) {
            final String tag = kv.getKey() + "=\"";
            int pa = xml.indexOf(tag);
            while (pa > -1) {
                final int s = pa + tag.length();
                final int pe = xml.indexOf("\"", s);
                if (pe > -1) {
                    final String str = xml.substring(pa, pe + 1);
                    searchList.add(str);
                    final String repl = xml.substring(pa, pa + tag.length())
                            + kv.getValue() + "\"";
                    replacementList.add(repl);
                }
                pa = xml.indexOf(tag, s);
            }
        }

        final String[] searchArray = searchList.toArray(new String[searchList
                .size()]);
        final String[] replacementArray = replacementList
                .toArray(new String[replacementList.size()]);
        return StringUtils.replaceEachRepeatedly(xml, searchArray,
                replacementArray);
    }

    /**
     * Represents a key and a value.
     */
    public static final class KV {

        private final String key;

        private final String value;

        /**
         * Constructor with key and value.
         * 
         * @param key
         *            Key.
         * @param value
         *            Value.
         */
        public KV(@NotNull final String key, @NotNull final String value) {
            super();
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }
            if (value == null) {
                throw new IllegalArgumentException("Value cannot be null");
            }
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key.
         * 
         * @return Key.
         */
        public final String getKey() {
            return key;
        }

        /**
         * Returns the value.
         * 
         * @return value.
         */
        public final String getValue() {
            return value;
        }

    }

}
