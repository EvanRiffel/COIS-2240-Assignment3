package application;


import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.geometry.Insets; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class RentalSystemNewGUI extends Application {
	
    private List<Vehicle> vehicles;
    private List<Customer> customers;
    private RentalHistory rentalHistory;
	RentalSystem rentalSystem = RentalSystem.getInstance();
	GridPane pane;
	Stage primary;

    @Override
    public void start(Stage primaryStage) {
    	primary = primaryStage;
    	loadData();
        pane = new GridPane();
        pane.setMinSize(350, 500); 
        pane.setPadding(new Insets(10, 10, 10, 10)); 

        pane.setAlignment(Pos.CENTER);
        pane.setVgap(5); 
        pane.setHgap(5); 

        Button addVehicleButton = new Button("Add Vehicle");
        addVehicleButton.setOnAction(e -> addVehicle());
        addVehicleButton.setMaxSize(150, 50);
        addVehicleButton.setMinSize(150, 50);

        
        Button showVehiclesButton = new Button("Show Vehicles");
        showVehiclesButton.setOnAction(e -> showVehicles());
        showVehiclesButton.setMaxSize(150, 50);
        showVehiclesButton.setMinSize(150, 50);
        
        pane.add(addVehicleButton, 0,0);
        pane.add(showVehiclesButton, 0,1);
        pane.setStyle("-fx-background-color: mediumslateblue");
       
        Scene scene = new Scene(pane, 350, 500);
        primaryStage.setTitle("Rental System GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void addVehicle() { 
    	System.out.println("AddVehicle");
        final Stage popup = new Stage();

        Label featureLabel = new Label("");
        TextField feature = new TextField();
        Label errorLabel = new Label("");

        
    	ComboBox<String> comboBox = new ComboBox<String>();

    	Label type = new Label("Type");
        comboBox.getItems().add("1: Car");
        comboBox.getItems().add("2: Motorcycle");
        comboBox.getItems().add("3: Truck");
        
        comboBox.setOnAction((event) -> {
        	errorLabel.setText("");
            if( comboBox.getValue().equals("1: Car"))
            {
            	featureLabel.setText("Feature: Number of Seats");
            }
            else if( comboBox.getValue().equals("2: Motorcycle"))
            {
            	featureLabel.setText("Feature: Has side car: Y or N");
            }
            else if( comboBox.getValue().equals("3: Truck"))
            {
            	featureLabel.setText("Feature: Cargo Capacity");
            }
            else
            {
            	featureLabel.setText("");
            }
        });
        
    	Label licenseLabel = new Label("License");
        TextField license = new TextField("ABC123");
    	Label makeLabel = new Label("Make");
        TextField make = new TextField();
    	Label modelLabel = new Label("Model");
        TextField model = new TextField();
    	Label yearLabel = new Label("Year");
        TextField year = new TextField();
    	
       
        Button add = new Button("Add Vehicle");
        add.setOnAction(e -> {
        	errorLabel.setText("");
        	if(!license.getText().isEmpty() && 
        		!make.getText().isEmpty() &&
        		!model.getText().isEmpty() &&
        		!year.getText().isEmpty() &&
        		!comboBox.getSelectionModel().isEmpty())
        	{
        		try {
            		int vehicleYear = Integer.parseInt(year.getText());
        	        Vehicle v = null;
        	        
	        		if( comboBox.getValue().equals("1: Car")) {
	    	            int featureInt = Integer.parseInt(feature.getText());
	    				v = new Car(make.getText(), model.getText(), vehicleYear, featureInt);
	                }
	                else if( comboBox.getValue().equals("2: Motorcycle")) {
	                	if(feature.getText().equals("Y")) {
		    				v = new Motorcycle(make.getText(), model.getText(), vehicleYear, true);
	                	}
	                	else {
		    				v = new Motorcycle(make.getText(), model.getText(), vehicleYear, false);
	                	}
	                }
	                else if( comboBox.getValue().equals("3: Truck")) {
	                	int featureInt = Integer.parseInt(feature.getText());
	    				v = new Truck(make.getText(), model.getText(), vehicleYear, featureInt);
	                }
	        		
	        		if(v != null) {
	        			v.setLicensePlate(license.getText());
	        			if( rentalSystem.addVehicle(v)) {
	        		    	loadData();
	        				popup.close();
	        			}
	        			else {
	        				errorLabel.setText("Error adding vehicle - License already used");
	        			}
	        		}
        		}
                catch (NumberFormatException ex){
    	            errorLabel.setText("Year or Feature is not a number!");
    	        }
        	}

        	
        });
        Button close = new Button("Close");
        close.setOnAction(e -> {
        	
        	popup.close();
        	
        });
        
        popup.initModality(Modality.NONE);
        popup.setTitle("Add Vehicle");
        popup.initOwner(primary);
        
        GridPane popupGridPane = new GridPane();
        popupGridPane.setMinSize(500, 600); 
        popupGridPane.setPadding(new Insets(10, 10, 10, 10)); 


        popupGridPane.setAlignment(Pos.CENTER);
        popupGridPane.setVgap(5); 
        popupGridPane.setHgap(5); 
        popupGridPane.setStyle("-fx-background-color: lightblue");
        
        popupGridPane.add(type,0,0);
        popupGridPane.add(comboBox,1,0);
        popupGridPane.add(licenseLabel,0,1);
        popupGridPane.add(license,1,1);
        popupGridPane.add(makeLabel,0,2);
        popupGridPane.add(make,1,2);
        popupGridPane.add(modelLabel,0,3);
        popupGridPane.add(model,1,3);
        popupGridPane.add(yearLabel,0,4);
        popupGridPane.add(year,1,4);
        popupGridPane.add(featureLabel,0,5);
        popupGridPane.add(feature,1,5);
        popupGridPane.add(add,0,6);
        popupGridPane.add(close,1,6);
        popupGridPane.add(errorLabel, 0, 7, 1, 7);

        Scene popupScene = new Scene(popupGridPane, 400, 450);
        popup.setScene(popupScene);
        popup.show();
    }
    
    private void showVehicles()
    {
    	TableView<Vehicle> tableView = new TableView<Vehicle>();

        TableColumn<Vehicle, String> column1 = new TableColumn<>("License Plate");
        column1.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));

        TableColumn<Vehicle, String> column2 = new TableColumn<>("Make");
        column2.setCellValueFactory(new PropertyValueFactory<>("make"));
        
        TableColumn<Vehicle, String> column3 = new TableColumn<>("Model");
        column3.setCellValueFactory(new PropertyValueFactory<>("model"));
        
        TableColumn<Vehicle, Number> column4 = new TableColumn<>("Year");
        column4.setCellValueFactory(new PropertyValueFactory<>("year"));
        
        TableColumn<Vehicle, String> column5 = new TableColumn<>("Status");
        column5.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);

        for( Vehicle v : vehicles )
        {
        	tableView.getItems().add(v);
        }
        
        final Stage popup = new Stage();
        popup.initModality(Modality.NONE);
        popup.setTitle("Available Vehicles");
        popup.initOwner(primary);
        
        VBox popupVbox = new VBox(50);
        Button close = new Button("Close");
        close.setOnAction(e -> popup.close());
        
        popupVbox.setAlignment(Pos.CENTER);
        popupVbox.getChildren().addAll(tableView,close);
        popupVbox.setStyle("-fx-background-color: lightblue");

       
        Scene popupScene = new Scene(popupVbox, 400, 450);
        popup.setScene(popupScene);
        popup.show();
        
    }
    
    private void loadData() {
    	//vehicles
    	vehicles = new ArrayList<>();
    	customers = new ArrayList<>();
    	rentalHistory = new RentalHistory();
    	// initiate vehicle text file
    	File file = new File("vehicles.txt");
    	// checks for file existing
        if (!file.exists()) {
            System.out.println("vehicles.txt not found. Starting with an empty list.");
        }
        else {
	        //attepmts to read the file
	        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	            String line;
	            //loops through each line of the file until a null line, aka empty line
	            while ((line = reader.readLine()) != null) {
	// chunk the line up via the the |, which separates the values in the text files
	                //String[] parts = line.split(" \\| ");
	            	System.out.println(line);
	            	String[] parts = line.split("\\| ");
	                // Ensure that the format is valid, all 5 peices, no mising information
	                if (parts.length < 6) {
	                    continue;
	                }
	                
	                // Extract the individual values
	                String plate = parts[0].trim();
	                String make = parts[1].trim();
	                String model = parts[2].trim();
	                int year = Integer.parseInt(parts[3].trim());  // Parse year as integer since currently a string
	                Vehicle.VehicleStatus status = Vehicle.VehicleStatus.valueOf(parts[4].trim());  // Parse status enum value
	                
	                // Handle the variable part (Seats, Sidecar, or Cargo Capacity)
	                String variableInfo = parts[5].trim();
	                
	                // Create the vehicle based on the type of info in the variable part, since can be truck car or motorbike
	                Vehicle vehicle = null;
	                
	                if (variableInfo.startsWith("Seats:")) {  //starts with "Seats:", Car
	                    int seats = Integer.parseInt(variableInfo.replace("Seats: ", "").trim());
	                    vehicle = new Car(make, model, year, seats);  // make Car
	                } else if (variableInfo.startsWith("Sidecar:")) {  // starts with "Sidecar:", Motorcycle
	                    boolean hasSidecar = variableInfo.contains("Yes");
	                    vehicle = new Motorcycle(make, model, year, hasSidecar);  // make Motorcycle
	                } else if (variableInfo.startsWith("Cargo Capacity:")) {  // starts with "Cargo Capacity", Truck
	                    double cargoCapacity = Double.parseDouble(variableInfo.replace("Cargo Capacity: ", "").trim());
	                    vehicle = new Truck(make, model, year, cargoCapacity);  // make Truck
	                } else {
	                    System.out.println("Unknown vehicle type for plate: " + plate);
	                    continue;  // Skip unknown vehicle types
	                }
	
	                // Set the vehicle's license plate and status after creating the vehicle
	                if (vehicle != null) {
	                    vehicle.setLicensePlate(plate);
	                    vehicle.setStatus(status);
	                    vehicles.add(vehicle);  // Add the vehicle to list
	                }
	            }
	            System.out.println("Vehicles loaded successfully.");
	            // print out if vehicles loaded properly
	        } catch (IOException e) { // catch error for reading vehicles
	            System.out.println("Error reading vehicles.txt: " + e.getMessage());
	        } catch (NumberFormatException e) { // catch error for parsing the data
	            System.out.println("Error parsing vehicle data: " + e.getMessage());
	        }
        }
    
        
        // customers
    	// initiate customers text file
        File file2 = new File("customers.txt");
    	// checks for file existing
        if (!file2.exists()) {
            System.out.println("customers.txt not found. Starting with an empty customer list.");
        }
        else {
        	
	        
	        // attempts to read file
	        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
	            String line2;
	            //loops through each line of the file until a null line, aka empty line
	            while ((line2 = reader.readLine()) != null) {
	                // chunk the line up via the the |, which separates the values in the text files
	                String[] parts = line2.split(" \\| ");
	                if (parts.length < 2) continue;  // Ensure valid format and not too many values
	                // parse id to an int from a string and remove the text
	                int customerId = Integer.parseInt(parts[0].replace("Customer ID: ", "").trim());
	                // remove the text from the name
	                String name = parts[1].replace("Name: ", "").trim();
	
	                // Create customer and add to the list
	                Customer customer = new Customer(customerId, name);
	                customers.add(customer);
	            }
	            // print out that it's loaded properly
	            System.out.println("Customers loaded successfully.");
	        } catch (IOException e) { // catches reading errors
	            System.out.println("Error reading customers.txt: " + e.getMessage());
	        } catch (NumberFormatException e) { // catches parsing errors
	            System.out.println("Error parsing customer data: " + e.getMessage());
	        }
        }
        
        File file3 = new File("rental_records.txt");
        
        if (!file3.exists()) {
            System.out.println("rental_records.txt not found. Starting with an empty rental records list.");
        }
        else {
	        // rental history
	    	// initiate customers text file
	
	        // check for file existing
	        try (BufferedReader br = new BufferedReader(new FileReader(file3))) {
	            String line3;
	            // attempts to read file
	
	            while ((line3 = br.readLine()) != null) {
	                // chunk the line up via the the |, which separates the values in the text files
	                String[] parts = line3.split(" \\| ");  // Split by " | "
	                // extract the individual values
	                String rentalType = parts[0].trim();  // RENT or RETURN
	                String licensePlate = parts[1].split(":")[1].trim();
	                String customerName = parts[2].split(":")[1].trim();
	                String dateStr = parts[3].split(":")[1].trim();
	                String amountStr = parts[4].split(":")[1].trim();
	                
	                // Parse the date and amount to get rid of unnecessary values and just the specific items
	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	                LocalDate date = LocalDate.parse(dateStr, formatter);
	                double amount = Double.parseDouble(amountStr.replace("$", "").trim());
	
	                // Get vehicle and customer objects
	                Vehicle vehicle = rentalSystem.findVehicleByPlate(licensePlate);  
	                Customer customer = rentalSystem.findCustomerByName(customerName); 
	
	                // Create a RentalRecord object based on the parsed data
	                RentalRecord record = new RentalRecord(vehicle, customer, date, amount, rentalType);
	                
	                // Add the record to rental history
	                rentalHistory.addRecord(record);
	            }
	            System.out.println("Rental Records loaded successfully.");
	        } catch (IOException e) { // catches reading errors
	            System.out.println("Error reading rental_records.txt: " + e.getMessage());
	        } catch (NumberFormatException e) { // catches parsing errors
	            System.out.println("Error parsing rentalrecord data: " + e.getMessage());
	        }
        }
    }
}