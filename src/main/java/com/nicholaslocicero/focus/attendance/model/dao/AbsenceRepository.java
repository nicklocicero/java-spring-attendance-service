package com.nicholaslocicero.focus.attendance.model.dao;

import com.nicholaslocicero.focus.attendance.model.entity.Absence;
import com.nicholaslocicero.focus.attendance.model.entity.Student;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AbsenceRepository extends CrudRepository<Absence, Long> {

  Optional<Absence> findByStudentAndId(Student student, long id);

}
