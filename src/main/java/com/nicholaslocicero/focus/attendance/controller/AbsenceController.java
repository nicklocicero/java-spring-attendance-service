package com.nicholaslocicero.focus.attendance.controller;

import com.nicholaslocicero.focus.attendance.model.dao.AbsenceRepository;
import com.nicholaslocicero.focus.attendance.model.dao.StudentRepository;
import com.nicholaslocicero.focus.attendance.model.entity.Absence;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ExposesResourceFor(Absence.class)
@RequestMapping("/students/{studentId}/absences")
public class AbsenceController {

  private StudentRepository studentRepository;
  private AbsenceRepository absenceRepository;

  @Autowired
  public AbsenceController(StudentRepository studentRepository,
      AbsenceRepository absenceRepository) {
    this.studentRepository = studentRepository;
    this.absenceRepository = absenceRepository;
  }

  @GetMapping
  public List<Absence> list(@PathVariable("studentId") long studentId) {
    return studentRepository.findById(studentId)
        .map(student -> student.getAbsences())
        .get();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Absence> post(
      @PathVariable("studentId") long studentId,
      @RequestBody Absence absence) {
        return studentRepository.findById(studentId)
        .map(student -> {
            absence.setStudent(student);
            absenceRepository.save(absence);
            return ResponseEntity.created(absence.getHref()).body(absence);
          }).get();
  }

  @GetMapping("{absenceId}")
  public Absence get(
    @PathVariable("studentId") long studentId,
    @PathVariable("absenceId") long absenceId) {
    return studentRepository.findById(studentId).map(student -> absenceRepository.findByStudentAndId(student, absenceId).get()).get();
  }

  @GetMapping(value = "{absenceId}/excused", produces = MediaType.APPLICATION_JSON_VALUE)
  public boolean isExcusedJson(
      @PathVariable("studentId") long studentId,
      @PathVariable("absenceId") long absenceId) {
    return get(studentId, absenceId).isExcused();
  }

  @GetMapping(value = "{absenceId}/excused", produces = MediaType.TEXT_HTML_VALUE)
  public String isExcusedHtml(
      @PathVariable("studentId") long studentId,
      @PathVariable("absenceId") long absenceId) {
    return String.format("<h1>%s!!</h1>", Boolean.toString(isExcusedJson(studentId, absenceId)));
  }

  @PutMapping(value = "{absenceId}/excused", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public boolean setExcusedJson(@PathVariable("studentId") long studentId, @PathVariable("absenceId") long absenceId, @RequestBody boolean excused) {
    Absence absence = get(studentId, absenceId);
    absence.setExcused(excused);
    return absenceRepository.save(absence).isExcused();
  }

  @PutMapping(value = "{absenceId}/excused", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public String setExcusedText(@PathVariable("studentId") long studentId, @PathVariable("absenceId") long absenceId, @RequestBody String excused) {
    return Boolean.toString(setExcusedJson(studentId, absenceId, Boolean.parseBoolean(excused)));
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound(){}


}
