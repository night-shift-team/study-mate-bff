package com.studyMate.studyMate.domain.question.service;

import com.studyMate.studyMate.domain.question.dto.QuestionDetailDto;
import com.studyMate.studyMate.domain.question.entity.Question;
import com.studyMate.studyMate.domain.question.entity.QuestionFavorite;
import com.studyMate.studyMate.domain.question.repository.QuestionFavoriteRepository;
import com.studyMate.studyMate.domain.question.repository.QuestionRepository;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class QuestionFavoriteService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuestionFavoriteRepository questionFavoriteRepository;

    public List<QuestionDetailDto> getMyFavoriteQuestions(String userId, Integer page, Integer size) {
        User reqUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "createdDt");
        Page<QuestionFavorite> query = questionFavoriteRepository.findByUser(reqUser, pageRequest);

        return query.stream()
                .map(resp -> new QuestionDetailDto(resp.getQuestion(), resp.getCreatedDt()))
                .toList();
    }

    @Transactional
    public boolean toggleQuestionFavorite(String questionId, String userId) {
        Question question = questionRepository.findByQuestionId(questionId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_QUESTION));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));

        Optional<QuestionFavorite> favoriteOpt = questionFavoriteRepository.findByQuestionAndUser(question, user);

        if(favoriteOpt.isPresent()){
            questionFavoriteRepository.delete(favoriteOpt.get());
            return false; // 삭제됨
        } else {
            QuestionFavorite newFavorite = QuestionFavorite.builder()
                    .question(question)
                    .user(user)
                    .build();
            questionFavoriteRepository.save(newFavorite);
            return true; // 추가됨
        }
    }
}
