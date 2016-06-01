package demo.json.model;

import com.google.gson.annotations.SerializedName;


/**
 * @author resul.avan@gmail.com
 *
 */
public class Author {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
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
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        String delimiter = " ";

        StringBuilder sb = new StringBuilder("Author: ");
        sb.append("id=").append(id).append(delimiter);
        sb.append("name=").append(name);

        return sb.toString();
    }

}
