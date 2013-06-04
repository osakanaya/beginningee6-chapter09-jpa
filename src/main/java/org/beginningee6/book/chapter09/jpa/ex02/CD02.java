package org.beginningee6.book.chapter09.jpa.ex02;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "cd_ex02")
@NamedQuery(name = "CD02.findAllCDs", query = "SELECT c FROM CD02 c")
public class CD02 implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private Float price;
    @Column(length = 2000)
    private String description;
    @Lob
    private byte[] cover;
    private String musicCompany;
    private Integer numberOfCDs;
    private Float totalDuration;
    private String gender;

    public CD02() {}

	public CD02(String title, Float price, String description, byte[] cover,
			String musicCompany, Integer numberOfCDs, Float totalDuration,
			String gender) {
		this.title = title;
		this.price = price;
		this.description = description;
		this.cover = cover;
		this.musicCompany = musicCompany;
		this.numberOfCDs = numberOfCDs;
		this.totalDuration = totalDuration;
		this.gender = gender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getCover() {
		return cover;
	}

	public void setCover(byte[] cover) {
		this.cover = cover;
	}

	public String getMusicCompany() {
		return musicCompany;
	}

	public void setMusicCompany(String musicCompany) {
		this.musicCompany = musicCompany;
	}

	public Integer getNumberOfCDs() {
		return numberOfCDs;
	}

	public void setNumberOfCDs(Integer numberOfCDs) {
		this.numberOfCDs = numberOfCDs;
	}

	public Float getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Float totalDuration) {
		this.totalDuration = totalDuration;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(cover);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((musicCompany == null) ? 0 : musicCompany.hashCode());
		result = prime * result
				+ ((numberOfCDs == null) ? 0 : numberOfCDs.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((totalDuration == null) ? 0 : totalDuration.hashCode());
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
		CD02 other = (CD02) obj;
		if (!Arrays.equals(cover, other.cover))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (musicCompany == null) {
			if (other.musicCompany != null)
				return false;
		} else if (!musicCompany.equals(other.musicCompany))
			return false;
		if (numberOfCDs == null) {
			if (other.numberOfCDs != null)
				return false;
		} else if (!numberOfCDs.equals(other.numberOfCDs))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (totalDuration == null) {
			if (other.totalDuration != null)
				return false;
		} else if (!totalDuration.equals(other.totalDuration))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CD02 [id=" + id + ", title=" + title + ", price=" + price
				+ ", description=" + description + ", cover="
				+ Arrays.toString(cover) + ", musicCompany=" + musicCompany
				+ ", numberOfCDs=" + numberOfCDs + ", totalDuration="
				+ totalDuration + ", gender=" + gender + "]";
	}

}
