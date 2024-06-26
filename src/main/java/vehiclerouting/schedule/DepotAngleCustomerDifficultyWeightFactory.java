/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package vehiclerouting.schedule;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;
import vehiclerouting.domain.Customer;
import vehiclerouting.domain.Depot;
import vehiclerouting.domain.VehicleRoutingSolution;

import java.util.Comparator;

import static java.util.Comparator.*;

/**
 * On large datasets, the constructed solution looks like pizza slices.
 */
public class DepotAngleCustomerDifficultyWeightFactory
        implements SelectionSorterWeightFactory<VehicleRoutingSolution, Customer> {

    @Override
    public DepotAngleCustomerDifficultyWeight createSorterWeight(VehicleRoutingSolution vehicleRoutingSolution,
                                                                 Customer customer) {
        Depot depot = vehicleRoutingSolution.getDepotList().get(0);
        return new DepotAngleCustomerDifficultyWeight(customer,
                customer.getLocation().getAngle(depot.getLocation()),
                customer.getLocation().getDistanceTo(depot.getLocation())
                        + depot.getLocation().getDistanceTo(customer.getLocation()));
    }

    public static class DepotAngleCustomerDifficultyWeight
            implements Comparable<DepotAngleCustomerDifficultyWeight> {

        private static final Comparator<DepotAngleCustomerDifficultyWeight> COMPARATOR = comparingDouble(
                (DepotAngleCustomerDifficultyWeight weight) -> weight.depotAngle)
                .thenComparingLong(weight -> weight.depotRoundTripDistance) // Ascending (further from the depot are more difficult)
                .thenComparing(weight -> weight.customer, comparing(Customer::getId));

        private final Customer customer;
        private final double depotAngle;
        private final long depotRoundTripDistance;

        public DepotAngleCustomerDifficultyWeight(Customer customer,
                                                  double depotAngle, long depotRoundTripDistance) {
            this.customer = customer;
            this.depotAngle = depotAngle;
            this.depotRoundTripDistance = depotRoundTripDistance;
        }

        @Override
        public int compareTo(DepotAngleCustomerDifficultyWeight other) {
            return COMPARATOR.compare(this, other);
        }
    }
}
