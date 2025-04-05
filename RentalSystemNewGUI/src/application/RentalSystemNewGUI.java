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
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Pos;
import javafx.stage.Modality;

public class RentalSystemNewGUI extends Application {

    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
RentalSystem rentalSystem = RentalSystem.getInstance();
FlowPane pane;
Stage primary;

    @Override
    public void start(Stage primaryStage) {
    primary = primaryStage;
    loadData();
        pane = new FlowPane();
        Button showVehiclesButton = new Button("Show Vehicles");
        showVehiclesButton.setOnAction(e -> showVehicles());

        pane.getChildren().addAll(showVehiclesButton);
       
        Scene scene = new Scene(pane, 550, 300);
        primaryStage.setTitle("FX Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Available Vehicles");
        popup.initOwner(primary);
       
        VBox popupVbox = new VBox(50);
        Button close = new Button("Close");
        close.setOnAction(e -> popup.close());
       
        popupVbox.setAlignment(Pos.CENTER);
        popupVbox.getChildren().addAll(tableView,close);
       
        Scene popupScene = new Scene(popupVbox, 600, 300);
        popup.setScene(popupScene);
        popup.showAndWait();
       
    }
   
    private void loadData() {
    //vehicles
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