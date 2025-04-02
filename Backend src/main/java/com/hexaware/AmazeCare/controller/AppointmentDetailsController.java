package com.hexaware.AmazeCare.controller;

import com.hexaware.AmazeCare.dto.AppointmentDetailsDTO;
import com.hexaware.AmazeCare.service.AppointmentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment-details")
@CrossOrigin(origins="http://localhost:3000")
public class AppointmentDetailsController {

    @Autowired
    private AppointmentDetailsService detailsService;

    //@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AppointmentDetailsDTO> createAppointmentDetails(@RequestBody AppointmentDetailsDTO detailsDTO) {
        return ResponseEntity.ok(detailsService.createAppointmentDetails(detailsDTO));
    }

    //@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDetailsDTO> getAppointmentDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok(detailsService.getAppointmentDetailsById(id));
    }

    //@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppointmentDetailsDTO>> getAllAppointmentDetails() {
        return ResponseEntity.ok(detailsService.getAllAppointmentDetails());
    }

    //@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDetailsDTO> updateAppointmentDetails(
            @PathVariable Long id, @RequestBody AppointmentDetailsDTO detailsDTO) {
        return ResponseEntity.ok(detailsService.updateAppointmentDetails(id, detailsDTO));
    }

    //@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentDetails(@PathVariable Long id) {
        detailsService.deleteAppointmentDetails(id);
        return ResponseEntity.noContent().build();
    }
}
