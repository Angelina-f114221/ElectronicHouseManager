package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.dto.*;
import org.example.entity.*;
import org.example.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
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
                    case "1" -> CompanyCrud();
                    case "2" -> EmployeesCrud();
                    case "3" -> BuildingCrud();
                    case "4" -> ApartmentCrud();
                    case "5" -> OwnerCrud();
                    case "6" -> ResidentCrud();
                    case "7" -> Assignments();
                    case "8" -> ManageFees();
                    case "9" -> createPayment();
                    case "10" -> FilterSort();
                    case "11" -> Reports();
                    case "12" -> PaymentExport();
                    case "13" -> PaymentStatus();
                    case "14" -> ReportDao.getDueSumForCompany(1);
                    case "15" -> { return; }
                    default -> System.out.println("Unknown command.");
                }
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }

            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("1 - Company");
        System.out.println("2 - Employee");
        System.out.println("3 - Building");
        System.out.println("4 - Apartment");
        System.out.println("5 - Owner");
        System.out.println("6 - Resident");
        System.out.println("7 - Building assignment rules");
        System.out.println("8 - Manage fees for building");
        System.out.println("9 - Pay fees");
        System.out.println("10 - Filter and sort");
        System.out.println("11 - Reports (counts/lists/sums)");
        System.out.println("12 - Export payment line to file");
        System.out.println("13 - Check payment status");
        System.out.println("14 - Get due sum for company 1");
        System.out.println("15 - Exit");
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
                    CompanyDto dto = new CompanyDto(0L, name);
                    ValidationUtil.validateOrThrow(dto);
                    CompanyDao.createCompany(dto);
                }
                case "2" -> {
                    long id = readLong("Company id: ");
                    String newName = readString("New name: ");
                    CompanyDto dto = new CompanyDto(0L, newName);
                    ValidationUtil.validateOrThrow(dto);
                    CompanyDao.updateCompany(id, dto);
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
                    System.out.println("\nAll companies:");
                    CompanyDao.getCompanies().forEach(System.out::println);

                    BuildingDto dto = readBuildingDto();
                    ValidationUtil.validateOrThrow(dto);
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
                    System.out.println("7 - company_id");
                    System.out.println("8 - employee_id");
                    String field = sc.nextLine().trim();

                    BuildingDto upd = new BuildingDto(
                            0L,
                            cur.getName(),
                            cur.getFloors(),
                            cur.getAddress(),
                            cur.getCommon_areas(),
                            cur.getTotal_areas(),
                            cur.getContract_start_date(),
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
                        case "7" -> {
                            System.out.println("\nAll companies:");
                            CompanyDao.getCompanies().forEach(System.out::println);
                            upd.setCompany_id(readOptionalLong("New company_id (0 for null): "));
                        }
                        case "8" -> {
                            System.out.println("\nAll employees:");
                            EmployeeDao.getEmployees().forEach(System.out::println);
                            upd.setEmployee_id(readOptionalLong("New employee_id (0 for null): "));
                        }
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }

                    ValidationUtil.validateOrThrow(upd);
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
                    System.out.println("All buildings:");
                    BuildingDao.getBuildings().forEach(System.out::println);
                    ApartmentDto apt = new ApartmentDto(
                            0L,
                            (int) readLong("Apartment number: "),
                            (int) readLong("Floor: "),
                            readBigDecimal("Area: "),
                            (int) readLong("Pets using common areas: "),
                            readLong("Building id: ")
                    );
                    ValidationUtil.validateOrThrow(apt);
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
                            0L,
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
                        case "5" -> {
                            System.out.println("All buildings:");
                            BuildingDao.getBuildings().forEach(System.out::println);
                            upd.setBuilding_id(readLong("New building_id: "));
                        }
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }
                    ValidationUtil.validateOrThrow(upd);
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
                    System.out.println("\nAll apartments:");
                    ApartmentDao.getApartments().forEach(System.out::println);

                    // ✅ ИЗБЕРИ АПАРТАМЕНТИ
                    Set<Long> apartment_ids = new HashSet<>();
                    String addMore = "yes";
                    while ("yes".equalsIgnoreCase(addMore)) {
                        long apartment_id = readLong("Apartment id (0 for skip): ");
                        if (apartment_id > 0) {
                            apartment_ids.add(apartment_id);
                        }
                        addMore = readString("Add another apartment? (yes/no): ");
                    }

                    OwnerDto owner = new OwnerDto(
                            0L,
                            readString("Owner name: "),
                            readDate("Owner date of birth: "),
                            apartment_ids  // ✅ ДОБАВИ АПАРТАМЕНТИ!
                    );
                    ValidationUtil.validateOrThrow(owner);
                    OwnerDao.createOwner(owner);
                }
                case "2" -> {
                    long id = readLong("Owner id: ");
                    OwnerDto cur = OwnerDao.getOwner(id);

                    System.out.println("Change:");
                    System.out.println("1 - name");
                    System.out.println("2 - date of birth");
                    System.out.println("3 - apartments");  // ✅ НОВО!
                    String field = sc.nextLine().trim();

                    OwnerDto upd = new OwnerDto(
                            0L,
                            cur.getName(),
                            cur.getBirth_date(),
                            new HashSet<>(cur.getApartment_ids())  // ✅ КОПИРАЙ АПАРТАМЕНТИ!
                    );

                    switch (field) {
                        case "1" -> upd.setName(readString("New name: "));
                        case "2" -> upd.setBirth_date(readDate("New date of birth: "));
                        case "3" -> {  // ✅ НОВО - АКТУАЛИЗИРАЙ АПАРТАМЕНТИ!
                            System.out.println("\nAll apartments:");
                            ApartmentDao.getApartments().forEach(System.out::println);

                            Set<Long> new_apartment_ids = new HashSet<>();
                            String addMore = "yes";
                            while ("yes".equalsIgnoreCase(addMore)) {
                                long apartment_id = readLong("Apartment id (0 for skip): ");
                                if (apartment_id > 0) {
                                    new_apartment_ids.add(apartment_id);
                                }
                                addMore = readString("Add another apartment? (yes/no): ");
                            }
                            upd.setApartment_ids(new_apartment_ids);
                        }
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }
                    ValidationUtil.validateOrThrow(upd);
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
                    System.out.println("All apartments:");
                    ApartmentDao.getApartments().forEach(System.out::println);
                    ResidentDto resident = new ResidentDto(
                            0L,
                            readString("Resident name: "),
                            readDate("Resident date of birth: "),
                            readBoolean("Resident uses elevator (true/false): "),
                            readDate("Resident contract_start (YYYY-MM-DD): "),
                            readLong("Resident apartment_id: ")
                    );
                    ValidationUtil.validateOrThrow(resident);
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
                            0L,
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
                        case "5" -> {
                            System.out.println("All apartments:");
                            ApartmentDao.getApartments().forEach(System.out::println);
                            upd.setApartment_id(readLong("New apartment_id: "));
                        }
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }
                    ValidationUtil.validateOrThrow(upd);
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

                    Set<Long> company_ids = new HashSet<>();
                    String addMore = "yes";
                    while ("yes".equalsIgnoreCase(addMore)) {
                        long company_id = readLong("Employee company_id (0 for skip): ");
                        if (company_id > 0) {
                            company_ids.add(company_id);
                        }
                        addMore = readString("Add another company? (yes/no): ");
                    }

                    EmployeeDto employee = new EmployeeDto(
                            0L,
                            readString("Employee name: "),
                            readDate("Employee date of birth: "),
                            company_ids
                    );
                    ValidationUtil.validateOrThrow(employee);
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
                            0L,
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

                            Set<Long> newcompany_ids = new HashSet<>();
                            String addMore = "yes";
                            while ("yes".equalsIgnoreCase(addMore)) {
                                long company_id = readLong("Company id (0 for skip): ");
                                if (company_id > 0) {
                                    newcompany_ids.add(company_id);
                                }
                                addMore = readString("Add another company? (yes/no): ");
                            }
                            upd.setCompany_ids(newcompany_ids);
                        }
                        default -> {
                            System.out.println("No such option");
                            return;
                        }
                    }
                    ValidationUtil.validateOrThrow(upd);
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
                    System.out.println("All buildings:");
                    BuildingDao.getBuildings().forEach(System.out::println);
                    long building_id = readLong("Building id (existing): ");
                    System.out.println("All companies:");
                    CompanyDao.getCompanies().forEach(System.out::println);
                    long company_id = readLong("Company id (existing): ");
                    BuildingAssignmentService.contractBuildingToCompany(building_id, company_id);
                }
                case "2" -> {
                    System.out.println("All employees:");
                    EmployeeDao.getEmployees().forEach(System.out::println);
                    long fired_employee_id = readLong("Fired employee id (existing): ");
                    BuildingAssignmentService.redistributeBuildingsFromFiredEmployee(fired_employee_id);
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void ManageFees() {
        System.out.println("Manage Fees");
        System.out.println("1 - Add new fee");
        System.out.println("2 - View fee history for building");
        System.out.println("3 - Delete fee");
        String cmd = sc.nextLine().trim();

        try {
            switch (cmd) {
                case "1" -> {
                    System.out.println("All buildings:");
                    BuildingDao.getBuildings().forEach(System.out::println);
                    long building_id = readLong("Building id: ");

                    Building building = null;
                    try (var session = SessionFactoryUtil.getSessionFactory().openSession()) {
                        building = session.find(Building.class, building_id);
                    }

                    if (building == null) {
                        System.out.println("Building not found");
                        return;
                    }

                    BigDecimal feeSqm = readBigDecimal("fee_per_sqm: ");
                    BigDecimal feePet = readBigDecimal("fee_per_pet_using_ca: ");
                    BigDecimal feeElev = readBigDecimal("fee_per_person_over_7_using_elevator: ");

                    Fee fee = new Fee(feeSqm, feePet, feeElev, LocalDate.now(), building);

                    ValidationUtil.validateOrThrow(fee);
                    FeeDao.createFee(fee);
                }

                case "2" -> {
                    System.out.println("All buildings:");
                    BuildingDao.getBuildings().forEach(System.out::println);
                    long building_id = readLong("Building id: ");
                    List<Fee> fees = FeeDao.getFeesForBuilding(building_id);

                    if (fees.isEmpty()) {
                        System.out.println("No fees found for this building");
                    } else {
                        System.out.println("Fee history:");
                        fees.forEach(System.out::println);
                    }
                }

                case "3" -> {
                    System.out.println("All fees:");
                    FeeDao.getAllFees().forEach(System.out::println);
                    long fee_id = readLong("Fee id: ");
                    FeeDao.deleteFee(fee_id);
                    FeeDao.getAllFees().forEach(System.out::println);
                }

                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void createPayment() {
        System.out.println("Create Payment");

        long apartment_id = readLong("apartment_id: ");
        LocalDate paymentDate = LocalDate.now();

        PayRequestDto req = new PayRequestDto(apartment_id, paymentDate);
        ValidationUtil.validateOrThrow(req);
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
                    System.out.println("All companies:");
                    CompanyDao.getCompanies().forEach(System.out::println);
                    long company_id = readLong("company_id: ");

                    System.out.println("All employees:");
                    EmployeeDao.getEmployees().forEach(System.out::println);
                    long employee_id = readLong("employee_id: ");

                    System.out.println("Count: " + ReportDao.countBuildingsByEmployeeInCompany(company_id, employee_id));
                    ReportDao.getBuildingsByEmployeeInCompany(company_id, employee_id).forEach(System.out::println);
                }
                case "2" -> {
                    System.out.println("All buildings:");
                    BuildingDao.getBuildings().forEach(System.out::println);
                    long building_id = readLong("building_id: ");

                    System.out.println("Count: " + ReportDao.countApartmentsInBuilding(building_id));
                    ReportDao.getApartmentIdsInBuilding(building_id).forEach(id -> System.out.println("apartment_id=" + id));
                }
                case "3" -> {
                    System.out.println("All buildings:");
                    BuildingDao.getBuildings().forEach(System.out::println);

                    long building_id = readLong("building_id: ");
                    System.out.println("Count: " + ReportDao.countResidentsInBuilding(building_id));
                    ReportDao.getResidentsInBuilding(building_id).forEach(System.out::println);
                }
                case "4" -> {
                    System.out.println("All companies:");
                    CompanyDao.getCompanies().forEach(System.out::println);
                    long company_id = readLong("company_id: ");
                    System.out.println("All employees:");
                    EmployeeDao.getEmployees().forEach(System.out::println);
                    long employee_id = readLong("employee_id: ");
                    System.out.println("All buildings:");
                    BuildingDao.getBuildings().forEach(System.out::println);
                    long building_id = readLong("building_id: ");
                    System.out.println("Company due: " + BillingReportService.getDueSumForCompany(company_id));
                    System.out.println("Building due: " + BillingReportService.getDueSumForBuilding(building_id));
                    System.out.println("Employee due: " + BillingReportService.getDueSumForEmployee(employee_id));
                }
                case "5" -> {
                    System.out.println("All companies:");
                    CompanyDao.getCompanies().forEach(System.out::println);
                    long company_id = readLong("company_id: ");
                    System.out.println("All employees:");
                    EmployeeDao.getEmployees().forEach(System.out::println);
                    long employee_id = readLong("employee_id: ");
                    System.out.println("All buildings:");
                    BuildingDao.getBuildings().forEach(System.out::println);
                    long building_id = readLong("building_id: ");
                    System.out.println("Company paid: " + ReportDao.getPaidSumForCompany(company_id));
                    System.out.println("Building paid: " + ReportDao.getPaidSumForBuilding(building_id));
                    System.out.println("Employee paid: " + ReportDao.getPaidSumForEmployee(employee_id));
                }
                default -> System.out.println("No such option");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void PaymentExport() {
        String company_name = readString("company_name: ");
        String employee_name = readString("employee_name: ");
        String building_name = readString("building_name: ");
        long apartment_id = readLong("apartment_id: ");
        BigDecimal amount = readBigDecimal("amount: ");
        LocalDate date = readDate("payment_date (YYYY-MM-DD): ");

        PaymentExportService.appendPaymentLine(
                PAYMENTS_FILE,
                company_name,
                employee_name,
                building_name,
                apartment_id,
                amount,
                date
        );

        System.out.println("Exported to " + PAYMENTS_FILE);
    }

    private static void PaymentStatus() {
        System.out.println("All apartments:");
        ApartmentDao.getApartments().forEach(System.out::println);

        long apartment_id = readLong("Apartment id: ");
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        BigDecimal due = BillingService.calculateMonthlyFeeForApartment(apartment_id);

        BigDecimal paid = PaymentDao.getTotalPaid(apartment_id, month);

        boolean isPaid = paid.compareTo(due) >= 0;
        System.out.printf("Apartment %d for %s: Due=%.2f, Paid=%.2f, %s%n",
                apartment_id, month, due, paid, isPaid ? "PAID" : "PENDING");
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
        BigDecimal common_areas = readBigDecimal("common_areas: ");
        BigDecimal total_areas = readBigDecimal("total_areas: ");
        LocalDate contract_start = readDate("contract_start_date (YYYY-MM-DD): ");

        Long company_id = readOptionalLong("company_id (0 for null): ");

        return new BuildingDto(
                0L,
                name,
                floors,
                address,
                common_areas,
                total_areas,
                contract_start,
                company_id,
                0L
        );
    }
}
