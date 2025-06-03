package com.example.backendqlks.service;

import com.example.backendqlks.dao.InvoiceDetailRepository;
import com.example.backendqlks.dao.InvoiceRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.invoiceDetail.InvoiceDetailDto;
import com.example.backendqlks.dto.invoiceDetail.ResponseInvoiceDetailDto;
import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.InvoiceDetail;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.InvoiceDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class InvoiceDetailService {
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final RentalFormRepository rentalFormRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailMapper invoiceDetailMapper;
    private final RoomRepository roomRepository;

    public InvoiceDetailService(InvoiceDetailRepository invoiceDetailRepository,
                                InvoiceDetailMapper invoiceDetailMapper,
                                RentalFormRepository rentalFormRepository,
                                RoomRepository roomRepository,
                                InvoiceRepository invoiceRepository) {
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.invoiceDetailMapper = invoiceDetailMapper;
        this.rentalFormRepository = rentalFormRepository;
        this.roomRepository = roomRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional(readOnly = true)
    public ResponseInvoiceDetailDto get(int id) {
        var existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice detail id"));
        return invoiceDetailMapper.toResponseDto(existingInvoiceDetail);
    }

    @Transactional(readOnly = true)
    public List<ResponseInvoiceDetailDto> getAll() {
        var allInvoiceDetails = invoiceDetailRepository.findAll();
        return invoiceDetailMapper.toResponseDtoList(allInvoiceDetails);
    }

    public ResponseInvoiceDetailDto create(InvoiceDetailDto invoiceDetailDto) {
        var rentalForm = rentalFormRepository.findById(invoiceDetailDto.getRentalFormId())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect rental form id"));
//TODO: check xem rental form tương ứng hợp lệ không, nếu không (đã trả rồi) thì quăng ra lỗi
//        if(!rentalForm.getIsPaidAt()){
//            throw new IllegalArgumentException("")
//        }
        var invoiceDetail = invoiceDetailMapper.toEntity(invoiceDetailDto);
        invoiceDetailRepository.save(invoiceDetail);
        return invoiceDetailMapper.toResponseDto(invoiceDetail);
    }

    public ResponseInvoiceDetailDto update(int id,InvoiceDetailDto invoiceDetailDto) {
        var existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice detail id"));
        invoiceDetailMapper.updateEntityFromDto(invoiceDetailDto, existingInvoiceDetail);
        invoiceDetailRepository.save(existingInvoiceDetail);
        return invoiceDetailMapper.toResponseDto(existingInvoiceDetail);
    }

    //TODO: modify later
    public void delete(int id) {
        var existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice detail id"));
            invoiceDetailRepository.delete(existingInvoiceDetail);
        // nếu xoá invoice detail thì cũng xoá luôn rental form tương ứng
        rentalFormRepository.deleteById(existingInvoiceDetail.getRentalFormId());
        if(existingInvoiceDetail.getInvoice().getInvoiceDetails().size() == 1) {
            // nếu là invoice detail cuối cùng thì xoá luôn invoice
            invoiceRepository.delete(existingInvoiceDetail.getInvoice());
        }
    }

    public List<ResponseInvoiceDetailDto> createInvoiceDetails(int invoiceId, List<Integer> rentalFormIds){
        var resultList = new ArrayList<ResponseInvoiceDetailDto>();
        rentalFormIds.stream().filter(Objects::nonNull)
                // check hết 1 lượt, nếu thoả hết mới cho tạo
                .forEach(id -> {
                    var rentalForm = rentalFormRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Incorrect rental form id"));
                    if(rentalForm.getIsPaidAt() != null) {
                        throw new IllegalArgumentException("Rental form has already been paid");
                    }
                });
            rentalFormIds.stream().filter(Objects::nonNull)
                    .forEach(id->{
                        var rentalForm = rentalFormRepository.findById(id).get();
                        // với mỗi rental form, cập nhật thời gian thanh toán, cập nhật trạng thái phòng thành đang
                        // dọn dẹp, với mỗi invoice detail, tính tiền dựa trên tổng ngày thuê của rental form
                        // và rental extension nếu có, nhân cho giá tiền phòng
                        rentalForm.setIsPaidAt(LocalDateTime.now());
                        rentalForm.getRoom().setRoomState(RoomState.BEING_CLEANED);
                        rentalFormRepository.save(rentalForm);
                        InvoiceDetail invoiceDetail = constructInvoiceDetail(invoiceId, id, rentalForm);
                        invoiceDetailRepository.save(invoiceDetail);
                        var responseDto = invoiceDetailMapper.toResponseDto(invoiceDetail);
                        resultList.add(responseDto);
                    });
            return resultList;
    }

    private InvoiceDetail constructInvoiceDetail(int invoiceId, Integer id, RentalForm rentalForm) {
        short numberOfRentalDays = rentalForm.getNumberOfRentalDays();
        var extensionForms = rentalForm.getRentalExtensionForms();
        for (var extensionForm : extensionForms) {
            if(extensionForm != null) numberOfRentalDays += extensionForm.getNumberOfRentalDays();
        }
        var price = rentalForm.getRoom().getRoomType().getPrice() * numberOfRentalDays;
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        // set các trường cần thiết cho invoice detail
        invoiceDetail.setNumberOfRentalDays(numberOfRentalDays);
        invoiceDetail.setInvoiceId(invoiceId);
        invoiceDetail.setReservationCost(price);
        invoiceDetail.setRentalFormId(id);
        return invoiceDetail;
    }
}
