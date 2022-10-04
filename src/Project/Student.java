package Project;

public class Student {
	String id;
	String name;
	int javaScore;
	int pythonScore;
	int phpScore;
	int linuxScore;
	
	public Student(String id, String name, int javaScore, int pythonScore, int phpScore, int linuxScore) {
		super();
		this.id = id;
		this.name = name;
		this.javaScore = javaScore;
		this.pythonScore = pythonScore;
		this.phpScore = phpScore;
		this.linuxScore = linuxScore;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getJavaScore() {
		return javaScore;
	}

	public int getPythonScore() {
		return pythonScore;
	}
	
	public int getPhpScore() {
		return phpScore;
	}

	public int getLinuxScore() {
		return linuxScore;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", javaScore=" + javaScore + ", pythonScore=" + pythonScore
				+ ", phpScore=" + phpScore + ", linuxScore=" + linuxScore + "]";
	}


}
