import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter; 
import java.io.IOException;  // Import the IOException class to handle errors
import java.time.format.DateTimeFormatter;


public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    //Singleton Implementation - 1
    private static RentalSystem instance;

    private RentalSystem() {
    	loadData();
    }

    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }
    //-------------------------------------  
    
    
    // Save Methods - 2
    // save vehicle method, try and catch to attempt to add vehicles to text file
    public void saveVehicle(Vehicle vehicle) {
		try (FileWriter myWriter = new FileWriter("vehicles.txt", true)){
			String vehicleEntry = vehicle.getInfo() + "\n";
			vehicleEntry = vehicleEntry.substring(2);
			myWriter.write(vehicleEntry);
			myWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    // save customer method, try and catch to attempt to add customers to text file
    public void saveCustomer(Customer customer) {
		try (FileWriter myWriter = new FileWriter("customers.txt", true)){
			myWriter.write(customer.toString() + "\n");
			myWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    // save record method, try and catch to attempt to add vehicles to text file
    public void saveRecord(RentalRecord record) {
    	try (FileWriter myWriter = new FileWriter("rental_records.txt", true)){
    		myWriter.write(record.toString() + "\n");
    		myWriter.flush();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
			e.printStackTrace();
    	}
    }
    // --------------------------------
    
    
    // loadData method ----------------------
    private void loadData() {
    	//vehicles
    	// initiate vehicle text file
    	File file = new File("vehicles.txt");
    	// checks for file existing
        if (!file.exists()) {
            System.out.println("vehicles.txt not found. Starting with an empty list.");
            }
        //attepmts to read the file
        else {
        	try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                //loops through each line of the file until a null line, aka empty line
                while ((line = reader.readLine()) != null) {
                    // chunk the line up via the the |, which separates the values in the text files
                    String[] parts = line.split(" \\| ");
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
        // attempts to read file
        else {
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
        // rental history
        File file3 = new File("rental_records.txt");
    	// initiate customers text file
        if (!file3.exists()) {
            System.out.println("rental_records.txt not found. Starting with an empty rentalRecord list.");
            
        }
        else {
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
                    Vehicle vehicle = findVehicleByPlate(licensePlate);  
                    Customer customer = findCustomerByName(customerName); 

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
        // check for file existing
        }
        
    }
    
    public boolean addVehicle(Vehicle vehicle) { // changed to boolean and checks for an existing license plate vehicle
        if (findVehicleByPlate(vehicle.getLicensePlate()) != null)
        {
        	System.out.println("ERROR: A vehicle with this license plate already exists.");
        	return false;
        }
        vehicles.add(vehicle);
        saveVehicle(vehicle);
        return true;
        
        
    }

    public boolean addCustomer(Customer customer) { // changed to bool and checks for an existing id from a customer
    	if (findCustomerById(customer.getCustomerId()) != null) {
    		System.out.println("ERROR: A Customer with that ID already exists");
    		return false;
    	}
        customers.add(customer);
        saveCustomer(customer);
        return true;
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            
            // calling rental save by adjusting how its made then calling the save record method
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(record);
            saveRecord(record);        
            //-------------
            
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            
            // calling rental save by adjusting how its made then calling the save record method
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(record);
            saveRecord(record);
            //-----------------
            
            
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayAvailableVehicles() {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllVehicles() {
        for (Vehicle v : vehicles) {
            System.out.println("  " + v.getInfo());
        }
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }

    public Customer findCustomerByName(String name) {
        for (Customer c : customers)
            if (c.getCustomerName().equalsIgnoreCase(name))
                return c;
        return null;
    }
}