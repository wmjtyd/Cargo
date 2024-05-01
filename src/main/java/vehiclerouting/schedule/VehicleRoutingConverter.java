package vehiclerouting.schedule;

import vehiclerouting.domain.Customer;
import vehiclerouting.domain.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleRoutingConverter {

//    public static void main(String[] args) {
//        // Example vehicle and customers setup, replace with your actual list initialization
//        List<Vehicle> vehicleList = new ArrayList<>();
//
//        Vehicle vehicle = new Vehicle("3", 5, new Depot("25", new Location(93.9154, 1.2850)));
//        Customer customer29 = new Customer("29", new Location(99.8878, 28.1426), 13);
//        Customer customer31 = new Customer("31", new Location(85.9242, 34.3687), 13);
//        Customer customer17 = new Customer("17", new Location(81.0901, 55.5466), 10);
//
//        // Setting next customer links
//        customer29.setNextCustomer(customer31);
//        customer31.setNextCustomer(customer17);
//
//        List<Customer> customers = new ArrayList<>();
//        customers.add(customer29);  // Add the head of the linked list
//        vehicle.setCustomers(customers);
//
//        vehicleList.add(vehicle);
//
//        Solution solution = convertToSolution(vehicleList);
//        printSolution(solution);
//    }

    public static Solution convertToSolution(List<Vehicle> vehicleList) {
        List<Plan> plans = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            List<Path> path = followCustomerChain(vehicle.getCustomers());
            Location location = new Location(vehicle.getLocation().getLatitude(), vehicle.getLocation().getLongitude());
            Car car = new Car(vehicle.getId(), vehicle.getCapacity(), location);
            plans.add(new Plan(car, path));
        }
        Solution solution = new Solution();
        solution.setPlans(plans);
        return solution;
    }

    private static List<Path> followCustomerChain(List<Customer> headCustomers) {
        List<Path> paths = new ArrayList<>();
        for (Customer head : headCustomers) {
            Location location = new Location(head.getLocation().getLatitude(), head.getLocation().getLongitude());
            Path node = new Path(head.getId(), location, head.getDemand());
            paths.add(node);
        }
        return paths;
    }

    public static void printSolution(Solution solution) {
        for (Plan plan : solution.getPlans()) {
            System.out.println("Vehicle ID: " + plan.getCar().getCarId() + " with capacity: " + plan.getCar().getCapacity());
            for (Path customer : plan.getPath()) {
                System.out.println("  -> Customer ID: " + customer.getOrderId() + " at Location: " + customer.getLocation().getLatitude() + "," + customer.getLocation().getLongitude());
            }
        }
    }
}

// Supporting classes assumed to be the same as provided earlier