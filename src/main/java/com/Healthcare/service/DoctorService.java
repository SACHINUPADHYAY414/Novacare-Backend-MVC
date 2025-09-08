package com.Healthcare.service;

import com.Healthcare.model.Doctor;
import com.Healthcare.repository.DoctorRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private Cloudinary cloudinary;

    // Save single doctor
    public Doctor addDoctor(Doctor doctor) {
        if (doctor.getStatus() == null) {
            doctor.setStatus(true);
        }
        return doctorRepository.save(doctor);
    }

    // Save multiple doctors
    public List<Doctor> addDoctorsBulk(List<Doctor> doctors) {
        for (Doctor d : doctors) {
            if (d.getStatus() == null) {
                d.setStatus(true);
            }
        }
        return doctorRepository.saveAll(doctors);
    }

    // Only active doctors
    public List<Doctor> getAllActiveDoctors() {
        return doctorRepository.findByStatusTrue();
    }

    // All doctors (active + inactive)
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    // ✅ Upload to Cloudinary
    public String saveProfileImage(MultipartFile file) throws IOException {
        String contentType = file.getContentType();

        // ✅ Accept only PNG or JPG/JPEG
        if (contentType == null || 
            !(contentType.equals("image/png") || contentType.equals("image/jpg") || contentType.equals("image/jpeg"))) {
            throw new IOException("Invalid file type. Only PNG and JPG images are allowed.");
        }

        // ✅ Upload to Cloudinary in 'doctor_images' folder
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "folder", "doctor_images",
            "resource_type", "image"
        ));

        return uploadResult.get("secure_url").toString();
    }


    public Doctor updateDoctor(Long id, Doctor doctorData, MultipartFile profileImage) throws Exception {
        Doctor existingDoctor = doctorRepository.findById(id).orElse(null);
        if (existingDoctor == null) return null;

        existingDoctor.setName(doctorData.getName());
        existingDoctor.setGender(doctorData.getGender());
        existingDoctor.setStateId(doctorData.getStateId());
        existingDoctor.setCityId(doctorData.getCityId());
        existingDoctor.setQualification(doctorData.getQualification());
        existingDoctor.setSpecialization(doctorData.getSpecialization());
        existingDoctor.setStatus(doctorData.getStatus());

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = saveProfileImage(profileImage);
            existingDoctor.setProfileImageUrl(imageUrl);
        }

        return doctorRepository.save(existingDoctor);
    }

    public boolean deleteDoctor(Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
