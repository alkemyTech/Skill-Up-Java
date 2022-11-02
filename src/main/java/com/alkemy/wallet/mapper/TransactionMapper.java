package com.alkemy.wallet.mapper;

import com.alkemy.wallet.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {


    //Entity2DTO
    public TransactionDTO transEntity2DTO(Transaction trans) {

        TransactionDTO transDTO = new TransactionDTO();
        transDTO.setAmount(trans.getAmount());
        transDTO.setType(trans.getType());
        transDTO.setId(trans.getId());
        transDTO.setDescription(trans.getDescription());
        transDTO.setUser(trans.getUser());
        transDTO.setAccount(trans.getAccount());
        transDTO.setTransactionDate(trans.getTransactonDate());
        return transDTO;
    }
    //DTO2Entity
    public Transaction transDTO2Entity(TransactionDTO dto) {

        Transaction transEntity = new Transaction();
        transEntity.setAmount(dto.getAmount());
        transEntity.setType(dto.getType());
        transEntity.setId(dto.getId());
        transEntity.setDescription(dto.getDescription());
        transEntity.setUser(dto.getUser());
        transEntity.setAccount(dto.getAccount());
        transEntity.setTransactionDate(dto.getTransactonDate());
        return transEntity;
    }
    //ListDTO2ListEntity
    public List<Transaction> transListDTO2ListEntity(List<TransactionDTO> dtos){
        List<Transaction> result = new ArrayList<>();

        for(TransactionDTO dto : dtos){
            result.add(transDTO2Entity(dto));
        }

        return result;
    }
    //ListEntity2ListDTO
    public List<TransactionDTO> transListEntity2ListDTO(List<Transaction> transList){
        List<TransactionDTO> result = new ArrayList<>();

        for(Transaction trans : transList){
            result.add(transEntity2DTO(trans));
        }
        return result;
    }

    //TODO
    public Transaction transUpdateDTO2Entity(TransactionUpdateDTO transDTO, Transaction transEntity) {
        Transaction transUpdated = transEntity.setDescription(transDTO.getDescription());

        return transUpdated;
    }

    //TODO : Check Ids...
    public Transaction transCreateDTO2Entity(TransactionCreateDTO dto) {
        Transaction transEntity = new Transaction();
        transEntity.setAmount(dto.getAmount());
        transEntity.setType(dto.getType());
        transEntity.setDescription(dto.getDescription());
        transEntity.setUser(dto.getUser());
        transEntity.setAccount(dto.getAccount());
        transEntity.setTransactionDate(dto.getTransactonDate());

        return transEntity;
    }
}
