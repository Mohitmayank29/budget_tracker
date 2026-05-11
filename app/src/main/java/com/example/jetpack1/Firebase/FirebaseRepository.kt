package com.example.jetpack1.Firebase

import com.example.jetpack1.Database.Table.TransactionTable
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository {

    private val db = FirebaseDatabase.getInstance().reference

    fun saveTransaction(transaction: TransactionTable) {
        db.child("transactions")
            .child(transaction.id.toString())
            .setValue(transaction)
    }

    fun getTransactions(onResult: (List<TransactionTable>) -> Unit) {
        db.child("transactions")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = mutableListOf<TransactionTable>()
                snapshot.children.forEach {
                    val item = it.getValue(TransactionTable::class.java)
                    item?.let { list.add(it) }
                }
                onResult(list)
            }
    }
}