/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.units4j;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;
import org.fuin.utils4j.JaxbUtils;
import org.fuin.utils4j.Utils4J;
import org.fuin.utils4j.fileprocessor.FileHandler;
import org.fuin.utils4j.fileprocessor.FileHandlerResult;
import org.fuin.utils4j.fileprocessor.FileProcessor;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.Indexer;

/**
 * Utilities for use in tests.
 */
public final class Units4JUtils {

    /** Standard XML prefix with UTF-8 encoding. */
    public static final String XML_PREFIX = JaxbUtils.XML_PREFIX;

    private Units4JUtils() {
        throw new UnsupportedOperationException("It's not allowed to create an instance of a utility class");
    }

    /**
     * Serializes the given object. A <code>null</code> argument returns <code>null</code>.
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
     * Deserializes a byte array to an object. A <code>null</code> argument returns <code>null</code>.
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
     * Marshals the given data. A <code>null</code> data argument returns <code>null</code>.
     * 
     * @param data
     *            Data to serialize or <code>null</code>.
     * @param classesToBeBound
     *            List of java classes to be recognized by the {@link JAXBContext}.
     * 
     * @return XML data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the data.
     * 
     * @deprecated Use {@link JaxbUtils#marshal(Object, Class...)}
     */
    @Deprecated
    public static <T> String marshal(final T data, @NotNull final Class<?>... classesToBeBound) {
        return JaxbUtils.marshal(data, classesToBeBound);
    }

    /**
     * Marshals the given data. A <code>null</code> data argument returns <code>null</code>.
     * 
     * @param data
     *            Data to serialize or <code>null</code>.
     * @param adapters
     *            Adapters to associate with the marshaller or <code>null</code> .
     * @param classesToBeBound
     *            List of java classes to be recognized by the {@link JAXBContext}.
     * 
     * @return XML data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the data.
     * 
     * @deprecated Use {@link JaxbUtils#marshal(Object, XmlAdapter[], Class...)}
     */
    @Deprecated
    public static <T> String marshal(final T data, final XmlAdapter<?, ?>[] adapters, @NotNull final Class<?>... classesToBeBound) {
        return JaxbUtils.marshal(data, adapters, classesToBeBound);
    }

    /**
     * Marshals the given data using a given context. A <code>null</code> data argument returns <code>null</code>.
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
    public static <T> String marshal(@NotNull final JAXBContext ctx, final T data) {
        return JaxbUtils.marshal(marshal(ctx, data));
    }

    /**
     * Marshals the given data using a given context. A <code>null</code> data argument returns <code>null</code>.
     * 
     * @param ctx
     *            Context to use.
     * @param data
     *            Data to serialize or <code>null</code>.
     * @param adapters
     *            Adapters to associate with the marshaller or <code>null</code> .
     * 
     * @return XML data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the data.
     * 
     * @deprecated Use {@link JaxbUtils#marshal(JAXBContext, Object, XmlAdapter[])}
     */
    @Deprecated
    public static <T> String marshal(@NotNull final JAXBContext ctx, final T data, final XmlAdapter<?, ?>[] adapters) {
        return JaxbUtils.marshal(ctx, data, adapters);
    }

    /**
     * Unmarshals the given data. A <code>null</code> XML data argument returns <code>null</code>.
     * 
     * @param xmlData
     *            XML data or <code>null</code>.
     * @param classesToBeBound
     *            List of java classes to be recognized by the {@link JAXBContext}.
     * 
     * @return Data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the expected data.
     * 
     * @deprecated Use {@link JaxbUtils#unmarshal(String, Class...)}
     */
    @Deprecated
    public static <T> T unmarshal(final String xmlData, @NotNull final Class<?>... classesToBeBound) {
        return JaxbUtils.unmarshal(xmlData, classesToBeBound);
    }

    /**
     * Unmarshals the given data. A <code>null</code> XML data argument returns <code>null</code>.
     * 
     * @param xmlData
     *            XML data or <code>null</code>.
     * @param adapters
     *            Adapters to associate with the unmarshaller or <code>null</code>.
     * @param classesToBeBound
     *            List of java classes to be recognized by the {@link JAXBContext}.
     * 
     * @return Data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the expected data.
     * 
     * @deprecated Use {@link JaxbUtils#unmarshal(String, XmlAdapter[], Class...)}
     */
    @Deprecated
    public static <T> T unmarshal(final String xmlData, final XmlAdapter<?, ?>[] adapters, @NotNull final Class<?>... classesToBeBound) {
        return JaxbUtils.unmarshal(xmlData, adapters, classesToBeBound);
    }

    /**
     * Unmarshals the given data using a given context. A <code>null</code> XML data argument returns <code>null</code>.
     * 
     * @param ctx
     *            Context to use.
     * @param xmlData
     *            XML data or <code>null</code>.
     * @param adapters
     *            Adapters to associate with the unmarshaller or <code>null</code>.
     * 
     * @return Data or <code>null</code>.
     * 
     * @param <T>
     *            Type of the expected data.
     * 
     * @deprecated Use {@link JaxbUtils#unmarshal(JAXBContext, String, XmlAdapter[])}
     */
    @Deprecated
    public static <T> T unmarshal(@NotNull final JAXBContext ctx, final String xmlData, final XmlAdapter<?, ?>[] adapters) {
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
    public static void setPrivateField(final Object obj, final String name, final Object value) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (final Exception ex) {
            throw new RuntimeException("Couldn't set field '" + name + "' in class '" + obj.getClass() + "'", ex);
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
    public static void assertCauseMessage(final Throwable ex, final String expectedMessage) {
        assertThat(ex.getCause()).isNotNull();
        assertThat(ex.getCause().getMessage()).isEqualTo(expectedMessage);
    }

    /**
     * Verifies that the cause of a cause of an exception contains an expected message.
     * 
     * @param ex
     *            Exception with the cause/cause to check,
     * @param expectedMessage
     *            Message of the cause/cause.
     */
    public static void assertCauseCauseMessage(final Throwable ex, final String expectedMessage) {
        assertThat(ex.getCause()).isNotNull();
        assertThat(ex.getCause().getCause()).isNotNull();
        assertThat(ex.getCause().getCause().getMessage()).isEqualTo(expectedMessage);
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
    public static Set<ConstraintViolation<Object>> validate(final Object obj, final Class<?>... scopes) {
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
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    /**
     * Returns a list of files with a given extension.
     * 
     * @param dir
     *            Directory and it's subdirectories to inspect.
     * @param extension
     *            Extension to find (like "txt" or "class") without the ".".
     * 
     * @return List fo files.
     */
    public static List<File> findFilesRecursive(final File dir, final String extension) {
        final String dotExtension = "." + extension;
        final List<File> files = new ArrayList<>();
        final FileProcessor fileProcessor = new FileProcessor(new FileHandler() {
            @Override
            public final FileHandlerResult handleFile(final File file) {
                // Directory
                if (file.isDirectory()) {
                    return FileHandlerResult.CONTINUE;
                }
                // File
                final String name = file.getName();
                if (name.endsWith(dotExtension)) {
                    files.add(file);
                }
                return FileHandlerResult.CONTINUE;
            }
        });

        fileProcessor.process(dir);
        return files;
    }

    /**
     * Returns a list of class files in the directory and it's sub directories.
     * 
     * @param dir
     *            Directory to inspect.
     * 
     * @return List of class files.
     */
    public static List<File> findAllClasses(final File dir) {
        return findFilesRecursive(dir, "class");
    }

    /**
     * Creates an index for all class files in the given list.
     * 
     * @param classFiles
     *            List of ".class" files.
     * 
     * @return Index.
     */
    public static final Index indexAllClasses(final List<File> classFiles) {
        final Indexer indexer = new Indexer();
        indexAllClasses(indexer, classFiles);
        return indexer.complete();
    }

    /**
     * Index all class files in the given list.
     * 
     * @param indexer
     *            Indexer to use.
     * @param classFiles
     *            List of ".class" files.
     */
    public static final void indexAllClasses(final Indexer indexer, final List<File> classFiles) {
        classFiles.forEach(file -> {
            try {
                final InputStream in = new FileInputStream(file);
                try {
                    indexer.index(in);
                } finally {
                    in.close();
                }
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Loads a class and creates a Jandex class information for it. Uses the class loader of this utility class.
     * 
     * @param clasz
     *            Class to load.
     * 
     * @return Jandex class information.
     */
    public static ClassInfo classInfo(final Class<?> clasz) {
        return classInfo(Units4JUtils.class.getClassLoader(), clasz);
    }

    /**
     * Loads a class and creates a Jandex class information for it.
     * 
     * @param cl
     *            Class loader to use.
     * @param clasz
     *            Class to load.
     * 
     * @return Jandex class information.
     */
    public static ClassInfo classInfo(final ClassLoader cl, final Class<?> clasz) {
        return classInfo(cl, clasz.getName());
    }

    /**
     * Loads a class by it's name and creates a Jandex class information for it. Uses the class loader of this utility class.
     * 
     * @param className
     *            Full qualified name of the c
     * 
     * @return Jandex class information.
     */
    public static ClassInfo classInfo(final String className) {
        return classInfo(Units4JUtils.class.getClassLoader(), className);
    }

    /**
     * Loads a class by it's name and creates a Jandex class information for it.
     * 
     * @param cl
     *            Class loader to use.
     * @param className
     *            Full qualified name of the class.
     * 
     * @return Jandex class information.
     */
    public static ClassInfo classInfo(final ClassLoader cl, final String className) {
        final Index index = index(cl, className);
        return index.getClassByName(DotName.createSimple(className));
    }

    /**
     * Returns a Jandex index for a class by it's name.
     * 
     * @param cl
     *            Class loader to use.
     * @param className
     *            Full qualified name of the class.
     * 
     * @return Jandex index.
     */
    public static Index index(final ClassLoader cl, final String className) {
        final Indexer indexer = new Indexer();
        index(indexer, cl, className);
        return indexer.complete();
    }

    /**
     * Indexes a class by it's name.
     * 
     * @param indexer
     *            Indexer to use.
     * @param cl
     *            Class loader to use.
     * @param className
     *            Full qualified name of the class.
     */
    public static void index(final Indexer indexer, final ClassLoader cl, final String className) {
        final InputStream stream = cl.getResourceAsStream(className.replace('.', '/') + ".class");
        try {
            indexer.index(stream);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
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
                    final String repl = xml.substring(pa, pa + tag.length()) + kv.getValue() + "\"";
                    replacementList.add(repl);
                }
                pa = xml.indexOf(tag, s);
            }
        }

        final String[] searchArray = searchList.toArray(new String[searchList.size()]);
        final String[] replacementArray = replacementList.toArray(new String[replacementList.size()]);
        return StringUtils.replaceEachRepeatedly(xml, searchArray, replacementArray);
    }

    /**
     * Determines if an object has an expected type in a null-safe way.
     * 
     * @param expectedClass
     *            Expected type.
     * @param obj
     *            Object to test.
     * 
     * @return TRUE if the object is exactly of the same class, else FALSE.
     */
    public static boolean isExpectedType(final Class<?> expectedClass, final Object obj) {
        final Class<?> actualClass;
        if (obj == null) {
            actualClass = null;
        } else {
            actualClass = obj.getClass();
        }
        return Objects.equals(expectedClass, actualClass);
    }

    /**
     * Determines if an exception has an expected type and message in a null-safe way.
     * 
     * @param expectedClass
     *            Expected exception type.
     * @param expectedMessage
     *            Expected message.
     * @param ex
     *            Exception to test.
     * 
     * @return TRUE if the object is exactly of the same class and has the same message, else FALSE.
     */
    public static boolean isExpectedException(final Class<? extends Exception> expectedClass,
            final String expectedMessage, final Exception ex) {
        if (!isExpectedType(expectedClass, ex)) {
            return false;
        }
        if ((expectedClass != null) && (expectedMessage != null) && (ex != null)) {
            return Objects.equals(expectedMessage, ex.getMessage());
        }
        return true;
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
            if (key == null) { // NOSONAR
                throw new IllegalArgumentException("Key cannot be null");
            }
            if (value == null) { // NOSONAR
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
