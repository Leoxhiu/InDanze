package com.example.indanze.data

data class Event(var userID : String ?= null
                 , var image : String ?= null
                 , var name: String?= null
                 , var date : String ?= null
                 , var time : String ?= null
                 , var description: String ?= null
                 , var location : String ?= null
                 , var latitude : String ?= null
                 , var longitude : String ?= null
)