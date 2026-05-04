package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.*;
import com.covoLiv.backend.entity.*;
import com.covoLiv.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final UtilisateurRepository utilisateurRepository;

    public EvaluationResponse creerEvaluation(EvaluationRequest request, String emailEvaluateur) {
        Utilisateur evaluateur = utilisateurRepository.findByEmail(emailEvaluateur)
                .orElseThrow(() -> new RuntimeException("Évaluateur non trouvé"));
        Utilisateur evalue = utilisateurRepository.findById(request.getEvalueId())
                .orElseThrow(() -> new RuntimeException("Utilisateur évalué non trouvé"));

        Evaluation evaluation = Evaluation.builder()
                .note(request.getNote())
                .commentaire(request.getCommentaire())
                .dateEvaluation(LocalDateTime.now())
                .evaluateur(evaluateur)
                .evalue(evalue)
                .build();

        evaluationRepository.save(evaluation);

        // Mettre à jour la note moyenne
        List<Evaluation> evals = evaluationRepository.findByEvalue(evalue);
        float moyenne = (float) evals.stream().mapToInt(Evaluation::getNote).average().orElse(0.0);
        evalue.setNoteMoyenne(moyenne);
        utilisateurRepository.save(evalue);

        return mapToResponse(evaluation);
    }

    public List<EvaluationResponse> getEvaluationsUtilisateur(Long userId) {
        Utilisateur u = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return evaluationRepository.findByEvalue(u)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private EvaluationResponse mapToResponse(Evaluation e) {
        EvaluationResponse r = new EvaluationResponse();
        r.setId(e.getId());
        r.setNote(e.getNote());
        r.setCommentaire(e.getCommentaire());
        r.setDateEvaluation(e.getDateEvaluation());
        r.setNomEvaluateur(e.getEvaluateur().getNom());
        r.setNomEvalue(e.getEvalue().getNom());
        return r;
    }
}