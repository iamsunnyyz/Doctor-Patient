package com.hospital.controller;

import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.enums.City;
import com.hospital.enums.Speciality;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/{patientId}")
    public ResponseEntity<?> suggestDoctor(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) {
            return ResponseEntity.badRequest().body("Patient not found");
        }

        City city;
        try {
            city = City.valueOf(patient.getCity().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok("We are still waiting to expand to your location");
        }

        String symptom = patient.getSymptom();
        Speciality speciality = getSpecialityFromSymptom(symptom);

        if (speciality == null) {
            return ResponseEntity.badRequest().body("Invalid symptom");
        }

        List<Doctor> doctors = doctorService.findDoctorsByCityAndSpeciality(city, speciality);
        if (doctors.isEmpty()) {
            return ResponseEntity.ok("There isn’t any doctor present at your location for your symptom");
        }

        return ResponseEntity.ok(doctors);
    }

    private Speciality getSpecialityFromSymptom(String symptom) {
        switch (symptom.toLowerCase()) {
            case "arthritis":
            case "back pain":
            case "tissue injuries":
                return Speciality.ORTHOPEDIC;
            case "dysmenorrhea":
                return Speciality.GYNECOLOGY;
            case "skin infection":
            case "skin burn":
                return Speciality.DERMATOLOGY;
            case "ear pain":
                return Speciality.ENT;
            default:
                return null;
        }
    }
}
