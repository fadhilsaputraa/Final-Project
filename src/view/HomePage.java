package view;

import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Main;
import service.menuService;
import model.Menu;

public class HomePage {
	
	private Stage stage;
	private BorderPane root = new BorderPane();
	private GridPane menus = new GridPane();
	private Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
	private Label idLbl = new Label("Menu ID: ");
	private Label nameLbl = new Label("Menu Name: ");
	private Label priceLbl = new Label("Menu Price: ");
	private Label stockLbl = new Label("Menu Stock: ");
	private TextField idTf = new TextField();
	private TextField nameTf = new TextField();
	private TextField priceTf = new TextField();
	private Spinner<Integer> stockSpinner = new Spinner<Integer>(0, 100, 50);
	private Button addBtn = new Button("Add");
	private Button updateBtn = new Button("Update");
	private Button deleteBtn = new Button("Delete");
	private HBox buttonBox = new HBox(addBtn, deleteBtn, updateBtn);
	private TableView<Menu> table = new TableView<Menu>();
	private TableColumn<Menu, String> idCol = new TableColumn<Menu, String>("Menu ID");
	private TableColumn<Menu, String> nameCol = new TableColumn<Menu, String>("Menu Name");
	private TableColumn<Menu, Integer> priceCol = new TableColumn<Menu, Integer>("Menu Price");
	private TableColumn<Menu, Integer> stockCol = new TableColumn<Menu, Integer>("Menu Stock");
	
	private Menu selectedMenu;
	private ObservableList<Menu> menuList = FXCollections.observableArrayList();
	
	public HomePage(Stage stage) {
		this.stage = stage;
		this.setComponent();
		this.setStyle();
		this.setTableColumns();
		this.populateTable();
		this.handleButton();
		this.setTableListener();
	}
	
	@SuppressWarnings("unchecked")
	private void setComponent() {
		menus.add(idLbl, 0, 0);
		menus.add(idTf, 1, 0);
		menus.add(nameLbl, 0, 1);
		menus.add(nameTf, 1, 1);
		menus.add(priceLbl, 0, 2);
		menus.add(priceTf, 1, 2);
		menus.add(stockLbl, 0, 3);
		menus.add(stockSpinner, 1, 3);
		menus.add(buttonBox, 0, 4, 2, 1);	
		
		table.getColumns().addAll(idCol, nameCol, priceCol, stockCol);
		
		idTf.setEditable(false);
		idTf.setText("auto generated");
		stockSpinner.setEditable(true);
		
		root.setTop(table);
		root.setBottom(menus);
		stage.setScene(scene);
	}
	
	private void setStyle() {
		root.setPadding(new Insets(15));
		menus.setVgap(10);
		menus.setHgap(10);
		stockSpinner.setMaxWidth(Double.MAX_VALUE);
		addBtn.setMinWidth(90);
		deleteBtn.setMinWidth(90);
		updateBtn.setMinWidth(90);
		buttonBox.setSpacing(10);
		menus.setAlignment(Pos.CENTER);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}
	
	private void setTableColumns() {
		idCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
		priceCol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("price"));
		stockCol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("stock"));
	}
	
	private void setTableListener() {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedMenu) -> {
			if (selectedMenu != null) {
				this.selectedMenu = selectedMenu;
				idTf.setText(selectedMenu.getId());
				nameTf.setText(selectedMenu.getName());
				priceTf.setText(String.valueOf(selectedMenu.getPrice()));
				stockSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, selectedMenu.getStock()));
			}
		});
	}
	
	private void populateTable() {
		menuList = menuService.getAll();
		table.setItems(FXCollections.observableArrayList(menuList));
		clearSelection();
	}
	
	private void clearSelection() {
		idTf.setText("auto generated");
		nameTf.clear();
		priceTf.clear();
		stockSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50));
		
	}
	
	private void handleButton() {
		
		addBtn.setOnAction(event -> {
			String id = generateID();
			String name = nameTf.getText();
			String price = priceTf.getText();
			int stock = stockSpinner.getValue();
			
			if (name.isEmpty() || price.isEmpty()) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Fill in required fields!");
				return;
			}
			
			try {
				Integer.valueOf(price);
			} catch (Exception e) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Price must be numeric!");
				return;
			}
			
			menuService.add(new Menu(id, name, Integer.valueOf(price), stock));
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "Menu successfully added!");
		});
		
		deleteBtn.setOnAction(event -> {
			if (nameTf.getText().isEmpty() || priceTf.getText().isEmpty() || !checkID()) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Select a menu to delete!");
				return;
			}
			
			menuService.delete(selectedMenu);
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "Menu successfully deleted!");
		});
		
		updateBtn.setOnAction(event -> {
			String id = idTf.getText();
			String name = nameTf.getText();
			String price = priceTf.getText();
			int stock = stockSpinner.getValue();
			
			if (name.isEmpty() || price.isEmpty() || !checkID()) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Select a menu to update!");
				return;
			}
			
			try {
				Integer.valueOf(price);
			} catch (Exception e) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Price must be numeric!");
				return;
			}
			
			menuService.update(new Menu(id, name, Integer.valueOf(price), stock));
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "Menu successfully updated");
		});
		
	}
	private String generateID() {
		Random rand = new Random();
		String ID = "PD";
		for(int i = 0; i < 3; i++) {
			ID += rand.nextInt(10);
			
		}
		
		for(Menu menu : menuList) {
			if(menu.getId().equals(ID)) {
				generateID();
			}
		}
		
		return ID;
	}
	
	private boolean checkID() {
		for(Menu menu : menuList) {
			if(idTf.getText().equals(menu.getId())) {
				return true;
			}
		}
		return false;
	}
	
	private void alert(AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.initOwner(stage);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
