/**************************************************************************
 * copyright file="ExtendedProperty.java" company="Microsoft"
 *     Copyright (c) Microsoft Corporation.  All rights reserved.
 * 
 * Defines the ExtendedProperty.java.
 **************************************************************************/
package microsoft.exchange.webservices.data;

import java.util.ArrayList;

import com.innovaturelabs.xml.stream.XMLStreamException;

/***
 * Represents an extended property.
 * 
 */
public final class ExtendedProperty extends ComplexProperty {

	/** The property definition. */
	private ExtendedPropertyDefinition propertyDefinition;

	/** The value. */
	private Object value;

	/**
	 * Initializes a new instance.
	 */
	protected ExtendedProperty() {
	}

	/**
	 * * Initializes a new instance.
	 * 
	 * @param propertyDefinition
	 *            The definition of the extended property.
	 * @throws Exception
	 *             the exception
	 */
	protected ExtendedProperty(ExtendedPropertyDefinition propertyDefinition)
			throws Exception {
		this();
		EwsUtilities.validateParam(propertyDefinition, "propertyDefinition");
		this.propertyDefinition = propertyDefinition;
	}

	/**
	 * * Tries to read element from XML.
	 * 
	 * @param reader
	 *            The reader.
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	@Override
	protected boolean tryReadElementFromXml(EwsServiceXmlReader reader)
			throws Exception {

		if (reader.getLocalName().equals(XmlElementNames.ExtendedFieldURI)) {
			this.propertyDefinition = new ExtendedPropertyDefinition();
			this.propertyDefinition.loadFromXml(reader);
			return true;
		} else if (reader.getLocalName().equals(XmlElementNames.Value)) {
			EwsUtilities.EwsAssert(this.getPropertyDefinition() != null,
					"ExtendedProperty.TryReadElementFromXml",
					"PropertyDefintion is missing");
			String stringValue = reader.readElementValue();
			this.value = MapiTypeConverter.convertToValue(this
					.getPropertyDefinition().getMapiType(), stringValue);
			return true;
		} else if (reader.getLocalName().equals(XmlElementNames.Values)) {
			EwsUtilities.EwsAssert(this.getPropertyDefinition() != null,
					"ExtendedProperty.TryReadElementFromXml",
					"PropertyDefintion is missing");

			StringList stringList = new StringList(XmlElementNames.Value);
			stringList.loadFromXml(reader, reader.getLocalName());
			this.value = MapiTypeConverter.convertToValue(this
					.getPropertyDefinition().getMapiType(), stringList
					.iterator());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Writes elements to XML.
	 * 
	 * @param writer
	 *            the writer
	 * @throws ServiceXmlSerializationException
	 *             the service xml serialization exception
	 * @throws XMLStreamException
	 *             the xML stream exception
	 */
	@Override
	protected void writeElementsToXml(EwsServiceXmlWriter writer)
			throws ServiceXmlSerializationException, XMLStreamException {
		this.getPropertyDefinition().writeToXml(writer);

		if (MapiTypeConverter.isArrayType(this.getPropertyDefinition()
				.getMapiType())) {
			ArrayList array = (ArrayList) this.getValue();
			writer
					.writeStartElement(XmlNamespace.Types,
							XmlElementNames.Values);
			for (int index = 0; index <= array.size(); index++) {
				writer.writeElementValue(XmlNamespace.Types,
						XmlElementNames.Value, MapiTypeConverter
								.convertToString(this.getPropertyDefinition()
										.getMapiType(), array.get(index)));
			}
			writer.writeEndElement();
		} else {
			writer.writeElementValue(XmlNamespace.Types, XmlElementNames.Value,
					MapiTypeConverter.convertToString(this
							.getPropertyDefinition().getMapiType(), this
							.getValue()));
		}
	}

	/***
	 * Gets the definition of the extended property.
	 * 
	 * @return The definition of the extended property.
	 */
	public ExtendedPropertyDefinition getPropertyDefinition() {
		return this.propertyDefinition;
	}

	/**
	 * * Gets the value of the extended property.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * * Sets the value of the extended property.
	 * 
	 * @param val
	 *            value of the extended property
	 * @throws Exception
	 *             the exception
	 */
	public void setValue(Object val) throws Exception {
		EwsUtilities.validateParam(val, "value");
		if (this.canSetFieldValue(this.value, MapiTypeConverter.changeType(this
				.getPropertyDefinition().getMapiType(), val))) {
			this.value = MapiTypeConverter.changeType(this
					.getPropertyDefinition().getMapiType(), val);
			this.changed();
		}
	}

	/**
	 * Gets the string value.
	 * 
	 * @return String
	 */
	private String getStringValue() {
		if (MapiTypeConverter.isArrayType(this.getPropertyDefinition()
				.getMapiType())) {
			ArrayList array = (ArrayList) this.getValue();
			if (array == null) {
				return null;
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				for (int index = 0; index <= array.size(); index++) {
					sb.append(MapiTypeConverter.convertToString(this
							.getPropertyDefinition().getMapiType(), array
							.get(index)));
					sb.append(",");
				}
				sb.append("]");

				return sb.toString();
			}
		} else {
			return MapiTypeConverter.convertToString(this
					.getPropertyDefinition().getMapiType(), this.getValue());
		}
	}

	/**
	 * Determines whether the specified <see cref="T:System.Object"/> is equal
	 * to the current <see cref="T:System.Object"/> true if the specified <see
	 * cref="T:System.Object"/> is equal to the current <see
	 * cref="T:System.Object"/>
	 * 
	 * @param obj
	 *            the obj
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ExtendedProperty) {
			ExtendedProperty other = (ExtendedProperty) obj;
			if (other.getPropertyDefinition().equals(
					this.getPropertyDefinition())) {
				return this.getStringValue().equals(other.getStringValue());
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Serves as a hash function for a particular type.
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		String printableName = this.getPropertyDefinition() != null ? this
				.getPropertyDefinition().getPrintableName() : "";
		String stringVal = this.getStringValue();
		return (printableName + stringVal).hashCode();
	}
}
