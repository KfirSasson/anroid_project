package com.example.kotlinfinal.Model

class Model {

    val trainings: MutableList<Training> = ArrayList()
    companion object {
        val instance: Model = Model()
    }

    init {
        for (i in 0..20){
            val training = Training("$i",
                "$i",
                "avatar.png",
                userId = "$i")
            trainings.add(training)
        }
    }
}