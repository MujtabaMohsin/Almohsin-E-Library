package controller;

import database.DatabaseConnection;
import database.RetrieveData;
import database.initializeDB;
import javafx.scene.text.Font;
import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class HomeController implements Initializable {

	// variables

	public static String db_directory;
	public static String book_folder_directory;

	public static boolean isThereDB = true;
	public static boolean isdbError = false;

	ArrayList<ArrayList<String>> categories_data = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> category_books_data = new ArrayList<ArrayList<String>>();
	ArrayList<String> book_info = new ArrayList<String>();
	ArrayList<ArrayList<String>> Mybooks_data = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> seach_result = new ArrayList<ArrayList<String>>();
	ArrayList<String> settings_data = new ArrayList<String>();

	ArrayList<String> back_list = new ArrayList<String>();

	// contains String data such as category name
	ArrayList<String> back_list2 = new ArrayList<String>();

	// contains integer data such as book id
	ArrayList<Integer> back_list3 = new ArrayList<Integer>();

	String category_toAdd = "";
	int book_id_toAdd;

	@FXML
	private Button categories_btn;

	@FXML
	private Button mybooks_btn;

	@FXML
	private Button settings_btn;

	@FXML
	private Button about_btn;

	@FXML
	private Button search_btn;

	@FXML
	private ComboBox<String> filter_box;

	@FXML
	private TextField txt_search_btn;

	@FXML
	private ScrollPane cards_scroll;

	@FXML
	private AnchorPane mybooks_pane;

	@FXML
	private AnchorPane about_pane;

	@FXML
	private VBox settings_pane;

	@FXML
	private FlowPane cards_pane;

	@FXML
	private VBox card_vbox;

	@FXML
	private Label card_label1;

	@FXML
	private Label card_label2;

	@FXML
	private Label title_label;

	@FXML
	private AnchorPane book_page;

	@FXML
	private Label book_title;

	@FXML
	private Label author_label;

	@FXML
	private Label inves_label;

	@FXML
	private Label source_label;

	@FXML
	private Button openBook_btn;

	@FXML
	private Button addBook_btn;

	@FXML
	private Button back_btn;

	@FXML
	private RadioButton rd_table;

	@FXML
	private RadioButton rd_cards;

	@FXML
	private ToggleGroup group;

	@FXML
	private TextField select_folder_text;

	@FXML
	private Button select_folder_btn;

	@FXML
	private TextField select_db_text;

	@FXML
	private Button select_db_btn;

	@FXML
	private StackPane main_stackPane;

	@FXML
	private Button update_setting_btn;

	String current_showType;



	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		db_directory = initializeDB.readDBFile();

		// if main text file doesn't exist, you have to create a new one
		if (db_directory.equals("no DB") || !initializeDB.checkDBexist(db_directory)) {

			isThereDB = false;
			filter_box.getItems().addAll("اسم الكتاب", "اسم المؤلف", "اسم الباحث", "اسم المصدر");
			filter_box.getSelectionModel().selectFirst();
			back_btn.setVisible(false);
			rd_cards.setUserData("بحث");
			rd_table.setUserData("ÌÏæá");

		}

		else {
			settingInitialize();
			db_directory = initializeDB.readDBFile();
			select_db_text.setText(db_directory);
			back_btn.setVisible(false);
			filter_box.getItems().addAll("اسم الكتاب", "اسم المؤلف", "اسم الباحث", "اسم المصدر");
			filter_box.getSelectionModel().selectFirst();
			rd_cards.setUserData("بحث");
			rd_table.setUserData("ÌÏæá");

			if (!isdbError) {
				categories_data = RetrieveData.getCategoriesDB();
				createCards("category", null, categories_data.get(0).size());
			}

		}

	}

	@FXML
	void changePane(ActionEvent event) {

		if (event.getSource() == settings_btn) {
			updateBackList("settings", null, -1);
			setInVisible("settings");
			settings_pane.toFront();
		}

		else if (event.getSource() == categories_btn && isThereDB) {
			updateBackList("categories", null, -1);
			categories_data = RetrieveData.getCategoriesDB();
			createCards("category", null, categories_data.get(0).size());
			setInVisible("categories");
			cards_scroll.toFront();
		}

		else if (event.getSource() == mybooks_btn && isThereDB) {

			Mybooks_data = RetrieveData.getMyBooksDB();

			if (current_showType.equals("بحث")) {
				updateBackList("mybooks", null, -1);
				createCards("my_books", null, Mybooks_data.get(1).size());
				setInVisible("mybooks");
				cards_scroll.toFront();
			} else {
				updateBackList("mybooks", null, -1);
				createTabel("my_books", null, Mybooks_data.get(1).size());
				setInVisible("mybooks");
		 
			}

			title_label.setText("كتبي");
		}

		else if (event.getSource() == about_btn) {
			updateBackList("about", null, -1);
			setInVisible("about");
			about_pane.toFront();
		}

	}

	@FXML
	void backMethod(ActionEvent event) {

		if (event.getSource() == back_btn) {
			int size = back_list.size();
			int last = back_list.size() - 1;

			if (last == 0) {
				back_btn.setVisible(false);
				back_list.set(0, "categories");
				createCards("category", null, categories_data.get(0).size());
				setInVisible("categories");
				 
			}

			else {
				back_list.remove(last);
				back_list2.remove(last);
				back_list3.remove(last);
				setInVisible(back_list.get(last - 1));

				if (back_list.get(last - 1).equals("categories")) {
					createCards("category", null, categories_data.get(0).size());
				}

				else if (back_list.get(last - 1).equals("mybooks")) {
					title_label.setText("كتبي");
					Mybooks_data = RetrieveData.getMyBooksDB();

					if (current_showType.equals("بحث")) {
						createCards("my_books", null, Mybooks_data.get(1).size());
					} else {
						createTabel("my_books", null, Mybooks_data.get(1).size());
					}

				}

				else if (back_list.get(last - 1).equals("books")) {
					if (current_showType.equals("بحث")) {
						createCards("book", back_list2.get(last - 1), back_list3.get(last - 1));
					} else {
						createTabel("book", back_list2.get(last - 1), back_list3.get(last - 1));

					}

				}

				else if (back_list.get(last - 1).equals("bookpage")) {

					book_info = RetrieveData.getBookDB(back_list2.get(last - 1), back_list3.get(last - 1));

					createBookPage(back_list2.get(last - 1), back_list3.get(last - 1));

				}

				else if (back_list.get(last - 1).equals("search")) {
					search(back_list2.get(last - 1), back_list3.get(last - 1));
					// createCards("search", back_list2.get(last-1), back_list3.get(last-1));

				}

				 
			}

		}
		


	}

	public void updateBackList(String pane, String category, int book_id) {
		back_btn.setVisible(true);
		int size = back_list.size();
		int last = back_list.size() - 1;

		if (size == 0) {
			back_list.add(pane);
			back_list2.add(category);
			back_list3.add(book_id);
		 
		}

		else {

			// no repeat
			if (!back_list.get(last).equals(pane)) {
				back_list.add(pane);
				back_list2.add(category);
				back_list3.add(book_id);
				 
			}

		}

	}

	public void setInVisible(String pane) {
		 
		if (pane.equals("bookpage")) {

			title_label.setText("عن الكتاب");
			book_page.setVisible(true);
			settings_pane.setVisible(false);
			cards_scroll.setVisible(false);
			about_pane.setVisible(false);
			book_page.toFront();

		}

		else if (pane.equals("settings")) {

			title_label.setText("الإعدادات");
			book_page.setVisible(false);
			settings_pane.setVisible(true);
			cards_scroll.setVisible(false);
			about_pane.setVisible(false);
			settings_pane.toFront();

		}

		else if (pane.equals("about")) {

			title_label.setText("عن التطبيق");
			book_page.setVisible(false);
			settings_pane.setVisible(false);
			cards_scroll.setVisible(false);
			about_pane.setVisible(true);
			about_pane.toFront();
		}

		else if (pane.equals("categories")) {

			title_label.setText("التصنيفات");
			book_page.setVisible(false);
			settings_pane.setVisible(false);
			cards_scroll.setVisible(true);
			about_pane.setVisible(false);
			cards_scroll.toFront();

		}

		else if (pane.equals("mybooks")) {

			book_page.setVisible(false);
			settings_pane.setVisible(false);

			about_pane.setVisible(false);

			if (current_showType.equals("بحث")) {
				cards_scroll.setVisible(true);
				cards_scroll.toFront();

			} else {
				
				cards_scroll.setVisible(false);
			}

		}

		else if (pane.equals("books")) {

			 
			book_page.setVisible(false);
			settings_pane.setVisible(false);

			about_pane.setVisible(false);

			if (current_showType.equals("بحث")) {
				cards_scroll.setVisible(true);
				
			} else {
				
				cards_scroll.setVisible(false);
			}

		}

		else if (pane.equals("search")) {

			title_label.setText("بحث");
			book_page.setVisible(false);
			settings_pane.setVisible(false);

			about_pane.setVisible(false);

			if (current_showType.equals("بحث")) {
				cards_scroll.setVisible(true);
				
			} else {
				
				cards_scroll.setVisible(false);
			}

		}

	}

	public void createCards(String type, String category, int number_of_cards) {

		cards_pane.getChildren().removeAll(cards_pane.getChildren());
		cards_pane.setAlignment(Pos.TOP_LEFT);

		if (number_of_cards == 0 && type != "category") {

			Label no_cards_text = new Label();
			no_cards_text.setText("ÚÐÑÇõ¡ åÐÇ ÇáÞÓã áÇ íÊæÝÑ Úáì ßÊÈ");
			no_cards_text.setId("nobooks_id");
			cards_pane.getChildren().add(no_cards_text);
			cards_pane.setAlignment(Pos.TOP_CENTER);

		}

		else {

			if (type.equals("category")) {

				for (int i = 0; i < number_of_cards; i++) {

					VBox card = new VBox();
					card.setId("card" + i);
					card.setAlignment(Pos.CENTER);
					card.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
					card.setPrefHeight(122.0);
					card.setPrefWidth(199.0);
					card.setSpacing(20.0);
					card.setStyle("-fx-background-color: #2ECC40; -fx-border-color: black; -fx-border-width: 0.5");

					Label label1 = new Label();
					label1.setId("card" + i + "label" + 1);
					label1.setText(categories_data.get(0).get(i));

					Label label2 = new Label();
					label2.setId("card" + i + "label" + 2);
					label2.setText(categories_data.get(1).get(i) + " كتاب");

					setCardsFonts(label1, label2);

					card.getChildren().add(label1);
					card.getChildren().add(label2);

					cards_pane.getChildren().addAll(card);

					int temp_num = i;

					card.setOnMouseEntered((MouseEvent e) -> {

						card.setStyle("-fx-background-color: #01FF70; -fx-border-color: black; -fx-border-width: 0.5");
					});

					card.setOnMouseExited((MouseEvent e) -> {

						card.setStyle("-fx-background-color: #2ECC40; -fx-border-color: black; -fx-border-width: 0.5");

					});

					card.setOnMouseClicked(event -> {
						updateBackList("categories", null, -1);

						category_books_data = RetrieveData.getCategoryBooksDB(categories_data.get(2).get(temp_num));
						title_label.setText(categories_data.get(0).get(temp_num));

						if (current_showType.equals("بحث")) {
							createCards("book", categories_data.get(2).get(temp_num),
									category_books_data.get(0).size());
						} else {
							createTabel("book", categories_data.get(2).get(temp_num),
									category_books_data.get(0).size());
						}

					});

				}

			}

			else if (type.equals("book")) {
				title_label.setText(RetrieveData.getCategoryTitle(category));
				for (int i = 0; i < number_of_cards; i++) {

					VBox card = new VBox();
					card.setId("card" + i);
					card.setAlignment(Pos.CENTER);
					card.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
					card.setPrefHeight(122.0);
					card.setPrefWidth(199.0);
					card.setSpacing(20.0);
					card.setStyle("-fx-background-color: #2ECC40; -fx-border-color: black; -fx-border-width: 0.5");

					Label label1 = new Label();
					label1.setId("card" + i + "label" + 1);
					if (category_books_data.get(0).get(i) == null) {
						label1.setText("ÈÏæä ÇÓã");
					} else {
						label1.setText(category_books_data.get(0).get(i));
					}

					Label label2 = new Label();
					label2.setId("card" + i + "label" + 2);

					if (category_books_data.get(1).get(i) == null || category_books_data.get(1).get(i).equals(" ")) {
						label2.setText("ÈÏæä ãÄáÝ");
					}

					else {
						label2.setText(category_books_data.get(1).get(i));
					}

					setCardsFonts(label1, label2);
					 
					card.getChildren().add(label1);
					card.getChildren().add(label2);

					cards_pane.getChildren().addAll(card);

					int temp_num = i;

					card.setOnMouseEntered((MouseEvent e) -> {

						card.setStyle("-fx-background-color: #01FF70; -fx-border-color: black; -fx-border-width: 0.5");
					});

					card.setOnMouseExited((MouseEvent e) -> {

						card.setStyle("-fx-background-color: #2ECC40; -fx-border-color: black; -fx-border-width: 0.5");

					});

					updateBackList("books", category, number_of_cards);
					card.setOnMouseClicked(event -> {
						 
						book_info = RetrieveData.getBookDB(category, temp_num + 1);

						createBookPage(category, temp_num + 1);
						setInVisible("bookpage");

						book_page.toFront();

					});

				}

			}

			else if (type.equals("my_books")) {

				for (int i = 0; i < number_of_cards; i++) {

					VBox card = new VBox();
					card.setId("card" + i);
					card.setAlignment(Pos.CENTER);
					card.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
					card.setPrefHeight(122.0);
					card.setPrefWidth(199.0);
					card.setSpacing(20.0);
					card.setStyle("-fx-background-color: #2ECC40; -fx-border-color: black; -fx-border-width: 0.5");

					Label label1 = new Label();
					label1.setId("card" + i + "label" + 1);
					if (Mybooks_data.get(2).get(i) == null) {
						label1.setText("ÈÏæä ÇÓã");
					} else {
						label1.setText(Mybooks_data.get(2).get(i));
					}

					Label label2 = new Label();
					label2.setId("card" + i + "label" + 2);

					if (Mybooks_data.get(3).get(i) == null || Mybooks_data.get(3).get(i).equals(" ")) {
						label2.setText("ÈÏæä ãÄáÝ");
					}

					else {
						label2.setText(Mybooks_data.get(3).get(i));
					}

					setCardsFonts(label1, label2);

					card.getChildren().add(label1);
					card.getChildren().add(label2);

					cards_pane.getChildren().addAll(card);

					int temp_num = i;

					card.setOnMouseEntered((MouseEvent e) -> {

						card.setStyle("-fx-background-color: #01FF70; -fx-border-color: black; -fx-border-width: 0.5");
					});

					card.setOnMouseExited((MouseEvent e) -> {

						card.setStyle("-fx-background-color: #2ECC40; -fx-border-color: black; -fx-border-width: 0.5");

					});

					card.setOnMouseClicked(event -> {
						updateBackList("mybooks", null, -1);
						book_info = RetrieveData.getBookDB(Mybooks_data.get(0).get(temp_num),
								Integer.parseInt(Mybooks_data.get(1).get(temp_num)));

						String cate = Mybooks_data.get(0).get(temp_num);
						int id = Integer.parseInt(Mybooks_data.get(1).get(temp_num));
						createBookPage(cate, id);
						setInVisible("bookpage");

						book_page.toFront();

					});

				}

			}

			else if (type.equals("search")) {

				for (int i = 0; i < number_of_cards; i++) {

					VBox card = new VBox();
					card.setId("card" + i);
					card.setAlignment(Pos.CENTER);
					card.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
					card.setPrefHeight(122.0);
					card.setPrefWidth(199.0);
					card.setSpacing(20.0);
					card.setStyle("-fx-background-color: #2ECC40; -fx-border-color: black; -fx-border-width: 0.5");

					Label label1 = new Label();
					label1.setId("card" + i + "label" + 1);
					if (seach_result.get(1).get(i) == null) {
						label1.setText("ÈÏæä ÇÓã");
					} else {
						label1.setText(seach_result.get(1).get(i));
					}

					Label label2 = new Label();
					label2.setId("card" + i + "label" + 2);

					if (seach_result.get(2).get(i) == null || seach_result.get(2).get(i).equals(" ")) {
						label2.setText("ÈÏæä ãÄáÝ");
					}

					else {
						label2.setText(seach_result.get(2).get(i));
					}

					setCardsFonts(label1, label2);

					card.getChildren().add(label1);
					card.getChildren().add(label2);

					cards_pane.getChildren().addAll(card);

					int temp_num = i;

					card.setOnMouseEntered((MouseEvent e) -> {

						card.setStyle("-fx-background-color: #01FF70; -fx-border-color: black; -fx-border-width: 0.5");
					});

					card.setOnMouseExited((MouseEvent e) -> {

						card.setStyle("-fx-background-color: #2ECC40; -fx-border-color: black; -fx-border-width: 0.5");

					});

					card.setOnMouseClicked(event -> {

						book_info = RetrieveData.getBookDB(seach_result.get(3).get(temp_num),
								Integer.parseInt(seach_result.get(0).get(temp_num)));

						String cate = seach_result.get(3).get(temp_num);
						int id = Integer.parseInt(seach_result.get(0).get(temp_num));
						createBookPage(cate, id);
						setInVisible("bookpage");

						book_page.toFront();

					});

				}

			}

		}

	}

	public void createBookPage(String category, int id) {
		updateBackList("bookpage", category, id);
		book_title.setText(book_info.get(0));
		author_label.setText(book_info.get(1));
		inves_label.setText(book_info.get(2));
		source_label.setText(book_info.get(3));

		if (RetrieveData.checkMyBooks(book_info.get(5), book_info.get(4))) {

			addBook_btn.setText("إزالة من كتبي");
			addBook_btn.setStyle("-fx-background-color: #FF4136");
		}

		else {

			addBook_btn.setText("اضف إلى كتبي");
			addBook_btn.setStyle("-fx-background-color: #007bff");
		}

	}

	@FXML
	void OpenBookMethod(ActionEvent event) {

		if (event.getSource() == openBook_btn) {

			String full_path = RetrieveData.getBookPath(book_info.get(5), book_info.get(4));

			try {

				Desktop.getDesktop().open(new File(full_path));

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	@FXML
	void addBookMethod(ActionEvent event) {

		// add book
		if (event.getSource() == addBook_btn
				&& (RetrieveData.checkMyBooks(book_info.get(5), book_info.get(4)) == false)) {

			RetrieveData.addToMyBooks(book_info.get(5), book_info.get(4));

			addBook_btn.setText("إزالة من كتبي");
			addBook_btn.setStyle("-fx-background-color: #FF4500");

		}

		// remove book
		else if (event.getSource() == addBook_btn && RetrieveData.checkMyBooks(book_info.get(5), book_info.get(4))) {

			RetrieveData.removeFromeMyBooks(book_info.get(5), book_info.get(4));

			addBook_btn.setText("اضف إلى كتبي");
			addBook_btn.setStyle("-fx-background-color: #4169E1");
		}

	}

	@FXML
	void select_folder_method(ActionEvent event) {

		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("ÇÎÊÑ ãÌáÏ ÇáßÊÈ");
		File selectedDirectory = chooser.showDialog(Main.primaryStage);

		if (event.getSource() == select_folder_btn) {

			if (selectedDirectory != null) {

				select_folder_text.setText(selectedDirectory.getAbsolutePath());

			}

		}

	}

	@FXML
	void select_db_method(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("ÇÎÊÑ ÞÇÚÏÉ ÇáÈíÇäÇÊ");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Access File", "*.accdb"));

		File selectedFile = fileChooser.showOpenDialog(Main.primaryStage);

		if (event.getSource() == select_db_btn) {

			if (selectedFile != null) {

				select_db_text.setText(selectedFile.getAbsolutePath());

			}

		}

	}

	@FXML
	void update_setting_method(ActionEvent event) {

		Alert error_dialog = new Alert(AlertType.NONE);

		String show_trype = group.getSelectedToggle().getUserData().toString();
		String db_path = select_db_text.getText();
		String folder_path = select_folder_text.getText();

		if (event.getSource() == update_setting_btn) {

			if (db_path.trim().isEmpty() || folder_path.trim().isEmpty()) {

				error_dialog.setAlertType(AlertType.WARNING);

				error_dialog.setTitle("ÎØÃ ÈÇáÊÍÏíË");
				error_dialog.setHeight(200);
				error_dialog.setContentText("ÇáÑÌÇÁ ÇÏÎÇá ÇáÈíÇäÇÊ ÈÔßá ÕÍíÍ");
				// show the dialog
				error_dialog.show();

			}

			else {

				if (!isThereDB) {

					initializeDB.createDBFile();
					initializeDB.writeDBFile(db_path);
					db_directory = db_path;
					book_folder_directory = folder_path;
					isThereDB = true;
				}

				else {
					initializeDB.writeDBFile(db_path);
					db_directory = db_path;
					book_folder_directory = folder_path;
					current_showType = show_trype;
					RetrieveData.updateSetting(show_trype, folder_path, db_path);

					error_dialog.setAlertType(AlertType.INFORMATION);

					error_dialog.setTitle("Êã ÊÍÏíË ÇáÅÚÏÇÏÇÊ");
					error_dialog.setHeight(200);
					error_dialog.setContentText("Êã ÊÍÏíË ÇáÅÚÏÇÏÇÊ ÈäÌÇÍ");
					// show the dialog
					error_dialog.show();

				}
			}

			db_directory = select_db_text.getText();
			book_folder_directory = select_folder_text.getText();

		}

	}

	public void settingInitialize() {

		settings_data = RetrieveData.settingsCurrentData();

		if (!settings_data.isEmpty()) {

			select_folder_text.setText(settings_data.get(1));
			select_db_text.setText(settings_data.get(2));

			book_folder_directory = settings_data.get(1);

			if (settings_data.get(0).equals("ÌÏæá")) {
				rd_table.setSelected(true);
				current_showType = "ÌÏæá";
			}

			else {
				rd_cards.setSelected(true);
				current_showType = "بحث";
			}
		}

		else {
			isdbError = true;
		}

	}

	public static void errorDialog() {

		Alert error_dialog = new Alert(AlertType.NONE);
		error_dialog.setAlertType(AlertType.WARNING);

		error_dialog.setTitle("ÎØÃ ÈÞÇÚÏÉ ÇáÈíÇäÇÊ");
		error_dialog.setHeight(200);
		error_dialog.setContentText("ÎØÃ ÈÞÇÚÏÉ ÇáÈíÇäÇÊ¡ ÇáÑÌÇÁ ÇÏÎÇá ÞÇÚÏÉ ÇáÈíÇäÇÊ ÇáÕÍíÍÉ Ãæ ÊÍÏíËåÇ");
		// show the dialog
		error_dialog.show();

		isdbError = true;

	}

	public void setCardsFonts(Label L1, Label L2) {

		L1.setTextFill(Color.BLACK);
		L1.setFont(new Font("Hacen Tunisia", 15));
		L1.setStyle("-fx-font-weight: 500; -fx-alignment: center");

		L2.setPrefHeight(58);
		L2.setPrefWidth(177);

		L2.setTextFill(Color.BLACK);
		L2.setFont(new Font("Hacen Tunisia", 14));
		L2.setStyle("-fx-font-weight: 300; -fx-alignment: center");
		L2.setWrapText(false);
		L2.setPrefHeight(18);
		L2.setPrefWidth(174);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createTabel(String type, String category, int number_of_rows) {

 

		 
		@SuppressWarnings("rawtypes")
		TableView tabel_books = new TableView();
		TableColumn col_num = new TableColumn("#");

		TableColumn col_book_name = new TableColumn("اسم الكتاب");

		TableColumn col_author = new TableColumn("اسم المؤلف");
		
		// prepare the table
		tabel_books.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		tabel_books.setFixedCellSize(-1);
		col_num.setPrefWidth(46.39990311861038);
		col_book_name.setPrefWidth(317.5999298095703);
		col_author.setPrefWidth(285.60003662109375);
		tabel_books.getColumns().addAll(col_num, col_book_name, col_author);
		tabel_books.setPlaceholder(new Label("ÚÐÑÇõ¡ åÐÇ ÇáÞÓã áÇ íÊæÝÑ Úáì ßÊÈ"));

		if (type.equals("book")) {
			title_label.setText(RetrieveData.getCategoryTitle(category));
			ObservableList<BookTableData> data1 = FXCollections.observableArrayList();
			updateBackList("books", category, number_of_rows);

			// When clicking on a row:
			tabel_books.setRowFactory(t -> {
				TableRow<BookTableData> row1 = new TableRow<>();
				row1.setOnMouseClicked(event1 -> {
					if (event1.getClickCount() == 2 && (!row1.isEmpty())) {
						updateBackList("books", category, number_of_rows);
						BookTableData rowData1 = row1.getItem();

						book_info = RetrieveData.getBookDB(category, rowData1.getNumber());

						createBookPage(category, rowData1.getNumber() + 1);
						setInVisible("bookpage");

						book_page.toFront();
					}
				});
				return row1;
			});

			// create rows
			for (int i = 0; i < number_of_rows; i++) {

				// enter data
				String name = category_books_data.get(0).get(i);
				String author = category_books_data.get(1).get(i);

				if (name == null || name.equals(" ")) {
					name = "بدون عنوان";
				}

				data1.add(new BookTableData(i + 1, name, author));

			}

			tabel_books.setItems(data1);
			col_num.setCellValueFactory(new PropertyValueFactory<>("number"));
			col_book_name.setCellValueFactory(new PropertyValueFactory<>("name"));
			col_author.setCellValueFactory(new PropertyValueFactory<>("author"));

			if (!main_stackPane.getChildren().contains(tabel_books)) {

				main_stackPane.getChildren().add(tabel_books);
			}

		}

		else if (type.equals("my_books")) {
			 
			ObservableList<BookTableData> data = FXCollections.observableArrayList();
			 
			// When clicking on a row:
			tabel_books.setRowFactory(tv -> {
				TableRow<BookTableData> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						// updateBackList("mybooks", null, -1);
						BookTableData rowData = row.getItem();

						String cate = Mybooks_data.get(0).get(rowData.getNumber() - 1);
						int id = Integer.parseInt(Mybooks_data.get(1).get(rowData.getNumber() - 1));

						 

						book_info = RetrieveData.getBookDB(Mybooks_data.get(0).get(rowData.getNumber() - 1),
								Integer.parseInt(Mybooks_data.get(1).get(rowData.getNumber() - 1)));

						 
						createBookPage(cate, id);

						setInVisible("bookpage");

						book_page.toFront();
					}
				});
				return row;
			});

			// create rows
			for (int i = 0; i < number_of_rows; i++) {

				// enter data
				String name = Mybooks_data.get(2).get(i);
				String author = Mybooks_data.get(3).get(i);

				if (name == null || name.equals(" ")) {
					name = "بدون عنوان";
				}

				data.add(new BookTableData(i + 1, name, author));

			}

			tabel_books.setItems(data);
			col_num.setCellValueFactory(new PropertyValueFactory<>("number"));
			col_book_name.setCellValueFactory(new PropertyValueFactory<>("name"));
			col_author.setCellValueFactory(new PropertyValueFactory<>("author"));

			if (!main_stackPane.getChildren().contains(tabel_books)) {
				main_stackPane.getChildren().add(tabel_books);
			}

		}

		else if (type.equals("search")) {

			ObservableList<BookTableData> data = FXCollections.observableArrayList();

			// When clicking on a row:
			tabel_books.setRowFactory(tv -> {
				TableRow<BookTableData> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						BookTableData rowData = row.getItem();

						book_info = RetrieveData.getBookDB(seach_result.get(3).get(rowData.getNumber() - 1),
								Integer.parseInt(seach_result.get(0).get(rowData.getNumber() - 1)));

						String cate = seach_result.get(3).get(rowData.getNumber() - 1);
						int id = Integer.parseInt(seach_result.get(0).get(rowData.getNumber() - 1));

						createBookPage(cate, id);
						setInVisible("bookpage");

						book_page.toFront();
					}
				});
				return row;
			});

			// create rows
			for (int i = 0; i < number_of_rows; i++) {

				// enter data
				String name = seach_result.get(1).get(i);
				String author = seach_result.get(2).get(i);

				if (name == null || name.equals(" ")) {
					name = "بدون عنوان";
				}

				data.add(new BookTableData(i + 1, name, author));

			}

			tabel_books.setItems(data);
			col_num.setCellValueFactory(new PropertyValueFactory<>("number"));
			col_book_name.setCellValueFactory(new PropertyValueFactory<>("name"));
			col_author.setCellValueFactory(new PropertyValueFactory<>("author"));

			if (!main_stackPane.getChildren().contains(tabel_books)) {
				main_stackPane.getChildren().add(tabel_books);
			}

		}

		
		tabel_books.toFront();

	}

	@FXML
	void seachByKeyboard(KeyEvent event) {

		if (event.getSource() == txt_search_btn && event.getCode().equals(KeyCode.ENTER)) {
			search(null, -1);

		}

	}

	@FXML
	void searchByMouse(ActionEvent event) {

		if (event.getSource() == search_btn && isThereDB) {
			search(null, -1);

		}

	}

	public void search(String word, int filter) {

		String text = "";
		String selected_filter = "";
		title_label.setText("بحث");

		if (filter != -1) {
			if (filter == 1) {
				selected_filter = "اسم الكتاب";
			}

			else if (filter == 2) {
				selected_filter = "اسم المؤلف";
			}

			else if (filter == 3) {
				selected_filter = "اسم الباحث";
			}

			else if (filter == 4) {
				selected_filter = "اسم المصدر";
			}

		}

		else {
			selected_filter = filter_box.getSelectionModel().getSelectedItem().toString();
		}

		if (word != null) {
			text = word;
		} else {
			text = txt_search_btn.getText().trim();
		}

		boolean valid_text = true;

		if (text.length() < 2) {
			valid_text = false;
		}

		if (selected_filter.equals("اسم الكتاب") && valid_text) {
			seach_result = RetrieveData.getSearchedBook("book_name", text, categories_data.get(0).size(),
					categories_data);

			if (current_showType.equals("بحث")) {
				createCards("search", null, seach_result.get(0).size());
				updateBackList("search", text, 1);
				setInVisible("search");
				cards_scroll.toFront();
			} else {
				createTabel("search", null, seach_result.get(0).size());
				updateBackList("search", text, 1);
				setInVisible("search");
			}

		}

		else if (selected_filter.equals("اسم المؤلف") && valid_text) {
			seach_result = RetrieveData.getSearchedBook("author", text, categories_data.get(0).size(), categories_data);

			if (current_showType.equals("بحث")) {
				createCards("search", null, seach_result.get(0).size());
				updateBackList("search", text, 2);
				setInVisible("search");
				cards_scroll.toFront();
			} else {
				createTabel("search", null, seach_result.get(0).size());
				updateBackList("search", text, 2);
				setInVisible("search");
			}

		}

		else if (selected_filter.equals("اسم الباحث") && valid_text) {
			seach_result = RetrieveData.getSearchedBook("investigator", text, categories_data.get(0).size(),
					categories_data);

			if (current_showType.equals("بحث")) {
				createCards("search", null, seach_result.get(0).size());
				updateBackList("search", text, 3);
				setInVisible("search");
				cards_scroll.toFront();
			} else {
				createTabel("search", null, seach_result.get(0).size());
				updateBackList("search", text, 3);
				setInVisible("search");
			}

		}

		else if (selected_filter.equals("اسم المصدر") && valid_text) {
			seach_result = RetrieveData.getSearchedBook("source", text, categories_data.get(0).size(), categories_data);

			if (current_showType.equals("بحث")) {
				createCards("search", null, seach_result.get(0).size());
				updateBackList("search", text, 4);
				setInVisible("search");
				cards_scroll.toFront();
			} else {
				createTabel("search", null, seach_result.get(0).size());
				updateBackList("search", text, 4);
				setInVisible("search");
			}

		}

	}
	

    @FXML
    void changeBackBtnColor1(MouseEvent event) {
    	
    	if (event.getSource() == back_btn) {
    		back_btn.setStyle("-fx-background-color: #cacaca;");
    	}

    }

    @FXML
    void changeBackBtnColor2(MouseEvent event) {
    	back_btn.setStyle("-fx-background-color: #DDDDDD;");
    }

}
