package service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Menu;

public class menuService {
	
	private static Database db = Database.getInstance();
	
	public static ObservableList<Menu>  getAll() {
		String query = "SELECT * FROM menu";
		ObservableList<Menu> menuList = FXCollections.observableArrayList();
		
		try {
			PreparedStatement ps = db.preparedStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				menuList.add(new Menu(rs.getString("id"), rs.getString("name"), rs.getInt("price"), rs.getInt("stock")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return menuList;
	}
	
	public static void add(Menu menu) {
		String query = "INSERT INTO menu VALUES (?, ?, ?, ?)";
		
		try {
			PreparedStatement ps = db.preparedStatement(query);
			ps.setString(1, menu.getId());
			ps.setString(2, menu.getName());
			ps.setInt(3, menu.getPrice());
			ps.setInt(4, menu.getStock());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void update(Menu menu) {
		String query = "UPDATE menu SET name = ?, price = ?, stock = ? WHERE id = ?";

		try {
			PreparedStatement ps = db.preparedStatement(query);
			ps.setString(1, menu.getName());
			ps.setInt(2, menu.getPrice());
			ps.setInt(3, menu.getStock());
			ps.setString(4, menu.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Menu menu) {
		String query = "DELETE FROM menu WHERE id = ?";
		
		try {
			PreparedStatement ps = db.preparedStatement(query);
			ps.setString(1, menu.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
