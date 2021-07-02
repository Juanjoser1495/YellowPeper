package com.yellowpeper.fundstransferapp.repository;

import com.yellowpeper.fundstransferapp.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDataRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(nativeQuery = true, value = "Select count(t.id_transaction)  from TRANSACTIONS t left join ACCOUNTS a on a.id_account = t.account_origin where t.date_transaction = CURRENT_DATE() and a.account_number = :accountOrigin ")
    int countForAccountOriginAndDate(String accountOrigin);
}
