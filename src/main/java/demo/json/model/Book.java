/**
 * 
 */
package demo.json.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author resulav
 *
 */
public class Book {

	@SerializedName("id")
	public String id;
	@SerializedName("name")
	public String name;

	@SerializedName("author")
	public String author;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	public String toString() {

		String delimiter = " ";

		StringBuilder sb = new StringBuilder("Book: ");
		sb.append("id=").append(id).append(delimiter);
		sb.append("name=").append(name).append(delimiter);
		sb.append("author=");
		if (author != null) {
			sb.append(author.toString());
		}

		return sb.toString();
	}

}
