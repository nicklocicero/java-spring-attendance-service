package com.nicholaslocicero.focus.attendance.model.dao;

import com.nicholaslocicero.focus.attendance.model.entity.Absence;
import org.springframework.data.repository.CrudRepository;

public interface AbsenceRepository extends CrudRepository<Absence, Long> {

}
