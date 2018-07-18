package com.nicholaslocicero.focus.attendance.controller;

import com.nicholaslocicero.focus.attendance.model.dao.StudentRepository;
import com.nicholaslocicero.focus.attendance.model.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ExposesResourceFor(Student.class)
@RequestMapping("/students")
public class StudentController {

  private StudentRepository studentRepository;

  @Autowired
  public StudentController(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @GetMapping
  public  Iterable<Student> list() {
    return studentRepository.findAllByOrderByLastNameAscFirstNameAsc();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Student> post(@RequestBody Student student) {
    studentRepository.save(student);
    return ResponseEntity.created(student.getHref()).body(student);
  }

  @GetMapping("{student_id}")
  public Student get(@PathVariable("student_id") long studentId) {
    return studentRepository.findById(studentId).get();
  }

  @PatchMapping(value = "{studentId}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public Student patch(@PathVariable("studentId") long studentId,
      @RequestBody Student update) {
    Student student = studentRepository.findById(studentId).get();
    student.patch(update);
    return studentRepository.save(student);
  }

  @DeleteMapping("{studentId}") // @RequestMapping(method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("studentId") long studentId) {
    studentRepository.delete(studentRepository.findById(studentId).get());
  }
}
