package edu.ncsu.csc.itrust.model.emergencyRecord;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;

/**
 * A class for storing emergency health record data for UC21
 * @author nmiles
 *
 */
public class EmergencyRecord {
    private String name;
    private int age;
    private String gender;
    private String contactName;
    private String contactPhone;
    private List<AllergyBean> allergies;
    private String bloodType;
    private List<Diagnosis> diagnoses;
    private List<Prescription> prescriptions;
    private List<Immunization> immunizations;
    
    /**
     * Get the patient name
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the patient name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get the patient age
     * @return
     */
    public int getAge() {
        return age;
    }
    
    /**
     * Set the patient age
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }
    
    /**
     * Get the patient's gender
     * @return
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * Set the patient's gender
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * Get the patient's emergency contact's name
     * @return
     */
    public String getContactName() {
        return contactName;
    }
    
    /**
     * Set the patient's emergency contact's name
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    
    /**
     * Get the patient's emergency contact's phone number
     * @return
     */
    public String getContactPhone() {
        return contactPhone;
    }
    
    /**
     * Set the patient's emergency contact's name
     * @param contactPhone
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    /**
     * Gets a list of all this patient's allergies.
     * WARNING: Allergy functionality has not been added to the system yet, so
     * this list will always be empty or null.
     * @return
     */
    public List<AllergyBean> getAllergies() {
        return allergies;
    }
    
    /**
     * Sets a List of all this patient's allergies
     * @param allergies
     */
    public void setAllergies(List<AllergyBean> allergies) {
        this.allergies = allergies;
    }
    
    /**
     * Gets the patient's blood type
     * @return
     */
    public String getBloodType() {
        return bloodType;
    }
    
    /**
     * Sets the patient's blood type
     * @param bloodType
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    
    /**
     * Gets a List of all the patient's diagnoses
     * WARNING: Diagnosis functionality has not been added to the system yet,
     * so this list will always be empty or null.
     * @return
     */
    public List<Diagnosis> getDiagnoses() {
        return diagnoses;
    }
    
    /**
     * Sets the List of this patient's diagnoses
     * @param diagnoses
     */
    public void setDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses = new ArrayList<Diagnosis>(diagnoses);
    }
    
    /**
     * Gets a List of all the patient's prescriptions
     * WARNING: Prescription functionality has not been added to the system yet,
     * so this list will always be empty or null.
     * @return
     */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }
    
    /**
     * Sets a List of all the patient's prescriptions
     * @param prescriptions
     */
    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
    
    /**
     * Gets a List of all the patient's immunizations
     * WARNING: Immunization functionality has not been added to the system yet,
     * so this list will always be empty or null.
     * @return
     */
    public List<Immunization> getImmunizations() {
        return immunizations;
    }
    
    /**
     * Sets a List of all the patient's immunizations
     * @param immunizations
     */
    public void setImmunizations(List<Immunization> immunizations) {
        this.immunizations = immunizations;
    }
}
