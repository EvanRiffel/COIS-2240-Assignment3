import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleRentalTest {

	private Vehicle car1;
    private Vehicle car2;
    private Vehicle car3;

    @BeforeEach
    public void setUp() {
        car1 = new Car("Toyota", "Corolla", 2020, 4);
        car2 = new Car("Honda", "Civic", 2021, 5);
        car3 = new Car("Ford", "Mustang", 2022, 2);
    }

    // Testing valid license plates
    @Test
    public void testValidLicensePlateAAA100() {
        assertDoesNotThrow(() -> car1.setLicensePlate("AAA100"));
    }

    @Test
    public void testValidLicensePlateABC567() {
        assertDoesNotThrow(() -> car2.setLicensePlate("ABC567"));
    }

    @Test
    public void testValidLicensePlateZZZ999() {
        assertDoesNotThrow(() -> car3.setLicensePlate("ZZZ999"));
    }

    // Testing invalid license plates
    @Test
    public void testInvalidLicensePlateEmptyString() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> car1.setLicensePlate(""));
        assertEquals("incorrect license plate format. needs 3 letters, 3 numbers", exception.getMessage());
    }

    @Test
    public void testInvalidLicensePlateNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> car2.setLicensePlate(null));
        assertEquals("incorrect license plate format. needs 3 letters, 3 numbers", exception.getMessage());
    }

    @Test
    public void testInvalidLicensePlateTooManyDigits() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> car3.setLicensePlate("AAA1000"));
        assertEquals("incorrect license plate format. needs 3 letters, 3 numbers", exception.getMessage());
    }

    @Test
    public void testInvalidLicensePlateTooFewDigits() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> car2.setLicensePlate("ZZZ99"));
        assertEquals("incorrect license plate format. needs 3 letters, 3 numbers", exception.getMessage());
    }

    
    // part 2----------------------------------------
}
