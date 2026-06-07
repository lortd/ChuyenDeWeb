package com.example.dailydictation.service;

import com.example.dailydictation.dto.request.CommentRequest;
import com.example.dailydictation.dto.request.CommentRequestUpdate;
import com.example.dailydictation.dto.response.CommentResponse;
import com.example.dailydictation.dto.response.CommentResponseShow;
import com.example.dailydictation.entity.*;
import com.example.dailydictation.mapper.CommentMapper;
import com.example.dailydictation.repository.*;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentClosureRepository closureRepository;
    @Autowired
    private NotificationRepository notificationRepository;


    public CommentResponse comment(CommentRequest commentRequest) {
        User user = userRepository.findUserById(commentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findCourseById(commentRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        String message = user.getUserName();

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setCourse(course);
        comment.setUser(user);
        comment.setCreateDate(LocalDateTime.now());

        Notification notification = null;

        // Nếu đây là reply thì gửi notification cho người viết comment cha
        if (commentRequest.getParentId() != null) {
            Comment parent = commentRepository.findCommentById(commentRequest.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));

            comment.setParent(parent);

            User parentUser = parent.getUser();

            // Nếu user đang comment KHÔNG PHẢI là chính người viết comment cha → gửi noti
            if (user.getId() != parentUser.getId()) {
                Notification createNotification = Notification.builder()
                        .createdAt(LocalDateTime.now())
                        .course(course)
                        .user(parentUser) // người nhận thông báo
                        .triggerUser(user) // người tạo hành động
                        .message(message + " đã nhắc đến bạn trong một bình luận")
                        .build();
                notification = notificationRepository.save(createNotification);
            }
        }

        comment.setNotification(notification);
        commentRepository.save(comment);

        // Closure table insert
        if (commentRequest.getParentId() != null) {
            List<CommentClosure> ancestors = closureRepository.findAllByIdDescendantId(commentRequest.getParentId());
            for (CommentClosure ac : ancestors) {
                closureRepository.save(new CommentClosure(ac.getAncestorId(), comment.getId(), ac.getDepth() + 1));
            }
        }

        // Always insert self link
        closureRepository.save(new CommentClosure(comment.getId(), comment.getId(), 0));

        return commentMapper.toCommentResponse(comment);
    }


    public List<CommentResponse> getAllCommentResponses(int courseId) {
        List<Comment> comments = commentRepository.findByCourseId(courseId);
        return comments.stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }

    public CommentResponse updateComment(CommentRequestUpdate commentRequestUpdate) {
        Comment comment = commentRepository.findByIdAndUserId(commentRequestUpdate.getCommentId(), commentRequestUpdate.getUserId());
        comment.setContent(commentRequestUpdate.getContent());
        comment.setCreateDate(LocalDateTime.now());
        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(int commentId, int userId) {
        commentRepository.deleteByIdAndUserId(commentId, userId);
    }


    public List<CommentResponseShow> showCommentUser(int userId) {
        List<Comment> comments = commentRepository.findAllByUserId(userId);
        List<CommentResponseShow> commentResponseShows = new ArrayList<>();
        for (Comment comment : comments) {
          CommentResponseShow commentResponseShow = new CommentResponseShow();
          commentResponseShow.setCreateDate(comment.getCreateDate());
            commentResponseShow.setId(comment.getId());
            commentResponseShow.setContent(comment.getContent());
            commentResponseShow.setUserName(comment.getUser().getUserName());
            commentResponseShow.setCourseId(comment.getCourse().getId());
            commentResponseShow.setCourseName(comment.getCourse().getName());
            commentResponseShows.add(commentResponseShow);

        }
        return commentResponseShows;
    }


    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        return comments.stream()
                .map(comment -> buildCommentResponse(comment))
                .toList();
    }
    private CommentResponse buildCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .courseId(comment.getCourse().getId())
                .createDate(comment.getCreateDate())
                .build();
    }
}
