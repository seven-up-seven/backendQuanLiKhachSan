package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.entity.enums.Sex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Page<Staff> findByFullName(String fullName, Pageable pageable);

    Page<Staff> findByAge(int age, Pageable pageable);

    Page<Staff> findByIdentificationNumberContaining(String identificationNumber, Pageable pageable);

    Page<Staff> findByAddressContainingIgnoreCase(String address, Pageable pageable);

    Page<Staff> findBySex(Sex sex, Pageable pageable);

    Page<Staff> findBySalaryMultiplier(double salaryMultiplier, Pageable pageable);

    Page<Staff> findByPositionId(Integer positionId, Pageable pageable);

    Page<Staff> findByAccountIdIsNull(Pageable pageable);

    List<Staff> findByFullNameContainingIgnoreCase(String fullName);

    Optional<Staff> findByAccountId(Integer accountId);

    boolean existsByIdentificationNumber(String identificationNumber);
}
