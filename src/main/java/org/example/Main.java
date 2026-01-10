package org.example;

import org.example.dao.*;
import org.example.dto.*;
import org.example.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final String PAYMENTS_FILE = "payments.csv";

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String cmd = sc.nextLine().trim();

            try {
                switch (cmd) {
                    case "0" -> CompanyCrud();
                    case "1" -> EmployeesCrud();
                    case "2" -> BuildingCrud();
                    case "3" -> ApartmentCrud();
                    case "4" -> OwnerCrud();
                    case "5" -> ResidentCrud();
                    case "6" -> Assignments();
                    case "7" -> setFees();
                    case "8" -> createPayment();
                    case "9" -> FilterSort();
                    case "10" -> Reports();
                    case "11" -> PaymentExport();
                    case "12" -> PaymentStatus();
                    case "13" -> { return; }
                    default -> System.out.println("Unknown command.");
                }
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }

            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("0 - Company");
        System.out.println("1 - Employee");
        System.out.println("2 - Building");
        System.out.println("3 - Apartment");
        System.out.println("4 - Owner");
        System.out.println("5 - Resident");
        System.out.println("6 - Building assignment rules");
        System.out.println("7 - Set fees for building");
        System.out.println("8 - Pay fees");
        System.out.println("9 - Filter and sort");
        System.out.println("10 - Reports (counts/lists/sums)");
        System.out.println("11 - Export payment line to file");
        System.out.println("12 - Check payment status");
        System.out.println("13 - Exit");
        System.out.println();
    }

    private static void CompanyCrud() {
        System.out.println("All companies:");
        CompanyDao.getCompanies().forEach(System.out::println);
        System.out.println("Create (1), update (2) or delete company (3)?");
        String cmd1 = sc.nextLine().trim();

        try {
            switch (cmd1) {
                case "1" -> {
                    String name = readString("Company name: ");
                    CompanyDao.createCompany(new CompanyDto(0, name));
                }
                case "2" -> {
                    long id = readLong("Company id: ");
                    String newName = readString("New name: ");
                    CompanyDao.updateCompany(id, new CompanyDto(0, newName));
                }
                case "3" -> {
                    long id = readLong("Company id: ");
                    CompanyDao.deleteCompany(id);
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        System.out.println("All companies:");
        CompanyDao.getCompanies().forEach(System.out::println);
    }

    private static void BuildingCrud() {
        System.out.println("All buildings:");
        BuildingDao.getBuildings().forEach(System.out::println);
        System.out.println("Create (1), update (2) or delete building (3)?");
        String cmd = sc.nextLine().trim();

        try {
            switch (cmd) {
                case "1" -> {
                    BuildingDto dto = readBuildingDto();
                    BuildingDao.createBuilding(dto);
                }
                case "2" -> {
                    long id = readLong("Building id: ");
                    BuildingDto cur = BuildingDao.getBuilding(id);

                    System.out.println("Change:");
                    System.out.println("1 - name");
                    System.out.println("2 - floors");
                    System.out.println("3 - address");
                    System.out.println("4 - common_areas");
                    System.out.println("5 - total_areas");
                    System.out.println("6 - contract_start_date");
                    System.out.println("7 - fee_per_sqm");
                    System.out.println("8 - fee_per_pet_using_ca");
                    System.out.println("9 - fee_per_person_over_7_using_elevator");
                    System.out.println("10 - company_id");
                    System.out.println("11 - employee_id");
                    String field = sc.nextLine().trim();

                    BuildingDto upd = new BuildingDto(
                            0,
                            cur.getName(),
                            cur.getFloors(),
                            cur.getAddress(),
                            cur.getCommon_areas(),
                            cur.getTotal_areas(),
                            cur.getContract_start_date(),
                            cur.getFee_per_sqm(),
                            cur.getFee_per_pet_using_ca(),
                            cur.getFee_per_person_over_7_using_elevator(),
                            cur.getCompany_id(),
                            cur.getEmployee_id()
                    );

                    switch (field) {
                        case "1" -> upd.setName(readString("New name: "));
                        case "2" -> upd.setFloors((int) readLong("New floors: "));
                        case "3" -> upd.setAddress(readString("New address: "));
                        case "4" -> upd.setCommon_areas(BigDecimal.valueOf(readBigDecimal("New common_areas: ").doubleValue()));
                        case "5" -> upd.setTotal_areas(BigDecimal.valueOf(readBigDecimal("New total_areas: ").doubleValue()));
                        case "6" -> upd.setContract_start_date(readDate("New contract_start_date (YYYY-MM-DD): "));
                        case "7" -> upd.setFee_per_sqm(BigDecimal.valueOf(readBigDecimal("New fee_per_sqm: ").doubleValue()));
                        case "8" -> upd.setFee_per_pet_using_ca(BigDecimal.valueOf(readBigDecimal("New fee_per_pet_using_ca: ").doubleValue()));
                        case "9" -> upd.setFee_per_person_over_7_using_elevator(BigDecimal.valueOf(readBigDecimal("New fee_per_person_over_7_using_elevator: ").doubleValue()));
                        case "10" -> upd.setCompany_id(readOptionalLong("New company_id (0 for null): "));
                        case "11" -> upd.setEmployee_id(readOptionalLong("New employee_id: "));
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }

                    BuildingDao.updateBuilding(id, upd);
                    System.out.println("Updated: " + BuildingDao.getBuilding(id));
                }

                case "3" -> {
                    long id = readLong("Building id: ");
                    BuildingDao.deleteBuilding(id);
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        System.out.println("All buildings:");
        BuildingDao.getBuildings().forEach(System.out::println);
    }

    private static void ApartmentCrud() {
        System.out.println("All apartments:");
        ApartmentDao.getApartments().forEach(System.out::println);
        System.out.println("Create (1), update (2) or delete apartment (3)?");
        String cmd = sc.nextLine().trim();

        try {
            switch (cmd) {
                case "1" -> {
                    ApartmentDto apt = new ApartmentDto(
                            0,
                            (int) readLong("Apartment number: "),
                            (int) readLong("Floor: "),
                            readBigDecimal("Area: "),
                            (int) readLong("Pets using common areas: "),
                            readLong("Building id: ")
                    );
                    ApartmentDao.createApartment(apt);
                }
                case "2" -> {
                    long id = readLong("Apartment id: ");
                    ApartmentDto cur = ApartmentDao.getApartment(id);

                    System.out.println("Change:");
                    System.out.println("1 - number");
                    System.out.println("2 - floor");
                    System.out.println("3 - area");
                    System.out.println("4 - pets_using_ca");
                    System.out.println("5 - building_id");
                    String field = sc.nextLine().trim();

                    ApartmentDto upd = new ApartmentDto(
                            0,
                            cur.getNumber(),
                            cur.getFloor(),
                            cur.getArea(),
                            cur.getPets_using_ca(),
                            cur.getBuilding_id()
                    );

                    switch (field) {
                        case "1" -> upd.setNumber((int) readLong("New apartment number: "));
                        case "2" -> upd.setFloor((int) readLong("New floor: "));
                        case "3" -> upd.setArea(readBigDecimal("New area: "));
                        case "4" -> upd.setPets_using_ca((int) readLong("New pets_using_ca: "));
                        case "5" -> upd.setBuilding_id(readLong("New building_id: "));
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }
                    ApartmentDao.updateApartment(id, upd);
                }
                case "3" -> {
                    long id = readLong("Apartment id: ");
                    ApartmentDao.deleteApartment(id);
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        System.out.println("All apartments:");
        ApartmentDao.getApartments().forEach(System.out::println);
    }

    private static void OwnerCrud() {
        System.out.println("All owners:");
        OwnerDao.getOwners().forEach(System.out::println);
        System.out.println("Create (1), update (2) or delete owner (3)?");
        String cmd = sc.nextLine().trim();

        try {
            switch (cmd) {
                case "1" -> {
                    OwnerDto owner = new OwnerDto(
                            0,
                            readString("Owner name: "),
                            readDate("Owner date of birth: ")
                    );
                    OwnerDao.createOwner(owner);
                }
                case "2" -> {
                    long id = readLong("Owner id: ");
                    OwnerDto cur = OwnerDao.getOwner(id);

                    System.out.println("Change:");
                    System.out.println("1 - name");
                    System.out.println("2 - date of birth");
                    String field = sc.nextLine().trim();

                    OwnerDto upd = new OwnerDto(0, cur.getName(), cur.getBirth_date());

                    switch (field) {
                        case "1" -> upd.setName(readString("New name: "));
                        case "2" -> upd.setBirth_date(readDate("New date of birth: "));
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }
                    OwnerDao.updateOwner(id, upd);
                }
                case "3" -> {
                    long id = readLong("Owner id: ");
                    OwnerDao.deleteOwner(id);
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        System.out.println("All owners:");
        OwnerDao.getOwners().forEach(System.out::println);
    }

    private static void ResidentCrud() {
        System.out.println("All residents:");
        ResidentDao.getResidents().forEach(System.out::println);
        System.out.println("Create (1), update (2) or delete resident (3)?");
        String cmd = sc.nextLine().trim();

        try {
            switch (cmd) {
                case "1" -> {
                    ResidentDto resident = new ResidentDto(
                            0,
                            readString("Resident name: "),
                            readDate("Resident date of birth: "),
                            readBoolean("Resident uses elevator (true/false): "),
                            readDate("Resident contract_start (YYYY-MM-DD): "),
                            readLong("Resident apartment_id: ")
                    );
                    ResidentDao.createResident(resident);
                }
                case "2" -> {
                    long id = readLong("Resident id: ");
                    ResidentDto cur = ResidentDao.getResident(id);

                    System.out.println("Change:");
                    System.out.println("1 - name");
                    System.out.println("2 - date of birth");
                    System.out.println("3 - uses_elevator");
                    System.out.println("4 - contract_start");
                    System.out.println("5 - apartment_id");
                    String field = sc.nextLine().trim();

                    ResidentDto upd = new ResidentDto(
                            0,
                            cur.getName(),
                            cur.getBirth_date(),
                            cur.isUses_elevator(),
                            cur.getContract_start(),
                            cur.getApartment_id()
                    );

                    switch (field) {
                        case "1" -> upd.setName(readString("New name: "));
                        case "2" -> upd.setBirth_date(readDate("New date of birth: "));
                        case "3" -> upd.setUses_elevator(readBoolean("New uses_elevator (true/false): "));
                        case "4" -> upd.setContract_start(readDate("New contract_start (YYYY-MM-DD): "));
                        case "5" -> upd.setApartment_id(readLong("New apartment_id: "));
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }
                    ResidentDao.updateResident(id, upd);
                }
                case "3" -> {
                    long id = readLong("Resident id: ");
                    ResidentDao.deleteResident(id);
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        System.out.println("All residents:");
        ResidentDao.getResidents().forEach(System.out::println);
    }

    private static void EmployeesCrud() {
        System.out.println("All employees:");
        EmployeeDao.getEmployees().forEach(System.out::println);
        System.out.println("Create (1), update (2) or delete employee (3)?");
        String cmd = sc.nextLine().trim();

        try {
            switch (cmd) {
                case "1" -> {
                    System.out.println("All companies:");
                    CompanyDao.getCompanies().forEach(System.out::println);

                    Set<Long> companyIds = new HashSet<>();
                    String addMore = "yes";
                    while ("yes".equalsIgnoreCase(addMore)) {
                        long companyId = readLong("Employee company_id (0 for skip): ");
                        if (companyId > 0) {
                            companyIds.add(companyId);
                        }
                        addMore = readString("Add another company? (yes/no): ");
                    }

                    EmployeeDto employee = new EmployeeDto(
                            0,
                            readString("Employee name: "),
                            readDate("Employee date of birth: "),
                            companyIds
                    );
                    EmployeeDao.createEmployee(employee);
                }
                case "2" -> {
                    long id = readLong("Employee id: ");
                    EmployeeDto cur = EmployeeDao.getEmployee(id);

                    System.out.println("Change:");
                    System.out.println("1 - name");
                    System.out.println("2 - date of birth");
                    System.out.println("3 - company_ids");
                    String field = sc.nextLine().trim();

                    EmployeeDto upd = new EmployeeDto(
                            0,
                            cur.getName(),
                            cur.getBirth_date(),
                            new HashSet<>(cur.getCompany_ids())
                    );

                    switch (field) {
                        case "1" -> upd.setName(readString("New name: "));
                        case "2" -> upd.setBirth_date(readDate("New date of birth: "));
                        case "3" -> {
                            System.out.println("All companies:");
                            CompanyDao.getCompanies().forEach(System.out::println);

                            Set<Long> newCompanyIds = new HashSet<>();
                            String addMore = "yes";
                            while ("yes".equalsIgnoreCase(addMore)) {
                                long companyId = readLong("Company id (0 for skip): ");
                                if (companyId > 0) {
                                    newCompanyIds.add(companyId);
                                }
                                addMore = readString("Add another company? (yes/no): ");
                            }
                            upd.setCompany_ids(newCompanyIds);
                        }
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }
                    EmployeeDao.updateEmployee(id, upd);
                }
                case "3" -> {
                    long id = readLong("Employee id: ");
                    System.out.println("Redistributing buildings");
                    BuildingAssignmentService.redistributeBuildingsFromFiredEmployee(id);
                    EmployeeDao.deleteEmployee(id);
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        System.out.println("All employees:");
        EmployeeDao.getEmployees().forEach(System.out::println);
    }

    private static void Assignments() {
        System.out.println("1 - Sign a contract between building and company");
        System.out.println("2 - Fire employee and redistribute buildings");
        String cmd = sc.nextLine().trim();

        try {
            switch (cmd) {
                case "1" -> {
                    long buildingId = readLong("Building id (existing): ");
                    long companyId = readLong("Company id (existing): ");
                    BuildingAssignmentService.contractBuildingToCompany(buildingId, companyId);
                }
                case "2" -> {
                    long firedEmployeeId = readLong("Fired employee id (existing): ");
                    BuildingAssignmentService.redistributeBuildingsFromFiredEmployee(firedEmployeeId);
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void setFees() {
        System.out.println("Set fee for building");
        long buildingId = readLong("Building id: ");
        BuildingDto b = BuildingDao.getBuilding(buildingId);

        BigDecimal feeSqm = readBigDecimal("fee_per_sqm: ");
        BigDecimal feePet = readBigDecimal("fee_per_pet_using_ca: ");
        BigDecimal feeElev = readBigDecimal("fee_per_person_over_7_using_elevator: ");

        BuildingDto updated = new BuildingDto(
                0,
                b.getName(),
                b.getFloors(),
                b.getAddress(),
                b.getCommon_areas(),
                b.getTotal_areas(),
                b.getContract_start_date(),
                feeSqm,
                feePet,
                feeElev,
                b.getCompany_id(),
                b.getEmployee_id()
        );

        BuildingDao.updateBuilding(buildingId, updated);
        System.out.println(BuildingDao.getBuilding(buildingId));
    }

    private static void createPayment() {
        System.out.println("Create Payment)");

        long apartmentId = readLong("apartment_id: ");
        LocalDate paymentDate = readDate("payment_date (YYYY-MM-DD): ");

        PayRequestDto req = new PayRequestDto(apartmentId, paymentDate);
        PaymentService.pay(req, PAYMENTS_FILE);

        PaymentDao.getPayments().forEach(System.out::println);
    }

    private static void FilterSort() {
        System.out.println("Companies by income (descending):");
        FilterSortDao.getCompaniesByIncomeDesc().forEach(System.out::println);

        System.out.println("Employees by name (ascending):");
        FilterSortDao.getEmployeesByNameAsc().forEach(System.out::println);

        System.out.println("Employees by buildings count (descending):");
        FilterSortDao.getEmployeesByBuildingsCountDesc().forEach(System.out::println);

        System.out.println("Residents by name (ascending):");
        FilterSortDao.getResidentsByNameAsc().forEach(System.out::println);

        System.out.println("Residents by age (descending):");
        FilterSortDao.getResidentsByAgeDesc().forEach(System.out::println);
    }

    private static void Reports() {
        System.out.println("Reports");
        System.out.println("1 - Buildings by employee in company");
        System.out.println("2 - Apartments in building");
        System.out.println("3 - Residents in building");
        System.out.println("4 - Sums to be paid (company/building/employee)");
        System.out.println("5 - Paid sums (company/building/employee)");
        String cmd = sc.nextLine().trim();

        try {
            switch (cmd) {
                case "1" -> {
                    long companyId = readLong("company_id: ");
                    long employeeId = readLong("employee_id: ");
                    System.out.println("Count: " + ReportDao.countBuildingsByEmployeeInCompany(companyId, employeeId));
                    ReportDao.getBuildingsByEmployeeInCompany(companyId, employeeId).forEach(System.out::println);
                }
                case "2" -> {
                    long buildingId = readLong("building_id: ");
                    System.out.println("Count: " + ReportDao.countApartmentsInBuilding(buildingId));
                    ReportDao.getApartmentIdsInBuilding(buildingId).forEach(id -> System.out.println("ApartmentId=" + id));
                }
                case "3" -> {
                    long buildingId = readLong("building_id: ");
                    System.out.println("Count: " + ReportDao.countResidentsInBuilding(buildingId));
                    ReportDao.getResidentsInBuilding(buildingId).forEach(System.out::println);
                }
                case "4" -> {
                    long companyId = readLong("company_id: ");
                    long employeeId = readLong("employee_id: ");
                    long buildingId = readLong("building_id: ");
                    System.out.println("Company due: " + BillingReportService.getDueSumForCompany(companyId));
                    System.out.println("Building due: " + BillingReportService.getDueSumForBuilding(buildingId));
                    System.out.println("Employee due: " + BillingReportService.getDueSumForEmployee(employeeId));
                }
                case "5" -> {
                    long companyId = readLong("company_id: ");
                    long employeeId = readLong("employee_id: ");
                    long buildingId = readLong("building_id: ");
                    System.out.println("Company paid: " + ReportDao.getPaidSumForCompany(companyId));
                    System.out.println("Building paid: " + ReportDao.getPaidSumForBuilding(buildingId));
                    System.out.println("Employee paid: " + ReportDao.getPaidSumForEmployee(employeeId));
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void PaymentExport() {
        String companyName = readString("company_name: ");
        String employeeName = readString("employee_name: ");
        String buildingName = readString("building_name: ");
        long apartmentId = readLong("apartment_id: ");
        BigDecimal amount = readBigDecimal("amount: ");
        LocalDate date = readDate("payment_date (YYYY-MM-DD): ");

        PaymentExportService.appendPaymentLine(
                PAYMENTS_FILE,
                companyName,
                employeeName,
                buildingName,
                apartmentId,
                amount,
                date
        );

        System.out.println("Exported to " + PAYMENTS_FILE);
    }

    private static void PaymentStatus() {
        long apartmentId = readLong("Apartment id: ");
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        BigDecimal due = BillingService.calculateMonthlyFeeForApartment(apartmentId);

        BigDecimal paid = PaymentDao.getTotalPaid(apartmentId, month);

        boolean isPaid = paid.compareTo(due) >= 0;
        System.out.printf("Apartment %d for %s: Due=%.2f, Paid=%.2f, %s%n",
                apartmentId, month, due, paid, isPaid ? "PAID" : "PENDING");
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static long readLong(String prompt) {
        System.out.print(prompt);
        return Long.parseLong(sc.nextLine().trim());
    }

    private static Long readOptionalLong(String prompt) {
        long v = readLong(prompt);
        return v <= 0 ? null : v;
    }

    private static BigDecimal readBigDecimal(String prompt) {
        System.out.print(prompt);
        return new BigDecimal(sc.nextLine().trim());
    }

    private static LocalDate readDate(String prompt) {
        System.out.print(prompt);
        return LocalDate.parse(sc.nextLine().trim());
    }

    private static boolean readBoolean(String prompt) {
        System.out.print(prompt);
        return Boolean.parseBoolean(sc.nextLine().trim());
    }

    private static BuildingDto readBuildingDto() {
        String name = readString("Building name: ");
        int floors = (int) readLong("floors: ");
        String address = readString("address: ");
        BigDecimal commonAreas = readBigDecimal("common_areas: ");
        BigDecimal totalAreas = readBigDecimal("total_areas: ");
        LocalDate contractStart = readDate("contract_start_date (YYYY-MM-DD): ");
        BigDecimal feeSqm = readBigDecimal("fee_per_sqm: ");
        BigDecimal feePet = readBigDecimal("fee_per_pet_using_ca: ");
        BigDecimal feeElev = readBigDecimal("fee_per_person_over_7_using_elevator: ");

        Long companyId = readOptionalLong("company_id (0 for null): ");
        Long employeeId = readOptionalLong("employee_id (0 for null): ");

        return new BuildingDto(
                0,
                name,
                floors,
                address,
                commonAreas,
                totalAreas,
                contractStart,
                feeSqm,
                feePet,
                feeElev,
                companyId,
                employeeId
        );
    }
}
