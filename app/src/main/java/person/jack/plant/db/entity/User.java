package person.jack.plant.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable(tableName = "user_record")
public class User 
{
	@DatabaseField(columnName = "id",generatedId = true)
	private int id;
	@DatabaseField(columnName = "image_path")
	private String imgaePath;
	@DatabaseField(columnName = "name")
	private String name;
	@DatabaseField(columnName = "password")
	private String pwd;

	@DatabaseField(columnName = "phone")
	private String phone;

	public User(int id, String imgaePath, String name, String pwd, String phone) {
		this.id = id;
		this.imgaePath = imgaePath;
		this.name = name;
		this.pwd = pwd;
		this.phone = phone;
	}

	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImgaePath() {
		return imgaePath;
	}

	public void setImgaePath(String imgaePath) {
		this.imgaePath = imgaePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
