import java.util.*;
import java.time.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a flight reservation system that allows users to search for
 * flights and make reservations.
 */
public class FlightReservationSystem {

    private static FlightReservationSystem instance;
    private List<Flight> flights = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void setreservations(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public List<Reservation> getreservations() {
        return this.reservations;
    }

    FlightReservationSystem() {
        flights.add(new Flight("INDIGO1", "NAG", "BOM", "INDIGO AIRLINE", "2023-09-19"));
        flights.add(new Flight("AIR1", "HYD", "BEN", "AIR INDIA", "2023-09-19"));
        flights.add(new Flight("AKASA1", "DEL", "KOC", "AKASA AIRLINE", "2023-09-19"));
        flights.add(new Flight("SP1", "KOL", "AHM", "SPICEJET", "2023-09-19"));
    }

    public static FlightReservationSystem getInstance() {
        if (instance == null) {
            instance = new FlightReservationSystem();
        }
        return instance;
    }

    /**
     * Searches for available flights based on the specified criteria.
     * s
     * 
     * @param origin        The departure airport code.
     * @param destination   The arrival airport code.
     * @param departureDate The date of departure (YYYY-MM-DD).
     * @param returnDate    The date of return (YYYY-MM-DD, optional).
     * @param passengers    The number of passengers (adults, children, infants).
     * @param travelClass   The class of service (e.g., Economy, Business).
     * @return A list of available flight options.
     */
    public List<Flight> searchFlights(String origin, String destination, String departureDate,
            String returnDate, int passengers, String travelClass) {
        List<Flight> searchResult = new ArrayList<>();
        for (Flight flight : flights) {
            // Check if the flight matches the origin and destination
            if (flight.getDepartureAirportCode().equals(origin)
                    && flight.getArrivalAirportCode().equals(destination)) {
                LocalDate flightDepartureDate = flight.getDepartureDate();
                // LocalDateTime flightArrDateTime = flight.getArrivalDate();
                if (flightDepartureDate.equals(flight.getDepartureDate())) {
                    Map<String, Integer> availableSeats = flight.getAvailableSeatsByClass();
                    if (availableSeats.containsKey(travelClass) && availableSeats.get(travelClass) >= passengers) {
                        searchResult.add(flight);
                    }
                }
                // Check if there are enough available seats in the desired travel class

                // }

            }
        }
        // Return a list of Flight objects matching the criteria
        return searchResult;
    }

    /**
     * Makes a flight reservation for the specified flight and passengers.
     *
     * @param flightId    The unique identifier for the selected flight.
     * @param passengers  The list of passenger details.
     * @param paymentInfo The payment information (credit card details).
     * @return A reservation confirmation.
     */
    public Reservation makeReservation(String flightId, String classType, List<Passenger> passengers,
            PaymentInfo paymentInfo) {
        // Implement reservation logic here
        // Return a reservation confirmation
        Flight tempFlight = null;
        for (Flight flight : flights) {
            if (flight.getFlightId().equals(flightId)) {
                tempFlight = flight;
                break;
            }
        }

        if (tempFlight != null) {
            // Check if there are enough available seats in the desired travel class
            Map<String, Integer> availableSeats = tempFlight.getAvailableSeatsByClass();
            if (availableSeats.containsKey(classType) && availableSeats.get(classType) >= passengers.size()) {
                // Reduce the available seats
                availableSeats.put(classType, availableSeats.get(classType) - passengers.size());
                // Create a reservation
                Reservation reservation = new Reservation(flightId, passengers, paymentInfo);
                setreservations(reservation);
                return reservation;
            } else {
                return null;
            }
        }
        // Reservation failed
        return null;
    }

    public static void main(String[] args) {
        FlightReservationSystem system = getInstance();
        Scanner sc = new Scanner(System.in);
        System.out.println("enter the below stated to serach a flight");
        System.out.println("from :");
        String origin = sc.next();
        System.out.println("to :");
        String destination = sc.next();
        System.out.println("departure date :");
        String departureDate = sc.next();
        System.out.println("return date : ");
        String returnDate = sc.next();
        System.out.println("number of passengers :");
        int passengers = sc.nextInt();
        System.out.println("enter ticket class :");
        String travelClass = sc.next();
        List<Flight> searchResult = system.searchFlights(origin, destination, departureDate, returnDate, passengers,
                travelClass);
        if (searchResult == null) {
            System.out.println("required flight not available");
        } else {
            for (Flight flight : searchResult) {
                System.out.println(flight.getFlightId() + " " + flight.getDepartureAirportCode() + " "
                        + flight.getArrivalAirportCode() + " " + flight.getAirline());
            }
            System.out.println("enter the flight id of your choice :");
            String flightId = sc.next();
            System.out.println("enter pasenger details :");
            List<Passenger> passenger = new ArrayList<Passenger>();
            for (int i = 0; i < passengers; i++) {
                System.out.println("enter name :");
                String Name = sc.next();
                System.out.println("enter age :");
                String Age = sc.next();
                passenger.add(new Passenger(Name, Age));
            }
            System.out.println("enter Payment Details : ");
            System.out.println("enter credit card number :");
            String creditCardNumber = sc.next();
            System.out.println("enter card holder name:");
            String cardHolderName = sc.next();
            System.out.println("enter date of expiry :");
            String expirationDate = sc.next();
            System.out.println("enter PIN code : ");
            String securityCode = sc.next();
            Reservation reservationStatus = system.makeReservation(flightId, travelClass, passenger,
                    new PaymentInfo(creditCardNumber, cardHolderName, expirationDate, securityCode));
            if (reservationStatus == null) {
                System.out.println("reservation unsuccesfull");
            } else {
                System.out
                        .println("reservation succesfull use" + " " + reservationStatus.getReservationId() + " "
                                + "for reference");
            }
        }
    }
}

/**
 * 
 * Represents a flight available for booking.
 */
class Flight {
    /**
     * Gets the unique identifier for the flight.
     *
     * @return The flight's unique identifier.
     */
    private String FlightId;
    private String DepartureAirportCode;
    private String ArrivalAirportCode;
    private String Airline;
    private LocalDateTime ArrivalDate;
    private LocalDate DepartureDate;
    HashMap<String, Integer> seats = new HashMap<>();

    Flight(String FlightId, String DepartureAirportCode, String ArrivalAirportCode, String Airline,
            String DepartureDate) {
        this.FlightId = FlightId;
        this.DepartureAirportCode = DepartureAirportCode;
        this.Airline = Airline;
        this.ArrivalAirportCode = ArrivalAirportCode;
        this.DepartureDate = LocalDate.parse(DepartureDate);
        this.seats.put("Economy", 50);
        this.seats.put("Business", 30);
        this.seats.put("First", 20);
    }

    public String getFlightId() {
        // Implement getter method
        return this.FlightId;
    }

    /**
     * Gets the departure airport code for the flight.
     *
     * @return The departure airport code.
     */
    public String getDepartureAirportCode() {
        // Implement getter method
        return this.DepartureAirportCode;
    }

    /**
     * Gets the arrival airport code for the flight.
     *
     * @return The arrival airport code.
     */
    public String getArrivalAirportCode() {
        // Implement getter method
        return this.ArrivalAirportCode;
    }

    /**
     * Gets the departure date and time for the flight.
     *
     * @return The departure date and time.
     */
    public LocalDate getDepartureDate() {
        // Implement getter method
        return this.DepartureDate;
    }

    /**
     * Gets the arrival date and time for the flight.
     *
     * @return The arrival date and time.
     */
    public LocalDateTime getArrivalDate() {
        // Implement getter method
        return this.ArrivalDate;
    }

    /**
     * Gets the airline name for the flight.
     *
     * @return The airline name.
     */
    public String getAirline() {
        // Implement getter method
        return this.Airline;
    }

    /**
     * Gets the flight's available seats in different travel classes.
     *
     * @return A map where keys are travel class names (e.g., "Economy") and values
     *         are available seat counts.
     */
    public HashMap<String, Integer> getAvailableSeatsByClass() {
        // Implement getter method
        return this.seats;
    }

    // Other flight-related properties and methods
}

/**
 * Represents a passenger with personal information.
 */
class Passenger {

    /**
     * Gets the name of the passenger.
     *
     * @return The passenger's name.
     */
    private String Name;
    private String Age;

    Passenger(String Name, String Age) {
        this.Name = Name;
        this.Age = Age;
    }

    public String getName() {
        // Implement getter method
        return this.Name;
    }

    public String getAge() {
        return this.Age;
    }

    // Other passenger-related properties and methods
}

/**
 * Represents payment information for a reservation.
 */
class PaymentInfo {

    /**
     * Gets the credit card number used for payment.
     *
     * @return The credit card number.
     */
    private String creditCardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String securityCode;

    public PaymentInfo(String creditCardNumber, String cardHolderName, String expirationDate, String securityCode) {
        this.creditCardNumber = creditCardNumber;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    // Other payment-related properties and methods
}

/**
 * Represents a flight reservation confirmation.
 */
class Reservation {

    /**
     * Gets the unique identifier for the reservation.
     *
     * @return The reservation's unique identifier.
     */
    private List<Passenger> passengers;

    private String reservationID;

    private String flightId;

    private PaymentInfo paymentInfo;

    Reservation(String flightId, List<Passenger> passengers, PaymentInfo paymentInfo) {
        this.flightId = flightId;
        this.passengers = passengers;
        this.reservationID = UUID.randomUUID().toString();
        this.paymentInfo = paymentInfo;
    }

    public String getReservationId() {
        // Implement getter method
        return this.reservationID;
    }

    public String getflightId() {
        return this.flightId;
    }

    public List<Passenger> getpassengers() {
        return this.passengers;
    }

    // Other reservation-related properties and methods
}
