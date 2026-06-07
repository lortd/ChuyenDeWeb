package com.example.dailydictation.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.dailydictation.dto.request.CourseRequest;
import com.example.dailydictation.dto.response.CourseResponse;
import com.example.dailydictation.dto.response.CourseResponseList;
import com.example.dailydictation.entity.Course;
import com.example.dailydictation.entity.Practice;
import com.example.dailydictation.entity.Section;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.enums.EStatus;
import com.example.dailydictation.mapper.CourseMapper;
import com.example.dailydictation.repository.CourseRepository;
import com.example.dailydictation.repository.PracticeRepository;
import com.example.dailydictation.repository.SectionRepository;
import com.example.dailydictation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PracticeRepository practiceRepository;

    public Course createCourse(CourseRequest courseRequest) throws IOException {
        Section section = sectionRepository.findById(courseRequest.getSectionId()).orElseThrow(() -> new RuntimeException("not found section"));

        List<String> listSentenceAudio = new ArrayList<>();
        Map mainAudio = cloudinary.uploader().upload(
                courseRequest.getMainAudio().getBytes(),
                ObjectUtils.asMap("resource_type", "auto")
        );

        String audioUrl = mainAudio.get("secure_url").toString();

        for (MultipartFile sentenceAudioFile : courseRequest.getSentenceAudios()) {
            Map sentenceAudio = cloudinary.uploader().upload(
                    sentenceAudioFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")
            );
            listSentenceAudio.add(sentenceAudio.get("secure_url").toString());
        }

        Course course = new Course();

        course.setName(courseRequest.getName());
        course.setLevel(courseRequest.getLevel());
        course.setCountOfSentence(courseRequest.getCountOfSentence());
        course.setMainAudio(audioUrl);
        course.setSentences(courseRequest.getSentences());
        course.setSentenceAudios(listSentenceAudio);
        course.setTranscript(courseRequest.getTranscript());
        course.setSection(section);
        return courseRepository.save(course);

    }


    public CourseResponse getCourse(int courseId) {

        Course course = courseRepository.getCourseById(courseId);

        return courseMapper.toCourseResponse(course);
    }

    public List<String> getListSentence(int courseId) {

        return courseRepository.findSentencesByCourseId(courseId);
    }

    private int currentIndex = 0;

    public String checkSentence(int courseId, String userSentence,int userId) {
        User user = userRepository.findUserById(userId).orElseThrow(()
                -> new RuntimeException("user not found"));
        Course course = courseRepository.findCourseById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        List<String> listSentenceOfProgram = getListSentence(courseId);
        if (currentIndex >= listSentenceOfProgram.size()) {
            boolean exists = practiceRepository.existsByUserIdAndCourseId(userId, courseId);
            if (!exists) {
                Practice practice = Practice.builder()
                        .user(user)
                        .status(EStatus.finished)
                        .course(course)
                        .score(100)
                        .build();
                practiceRepository.save(practice);
                return "All sentences completed!";
            }
            return "All sentences completed!";
        }
        String correctSentence = listSentenceOfProgram.get(currentIndex);
        if (userSentence.trim().equalsIgnoreCase(correctSentence.trim())) {
            currentIndex++;
            System.out.println(currentIndex);
            if (currentIndex >= listSentenceOfProgram.size()) {
                boolean exists = practiceRepository.existsByUserIdAndCourseId(userId, courseId);
                if (!exists) {
                    Practice practice = Practice.builder()
                            .user(user)
                            .status(EStatus.finished)
                            .course(course)
                            .score(100)
                            .build();
                    practiceRepository.save(practice);
                    return "All sentences completed!";
                }
            }
            return " Correct! Go to next sentence.";
        } else {
            return " Incorrect. Try again";
        }
    }

    public String getAudioSentence(int courseId) {
        List<String> listAudioSentence = courseRepository.findSentenceAudiosByCourseId(courseId);
        if (listAudioSentence.isEmpty() || currentIndex >= listAudioSentence.size()) {

            return "No audio available or all sentences completed!";
        }
        System.out.println(currentIndex);
        return listAudioSentence.get(currentIndex);

    }

    public byte[] downloadFile(String url) throws IOException {
        // 1. Tạo URL object từ chuỗi URL
        URL fileUrl = new URL(url);

        // 2. Mở kết nối HTTP tới URL
        HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
        connection.setRequestMethod("GET");

        // 3. Đọc dữ liệu từ InputStream và lưu vào ByteArrayOutputStream
        try (InputStream inputStream = connection.getInputStream()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StreamUtils.copy(inputStream, outputStream);
            return outputStream.toByteArray(); // Trả về mảng byte
        }
    }

    public String getTranscript(int courseId) {
        return courseRepository.getTranscriptById(courseId);

    }

    public String getMainAudio(int courseId) {

        return courseRepository.getMainAudioById(courseId);
    }

    public List<CourseResponseList> showAllCourse (int sectionId){
        List<Course> courses = courseRepository.findBySectionId(sectionId);
        List<CourseResponseList> courseResponseList = new ArrayList<>();
        for (Course course: courses) {
            CourseResponseList courseList = new CourseResponseList();
            courseList.setId(course.getId());
            courseList.setName(course.getName());
            courseList.setLevel(course.getLevel());
            courseResponseList.add(courseList);
        }
         return courseResponseList;
    }

    public List<CourseResponseList> getAllCourseNames() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> new CourseResponseList(course.getId(), course.getName()))
                .collect(Collectors.toList());
    }


    public void deleteCourse(int courseId) {
        // Kiểm tra xem khóa học có tồn tại trong cơ sở dữ liệu không
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("Course not found");
        }

        // Xóa khóa học
        courseRepository.deleteById(courseId);
    }
}
