package com.example.kotlinfinal.Model

class Model {

    val trainings: MutableList<Training> = ArrayList()
    companion object {
        val instance: Model = Model()
    }


}