package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.account.AccountDto;
import com.example.backendqlks.dto.account.ResponseAccountDto;
import com.example.backendqlks.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDto accountDto);
    AccountDto toDto(Account account);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AccountDto accountDto, @MappingTarget Account account);

    ResponseAccountDto toResponseDto(Account account);
    List<ResponseAccountDto> toResponseDtoList(List<Account> accounts);
}
