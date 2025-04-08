package application;



//libraries imported
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.geometry.Insets; 

public class RentalSystemNewGUI extends Application {
	
	//Class variables including a list of vehicles and customers, the rental history and a singleton RentalSystem
	
  private List<Vehicle> vehicles;
  private List<Customer> customers;
  private RentalHistory rentalHistory;
	RentalSystem rentalSystem = RentalSystem.getInstance();
	
	//GUI components
	GridPane pane;
	Stage primary;
  Label statusLabel = new Label("");

  //start method to set up the GUI
  @Override
  public void start(Stage primaryStage) {
  	
  	primary = primaryStage;
  	
  	//populate the lists and rental history from files
  	loadData();
  	
  	//general status label
  	statusLabel.setTextFill(Color.WHITE);
  	statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
  	
  	//Grid pane for all buttons
      pane = new GridPane();
      pane.setMinSize(550, 300); 
      pane.setPadding(new Insets(10, 10, 10, 10)); 

      pane.setAlignment(Pos.CENTER);
      pane.setVgap(5); 
      pane.setHgap(5); 
      
      //Configuring menu items - also able to do these operations through the buttons but wantd to do both
      //each is connected to a method 
      MenuItem menuItem1 = new MenuItem("Add Vehicle");
      menuItem1.setOnAction( action -> { addVehicle(); });
             
      MenuItem menuItem2 = new MenuItem("Add Customer");
      menuItem2.setOnAction( action -> { addCustomer(); });
      
      //add menu items to menu1
      Menu menu1 = new Menu("Add");
      menu1.getItems().add(menuItem1);
      menu1.getItems().add(menuItem2);
      
      MenuItem menuItem3 = new MenuItem("Rent Vehicle");
      menuItem3.setOnAction( action -> { rentVehicle(); });
      
      MenuItem menuItem4 = new MenuItem("Return Vehicle");
      menuItem4.setOnAction( action -> { returnVehicle(); });
              
    //add menu items to menu2
      Menu menu2 = new Menu("Rentals");
      menu2.getItems().add(menuItem3);
      menu2.getItems().add(menuItem4);
      
      MenuItem menuItem5 = new MenuItem("All Vehicles");
      menuItem5.setOnAction( action -> { displayVehicles(false); });

      MenuItem menuItem6 = new MenuItem("Available Vehicles");
      menuItem6.setOnAction( action -> { displayVehicles(true); });
      
      MenuItem menuItem7 = new MenuItem("Customers");
      menuItem7.setOnAction( action -> { displayCustomers(); });
      
      MenuItem menuItem8 = new MenuItem("Rental Records");
      menuItem8.setOnAction( action -> { showRentalRecords(); });
      
      //add menu items to menu3
      Menu menu3 = new Menu("Show");
      menu3.getItems().add(menuItem5);
      menu3.getItems().add(menuItem6);
      menu3.getItems().add(menuItem7);
      menu3.getItems().add(menuItem8);
      
      //create the menu bar
      MenuBar menuBar = new MenuBar();
      menuBar.getMenus().add(menu1);
      menuBar.getMenus().add(menu2);
      menuBar.getMenus().add(menu3);
      
      //add all the buttons, each connected to a method
      Button addVehicleButton = new Button("Add Vehicle");
      addVehicleButton.setOnAction(e -> addVehicle());
      addVehicleButton.setMaxSize(150, 50);
      addVehicleButton.setMinSize(150, 50);

      Button addCustomerButton = new Button("Add Customer");
      addCustomerButton.setOnAction(e -> addCustomer());
      addCustomerButton.setMaxSize(150, 50);
      addCustomerButton.setMinSize(150, 50);
      
      Button showAllVehiclesButton = new Button("Show All Vehicles");
      showAllVehiclesButton.setOnAction(e -> displayVehicles(false));
      showAllVehiclesButton.setMaxSize(150, 50);
      showAllVehiclesButton.setMinSize(150, 50);
      
      Button showAvailableVehiclesButton = new Button("Show Available Vehicles");
      showAvailableVehiclesButton.setOnAction(e -> displayVehicles(true));
      showAvailableVehiclesButton.setMaxSize(200, 50);
      showAvailableVehiclesButton.setMinSize(200, 50);
      
      Button showRentalHistoryButton = new Button("Show Rental History");
      showRentalHistoryButton.setOnAction(e -> showRentalRecords());
      showRentalHistoryButton.setMaxSize(200, 50);
      showRentalHistoryButton.setMinSize(200, 50);
      
      Button showCustomersButton = new Button("Show Customers");
      showCustomersButton.setOnAction(e -> displayCustomers());
      showCustomersButton.setMaxSize(150, 50);
      showCustomersButton.setMinSize(150, 50);
      
      Button showRentButton = new Button("Rent Vehicle");
      showRentButton.setOnAction(e -> rentVehicle());
      showRentButton.setMaxSize(150, 50);
      showRentButton.setMinSize(150, 50);
      
      Button showReturnButton = new Button("Return Vehicle");
      showReturnButton.setOnAction(e -> returnVehicle());
      showReturnButton.setMaxSize(150, 50);
      showReturnButton.setMinSize(150, 50);
      
      //add all peices to the grid pane
      pane.add(addCustomerButton, 0, 1);
      pane.add(showCustomersButton, 1, 1);
      pane.add(addVehicleButton, 0,2);
      pane.add(showAllVehiclesButton, 1,2);
      pane.add(showAvailableVehiclesButton, 2,2);
      pane.add(showRentButton, 0, 3);
      pane.add(showReturnButton, 1, 3);
      pane.add(showRentalHistoryButton, 2,3);
      pane.add(statusLabel, 1,4,2, 1);
      
      pane.setStyle("-fx-background-color: mediumslateblue");
      
      //overall structure is a VBox with the menuBar in the top and the grid pane in the bottom
      VBox vBox = new VBox(menuBar,pane);
      Scene scene = new Scene(vBox, 550, 300);

      primaryStage.setTitle("Rental System GUI");
      primaryStage.setScene(scene);
      primaryStage.show();
  }

  //main method
  public static void main(String[] args) {
      launch(args);
  }
  
  //add vehicle method, add a vehicle to the list of vehicles with user input
  public void addVehicle() { 
  	
  	//set up a status label for errors
  	statusLabel.setText("");
      
  	//create a popup window for getting vehicle properties
  	final Stage popup = new Stage();
      Label featureLabel = new Label("");
      TextField feature = new TextField();
      Label errorLabel = new Label("");
      
      //set up the combobox for picking the vehicle type
      ComboBox<String> comboBox = new ComboBox<String>();
  	Label type = new Label("Type");
      comboBox.getItems().add("1: Car");
      comboBox.getItems().add("2: Motorcycle");
      comboBox.getItems().add("3: Truck");
      
      //when an item is selected in the combobox, set the feature specifc to each type of vehicle
      comboBox.setOnAction((event) -> {
      	errorLabel.setText("");
          if( comboBox.getValue().equals("1: Car")) {
          	featureLabel.setText("Feature: Number of Seats");
          }
          else if( comboBox.getValue().equals("2: Motorcycle")) {
          	featureLabel.setText("Feature: Has side car: Y or N");
          }
          else if( comboBox.getValue().equals("3: Truck")) {
          	featureLabel.setText("Feature: Cargo Capacity");
          }
          else {
          	featureLabel.setText("");
          }
      });
      
      //set up the input fields
  	Label licenseLabel = new Label("License");
      TextField license = new TextField("ABC123");
  	Label makeLabel = new Label("Make");
      TextField make = new TextField();
  	Label modelLabel = new Label("Model");
      TextField model = new TextField();
  	Label yearLabel = new Label("Year");
      TextField year = new TextField();
     
      //add vehicle button setup
      Button add = new Button("Add Vehicle");
      
      //set up the button so that it only tries to add a vehicle if all the fields are filled in
      add.setOnAction(e -> {
      	errorLabel.setText("");
      	if(!license.getText().isEmpty() && 
      		!make.getText().isEmpty() &&
      		!model.getText().isEmpty() &&
      		!year.getText().isEmpty() &&
      		!comboBox.getSelectionModel().isEmpty()) {
      		
      		//ensure parseInt error is handled correctly when doing String to int
      		try {
          		int vehicleYear = Integer.parseInt(year.getText());
      	        Vehicle v = null;
      	        
      	        //handle the feature in the right way depending on the type selected in combobox
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
	        		
	        		//if we have created a vehicle, set the license plate and then add the vehicle
	        		if(v != null) {
	        			v.setLicensePlate(license.getText());
	        			//if successfully added, reload the car data from file and give message on main screen
	        			if( rentalSystem.addVehicle(v)) {
	        		    	loadData();
	        		        statusLabel.setText("Vehicle Added!");
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
      	else {
      		errorLabel.setText("Error adding vehicle - Please fill all feilds");
      	}
      });
      
      //adding a close button
      Button close = new Button("Close");
      close.setOnAction(e -> { popup.close(); });
      
      //NONE ensures we can pop up multiple windows at the same time to look at other data while doing things
      popup.initModality(Modality.NONE);
      popup.setTitle("Add Vehicle");
      popup.initOwner(primary);
      
      //everythnig gets added to the grid pane
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
      popupGridPane.add(errorLabel, 0, 7, 2, 1);

      //display the grid pane and show the popup
      Scene popupScene = new Scene(popupGridPane, 400, 450);
      popup.setScene(popupScene);
      popup.show();
  }
  
  //add customer method, get all customer info and try to add it
  public void addCustomer() { 
  	
  	statusLabel.setText("");
      final Stage popup = new Stage();

      //set up GUI parts
      Label errorLabel = new Label("");
  	Label idLabel = new Label("Customer ID");
      TextField id = new TextField();
  	Label nameLabel = new Label("Name");
      TextField name = new TextField();
  
      //add an add customer button and check if all fields are filled out before creating a customer
  	Button add = new Button("Add Customer");
      add.setOnAction(e -> {
      	errorLabel.setText("");
      	if(!id.getText().isEmpty() && 
      		!name.getText().isEmpty()) {
      		//try catch here to handle parseInt failure if it happens
      		try {
          		int idNumber = Integer.parseInt(id.getText());
      	        Customer c = null;
      	        c = new Customer(idNumber, name.getText());
      	        if(c != null) {
      	        	//if adding customer works, send a message to main GUI window and reload the data
	        			if( rentalSystem.addCustomer(c)) {
	        				statusLabel.setText("Customer Added!");
	        		    	loadData();
	        				popup.close();
	        			}
	        			else {
	        				errorLabel.setText("Error adding customer - customer id already used");
	        			}
	        		}
      		}
              catch (NumberFormatException ex){
  	            errorLabel.setText("Customer id is not a number!");
  	        }
      	}
      	else {
      		errorLabel.setText("Error adding customer - Please fill all fields");
      	}
      });
      
      //set up a close button
      Button close = new Button("Close");
      close.setOnAction(e -> { popup.close();});
      
      popup.initModality(Modality.NONE);
      popup.setTitle("Add Customer");
      popup.initOwner(primary);
      
      //add all parts to the grid pane
      GridPane popupGridPane = new GridPane();
      popupGridPane.setMinSize(500, 600); 
      popupGridPane.setPadding(new Insets(10, 10, 10, 10)); 

      popupGridPane.setAlignment(Pos.CENTER);
      popupGridPane.setVgap(5); 
      popupGridPane.setHgap(5); 
      popupGridPane.setStyle("-fx-background-color: lightblue");
      
      popupGridPane.add(idLabel,0,0);
      popupGridPane.add(id,1,0);
      popupGridPane.add(nameLabel,0,1);
      popupGridPane.add(name,1,1);
      popupGridPane.add(add,0,2);
      popupGridPane.add(close,1,2);
      popupGridPane.add(errorLabel, 0, 3, 2, 1);

      Scene popupScene = new Scene(popupGridPane, 500, 550);
      popup.setScene(popupScene);
      popup.show();
  }
  
  //rent vehicle method, 
  public void rentVehicle() { 
  	
		statusLabel.setText("");
      final Stage popup = new Stage();
      
      //set up the popup window parts
      Label errorLabel = new Label("");
  	Label amountLabel = new Label("Rental Amount");
      TextField amount = new TextField();
      
      //get the vehicle and customer info to display
      TableView<Vehicle> vehicleTableView = getVehicleTableView(true);
      TableView<Customer> customerTableView = getCustomerTableView();
  
      //add a rent button and only process the rent action if all fields are not empty
  	Button rent = new Button("Rent Vehicle");
      rent.setOnAction(e -> {
      	errorLabel.setText("");
      	if(!amount.getText().isEmpty() && 
      		!vehicleTableView.getSelectionModel().isEmpty() &&
      		!customerTableView.getSelectionModel().isEmpty()) {
      		//try catch to make sure parseDouble is handled correctly
      		try {
          		double amountInDollars = Double.parseDouble(amount.getText());
          		//get the selected vehicle and customer
          		Vehicle v = vehicleTableView.getSelectionModel().getSelectedItem();
          		Customer c = customerTableView.getSelectionModel().getSelectedItem();
          		rentalSystem.rentVehicle(v, c, LocalDate.now(), amountInDollars);
          		statusLabel.setText("Vehicle Rented!");
          		loadData();
          		popup.close();
          		
      		}
              catch (NumberFormatException ex){
  	            errorLabel.setText("Amount is not a number!");
  	        }
      	}
      	else {
      		errorLabel.setText("Error renting vehicle - Please fill in an amount and select a customer and vehicle");
      	}
      });
      
      //set up the rest of the popup components on the grid plane
      Button close = new Button("Close");
      close.setOnAction(e -> { popup.close(); });
      
      popup.initModality(Modality.NONE);
      popup.setTitle("Rent Vehicle");
      popup.initOwner(primary);
      
      GridPane popupGridPane = new GridPane();
      popupGridPane.setMinSize(500, 600); 
      popupGridPane.setPadding(new Insets(10, 10, 10, 10)); 

      popupGridPane.setAlignment(Pos.CENTER);
      popupGridPane.setVgap(5); 
      popupGridPane.setHgap(5); 
      popupGridPane.setStyle("-fx-background-color: lightblue");
      
      popupGridPane.add(vehicleTableView, 0, 0, 2, 1);
      popupGridPane.add(customerTableView, 0,1, 2, 1);
      popupGridPane.add(amountLabel,0,2);
      popupGridPane.add(amount,1,2);
      popupGridPane.add(rent, 0, 3);
      popupGridPane.add(close,1,3);
      popupGridPane.add(errorLabel, 0, 4, 2, 2);

      Scene popupScene = new Scene(popupGridPane, 500, 550);
      popup.setScene(popupScene);
      popup.show();
  }

  //return vehicle method, checks if the vehicle is actually rented and returns it with a fee if needed
	public void returnVehicle() { 
		
		//setup popup parts
		statusLabel.setText("");
	    final Stage popup = new Stage();
	
	    Label errorLabel = new Label("");
		Label amountLabel = new Label("Return Fees");
	    TextField amount = new TextField();
	    
	    //get all vehicles into a table view
	    TableView<Vehicle> vehicleTableView = getVehicleTableView(false);
	    //then eliminate the ones that dont have RENTED status
		vehicleTableView.getItems().removeIf(element -> element.getStatus() != Vehicle.VehicleStatus.RENTED);

		//get all the customers
	    TableView<Customer> customerTableView = getCustomerTableView();
	
	    //set up the button for return, only return if all fields are filled out.
		Button returnVehicle = new Button("Return Vehicle");
	    returnVehicle.setOnAction(e -> {
	    	errorLabel.setText("");
	    	if(!amount.getText().isEmpty() && 
	    		!vehicleTableView.getSelectionModel().isEmpty() &&
	    		!customerTableView.getSelectionModel().isEmpty()) {
	    		//try catch for the parseDouble so it handles errors correctly
	    		try {
	        		double amountInDollars = Double.parseDouble(amount.getText());
	        		Vehicle v = vehicleTableView.getSelectionModel().getSelectedItem();
	        		Customer c = customerTableView.getSelectionModel().getSelectedItem();
	        		rentalSystem.returnVehicle(v, c, LocalDate.now(), amountInDollars);
	        		statusLabel.setText("Vehicle Returned!");
	        		loadData();
	        		popup.close();
	        		
	    		}
	            catch (NumberFormatException ex){
		            errorLabel.setText("Amount is not a number!");
		        }
	    	}
	    	else {
	    		errorLabel.setText("Error renting vehicle - Please fill in an amount and select a customer and vehicle");
	    	}
	    });
	    
	    //set up the rest of the window components and put them in the popup window
	    Button close = new Button("Close");
	    close.setOnAction(e -> { popup.close(); });
	    
	    popup.initModality(Modality.NONE);
	    popup.setTitle("Return Vehicle");
	    popup.initOwner(primary);
	    
	    GridPane popupGridPane = new GridPane();
	    popupGridPane.setMinSize(500, 600); 
	    popupGridPane.setPadding(new Insets(10, 10, 10, 10)); 
	
	    popupGridPane.setAlignment(Pos.CENTER);
	    popupGridPane.setVgap(5); 
	    popupGridPane.setHgap(5); 
	    popupGridPane.setStyle("-fx-background-color: lightblue");
	    
	    popupGridPane.add(vehicleTableView, 0, 0, 2, 1);
	    popupGridPane.add(customerTableView, 0,1, 2, 1);
	    popupGridPane.add(amountLabel,0,2);
	    popupGridPane.add(amount,1,2);
	    popupGridPane.add(returnVehicle, 0, 3);
	    popupGridPane.add(close,1,3);
	    popupGridPane.add(errorLabel, 0, 4, 2, 2);
	
	    Scene popupScene = new Scene(popupGridPane, 500, 550);
	    popup.setScene(popupScene);
	    popup.show();
	}

	//method to get the vehicle data into a Table view, used twice
	//method can show all vehicles (false) or only available vehicles (true)
	private TableView<Vehicle> getVehicleTableView(boolean showOnlyAvailable)
	{
		TableView<Vehicle> tableView = new TableView<Vehicle>();
		
		//set up the table columns with the property values to get the data from the Vehicle class
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

      //loop through all vehicles and only show available ones if parameter is true
      for( Vehicle v : vehicles ) {
      	if(showOnlyAvailable == true) {
      		if( v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
      			tableView.getItems().add(v);
      		}
      		//remove previously added vehicle if it now shows as rented.  This fixes a bug 
      		//in saveVehicle which only uses append
      		else {
      			tableView.getItems().removeIf(element -> element.getLicensePlate().equals(v.getLicensePlate()));
      		}
      	}
      	else {
      		//only show the most recent entry for a vehicle
      		//fixes a bug in saveVehicle which only uses append
  			tableView.getItems().removeIf(element -> element.getLicensePlate().equals(v.getLicensePlate()));
      		tableView.getItems().add(v);
      	}
      	//get rid of duplicate entries
      }
      return tableView;
	}
  
	//method to displayVehicles, pass in true to show available only, false to show all
  private void displayVehicles(boolean showOnlyAvailable)
  {
  	statusLabel.setText("");
  	TableView<Vehicle> tableView = getVehicleTableView(showOnlyAvailable);
      
  	//set up the popup window with vehicles and a close button
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
  
  //method to get a tableview of all customers, used twice so made it a method
  private TableView<Customer> getCustomerTableView()
  {
  	TableView<Customer> tableView = new TableView<Customer>();

  	//set up the table columns with the property values from the Customer class
      TableColumn<Customer, Number> column1 = new TableColumn<>("Customer ID");
      column1.setCellValueFactory(new PropertyValueFactory<>("customerId"));

      TableColumn<Customer, String> column2 = new TableColumn<>("Name");
      column2.setCellValueFactory(new PropertyValueFactory<>("customerName"));
      
      tableView.getColumns().add(column1);
      tableView.getColumns().add(column2);
    
      //loop through all customers and add them to the tableview
      for( Customer c : customers ) {
      	tableView.getItems().add(c);
      }
      return tableView;
  }
  
  //method to display all customers
  private void displayCustomers()
  {
  	statusLabel.setText("");
  	
  	//get the customers into a tableview
      TableView<Customer> tableView = getCustomerTableView();
      
      //set up the popup window with the table view and a button
      final Stage popup = new Stage();
      popup.initModality(Modality.NONE);
      popup.setTitle("Current Customers");
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
  
  //method to show all the rental records
  private void showRentalRecords()
  {
  	statusLabel.setText("");
  	List<RentalRecord> records = rentalHistory.getRentalHistory();
  	
  	//using listview as RentalRecords has nested classes and using tableView was super complex
  	//could be improved with tableView
  	
  	ListView<String> listView = new ListView<String>();
     
  	//add the records as strings to the list view
      for( RentalRecord r : records ) {
      	listView.getItems().add(r.toString());
      }
      
      //set up the popup window with the list and a button
      final Stage popup = new Stage();
      popup.initModality(Modality.NONE);
      popup.setTitle("Rental Records");
      popup.initOwner(primary);
      
      VBox popupVbox = new VBox(50);
      Button close = new Button("Close");
      close.setOnAction(e -> popup.close());
      
      popupVbox.setAlignment(Pos.CENTER);
      popupVbox.getChildren().addAll(listView,close);
      popupVbox.setStyle("-fx-background-color: lightblue");
     
      Scene popupScene = new Scene(popupVbox, 600, 450);
      popup.setScene(popupScene);
      popup.show();
      
  }
  
  //method to load the data from the text files
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