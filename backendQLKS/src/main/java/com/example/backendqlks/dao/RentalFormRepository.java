package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RentalExtensionForm;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.RentalFormDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RentalFormRepository extends JpaRepository<RentalForm, Integer> {
    Page<RentalForm> findAll(Pageable pageable);

    Page<RentalForm> findByRentalDate(LocalDateTime rentalDate, Pageable pageable);

    Optional<RentalForm> findByInvoiceDetailId(Integer invoiceDetailId);

    @Query(value = """
                select rf from RentalForm rf
                join fetch rf.room where (rf.room.id = :roomId or :roomId is null) and (rf.room.name like concat('%', :roomName, '%') or :roomName is null)
                and (rf.id = :rfId or :rfId is null)
""", nativeQuery = false)
    List<RentalForm> findByRoomIdAndRoomNameAndRentalFormId(
            @Param("roomId") Integer roomId,
            @Param("roomName") String roomName,
            @Param("rfId") Integer rfId
    );

    @Query(value = "select rf from RentalForm rf join fetch rf.room where rf.id = :id")
    Optional<RentalForm> findByIdWithRoom(@Param("id") Integer id);
}
