package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.TransactionResponseDTO;
import com.alkemy.wallet.dto.TransactionUpdateDTO;
import com.alkemy.wallet.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    @Autowired
    ModelMapper modelMapper;

    // Entity2DTO
    public TransactionDTO transEntity2DTO(Transaction trans) {
        return modelMapper.map(trans, TransactionDTO.class);
    }

    // DTO2Entity
    public Transaction transDTO2Entity(TransactionDTO dto) {
        return modelMapper.map(dto, Transaction.class);
    }

    // ListDTO2ListEntity
    public List<Transaction> transListDTO2ListEntity(List<TransactionDTO> dtos) {
        List<Transaction> result = new ArrayList<>();
        dtos.forEach((TransactionDTO dto) -> result.add(transDTO2Entity(dto)));
        return result;
    }

    // ListEntity2ListDTO
    public List<TransactionDTO> transListEntity2ListDTO(List<Transaction> transList) {
        List<TransactionDTO> result = new ArrayList<>();
        transList.forEach((Transaction trans) -> result.add(transEntity2DTO(trans)));
        return result;
    }

    // TODO : Check Ids...
    public Transaction transCreateDTO2Entity(TransactionCreateDTO dto) {
        return modelMapper.map(dto, Transaction.class);
    }

    public TransactionResponseDTO transEntity2ResponseDTO(Transaction lastTrans) {
        return modelMapper.map(lastTrans, TransactionResponseDTO.class);
    }
}
