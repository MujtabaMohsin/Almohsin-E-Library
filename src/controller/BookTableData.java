package controller;


public class BookTableData {

	int number;
	String name;
	String author;

	public BookTableData() {
		this.number = 0;
		this.name = "";
		this.author = "";

	}

	public BookTableData(int number, String name, String author) {
		this.number = number;
		this.name = name;
		this.author = author;

	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}