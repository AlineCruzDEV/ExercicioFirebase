package com.digitalhouse.firebasestorage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.digitalhouse.firebasestorage.databinding.ActivityHomeBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var cr: CollectionReference
    private var TAG = "Home Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        config()

        binding.btnSave.setOnClickListener {
            var prod = getData()
            sendProd(prod)
        }

        readProds()

        binding.btnUpdate.setOnClickListener {
            var prod = getData()
            updateProd(prod)
        }

        binding.btnDelete.setOnClickListener {
            deleteProd()
        }
    }

    fun config(){
        db = FirebaseFirestore.getInstance()
        cr = db.collection("produtos")
    }

    fun getData(): MutableMap<String, Any>{
        val prod: MutableMap<String, Any> = HashMap()

        prod["nome"] = binding.edNomeProd.text.toString()
        prod["qtd"] = binding.edQtdProd.text.toString()
        prod["preco"] = binding.edPrecoProd.text.toString()

        return prod
    }

    fun sendProd(prod: MutableMap<String, Any>){
        val nome = binding.edNomeProd.text.toString()

        cr.document(nome).set(prod).addOnSuccessListener {

        }.addOnFailureListener{
            Log.i(TAG, it.toString())
        }
    }

    private fun updateProd(prod: MutableMap<String, Any>){
        cr.document("mouse").update(prod)
    }

    private fun deleteProd(){
        cr.document("mouse").delete().addOnSuccessListener {
            Log.i(TAG, "Produto deletado com sucesso")
        }.addOnFailureListener{
            Log.i(TAG, it.toString())
        }
    }

    fun readProds(){

            cr.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

}