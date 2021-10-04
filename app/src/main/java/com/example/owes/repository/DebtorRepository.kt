package com.example.owes.repository

import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.db.DebtorDao
import com.example.owes.data.model.entities.PartialPayment
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtorRepository @Inject constructor(private val debtorDao: DebtorDao): DebtorRepositoryImpl {


     fun insertDebtor(debtor: Debtor) {
         CoroutineScope(Dispatchers.IO).launch {
             debtorDao.addDebtor(debtor)
         }
     }

    fun insertPPayment(partialPayment: PartialPayment) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDao.addPPayment(partialPayment)
        }
    }

     fun deleteDebtor(debtorId: Int)  {
         CoroutineScope(Dispatchers.IO).launch {
             debtorDao.deleteDebtor(debtorId)
         }
     }

    fun deletePpaymentsForDebtor(debtorId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDao.deletePPaymentsForDebtor(debtorId)
        }
    }

    fun deletepPayment(ppayment: PartialPayment)  {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDao.deletePartialPayment(ppayment)
        }
    }


    fun updateDebtor(debtor: Debtor) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDao.updateDebtor(debtor)
        }
    }

    override fun getAllDebtors() = runBlocking {
        debtorDao.getAllDebtors()
    }

    fun isDebtorExisting(debtorName:String) = runBlocking {
        debtorDao.isDebtorExisting(debtorName)
    }

     fun getAllPayments() = debtorDao.getAllUnpaidDebtors()
     fun getAllPaidDebts() = debtorDao.getAllPaidDebtors()
     fun getSingleDebtor(debtorId: Int) = debtorDao.getSingleDebtor(debtorId)
     fun getPPayments(debtorId: Int) = debtorDao.getPPaymentsForDebtor(debtorId)

    fun getIncomeMoney(): List<Double> = runBlocking {
        debtorDao.getIncomeAmount()
    }


    fun getOutcomeMoney(): List<Double> = runBlocking {
        debtorDao.getOutcomeAmount()
    }


}