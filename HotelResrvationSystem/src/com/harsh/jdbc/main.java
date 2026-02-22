package com.harsh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class main {
	private static String url = "jdbc:mysql://localhost:3306/hotel_db";
	private static String user = "root";
	private static String pswd = "Harsh@123";
	
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Connection connection = DriverManager.getConnection(url,user,pswd);
			while(true) {
				System.out.println();
				System.out.println("Hotel Management System");
				Scanner sc = new Scanner(System.in);
				System.out.println("1. Reserve a room");
				System.out.println("2. View Reservation");
				System.out.println("3. Get Room Number");
				System.out.println("4. Update Reservation");
				System.out.println("5. Delete Reservation");
				System.out.println("0. Exit");
				System.out.println("Choose an option");
				int choice = sc.nextInt();
				switch (choice) {
				case 1:
					reserveRoom(connection,sc);
					break;
				case 2:
					viewReservation(connection);
					break;
				case 3:
					getRoomNumber(connection,sc);
					break;
				case 4:
					updateReservation(connection,sc);
					break;
				case 5:
					deleteReservation(connection,sc);
					break;
				case 0:
					exit();
					sc.close();
					return;
				default:
					System.out.println("Invalid choice. Try again");
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private static void exit() {
		System.out.print("Exiting System");
		int i=5;
		while(i>0) {
			System.out.print(".");
			try {
				Thread.sleep(450);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i--;
		}
		System.out.println();
		System.out.println("Thank you for using Hotel reservation system.");
		
	}

	private static void deleteReservation(Connection connection, Scanner sc) {
		System.out.print("Enter Id to be deleted : ");
		int reservationId = sc.nextInt();
		String query = "Delete from reservations where reservation_id = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, reservationId);
			int count = stmt.executeUpdate();
			System.out.println(count + " row(s) affected");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	private static void updateReservation(Connection connection, Scanner sc) {
		System.out.print("Enter the Reservation Id to be updated : ");
		int reservationId = sc.nextInt();
		sc.nextLine();
		if(!reservationExists(connection,reservationId)) {
			System.out.println("Reservation not found for given Id ");
			return;
		}
		System.out.print("Enter new guest name : ");
		String guestName = sc.nextLine();
		System.out.print("Enter new room number : ");
		int roomNumber = sc.nextInt();
		System.out.print("Enter new contact number : ");
		String contactNumber = sc.next();
		
		String query = "Update reservations set guest_name = ?, room_number = ?,contact_number = ? where reservation_id = reservationId";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, guestName);
			stmt.setInt(2, roomNumber);
			stmt.setString(3, contactNumber);
			int count = stmt.executeUpdate();
			System.out.println(count+" row(s) affected");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	

	private static boolean reservationExists(Connection connection, int reservationId) {
		String query = "Select reservation_id from reservations where reservation_id = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, reservationId);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void getRoomNumber(Connection connection, Scanner sc) {
		System.out.print("Enter Reservation ID : ");
		int reservationId = sc.nextInt();
		System.out.println("Enter guest name : ");
		String guestName = sc.next();
		String query = "Select room_number from reservations where reservation_id = ? and guest_name = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1,reservationId);
			stmt.setString(2, guestName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int roomNumber = rs.getInt(1);
				System.out.println("Room no. for Reservation Id : "+reservationId+" and Guest Name : "+guestName+" is "+roomNumber);
			} else {
				System.out.println("Reservation not found for the given Id and name.");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	private static void viewReservation(Connection connection) {
		String query = "Select * from reservations";
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Current reservations ");
			while(rs.next()) {
				int id = rs.getInt(1);
				String guestName = rs.getString(2);
				int roomNumber = rs.getInt(3);
				String contactNumber = rs.getString(4);
				String reservationDate = rs.getTimestamp(5).toString();
				
				System.out.println(id +" : "+ guestName+ " : "+ roomNumber+ " : "+ contactNumber+ " : "+ reservationDate);
			}
			System.out.println();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	private static void reserveRoom(Connection connection, Scanner sc) {
		System.out.print("Enter guest name: ");
		String guestName = sc.next();
		sc.nextLine();
		System.out.print("Enter the room number: ");
		int roomNumber = sc.nextInt();
		System.out.print("Enter contact number: ");
		String contactNumber = sc.next();
		
		String query = "insert into reservations(guest_name,room_number,contact_number) values(?,?,?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1,guestName);
			stmt.setInt(2,roomNumber);
			stmt.setString(3,contactNumber);
			
			int count = stmt.executeUpdate();
			System.out.println(count+" row(s) affected");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
