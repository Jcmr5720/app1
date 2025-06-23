package com.example.holamundo.controller

import com.example.holamundo.model.MainModel

/**
 * Controlador simple que gestiona el contenido del modelo.
 */
class MainController(private val model: MainModel) {

    /** Obtiene el mensaje almacenado en el modelo. */
    fun getMessage(): String = model.message

    /** Establece un nuevo mensaje en el modelo. */
    fun setMessage(text: String) {
        model.message = text
    }
}
