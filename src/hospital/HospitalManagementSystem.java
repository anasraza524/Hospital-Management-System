package hospital;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Map;
import java.util.HashMap;
import java.util.*; 
import java.time.LocalDate;
public class HospitalManagementSystem extends Application {

    private Hospital hospital = new Hospital("City Hospital");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hospital Management System");

        BorderPane root = new BorderPane();
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(20));
        menuLayout.setStyle("-fx-background-color: #2C3E50;");

        Button btnDashboard = new Button("Dashboard");
        Button btnAddPatient = new Button("Add Patient");
        Button btnAddDoctor = new Button("Add Doctor");
        Button btnSearch = new Button("Search Records");
        Button btnAppointments = new Button("Manage Appointments");
        Button btnBilling = new Button("Billing & Payments");
        Button btnExit = new Button("Exit");

        menuLayout.getChildren().addAll(btnDashboard, btnAddPatient, btnAddDoctor, btnSearch, btnAppointments, btnBilling, btnExit);
        root.setLeft(menuLayout);

        VBox centerLayout = new VBox(10);
        centerLayout.setPadding(new Insets(20));
        root.setCenter(centerLayout);

        btnDashboard.setOnAction(e -> showDashboard(centerLayout));
        btnAddPatient.setOnAction(e -> addPatientForm(centerLayout));
        btnAddDoctor.setOnAction(e -> addDoctorForm(centerLayout));
        btnSearch.setOnAction(e -> searchRecordsForm(centerLayout));
        btnAppointments.setOnAction(e -> manageAppointmentsForm(centerLayout));
        btnBilling.setOnAction(e -> billingForm(centerLayout));
        btnExit.setOnAction(e -> primaryStage.close());

        showDashboard(centerLayout);

        // Scene
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showDashboard(VBox layout) {
        layout.getChildren().clear();

        Label titleLabel = new Label("Dashboard");

        VBox patientCard = createDashboardCard("Total Patients", String.valueOf(hospital.getPatientCount()), "#3498DB");
        VBox doctorCard = createDashboardCard("Total Doctors", String.valueOf(hospital.getDoctorCount()), "#2ECC71");

        List<Appointment> todayAppointments = hospital.getTodayAppointments();
        VBox appointmentCard = new VBox(10);
        appointmentCard.setPadding(new Insets(10));
        appointmentCard.setStyle("-fx-background-color: #E74C3C; -fx-border-radius: 8; -fx-background-radius: 8;");
        Label appointmentTitle = new Label("Today's Appointments");
        appointmentTitle.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        appointmentCard.getChildren().add(appointmentTitle);

        for (Appointment appointment : todayAppointments) {
            Label appointmentLabel = new Label(appointment.toString());
            appointmentLabel.setStyle("-fx-text-fill: white;");
            appointmentCard.getChildren().add(appointmentLabel);
        }

        VBox paymentCard = createDashboardCard("Total Payments", "$" + hospital.getTotalPayments(), "#F1C40F");

        layout.getChildren().addAll(titleLabel, patientCard, doctorCard, appointmentCard, paymentCard);
    }


    private VBox createDashboardCard(String title, String value, String color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }



    private void addPatientForm(VBox layout) {
        layout.getChildren().clear();
        Label titleLabel = new Label("Add Patient");
        TextField idField = new TextField();
        idField.setPromptText("Enter Patient ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Patient Name");
        TextField ageField = new TextField();
        ageField.setPromptText("Enter Patient Age");
        TextArea historyField = new TextArea();
        historyField.setPromptText("Enter Medical History");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String history = historyField.getText();

                Patient patient = new Patient(id, name, age, history);
                hospital.addPatient(patient);
                showAlert("Success", "Patient added successfully!");
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid age.");
            }
        });

        layout.getChildren().addAll(titleLabel, idField, nameField, ageField, historyField, submitButton);
    }

    private void addDoctorForm(VBox layout) {
        layout.getChildren().clear();
        Label titleLabel = new Label("Add Doctor");
        TextField idField = new TextField();
        idField.setPromptText("Enter Doctor ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Doctor Name");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            Doctor doctor = new Doctor(id, name);
            hospital.addDoctor(doctor);
            showAlert("Success", "Doctor added successfully!");
        });

        layout.getChildren().addAll(titleLabel, idField, nameField, submitButton);
    }

    private void searchRecordsForm(VBox layout) {
        layout.getChildren().clear();
        Label titleLabel = new Label("Search Records");
        TextField searchField = new TextField();
        searchField.setPromptText("Enter Patient/Doctor/Staff ID");

        Button searchButton = new Button("Search");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        searchButton.setOnAction(e -> {
            String id = searchField.getText();
            String result = hospital.searchById(id);
            resultArea.setText(result);
        });

        layout.getChildren().addAll(titleLabel, searchField, searchButton, resultArea);
    }

    private void manageAppointmentsForm(VBox layout) {
        layout.getChildren().clear();
        Label titleLabel = new Label("Manage Appointments");
        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Enter Patient ID");
        TextField doctorIdField = new TextField();
        doctorIdField.setPromptText("Enter Doctor ID");
        DatePicker appointmentDate = new DatePicker();

        Button scheduleButton = new Button("Schedule Appointment");
        scheduleButton.setOnAction(e -> {
            String patientId = patientIdField.getText();
            String doctorId = doctorIdField.getText();
            String date = appointmentDate.getValue().toString();

            if (hospital.scheduleAppointment(patientId, doctorId, date)) {
                showAlert("Success", "Appointment scheduled successfully!");
            } else {
                showAlert("Error", "Appointment scheduling failed.");
            }
        });

        layout.getChildren().addAll(titleLabel, patientIdField, doctorIdField, appointmentDate, scheduleButton);
    }

    private void billingForm(VBox layout) {
        layout.getChildren().clear();
        Label titleLabel = new Label("Billing & Payments");
        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Enter Patient ID");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount");

        Button payButton = new Button("Pay");
        payButton.setOnAction(e -> {
            try {
                String patientId = patientIdField.getText();
                double amount = Double.parseDouble(amountField.getText());
                if (hospital.processPayment(patientId, amount)) {
                    showAlert("Success", "Payment processed successfully!");
                } else {
                    showAlert("Error", "Payment processing failed.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid amount.");
            }
        });

        layout.getChildren().addAll(titleLabel, patientIdField, amountField, payButton);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
class Appointment {
    private String patientId;
    private String doctorId;
    private LocalDate date;

    public Appointment(String patientId, String doctorId, LocalDate date) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Patient: " + patientId + ", Doctor: " + doctorId + ", Date: " + date;
    }
}

class Hospital {
    private String name;
    private Map<String, Patient> patients = new HashMap<>();
    private Map<String, Doctor> doctors = new HashMap<>();
    private List<Appointment> appointments = new ArrayList<>();
    private double totalPayments = 0;
    public Hospital(String name) {
        this.name = name;
    }

    public boolean processPayment(String patientId, double amount) {
        if (patients.containsKey(patientId)) {
            totalPayments += amount;
            System.out.println("Payment of $" + amount + " processed for Patient ID: " + patientId);
            return true;  // Payment successful
        } else {
            System.out.println("Patient not found: " + patientId);
            return false;  // Payment failed
        }
    }



    public void addPatient(Patient patient) {
        patients.put(patient.getId(), patient);
    }

    public void addDoctor(Doctor doctor) {
        doctors.put(doctor.getId(), doctor);
    }

    public boolean scheduleAppointment(String patientId, String doctorId, String date) {
        if (patients.containsKey(patientId) && doctors.containsKey(doctorId)) {
            appointments.add(new Appointment(patientId, doctorId, LocalDate.parse(date)));
            return true;
        }
        return false;
    }

    public List<Appointment> getTodayAppointments() {
        LocalDate today = LocalDate.now();
        List<Appointment> todayAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(today)) {
                todayAppointments.add(appointment);
            }
        }
        return todayAppointments;
    }

    public void addPayment(double amount) {
        totalPayments += amount;
    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public String searchById(String id) {
        if (patients.containsKey(id)) {
            return "Patient found: " + patients.get(id);
        } else if (doctors.containsKey(id)) {
            return "Doctor found: " + doctors.get(id);
        }
        return "Record not found.";
    }

    public int getPatientCount() {
        return patients.size();
    }

    public int getDoctorCount() {
        return doctors.size();
    }
}


class Patient {
    private String id;
    private String name;
    private int age;
    private String medicalHistory;

    public Patient(String id, String name, int age, String medicalHistory) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.medicalHistory = medicalHistory;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", medicalHistory='" + medicalHistory + '\'' +
                '}';
    }
}

class Doctor {
    private String id;
    private String name;

    public Doctor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}