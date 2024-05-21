package com.hospital.service;

import com.hospital.entity.Doctor;
import com.hospital.enums.City;
import com.hospital.enums.Speciality;
import com.hospital.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> findDoctorsByCityAndSpeciality(City city, Speciality speciality) {
        return doctorRepository.findByCityAndSpeciality(city, speciality);
    }

	public Object addDoctor(Doctor doctor) {
		return doctorRepository.save(doctor);
	}

	public void removeDoctor(Long id) {
		doctorRepository.deleteById(id);
	}
	
	public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

}
