package com.hexaware.AmazeCare;

import com.hexaware.AmazeCare.dto.MedicalRecordDTO;
import com.hexaware.AmazeCare.mapper.MedicalRecordMapper;
import com.hexaware.AmazeCare.model.MedicalRecord;
import com.hexaware.AmazeCare.model.Patient;
import com.hexaware.AmazeCare.repository.MedicalRecordRepository;
import com.hexaware.AmazeCare.repository.PatientRepository;
import com.hexaware.AmazeCare.service.MedicalRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordServiceImplTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicalRecordMapper medicalRecordMapper;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMedicalRecord_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFullName("John Doe");

        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientId(1L);
        dto.setRecordDate(LocalDate.now());
        dto.setDiagnosis("Flu");
        dto.setTreatmentPlan("Rest and medication");

        MedicalRecord medicalRecord = new MedicalRecord();
        MedicalRecord savedRecord = new MedicalRecord();
        savedRecord.setId(1L);
        savedRecord.setPatient(patient);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicalRecordMapper.toEntity(dto, patient)).thenReturn(medicalRecord);
        when(medicalRecordRepository.save(medicalRecord)).thenReturn(savedRecord);
        when(medicalRecordMapper.toDTO(savedRecord)).thenReturn(dto);

        // Act
        MedicalRecordDTO result = medicalRecordService.createMedicalRecord(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getDiagnosis(), result.getDiagnosis());
        assertEquals(dto.getPatientId(), result.getPatientId());
    }

    @Test
    void testCreateMedicalRecord_PatientNotFound() {
        // Arrange
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientId(999L);

        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicalRecordService.createMedicalRecord(dto);
        });

        assertEquals("Patient not found with ID: 999", exception.getMessage());
    }

    @Test
    void testGetMedicalRecordById_Success() {
        // Arrange
        MedicalRecord record = new MedicalRecord();
        record.setId(1L);
        record.setDiagnosis("Fever");
        record.setTreatmentPlan("Medication");

        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setDiagnosis("Fever");
        dto.setTreatmentPlan("Medication");

        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(medicalRecordMapper.toDTO(record)).thenReturn(dto);

        // Act
        MedicalRecordDTO result = medicalRecordService.getMedicalRecordById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Fever", result.getDiagnosis());
        assertEquals("Medication", result.getTreatmentPlan());
    }

    @Test
    void testGetMedicalRecordById_NotFound() {
        // Arrange
        when(medicalRecordRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicalRecordService.getMedicalRecordById(999L);
        });

        assertEquals("Medical Record not found with ID: 999", exception.getMessage());
    }

    @Test
    void testGetAllMedicalRecords() {
        // Arrange
        MedicalRecord record = new MedicalRecord();
        MedicalRecordDTO dto = new MedicalRecordDTO();

        when(medicalRecordRepository.findAll()).thenReturn(Collections.singletonList(record));
        when(medicalRecordMapper.toDTO(record)).thenReturn(dto);

        // Act
        var result = medicalRecordService.getAllMedicalRecords();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteMedicalRecord_Success() {
        // Arrange
        MedicalRecord record = new MedicalRecord();
        record.setId(1L);

        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        doNothing().when(medicalRecordRepository).delete(record);

        // Act
        medicalRecordService.deleteMedicalRecord(1L);

        // Assert
        verify(medicalRecordRepository, times(1)).delete(record);
    }

    @Test
    void testDeleteMedicalRecord_NotFound() {
        // Arrange
        when(medicalRecordRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicalRecordService.deleteMedicalRecord(999L);
        });

        assertEquals("Medical Record not found with ID: 999", exception.getMessage());
    }
}
