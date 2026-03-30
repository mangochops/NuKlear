package com.example.nuklear

data class Station(
    val id: Int,
    val name: String,
    val location: String,
    val status: String,
    val capacity: String
)

val sampleStations = listOf(
    Station(1, "Zaporizhzhia", "Ukraine", "Active", "5,700 MW"),
    Station(2, "Kashiwazaki-Kariwa", "Japan", "Suspended", "7,965 MW"),
    Station(3, "Bruce", "Canada", "Active", "6,430 MW"),
    Station(4, "Hanul", "South Korea", "Active", "5,928 MW"),
    Station(5, "Fuqing", "China", "Active", "6,678 MW"),
    Station(6, "Gravelines", "France", "Active", "5,460 MW"),
    Station(7, "Paluel", "France", "Active", "5,320 MW"),
    Station(8, "Cattenom", "France", "Active", "5,200 MW")
)