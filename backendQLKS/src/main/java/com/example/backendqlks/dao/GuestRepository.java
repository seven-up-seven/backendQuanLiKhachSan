package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Integer> {
    List<Guest> findGuestsByNameContainingIgnoreCase(String name);

    Optional<Guest> findByPhoneNumber(String phoneNumber);

    Optional<Guest> findByIdentificationNumber(String identificationNumber);

    Optional<Guest> findByEmailContainingIgnoreCase(String email);

    Optional<Guest> findByAccountId(Integer accountId);

    @Query(value = """
    select *
    from guest g
    where (:id is null or g.id = :id)\s
        and (:phoneNumber is null or g.PHONE_NUMBER = :phoneNumber)
        and (:identificationNumber is null or g.IDENTIFICATION_NUMBER = :identificationNumber)
        and (:email is null or g.EMAIL like concat('%', :email, '%'))
        and (:accountId is null or g.ACCOUNT_ID = :accountId)
   \s""", nativeQuery = true)
    List<Guest> findByMultipleCriteria(
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("phoneNumber") String phoneNumber,
            @Param("identificationNumber") String identificationNumber,
            @Param("email")String email,
            @Param("accountId") Integer accountId
    );
}
