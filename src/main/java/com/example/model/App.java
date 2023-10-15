package com.example.model;

import java.io.Serializable;
import java.lang.String;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "apps")
public class App extends TimeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 1

	@Column(name = "name", nullable = false)
	private String name; // 8

	// @Column(name = "create_at", nullable = false)
	// private Timestamp create_at; // 2

	// @Column(name = "update_at", nullable = false)
	// private Timestamp update_at; // 3

	// @ColumnDefault("'Unknown'")
	@Column(name = "url", nullable = false)

	private String url; // 9
	// columnDefinition = "VARCHAR(255) DEFAULT 'AAA'"

	@Column(name = "developer", nullable = false)
	private String developer; // 6

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description; // 5

	@Column(name = "image", nullable = false, columnDefinition = "TEXT")
	private String image; // 7

	@Column(name = "active", nullable = false)
	private Boolean active; // 4

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	// public void setcreate_at(String create_at) { // 2
	// this.create_at = create_at;
	// }

	// public Timestamp getcreate_at() {
	// return create_at;
	// }

	// public void setupdate_at(Timestamp update_at) { // 3
	// this.update_at = update_at;
	// }

	// public Timestamp getupdate_at() {
	// return update_at;
	// }

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
