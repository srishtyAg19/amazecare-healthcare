package com.hexaware.AmazeCare;

import com.hexaware.AmazeCare.dto.AppointmentDTO;
import com.hexaware.AmazeCare.mapper.AppointmentMapper;
import com.hexaware.AmazeCare.model.Appointment;
import com.hexaware.AmazeCare.model.Doctor;
import com.hexaware.AmazeCare.model.Patient;
import com.hexaware.AmazeCare.repository.AppointmentRepository;
import com.hexaware.AmazeCare.repository.DoctorRepository;
import com.hexaware.AmazeCare.repository.PatientRepository;
import com.hexaware.AmazeCare.service.AppointmentServiceImpl;
import com.hexaware.AmazeCare.model.AppointmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAppointment_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFullName("John Doe");

        Doctor doctor = new Doctor();
        doctor.setId(2L);
        doctor.setName("Dr. Smith");

        AppointmentDTO dto = new AppointmentDTO();
        dto.setPatientId(1L);
        dto.setDoctorId(2L);
        dto.setAppointmentDate(LocalDateTime.now()); // Use LocalDateTime
        dto.setStatus(AppointmentStatus.SCHEDULED); // Use the appropriate enum

        Appointment appointment = new Appointment();
        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentMapper.toEntity(dto, patient, doctor)).thenReturn(appointment);
        when(appointmentRepository.save(appointment)).thenReturn(savedAppointment);
        when(appointmentMapper.toDTO(savedAppointment)).thenReturn(dto);

        // Act
        AppointmentDTO result = appointmentService.createAppointment(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getPatientId());
        assertEquals(2L, result.getDoctorId());
        assertEquals(AppointmentStatus.SCHEDULED, result.getStatus());
    }

    @Test
    void testCreateAppointment_PatientNotFound() {
        // Arrange
        AppointmentDTO dto = new AppointmentDTO();
        dto.setPatientId(999L);
        dto.setDoctorId(2L);

        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(dto);
        });

        assertEquals("Patient not found with ID: 999", exception.getMessage());
    }

    @Test
    void testCreateAppointment_DoctorNotFound() {
        // Arrange
        AppointmentDTO dto = new AppointmentDTO();
        dto.setPatientId(1L);
        dto.setDoctorId(999L);

        Patient patient = new Patient();
        patient.setId(1L);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(dto);
        });

        assertEquals("Doctor not found with ID: 999", exception.getMessage());
    }

    @Test
    void testGetAppointmentById_Success() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        AppointmentDTO dto = new AppointmentDTO();
        dto.setAppointmentDate(LocalDateTime.now());

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDTO(appointment)).thenReturn(dto);

        // Act
        AppointmentDTO result = appointmentService.getAppointmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getAppointmentDate(), result.getAppointmentDate());
    }

    @Test
    void testGetAppointmentById_NotFound() {
        // Arrange
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.getAppointmentById(999L);
        });

        assertEquals("Appointment not found with ID: 999", exception.getMessage());
    }

    @Test
    void testGetAllAppointments() {
        // Arrange
        Appointment appointment = new Appointment();
        AppointmentDTO dto = new AppointmentDTO();

        when(appointmentRepository.findAll()).thenReturn(Collections.singletonList(appointment));
        when(appointmentMapper.toDTO(appointment)).thenReturn(dto);

        // Act
        var result = appointmentService.getAllAppointments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteAppointment_Success() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentRepository).delete(appointment);

        // Act
        appointmentService.deleteAppointment(1L);

        // Assert
        verify(appointmentRepository, times(1)).delete(appointment);
    }

    @Test
    void testDeleteAppointment_NotFound() {
        // Arrange
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.deleteAppointment(999L);
        });

        assertEquals("Appointment not found with ID: 999", exception.getMessage());
    }
}
