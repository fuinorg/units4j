// CHECKSTYLE:OFF
package org.fuin.units4j;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

@Entity
@Table(name = "MY_TEST_TABLE")
@XmlRootElement(name = "my-test-class")
public class MyTestClass implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Min(1)
	@NotNull
	@XmlAttribute(name = "id")
	@Column(name = "ID", nullable = false)
	private Integer id;

	@NotNull
	@XmlAttribute(name = "name")
	@Column(name = "NAME", length = 50, nullable = false)
	private String name;

	public MyTestClass() {
		super();
	}

	public MyTestClass(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyTestClass other = (MyTestClass) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
// CHECKSTYLE:OFF
