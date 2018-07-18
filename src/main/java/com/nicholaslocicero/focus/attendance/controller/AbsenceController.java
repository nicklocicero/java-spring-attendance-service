package com.nicholaslocicero.focus.attendance.controller;

import com.nicholaslocicero.focus.attendance.model.dao.AbsenceRepository;
import com.nicholaslocicero.focus.attendance.model.dao.StudentRepository;
import com.nicholaslocicero.focus.attendance.model.entity.Absence;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
