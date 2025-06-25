package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RentalExtensionForm;
import com.example.backendqlks.entity.RentalForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
@RepositoryRestResource(exported = false)
public interface RentalExtensionFormRepository extends JpaRepository<RentalExtensionForm, Integer> {
    List<RentalExtensionForm> findByRentalFormId(int rentalFormId);

    List<RentalExtensionForm> findRentalExtensionFormsByRentalFormId(int rentalFormId);
}
