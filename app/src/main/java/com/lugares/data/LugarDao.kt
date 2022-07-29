package com.lugares.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.lugares.model.Lugar

class LugarDao {
        private val coleccion1 = "lugaresApp"
        private val usuario = Firebase.auth.currentUser?.email.toString()
        private val coleccion2 = "misLugares"
        private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        init {
                firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        }

        //@Insert(onConflict = OnConflictStrategy.IGNORE)
        /*suspend fun addLugar(lugar: Lugar)

        //@Update(onConflict = OnConflictStrategy.IGNORE)
        suspend fun updateLugar(lugar: Lugar)

        //@Delete
        suspend fun deleteLugar(lugar: Lugar)*/

        //@Query ("SELECT * FROM LUGAR")

        fun getAllData() : MutableLiveData<List<Lugar>> {
                val listaLugares = MutableLiveData<List<Lugar>>()

                firestore.collection(coleccion1).document(usuario).collection(coleccion2).addSnapshotListener { instantanea, e ->
                        if (e != null) { // Se valida si se generó algún error en la captura de los documentos
                                return@addSnapshotListener
                        }
                        if (instantanea != null) { // Si hay información recuperada...
                                // Recorro la instantanea (documentos) para crear la lista de lugares
                                val lista = ArrayList<Lugar>()
                                instantanea.documents.forEach {
                                        val lugar = it.toObject(Lugar::class.java)
                                        if (lugar != null) {
                                                lista.add(lugar)
                                        }
                                }
                                listaLugares.value=lista
                        }
                }
                return listaLugares
        }

        fun saveLugar(lugar: Lugar) {
                val documento: DocumentReference
                if (lugar.id.isEmpty()) { // Si ID no tiene valor, entonces es un documento nuevo
                        documento = firestore.collection(coleccion1).document(usuario).collection(coleccion2).document()
                        lugar.id = documento.id
                } else { // Si ID tiene valor, entonces el documento existe y recupero la info del mismo
                        documento = firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(lugar.id)
                }
                documento.set(lugar)
                        .addOnSuccessListener { Log.d("saveLugar","Se creó o modificó un lugar") }
                        .addOnCanceledListener { Log.e("saveLugar","No se creó o modificó un lugar") }
        }

        fun deleteLugar(lugar: Lugar) {
                if (lugar.id.isNotEmpty()) { // Si ID tiene valor, entonces podemos eliminar el lugar porque existe
                        firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(lugar.id).delete()
                                .addOnSuccessListener { Log.d("deleteLugar","Se eliminó un lugar") }
                                .addOnCanceledListener { Log.e("deleteLugar","No se eliminó un lugar") }
                }
        }
}
