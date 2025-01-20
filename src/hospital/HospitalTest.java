
import hospital.Hospital;

 public class HospitalTest {

    @Test
    public void testAddPatient() {
        Hospital hospital = new Hospital("City Hospital");
        Patient patient = new Patient("P001", "Anas", 30, "No allergies");

        hospital.addPatient(patient);
        String result = hospital.searchById("P001");

        assertEquals("Patient found: Patient{id='P001', name='Anas', age=30, medicalHistory='No allergies'}", result);
    }

    @Test
    public void testProcessPayment() {
        Hospital hospital = new Hospital("City Hospital");
        hospital.addPatient(new Patient("P001", "Anas", 28, "Diabetic"));

        boolean paymentSuccess = hospital.processPayment("P001", 200.0);

        assertTrue(paymentSuccess, "Payment should be successful");
        assertEquals(200.0, hospital.getTotalPayments(), "Total payments should be updated");
    }
}

