package com.example.backendqlks.service;

import com.example.backendqlks.dao.InvoiceDetailRepository;
import com.example.backendqlks.dao.InvoiceRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.invoiceDetail.InvoiceDetailDto;
import com.example.backendqlks.dto.invoiceDetail.ResponseInvoiceDetailDto;
import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.InvoiceDetail;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.InvoiceDetailMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final HistoryService historyService;

    public InvoiceDetailService(InvoiceDetailRepository invoiceDetailRepository,
                                InvoiceDetailMapper invoiceDetailMapper,
                                RentalFormRepository rentalFormRepository,
                                HistoryService historyService,
                                InvoiceRepository invoiceRepository) {
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.invoiceDetailMapper = invoiceDetailMapper;
        this.rentalFormRepository = rentalFormRepository;
        this.invoiceRepository = invoiceRepository;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public ResponseInvoiceDetailDto get(int id) {
        var existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice detail id"));
        return invoiceDetailMapper.toResponseDto(existingInvoiceDetail);
    }

    @Transactional(readOnly = true)
    public Page<ResponseInvoiceDetailDto> getAllPage(Pageable pageable) {
        var invoiceDetailPage = invoiceDetailRepository.findAll(pageable);
        List<ResponseInvoiceDetailDto> invoiceDetails = invoiceDetailMapper.toResponseDtoList(invoiceDetailPage.getContent());
        return new PageImpl<>(invoiceDetails, pageable, invoiceDetailPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ResponseInvoiceDetailDto> getAll() {
         return invoiceDetailMapper.toResponseDtoList(invoiceDetailRepository.findAll());
    }

    public ResponseInvoiceDetailDto create(InvoiceDetailDto invoiceDetailDto, int impactorId, String impactor) {
        var rentalForm = rentalFormRepository.findById(invoiceDetailDto.getRentalFormId())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect rental form id"));
        if(rentalForm.getIsPaidAt() == null){
            throw new IllegalArgumentException("Rental form has been paid ");
        }
        var invoiceDetail = invoiceDetailMapper.toEntity(invoiceDetailDto);
        String content = String.format(
                "Số ngày thuê: %d; Chi phí đặt phòng: %.2f; Mã phiếu thuê: %d; Mã hóa đơn: %d",
                invoiceDetailDto.getNumberOfRentalDays(),
                invoiceDetailDto.getReservationCost(),
                invoiceDetailDto.getRentalFormId(),
                invoiceDetailDto.getInvoiceId()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Chi tiết hóa đơn")
                .affectedObjectId(invoiceDetail.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        invoiceDetailRepository.save(invoiceDetail);
        rentalForm.setIsPaidAt(LocalDateTime.now());
        rentalForm.getRoom().setRoomState(RoomState.BEING_CLEANED);
        rentalFormRepository.save(rentalForm);
        return invoiceDetailMapper.toResponseDto(invoiceDetail);
    }

    public ResponseInvoiceDetailDto update(int id,InvoiceDetailDto invoiceDetailDto, int impactorId, String impactor) {
        var existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice detail id"));
        invoiceDetailMapper.updateEntityFromDto(invoiceDetailDto, existingInvoiceDetail);
        invoiceDetailRepository.save(existingInvoiceDetail);
        List<String> changes = new ArrayList<>();
        if (!Objects.equals(existingInvoiceDetail.getNumberOfRentalDays(), invoiceDetailDto.getNumberOfRentalDays())) {
            changes.add("Số ngày thuê: " + existingInvoiceDetail.getNumberOfRentalDays() + " → " + invoiceDetailDto.getNumberOfRentalDays());
        }
        if (!Objects.equals(existingInvoiceDetail.getReservationCost(), invoiceDetailDto.getReservationCost())) {
            changes.add("Chi phí đặt phòng: " + existingInvoiceDetail.getReservationCost() + " → " + invoiceDetailDto.getReservationCost());
        }
        if (!Objects.equals(existingInvoiceDetail.getRentalFormId(), invoiceDetailDto.getRentalFormId())) {
            changes.add("Mã phiếu thuê: " + existingInvoiceDetail.getRentalFormId() + " → " + invoiceDetailDto.getRentalFormId());
        }
        if (!Objects.equals(existingInvoiceDetail.getInvoiceId(), invoiceDetailDto.getInvoiceId())) {
            changes.add("Mã hóa đơn: " + existingInvoiceDetail.getInvoiceId() + " → " + invoiceDetailDto.getInvoiceId());
        }
        String content = changes.isEmpty() ? "Không có thay đổi." : String.join("; ", changes);
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Chi tiết hóa đơn")
                .affectedObjectId(existingInvoiceDetail.getId())
                .action(Action.UPDATE)
                .content(content)
                .build();
        historyService.create(history);
        return invoiceDetailMapper.toResponseDto(existingInvoiceDetail);
    }

    //TODO: modify later
    public void delete(int id, int impactorId, String impactor) {
        var existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice detail id"));
        invoiceDetailRepository.delete(existingInvoiceDetail);
        String content = String.format(
                "Số ngày thuê: %d; Chi phí đặt phòng: %.2f; Mã phiếu thuê: %d; Mã hóa đơn: %d",
                existingInvoiceDetail.getNumberOfRentalDays(),
                existingInvoiceDetail.getReservationCost(),
                existingInvoiceDetail.getRentalFormId(),
                existingInvoiceDetail.getInvoiceId()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Chi tiết hóa đơn")
                .affectedObjectId(existingInvoiceDetail.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        // nếu xoá invoice detail thì cũng xoá luôn rental form tương ứng
        rentalFormRepository.deleteById(existingInvoiceDetail.getRentalFormId());
        var rentalForm = rentalFormRepository.findById(existingInvoiceDetail.getRentalFormId()).orElse(null);
        if (rentalForm != null) {
            String rentalFormContent = String.format(
                    "Mã phòng: %d; Mã nhân viên: %d; Ngày thuê: %s; Số ngày thuê: %d; Ghi chú: %s",
                    rentalForm.getRoomId(),
                    rentalForm.getStaffId(),
                    rentalForm.getRentalDate().toString(),
                    rentalForm.getNumberOfRentalDays(),
                    rentalForm.getNote() != null ? rentalForm.getNote() : "Không có"
            );
            HistoryDto rentalFormHistory = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Phiếu thuê phòng")
                    .affectedObjectId(rentalForm.getId())
                    .action(Action.DELETE)
                    .content(rentalFormContent)
                    .build();
            historyService.create(rentalFormHistory);
        }
        if(existingInvoiceDetail.getInvoice().getInvoiceDetails().size() == 1) {
            // nếu là invoice detail cuối cùng thì xoá luôn invoice
            invoiceRepository.delete(existingInvoiceDetail.getInvoice());
            Invoice invoice = existingInvoiceDetail.getInvoice();
            if (invoice != null && invoice.getInvoiceDetails().size() == 1) {
                String invoiceContent = String.format(
                        "Tổng chi phí đặt phòng: %.2f; Mã khách thanh toán: %d; Mã nhân viên: %d",
                        invoice.getTotalReservationCost(),
                        invoice.getPayingGuestId(),
                        invoice.getStaffId()
                );
                HistoryDto invoiceHistory = HistoryDto.builder()
                        .impactor(impactor)
                        .impactorId(impactorId)
                        .affectedObject("Hóa đơn")
                        .affectedObjectId(invoice.getId())
                        .action(Action.DELETE)
                        .content(invoiceContent)
                        .build();
                historyService.create(invoiceHistory);
            }
        }
    }

    public List<ResponseInvoiceDetailDto> createInvoiceDetails(int invoiceId, List<Integer> rentalFormIds, int impactorId, String impactor) {
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
                    HistoryDto roomHistory = HistoryDto.builder()
                            .impactor(impactor)
                            .impactorId(impactorId)
                            .affectedObject("Phòng")
                            .affectedObjectId(rentalForm.getRoomId())
                            .action(Action.UPDATE)
                            .content("Trạng thái phòng chuyển thành: Đang dọn dẹp")
                            .build();
                    historyService.create(roomHistory);
                    rentalFormRepository.save(rentalForm);
                    InvoiceDetail invoiceDetail = constructInvoiceDetail(invoiceId, id, rentalForm);
                    invoiceDetailRepository.save(invoiceDetail);
                    String content = String.format(
                            "Số ngày thuê: %d; Chi phí đặt phòng: %.2f; Mã phiếu thuê: %d; Mã hóa đơn: %d",
                            invoiceDetail.getNumberOfRentalDays(),
                            invoiceDetail.getReservationCost(),
                            invoiceDetail.getRentalFormId(),
                            invoiceDetail.getInvoiceId()
                    );
                    HistoryDto history = HistoryDto.builder()
                            .impactor(impactor)
                            .impactorId(impactorId)
                            .affectedObject("Chi tiết hóa đơn")
                            .affectedObjectId(invoiceDetail.getId())
                            .action(Action.CREATE)
                            .content(content)
                            .build();
                    historyService.create(history);
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
