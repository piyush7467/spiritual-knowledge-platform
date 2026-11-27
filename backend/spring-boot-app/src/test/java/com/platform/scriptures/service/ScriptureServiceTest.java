package com.platform.scriptures.service;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.audit.AuditLogService;
import com.platform.common.audit.EvidenceStatus;
import com.platform.scriptures.dto.ScriptureDtos.CreatePassageRequest;
import com.platform.scriptures.dto.ScriptureDtos.PassageView;
import com.platform.scriptures.dto.ScriptureDtos.ReferenceInput;
import com.platform.scriptures.entity.SourcePassageEntity;
import com.platform.scriptures.entity.SourceReferenceEntity;
import com.platform.scriptures.repository.SourcePassageRepository;
import com.platform.scriptures.repository.SourceReferenceRepository;
import com.platform.sources.service.SourceQueryPort;
import com.platform.sources.service.SourceQueryPort.EditionProvenance;
import com.platform.translations.service.TranslationQueryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScriptureServiceTest {

    @Mock
    private SourcePassageRepository passageRepository;
    @Mock
    private SourceReferenceRepository referenceRepository;
    @Mock
    private SourceQueryPort sourceQueryPort;
    @Mock
    private TranslationQueryPort translationQueryPort;
    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private ScriptureService scriptureService;

    @Test
    void createPassagePreservesPrintedVsPdfPageAndStartsAsNeedsReview() {
        UUID editionPublicId = UUID.randomUUID();
        UUID sourcePublicId = UUID.randomUUID();
        EditionProvenance provenance = new EditionProvenance(
                10L, editionPublicId, sourcePublicId, "Rigveda Samhita", "SCRIPTURE",
                "Standard Reference Edition", "Archival Press", 2014, "sa",
                "Public domain reference", true
        );

        when(sourceQueryPort.resolveEditionByPublicId(editionPublicId)).thenReturn(provenance);
        when(passageRepository.save(any(SourcePassageEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(referenceRepository.save(any(SourceReferenceEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(referenceRepository.findAllByPassageId(any())).thenReturn(List.of());
        when(translationQueryPort.findAllByPassageId(any())).thenReturn(List.of());

        PlatformUserPrincipal admin = new PlatformUserPrincipal(1L, UUID.randomUUID(), "admin@test.local", List.of("SUPER_ADMIN"));
        CreatePassageRequest req = new CreatePassageRequest(
                editionPublicId,
                "sa",
                "ORIGINAL_TEXT",
                "सहस्रशीर्षा पुरुषः",
                "Opening verse",
                "Next verse",
                "642",
                658,
                null,
                false,
                new ReferenceInput("RIGVEDA-10-90-1", "Mandal", "10", "Sukta", "90", "Mantra", "1", null, null, "Rigveda 10.90.1")
        );

        PassageView view = scriptureService.createPassage(req, admin);

        assertEquals(EvidenceStatus.NEEDS_REVIEW, view.verificationStatus());
        assertEquals("642", view.printedPageLabel());
        assertEquals(658, view.pdfPageNumber());
        assertEquals("Rigveda Samhita", view.sourceTitle());
        verify(auditLogService).record(eq(1L), eq("SUPER_ADMIN"), eq("PASSAGE_CREATED"), eq("PASSAGE"), any(), anyMap());
    }
}

