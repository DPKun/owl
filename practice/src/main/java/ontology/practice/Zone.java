package ontology.practice;

public class Zone {

	private String Name;
	private String ID;

	public Zone(String name, String iD) {
		super();
		Name = name;
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	@Override
	public String toString() {
		return "Zone [Name=" + Name + ", ID=" + ID + "]";
	}

}
