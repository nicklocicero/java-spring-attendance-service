package com.nicholaslocicero.focus.attendance.model.dao;

import com.nicholaslocicero.focus.attendance.model.entity.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {

  Iterable<Student> findAllByOrderByLastNameAscFirstNameAsc(); // gets everything ordered by lastName then firstName

}
