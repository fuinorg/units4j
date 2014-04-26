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

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
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
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;

/**
 * Utilities for use in tests.
 */
public final class Units4JUtils {

	/** Standard XML prefix with UTF-8 encoding. */
	public static final String XML_PREFIX = "<?xml version=\"1.0\" "
			+ "encoding=\"UTF-8\" standalone=\"yes\"?>";

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
	 */
	public static byte[] serialize(final Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				final ObjectOutputStream out = new ObjectOutputStream(baos);
				out.writeObject(obj);
				return baos.toByteArray();
			} finally {
				baos.close();
			}
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
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
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(final byte[] data) {
		if (data == null) {
			return null;
		}
		try {
			final ByteArrayInputStream bais = new ByteArrayInputStream(data);
			try {
				final ObjectInputStream in = new ObjectInputStream(bais);
				return (T) in.readObject();
			} finally {
				bais.close();
			}
		} catch (final ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
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
	 */
	public static <T> String marshal(final T data,
			@NotNull final Class<?>... classesToBeBound) {
		if (data == null) {
			return null;
		}
		try {
			final JAXBContext ctx = JAXBContext.newInstance(classesToBeBound);
			return marshal(ctx, data);
		} catch (final JAXBException ex) {
			throw new RuntimeException("Error marshalling test data", ex);
		}
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
	 */
	public static <T> String marshal(@NotNull final JAXBContext ctx,
			final T data) {
		if (data == null) {
			return null;
		}
		try {
			final Marshaller marshaller = ctx.createMarshaller();
			final StringWriter writer = new StringWriter();
			marshaller.marshal(data, writer);
			return writer.toString();
		} catch (final JAXBException ex) {
			throw new RuntimeException("Error marshalling test data", ex);
		}
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
	 */
	public static <T> T unmarshal(final String xmlData,
			@NotNull final Class<?>... classesToBeBound) {
		return unmarshal(xmlData, null, classesToBeBound);
	}

	/**
	 * Unmarshals the given data. A <code>null</code> XML data argument returns
	 * <code>null</code>.
	 * 
	 * @param xmlData
	 *            XML data or <code>null</code>.
	 * @param adapters
	 *            Adapters to associate with the JAXB context or
	 *            <code>null</code>.
	 * @param classesToBeBound
	 *            List of java classes to be recognized by the
	 *            {@link JAXBContext}.
	 * 
	 * @return Data or <code>null</code>.
	 * 
	 * @param <T>
	 *            Type of the expected data.
	 */
	public static <T> T unmarshal(final String xmlData,
			final XmlAdapter<?, ?>[] adapters,
			@NotNull final Class<?>... classesToBeBound) {
		if (xmlData == null) {
			return null;
		}
		try {
			final JAXBContext ctx = JAXBContext.newInstance(classesToBeBound);
			return unmarshal(ctx, xmlData, adapters);
		} catch (final JAXBException ex) {
			throw new RuntimeException("Error unmarshalling test data", ex);
		}
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
	 *            Adapters to associate with the JAXB context or
	 *            <code>null</code>.
	 * 
	 * @return Data or <code>null</code>.
	 * 
	 * @param <T>
	 *            Type of the expected data.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(@NotNull final JAXBContext ctx,
			final String xmlData, final XmlAdapter<?, ?>[] adapters) {
		if (xmlData == null) {
			return null;
		}
		try {
			final Unmarshaller unmarshaller = ctx.createUnmarshaller();
			if (adapters != null) {
				for (XmlAdapter<?, ?> adapter : adapters) {
					unmarshaller.setAdapter(adapter);
				}
			}
			unmarshaller.setEventHandler(new ValidationEventHandler() {
				@Override
				public boolean handleEvent(final ValidationEvent event) {
					if (event.getSeverity() > 0) {
						if (event.getLinkedException() == null) {
							throw new RuntimeException(
									"Error unmarshalling the data: "
											+ event.getMessage());
						}
						throw new RuntimeException(
								"Error unmarshalling the data", event
										.getLinkedException());
					}
					return true;
				}
			});
			return (T) unmarshaller.unmarshal(new StringReader(xmlData));
		} catch (final JAXBException ex) {
			throw new RuntimeException("Error unmarshalling test data", ex);
		}
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
	public static String replaceXmlAttr(final String xml,
			final KV... keyValues) {

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
