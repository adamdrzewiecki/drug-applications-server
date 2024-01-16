package org.example.service;

import org.example.model.DrugRecordApplication;
import org.example.model.DrugRecordApplicationDto;
import org.example.repository.DrugRecordApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DrugRecordApplicationServiceTest {

    @Mock
    DrugRecordApplicationRepository drugRecordApplicationRepository;

    DrugRecordApplicationService drugRecordApplicationService;

    @BeforeEach
    void setup() {
        drugRecordApplicationService = new DrugRecordApplicationService(drugRecordApplicationRepository);
    }

    @Test
    void shouldGetAllDrugRecords() {
        // given
        List<DrugRecordApplication> drugRecordApplications = List.of(
            new DrugRecordApplication("1", "a", "test1", List.of("1", "2")),
            new DrugRecordApplication("2", "b", "test2", List.of("3", "4"))
        );
        when(drugRecordApplicationRepository.findAll()).thenReturn(drugRecordApplications);

        // when
        List<DrugRecordApplicationDto> drugRecordApplicationDtos = drugRecordApplicationService.getAllDrugRecordApplications();

        // then
        assertThat(drugRecordApplicationDtos).isNotNull();
        assertThat(drugRecordApplicationDtos).isNotEmpty();
        assertThat(drugRecordApplicationDtos.size()).isEqualTo(2);
    }

    @Test
    void shouldGetDrugRecordById() {
        // given
        List<DrugRecordApplication> drugRecordApplications = List.of(
            new DrugRecordApplication("1", "a", "test1", List.of("1", "2")),
            new DrugRecordApplication("2", "b", "test2", List.of("3", "4"))
        );
        when(drugRecordApplicationRepository.findById(anyString())).thenAnswer(invocationOnMock -> {
            String id = invocationOnMock.getArgument(0);
            return drugRecordApplications.stream().filter(drugRecordApplication -> drugRecordApplication.applicationNumber().equals(id)).findFirst();
        });

        // when
        DrugRecordApplicationDto drugRecordApplicationDto = drugRecordApplicationService.getDrugRecordApplicationById("2");

        // then
        assertThat(drugRecordApplicationDto).isNotNull();
        assertThat(drugRecordApplicationDto.applicationNumber()).isEqualTo("2");
    }

    @Test
    void shouldThrowNoSuchElementExceptionForUnknownId() {
        // given
        List<DrugRecordApplication> drugRecordApplications = List.of(
            new DrugRecordApplication("1", "a", "test1", List.of("1", "2")),
            new DrugRecordApplication("2", "b", "test2", List.of("3", "4"))
        );
        when(drugRecordApplicationRepository.findById(anyString())).thenAnswer(invocationOnMock -> {
            String id = invocationOnMock.getArgument(0);
            return drugRecordApplications.stream().filter(drugRecordApplication -> drugRecordApplication.applicationNumber().equals(id)).findFirst();
        });

        // then
        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> drugRecordApplicationService.getDrugRecordApplicationById("123"))
            .withMessage("Cannot find drug record application with id: 123");
    }
}
